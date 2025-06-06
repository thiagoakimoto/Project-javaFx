package com.project.javafxt.view; // Pacote onde a classe está localizada

import com.project.javafxt.model.Board; // Importa a classe Board, que representa um quadro
import com.project.javafxt.model.Projeto; // NOVO: Importa a classe Projeto, que representa um projeto
import com.project.javafxt.persistence.BoardFileHandler; // Importa a classe que lida com a persistência dos quadros
import com.project.javafxt.persistence.ProjetoFileHandler; // NOVO: Importa a classe que lida com a persistência dos projetos
import javafx.application.Application; // Importa a classe base para aplicações JavaFX
import javafx.geometry.Insets; // Importa para definir espaçamentos
import javafx.scene.Scene; // Importa a classe que representa uma cena na interface gráfica
import javafx.scene.control.*; // Importa classes de controles como botões e campos de texto
import javafx.scene.layout.*; // Importa classes para layouts
import javafx.stage.Stage; // Importa a classe que representa uma janela da aplicação

import java.util.List; // Importa a classe List para trabalhar com listas de quadros
import java.util.ArrayList;
import java.util.stream.Collectors;

/**
 * Interface gráfica para gerenciar boards (quadros).
 * Agora com suporte para associar boards a projetos.
 */
public class BoardApp extends Application {
    // Lista de boards carregados do arquivo
    private List<Board> boards = BoardFileHandler.carregar();
    // Lista para exibição visual dos boards
    private ListView<String> boardListView = new ListView<>();
    // ID do projeto selecionado (null se nenhum projeto estiver selecionado)
    private String projetoSelecionado = null;
    
    /**
     * Define o projeto selecionado para filtrar os boards.
     * @param projetoId ID do projeto selecionado
     */
    public void setProjetoSelecionado(String projetoId) {
        this.projetoSelecionado = projetoId;
    }

