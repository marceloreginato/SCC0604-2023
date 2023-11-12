package Controler;
    
import Modelo.Inimigo.AtiraNaVisao;  
import Modelo.Inimigo.AtiraPelaMoeda;  
import Modelo.Blocos.Coletavel;  
import Modelo.Blocos.Estatico;  
import Modelo.Fases.Fase;  
import Modelo.Fases.Fase1;  
import Modelo.Fases.Fase2;  
import Modelo.Fases.Fase3;  
import Modelo.Fases.Fase4;  
import Modelo.Hero;  
import Modelo.Inimigo.Inimigo;  
import Modelo.Personagem;
import Modelo.Inimigo.InimigoAtirador;
import Modelo.Blocos.Numero;
import Modelo.Blocos.Porta;
import Modelo.Tiro;
import Modelo.Inimigo.ZigueZague;
import Modelo.Progresso;
import Auxiliar.Consts;
import Auxiliar.Desenho;
import Auxiliar.Posicao;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;     
import java.util.logging.Logger;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;  
import javax.swing.JButton;                 

public class Tela extends javax.swing.JFrame implements MouseListener, KeyListener {
    private Hero hero;
    private ArrayList<Personagem> faseAtual;    
    private ControleDeJogo cj = new ControleDeJogo();
    private Graphics g2;    
    private int fase;                               
    private int qntmoedas;
    private int moedasColetadas;
    private int qntvidas;
    private Progresso progresso = new Progresso(this);
 
    public Tela() {
        Desenho.setCenario(this);           
        initComponents();
        this.addMouseListener(this); /*mouse*/

        this.addKeyListener(this);   /*teclado*/
        /*Cria a janela do tamanho do tabuleiro + insets (bordas) da janela*/
        this.setSize(Consts.RES * Consts.CELL_SIDE + getInsets().left + getInsets().right,
                Consts.RES * Consts.CELL_SIDE + getInsets().top + getInsets().bottom);

        faseAtual = new ArrayList<Personagem>();
        hero = new Hero("HeroEstaticFace.png");
        
        progresso.restaurar();
        
        criaFase();
        progresso.salvamento();
    }   

    public boolean ehPosicaoValida(Posicao p, char sentidoMovimento, char tipoPersonagem){
        return cj.ehPosicaoValida(this.faseAtual, p, sentidoMovimento, tipoPersonagem);
    }                       

    public boolean ehValidoZigueZague(Posicao p){
        return cj.ehValidoZigueZague(this.faseAtual, p);
    }

    public void addPersonagem(Personagem umPersonagem) {
        faseAtual.add(umPersonagem);
    }           

    public void removePersonagem(Personagem umPersonagem) {
        faseAtual.remove(umPersonagem);
    }   

    public int tamanhoFase(){
        return faseAtual.size();
    }

    public int getFase(){   
        return fase;
    }

    public void setFase(int i){
        fase = i;
    } 

    public int getMoedas() {
        return qntmoedas;
    }

    protected void setMoedas(int i){
        qntmoedas = i;
    } 

    public void addMoedas(){
        qntmoedas++;
    }  

    public void removeMoedas(){
        qntmoedas--;
    } 

    public int getVidas(){   
        return qntvidas;
    }

    public void setVidas(int i){
        qntvidas = i;
    } 
    
    public void removeVidas(){
        if(qntvidas == 1){
            qntvidas = 5;
            fase = 1;
            progresso.salvamento();
            System.exit(0);
        } 
        qntvidas--;
    }

    public int getMoedasColetadas() {
        return moedasColetadas;
    }

    public void setMoedasColetadas(int moedasColetadas) {
        this.moedasColetadas = moedasColetadas;
    }

    public void addMoedasColetadas() { 
        moedasColetadas++;
    }

    public void atualizaNumeros(){
        Numero vidas = new Numero(getVidas(), 'v');
        Numero moedas = new Numero(getMoedas(), 'm');
        Numero fases = new Numero(getFase(), 'f');
    
        faseAtual.set(2, vidas); 
        faseAtual.set(3, moedas);   
        faseAtual.set(4, fases);   
    }

    public void criaFase(){
        if(fase <= 5)
            faseAtual.clear();
        setMoedas(0);
        setMoedasColetadas(0);

        switch (fase) {
            // case 0:
            //     new Inicio();
            //     break;

            case 1:
                setVidas(5);
                new Fase1(hero);
                break;

            case 2:
                new Fase2(hero);
                break;

            case 3:
                new Fase3(hero);                    
                break;

            case 4:
                new Fase4(hero);
                break;
        
            case 5:
                new Fase1(hero);
                break;

            default:
                break;
        }
    }

