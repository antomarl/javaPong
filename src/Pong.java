import javax.swing.JFrame;
import javax.swing.JButton;
import javax.swing.JPanel;
import java.awt.BorderLayout;
public class Pong {
     static void main(String[] args){
        JFrame finestra = new JFrame("il mio pong"); // creao l'ggetto
        CampoGioco campo = new CampoGioco();
        JButton ricomincia = new JButton("ricomincia"); // creo il pulsante per ricominciare
        ricomincia.setVisible(false); // all'inizio deve restare nascosto
        ricomincia.addActionListener(e -> campo.ricominciaPartita()); // stabilisce cosa succede quando lo clicchi
        campo.collegaPulsanteRicomincia(ricomincia);
        JPanel contenitore = new JPanel(new BorderLayout());
        contenitore.add(campo, BorderLayout.CENTER);
        contenitore.add(ricomincia, BorderLayout.SOUTH); // posiziona il pulsante in basso
        finestra.add(contenitore); // aggionge il pannello alla finestra
        finestra.setSize(800, 530); // do le misure della finestra
        finestra.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // se clicco x si chiude
        finestra.setLocationRelativeTo(null); // gli dico dove deve comparire a shcermo
        finestra.setVisible(true); // la rendo visibile
    }
}
