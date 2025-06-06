package com.project.javafxt.view; // Pacote onde a classe está localizada

import com.project.javafxt.model.Task; // Importa a classe Task, que representa um tarefa
import com.project.javafxt.model.Board; // NOVO: Importa a classe board, que representa um board
import com.project.javafxt.persistence.TaskFileHandler; // Importa a classe que lida com a persistência dos tarefas
import com.project.javafxt.persistence.BoardFileHandler; // NOVO: Importa a classe que lida com a persistência dos boards
import javafx.application.Application; // Importa a classe base para aplicações JavaFX
import javafx.geometry.Insets; // Importa para definir espaçamentos
import javafx.scene.Scene; // Importa a classe que representa uma cena na interface gráfica
import javafx.scene.control.*; // Importa classes de controles como botões e campos de texto
import javafx.scene.layout.*; // Importa classes para layouts
import javafx.stage.Stage; // Importa a classe que representa uma janela da aplicação

import java.util.List; // Importa a classe List para trabalhar com listas de tarefas
import java.util.ArrayList;
import java.util.stream.Collectors;

/**
 * Interface gráfica para gerenciar Tasks (tarefas).
 * Agora com suporte para associar Tasks a boards.
 */
public class TaskApp extends Application {
    // Lista de Tasks carregados do arquivo
    private List<Task> Tasks = TaskFileHandler.carregar();
    // Lista para exibição visual dos Tasks
    private ListView<String> TaskListView = new ListView<>();
    // ID do board selecionado (null se nenhum board estiver selecionado)
    private String boardSelecionado = null;

    /**
     * Define o board selecionado para filtrar os Tasks.
     * @param boardId ID do board selecionado
     */
    public void setboardSelecionado(String boardId) {
        this.boardSelecionado = boardId;
    }

