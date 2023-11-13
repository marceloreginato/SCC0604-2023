package Modelo.Fases;

import java.awt.Image;
import java.awt.Toolkit;
import java.awt.Graphics;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import Auxiliar.Consts;
import Auxiliar.Desenho;
import Controler.Tela;
import Modelo.Hero;

public class Janela{
    String sNomeBackground;

    public Janela(String sNomeBackgorund){
        this.sNomeBackground = sNomeBackgorund;

        try {   
            Image backgroundImage = Toolkit.getDefaultToolkit().getImage(new java.io.File("..").getCanonicalPath() + Consts.PATH_TELA + sNomeBackgorund);
            Desenho.acessoATelaDoJogo().getGraphicsBuffer().drawImage(backgroundImage, 0, 0, Desenho.acessoATelaDoJogo());
        } catch (IOException ex) {
            Logger.getLogger(Tela.class.getName()).log(Level.SEVERE, null, ex);
        }  
    }
}