    public Graphics getGraphicsBuffer(){
        return g2;  
    }
    public void paint(Graphics gOld) {
        Graphics g = this.getBufferStrategy().getDrawGraphics();
        /*Criamos um contexto gráfico*/
        g2 = g.create(getInsets().left, getInsets().top, getWidth() - getInsets().right, getHeight() - getInsets().top);
        /*************Desenha cenário de fundo**************/
        for (int i = 0; i < Consts.RES; i++) {
            for (int j = 0; j < Consts.RES; j++) {
                try {
                    Image newImage = Toolkit.getDefaultToolkit().getImage(new java.io.File("..").getCanonicalPath() + Consts.PATH + "black.png");
                    g2.drawImage(newImage,
                            j * Consts.CELL_SIDE, i * Consts.CELL_SIDE, Consts.CELL_SIDE, Consts.CELL_SIDE, null);

                } catch (IOException ex) {
                    Logger.getLogger(Tela.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }

        atualizaNumeros();

        if(tamanhoFase() > 0 && !faseAtual.get(1).isbPorta() && fase < 5){
            fase++;
            criaFase();
            progresso.salvamento();
        }

        if (!this.faseAtual.isEmpty()) {
            this.cj.desenhaTudo(faseAtual);
            this.cj.processaTudo(faseAtual);
        }

        g.dispose();
        g2.dispose();
        if (!getBufferStrategy().contentsLost()) {
            getBufferStrategy().show();
        }
    }

    public void go() {
        TimerTask task = new TimerTask() {
            public void run() {
                repaint();
            }
        };
        Timer timer = new Timer();
        timer.schedule(task, 0, Consts.PERIOD);
    }

    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_C) {
            this.faseAtual.clear();
        } else if (e.getKeyCode() == KeyEvent.VK_UP || e.getKeyCode() == KeyEvent.VK_W) {
            hero.moveUp();
        } else if (e.getKeyCode() == KeyEvent.VK_DOWN || e.getKeyCode() == KeyEvent.VK_S) {
            hero.moveDown();
        } else if (e.getKeyCode() == KeyEvent.VK_LEFT || e.getKeyCode() == KeyEvent.VK_A) {
            hero.moveLeft();
        } else if (e.getKeyCode() == KeyEvent.VK_RIGHT || e.getKeyCode() == KeyEvent.VK_D) {
            hero.moveRight();
        } else if (e.getKeyCode() == KeyEvent.VK_SPACE && this.getMoedas() != 0) {
            hero.atira(hero.getLastMovment());  
            removeMoedas();
        } else if (e.getKeyCode() == KeyEvent.VK_M) { // salvar e sair
            progresso.salvamento();
            // setFase(0);
        } else if (e.getKeyCode() == KeyEvent.VK_R) { // salvar e sair
            progresso.salvamento();
            System.exit(0);
        }

        this.setTitle("-> Cell: " + (hero.getPosicao().getColuna()) + ", "
                + (hero.getPosicao().getLinha()));

        //repaint(); /*invoca o paint imediatamente, sem aguardar o refresh*/
    }

    public void mousePressed(MouseEvent e) {
        /* Clique do mouse desligado*/
         int x = e.getX();
         int y = e.getY();
     
         this.setTitle("X: "+ x + ", Y: " + y +
         " -> Cell: " + (y/Consts.CELL_SIDE) + ", " + (x/Consts.CELL_SIDE));
        
         this.hero.getPosicao().setPosicao(y/Consts.CELL_SIDE, x/Consts.CELL_SIDE);
         
        repaint();
    }   


    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("POO2023 - Eggerland Mystery - Marcelo e Rhayna");
        setAlwaysOnTop(true);
        setAutoRequestFocus(false);
        setResizable(false);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 561, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 500, Short.MAX_VALUE)    
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables

    public void mouseMoved(MouseEvent e) {
    }

    public void mouseClicked(MouseEvent e) {
    }

    public void mouseReleased(MouseEvent e) {
    }

    public void mouseEntered(MouseEvent e) {
    }

    public void mouseExited(MouseEvent e) {
    }

    public void mouseDragged(MouseEvent e) {
    }

    public void keyTyped(KeyEvent e) {
    }

    public void keyReleased(KeyEvent e) {
    }
}
