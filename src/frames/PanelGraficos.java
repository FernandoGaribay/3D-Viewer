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
    
    private Objeto3D objetoActual;
    private ArrayList<Objeto3D> listaCubos = new ArrayList<>();
    private int currentIndex = 0;

    public PanelGraficos(LabelManager labelManager) {
        SwingUtilities.invokeLater(() -> {
            this.setBackground(new Color(38, 38, 38));

            double[] origenCubo = {450, 300, 700};
            Cubo3D cubo = new Cubo3D(getWidth(), getHeight(), origenCubo, puntoFuga, labelManager);
            listaCubos.add(cubo);

            double[] origenCubo2 = {200, 300, 700};
            Cubo3D cubo2 = new Cubo3D(getWidth(), getHeight(), origenCubo2, puntoFuga, labelManager);
            listaCubos.add(cubo2);

            if (!listaCubos.isEmpty()) {
                objetoActual = listaCubos.get(currentIndex);
                objetoActual.setSeleccionado(true);
            }

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

    public void siguienteElemento() {
        if (currentIndex < listaCubos.size() - 1) {
            objetoActual.setSeleccionado(false);
            currentIndex++;
            objetoActual = listaCubos.get(currentIndex);
            System.out.println("Seleccionado: " + objetoActual.getIdObjeto());
            objetoActual.setSeleccionado(true);
        } else {
            System.out.println("No hay más elementos siguientes.");
        }
    }

    public void anteriorElemento() {
        if (currentIndex > 0) {
            objetoActual.setSeleccionado(false);
            currentIndex--;
            objetoActual = listaCubos.get(currentIndex);
            System.out.println("Seleccionado: " + objetoActual.getIdObjeto());
            objetoActual.setSeleccionado(true);
        } else {
            System.out.println("No hay más elementos anteriores.");
        }
    }

    public void setMostrarAnimacion() {
        objetoActual.setMostrarAnimacion();
    }

    public void setEscala(int escala) {
        objetoActual.setEscala(escala);
    }

    public void setRotacionTransformacion() {
        objetoActual.setRotacionTransformacion();
    }

    public void setRotacionTransformacionMouse(int x, int y) {
        objetoActual.setRotacionTransformacionMouse(x, y);
    }

    public void setRotacionTransformacionArriba() {
        objetoActual.setRotacionTransformacionArriba();
    }

    public void setRotacionTransformacionAbajo() {
        objetoActual.setRotacionTransformacionAbajo();
    }

    public void setRotacionTransformacionIzquierda() {
        objetoActual.setRotacionTransformacionIzquierda();
    }

    public void setRotacionTransformacionDerecha() {
        objetoActual.setRotacionTransformacionDerecha();
    }

    public void setRotacionTransformacionZPositiva() {
        objetoActual.setRotacionTransformacionZPositiva();
    }

    public void setRotacionTransformacionZNegativa() {
        objetoActual.setRotacionTransformacionZNegativa();
    }

    public void setMostrarPuntos() {
        objetoActual.setMostrarPuntos();
    }

    public void setMostrarLineas() {
        objetoActual.setMostrarLineas();
    }

    public void setMostrarCaras() {
        objetoActual.setMostrarCaras();
    }

    public void setEjeXAnimacion() {
        objetoActual.setEjeXAnimacion();
    }

    public void setEjeYAnimacion() {
        objetoActual.setEjeYAnimacion();
    }

    public void setEjeZAnimacion() {
        objetoActual.setEjeZAnimacion();
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
