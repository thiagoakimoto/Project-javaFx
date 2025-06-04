package com.project.javafxt.view;

import com.project.javafxt.model.Usuario;
import com.project.javafxt.persistence.UsuarioFileHandler;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.util.List;


public class ListaUsuariosApp extends Application {

    @Override
    public void start(Stage stage) {
        VBox layout = new VBox(10);
        layout.setPadding(new Insets(15));

        Label titulo = new Label("Usuários Cadastrados");
        ListView<String> lista = new ListView<>();

        // Carrega e adiciona os usuários no ListView
        List<Usuario> usuarios = UsuarioFileHandler.carregar();
        for (Usuario u : usuarios) {
            lista.getItems().add("Nome: " + u.getNome() + " | Email: " + u.getEmail());
        }

        Button voltarBtn = new Button("Voltar");
        voltarBtn.setOnAction(e -> {
            stage.close();
            new LoginApp().start(new Stage());
        });

        layout.getChildren().addAll(titulo, lista, voltarBtn);
        Scene scene = new Scene(layout, 400, 300);
        stage.setScene(scene);
        stage.setTitle("Lista de Usuários");
        stage.show();
    }
}