    // Método que inicia a aplicação
    @Override
    public void start(Stage stage) {
        // Atualiza a lista de boards na interface
        atualizarLista();

        // Campos para edição de boards
        TextField tituloField = new TextField(); // Campo para o título do quadro
        TextField descField = new TextField(); // Campo para a descrição do quadro
        
        // NOVO: ComboBox para selecionar projetos
        ComboBox<String> projetoComboBox = new ComboBox<>();
        // Adiciona a opção "Sem projeto"
        projetoComboBox.getItems().add("[Sem projeto]");
        
        // Carrega a lista de projetos e adiciona ao ComboBox
        List<Projeto> projetos = ProjetoFileHandler.carregar();
        for (Projeto p : projetos) {
            projetoComboBox.getItems().add(p.getId() + " - " + p.getNome());
        }
        
        // Seleciona "Sem projeto" por padrão
        projetoComboBox.getSelectionModel().select(0);
        
        // Se temos um projeto selecionado, encontra-o no ComboBox
        if (projetoSelecionado != null) {
            for (int i = 0; i < projetoComboBox.getItems().size(); i++) {
                String item = projetoComboBox.getItems().get(i);
                if (item.startsWith(projetoSelecionado)) {
                    projetoComboBox.getSelectionModel().select(i);
                    break;
                }
            }
        }
        
        // Label para mostrar o projeto selecionado
        Label labelProjeto = new Label();
        atualizarLabelProjeto(labelProjeto);

        // Botões para as operações CRUD
        Button btnCriar = new Button("Criar"); // Botão para criar um novo quadro
        Button btnEditar = new Button("Editar"); // Botão para editar um quadro existente
        Button btnExcluir = new Button("Excluir"); // Botão para excluir um quadro
        Button btnVoltar = new Button("Voltar"); // Botão para voltar à tela anterior
        Button btnVerTasks = new Button("Ver Tasks do Board");
        // NOVO: Botão para limpar o filtro de projeto
        Button btnLimparFiltro = new Button("Mostrar Todos");
        // NOVO: Botão para ir para a tela de projetos
        Button btnGerenciarProjetos = new Button("Gerenciar Projetos");

        // Configuração do botão Criar
        btnCriar.setOnAction(e -> {
            // Verifica se os campos obrigatórios estão preenchidos
            if (tituloField.getText().trim().isEmpty() || descField.getText().trim().isEmpty()) {
                showAlert("Erro", "Preencha o título e a descrição para criar um board.");
                return;
            }
            
            // Obtém o projeto selecionado (se houver)
            String projetoId = obterProjetoIdDoComboBox(projetoComboBox);
            
            // Cria um novo board com os dados fornecidos
            Board novo;
            if (projetoId != null) {
                novo = new Board(tituloField.getText(), descField.getText(), projetoId);
            } else {
                novo = new Board(tituloField.getText(), descField.getText());
            }
            
            // Adiciona o board à lista e salva
            boards.add(novo);
            BoardFileHandler.salvarTodos(boards);
            
            // Atualiza a interface e limpa os campos
            atualizarLista();
            tituloField.clear();
            descField.clear();
            
            showAlert("Sucesso", "Board criado com sucesso!");
        });

        // Configuração do botão Editar
        btnEditar.setOnAction(e -> {
            // Obtém o índice do board selecionado
            int index = boardListView.getSelectionModel().getSelectedIndex();
            
            // Verifica se um board está selecionado
            if (index < 0) {
                showAlert("Aviso", "Selecione um board para editar.");
                return;
            }
            
            // Verifica se os campos obrigatórios estão preenchidos
            if (tituloField.getText().trim().isEmpty() || descField.getText().trim().isEmpty()) {
                showAlert("Erro", "Preencha o título e a descrição para editar o board.");
                return;
            }
            
            // Obtém o board selecionado
            Board b = getBoardDaLista(index);
            if (b == null) {
                showAlert("Erro", "Board não encontrado na lista completa.");
                return;
            }
            
            // Atualiza os dados do board
            b.setTitulo(tituloField.getText());
            b.setDescricao(descField.getText());
            
            // Atualiza o projeto associado ao board
            String projetoId = obterProjetoIdDoComboBox(projetoComboBox);
            b.setProjetoId(projetoId);
            
            // Salva a lista atualizada
            BoardFileHandler.salvarTodos(boards);
            
            // Atualiza a interface
            atualizarLista();
            
            showAlert("Sucesso", "Board atualizado com sucesso!");
        });

        // Configuração do botão Excluir
        btnExcluir.setOnAction(e -> {
            // Obtém o índice do board selecionado
            int index = boardListView.getSelectionModel().getSelectedIndex();
            
            // Verifica se um board está selecionado
            if (index < 0) {
                showAlert("Aviso", "Selecione um board para excluir.");
                return;
            }
            
            // Obtém o board selecionado
            Board b = getBoardDaLista(index);
            if (b == null) {
                showAlert("Erro", "Board não encontrado na lista completa.");
                return;
            }
            
            // Confirma a exclusão com o usuário
            Alert confirmacao = new Alert(Alert.AlertType.CONFIRMATION);
            confirmacao.setTitle("Confirmar Exclusão");
            confirmacao.setHeaderText("Excluir Board");
            confirmacao.setContentText("Tem certeza que deseja excluir o board '" + b.getTitulo() + "'?");
            
            // Se o usuário confirmar, exclui o board
            if (confirmacao.showAndWait().get() == ButtonType.OK) {
                // Remove o board da lista completa
                boards.remove(b);
                
                // Salva a lista atualizada
                BoardFileHandler.salvarTodos(boards);
                
                // Atualiza a interface e limpa os campos
                atualizarLista();
                tituloField.clear();
                descField.clear();
                
                showAlert("Sucesso", "Board excluído com sucesso!");
            }
        });

        // Configuração do botão Voltar
        btnVoltar.setOnAction(e -> {
            // Fecha a tela atual
            stage.close();
            
            // Se temos um projeto selecionado, voltamos para a tela de projetos
            if (projetoSelecionado != null) {
                try {
                    new ProjetoApp().start(new Stage());
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
            // Remove o filtro de projeto
            projetoSelecionado = null;
            
            // Seleciona "Sem projeto" no ComboBox
            projetoComboBox.getSelectionModel().select(0);
            
            // Atualiza a interface
            atualizarLista();
            atualizarLabelProjeto(labelProjeto);
        });
        
        // Configuração do botão Gerenciar Projetos
        btnGerenciarProjetos.setOnAction(e -> {
            // Fecha a tela atual
            stage.close();
            
            // Abre a tela de projetos
            try {
                new ProjetoApp().start(new Stage());
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        // Configuração do botão Ver Tasks
        btnVerTasks.setOnAction(e -> {
            // Obtém o índice do board selecionado
            int index = boardListView.getSelectionModel().getSelectedIndex();

            // Verifica se um board está selecionado
            if (index < 0) {
                showAlert("Aviso", "Selecione um board para ver seus Tasks.");
                return;
            }

            // Obtém o board selecionado
            Board boardSelecionado = boards.get(index);

            // Fecha a tela atual
            stage.close();

            // Abre a tela de Tasks filtrada para este board
            try {
                // Criamos uma instância de TaskApp e passamos o ID do board como parâmetro
                TaskApp TaskApp = new TaskApp();
                // Definimos qual board está selecionado antes de abrir a tela
                TaskApp.setboardSelecionado(boardSelecionado.getId());
                TaskApp.start(new Stage());
            } catch (Exception ex) {
                ex.printStackTrace();
                showAlert("Erro", "Não foi possível abrir a tela de Tasks.");
            }
        });

        // Adiciona um listener para quando um board é selecionado na lista
        boardListView.getSelectionModel().selectedIndexProperty().addListener((obs, oldVal, newVal) -> {
            int i = newVal.intValue();
            if (i >= 0) {
                // Obtém o board selecionado
                Board b = getBoardDaLista(i);
                if (b != null) {
                    // Preenche os campos com os dados do board
                    tituloField.setText(b.getTitulo());
                    descField.setText(b.getDescricao());
                    
                    // Seleciona o projeto do board no ComboBox (se houver)
                    if (b.getProjetoId() != null) {
                        for (int j = 0; j < projetoComboBox.getItems().size(); j++) {
                            String item = projetoComboBox.getItems().get(j);
                            if (item.startsWith(b.getProjetoId())) {
                                projetoComboBox.getSelectionModel().select(j);
                                break;
                            }
                        }
                    } else {
                        // Se não tiver projeto, seleciona "Sem projeto"
                        projetoComboBox.getSelectionModel().select(0);
                    }
                }
            }
        });

        // Criação do layout da interface
        VBox layout = new VBox(10);
        layout.setPadding(new Insets(15));
        
        // NOVO: HBox para mostrar o projeto selecionado e o botão para limpar o filtro
        HBox projetoFilterBox = new HBox(10, labelProjeto, btnLimparFiltro);
        
        // Adiciona os componentes ao layout
        layout.getChildren().addAll(
                new Label("Boards:"),
                // Se temos um projeto selecionado, mostramos a informação
                projetoFilterBox,
                boardListView,
                new Label("Título:"),
                tituloField,
                new Label("Descrição:"),
                descField,
                new Label("Projeto:"),
                projetoComboBox,
                new HBox(10, btnCriar, btnEditar, btnExcluir),
                new HBox(10, btnVoltar, btnGerenciarProjetos)
        );

        // Configura a cena e o stage
        Scene scene = new Scene(layout, 500, 600);
        stage.setScene(scene);
        stage.setTitle("Gerenciar Boards");
        stage.show();
    }

    /**
     * Atualiza a lista de boards na interface gráfica.
     * Se um projeto estiver selecionado, filtra os boards desse projeto.
     */
    private void atualizarLista() {
        // Recarrega a lista completa de boards
        boards = BoardFileHandler.carregar();
        
        // Limpa a lista visual
        boardListView.getItems().clear();
        
        // Lista filtrada de boards (todos ou apenas de um projeto)
        List<Board> boardsFiltrados;
        
        // Se temos um projeto selecionado, filtramos os boards desse projeto
        if (projetoSelecionado != null) {
            boardsFiltrados = boards.stream()
                              .filter(b -> b.getProjetoId() != null && 
                                      b.getProjetoId().equals(projetoSelecionado))
                              .collect(Collectors.toList());
        } else {
            // Caso contrário, mostramos todos os boards
            boardsFiltrados = boards;
        }
        
        // Adiciona cada board à lista visual
        for (Board b : boardsFiltrados) {
            // Se o board tem um projeto associado, mostramos o nome do projeto
            String projetoNome = "";
            if (b.getProjetoId() != null) {
                Projeto p = ProjetoFileHandler.buscarPorId(b.getProjetoId());
                if (p != null) {
                    projetoNome = " [Projeto: " + p.getNome() + "]";
                }
            }
            
            boardListView.getItems().add("📌 " + b.getTitulo() + " - " + b.getDescricao() + projetoNome);
        }
    }
    
    /**
     * Obtém o board completo a partir do índice na lista filtrada.
     * 
     * @param indexNaListaVisual Índice do board na lista visual
     * @return O board correspondente ou null se não encontrado
     */
    private Board getBoardDaLista(int indexNaListaVisual) {
        // Se não há boards na lista ou o índice é inválido, retorna null
        if (boards.isEmpty() || indexNaListaVisual < 0 || 
            indexNaListaVisual >= boardListView.getItems().size()) {
            return null;
        }
        
        // Lista filtrada de boards (todos ou apenas de um projeto)
        List<Board> boardsFiltrados;
        
        // Se temos um projeto selecionado, filtramos os boards desse projeto
        if (projetoSelecionado != null) {
            boardsFiltrados = boards.stream()
                              .filter(b -> b.getProjetoId() != null && 
                                      b.getProjetoId().equals(projetoSelecionado))
                              .collect(Collectors.toList());
        } else {
            // Caso contrário, usamos todos os boards
            boardsFiltrados = boards;
        }
        
        // Verifica se o índice é válido na lista filtrada
        if (indexNaListaVisual < boardsFiltrados.size()) {
            return boardsFiltrados.get(indexNaListaVisual);
        }
        
        return null;
    }
    
    /**
     * Obtém o ID do projeto selecionado no ComboBox.
     * 
     * @param comboBox ComboBox com os projetos
     * @return ID do projeto selecionado ou null se "Sem projeto" estiver selecionado
     */
    private String obterProjetoIdDoComboBox(ComboBox<String> comboBox) {
        String selecionado = comboBox.getValue();
        
        // Se "Sem projeto" estiver selecionado, retorna null
        if (selecionado == null || selecionado.equals("[Sem projeto]")) {
            return null;
        }
        
        // Extrai o ID do projeto do formato "ID - Nome"
        return selecionado.split(" - ")[0];
    }
    
    /**
     * Atualiza o label que mostra o projeto selecionado.
     * 
     * @param label Label a ser atualizado
     */
    private void atualizarLabelProjeto(Label label) {
        if (projetoSelecionado != null) {
            Projeto p = ProjetoFileHandler.buscarPorId(projetoSelecionado);
            if (p != null) {
                label.setText("Filtrando boards do projeto: " + p.getNome());
            } else {
                label.setText("Filtrando boards (projeto não encontrado)");
            }
        } else {
            label.setText("Mostrando todos os boards");
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