package com.project.javafxt.model;

// Aqui estamos dizendo que essa classe pode ser salva e lida de um arquivo.
import java.io.Serializable;

public class Usuario implements Serializable {
    // Isso é como um número de identificação para saber se a versão do arquivo é a mesma.
    private static final long serialVersionUID = 1L;

    // Aqui estamos criando três atributos que cada usuário vai ter: nome, email e senha.
    private String nome;
    private String email;
    private String senha;

    // Este é o "construtor", que é como uma receita para fazer um novo usuário.
    public Usuario(String nome, String email, String senha) {
        this.nome = nome; // Guardamos o nome do usuário.
        this.email = email; // Guardamos o email do usuário.
        this.senha = senha; // Guardamos a senha do usuário.
    }

    // Aqui estão as "ferramentas" para pegar as informações do usuário.
    public String getNome()  { return nome; }
    public String getEmail() { return email; }
    public String getSenha() { return senha; }

    // E aqui estão as "ferramentas" para mudar as informações do usuário.
    public void setNome(String nome)   { this.nome = nome; }
    public void setEmail(String email) { this.email = email; }
    public void setSenha(String senha) { this.senha = senha; }

    // Este método ajuda a mostrar as informações do usuário de um jeito fácil.
    @Override
    public String toString() {
        return "Usuario{nome='" + nome + "', email='" + email + "', senha='" + senha + "'}";
    }
}