    // Método que inicia a aplicação
    @Override
    public void start(Stage stage) {
        // Atualiza a lista de Tasks na interface
        atualizarLista();

        // Campos para edição de Tasks
        TextField tituloField = new TextField(); // Campo para o título do tarefa
        TextField descField = new TextField(); // Campo para a descrição do tarefa

        // NOVO: ComboBox para selecionar boards
        ComboBox<String> boardComboBox = new ComboBox<>();
        // Adiciona a opção "Sem board"
        boardComboBox.getItems().add("[Sem board]");

        // Carrega a lista de boards e adiciona ao ComboBox
        List<Board> boards = BoardFileHandler.carregar();
        for (Board p : boards) {
            boardComboBox.getItems().add(p.getId() + " - " + p.getTitulo());
        }

        // Seleciona "Sem board" por padrão
        boardComboBox.getSelectionModel().select(0);

        // Se temos um board selecionado, encontra-o no ComboBox
        if (boardSelecionado != null) {
            for (int i = 0; i < boardComboBox.getItems().size(); i++) {
                String item = boardComboBox.getItems().get(i);
                if (item.startsWith(boardSelecionado)) {
                    boardComboBox.getSelectionModel().select(i);
                    break;
                }
            }
        }

        // Label para mostrar o board selecionado
        Label labelboard = new Label();
        atualizarLabelboard(labelboard);

        // Botões para as operações CRUD
        Button btnCriar = new Button("Criar"); // Botão para criar um novo tarefa
        Button btnEditar = new Button("Editar"); // Botão para editar um tarefa existente
        Button btnExcluir = new Button("Excluir"); // Botão para excluir um tarefa
        Button btnVoltar = new Button("Voltar"); // Botão para voltar à tela anterior
        // NOVO: Botão para limpar o filtro de board
        Button btnLimparFiltro = new Button("Mostrar Todos");
        // NOVO: Botão para ir para a tela de boards
        Button btnGerenciarboards = new Button("Gerenciar boards");

        // Configuração do botão Criar
        btnCriar.setOnAction(e -> {
            // Verifica se os campos obrigatórios estão preenchidos
            if (tituloField.getText().trim().isEmpty() || descField.getText().trim().isEmpty()) {
                showAlert("Erro", "Preencha o título e a descrição para criar um Task.");
                return;
            }

            // Obtém o board selecionado (se houver)
            String boardId = obterboardIdDoComboBox(boardComboBox);

            // Cria um novo Task com os dados fornecidos
            Task novo;
            if (boardId != null) {
                novo = new Task(tituloField.getText(), descField.getText(), boardId);
            } else {
                novo = new Task(tituloField.getText(), descField.getText());
            }

            // Adiciona o Task à lista e salva
            Tasks.add(novo);
            TaskFileHandler.salvarTodos(Tasks);

            // Atualiza a interface e limpa os campos
            atualizarLista();
            tituloField.clear();
            descField.clear();

            showAlert("Sucesso", "Task criado com sucesso!");
        });

        // Configuração do botão Editar
        btnEditar.setOnAction(e -> {
            // Obtém o índice do Task selecionado
            int index = TaskListView.getSelectionModel().getSelectedIndex();

            // Verifica se um Task está selecionado
            if (index < 0) {
                showAlert("Aviso", "Selecione um Task para editar.");
                return;
            }

            // Verifica se os campos obrigatórios estão preenchidos
            if (tituloField.getText().trim().isEmpty() || descField.getText().trim().isEmpty()) {
                showAlert("Erro", "Preencha o título e a descrição para editar o Task.");
                return;
            }

            // Obtém o Task selecionado
            Task b = getTaskDaLista(index);
            if (b == null) {
                showAlert("Erro", "Task não encontrado na lista completa.");
                return;
            }

            // Atualiza os dados do Task
            b.setTitulo(tituloField.getText());
            b.setDescricao(descField.getText());

            // Atualiza o board associado ao Task
            String boardId = obterboardIdDoComboBox(boardComboBox);
            b.setBoardId(boardId);

            // Salva a lista atualizada
            TaskFileHandler.salvarTodos(Tasks);

            // Atualiza a interface
            atualizarLista();

            showAlert("Sucesso", "Task atualizado com sucesso!");
        });

        // Configuração do botão Excluir
        btnExcluir.setOnAction(e -> {
            // Obtém o índice do Task selecionado
            int index = TaskListView.getSelectionModel().getSelectedIndex();

            // Verifica se um Task está selecionado
            if (index < 0) {
                showAlert("Aviso", "Selecione um Task para excluir.");
                return;
            }

            // Obtém o Task selecionado
            Task b = getTaskDaLista(index);
            if (b == null) {
                showAlert("Erro", "Task não encontrado na lista completa.");
                return;
            }

            // Confirma a exclusão com o usuário
            Alert confirmacao = new Alert(Alert.AlertType.CONFIRMATION);
            confirmacao.setTitle("Confirmar Exclusão");
            confirmacao.setHeaderText("Excluir Task");
            confirmacao.setContentText("Tem certeza que deseja excluir o Task '" + b.getTitulo() + "'?");

            // Se o usuário confirmar, exclui o Task
            if (confirmacao.showAndWait().get() == ButtonType.OK) {
                // Remove o Task da lista completa
                Tasks.remove(b);

                // Salva a lista atualizada
                TaskFileHandler.salvarTodos(Tasks);

                // Atualiza a interface e limpa os campos
                atualizarLista();
                tituloField.clear();
                descField.clear();

                showAlert("Sucesso", "Task excluído com sucesso!");
            }
        });

        // Configuração do botão Voltar
        btnVoltar.setOnAction(e -> {
            // Fecha a tela atual
            stage.close();

            // Se temos um board selecionado, voltamos para a tela de boards
            if (boardSelecionado != null) {
                try {
                    new BoardApp().start(new Stage());
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            } else {
                // Caso contrário, voltamos para a tela Home
                try {
                    new HomeApp().start(new Stage());
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });

        // Configuração do botão Limpar Filtro
        btnLimparFiltro.setOnAction(e -> {
            // Remove o filtro de board
            boardSelecionado = null;

            // Seleciona "Sem board" no ComboBox
            boardComboBox.getSelectionModel().select(0);

            // Atualiza a interface
            atualizarLista();
            atualizarLabelboard(labelboard);
        });

        // Configuração do botão Gerenciar boards
        btnGerenciarboards.setOnAction(e -> {
            // Fecha a tela atual
            stage.close();

            // Abre a tela de boards
            try {
                new BoardApp().start(new Stage());
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        // Adiciona um listener para quando um Task é selecionado na lista
        TaskListView.getSelectionModel().selectedIndexProperty().addListener((obs, oldVal, newVal) -> {
            int i = newVal.intValue();
            if (i >= 0) {
                // Obtém o Task selecionado
                Task b = getTaskDaLista(i);
                if (b != null) {
                    // Preenche os campos com os dados do Task
                    tituloField.setText(b.getTitulo());
                    descField.setText(b.getDescricao());

                    // Seleciona o board do Task no ComboBox (se houver)
                    if (b.getBoardId() != null) {
                        for (int j = 0; j < boardComboBox.getItems().size(); j++) {
                            String item = boardComboBox.getItems().get(j);
                            if (item.startsWith(b.getBoardId())) {
                                boardComboBox.getSelectionModel().select(j);
                                break;
                            }
                        }
                    } else {
                        // Se não tiver board, seleciona "Sem board"
                        boardComboBox.getSelectionModel().select(0);
                    }
                }
            }
        });

        // Criação do layout da interface
        VBox layout = new VBox(10);
        layout.setPadding(new Insets(15));

        // NOVO: HBox para mostrar o board selecionado e o botão para limpar o filtro
        HBox boardFilterBox = new HBox(10, labelboard, btnLimparFiltro);

        // Adiciona os componentes ao layout
        layout.getChildren().addAll(
                new Label("Tasks:"),
                // Se temos um board selecionado, mostramos a informação
                boardFilterBox,
                TaskListView,
                new Label("Título:"),
                tituloField,
                new Label("Descrição:"),
                descField,
                new Label("board:"),
                boardComboBox,
                new HBox(10, btnCriar, btnEditar, btnExcluir),
                new HBox(10, btnVoltar, btnGerenciarboards)
        );

        // Configura a cena e o stage
        Scene scene = new Scene(layout, 500, 600);
        stage.setScene(scene);
        stage.setTitle("Gerenciar Tasks");
        stage.show();
    }

    /**
     * Atualiza a lista de Tasks na interface gráfica.
     * Se um board estiver selecionado, filtra os Tasks desse board.
     */
    private void atualizarLista() {
        // Recarrega a lista completa de Tasks
        Tasks = TaskFileHandler.carregar();

        // Limpa a lista visual
        TaskListView.getItems().clear();

        // Lista filtrada de Tasks (todos ou apenas de um board)
        List<Task> TasksFiltrados;

        // Se temos um board selecionado, filtramos os Tasks desse board
        if (boardSelecionado != null) {
            TasksFiltrados = Tasks.stream()
                    .filter(b -> b.getBoardId() != null &&
                            b.getBoardId().equals(boardSelecionado))
                    .collect(Collectors.toList());
        } else {
            // Caso contrário, mostramos todos os Tasks
            TasksFiltrados = Tasks;
        }

        // Adiciona cada Task à lista visual
        for (Task b : TasksFiltrados) {
            // Se o Task tem um board associado, mostramos o nome do board
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

    /**
     * Obtém o Task completo a partir do índice na lista filtrada.
     *
     * @param indexNaListaVisual Índice do Task na lista visual
     * @return O Task correspondente ou null se não encontrado
     */
    private Task getTaskDaLista(int indexNaListaVisual) {
        // Se não há Tasks na lista ou o índice é inválido, retorna null
        if (Tasks.isEmpty() || indexNaListaVisual < 0 ||
                indexNaListaVisual >= TaskListView.getItems().size()) {
            return null;
        }

        // Lista filtrada de Tasks (todos ou apenas de um board)
        List<Task> TasksFiltrados;

        // Se temos um board selecionado, filtramos os Tasks desse board
        if (boardSelecionado != null) {
            TasksFiltrados = Tasks.stream()
                    .filter(b -> b.getBoardId() != null &&
                            b.getBoardId().equals(boardSelecionado))
                    .collect(Collectors.toList());
        } else {
            // Caso contrário, usamos todos os Tasks
            TasksFiltrados = Tasks;
        }

        // Verifica se o índice é válido na lista filtrada
        if (indexNaListaVisual < TasksFiltrados.size()) {
            return TasksFiltrados.get(indexNaListaVisual);
        }

        return null;
    }

    /**
     * Obtém o ID do board selecionado no ComboBox.
     *
     * @param comboBox ComboBox com os boards
     * @return ID do board selecionado ou null se "Sem board" estiver selecionado
     */
    private String obterboardIdDoComboBox(ComboBox<String> comboBox) {
        String selecionado = comboBox.getValue();

        // Se "Sem board" estiver selecionado, retorna null
        if (selecionado == null || selecionado.equals("[Sem board]")) {
            return null;
        }

        // Extrai o ID do board do formato "ID - Nome"
        return selecionado.split(" - ")[0];
    }

    /**
     * Atualiza o label que mostra o board selecionado.
     *
     * @param label Label a ser atualizado
     */
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
