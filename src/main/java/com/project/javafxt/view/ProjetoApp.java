package com.project.javafxt.view;

import com.project.javafxt.model.Projeto;
import com.project.javafxt.model.Board;
import com.project.javafxt.persistence.ProjetoFileHandler;
import com.project.javafxt.persistence.BoardFileHandler;
import com.project.javafxt.util.WindowManager;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.util.List;
import java.util.ArrayList;

/**
 * Interface gráfica para gerenciar projetos.
 * Permite criar, visualizar, editar e excluir projetos,
 * bem como visualizar os boards associados a cada projeto.
 */
public class ProjetoApp extends Application {
    
    // Lista de projetos carregados do arquivo
    private List<Projeto> projetos = ProjetoFileHandler.carregar();
    // Lista para exibição visual dos projetos
    private ListView<String> projetoListView = new ListView<>();
    
    @Override
    public void start(Stage stage) {
        // Define modern styling with CSS
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
            ".button.success {\n" +
            "    -fx-background-color: #28a745;\n" +
            "}\n" +
            ".button.success:hover {\n" +
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
            ".text-field, .text-area {\n" +
            "    -fx-background-color: white;\n" +
            "    -fx-border-color: #e0e0e0;\n" +
            "    -fx-border-radius: 4;\n" +
            "    -fx-background-radius: 4;\n" +
            "    -fx-padding: 8;\n" +
            "}\n";
            
        // Atualiza a lista de projetos na interface
        atualizarLista();
        
        // Main container with BorderPane for better centering
        BorderPane borderPane = new BorderPane();
        
        // Main content container
        VBox mainContainer = new VBox(20);
        mainContainer.setPadding(new Insets(20));
        mainContainer.setAlignment(Pos.TOP_CENTER);
        mainContainer.setMaxWidth(700);
        
        // Header card
        VBox headerCard = new VBox(10);
        headerCard.getStyleClass().add("card");
        headerCard.setMaxWidth(600);
        headerCard.setAlignment(Pos.CENTER);
        
        Label titleLabel = new Label("Gerenciamento de Projetos");
        titleLabel.getStyleClass().add("header-title");
        titleLabel.setMaxWidth(Double.MAX_VALUE);
        
        headerCard.getChildren().add(titleLabel);
        
        // Project list card
        VBox listCard = new VBox(10);
        listCard.getStyleClass().add("card");
        listCard.setMaxWidth(600);
        listCard.setAlignment(Pos.CENTER);
        
        Label listTitle = new Label("Projetos");
        listTitle.getStyleClass().add("section-title");
        
        projetoListView.setPrefHeight(250);
        projetoListView.setMaxWidth(560);
        
        listCard.getChildren().addAll(listTitle, projetoListView);
        
        // Project details card
        VBox detailsCard = new VBox(15);
        detailsCard.getStyleClass().add("card");
        detailsCard.setMaxWidth(600);
        detailsCard.setAlignment(Pos.CENTER);
        
        Label detailsTitle = new Label("Detalhes do Projeto");
        detailsTitle.getStyleClass().add("section-title");
        
        VBox formFields = new VBox(10);
        formFields.setMaxWidth(560);
        
        TextField nomeField = new TextField();
        nomeField.setPromptText("Nome do projeto");
        nomeField.setMaxWidth(Double.MAX_VALUE);
        
        TextField descricaoField = new TextField();
        descricaoField.setPromptText("Descrição do projeto");
        descricaoField.setMaxWidth(Double.MAX_VALUE);
        
        formFields.getChildren().addAll(
            new Label("Nome do Projeto:"),
            nomeField,
            new Label("Descrição do Projeto:"),
            descricaoField
        );
        
        // Project action buttons
        HBox actionButtons = new HBox(15);
        actionButtons.setAlignment(Pos.CENTER);
        
        Button btnCriar = new Button("Criar Projeto");
        Button btnEditar = new Button("Editar Projeto");
        Button btnExcluir = new Button("Excluir Projeto");
        btnExcluir.getStyleClass().add("delete");
        
        actionButtons.getChildren().addAll(btnCriar, btnEditar, btnExcluir);
        
        detailsCard.getChildren().addAll(detailsTitle, formFields, actionButtons);
        
        // Navigation buttons
        HBox navButtons = new HBox(15);
        navButtons.setAlignment(Pos.CENTER);
        
        Button btnVerBoards = new Button("Ver Boards do Projeto");
        btnVerBoards.getStyleClass().add("success");
        
        Button btnVoltar = new Button("Voltar");
        btnVoltar.getStyleClass().add("secondary");
        
        navButtons.getChildren().addAll(btnVerBoards, btnVoltar);
        
        // Add all cards to main container
        mainContainer.getChildren().addAll(headerCard, listCard, detailsCard, navButtons);
        
        // Set main container in the center of BorderPane
        borderPane.setCenter(mainContainer);
        
        // Keep existing functionality for button actions
        // Configuração do botão Criar
        btnCriar.setOnAction(e -> {
            // Verifica se os campos estão preenchidos
            if (nomeField.getText().trim().isEmpty() || descricaoField.getText().trim().isEmpty()) {
                showAlert("Erro", "Preencha todos os campos para criar um projeto.");
                return;
            }
            
            // Cria um novo projeto com os dados dos campos
            Projeto novoProjeto = new Projeto(nomeField.getText(), descricaoField.getText());
            projetos.add(novoProjeto);
            
            // Salva a lista atualizada no arquivo
            ProjetoFileHandler.salvarTodos(projetos);
            
            // Atualiza a lista na interface e limpa os campos
            atualizarLista();
            nomeField.clear();
            descricaoField.clear();
            
            showAlert("Sucesso", "Projeto criado com sucesso!");
        });
        
        // Configuração do botão Editar
        btnEditar.setOnAction(e -> {
            // Obtém o índice do projeto selecionado
            int index = projetoListView.getSelectionModel().getSelectedIndex();
            
            // Verifica se um projeto está selecionado
            if (index < 0) {
                showAlert("Aviso", "Selecione um projeto para editar.");
                return;
            }
            
            // Verifica se os campos estão preenchidos
            if (nomeField.getText().trim().isEmpty() || descricaoField.getText().trim().isEmpty()) {
                showAlert("Erro", "Preencha todos os campos para editar o projeto.");
                return;
            }
            
            // Atualiza os dados do projeto selecionado
            Projeto p = projetos.get(index);
            p.setNome(nomeField.getText());
            p.setDescricao(descricaoField.getText());
            
            // Salva a lista atualizada
            ProjetoFileHandler.salvarTodos(projetos);
            
            // Atualiza a lista na interface
            atualizarLista();
            
            showAlert("Sucesso", "Projeto atualizado com sucesso!");
        });
        
        // Configuração do botão Excluir
        btnExcluir.setOnAction(e -> {
            // Obtém o índice do projeto selecionado
            int index = projetoListView.getSelectionModel().getSelectedIndex();
            
            // Verifica se um projeto está selecionado
            if (index < 0) {
                showAlert("Aviso", "Selecione um projeto para excluir.");
                return;
            }
            
            // Obtém o projeto selecionado
            Projeto projetoParaExcluir = projetos.get(index);
            
            // Confirma a exclusão com o usuário
            Alert confirmacao = new Alert(Alert.AlertType.CONFIRMATION);
            confirmacao.setTitle("Confirmar Exclusão");
            confirmacao.setHeaderText("Excluir Projeto");
            confirmacao.setContentText("Tem certeza que deseja excluir o projeto '" + 
                                       projetoParaExcluir.getNome() + "'?");
            
            // Se o usuário confirmar, exclui o projeto
            if (confirmacao.showAndWait().get() == ButtonType.OK) {
                // Verifica se existem boards associados a este projeto
                List<Board> todosBoardsExistentes = BoardFileHandler.carregar();
                List<Board> boardsAtualizados = new ArrayList<>();
                boolean temBoardsAssociados = false;
                
                // Para cada board, verifica se está associado ao projeto a ser excluído
                for (Board board : todosBoardsExistentes) {
                    if (board.getProjetoId() != null && 
                        board.getProjetoId().equals(projetoParaExcluir.getId())) {
                        // Este board está associado ao projeto - remove a associação
                        board.setProjetoId(null);
                        temBoardsAssociados = true;
                    }
                    boardsAtualizados.add(board);
                }
                
                // Se encontrou boards associados, atualiza-os
                if (temBoardsAssociados) {
                    BoardFileHandler.salvarTodos(boardsAtualizados);
                    showAlert("Informação", "Boards associados a este projeto foram desvinculados.");
                }
                
                // Remove o projeto da lista
                projetos.remove(index);
                
                // Salva a lista atualizada
                ProjetoFileHandler.salvarTodos(projetos);
                
                // Atualiza a lista na interface e limpa os campos
                atualizarLista();
                nomeField.clear();
                descricaoField.clear();
                
                showAlert("Sucesso", "Projeto excluído com sucesso!");
            }
        });
        
        // Configuração do botão Ver Boards
        btnVerBoards.setOnAction(e -> {
            // Obtém o índice do projeto selecionado
            int index = projetoListView.getSelectionModel().getSelectedIndex();
            
            // Verifica se um projeto está selecionado
            if (index < 0) {
                showAlert("Aviso", "Selecione um projeto para ver seus boards.");
                return;
            }
            
            // Obtém o projeto selecionado
            Projeto projetoSelecionado = projetos.get(index);
            
            // Salvar o estado da janela atual
            WindowManager.saveWindowState(stage);
            // Fecha a tela atual
            stage.close();
            
            // Abre a tela de boards filtrada para este projeto
            try {
                // Criamos uma instância de BoardApp e passamos o ID do projeto como parâmetro
                BoardApp boardApp = new BoardApp();
                // Definimos qual projeto está selecionado antes de abrir a tela
                boardApp.setProjetoSelecionado(projetoSelecionado.getId());
                
                Stage newStage = new Stage();
                boardApp.start(newStage);
                WindowManager.applyWindowState(newStage);
            } catch (Exception ex) {
                ex.printStackTrace();
                showAlert("Erro", "Não foi possível abrir a tela de boards.");
            }
        });
        
        // Configuração do botão Voltar
        btnVoltar.setOnAction(e -> {
            // Salvar o estado da janela atual
            WindowManager.saveWindowState(stage);
            // Fecha a tela atual
            stage.close();
            
            // Volta para a tela Home
            try {
                Stage newStage = new Stage();
                new HomeApp().start(newStage);
                WindowManager.applyWindowState(newStage);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
        
        // Adiciona um listener para quando um projeto é selecionado na lista
        projetoListView.getSelectionModel().selectedIndexProperty().addListener((obs, oldVal, newVal) -> {
            int i = newVal.intValue();
            if (i >= 0 && i < projetos.size()) {
                // Preenche os campos com os dados do projeto selecionado
                Projeto p = projetos.get(i);
                nomeField.setText(p.getNome());
                descricaoField.setText(p.getDescricao());
            }
        });
        
        // Set up the scene with modern styling
        Scene scene = new Scene(borderPane, 700, 700);
        scene.getStylesheets().add("data:text/css," + css.replace("\n", ""));
        
        stage.setScene(scene);
        stage.setTitle("Gerenciamento de Projetos");
        stage.show();
    }
    
    /**
     * Atualiza a lista de projetos na interface gráfica.
     */
    private void atualizarLista() {
        // Recarrega a lista de projetos do arquivo
        projetos = ProjetoFileHandler.carregar();
        
        // Limpa a lista visual
        projetoListView.getItems().clear();
        
        // Adiciona cada projeto à lista visual
        for (Projeto p : projetos) {
            projetoListView.getItems().add("📂 " + p.getNome() + " - " + p.getDescricao());
        }
    }
    
    /**
     * Exibe um alerta com o título e mensagem especificados.
     * 
     * @param titulo Título do alerta
     * @param mensagem Mensagem do alerta
     */
    private void showAlert(String titulo, String mensagem) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }
}
