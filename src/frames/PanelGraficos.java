package frames;

import graficos.Cubo3D;
import java.awt.Color;
import java.awt.Graphics;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import Interfaces.LabelManager;

public class PanelGraficos extends JPanel implements Runnable {

    private Thread hiloPanelGraficos;

    private double[] puntoFuga = {450, 300, 250};
    private double[] origenCubo = {450, 300, 700};
    private double[] origenCubo2 = {200, 300, 700};

    private Cubo3D cubo;
    private Cubo3D cubo2;

    public PanelGraficos(LabelManager labelManager) {
        SwingUtilities.invokeLater(() -> {
            this.cubo = new Cubo3D(getWidth(), getHeight(), origenCubo, puntoFuga, labelManager);
//            this.cubo2 = new Cubo3D(getWidth(), getHeight(), origenCubo2, puntoFuga, labelManager);
            this.setBackground(new Color(38, 38, 38));

            this.hiloPanelGraficos = new Thread(this);
            this.hiloPanelGraficos.start();
        });
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        g.drawImage(cubo.getBuffer(), 0, 0, null);
//        g.drawImage(cubo2.getBuffer(), 0, 0, null);
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
