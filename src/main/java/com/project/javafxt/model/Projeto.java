package com.project.javafxt.model;

import java.io.Serializable;
import java.util.UUID;

/**
 * Classe que representa um Projeto no sistema.
 * Um projeto pode conter vários boards e serve para organizar atividades relacionadas.
 * Esta classe implementa Serializable para permitir a persistência em arquivos binários.
 */
public class Projeto implements Serializable {
    // Identificador de versão para serialização
    private static final long serialVersionUID = 1L;
    
    // Atributos do projeto
    private String id; // Identificador único do projeto
    private String nome; // Nome do projeto
    private String descricao; // Descrição do projeto
    
    /**
     * Construtor que cria um novo projeto com um ID único gerado automaticamente.
     * 
     * @param nome Nome do projeto
     * @param descricao Descrição detalhada do projeto
     */
    public Projeto(String nome, String descricao) {
        // Gera um identificador único para cada projeto
        this.id = UUID.randomUUID().toString();
        this.nome = nome;
        this.descricao = descricao;
    }
    
    // Getters e Setters
    
    /**
     * Obtém o ID único do projeto.
     * @return O ID do projeto
     */
    public String getId() {
        return id;
    }
    
    /**
     * Obtém o nome do projeto.
     * @return O nome do projeto
     */
    public String getNome() {
        return nome;
    }
    
    /**
     * Define um novo nome para o projeto.
     * @param nome O novo nome do projeto
     */
    public void setNome(String nome) {
        this.nome = nome;
    }
    
    /**
     * Obtém a descrição do projeto.
     * @return A descrição do projeto
     */
    public String getDescricao() {
        return descricao;
    }
    
    /**
     * Define uma nova descrição para o projeto.
     * @param descricao A nova descrição do projeto
     */
    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }
    
    /**
     * Representação textual do projeto.
     * @return String com informações do projeto
     */
    @Override
    public String toString() {
        return "Projeto{id='" + id + "', nome='" + nome + "', descricao='" + descricao + "'}";
    }
}
