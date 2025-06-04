package com.project.javafxt.persistence;

import com.project.javafxt.model.Usuario;

import java.io.*;
import java.util.*;

/*
Basicamente ele salva os dados de um usuário, carrega eles e realiza a autenticação.


Classe responsável por manipular arquivos de usuários
 OU seja, recebe a ficha de cadastramento e guarda ela.
 Para depois buscar as informações do usuário. No arquivo txt

*/

public class UsuarioFileHandler {
    private static final String ARQUIVO = "usuarios.txt"; // Nome do arquivo onde os usuários serão salvos

    // Método para salvar um usuário no arquivo
    public static void salvar(Usuario u) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(ARQUIVO, true))) { // Abre o arquivo em modo append
            pw.println(u.toString()); // Escreve a representação do usuário no arquivo
        } catch (IOException e) {
            e.printStackTrace(); // Trata exceções de IO
        }
    }

    // Método para carregar todos os usuários do arquivo
    public static List<Usuario> carregar() {
        List<Usuario> lista = new ArrayList<>(); // Lista para armazenar os usuários
        try (BufferedReader br = new BufferedReader(new FileReader(ARQUIVO))) { // Lê o arquivo
            String linha;
            while ((linha = br.readLine()) != null) { // Enquanto houver linhas
                lista.add(Usuario.fromString(linha)); // Adiciona o usuário à lista
            }
        } catch (IOException e) {
            // Arquivo não existe ainda, não faz nada
        }
        return lista; // Retorna a lista de usuários
    }

    // Método para autenticar um usuário com email e senha
    public static Usuario autenticar(String email, String senha) {
        for (Usuario u : carregar()) { // Carrega os usuários e itera sobre eles
            if (u.getEmail().equals(email) && u.getSenha().equals(senha)) { // Verifica se email e senha estão corretos
                return u; // Retorna o usuário autenticado
            }
        }
        return null; // Retorna null se não encontrar o usuário
    }

    public static void salvarTodos(List<Usuario> lista) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(ARQUIVO))) {
            for (Usuario u : lista) {
                pw.println(u.toString());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
