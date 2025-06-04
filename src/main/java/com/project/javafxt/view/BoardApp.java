package com.project.javafxt.view; // Pacote onde a classe está localizada

import com.project.javafxt.model.Board; // Importa a classe Board, que representa um quadro
import com.project.javafxt.persistence.BoardFileHandler; // Importa a classe que lida com a persistência dos quadros
import javafx.application.Application; // Importa a classe base para aplicações JavaFX
import javafx.geometry.Insets; // Importa para definir espaçamentos
import javafx.scene.Scene; // Importa a classe que representa uma cena na interface gráfica
import javafx.scene.control.*; // Importa classes de controles como botões e campos de texto
import javafx.scene.layout.*; // Importa classes para layouts
import javafx.stage.Stage; // Importa a classe que representa uma janela da aplicação

import java.util.List; // Importa a classe List para trabalhar com listas de quadros

// Classe principal da aplicação que estende Application do JavaFX
public class BoardApp extends Application {
    // Lista de quadros carregados do arquivo
    private List<Board> boards = BoardFileHandler.carregar();
    // Lista para exibir os quadros na interface
    private ListView<String> boardListView = new ListView<>();

    // Método que inicia a aplicação
    @Override
    public void start(Stage stage) {
        atualizarLista(); // Atualiza a lista de quadros na interface

        // Cria campos de texto para editar informações do quadro
        TextField tituloField = new TextField(); // Campo para o título do quadro
        TextField descField = new TextField(); // Campo para a descrição do quadro

        // Cria botões para criar, editar, excluir e voltar
        Button btnCriar = new Button("Criar"); // Botão para criar um novo quadro
        Button btnEditar = new Button("Editar"); // Botão para editar um quadro existente
        Button btnExcluir = new Button("Excluir"); // Botão para excluir um quadro
        Button btnVoltar = new Button("Voltar"); // Botão para voltar à tela anterior

        // Ação do botão Criar
        btnCriar.setOnAction(e -> {
            // Cria um novo quadro com os dados dos campos de texto
            Board novo = new Board(tituloField.getText(), descField.getText());
            boards.add(novo); // Adiciona o novo quadro à lista
            BoardFileHandler.salvarTodos(boards); // Salva todos os quadros no arquivo
            atualizarLista(); // Atualiza a lista na interface
            tituloField.clear(); // Limpa o campo de título
            descField.clear(); // Limpa o campo de descrição
        });

        // Ação do botão Editar
        btnEditar.setOnAction(e -> {
            // Obtém o índice do quadro selecionado na lista
            int index = boardListView.getSelectionModel().getSelectedIndex();
            if (index >= 0) { // Verifica se um quadro está selecionado
                Board b = boards.get(index); // Obtém o quadro correspondente ao índice
                // Atualiza os dados do quadro com os valores dos campos de texto
                b.setTitulo(tituloField.getText());
                b.setDescricao(descField.getText());
                BoardFileHandler.salvarTodos(boards); // Salva todos os quadros no arquivo
                atualizarLista(); // Atualiza a lista na interface
            }
        });

        // Ação do botão Excluir
        btnExcluir.setOnAction(e -> {
            // Obtém o índice do quadro selecionado na lista
            int index = boardListView.getSelectionModel().getSelectedIndex();
            if (index >= 0) { // Verifica se um quadro está selecionado
                boards.remove(index); // Remove o quadro da lista
                BoardFileHandler.salvarTodos(boards); // Salva todos os quadros no arquivo
                atualizarLista(); // Atualiza a lista na interface
                tituloField.clear(); // Limpa o campo de título
                descField.clear(); // Limpa o campo de descrição
            }
        });

        // Adiciona um listener para quando um quadro na lista é selecionado
        boardListView.getSelectionModel().selectedIndexProperty().addListener((obs, oldVal, newVal) -> {
            int i = newVal.intValue(); // Obtém o novo índice selecionado
            if (i >= 0 && i < boards.size()) { // Verifica se o índice é válido
                // Preenche os campos de texto com os dados do quadro selecionado
                tituloField.setText(boards.get(i).getTitulo());
                descField.setText(boards.get(i).getDescricao());
            }
        });

        // Ação do botão Voltar
        btnVoltar.setOnAction(e -> {
            stage.close(); // Fecha a janela atual
            new HomeApp().start(new Stage()); // Abre a tela inicial novamente
        });

        // Cria o layout da interface
        VBox layout = new VBox(10, // Layout vertical com espaçamento de 10
                new Label("Boards:"), boardListView, // Título e lista de quadros
                new Label("Título:"), tituloField, // Campo para título
                new Label("Descrição:"), descField, // Campo para descrição
                new HBox(10, btnCriar, btnEditar, btnExcluir), // Botões lado a lado
                btnVoltar // Botão para voltar
        );
        layout.setPadding(new Insets(15)); // Adiciona espaçamento ao redor do layout

        // Configura a cena e a janela
        stage.setScene(new Scene(layout, 400, 500)); // Define a cena com o layout e tamanho
        stage.setTitle("Gerenciar Boards"); // Define o título da janela
        stage.show(); // Exibe a janela
    }

    // Método para atualizar a lista de quadros na interface
    private void atualizarLista() {
        boards = BoardFileHandler.carregar(); // Carrega a lista de quadros do arquivo
        boardListView.getItems().clear(); // Limpa a lista exibida
        // Adiciona cada quadro à lista exibida
        for (Board b : boards) {
            boardListView.getItems().add("📌 " + b.getTitulo() + " - " + b.getDescricao());
        }
    }
}
