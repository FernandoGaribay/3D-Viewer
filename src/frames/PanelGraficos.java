package frames;

import graficos.Cubo3D;
import java.awt.Color;
import java.awt.Graphics;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class PanelGraficos extends JPanel implements Runnable {

    private Thread hiloPanelGraficos;

    private double[] puntoFuga = {450, 300, 250};
    private double[] origenCubo = {450, 300, 700};

    private Cubo3D cubo;

    public PanelGraficos() {
        SwingUtilities.invokeLater(() -> {
            cubo = new Cubo3D(getWidth(), getHeight(), origenCubo, puntoFuga);
            setBackground(Color.WHITE);

            this.hiloPanelGraficos = new Thread(this);
            this.hiloPanelGraficos.start();
        });
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        g.drawImage(cubo.getBuffer(), 0, 0, null);
    }

    @Override
    public void run() {
        while (true) {
            repaint();
            try {
                Thread.sleep(16);
            } catch (InterruptedException ex) {
                Logger.getLogger(PanelGraficos.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
