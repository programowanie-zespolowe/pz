package controllers;

import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.apache.commons.io.IOUtils;
import sample.QrCodeGeneration;
import sample.Structs.Group;
import sample.Structs.Point;
import sample.Structs.PointDetail;
import sample.WebService.WebServiceConnection;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;


public class PointDetailsController {
    private Point point;
    private List<PointDetail> pointDetails;
    private MasterWindowController masterWindowController;
    private Group[] groups;

    @FXML
    private ListView listView;
    @FXML
    private TextField nameTextField;
    @FXML
    private ComboBox groupComboBox;
    @FXML
    private Label noImageLabel;
    @FXML
    private ImageView imageView;
    @FXML
    private TextField imagePath;
    @FXML
    private ImageView qrCodeImageView;

    @FXML
    public void initialize()
    {
        listView.getSelectionModel().selectedIndexProperty().addListener((observableValue, o, t1) ->
        {
            RefreshParameters(listView.getSelectionModel().selectedIndexProperty().get());
        });
    }

    private void RefreshParameters(int num)
    {
        if(num < 0)
            return;
        if(num >= pointDetails.size())
            return;

        nameTextField.setText(pointDetails.get(num).getNamePoint());
        imagePath.setVisible(false);
        try {
            ByteArrayInputStream bis = new ByteArrayInputStream(pointDetails.get(num).getImagePoint());
            BufferedImage bufferedImage = ImageIO.read(bis);
            Image image = SwingFXUtils.toFXImage(bufferedImage, null);
            imageView.setImage(image);
            imageView.setFitWidth(100);
            imageView.setFitHeight(100);
            imageView.setPreserveRatio(true);
            imageView.setVisible(true);
            noImageLabel.setVisible(false);
        }
        catch (Exception e)
        {
            noImageLabel.setVisible(true);
            imageView.setVisible(false);
        }
        for(int i = 0; i < groups.length; i++)
        {
            if(pointDetails.get(num).IdGroup == groups[i].getIdGroup())
            {
                groupComboBox.getSelectionModel().select(i);
                break;
            }
        }
    }

    final FileChooser fileChooser = new FileChooser();
    public void Browse(ActionEvent actionEvent) {
        File file = fileChooser.showOpenDialog(null);
        if (file != null) {
            imagePath.setText(file.getAbsolutePath());
            imagePath.setVisible(true);
            try {
                BufferedImage img = ImageIO.read(new File(file.getAbsolutePath()));
                Image image = SwingFXUtils.toFXImage(img, null);
                imageView.setImage(image);
                imageView.setFitWidth(100);
                imageView.setFitHeight(100);
                imageView.setPreserveRatio(true);
                imageView.setVisible(true);

                noImageLabel.setVisible(false);
            }
            catch (Exception e)
            {

            }
        }
    }

    public void setPoint(Point point)
    {
        this.point = point;
        SetQRCodeImage(point);
    }

    private void SetQRCodeImage(Point point) {
        if(point != null && masterWindowController != null)
        {
            try {
                qrCodeImageView.setImage(QrCodeGeneration.GetQRCodeImage(MessageFormat.format("BUILDING:{0}:{1}", masterWindowController.GetCurrentBuildingId(), point.getIdPoint())));
                qrCodeImageView.setFitWidth(125);
                qrCodeImageView.setFitHeight(125);
                qrCodeImageView.setPreserveRatio(true);
            }
            catch (Exception e)
            {
                System.out.println(e.getStackTrace());
            }
        }
    }

    public void setPointDetails(List<PointDetail> pointDetailList)
    {
        this.pointDetails = pointDetailList;
        if(pointDetails == null)
            pointDetails = new ArrayList<>();
        RefreshListView();
    }

    private void RefreshListView() {
        listView.getItems().clear();
        for(PointDetail pointDetail : pointDetails)
        {
            listView.getItems().add(pointDetail.getNamePoint());
        }
        if(listView.getItems().size() > 0)
            listView.getSelectionModel().select(0);
    }

    public void setMasterWindowController(MasterWindowController masterWindowController) {
        this.masterWindowController = masterWindowController;
        SetQRCodeImage(point);
    }

    public void RemovePoint(ActionEvent actionEvent) {
        if(WebServiceConnection.GetInstance().RemovePoint(point))
        {
            masterWindowController.PointRemoved(point);

            Stage stage = (Stage) nameTextField.getScene().getWindow();
            stage.close();
        }

    }

    public void RemovePointDetail(ActionEvent actionEvent) {
        int num = listView.getSelectionModel().selectedIndexProperty().get();
        if(num < 0)
            return;
        if(num >= pointDetails.size())
            return;
        if(WebServiceConnection.GetInstance().RemovePointDetail(pointDetails.get(num)))
        {
            masterWindowController.PointDetailsRemoved(point, pointDetails.get(num));
            RefreshListView();
        }
    }

    public void setGroups(Group[] groups) {
        this.groups = groups;
        groupComboBox.getItems().clear();
        for(Group group : groups)
        {
            groupComboBox.getItems().add(group.getNameGroup());
        }
    }

    public void AddPointDetail(ActionEvent actionEvent) {
        PointDetail pointDetail = new PointDetail();
        if(groups.length > 0)
            pointDetail.setIdGroup(groups[0].getIdGroup());
        pointDetail.setIdPoint(point.getIdPoint());
        pointDetail.setNamePoint("New point detail");
        pointDetail = WebServiceConnection.GetInstance().AddPointDetail(pointDetail);
        if(pointDetail != null)
        {
            pointDetails.add(pointDetail);
            RefreshListView();
            masterWindowController.PointDetailsAdded(pointDetail);
        }
    }
    public void QrCodeToFile(ActionEvent actionEvent) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select place to write file");
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("PNG files (*.png)", "*.png");
        fileChooser.getExtensionFilters().add(extFilter);
        File file = fileChooser.showSaveDialog(null);
        if(file != null)
        {
            try {
                QrCodeGeneration.GeneratePNG(MessageFormat.format("BUILDING:{0}:{1}", masterWindowController.GetCurrentBuildingId(), point.getIdPoint()),
                        file.getAbsolutePath());
            }
            catch (Exception e)
            {

            }
        }
    }

    public void SavePointDetail(ActionEvent actionEvent)
    {
        int num = listView.getSelectionModel().selectedIndexProperty().get();
        if(num < 0)
            return;
        if(num >= pointDetails.size())
            return;
        try {
            PointDetail pointDetail = pointDetails.get(num);
            pointDetail.setNamePoint(nameTextField.getText());
            pointDetail.setIdGroup(groups[groupComboBox.getSelectionModel().getSelectedIndex()].getIdGroup());
            if(imagePath.isVisible())
                pointDetail.setImagePoint(IOUtils.toByteArray(new FileInputStream(imagePath.getText())));
            if(WebServiceConnection.GetInstance().EditPointDetails(pointDetail, imagePath.getText()))
            {
                masterWindowController.PointDetailsEditted(pointDetail, point);
                RefreshListView();
                listView.getSelectionModel().select(num);
            }
        }
        catch (Exception e)
        {

        }
    }
}
