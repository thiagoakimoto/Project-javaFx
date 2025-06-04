package com.project.javafxt.persistence; // Pacote onde a classe está localizada

import com.project.javafxt.model.Board; // Importa a classe Board, que representa um quadro

import java.io.*; // Importa classes para manipulação de entrada e saída
import java.util.*; // Importa classes para trabalhar com coleções, como listas

// Classe responsável por manipular a persistência dos quadros (boards) em um arquivo
public class BoardFileHandler {
    // Nome do arquivo onde os quadros serão salvos
    private static final String ARQUIVO = "boards.txt";

    // Método para salvar um único quadro no arquivo
    public static void salvar(Board b) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(ARQUIVO, true))) { // Abre o arquivo em modo de adição
            pw.println(b.toString()); // Escreve a representação em string do quadro no arquivo
        } catch (IOException e) { // Captura exceções de entrada/saída
            e.printStackTrace(); // Imprime a pilha de erros no console
        }
    }

    // Método para carregar todos os quadros do arquivo
    public static List<Board> carregar() {
        List<Board> lista = new ArrayList<>(); // Cria uma lista para armazenar os quadros
        try (BufferedReader br = new BufferedReader(new FileReader(ARQUIVO))) { // Abre o arquivo para leitura
            String linha; // Variável para armazenar cada linha lida
            // Lê o arquivo linha por linha
            while ((linha = br.readLine()) != null) {
                Board b = Board.fromString(linha); // Converte a linha em um objeto Board
                if (b != null) lista.add(b); // Adiciona o quadro à lista se não for nulo
            }
        } catch (IOException e) { // Captura exceções de entrada/saída
            // ignora se não existir
        }
        return lista; // Retorna a lista de quadros carregados
    }

    // Método para salvar todos os quadros da lista no arquivo
    public static void salvarTodos(List<Board> lista) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(ARQUIVO))) { // Abre o arquivo para escrita (substitui o conteúdo)
            for (Board b : lista) {
                pw.println(b.toString()); // Escreve cada quadro no arquivo
            }
        } catch (IOException e) { // Captura exceções de entrada/saída
            e.printStackTrace(); // Imprime a pilha de erros no console
        }
    }
}
