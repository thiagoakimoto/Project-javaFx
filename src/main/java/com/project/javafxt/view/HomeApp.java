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

// IMPORTAÇÃO da tela BoardApp
import com.project.javafxt.view.BoardApp;
// IMPORTAÇÃO da tela ProjetoApp
import com.project.javafxt.view.ProjetoApp;

public class HomeApp extends Application {

    private ListView<String> listaView = new ListView<>();
    private List<Usuario> usuarios = UsuarioFileHandler.carregar();

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
            ".dashboard-title {\n" +
            "    -fx-font-size: 24px;\n" +
            "    -fx-font-weight: bold;\n" +
            "    -fx-text-fill: #333;\n" +
            "    -fx-alignment: center;\n" +
            "}\n" +
            ".dashboard-card {\n" +
            "    -fx-background-color: white;\n" +
            "    -fx-background-radius: 8;\n" +
            "    -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 10, 0, 0, 3);\n" +
            "    -fx-padding: 20;\n" +
            "}\n" +
            ".section-title {\n" +
            "    -fx-font-size: 16px;\n" +
            "    -fx-font-weight: bold;\n" +
            "    -fx-text-fill: #444;\n" +
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
            ".button.delete {\n" +
            "    -fx-background-color: #d32f2f;\n" +
            "}\n" +
            ".button.delete:hover {\n" +
            "    -fx-background-color: #b71c1c;\n" +
            "}\n" +
            ".button.secondary {\n" +
            "    -fx-background-color: #6c757d;\n" +
            "}\n" +
            ".button.secondary:hover {\n" +
            "    -fx-background-color: #5a6268;\n" +
            "}\n" +
            ".button.feature {\n" +
            "    -fx-background-color: #28a745;\n" +
            "    -fx-font-size: 14px;\n" +
            "    -fx-padding: 12 20;\n" +
            "}\n" +
            ".button.feature:hover {\n" +
            "    -fx-background-color: #218838;\n" +
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
            "}\n" +
            ".text-field, .password-field {\n" +
            "    -fx-background-color: white;\n" +
            "    -fx-border-color: #e0e0e0;\n" +
            "    -fx-border-radius: 4;\n" +
            "    -fx-background-radius: 4;\n" +
            "    -fx-padding: 8;\n" +
            "}\n";

        atualizarLista();

        // Main container with better spacing and centering
        BorderPane borderPane = new BorderPane();
        
        // Center container for all content
        VBox centerContainer = new VBox(20);
        centerContainer.setAlignment(Pos.TOP_CENTER);
        centerContainer.setPadding(new Insets(20));
        centerContainer.setMaxWidth(700);
        
        // Header section
        VBox headerBox = new VBox(10);
        headerBox.setAlignment(Pos.CENTER);
        headerBox.getStyleClass().add("dashboard-card");
        headerBox.setMaxWidth(600);
        
        Label titleLabel = new Label("Task Manager - Dashboard");
        titleLabel.getStyleClass().add("dashboard-title");
        titleLabel.setMaxWidth(Double.MAX_VALUE);
        
        headerBox.getChildren().add(titleLabel);
        
        // User management card
        VBox userCard = new VBox(15);
        userCard.getStyleClass().add("dashboard-card");
        userCard.setMaxWidth(600);
        userCard.setAlignment(Pos.CENTER);
        
        Label userSectionTitle = new Label("Gerenciar Usuários");
        userSectionTitle.getStyleClass().add("section-title");
        
        listaView.setPrefHeight(200);
        listaView.setMaxWidth(560);
        
        // User form
        GridPane userForm = new GridPane();
        userForm.setHgap(10);
        userForm.setVgap(10);
        userForm.setAlignment(Pos.CENTER);
        userForm.setMaxWidth(560);
        
        TextField nomeField = new TextField();
        nomeField.setPromptText("Nome do usuário");
        nomeField.setPrefWidth(300);
        
        TextField emailField = new TextField();
        emailField.setPromptText("Email do usuário");
        
        PasswordField senhaField = new PasswordField();
        senhaField.setPromptText("Senha do usuário");
        
        userForm.add(new Label("Nome:"), 0, 0);
        userForm.add(nomeField, 1, 0);
        userForm.add(new Label("Email:"), 0, 1);
        userForm.add(emailField, 1, 1);
        userForm.add(new Label("Senha:"), 0, 2);
        userForm.add(senhaField, 1, 2);
        
