package sample;

import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.DoubleBinding;
import javafx.beans.binding.NumberBinding;
import javafx.beans.property.*;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

public class Task {
    private final StringProperty priority = new SimpleStringProperty();
    private final StringProperty description = new SimpleStringProperty();
    private final ObjectProperty<Integer> progress = new SimpleObjectProperty<>(0);
    private final ObjectProperty<Integer> id = new SimpleObjectProperty<>(null);

    public Task() {

    }

    public Task(Integer id, String priority, String description, Integer progress) {
        this.id.set(id);
        this.priority.set(priority);
        this.description.set(description);
        this.progress.set(progress);
    }

    public Integer getId() {
        return id.get();
    }

    public ObjectProperty<Integer> idProperty() {
        return id;
    }

    public void setId(Integer id) {
        this.id.set(id);
    }

    public String getPriority() {
        return priority.get();
    }

    public StringProperty priorityProperty() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority.set(priority);
    }

    public String getDescription() {
        return description.get();
    }

    public StringProperty descriptionProperty() {
        return description;
    }

    public void setDescription(String description) {
        this.description.set(description);
    }

    public Integer getProgress() {
        return progress.get();
    }

    public ObjectProperty<Integer> progressProperty() {
        return progress;
    }

    public void setProgress(Integer progress) {
        this.progress.set(progress);
    }
}
