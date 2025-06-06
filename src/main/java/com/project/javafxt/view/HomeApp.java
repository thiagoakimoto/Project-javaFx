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

// IMPORTAÇÃO da tela BoardApp
import com.project.javafxt.view.BoardApp;
// IMPORTAÇÃO da tela ProjetoApp
import com.project.javafxt.view.ProjetoApp;

public class HomeApp extends Application {

    private ListView<String> listaView = new ListView<>();
    private List<Usuario> usuarios = UsuarioFileHandler.carregar();

    @Override
    public void start(Stage stage) {
        atualizarLista();

        TextField nomeField = new TextField();
        TextField emailField = new TextField();
        PasswordField senhaField = new PasswordField();

        Button btnEditar = new Button("Editar");
        Button btnExcluir = new Button("Excluir");
        Button btnVoltar = new Button("Voltar");
        Button btnBoards = new Button("Gerenciar Boards");
        // NOVO botão para acessar a tela de projetos
        Button btnProjetos = new Button("Gerenciar Projetos");

        // Editar
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

        // Excluir
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
            stage.close();
            new LoginApp().start(new Stage());
        });

        // Abrir CRUD de Boards
        btnBoards.setOnAction(e -> {
            stage.close();
            try {
                new BoardApp().start(new Stage());
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
        
        // NOVA ação: Abrir CRUD de Projetos
        btnProjetos.setOnAction(e -> {
            stage.close();
            try {
                new ProjetoApp().start(new Stage());
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        // Preencher campos ao selecionar usuário
        listaView.getSelectionModel().selectedIndexProperty().addListener((obs, oldVal, newVal) -> {
            int index = newVal.intValue();
            if (index >= 0 && index < usuarios.size()) {
                Usuario u = usuarios.get(index);
                nomeField.setText(u.getNome());
                emailField.setText(u.getEmail());
                senhaField.setText(u.getSenha());
            }
        });

        // Adicionamos o novo botão ao layout
        VBox root = new VBox(10,
                new Label("Usuários Cadastrados"), listaView,
                new Label("Nome:"), nomeField,
                new Label("Email:"), emailField,
                new Label("Senha:"), senhaField,
                new HBox(10, btnEditar, btnExcluir, btnVoltar),
                new HBox(10, btnBoards, btnProjetos) // Adicionamos os botões de gerenciamento
        );
        root.setPadding(new Insets(15));

        stage.setScene(new Scene(root, 500, 500));
        stage.setTitle("Home - Gerenciar Usuários");
        stage.show();
    }

    private void atualizarLista() {
        usuarios = UsuarioFileHandler.carregar();
        listaView.getItems().clear();
        for (Usuario u : usuarios) {
            listaView.getItems().add("Nome: " + u.getNome() + " | Email: " + u.getEmail());
        }
    }
}
