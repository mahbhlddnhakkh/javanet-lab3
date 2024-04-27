module labs.lab2_game {
  requires javafx.controls;
  requires javafx.fxml;
  requires javafx.graphics;
  requires javafx.base;
  requires org.hibernate.commons.annotations;
  requires org.hibernate.orm.core;
  requires jakarta.persistence;

  opens labs.lab3_game to javafx.fxml;

  exports labs.lab3_game;
}
