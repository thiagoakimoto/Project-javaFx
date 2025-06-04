package com.project.javafxt.model;

/* Classe que representa um usuário com nome, email e senha
Ou seja, é a ficha de cadastramento

UsuarioFileHandler, carrega os usuarios, usando o método carregar,  e esse méteodo usa o método
toString,   para criar um objetos Usuario, a partir das strings lidas.

Ou seja a classe Usuario serve para ser instanciada. Objeto são criados a partir dele.

*/
public class Usuario {
    private String nome, email, senha; // Atributos do usuário

    // Construtor que inicializa os atributos do usuário
    public Usuario(String nome, String email, String senha) {
        this.nome = nome; // Inicializa o nome
        this.email = email; // Inicializa o email
        this.senha = senha; // Inicializa a senha
    }

    // Método para obter o nome do usuário
    public String getNome() {
        return nome;
    }

    // Método para obter o email do usuário
    public String getEmail() {
        return email;
    }

    // Método para obter a senha do usuário
    public String getSenha() {
        return senha;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public void setSenha(String senha) {
        this.senha = senha;
    }



    // Método que retorna uma representação em String do usuário
    @Override
    public String toString() {
        return nome + ";" + email + ";" + senha; // Formato: nome;email;senha
    }

    // Método estático que cria um usuário a partir de uma String
    public static Usuario fromString(String linha) {
        String[] partes = linha.split(";"); // Divide a linha em partes
        return new Usuario(partes[0], partes[1], partes[2]); // Cria um novo usuário
    }

}
