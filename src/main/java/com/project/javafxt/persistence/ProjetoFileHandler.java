package com.project.javafxt.persistence;

import com.project.javafxt.model.Projeto;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe responsável por gerenciar a persistência dos projetos em arquivo.
 * Esta classe utiliza serialização de objetos para salvar e carregar projetos
 * de um arquivo binário.
 */
public class ProjetoFileHandler {
    // Nome do arquivo onde os projetos serão armazenados
    private static final String ARQUIVO = "projetos.dat";
    
    /**
     * Salva um projeto individual adicionando-o à lista existente.
     * 
     * @param projeto O projeto a ser salvo
     */
    public static void salvar(Projeto projeto) {
        // Carrega a lista atual de projetos
        List<Projeto> lista = carregar();
        
        // Adiciona o novo projeto à lista
        lista.add(projeto);
        
        // Salva a lista atualizada
        salvarTodos(lista);
    }
    
    /**
     * Carrega todos os projetos do arquivo.
     * Se o arquivo não existir ou estiver vazio, retorna uma lista vazia.
     * 
     * @return Lista de projetos carregados do arquivo
     */
    public static List<Projeto> carregar() {
        List<Projeto> lista = new ArrayList<>();
        
        // Tenta abrir e ler o arquivo de projetos
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(ARQUIVO))) {
            // Lê a lista de projetos do arquivo
            Object obj = ois.readObject();
            
            // Converte o objeto lido para uma lista de projetos
            lista = (List<Projeto>) obj;
        } catch (IOException | ClassNotFoundException e) {
            // Se ocorrer erro (arquivo não existe, está vazio ou há problemas de compatibilidade)
            // retorna a lista vazia inicializada anteriormente
        }
        
        return lista;
    }
    
    /**
     * Salva toda a lista de projetos no arquivo.
     * 
     * @param lista A lista completa de projetos a ser salva
     */
    public static void salvarTodos(List<Projeto> lista) {
        // Tenta abrir o arquivo para escrita
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(ARQUIVO))) {
            // Salva a lista de projetos inteira no arquivo
            oos.writeObject(lista);
        } catch (IOException e) {
            // Imprime a pilha de erros se ocorrer falha na gravação
            e.printStackTrace();
        }
    }
    
    /**
     * Busca um projeto pelo seu ID.
     * 
     * @param id O ID do projeto a ser encontrado
     * @return O projeto encontrado ou null se não encontrar
     */
    public static Projeto buscarPorId(String id) {
        // Carrega todos os projetos
        List<Projeto> projetos = carregar();
        
        // Procura o projeto com o ID especificado
        for (Projeto projeto : projetos) {
            if (projeto.getId().equals(id)) {
                return projeto;
            }
        }
        
        // Retorna null se não encontrar o projeto
        return null;
    }
}
