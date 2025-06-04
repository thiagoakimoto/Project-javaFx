module com.example.javafxteste {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires org.kordamp.bootstrapfx.core;

    opens com.project.javafxt to javafx.fxml;
    exports com.project.javafxt.model;
    exports com.project.javafxt.persistence;
    exports com.project.javafxt.view;
    opens com.project.javafxt.model to javafx.fxml;
}