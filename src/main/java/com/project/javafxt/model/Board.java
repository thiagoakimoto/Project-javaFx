package com.project.javafxt.model; // Pacote onde a classe está localizada

// Classe que representa um "Board" (quadro) com um título e uma descrição
public class Board {
    // Atributos que armazenam o título e a descrição do quadro
    private String titulo; // Armazena o título do quadro
    private String descricao; // Armazena a descrição do quadro

    // Construtor da classe, que inicializa os atributos com os valores fornecidos
    public Board(String titulo, String descricao) {
        this.titulo = titulo; // Define o título do quadro
        this.descricao = descricao; // Define a descrição do quadro
    }

    // Método para obter o título do quadro
    public String getTitulo() {
        return titulo; // Retorna o título
    }

    // Método para obter a descrição do quadro
    public String getDescricao() {
        return descricao; // Retorna a descrição
    }

    // Método para definir um novo título para o quadro
    public void setTitulo(String titulo) {
        this.titulo = titulo; // Atualiza o título
    }

    // Método para definir uma nova descrição para o quadro
    public void setDescricao(String descricao) {
        this.descricao = descricao; // Atualiza a descrição
    }

    // Método que retorna uma representação em string do quadro
    @Override
    public String toString() {
        return titulo + ";" + descricao; // Retorna o título e a descrição separados por ponto e vírgula
    }

    // Método estático que cria um objeto Board a partir de uma string
    public static Board fromString(String linha) {
        String[] partes = linha.split(";"); // Divide a string em partes usando o ponto e vírgula como delimitador
        if (partes.length < 2) return null; // Verifica se a string contém pelo menos duas partes
        return new Board(partes[0], partes[1]); // Cria e retorna um novo objeto Board com o título e a descrição
    }
}
