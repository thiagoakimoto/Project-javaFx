package com.project.javafxt.view;

import com.project.javafxt.model.Usuario;
import com.project.javafxt.persistence.UsuarioFileHandler;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

/*
Esse é o arquivo que cria a tela do sistema. Campos para preencher, botões,mensagens de alertas, etc
É por onde o usuário pode preencher suas informações, ou seja inserir, nome, email, etc.
Fazer o cadastramento.

Gerencia a lógica dos cliques
*/


// Classe principal da aplicação que estende Application do JavaFX
public class LoginApp extends Application {

    @Override
    public void start(Stage stage) {
        // Campos de entrada para nome, email e senha
        TextField nomeField = new TextField();
        TextField emailField = new TextField();
        PasswordField senhaField = new PasswordField(); 

        // Botões para cadastrar e fazer login
        Button btnCadastrar = new Button("Cadastrar");
        Button btnLogin = new Button("Login");

        // Layout vertical para organizar os componentes
        VBox layout = new VBox(10);
        layout.setPadding(new Insets(20)); // Adiciona espaçamento ao redor do layout
        layout.getChildren().addAll(
                new Label("Nome:"), nomeField,
                new Label("Email:"), emailField,
                new Label("Senha:"), senhaField,
                new HBox(10, btnCadastrar, btnLogin,btnVerUsuarios) // Botões lado a lado
        );

        // Ação para o botão de cadastrar
        btnCadastrar.setOnAction(e -> {
            String nome = nomeField.getText().trim(); // Obtém o nome
            String email = emailField.getText().trim(); // Obtém o email
            String senha = senhaField.getText().trim(); // Obtém a senha

            // Verifica se todos os campos estão preenchidos
            if (nome.isEmpty() || email.isEmpty() || senha.isEmpty()) {
                showAlert("Erro", "Preencha todos os campos."); // Exibe alerta de erro
            } else {
                Usuario novo = new Usuario(nome, email, senha); // Cria um novo usuário
                UsuarioFileHandler.salvar(novo); // Salva o usuário no arquivo
                showAlert("Sucesso", "Usuário cadastrado!"); // Exibe alerta de sucesso
                nomeField.clear(); emailField.clear(); senhaField.clear(); // Limpa os campos
            }
        });

        // Ação para o botão de login, que encaminha para a página home
        btnLogin.setOnAction(e -> {
            String email = emailField.getText().trim();
            String senha = senhaField.getText().trim();
            Usuario u = UsuarioFileHandler.autenticar(email, senha);

            if (u != null) {
                stage.close();
                try {
                    new HomeApp().start(new Stage());
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            } else {
                showAlert("Erro", "Email ou senha incorretos.");
            }
        });

        //botão para ver os usuários cadastrados
        btnVerUsuarios.setOnAction(e -> {
            stage.close(); // fecha a tela atual
            try {
                new ListaUsuariosApp().start(new Stage());
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });


        // Exibir a cena
        Scene scene = new Scene(layout, 300, 300); // Cria a cena com o layout
        stage.setScene(scene); // Define a cena no estágio
        stage.setTitle("Login e Cadastro"); // Define o título da janela
        stage.show(); // Exibe a janela
    }

    // Método para exibir alertas
    private void showAlert(String titulo, String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION); // Cria um alerta informativo
        alert.setTitle(titulo); // Define o título do alerta
        alert.setContentText(msg); // Define o texto do alerta
        alert.showAndWait(); // Exibe o alerta e espera o usuário fechar
    }

    // Método principal que inicia a aplicação
    public static void main(String[] args) {
        launch(); // Lança a aplicação JavaFX
    }

    Button btnVerUsuarios = new Button("Ver usuários");

}
