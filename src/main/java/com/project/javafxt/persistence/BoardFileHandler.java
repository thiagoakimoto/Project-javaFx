package com.project.javafxt.persistence; // Diz onde a classe mora

import com.project.javafxt.model.Board;   // Importa o modelo Board
import java.io.*;                         // Importa tudo para ler e escrever em arquivos
import java.util.ArrayList;               // Importa a lista
import java.util.List;                    // Importa o tipo List

/**
 * BoardFileHandler cuida de guardar e buscar “Boards” (quadros)
 * num arquivo binário chamado "boards.dat".
 * Cada vez que você criar, editar ou excluir boards, ele lê/escreve esse arquivo.
 */
public class BoardFileHandler {
    // “ARQUIVO” é o nome do arquivo onde vamos salvar/ler os Boards em binário
    private static final String ARQUIVO = "boards.dat";

    /**
     * Salva (adiciona) um único Board ao final do arquivo.
     * Em binário, não dá pra “apendar” direto sem regravar tudo,
     * então a gente sempre carrega lista toda, adiciona o novo no final e salva de novo.
     *
     * @param board o objeto Board que queremos adicionar
     */
    public static void salvar(Board board) {
        // 1) Carrega a lista atual de Boards que já existia
        List<Board> lista = carregar();

        // 2) Adiciona esse novo Board à lista
        lista.add(board);

        // 3) Salva toda a lista novamente no arquivo, sobrescrevendo “boards.dat”
        salvarTodos(lista);
    }

    /**
     * Carrega (lê) a lista completa de Boards que estão guardados no arquivo.
     * Se o arquivo não existir, devolve uma lista vazia (nenhum quadro).
     *
     * @return lista de todos os Boards já salvos (ou vazia se o arquivo não existir)
     */
    public static List<Board> carregar() {
        List<Board> lista = new ArrayList<>(); // Começa com lista vazia

        // Tenta abrir “boards.dat” para ler objetos
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(ARQUIVO))) {
            // Lê o objeto que está dentro, que deve ser uma List<Board>
            Object obj = ois.readObject();

            // Faz cast (transforma) aquele objeto genérico em List<Board>
            lista = (List<Board>) obj;
        } catch (IOException | ClassNotFoundException e) {
            // Se der erro (arquivo não existe ou classe diferente), não faz nada:
            // retorna a lista vazia (significa que ainda não havia Boards gravados).
        }

        return lista; // Devolve a lista (vazia ou com itens lidos)
    }



    /**
     * Salva toda a lista de Boards no arquivo, gravando em binário.
     * Usa ObjectOutputStream para gravar o List<Board> inteiro de uma vez.
     *
     * @param lista a lista completa de Boards que queremos manter no arquivo
     */
    public static void salvarTodos(List<Board> lista) {
        // Tenta abrir “boards.dat” para escrever (sobrescrevendo tudo)
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(ARQUIVO))) {
            // Escreve o objeto “lista” (que é List<Board>) em forma binária
            oos.writeObject(lista);
        } catch (IOException e) {
            // Se der erro ao abrir ou escrever, mostra no console para sabermos o que aconteceu
            e.printStackTrace();
        }
    }

    /**
     * Busca um board pelo seu ID.
     *
     * @param id O ID do board a ser encontrado
     * @return O board encontrado ou null se não encontrar
     */
    public static Board buscarPorId(String id) {
        // Carrega todos os board
        List<Board> boards = carregar();

        // Procura o board com o ID especificado
        for (Board board : boards) {
            if (board.getId().equals(id)) {
                return board;
            }
        }

        // Retorna null se não encontrar o board
        return null;
    }
}
