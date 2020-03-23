package controllers;

import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.ArrayUtils;
import sample.Structs.Group;
import sample.WebService.WebServiceConnection;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;

public class GroupsWindowController {
    @FXML
    private ListView listView;
    @FXML
    private TextField nameTextField;
    @FXML
    private Label noImageLabel;
    @FXML
    private ImageView imageView;
    @FXML
    private TextField imagePath;
    @FXML
    private Button browseButton;


    private Group[] groups;
    private int idBuilding;
    private MasterWindowController masterWindowController;

    public void setIdBuilding(int idBuilding) {
        this.idBuilding = idBuilding;
    }

    public void setGroups(Group[] groups) {
        this.groups = groups;
        RefreshList();
    }

    private void RefreshList()
    {
        listView.getItems().clear();
        for (Group group : groups)
        {
            listView.getItems().add(group.getNameGroup());
        }
        if(listView.getItems().size() > 0)
            listView.getSelectionModel().select(0);
    }

    @FXML
    public void initialize () {
        listView.getSelectionModel().selectedIndexProperty().addListener((observableValue, o, t1) ->
        {
            nameTextField.setDisable(false);
            browseButton.setDisable(false);
            RefreshParameters(listView.getSelectionModel().selectedIndexProperty().get());
        });
    }

    private void RefreshParameters(int num) {
        if(num < 0)
            return;
        if(num >= groups.length)
            return;

        nameTextField.setText(groups[num].getNameGroup());
        imagePath.setVisible(false);
        try {
            ByteArrayInputStream bis = new ByteArrayInputStream(groups[num].getImageGroup());
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
    }

    @FXML
    public void addGroup() {
        Group group = new Group();
        group.setNameGroup("New group");
        group = WebServiceConnection.GetInstance().AddGroup(group, "", idBuilding);
        if(group != null)
        {
            masterWindowController.groupAdded(group);
            groups = ArrayUtils.add(groups, group);
            listView.getItems().add(group.getNameGroup());
        }

    }

    public void setMasterWindowController(MasterWindowController masterWindowController) {
        this.masterWindowController = masterWindowController;
    }

    final FileChooser fileChooser = new FileChooser();

    public void Browse(ActionEvent actionEvent) {
        if(listView.getSelectionModel().getSelectedIndex() < 0)
            return;
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

    public void Save(ActionEvent actionEvent)
    {
        int num = listView.getSelectionModel().selectedIndexProperty().get();
        if(num < 0)
            return;
        if(num >= groups.length)
            return;
        try {
            Group group = groups[num];
            group.setNameGroup(nameTextField.getText());
            if(imagePath.isVisible())
                group.setImageGroup(IOUtils.toByteArray(new FileInputStream(imagePath.getText())));
            if(WebServiceConnection.GetInstance().EditGroup(group, imagePath.getText(), masterWindowController.GetCurrentBuildingId()))
            {
                masterWindowController.GroupEditted(group);
                RefreshList();
                listView.getSelectionModel().select(num);
            }
        }
        catch (Exception e)
        {

        }

    }

    public void Delete(ActionEvent actionEvent) {
        int num = listView.getSelectionModel().selectedIndexProperty().get();
        if(num < 0)
            return;
        if(num >= groups.length)
            return;
        if(WebServiceConnection.GetInstance().RemoveGroup(groups[num]))
        {
            masterWindowController.GroupRemoved(groups[num].getIdGroup());
            groups = ArrayUtils.remove(groups, num);
            RefreshList();
        }
    }
}