        // User action buttons
        HBox userActions = new HBox(10);
        userActions.setAlignment(Pos.CENTER);
        
        Button btnEditar = new Button("Editar");
        Button btnExcluir = new Button("Excluir");
        btnExcluir.getStyleClass().add("delete");
        Button btnVoltar = new Button("Voltar");
        btnVoltar.getStyleClass().add("secondary");
        
        userActions.getChildren().addAll(btnEditar, btnExcluir, btnVoltar);
        
        userCard.getChildren().addAll(
                userSectionTitle, 
                listaView,
                userForm,
                userActions
        );
        
        // Feature management card
        VBox featureCard = new VBox(15);
        featureCard.getStyleClass().add("dashboard-card");
        featureCard.setAlignment(Pos.CENTER);
        featureCard.setMaxWidth(600);
        
        Label featureSectionTitle = new Label("Funcionalidades do Sistema");
        featureSectionTitle.getStyleClass().add("section-title");
        
        // Feature buttons with improved layout
        HBox featureButtons = new HBox(20);
        featureButtons.setAlignment(Pos.CENTER);
        featureButtons.setPadding(new Insets(20, 0, 10, 0));
        
        Button btnBoards = new Button("Gerenciar Boards");
        btnBoards.getStyleClass().addAll("feature");
        btnBoards.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(btnBoards, Priority.ALWAYS);
        
        Button btnProjetos = new Button("Gerenciar Projetos");
        btnProjetos.getStyleClass().addAll("feature");
        btnProjetos.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(btnProjetos, Priority.ALWAYS);
        
        featureButtons.getChildren().addAll(btnBoards, btnProjetos);
        
        featureCard.getChildren().addAll(featureSectionTitle, featureButtons);
        
        // Add cards to center content
        centerContainer.getChildren().addAll(headerBox, userCard, featureCard);
        
        // Set center container to the BorderPane
        borderPane.setCenter(centerContainer);

        // Set up event handlers (keep existing functionality)
        btnEditar.setOnAction(e -> {
            int index = listaView.getSelectionModel().getSelectedIndex();
            if (index >= 0) {
                Usuario u = usuarios.get(index);
                u.setNome(nomeField.getText());
                u.setEmail(emailField.getText());
                u.setSenha(senhaField.getText());
                UsuarioFileHandler.salvarTodos(usuarios);
                atualizarLista();
            }
        });

        btnExcluir.setOnAction(e -> {
            int index = listaView.getSelectionModel().getSelectedIndex();
            if (index >= 0) {
                usuarios.remove(index);
                UsuarioFileHandler.salvarTodos(usuarios);
                atualizarLista();
                nomeField.clear();
                emailField.clear();
                senhaField.clear();
            }
        });

        // Voltar
        btnVoltar.setOnAction(e -> {
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

        // Abrir CRUD de Boards
        btnBoards.setOnAction(e -> {
            // Salvar o estado da janela atual
            WindowManager.saveWindowState(stage);
            stage.close();
            
            try {
                Stage newStage = new Stage();
                new BoardApp().start(newStage);
                WindowManager.applyWindowState(newStage);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
        
        // NOVA ação: Abrir CRUD de Projetos
        btnProjetos.setOnAction(e -> {
            // Salvar o estado da janela atual
            WindowManager.saveWindowState(stage);
            stage.close();
            
            try {
                Stage newStage = new Stage();
                new ProjetoApp().start(newStage);
                WindowManager.applyWindowState(newStage);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        listaView.getSelectionModel().selectedIndexProperty().addListener((obs, oldVal, newVal) -> {
            int index = newVal.intValue();
            if (index >= 0 && index < usuarios.size()) {
                Usuario u = usuarios.get(index);
                nomeField.setText(u.getNome());
                emailField.setText(u.getEmail());
                senhaField.setText(u.getSenha());
            }
        });

        // Set up the scene with the improved layout
        Scene scene = new Scene(borderPane, 700, 700);
        scene.getStylesheets().add("data:text/css," + css.replace("\n", ""));
        
        stage.setScene(scene);
        stage.setTitle("Home - Task Manager");
        stage.show();
    }

    private void atualizarLista() {
        usuarios = UsuarioFileHandler.carregar();
        listaView.getItems().clear();
        for (Usuario u : usuarios) {
            listaView.getItems().add("👤 " + u.getNome() + " | " + u.getEmail());
        }
    }
}
