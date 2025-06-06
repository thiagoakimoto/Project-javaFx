package com.project.javafxt.model; // Diz onde a classe mora

import java.io.Serializable;      // Importa para poder “serialize” (gravar em binário)

/**
 * Classe que representa um “Board” (quadro) com um título e uma descrição.
 * Ao marcar como Serializable, a gente diz que é possível gravar o objeto inteiro
 * dentro de um arquivo binário (.dat) e ler de volta depois.
 */
public class Board implements Serializable {
    private static final long serialVersionUID = 1L; // Identificador de versão (sempre usamos 1L para começar)

    // Atributos (campos) que guardam o que o quadro tem: título e descrição
    private String titulo;
    private String descricao;
    
    // NOVO: ID do projeto ao qual este board pertence (null se não estiver associado a nenhum projeto)
    private String projetoId;

    /**
     * Construtor: cria um novo Board e já define “título” e “descrição” de uma vez.
     * @param titulo texto que vai aparecer como nome do quadro
     * @param descricao texto que explica o conteúdo do quadro
     */
    public Board(String titulo, String descricao) {
        this.titulo = titulo;       // Guarda o valor passado em “titulo”
        this.descricao = descricao; // Guarda o valor passado em “descricao”
        this.projetoId = null;      // Inicialmente, o board não está associado a nenhum projeto
    }
    
    /**
     * Construtor alternativo que já associa o board a um projeto
     * @param titulo texto que vai aparecer como nome do quadro
     * @param descricao texto que explica o conteúdo do quadro
     * @param projetoId ID do projeto ao qual este board pertence
     */
    public Board(String titulo, String descricao, String projetoId) {
        this.titulo = titulo;
        this.descricao = descricao;
        this.projetoId = projetoId;
    }

    // GETTERS: métodos para ler (pegar) o que está dentro de titulo e descricao

    /**
     * @return o texto que foi guardado em “titulo”
     */
    public String getTitulo() {
        return titulo;
    }

    /**
     * @return o texto que foi guardado em “descricao”
     */
    public String getDescricao() {
        return descricao;
    }
    
    /**
     * @return o ID do projeto associado a este board ou null se não estiver associado
     */
    public String getProjetoId() {
        return projetoId;
    }

    // SETTERS: métodos para alterar (mudar) o que está dentro de titulo e descricao

    /**
     * Altera o texto guardado em “titulo” para este novo valor.
     * @param titulo novo título que você quer colocar
     */
    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    /**
     * Altera o texto guardado em “descricao” para este novo valor.
     * @param descricao nova descrição que você quer colocar
     */
    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }
    
    /**
     * Associa este board a um projeto pelo ID do projeto.
     * @param projetoId ID do projeto ao qual este board deve ser associado (null para desassociar)
     */
    public void setProjetoId(String projetoId) {
        this.projetoId = projetoId;
    }
}
