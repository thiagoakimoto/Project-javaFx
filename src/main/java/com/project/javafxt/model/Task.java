package com.project.javafxt.model; // Diz onde a classe mora

import java.io.Serializable;      // Importa para poder “serialize” (gravar em binário)

/**
 * Classe que representa um “Task” (tarefa) com um título e uma descrição.
 * Ao marcar como Serializable, a gente diz que é possível gravar o objeto inteiro
 * dentro de um arquivo binário (.dat) e ler de volta depois.
 */
public class Task implements Serializable {
    private static final long serialVersionUID = 1L; // Identificador de versão (sempre usamos 1L para começar)

    // Atributos (campos) que guardam o que o tarefa tem: título e descrição
    private String titulo;
    private String descricao;

    // NOVO: ID do board ao qual este Task pertence (null se não estiver associado a nenhum board)
    private String boardId;

    /**
     * Construtor: cria um novo Task e já define “título” e “descrição” de uma vez.
     * @param titulo texto que vai aparecer como nome do tarefa
     * @param descricao texto que explica o conteúdo do tarefa
     */
    public Task(String titulo, String descricao) {
        this.titulo = titulo;       // Guarda o valor passado em “titulo”
        this.descricao = descricao; // Guarda o valor passado em “descricao”
        this.boardId = null;      // Inicialmente, o Task não está associado a nenhum board
    }

    /**
     * Construtor alternativo que já associa o Task a um board
     * @param titulo texto que vai aparecer como nome do tarefa
     * @param descricao texto que explica o conteúdo do tarefa
     * @param boardId ID do board ao qual este Task pertence
     */
    public Task(String titulo, String descricao, String boardId) {
        this.titulo = titulo;
        this.descricao = descricao;
        this.boardId = boardId;
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
     * @return o ID do projeto associado a este Task ou null se não estiver associado
     */
    public String getBoardId() {
        return boardId;
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
     * Associa este Task a um board pelo ID do board.
     * @param boardId ID do board ao qual este Task deve ser associado (null para desassociar)
     */
    public void setBoardId(String boardId) {
        this.boardId = boardId;
    }
}
