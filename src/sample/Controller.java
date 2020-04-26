package sample;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.StringBinding;
import javafx.beans.property.ReadOnlyIntegerProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;

import java.net.URL;
import java.sql.SQLOutput;
import java.util.HashMap;
import java.util.Optional;
import java.util.ResourceBundle;

import static javafx.beans.property.ReadOnlyIntegerProperty.readOnlyIntegerProperty;

public class Controller implements Initializable {

    private final Task currentTask = new Task();
    private final ObservableList<Task> tasks = FXCollections.observableArrayList();
    private final HashMap<Integer, Task> tasksMap = new HashMap<>();

    public HashMap<Integer, Task> getTasksMap() {
        return tasksMap;
    }

    @FXML
    private ProgressBar progressBar;

    @FXML
    private TableView<Task> tasksTable;

    @FXML
    private TableColumn<Task, String> priorityColumn;

    @FXML
    private TableColumn<Task, String> descriptionColumn;

    @FXML
    private TableColumn<Task, String> progressColumn;

    @FXML
    private ComboBox<String> priorities;

    @FXML
    private TextField taskDescription;

    @FXML
    private Button add;

    @FXML
    private Spinner<Integer> progressSpinner;

    @FXML
    private CheckBox completedCheckBox;

    @FXML
    private Button cancel;

    int lastId = 0;

    @FXML
    private void addButtonClicked(ActionEvent event) {
        if(currentTask.getId() == null) {
            Task t = new Task(++lastId, currentTask.getPriority(), currentTask.getDescription(), currentTask.getProgress());
            tasks.add(t);
            tasksMap.put(lastId, t);
        } else {
            Task t = tasksMap.get(currentTask.getId());
            t.setDescription(currentTask.getDescription());
            t.setPriority(currentTask.getPriority());
            t.setProgress(currentTask.getProgress());
        }
        setCurrentTask(null);
    }

    @FXML
    private void cancelButtonClicked(ActionEvent event) {

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setHeaderText("Are you sure you want to cancel?");
        alert.setTitle("Canceling Update");
        alert.getButtonTypes().remove(0, 2);
        alert.getButtonTypes().add(0, ButtonType.YES);
        alert.getButtonTypes().add(1, ButtonType.NO);
        Optional<ButtonType> confirmationResponse = alert.showAndWait();
        if(confirmationResponse.get() == ButtonType.YES) {
            setCurrentTask(null);
            tasksTable.getSelectionModel().clearSelection();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        priorities.getItems().addAll("High", "Medium", "Low");
        progressSpinner.setValueFactory(
                new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 100, 0));

        progressSpinner.valueProperty().addListener(new ChangeListener<Integer>() {
            @Override
            public void changed(ObservableValue<? extends Integer> observableValue, Integer oldValue, Integer newValue) {
                if(newValue.intValue() == 100) {
                    completedCheckBox.setSelected(true);
                } else {
                    completedCheckBox.setSelected(false);
                }
            }
        });

        ReadOnlyIntegerProperty intProgress = readOnlyIntegerProperty(progressSpinner.valueProperty());
        progressBar.progressProperty().bind(intProgress.divide(100.0));

        priorities.valueProperty().bindBidirectional(currentTask.priorityProperty());
        taskDescription.textProperty().bindBidirectional(currentTask.descriptionProperty());
        progressSpinner.getValueFactory().valueProperty().bindBidirectional(currentTask.progressProperty());

        tasksTable.setItems(tasks);
        priorityColumn.setCellValueFactory(rowData -> rowData.getValue().priorityProperty());
        descriptionColumn.setCellValueFactory(rowData -> rowData.getValue().descriptionProperty());
        progressColumn.setCellValueFactory(rowData -> Bindings.concat(rowData.getValue().progressProperty(), "%"));

//        tasks.addAll(new Task(1, "High", "Complete Design Document", 10),
//                new Task(2, "Medium", "Update Class Diagram", 0),
//                new Task(3, "Low", "Fix Bug 245232", 0));

        StringBinding addButtonTextBinding = new StringBinding() {
            {
                super.bind(currentTask.idProperty());
            }
            @Override
            protected String computeValue() {
                if(currentTask.getId() == null) {
                    return "Add";
                } else {
                    return "Update";
                }
            }
        };
        add.textProperty().bind(addButtonTextBinding);
        add.disableProperty().bind(Bindings.greaterThan(3, currentTask.descriptionProperty().length()));
        tasksTable.getSelectionModel().selectedItemProperty().addListener((ObservableValue<? extends Task> observable, Task oldValue, Task newValue) -> {
            setCurrentTask(newValue);
        });

    }

    private void setCurrentTask(Task selectedTask) {
        if(selectedTask != null) {
            currentTask.setId(selectedTask.getId());
            currentTask.setPriority(selectedTask.getPriority());
            currentTask.setDescription(selectedTask.getDescription());
            currentTask.setProgress(selectedTask.getProgress());
        } else {
            currentTask.setId(null);
            currentTask.setPriority("");
            currentTask.setDescription("");
            currentTask.setProgress(0);
        }
    }

    void setTasksMap(HashMap<Integer, Task> initialTasksMap) {
        tasksMap.clear();
        tasks.clear();
        tasksMap.putAll(initialTasksMap);
        tasks.addAll(initialTasksMap.values());
        lastId = tasksMap.keySet().stream().max(Integer::compare).get();
    }

}
