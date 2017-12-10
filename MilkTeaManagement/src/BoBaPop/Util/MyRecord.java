/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package BoBaPop.Util;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

  public class MyRecord {

        private final StringProperty name;
        private final DoubleProperty value;

        public MyRecord() {
            this.name = new SimpleStringProperty();
            this.value = new SimpleDoubleProperty();
        }

        public MyRecord(String name, double value) {
            this.name = new SimpleStringProperty(name);
            this.value = new SimpleDoubleProperty(value);
        }

        public String getName() {
            return name.get();
        }

        public Double getValue() {
            return value.get();
        }

        public void setName(String name) {
            this.name.set(name);
        }

        public void setValue(double value) {
            this.value.set(value);
        }

        public StringProperty nameProperty() {
            return name;
        }

        public DoubleProperty valueProperty() {
            return value;
        }

        @Override
        public String toString() {
            return name + ":" + value;
        }

    }
