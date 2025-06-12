package com.project.javafxt.view;

import com.project.javafxt.model.Task;
import com.project.javafxt.model.Board;
import com.project.javafxt.persistence.TaskFileHandler;
import com.project.javafxt.persistence.BoardFileHandler;
import com.project.javafxt.util.WindowManager;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import java.util.List;
import java.util.stream.Collectors;
import java.util.ArrayList;

public class TaskApp extends Application {
    private List<Task> Tasks = TaskFileHandler.carregar();
    private ListView<String> TaskListView = new ListView<>();
    private String boardSelecionado = null;

    public void setboardSelecionado(String boardId) {
        this.boardSelecionado = boardId;
    }

    @Override
    public void start(Stage stage) {
        // Apply modern styling
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
            ".section-title {\n" +
            "    -fx-font-size: 16px;\n" +
            "    -fx-font-weight: bold;\n" +
            "    -fx-text-fill: #444;\n" +
            "}\n" +
            ".filter-badge {\n" +
            "    -fx-background-color: #e3f2fd;\n" +
            "    -fx-background-radius: 4;\n" +
            "    -fx-padding: 5 10;\n" +
            "    -fx-text-fill: #0d47a1;\n" +
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
            ".card {\n" +
            "    -fx-background-color: white;\n" +
            "    -fx-background-radius: 8;\n" +
            "    -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 10, 0, 0, 3);\n" +
            "    -fx-padding: 20;\n" +
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
            ".text-field, .text-area {\n" +
            "    -fx-background-color: white;\n" +
            "    -fx-border-color: #e0e0e0;\n" +
            "    -fx-border-radius: 4;\n" +
            "    -fx-background-radius: 4;\n" +
            "    -fx-padding: 8;\n" +
            "}\n";

        atualizarLista();

        // Create main container centered
        BorderPane borderPane = new BorderPane();
        
        // Create card-based layout
        VBox mainContainer = new VBox(20);
        mainContainer.setPadding(new Insets(20));
        mainContainer.setAlignment(Pos.TOP_CENTER);
        mainContainer.setMaxWidth(700);

        // Header with title and filter info
        VBox headerCard = new VBox(10);
        headerCard.getStyleClass().add("card");
        headerCard.setMaxWidth(600);
        headerCard.setAlignment(Pos.CENTER);

        Label titleLabel = new Label("Gerenciar Tasks");
        titleLabel.getStyleClass().add("header-title");
        titleLabel.setMaxWidth(Double.MAX_VALUE);
        
        Label labelboard = new Label();
        atualizarLabelboard(labelboard);
        labelboard.getStyleClass().add("filter-badge");
        
        HBox filterControls = new HBox(15);
        filterControls.setAlignment(Pos.CENTER);
        Button btnLimparFiltro = new Button("Mostrar Todos");
        btnLimparFiltro.getStyleClass().add("secondary");
        filterControls.getChildren().addAll(labelboard, btnLimparFiltro);
        
        headerCard.getChildren().addAll(titleLabel, filterControls);

        // Task list card
        VBox listCard = new VBox(10);
        listCard.getStyleClass().add("card");
        listCard.setMaxWidth(600);
        listCard.setAlignment(Pos.CENTER);
        
        Label listTitle = new Label("Lista de Tasks");
        listTitle.getStyleClass().add("section-title");
        
        TaskListView.setPrefHeight(250);
        TaskListView.setMaxWidth(560);
        
        listCard.getChildren().addAll(listTitle, TaskListView);

        // Task details card
        VBox detailsCard = new VBox(10);
        detailsCard.getStyleClass().add("card");
        detailsCard.setMaxWidth(600);
        detailsCard.setAlignment(Pos.CENTER);
        
        Label detailsTitle = new Label("Detalhes do Task");
        detailsTitle.getStyleClass().add("section-title");
        
        VBox formFields = new VBox(10);
        formFields.setMaxWidth(560);
        
        TextField tituloField = new TextField();
        tituloField.setPromptText("Título do Task");
        tituloField.setMaxWidth(Double.MAX_VALUE);
        
        TextField descField = new TextField();
        descField.setPromptText("Descrição do Task");
        descField.setMaxWidth(Double.MAX_VALUE);
        
        formFields.getChildren().addAll(
            new Label("Título:"), 
            tituloField,
            new Label("Descrição:"), 
            descField
        );
        
        // Action buttons
        HBox actionButtons = new HBox(15);
        actionButtons.setAlignment(Pos.CENTER);
        
        Button btnCriar = new Button("Criar");
        Button btnEditar = new Button("Editar");
        Button btnExcluir = new Button("Excluir");
        btnExcluir.getStyleClass().add("delete");
        
        actionButtons.getChildren().addAll(btnCriar, btnEditar, btnExcluir);
        
        detailsCard.getChildren().addAll(detailsTitle, formFields, actionButtons);

        // Navigation buttons
        HBox navButtons = new HBox(15);
        navButtons.setAlignment(Pos.CENTER);
        
        Button btnVoltar = new Button("Voltar");
        btnVoltar.getStyleClass().add("secondary");
        
        Button btnGerenciarboards = new Button("Gerenciar Boards");
        
        navButtons.getChildren().addAll(btnVoltar, btnGerenciarboards);

        // Add all cards to main container
        mainContainer.getChildren().addAll(headerCard, listCard, detailsCard, navButtons);
        
        // Set up the BorderPane with the centered content
        borderPane.setCenter(mainContainer);

        // Setup button actions (keep existing functionality)
        btnCriar.setOnAction(e -> {
            if (tituloField.getText().trim().isEmpty() || descField.getText().trim().isEmpty()) {
                showAlert("Erro", "Preencha o título e a descrição para criar um Task.");
                return;
            }

            Task novo = new Task(tituloField.getText(), descField.getText());
            if (boardSelecionado != null) {
                novo.setBoardId(boardSelecionado);
            }
            Tasks.add(novo);
            TaskFileHandler.salvarTodos(Tasks);

            atualizarLista();
            tituloField.clear();
            descField.clear();

            showAlert("Sucesso", "Task criado com sucesso!");
        });

        btnEditar.setOnAction(e -> {
            int index = TaskListView.getSelectionModel().getSelectedIndex();
            if (index < 0) {
                showAlert("Aviso", "Selecione um Task para editar.");
                return;
            }

            if (tituloField.getText().trim().isEmpty() || descField.getText().trim().isEmpty()) {
                showAlert("Erro", "Preencha o título e a descrição para editar o Task.");
                return;
            }

            Task b = getTaskDaLista(index);
            if (b == null) {
                showAlert("Erro", "Task não encontrado na lista completa.");
                return;
            }

            b.setTitulo(tituloField.getText());
            b.setDescricao(descField.getText());

            TaskFileHandler.salvarTodos(Tasks);
            atualizarLista();

            showAlert("Sucesso", "Task atualizado com sucesso!");
        });

        btnExcluir.setOnAction(e -> {
            int index = TaskListView.getSelectionModel().getSelectedIndex();
            if (index < 0) {
                showAlert("Aviso", "Selecione um Task para excluir.");
                return;
            }

            Task b = getTaskDaLista(index);
            if (b == null) {
                showAlert("Erro", "Task não encontrado na lista completa.");
                return;
            }

            Alert confirmacao = new Alert(Alert.AlertType.CONFIRMATION);
            confirmacao.setTitle("Confirmar Exclusão");
            confirmacao.setHeaderText("Excluir Task");
            confirmacao.setContentText("Tem certeza que deseja excluir o Task '" + b.getTitulo() + "'?");

            if (confirmacao.showAndWait().get() == ButtonType.OK) {
                Tasks.remove(b);
                TaskFileHandler.salvarTodos(Tasks);
                atualizarLista();
                tituloField.clear();
                descField.clear();

                showAlert("Sucesso", "Task excluído com sucesso!");
            }
        });

        btnVoltar.setOnAction(e -> {
            // Salvar o estado da janela atual
            WindowManager.saveWindowState(stage);
            stage.close();

            try {
                Stage newStage = new Stage();
                
                if (boardSelecionado != null) {
                    new BoardApp().start(newStage);
                } else {
                    new HomeApp().start(newStage);
                }
                
                // Aplicar o estado da janela salvo
                WindowManager.applyWindowState(newStage);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        btnLimparFiltro.setOnAction(e -> {
            boardSelecionado = null;
            atualizarLista();
            atualizarLabelboard(labelboard);
        });

        btnGerenciarboards.setOnAction(e -> {
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

        TaskListView.getSelectionModel().selectedIndexProperty().addListener((obs, oldVal, newVal) -> {
            int i = newVal.intValue();
            if (i >= 0) {
                Task b = getTaskDaLista(i);
                if (b != null) {
                    tituloField.setText(b.getTitulo());
                    descField.setText(b.getDescricao());
                }
            }
        });

        // Set up the scene with the CSS
        Scene scene = new Scene(borderPane, 700, 700);
        scene.getStylesheets().add("data:text/css," + css.replace("\n", ""));
        
        stage.setScene(scene);
        stage.setTitle("Gerenciar Tasks");
        stage.show();
    }

    private void atualizarLista() {
        Tasks = TaskFileHandler.carregar();
        TaskListView.getItems().clear();

        List<Task> TasksFiltrados;

        if (boardSelecionado != null) {
            TasksFiltrados = Tasks.stream()
                    .filter(b -> b.getBoardId() != null &&
                            b.getBoardId().equals(boardSelecionado))
                    .collect(Collectors.toList());
        } else {
            TasksFiltrados = Tasks;
        }

        for (Task b : TasksFiltrados) {
            String boardNome = "";
            if (b.getBoardId() != null) {
                Board p = BoardFileHandler.buscarPorId(b.getBoardId());
                if (p != null) {
                    boardNome = " [board: " + p.getTitulo() + "]";
                }
            }
            TaskListView.getItems().add("📌 " + b.getTitulo() + " - " + b.getDescricao() + boardNome);
        }
    }

    private Task getTaskDaLista(int indexNaListaVisual) {
        if (Tasks.isEmpty() || indexNaListaVisual < 0 || indexNaListaVisual >= TaskListView.getItems().size()) {
            return null;
        }

        List<Task> TasksFiltrados;

        if (boardSelecionado != null) {
            TasksFiltrados = Tasks.stream()
                    .filter(b -> b.getBoardId() != null &&
                            b.getBoardId().equals(boardSelecionado))
                    .collect(Collectors.toList());
        } else {
            TasksFiltrados = Tasks;
        }

        if (indexNaListaVisual < TasksFiltrados.size()) {
            return TasksFiltrados.get(indexNaListaVisual);
        }

        return null;
    }

    private void atualizarLabelboard(Label label) {
        if (boardSelecionado != null) {
            Board p = BoardFileHandler.buscarPorId(boardSelecionado);
            if (p != null) {
                label.setText("Filtrando Tasks do board: " + p.getTitulo());
            } else {
                label.setText("Filtrando Tasks (board não encontrado)");
            }
        } else {
            label.setText("Mostrando todos os Tasks");
        }
    }

    private void showAlert(String titulo, String mensagem) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }
}
