package controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
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
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.Pair;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.ArrayUtils;
import sample.Structs.Group;
import sample.Structs.OutdoorGameHints;
import sample.Structs.OutdoorGamePath;
import sample.WebService.WebServiceConnection;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;

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
    @FXML
    private Button deleteButton;


    private Group[] groups;
    private int idBuilding;
    private MasterWindowController masterWindowController;
    private boolean block = false;

    private List<Integer> GroupsAdded = new ArrayList<Integer>();
    private List<Integer> GroupsEdited = new ArrayList<Integer>();
    private List<Integer> GroupsDeleted = new ArrayList<Integer>();

    private List<Pair<Integer, String>> EditedFilePath = new ArrayList<Pair<Integer, String>>();

    @FXML
    public void initialize () {
        listView.getSelectionModel().selectedIndexProperty().addListener((observableValue, o, t1) ->
        {
            nameTextField.setDisable(false);
            browseButton.setDisable(false);
            nameTextField.requestFocus();
            RefreshParameters(listView.getSelectionModel().selectedIndexProperty().get());
        });
        nameTextField.textProperty().addListener((observableValue, s, t1) -> {
            int index = listView.getSelectionModel().getSelectedIndex();
            if(index < 0)
                return;
            if(groups[index].getNameGroup() == nameTextField.getText())
                return;
            if(block)
                return;

            groups[index].setNameGroup(nameTextField.getText());
            listView.getItems().set(index, nameTextField.getText());
            if(!GroupsEdited.contains(groups[index].getIdGroup()))
                GroupsEdited.add(groups[index].getIdGroup());
        });
        init();
    }

    public void setIdBuilding(int idBuilding) {
        this.idBuilding = idBuilding;
    }

    public void setGroups(Group[] groups) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            this.groups = objectMapper.readValue(objectMapper.writeValueAsString(groups), Group[].class);
        }
        catch (Exception e)
        {

        }
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


    private void RefreshParameters(int num) {
        if(num < 0)
            return;
        if(num >= groups.length)
            return;

        block = true;
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
        finally {
            block = false;
        }
    }

    @FXML
    public void addGroup() {
        Group group = new Group();
        group.setNameGroup("Nowa grupa");
        group = WebServiceConnection.GetInstance().AddGroup(group, "", idBuilding);
        if(group != null)
        {
            groups = ArrayUtils.add(groups, group);
            listView.getItems().add(group.getNameGroup());
            if(!GroupsAdded.contains(group.getIdGroup()))
                GroupsAdded.add(group.getIdGroup());

            Stage stage = (Stage) nameTextField.getScene().getWindow();
            stage.setOnCloseRequest(windowEvent -> {
                closeWindowEvent(windowEvent);
            });
            listView.getSelectionModel().select(listView.getItems().size() - 1);
        }

    }

    public void setMasterWindowController(MasterWindowController masterWindowController) {
        this.masterWindowController = masterWindowController;
    }

    final FileChooser fileChooser = new FileChooser();

    private void addFilePath(String filePath)
    {
        int num = listView.getSelectionModel().getSelectedIndex();
        if(num < 0)
            return;
        int groupId = groups[num].getIdGroup();
        for(int i = 0; i < EditedFilePath.size(); i++)
        {
            if(EditedFilePath.get(i).getKey() == groupId)
            {
                EditedFilePath.remove(i);
                break;
            }
        }
        EditedFilePath.add(new Pair<>(groupId, filePath));
        if(!GroupsEdited.contains(groups[num].getIdGroup()))
            GroupsEdited.add(groups[num].getIdGroup());
    }

    public void Browse(ActionEvent actionEvent) {
        int num = listView.getSelectionModel().getSelectedIndex();
        if(num < 0)
            return;
        File file = fileChooser.showOpenDialog(null);
        if (file != null) {
            addFilePath(file.getAbsolutePath());
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

                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                ImageIO.write(img, "png", bos );
                groups[num].setImageGroup(bos.toByteArray());
            }
            catch (Exception e)
            {

            }
        }
    }

    public void Save(ActionEvent actionEvent)
    {
        int num = listView.getSelectionModel().getSelectedIndex();
        try {
            for (Integer idAddedGroup :
                GroupsAdded) {
                Group group = getGroup(idAddedGroup);
                masterWindowController.groupAdded(group);
            }
            GroupsAdded.clear();
            for (Integer idEditedGroup :
                    GroupsEdited) {
                Group group = getGroup(idEditedGroup);
                String path = findEditedPath(idEditedGroup);
                WebServiceConnection.GetInstance().EditGroup(group, path, masterWindowController.GetCurrentBuildingId());
                masterWindowController.GroupEditted(group);
            }
            GroupsEdited.clear();
            EditedFilePath.clear();
            for (Integer idDeletedGroup :
                    GroupsDeleted) {
                WebServiceConnection.GetInstance().RemoveGroup(idDeletedGroup);
                masterWindowController.GroupRemoved(idDeletedGroup);
            }
            GroupsDeleted.clear();

            Stage stage = (Stage) nameTextField.getScene().getWindow();
            stage.close();
        }
        catch (Exception e)
        {

        }

    }

    private String findEditedPath(Integer idEditedGroup) {
        for(int i = 0; i < EditedFilePath.size(); i++)
        {
            if(EditedFilePath.get(i).getKey() == idEditedGroup)
                return EditedFilePath.get(i).getValue();
        }
        return "";
    }

    public void Delete(ActionEvent actionEvent) {
        int num = listView.getSelectionModel().selectedIndexProperty().get();
        if(num < 0)
            return;
        if(num >= groups.length)
            return;
        if(!GroupsDeleted.contains(groups[num].getIdGroup()))
            GroupsDeleted.add(groups[num].getIdGroup());
        if(GroupsAdded.contains(groups[num].getIdGroup())) {
            for(int i = 0; i < GroupsAdded.size(); i++)
            {
                if(GroupsAdded.get(i) == groups[num].getIdGroup())
                    GroupsAdded.remove(i);
            }
            if(GroupsEdited.contains(groups[num].getIdGroup()))
            {
                for(int i = 0; i < GroupsEdited.size(); i++)
                {
                    if(GroupsEdited.get(i) == groups[num].getIdGroup())
                        GroupsEdited.remove(i);
                }
            }
        }
        groups = ArrayUtils.remove(groups, num);
        RefreshList();
    }
//bindowanie
    void init(){
        deleteButton.disableProperty().bind(listView.getSelectionModel().selectedItemProperty().isNull());
    }

    public Group getGroup(int groupId)
    {
        for (Group group :
                groups) {
            if (group.getIdGroup() == groupId)
                return group;
        }
        return null;
    }


    public void closeWindowEvent(WindowEvent windowEvent) {
        for (Integer idAddedGroup :
                GroupsAdded) {
            WebServiceConnection.GetInstance().RemoveGroup(idAddedGroup);
        }
        GroupsAdded.clear();
    }
}
