package frames;

import graficos.Cubo3D;
import java.awt.Color;
import java.awt.Graphics;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import Interfaces.LabelManager;
import java.util.ArrayList;

public class PanelGraficos extends JPanel implements Runnable {

    private Thread hiloPanelGraficos;

    private double[] puntoFuga = {450, 300, 250};
    ArrayList<Cubo3D> listaCubos = new ArrayList<>();

    public PanelGraficos(LabelManager labelManager) {
        SwingUtilities.invokeLater(() -> {
            double[] origenCubo = {450, 300, 700};
            Cubo3D cubo = new Cubo3D(getWidth(), getHeight(), origenCubo, puntoFuga, labelManager);
            cubo.setSeleccionado(true);
            listaCubos.add(cubo);
            double[] origenCubo2 = {200, 300, 700};
            listaCubos.add(new Cubo3D(getWidth(), getHeight(), origenCubo2, puntoFuga, labelManager));

            this.setBackground(new Color(38, 38, 38));

            this.hiloPanelGraficos = new Thread(this);
            this.hiloPanelGraficos.start();
        });
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);

        for (Cubo3D cubo : listaCubos) {
            g.drawImage(cubo.getBuffer(), 0, 0, null);
        }
    }

    public void setMostrarAnimacion() {
        for (Cubo3D cubo : listaCubos) {
            if(cubo.isSeleccionado()){
                cubo.setMostrarAnimacion();
            }
        }
    }

    public void trasladarCubos(int d) {
        for (Cubo3D cubo : listaCubos) {
            cubo.trasladarX(d);
        }
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
