package frames;

import graficos.Cubo3D;
import java.awt.Color;
import java.awt.Graphics;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import Interfaces.LabelManager;
import graficos.Objeto3D;
import java.util.ArrayList;

public class PanelGraficos extends JPanel implements Runnable {

    private Thread hiloPanelGraficos;

    private double[] puntoFuga = {450, 300, 250};
    ArrayList<Objeto3D> listaCubos = new ArrayList<>();

    public PanelGraficos(LabelManager labelManager) {
        SwingUtilities.invokeLater(() -> {
            double[] origenCubo = {450, 300, 700};
            Cubo3D cubo = new Cubo3D(getWidth(), getHeight(), origenCubo, puntoFuga, labelManager);
            cubo.setSeleccionado(true);
            listaCubos.add(cubo);
//            double[] origenCubo2 = {200, 300, 700};
//            listaCubos.add(new Cubo3D(getWidth(), getHeight(), origenCubo2, puntoFuga, labelManager));

            this.setBackground(new Color(38, 38, 38));

            this.hiloPanelGraficos = new Thread(this);
            this.hiloPanelGraficos.start();
        });
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);

        for (Objeto3D cubo : listaCubos) {
            g.drawImage(cubo.getBuffer(), 0, 0, null);
        }
    }

    public void setMostrarAnimacion() {
        for (Objeto3D cubo : listaCubos) {
            if (cubo.isSeleccionado()) {
                cubo.setMostrarAnimacion();
            }
        }
    }

    public void setEscala(int escala) {
        for (Objeto3D cubo : listaCubos) {
            if (cubo.isSeleccionado()) {
                cubo.setEscala(escala);
            }
        }
    }

    public void setRotacionTransformacion() {
        for (Objeto3D cubo : listaCubos) {
            if (cubo.isSeleccionado()) {
                cubo.setRotacionTransformacion();
            }
        }
    }

    public void setRotacionTransformacionMouse(int x, int y) {
        for (Objeto3D cubo : listaCubos) {
            if (cubo.isSeleccionado()) {
                cubo.setRotacionTransformacionMouse(x, y);
            }
        }
    }

    public void setRotacionTransformacionArriba() {
        for (Objeto3D cubo : listaCubos) {
            if (cubo.isSeleccionado()) {
                cubo.setRotacionTransformacionArriba();
            }
        }
    }

    public void setRotacionTransformacionAbajo() {
        for (Objeto3D cubo : listaCubos) {
            if (cubo.isSeleccionado()) {
                cubo.setRotacionTransformacionAbajo();
            }
        }
    }

    public void setRotacionTransformacionIzquierda() {
        for (Objeto3D cubo : listaCubos) {
            if (cubo.isSeleccionado()) {
                cubo.setRotacionTransformacionIzquierda();
            }
        }
    }

    public void setRotacionTransformacionDerecha() {
        for (Objeto3D cubo : listaCubos) {
            if (cubo.isSeleccionado()) {
                cubo.setRotacionTransformacionDerecha();
            }
        }
    }

    public void setRotacionTransformacionZPositiva() {
        for (Objeto3D cubo : listaCubos) {
            if (cubo.isSeleccionado()) {
                cubo.setRotacionTransformacionZPositiva();
            }
        }
    }

    public void setRotacionTransformacionZNegativa() {
        for (Objeto3D cubo : listaCubos) {
            if (cubo.isSeleccionado()) {
                cubo.setRotacionTransformacionZNegativa();
            }
        }
    }

    public void setMostrarPuntos() {
        for (Objeto3D cubo : listaCubos) {
            if (cubo.isSeleccionado()) {
                cubo.setMostrarPuntos();
            }
        }
    }

    public void setMostrarLineas() {
        for (Objeto3D cubo : listaCubos) {
            if (cubo.isSeleccionado()) {
                cubo.setMostrarLineas();
            }
        }
    }

    public void setMostrarCaras() {
        for (Objeto3D cubo : listaCubos) {
            if (cubo.isSeleccionado()) {
                cubo.setMostrarCaras();
            }
        }
    }

    public void setEjeXAnimacion() {
        for (Objeto3D cubo : listaCubos) {
            if (cubo.isSeleccionado()) {
                cubo.setEjeXAnimacion();
            }
        }
    }

    public void setEjeYAnimacion() {
        for (Objeto3D cubo : listaCubos) {
            if (cubo.isSeleccionado()) {
                cubo.setEjeYAnimacion();
            }
        }
    }

    public void setEjeZAnimacion() {
        for (Objeto3D cubo : listaCubos) {
            if (cubo.isSeleccionado()) {
                cubo.setEjeZAnimacion();
            }
        }
    }

    public void trasladarCubos(int d) {
        for (Objeto3D cubo : listaCubos) {
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
