package sample;

import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.DoubleBinding;
import javafx.beans.binding.NumberBinding;
import javafx.beans.property.*;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

public class Examples {
    public static void main(String[] args) {

        //Property examples
        SimpleIntegerProperty intProp = new SimpleIntegerProperty();

        intProp.set(15);
        System.out.println(intProp.get());

        SimpleStringProperty stringProp = new SimpleStringProperty("InitialValue");
        System.out.println(stringProp.get());
        stringProp.set("New Value");
        System.out.println(stringProp.get());

        ReadOnlyBooleanWrapper readOnlyBooleanWrapper = new ReadOnlyBooleanWrapper(true);
        ReadOnlyBooleanProperty readOnlyBoolProp = readOnlyBooleanWrapper.getReadOnlyProperty();

        System.out.println(readOnlyBoolProp.get());
        readOnlyBooleanWrapper.set(false);
        System.out.println(readOnlyBoolProp.get());

        intProp.addListener((ObservableValue<? extends Number> observableValue, Number oldValue, Number newValue) -> {
            System.out.println("Listener 1: Integer Property is changed to " + newValue);
        });

        intProp.addListener((ObservableValue<? extends Number> observableValue, Number oldValue, Number newValue) -> {
            System.out.println("Listener 2: Integer Property is changed to " + newValue);
        });

        intProp.addListener(observable -> System.out.println("Int property changed"));

        intProp.set(90);
        intProp.set(100);

        //Binding examples
        StringProperty lastNameProp = new SimpleStringProperty();
        StringProperty surNameProp = new SimpleStringProperty();
        StringProperty firstNameProp = new SimpleStringProperty();

        surNameProp.bindBidirectional(lastNameProp);
        lastNameProp.set("Clarke");
        firstNameProp.set("Michael");
        System.out.println(firstNameProp.get());
        System.out.println(lastNameProp.get());
        System.out.println(surNameProp.get());

        StringProperty fullNameProp = new SimpleStringProperty();
        fullNameProp.bind(Bindings.concat(firstNameProp, " ", lastNameProp));
        System.out.println(fullNameProp.get());

        IntegerProperty length = new SimpleIntegerProperty(18);
        IntegerProperty width = new SimpleIntegerProperty(13);

        IntegerProperty area = new SimpleIntegerProperty();
        area.bind(length.multiply(width));

        NumberBinding perimeter = length.add(width).multiply(2);

        System.out.println(area.get());
        System.out.println(perimeter.getValue());

        //low level binding example
        DoubleProperty radius = new SimpleDoubleProperty(1.5);
        DoubleBinding volumeBinding = new DoubleBinding() {
            {
                super.bind(radius);
            }
            @Override
            protected double computeValue() {
                return 4 / 3 * Math.PI*Math.pow(radius.get(), 3);
            }
        };
        System.out.println(volumeBinding.get());
        radius.set(2.5);
        System.out.println(volumeBinding.get());

    }
}
