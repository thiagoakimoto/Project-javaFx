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
import javafx.scene.image.ImageView;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;

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
        // Define CSS styling for modern look
        String css = 
            "* {\n" +
            "    -fx-font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;\n" +
            "}\n" +
            ".root {\n" +
            "    -fx-background-color: linear-gradient(to bottom right, #6a11cb, #2575fc);\n" +
            "}\n" +
            ".login-card {\n" +
            "    -fx-background-color: white;\n" +
            "    -fx-background-radius: 8;\n" +
            "    -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.3), 15, 0, 0, 5);\n" +
            "}\n" +
            ".app-title {\n" +
            "    -fx-font-size: 24px;\n" +
            "    -fx-font-weight: bold;\n" +
            "    -fx-text-fill: #2575fc;\n" +
            "    -fx-alignment: center;\n" +
            "}\n" +
            ".field-label {\n" +
            "    -fx-font-size: 13px;\n" +
            "    -fx-text-fill: #555;\n" +
            "}\n" +
            ".text-field, .password-field {\n" +
            "    -fx-background-color: #f5f5f7;\n" +
            "    -fx-border-color: #e0e0e0;\n" +
            "    -fx-border-radius: 4;\n" +
            "    -fx-background-radius: 4;\n" +
            "    -fx-padding: 10;\n" +
            "    -fx-font-size: 14px;\n" +
            "}\n" +
            ".text-field:focused, .password-field:focused {\n" +
            "    -fx-border-color: #2575fc;\n" +
            "}\n" +
            ".button {\n" +
            "    -fx-background-color: #2575fc;\n" +
            "    -fx-text-fill: white;\n" +
            "    -fx-font-weight: bold;\n" +
            "    -fx-font-size: 14px;\n" +
            "    -fx-background-radius: 4;\n" +
            "    -fx-padding: 10 20;\n" +
            "    -fx-cursor: hand;\n" +
            "}\n" +
            ".button:hover {\n" +
            "    -fx-background-color: #1e63d0;\n" +
            "}\n" +
            ".button.secondary {\n" +
            "    -fx-background-color: #6c757d;\n" +
            "}\n" +
            ".button.secondary:hover {\n" +
            "    -fx-background-color: #5a6268;\n" +
            "}\n";

        // Main container centered
        BorderPane borderPane = new BorderPane();
        
        // Login container with centered card
        StackPane centerPane = new StackPane();
        centerPane.setPadding(new Insets(20));
        
        // Card container for login form
        VBox loginCard = new VBox(15);
        loginCard.getStyleClass().add("login-card");
        loginCard.setPadding(new Insets(30));
        loginCard.setMaxWidth(400);
        loginCard.setAlignment(Pos.CENTER);

        // App title
        Label titleLabel = new Label("Task Manager");
        titleLabel.getStyleClass().add("app-title");
        titleLabel.setMaxWidth(Double.MAX_VALUE);
        
        // Form fields
        TextField nomeField = new TextField();
        nomeField.setPromptText("Digite seu nome");
        nomeField.setMaxWidth(Double.MAX_VALUE);
        
        TextField emailField = new TextField();
        emailField.setPromptText("Digite seu email");
        emailField.setMaxWidth(Double.MAX_VALUE);
        
        PasswordField senhaField = new PasswordField();
        senhaField.setPromptText("Digite sua senha");
        senhaField.setMaxWidth(Double.MAX_VALUE);

        // Buttons with style
        Button btnCadastrar = new Button("Cadastrar");
        btnCadastrar.setMaxWidth(Double.MAX_VALUE);
        
        Button btnLogin = new Button("Login");
        btnLogin.setMaxWidth(Double.MAX_VALUE);
        
        Button btnVerUsuarios = new Button("Ver usuários");
        btnVerUsuarios.getStyleClass().add("secondary");
        btnVerUsuarios.setMaxWidth(Double.MAX_VALUE);

        // Add form fields with improved layout
        loginCard.getChildren().addAll(
                titleLabel,
                new Separator(),
                new Label("Nome:"), nomeField,
                new Label("Email:"), emailField,
                new Label("Senha:"), senhaField,
                new Separator(),
                btnCadastrar, 
                btnLogin,
                btnVerUsuarios
        );

        // Improve styling for labels
        loginCard.getChildren().stream()
            .filter(node -> node instanceof Label && !(node.equals(titleLabel)))
            .forEach(label -> label.getStyleClass().add("field-label"));

        // Add the login card to the center pane
        centerPane.getChildren().add(loginCard);
        
        // Set the center pane to the border pane
        borderPane.setCenter(centerPane);
        
        // Keep existing functionality
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
                // Salvar o estado da janela atual antes de fechá-la
                WindowManager.saveWindowState(stage);
                stage.close();
                
                try {
                    Stage newStage = new Stage();
                    HomeApp homeApp = new HomeApp();
                    homeApp.start(newStage);
                    
                    // Aplicar o estado da janela anterior à nova janela
                    WindowManager.applyWindowState(newStage);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            } else {
                showAlert("Erro", "Email ou senha incorretos.");
            }
        });

        //botão para ver os usuários cadastrados
        btnVerUsuarios.setOnAction(e -> {
            // Salvar o estado da janela atual antes de fechá-la
            WindowManager.saveWindowState(stage);
            stage.close();
            
            try {
                Stage newStage = new Stage();
                ListaUsuariosApp listaApp = new ListaUsuariosApp();
                listaApp.start(newStage);
                
                // Aplicar o estado da janela anterior à nova janela
                WindowManager.applyWindowState(newStage);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        // Set up the scene with styling
        Scene scene = new Scene(borderPane, 450, 600);
        scene.getStylesheets().add("data:text/css," + css.replace("\n", ""));
        
        stage.setScene(scene);
        stage.setTitle("Login e Cadastro");
        stage.show();
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
}
