import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.AbstractAction;
import javax.swing.JComponent;
import javax.swing.KeyStroke;
import javax.swing.Timer;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.Toolkit;
import java.awt.Font;
public class CampoGioco extends JPanel {
    private int racchettaY = 180; // la posizione verticale della racchettaù
    private int racchettaCPUY = 180;
    // ora devo far muovere la palla
    private int pallinaX = 390;
    private int pallinaY = 220;
    private int velocitaX = 5;
    private int velocitaY = 3;
    private double offsetCPU ;
    private final int DIAMETRO = 15;
    private int puntiGiocatore = 0;
    private int puntiCPU = 0;
    private JButton pulsanteRicomincia;
    private boolean partitaFinita = false;
    private boolean inPausa = false;
    public CampoGioco() {
        getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("pressed UP"), "su"); // associa la pressione del tatso al comando2
        getActionMap().put("su", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(inPausa){
                    return;
                }
                racchettaY -= 10; // la rachetta sale di 10 pixel
                racchettaY = Math.max(0, racchettaY); // impedisce che esca
                repaint(); // ridisegan il panello così si vede lo spostamento a schermo
            }
        });
        getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("pressed DOWN"), "giù");

        getActionMap().put("giù", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(inPausa){
                    return; // se il gioco è in pausa io non posso ne scendere ne salire
                }
                racchettaY += 10; // scinni
                racchettaY = Math.min(getHeight() -80 , racchettaY );
                repaint();
            }
        });
        getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("pressed SPACE"), "pausa");
        getActionMap().put("pausa", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if(partitaFinita){
                    return;
                }
                if (!inPausa){
                    inPausa = true;
                } else {
                    inPausa = false;
                }
                repaint();
            }
        });


        Timer timer = new Timer(16, e -> { // il timer esegue il codice ogni 16 milliecondi, tipo 62 fps
            aggiornaPallina();
            repaint();
        });

        timer.setInitialDelay(1000);
        timer.start();
    }
    @Override // perchè è un metodo ereditato dalla classe madre
    protected void paintComponent(Graphics g){
        super.paintComponent(g);
        g.setColor(Color.BLACK); // imposta il nero
        g.fillRect(0, 0, getWidth(), getHeight()); // sfondo nero
        g.setColor(Color.WHITE); // imposta il colore bianco
        g.fillRect(30, racchettaCPUY, 15, 80); // per disegnare un rettangolo
        g.fillRect(getWidth() - 45,  racchettaY, 15, 80); // l'altro rettangolo, che poi sarebbero le racchette
        g.fillOval(pallinaX, pallinaY, DIAMETRO, DIAMETRO); // la pallina
        g.setColor(Color.WHITE); //colore del punteggio
        g.setFont(new Font("Monospaced", Font.BOLD, 36));
        g.drawString("" + puntiCPU, getWidth() / 4, 50);
        g.drawString("" + puntiGiocatore, getWidth() * 3 / 4, 50);
        if (partitaFinita){
            String messaggio;
            if (puntiGiocatore >= 5){
                messaggio = "HAI VINTO!";
            } else {
                messaggio = "CPU HA VINTO!";
            }
            // setto le cose per dove far comparire la scritta e dove metterlo
            g.setFont(new java.awt.Font("Arial", java.awt.Font.BOLD, 32));
            java.awt.FontMetrics metriche = g.getFontMetrics();
            int x = (getWidth()- metriche.stringWidth(messaggio)) / 2;
            int y = getHeight() / 2;

            g.drawString(messaggio, x, y);
        }
        if (inPausa){
            String messaggio = "gioco in pausa";
            g.setColor(Color.GRAY);
            g.setFont(new java.awt.Font("Monospace", Font.BOLD, 29));
            java.awt.FontMetrics metriche = g.getFontMetrics();
            int x = (getWidth() - metriche.stringWidth(messaggio)) / 2 ;
            int y = getHeight() / 2;
            g.drawString(messaggio,x,y);
        }
        Toolkit.getDefaultToolkit().sync();

    }

    private void aggiornaPallina() {
        if (partitaFinita || inPausa){
            return;
        }
        // aggiorniamo la position della ball
        pallinaX += velocitaX;
        pallinaY += velocitaY;
        aggiornaCPU();
        boolean collisioneDestra = // valore booleano che deve avere tre condizioni true allo stesso momenoi
                pallinaX + DIAMETRO >= getWidth() - 45 // qua vediamo se il lato destro della pallina ha raggiunto il lato sinistro della racchetta
                && pallinaX <= getWidth() - 30 // qua vediamo se la pallina ha superato interamente la rachheta
                && pallinaY + DIAMETRO >= racchettaY // qua vediamo se la palina arriva abbastanza in basso
                && pallinaY <= racchettaY + 80; //e qua controlliamo se non è troppo in basso
        if (collisioneDestra && velocitaX > 0) {
            offsetCPU = (Math.random() * 30) - 15;
            pallinaX = getWidth() - 45 - DIAMETRO;
            velocitaX = -velocitaX; // fa eseguire il rembalzo se la pallina tocca la rachheta
            int centroPallina = pallinaY + DIAMETRO / 2;
            int centroRachetta = racchettaY + 40; // centro della pallina
            int differenza = centroPallina - centroRachetta; // centro della rachheta
            velocitaY = differenza / 8;
            if (velocitaY == 0 && differenza < 0) {
                velocitaY = -2;
            } else if (velocitaY == 0 && differenza > 0) {
                velocitaY = +2;

            } else {
                if(Math.random() < 0.5){
                    velocitaY = 2;
                } else {
                    velocitaY = -2;
                }
            }
        }

        boolean collissioneSinistra =
                pallinaX <= 45
                && pallinaX + DIAMETRO >= 30
                && pallinaY + DIAMETRO >= racchettaCPUY
                && pallinaY <= racchettaCPUY + 80;
        if (collissioneSinistra && velocitaX < 0){
            pallinaX = 45;
            velocitaX = -velocitaX;
            int centroPallina = pallinaY + DIAMETRO / 2;
            int centroRacchetta = racchettaCPUY + 40;

            int differenza = centroPallina - centroRacchetta;
            velocitaY = differenza / 8;
            if (velocitaY == 0 && differenza < 0) {
                velocitaY = -2;
            } else if (velocitaY == 0 && differenza > 0) {
                velocitaY = +2;

            } else {
                if(Math.random() < 0.5){
                    velocitaY = 2;
                } else {
                    velocitaY = -2;
                }
            }

        }
        // se va troppo in alto:
        if(pallinaY <= 0){
            pallinaY = 0;
            velocitaY = -velocitaY; // la spinge indietro
        }
        // se va troppo in basso
        if (pallinaY + DIAMETRO >= getHeight()) {
            pallinaY = getHeight() - DIAMETRO;
            velocitaY = -velocitaY;
        }

        if (pallinaX + DIAMETRO < 0){
            puntiGiocatore++; // diamo i punti al player
            if (puntiGiocatore >= 5){
                partitaFinita = true;
                pulsanteRicomincia.setVisible(true);
                pulsanteRicomincia.getParent().revalidate();
                pulsanteRicomincia.getParent().repaint();
            } else {
                resetPallina();
            }
        } else if (pallinaX > getWidth()) {
            puntiCPU++; // punti cpu
            if (puntiCPU >= 5){
                partitaFinita = true;
                pulsanteRicomincia.setVisible(true);
                pulsanteRicomincia.getParent().revalidate();
                pulsanteRicomincia.getParent().repaint();
            } else {
                resetPallina();
            }
        }

    }

    private void resetPallina() {
        // ripostriamo la pallina al centro
        pallinaX = (getWidth() - DIAMETRO) / 2;
        pallinaY = (getHeight() - DIAMETRO) / 2;
        velocitaX = -velocitaX;// inverte la posizione orizzontale
        if (velocitaY == 0){
            velocitaY = 3;
        } else {
            velocitaY = Math.abs(velocitaY);// restituisce il valore assoluto di un numer
        }
    }

    private void aggiornaCPU() {
        int centroPallina = pallinaY + DIAMETRO / 2;
        int centroCPU = racchettaCPUY + 40; // centro della racchetta della cpu
         if (velocitaX < 0){ // in questo modo la racchetta siamuove quando la pallina ve verso di lui
             if(centroCPU < centroPallina + offsetCPU) {
                 racchettaCPUY +=3; // cosi se la pallina e in basso la racchetta scende
             } else if (centroCPU > centroPallina + offsetCPU) {
                 racchettaCPUY -=3; // viceversa sale

             }
         }
         if (racchettaCPUY < 0){
             racchettaCPUY = 0;
         }
         if (racchettaCPUY + 80 > getHeight()){
             racchettaCPUY = getHeight() - 80;
         }
    }

    public void collegaPulsanteRicomincia(JButton pulsante){
        pulsanteRicomincia = pulsante;
    }

    public void ricominciaPartita(){
        puntiGiocatore = 0;
        puntiCPU = 0;
        partitaFinita = false; // resetta tutti i punti e resetta il boolean
        inPausa = false;

        pallinaX = getWidth() / 2 - DIAMETRO / 2;
        pallinaY = getHeight() / 2 -  DIAMETRO / 2; // la pallina torna al centro

        velocitaX = 5;
        velocitaY = 3;

        racchettaY = getHeight() / 2 - 40;
        racchettaCPUY = getHeight() / 2 - 40;

        pulsanteRicomincia.setVisible(false);
        pulsanteRicomincia.getParent().revalidate();
        pulsanteRicomincia.getParent().repaint();

        repaint();
    }
}
