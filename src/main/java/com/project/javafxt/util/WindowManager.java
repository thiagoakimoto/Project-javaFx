package com.project.javafxt.util;

import javafx.stage.Stage;

/**
 * Classe utilitária para gerenciar o estado das janelas (windows) entre transições.
 * Permite preservar o tamanho, posição e estado de maximização ou tela cheia.
 */
public class WindowManager {
    
    // Propriedades estáticas para manter o estado da janela
    private static double width = 700;
    private static double height = 700;
    private static double posX = -1;
    private static double posY = -1;
    private static boolean isMaximized = false;
    private static boolean isFullScreen = false;
    
    /**
     * Captura o estado atual da janela (Stage) para uso posterior.
     * 
     * @param stage O Stage do qual capturar o estado
     */
    public static void saveWindowState(Stage stage) {
        // Somente salva tamanho se não estiver maximizado ou em tela cheia
        if (!stage.isMaximized() && !stage.isFullScreen()) {
            width = stage.getWidth();
            height = stage.getHeight();
            posX = stage.getX();
            posY = stage.getY();
        }
        
        isMaximized = stage.isMaximized();
        isFullScreen = stage.isFullScreen();
    }
    
    /**
     * Aplica o estado salvo da janela a um novo Stage.
     * 
     * @param newStage O Stage ao qual aplicar o estado salvo
     */
    public static void applyWindowState(Stage newStage) {
        // Primeiro definimos o tamanho e posição
        newStage.setWidth(width);
        newStage.setHeight(height);
        
        // Só definimos a posição se tiver sido salva anteriormente
        if (posX >= 0 && posY >= 0) {
            newStage.setX(posX);
            newStage.setY(posY);
        }
        
        // Por fim, aplicamos os estados de maximização e tela cheia
        newStage.setMaximized(isMaximized);
        newStage.setFullScreen(isFullScreen);
    }
}
