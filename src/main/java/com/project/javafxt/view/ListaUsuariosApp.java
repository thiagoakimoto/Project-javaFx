package com.project.javafxt.view;

import com.project.javafxt.model.Usuario;
import com.project.javafxt.persistence.UsuarioFileHandler;
import com.project.javafxt.util.WindowManager;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.util.List;

public class ListaUsuariosApp extends Application {

    @Override
    public void start(Stage stage) {
        // Define CSS for modern styling
        String css = 
            "* {\n" +
            "    -fx-font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;\n" +
            "}\n" +
            ".root {\n" +
            "    -fx-background-color: #f5f5f7;\n" +
            "}\n" +
            ".header-title {\n" +
            "    -fx-font-size: 24px;\n" +
            "    -fx-font-weight: bold;\n" +
            "    -fx-text-fill: #333;\n" +
            "    -fx-alignment: center;\n" +
            "}\n" +
            ".card {\n" +
            "    -fx-background-color: white;\n" +
            "    -fx-background-radius: 8;\n" +
            "    -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 10, 0, 0, 3);\n" +
            "    -fx-padding: 20;\n" +
            "}\n" +
            ".button {\n" +
            "    -fx-background-color: #0078d7;\n" +
            "    -fx-text-fill: white;\n" +
            "    -fx-font-weight: bold;\n" +
            "    -fx-background-radius: 4;\n" +
            "    -fx-padding: 8 16;\n" +
            "    -fx-cursor: hand;\n" +
            "}\n" +
            ".button:hover {\n" +
            "    -fx-background-color: #0066b3;\n" +
            "}\n" +
            ".button.secondary {\n" +
            "    -fx-background-color: #6c757d;\n" +
            "}\n" +
            ".button.secondary:hover {\n" +
            "    -fx-background-color: #5a6268;\n" +
            "}\n" +
            ".list-view {\n" +
            "    -fx-background-color: white;\n" +
            "    -fx-background-radius: 4;\n" +
            "    -fx-border-color: #e0e0e0;\n" +
            "    -fx-border-radius: 4;\n" +
            "}\n" +
            ".list-cell {\n" +
            "    -fx-padding: 10 15;\n" +
            "    -fx-background-color: white;\n" +
            "}\n" +
            ".list-cell:filled:selected {\n" +
            "    -fx-background-color: #e6f2ff;\n" +
            "    -fx-text-fill: black;\n" +
            "}\n";

        // Create main container with proper centering
        BorderPane borderPane = new BorderPane();
        
        // Center container to hold content
        VBox centerContainer = new VBox(20);
        centerContainer.setAlignment(Pos.TOP_CENTER);
        centerContainer.setPadding(new Insets(20));
        centerContainer.setMaxWidth(600);
        
        // Header card
        VBox headerCard = new VBox(10);
        headerCard.getStyleClass().add("card");
        headerCard.setAlignment(Pos.CENTER);
        headerCard.setMaxWidth(550);
        
        Label titulo = new Label("Usuários Cadastrados");
        titulo.getStyleClass().add("header-title");
        titulo.setMaxWidth(Double.MAX_VALUE);
        
        headerCard.getChildren().add(titulo);
        
        // Users list card
        VBox listCard = new VBox(10);
        listCard.getStyleClass().add("card");
        listCard.setAlignment(Pos.CENTER);
        listCard.setMaxWidth(550);
        
        ListView<String> lista = new ListView<>();
        lista.setPrefHeight(300);
        lista.setMaxWidth(510);
        
        // Carrega e adiciona os usuários no ListView
        List<Usuario> usuarios = UsuarioFileHandler.carregar();
        for (Usuario u : usuarios) {
            lista.getItems().add("👤 " + u.getNome() + " | Email: " + u.getEmail());
        }
        
        // Button container
        HBox buttonBox = new HBox(10);
        buttonBox.setAlignment(Pos.CENTER);
        
        Button voltarBtn = new Button("Voltar");
        voltarBtn.getStyleClass().add("secondary");
        
        buttonBox.getChildren().add(voltarBtn);
        
        listCard.getChildren().addAll(lista, buttonBox);
        
        // Add cards to center container
        centerContainer.getChildren().addAll(headerCard, listCard);
        
        // Set center container to the border pane center
        borderPane.setCenter(centerContainer);
        
        // Keep existing functionality
        voltarBtn.setOnAction(e -> {
            // Salvar o estado da janela atual
            WindowManager.saveWindowState(stage);
            stage.close();
            
            try {
                Stage newStage = new Stage();
                new LoginApp().start(newStage);
                WindowManager.applyWindowState(newStage);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        // Set up the scene with styling
        Scene scene = new Scene(borderPane, 600, 500);
        scene.getStylesheets().add("data:text/css," + css.replace("\n", ""));
        
        stage.setScene(scene);
        stage.setTitle("Lista de Usuários");
        stage.show();
    }
}
