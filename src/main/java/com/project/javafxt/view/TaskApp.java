package com.project.javafxt.view;

import com.project.javafxt.model.Task;
import com.project.javafxt.model.Board;
import com.project.javafxt.persistence.TaskFileHandler;
import com.project.javafxt.persistence.BoardFileHandler;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

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
        atualizarLista();

        TextField tituloField = new TextField();
        TextField descField = new TextField();

        Label labelboard = new Label();
        atualizarLabelboard(labelboard);

        Button btnCriar = new Button("Criar");
        Button btnEditar = new Button("Editar");
        Button btnExcluir = new Button("Excluir");
        Button btnVoltar = new Button("Voltar");
        Button btnLimparFiltro = new Button("Mostrar Todos");
        Button btnGerenciarboards = new Button("Gerenciar boards");

        btnCriar.setOnAction(e -> {
            if (tituloField.getText().trim().isEmpty() || descField.getText().trim().isEmpty()) {
                showAlert("Erro", "Preencha o título e a descrição para criar um Task.");
                return;
            }

            Task novo = new Task(tituloField.getText(), descField.getText());
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
            stage.close();

            try {
                if (boardSelecionado != null) {
                    new BoardApp().start(new Stage());
                } else {
                    new HomeApp().start(new Stage());
                }
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
            stage.close();
            try {
                new BoardApp().start(new Stage());
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

        VBox layout = new VBox(10);
        layout.setPadding(new Insets(15));

        HBox boardFilterBox = new HBox(10, labelboard, btnLimparFiltro);

        layout.getChildren().addAll(
                new Label("Tasks:"),
                boardFilterBox,
                TaskListView,
                new Label("Título:"),
                tituloField,
                new Label("Descrição:"),
                descField,
                new HBox(10, btnCriar, btnEditar, btnExcluir),
                new HBox(10, btnVoltar, btnGerenciarboards)
        );

        Scene scene = new Scene(layout, 500, 600);
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
