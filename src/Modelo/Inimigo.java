package Modelo;

public class Inimigo extends Personagem {
    public Inimigo(String sNomeImagemPNG){
        super(sNomeImagemPNG, 'z');
        this.bMorrivel = true;
        this.bTransponivel = false;
    }
}
