package com.project.javafxt.persistence;

// Aqui estamos dizendo que vamos usar a classe Usuario que criamos antes.
import com.project.javafxt.model.Usuario;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class UsuarioFileHandler {
    // Aqui estamos dizendo que vamos guardar os usuários em um arquivo chamado "usuarios.dat".
    private static final String ARQUIVO = "usuarios.dat";

    // Este método salva um novo usuário no arquivo.
    public static void salvar(Usuario usuario) {
        List<Usuario> lista = carregar(); // Primeiro, carregamos a lista de usuários que já estão salvos.
        lista.add(usuario); // Depois, adicionamos o novo usuário a essa lista.
        salvarTodos(lista); // E finalmente, salvamos todos os usuários de volta no arquivo.
    }

    // Este método carrega todos os usuários do arquivo.
    public static List<Usuario> carregar() {
        List<Usuario> lista = new ArrayList<>(); // Começamos com uma lista vazia.
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(ARQUIVO))) {
            // Tentamos ler a lista de usuários do arquivo.
            lista = (List<Usuario>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            // Se não der, não tem problema, só voltamos com a lista vazia.
        }
        return lista; // Retornamos a lista de usuários.
    }

    // Este método salva todos os usuários de uma vez no arquivo.
    public static void salvarTodos(List<Usuario> lista) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(ARQUIVO))) {
            // Tentamos escrever a lista de usuários no arquivo.
            oos.writeObject(lista);
        } catch (IOException e) {
            e.printStackTrace(); // Se der erro, mostramos o erro.
        }
    }

    // Este método verifica se o email e a senha estão corretos para autenticar o usuário.
    public static Usuario autenticar(String email, String senha) {
        for (Usuario u : carregar()) { // Para cada usuário que carregamos do arquivo...
            // Se o email e a senha estiverem certos, retornamos esse usuário.
            if (u.getEmail().equals(email) && u.getSenha().equals(senha)) {
                return u;
            }
        }
        return null; // Se não encontrar, retornamos nulo (nada).
    }
}
