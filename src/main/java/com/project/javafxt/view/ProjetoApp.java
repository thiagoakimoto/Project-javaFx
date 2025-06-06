package com.project.javafxt.view;

import com.project.javafxt.model.Projeto;
import com.project.javafxt.model.Board;
import com.project.javafxt.persistence.ProjetoFileHandler;
import com.project.javafxt.persistence.BoardFileHandler;
import javafx.application.Application;
import javafx.geometry.Insets;
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
        // Atualiza a lista de projetos na interface
        atualizarLista();
        
        // Campos para edição de projetos
        TextField nomeField = new TextField();
        TextField descricaoField = new TextField();
        
        // Botões para as operações CRUD (Criar, Ler, Atualizar, Deletar)
        Button btnCriar = new Button("Criar Projeto");
        Button btnEditar = new Button("Editar Projeto");
        Button btnExcluir = new Button("Excluir Projeto");
        Button btnVerBoards = new Button("Ver Boards do Projeto");
        Button btnVoltar = new Button("Voltar");
        
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
            
            // Fecha a tela atual
            stage.close();
            
            // Abre a tela de boards filtrada para este projeto
            try {
                // Criamos uma instância de BoardApp e passamos o ID do projeto como parâmetro
                BoardApp boardApp = new BoardApp();
                // Definimos qual projeto está selecionado antes de abrir a tela
                boardApp.setProjetoSelecionado(projetoSelecionado.getId());
                boardApp.start(new Stage());
            } catch (Exception ex) {
                ex.printStackTrace();
                showAlert("Erro", "Não foi possível abrir a tela de boards.");
            }
        });
        
        // Configuração do botão Voltar
        btnVoltar.setOnAction(e -> {
            // Fecha a tela atual
            stage.close();
            
            // Volta para a tela Home
            try {
                new HomeApp().start(new Stage());
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
        
        // Criação do layout da interface
        VBox layout = new VBox(10);
        layout.setPadding(new Insets(15));
        
        // Adiciona os componentes ao layout
        layout.getChildren().addAll(
            new Label("Projetos:"),
            projetoListView,
            new Label("Nome do Projeto:"),
            nomeField,
            new Label("Descrição do Projeto:"),
            descricaoField,
            new HBox(10, btnCriar, btnEditar, btnExcluir),
            new HBox(10, btnVerBoards, btnVoltar)
        );
        
        // Configura a cena e o stage
        Scene scene = new Scene(layout, 500, 500);
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
