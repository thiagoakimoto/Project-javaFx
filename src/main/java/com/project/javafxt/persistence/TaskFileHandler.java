package com.project.javafxt.persistence; // Diz onde a classe mora

import com.project.javafxt.model.Task;   // Importa o modelo Task
import java.io.*;                         // Importa tudo para ler e escrever em arquivos
import java.util.ArrayList;               // Importa a lista
import java.util.List;                    // Importa o tipo List

/**
 * TaskFileHandler cuida de guardar e buscar “Tasks” (tarefas)
 * num arquivo binário chamado "Tasks.dat".
 * Cada vez que você criar, editar ou excluir Tasks, ele lê/escreve esse arquivo.
 */
public class TaskFileHandler {
    // “ARQUIVO” é o nome do arquivo onde vamos salvar/ler os Tasks em binário
    private static final String ARQUIVO = "Tasks.dat";

    /**
     * Salva (adiciona) um único Task ao final do arquivo.
     * Em binário, não dá pra “apendar” direto sem regravar tudo,
     * então a gente sempre carrega lista toda, adiciona o novo no final e salva de novo.
     *
     * @param Task o objeto Task que queremos adicionar
     */
    public static void salvar(Task Task) {
        // 1) Carrega a lista atual de Tasks que já existia
        List<Task> lista = carregar();

        // 2) Adiciona esse novo Task à lista
        lista.add(Task);

        // 3) Salva toda a lista novamente no arquivo, sobrescrevendo “Tasks.dat”
        salvarTodos(lista);
    }

    /**
     * Carrega (lê) a lista completa de Tasks que estão guardados no arquivo.
     * Se o arquivo não existir, devolve uma lista vazia (nenhum tarefa).
     *
     * @return lista de todos os Tasks já salvos (ou vazia se o arquivo não existir)
     */
    public static List<Task> carregar() {
        List<Task> lista = new ArrayList<>(); // Começa com lista vazia

        // Tenta abrir “Tasks.dat” para ler objetos
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(ARQUIVO))) {
            // Lê o objeto que está dentro, que deve ser uma List<Task>
            Object obj = ois.readObject();

            // Faz cast (transforma) aquele objeto genérico em List<Task>
            lista = (List<Task>) obj;
        } catch (IOException | ClassNotFoundException e) {
            // Se der erro (arquivo não existe ou classe diferente), não faz nada:
            // retorna a lista vazia (significa que ainda não havia Tasks gravados).
        }

        return lista; // Devolve a lista (vazia ou com itens lidos)
    }

    /**
     * Salva toda a lista de Tasks no arquivo, gravando em binário.
     * Usa ObjectOutputStream para gravar o List<Task> inteiro de uma vez.
     *
     * @param lista a lista completa de Tasks que queremos manter no arquivo
     */
    public static void salvarTodos(List<Task> lista) {
        // Tenta abrir “Tasks.dat” para escrever (sobrescrevendo tudo)
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(ARQUIVO))) {
            // Escreve o objeto “lista” (que é List<Task>) em forma binária
            oos.writeObject(lista);
        } catch (IOException e) {
            // Se der erro ao abrir ou escrever, mostra no console para sabermos o que aconteceu
            e.printStackTrace();
        }
    }
}
