package controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import org.apache.commons.lang3.ArrayUtils;
import sample.Structs.*;
import sample.WebService.WebServiceConnection;
import tornadofx.control.DateTimePicker;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class EditGameController {
    private OutdoorGame game;
    private OutdoorGamePath[] gamePoints;
    private OutdoorGameHints[] gameHints;
    private MasterWindowController masterWindowController;

    @FXML
    private TextField nameTextField;
    @FXML
    private DateTimePicker startDatePicker;
    @FXML
    private DateTimePicker endDatePicker;
    @FXML
    private ImageView gameImage;
    @FXML
    private Label noImageLabel;
    @FXML
    private ListView pointsListView;
    @FXML
    private VBox pointDetailsVBox;
    @FXML
    private TextField questionTextField;
    @FXML
    private TextField answerTextFeild;
    @FXML
    private Button deleteHint;
    @FXML
    private Button selectHint;
    @FXML
    private TextField hintTextField;
    @FXML
    private Button upButton;
    @FXML
    private Button downButton;
    @FXML
    private Button deleteButton;

    private List<Integer> EditedPoints = new ArrayList<Integer>();
    private List<Integer> DeletedPoints = new ArrayList<Integer>();
    private List<Integer> HintsAdded = new ArrayList<Integer>();
    private List<Integer> HintsEdited = new ArrayList<Integer>();
    private List<Integer> HintsDeleted = new ArrayList<Integer>();

    private boolean blocked = false;

    @FXML
    public void initialize()
    {
        pointsListView.getSelectionModel().selectedIndexProperty().addListener((observableValue, number, t1) -> {
            if(pointsListView.getSelectionModel().getSelectedIndex() >= 0)
                pointDetailsVBox.setVisible(true);
            else
                pointDetailsVBox.setVisible(false);
            blocked = true;
            pointSelectionChanged(pointsListView.getSelectionModel().getSelectedIndex());
            blocked = false;
        });

        questionTextField.textProperty().addListener((observableValue, s, t1) -> {
            if(blocked)
                return;
            OutdoorGamePath currentPoint = getOutdoorGamePointIndex(pointsListView.getSelectionModel().getSelectedIndex());
            if(currentPoint == null)
                return;
            if(currentPoint.getQuestion() == questionTextField.getText())
                return;
            if(!EditedPoints.contains(currentPoint.getIdQuestionPoint()))
                EditedPoints.add(currentPoint.getIdQuestionPoint());
            currentPoint.setQuestion(questionTextField.getText());
        });
        answerTextFeild.textProperty().addListener((observableValue, s, t1) -> {
            if(blocked)
                return;
            OutdoorGamePath currentPoint = getOutdoorGamePointIndex(pointsListView.getSelectionModel().getSelectedIndex());
            if(currentPoint == null)
                return;
            if(currentPoint.getAnswer() == answerTextFeild.getText())
                return;
            if(!EditedPoints.contains(currentPoint.getIdQuestionPoint()))
                EditedPoints.add(currentPoint.getIdQuestionPoint());
            currentPoint.setAnswer(answerTextFeild.getText());
        });
        hintTextField.textProperty().addListener((observableValue, s, t1) -> {
            if(blocked)
                return;
            OutdoorGameHints currentHint = getOutdoorGameHintIndex(pointsListView.getSelectionModel().getSelectedIndex());
            if(currentHint == null)
                return;
            if(currentHint.getHint() == hintTextField.getText())
                return;
            if(!HintsEdited.contains(currentHint.getIdHints()))
                HintsEdited.add(currentHint.getIdHints());
            currentHint.setHint(hintTextField.getText());
        });
    }
    public void closeWindowEvent(WindowEvent windowEvent) {
        for (Integer idAddedHint :
                HintsAdded) {
            OutdoorGameHints outdoorGameHints = getOutdoorGameHint(idAddedHint);
            WebServiceConnection.GetInstance().DeleteOutdoorGameHint(outdoorGameHints.getIdHints());
        }
        HintsAdded.clear();
    }



    public void setMasterWindowController(MasterWindowController masterWindowController) {
        this.masterWindowController = masterWindowController;
    }

    public void setGame(OutdoorGame game) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            this.game = objectMapper.readValue(objectMapper.writeValueAsString(game), OutdoorGame.class);
        }
        catch (Exception e)
        {

        }

        refreshGUI();
    }

    private void refreshGUI() {
        nameTextField.setText(game.getNameGame());
        startDatePicker.setValue(LocalDateTime.ofInstant(Instant.ofEpochMilli(game.getStartDateGame().getTime()), ZoneId.systemDefault()).toLocalDate());
        endDatePicker.setValue(LocalDateTime.ofInstant(Instant.ofEpochMilli(game.getEndDateGame().getTime()), ZoneId.systemDefault()).toLocalDate());
        if(game.getImageGame() == null)
        {
            gameImage.setVisible(false);
            noImageLabel.setVisible(true);
        }
        else
        {
            SetImage();
        }
    }

    private void SetImage() {
        try {
            ByteArrayInputStream bis = new ByteArrayInputStream(game.getImageGame());
            BufferedImage buildingImage = null;
            buildingImage = ImageIO.read(bis);
            Image image = SwingFXUtils.toFXImage(buildingImage, null);
            gameImage.setImage(image);
            gameImage.setFitWidth(200);
            gameImage.setFitHeight(200);
            gameImage.setPreserveRatio(true);
            gameImage.setVisible(true);
            noImageLabel.setVisible(false);
        }
        catch (Exception e)
        {

        }
    }

    final FileChooser fileChooser = new FileChooser();
    private String imagePath = "";
    public void pickImage(ActionEvent actionEvent) {
        File file = fileChooser.showOpenDialog(null);
        if (file != null) {
            imagePath = file.getAbsolutePath();
            try {
                gameImage.setImage(new Image("file:///" + file.getAbsolutePath()));
                SetImage();
                noImageLabel.setVisible(false);
                gameImage.setVisible(true);
            }
            catch (Exception e)
            {
                e.toString();
            }
        }
    }

    public void saveGame(ActionEvent actionEvent) {
        game.setNameGame(nameTextField.getText());
        game.setStartDateGame(Date.from(startDatePicker.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant()));
        game.setEndDateGame(Date.from(endDatePicker.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant()));
        try {
            if (imagePath != "")
                game.setImageGame(Files.readAllBytes(Paths.get(imagePath)));
        }
        catch (Exception e)
        {

        }

        for (Integer idEditedPoint :
                EditedPoints) {
            OutdoorGamePath outdoorGamePoint = getOutdoorGamePoint(idEditedPoint);
            WebServiceConnection.GetInstance().EditOutdoorGamePoint(outdoorGamePoint);
            masterWindowController.outdoorGamePointEdited(outdoorGamePoint);
        }
        EditedPoints.clear();

        for (Integer idDeletedPoints :
                DeletedPoints) {
            OutdoorGamePath outdoorGamePoint = getOutdoorGamePoint(idDeletedPoints);
            WebServiceConnection.GetInstance().DeleteOutdoorGamePoint(outdoorGamePoint.getIdQuestionPoint());
            masterWindowController.outdoorGamePointDeleted(outdoorGamePoint);
        }
        DeletedPoints.clear();
        for (Integer idAddedHint :
                HintsAdded) {
            OutdoorGameHints outdoorGameHints = getOutdoorGameHint(idAddedHint);
            masterWindowController.hintAdded(outdoorGameHints);
        }
        HintsAdded.clear();
        for (Integer idEditedHint :
                HintsEdited) {
            OutdoorGameHints outdoorGameHints = getOutdoorGameHint(idEditedHint);
            WebServiceConnection.GetInstance().EditOutdoorGameHint(outdoorGameHints);
            masterWindowController.hintEdited(outdoorGameHints);
        }
        HintsEdited.clear();
        for (Integer idEditedHint :
                HintsDeleted) {
            OutdoorGameHints outdoorGameHints = getOutdoorGameHint(idEditedHint);
            WebServiceConnection.GetInstance().DeleteOutdoorGameHint(outdoorGameHints.getIdHints());
            masterWindowController.hintDeleted(outdoorGameHints);
        }
        HintsDeleted.clear();

        WebServiceConnection.GetInstance().EditOutdoorGame(game, imagePath);
        masterWindowController.outdoorGameEdited(game);

        Stage stage = (Stage) nameTextField.getScene().getWindow();
        stage.close();
    }

    public void deleteGame(ActionEvent actionEvent) {
        if(WebServiceConnection.GetInstance().DeleteOutdoorGame(game.getIdOutdoorGame()))
            masterWindowController.OutdoorGameDeleted(game.getIdOutdoorGame());

        Stage stage = (Stage) nameTextField.getScene().getWindow();
        stage.close();
    }

    public void setGamePoints(OutdoorGamePath[] outdoorGamePoints, OutdoorGameHints[] outdoorGameHints) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            this.gamePoints = objectMapper.readValue(objectMapper.writeValueAsString(outdoorGamePoints), OutdoorGamePath[].class);
            this.gameHints = objectMapper.readValue(objectMapper.writeValueAsString(outdoorGameHints), OutdoorGameHints[].class);
        }
        catch (Exception e)
        {

        }

        pointsListView.getItems().clear();
        Integer idGamePoint = game.getIdFirstPoint();
        int number = 1;
        while(idGamePoint != null)
        {
            pointsListView.getItems().add(String.valueOf(number++));
            idGamePoint = getOutdoorGamePoint(idGamePoint).getIdNextPoint();
        }
    }

    public OutdoorGamePath getOutdoorGamePoint(int id)
    {
        for (OutdoorGamePath point :
                gamePoints) {
            if (point.getIdQuestionPoint()  != id)
                continue;
            return point;
        }
        return null;
    }
    public OutdoorGameHints getOutdoorGameHint(int id)
    {
        for (OutdoorGameHints hint :
                gameHints) {
            if (hint.getIdHints()  != id)
                continue;
            return hint;
        }
        return null;
    }

    public OutdoorGamePath getOutdoorGamePointIndex(int index)
    {
        Integer idGamePoint = game.getIdFirstPoint();
        int number = 0;
        if(number == index)
            return getOutdoorGamePoint(idGamePoint);

        while(idGamePoint != null)
        {
            number++;
            idGamePoint = getOutdoorGamePoint(idGamePoint).getIdNextPoint();
            if(number == index)
                return getOutdoorGamePoint(idGamePoint);
        }
        return null;
    }
    public OutdoorGameHints getOutdoorGameHintIndex(int index)
    {
        OutdoorGamePath gamePoint = getOutdoorGamePointIndex(index);
        if(gamePoint == null)
            return null;
        if(gamePoint.getIdHintPoint() == null)
            return null;
        for (OutdoorGameHints hint :
                gameHints) {
            if (hint.getIdHints() == gamePoint.getIdHintPoint())
                return hint;
        }
        return null;
    }

    public void selectHint(ActionEvent actionEvent) {
        masterWindowController.selectHintPoint(this);
    }

    private void pointSelectionChanged(int selectedIndex)
    {
        OutdoorGamePath currentPoint = getOutdoorGamePointIndex(selectedIndex);
        questionTextField.setText(currentPoint.getQuestion());
        answerTextFeild.setText(currentPoint.getAnswer());
        if(currentPoint.getIdHintPoint() == null)
        {
            selectHint.setVisible(true);
            deleteHint.setVisible(false);
            hintTextField.setVisible(false);
        }
        else
        {
            selectHint.setVisible(false);
            deleteHint.setVisible(true);
            hintTextField.setVisible(true);
            hintTextField.setText(getOutdoorGameHint(currentPoint.getIdHintPoint()).getHint());
        }

        if(selectedIndex == 0)
            upButton.setDisable(true);
        else
            upButton.setDisable(false);

        if(selectedIndex == pointsListView.getItems().size() - 1)
            downButton.setDisable(true);
        else
            downButton.setDisable(false);
    }

    public void upClicked(ActionEvent actionEvent) {
        int index = pointsListView.getSelectionModel().getSelectedIndex();
        if(index == 0)
            return;
        Integer previousId = getOutdoorGamePointIndex(index - 1).getIdQuestionPoint();
        Integer currentId = getOutdoorGamePointIndex(index).getIdQuestionPoint();
        Integer nextId = getOutdoorGamePoint(currentId).getIdNextPoint();
        if(index == 1)
        {
            game.setIdFirstPoint(currentId);
        }
        else
        {
            int twoBeforeId = getOutdoorGamePointIndex(index - 2).getIdQuestionPoint();
            getOutdoorGamePoint(twoBeforeId).setIdNextPoint(currentId);
            if(!EditedPoints.contains(twoBeforeId))
                EditedPoints.add(twoBeforeId);
        }
        getOutdoorGamePoint(currentId).setIdNextPoint(previousId);
        if(!EditedPoints.contains(currentId))
            EditedPoints.add(currentId);
        getOutdoorGamePoint(previousId).setIdNextPoint(nextId);
        if(!EditedPoints.contains(previousId))
            EditedPoints.add(previousId);
        pointsListView.getSelectionModel().select(index -  1);
    }

    public void downClicked(ActionEvent actionEvent) {
        int index = pointsListView.getSelectionModel().getSelectedIndex();
        pointsListView.getSelectionModel().select(index +  1);
        upClicked(actionEvent);
        pointsListView.getSelectionModel().select(index +  1);

    }

    public void deleteClicked(ActionEvent actionEvent) {
        int index = pointsListView.getSelectionModel().getSelectedIndex();
        OutdoorGamePath gamePoint = getOutdoorGamePointIndex(index);
        if(!DeletedPoints.contains(gamePoint.getIdQuestionPoint()))
            DeletedPoints.add(gamePoint.getIdQuestionPoint());

        if(gamePoint.getIdHintPoint() != null)
        {
            if(!HintsDeleted.contains(gamePoint.getIdHintPoint()))
                HintsDeleted.add(gamePoint.getIdHintPoint());
        }

        pointsListView.getItems().remove(pointsListView.getSelectionModel().getSelectedIndex());

        Integer currentId = getOutdoorGamePointIndex(index).getIdQuestionPoint();
        Integer nextId = getOutdoorGamePoint(currentId).getIdNextPoint();
        if(index == 0)
        {
            game.setIdFirstPoint(nextId);
        }
        else
        {
            Integer previousId = getOutdoorGamePointIndex(index - 1).getIdQuestionPoint();
            getOutdoorGamePoint(previousId).setIdNextPoint(nextId);
            if(!EditedPoints.contains(previousId))
                EditedPoints.add(previousId);
        }
    }

    public void hintSelected(Point point) {
        Stage stage = (Stage) nameTextField.getScene().getWindow();
        stage.setAlwaysOnTop(true);
        stage.setAlwaysOnTop(false);

        int index = pointsListView.getSelectionModel().getSelectedIndex();
        if(index < 0)
            return;
        OutdoorGamePath gamePoint = getOutdoorGamePointIndex(index);

        OutdoorGameHints hint = new OutdoorGameHints();
        hint.setIdPoint(point.getIdPoint());
        hint.setIdOutdoorGame(gamePoint.getIdOutdoorGame());
        hint = WebServiceConnection.GetInstance().AddOutdoorGameHint(hint);
        if(hint == null)
            return;
        gameHints = ArrayUtils.add(gameHints, hint);
        if(!HintsAdded.contains(hint.getIdHints()))
            HintsAdded.add(hint.getIdHints());

        stage.setOnCloseRequest(windowEvent -> {
            closeWindowEvent(windowEvent);
        });

        gamePoint.setIdHintPoint(hint.getIdHints());
        if(!EditedPoints.contains(gamePoint.getIdQuestionPoint()))
            EditedPoints.add(gamePoint.getIdQuestionPoint());
        pointSelectionChanged(index);
    }

    public void deleteHint(ActionEvent actionEvent) {
        int index = pointsListView.getSelectionModel().getSelectedIndex();
        if(index < 0)
            return;
        OutdoorGamePath gamePoint = getOutdoorGamePointIndex(index);
        OutdoorGameHints hint = getOutdoorGameHintIndex(index);

        gamePoint.setIdHintPoint(null);
        WebServiceConnection.GetInstance().DeleteOutdoorGameHint(hint.getIdHints());
        pointSelectionChanged(index);
    }
}
