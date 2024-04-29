module labs.lab2_game {
  requires javafx.controls;
  requires javafx.fxml;
  requires javafx.graphics;
  requires javafx.base;
  //requires org.hibernate.commons.annotations;
  //requires org.hibernate.orm.core;
  //requires java.persistence;
  //requires java.naming;
  requires java.sql;
  //requires sqlite.dialect;

  opens labs.lab3_game to javafx.fxml;

  exports labs.lab3_game;
}
