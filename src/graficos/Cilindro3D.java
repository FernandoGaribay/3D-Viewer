package graficos;

import java.awt.Color;
import java.awt.Polygon;
import java.awt.geom.Point2D;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JLabel;
import Interfaces.LabelManager;
import java.util.ArrayList;
import java.util.Random;
import utils.Constantes;

public class Cilindro3D extends Objeto3D implements Runnable {

    private final Thread hiloCubo;
    private double[][] verticesTrasladados;

    private ArrayList<double[]> vertices = new ArrayList<>();
    private double anguloMaximo = 2 * Math.PI;
    private int numPuntos = 30;
    private double anguloIncremento = anguloMaximo / numPuntos;

    private int colorCount = 0;
    private Color[] colores = new Color[12];

    public Cilindro3D(int frameWidth, int frameHeight, double[] origenCubo, double[] puntoFuga, LabelManager labelManager) {
        super(frameWidth, frameHeight, origenCubo, puntoFuga, labelManager);
        this.verticesTrasladados = new double[8][3];

        JLabel etiquetaActual = new JLabel("Cubo " + (idObjeto + 1));
        this.labelManager.aniadirEtiqueta(etiquetaActual);

        for (double alpha = 0; alpha < anguloMaximo; alpha += anguloIncremento) {
            for (double beta = 0; beta < anguloMaximo; beta += anguloIncremento) {
                double[] vertice = new double[3];
                vertice[0] = (2 + Math.cos(alpha)) * Math.cos(beta);
                vertice[1] = (2 + Math.cos(alpha)) * Math.sin(beta);
                vertice[2] = alpha;
                vertices.add(vertice);
            }
        }
        verticesTrasladados = new double[vertices.size()][3];

        Random rand = new Random();
        for (int i = 0; i < 12; i++) {
            colores[i] = Color.getHSBColor(rand.nextFloat(), 1, 1);
        }

        this.hiloCubo = new Thread(this);
        this.hiloCubo.start();
    }

    private synchronized void dibujarCubo() {
        g2d.resetBuffer();
        transformarVertices();

        if (mostrarAnimacion) {
            rotaciones[0] += (animacionEjeX) ? 1 : 0;
            rotaciones[1] += (animacionEjeY) ? 1 : 0;
            rotaciones[2] += (animacionEjeZ) ? 1 : 0;
        }
        if (mostrarPuntos) {
            dibujarPuntos();
        }
    }

    private void transformarVertices() {
        for (int i = 0; i < vertices.size(); i++) {
            double[] vertice = vertices.get(i);
            vertice = rotarX(vertice, rotaciones[0]);
            vertice = rotarY(vertice, rotaciones[1]);
            vertice = rotarZ(vertice, rotaciones[2]);
            verticesTrasladados[i] = vertice;
        }

        for (int i = 0; i < vertices.size(); i++) {
            double[] v = verticesTrasladados[i];
            double[] trasladado = {
                (v[0] * escala) + traslaciones[0] + origenCubo[0],
                (v[1] * escala) + traslaciones[1] + origenCubo[1],
                (v[2] * escala) + traslaciones[2] + origenCubo[2]
            };
            verticesTrasladados[i] = trasladado;
        }

        colorCount = 0;
        for (int i = 0; i < numPuntos - 1; i++) {
            for (int j = 0; j < numPuntos; j++) {
                int index0 = i * numPuntos + j;
                int index1 = (i + 1) * numPuntos + j;
                int index2 = i * numPuntos + (j + 1) % numPuntos;
                int index3 = (i + 1) * numPuntos + (j + 1) % numPuntos;

                double[] vertice0 = verticesTrasladados[index0];
                double[] vertice1 = verticesTrasladados[index1];
                double[] vertice2 = verticesTrasladados[index2];
                double[] vertice3 = verticesTrasladados[index3];

                Polygon poly = new Polygon();
                Point2D p0 = punto3D_a_2D(vertice0[0], vertice0[1], vertice0[2]);
                Point2D p1 = punto3D_a_2D(vertice1[0], vertice1[1], vertice1[2]);
                Point2D p2 = punto3D_a_2D(vertice2[0], vertice2[1], vertice2[2]);
                Point2D p3 = punto3D_a_2D(vertice3[0], vertice3[1], vertice3[2]);

                poly.addPoint((int) p0.getX(), (int) p0.getY());
                poly.addPoint((int) p1.getX(), (int) p1.getY());
                poly.addPoint((int) p2.getX(), (int) p2.getY());
                poly.addPoint((int) p3.getX(), (int) p3.getY());

                if (mostrarLineas) {
                    g2d.setColor(Color.WHITE);
                    g2d.drawLine((int) p0.getX(), (int) p0.getY(), (int) p1.getX(), (int) p1.getY());
                    g2d.drawLine((int) p1.getX(), (int) p1.getY(), (int) p3.getX(), (int) p3.getY());
                    g2d.drawLine((int) p3.getX(), (int) p3.getY(), (int) p2.getX(), (int) p2.getY());
                    g2d.drawLine((int) p2.getX(), (int) p2.getY(), (int) p0.getX(), (int) p0.getY());
                }

                if (mostrarCaras) {
                    g2d.setColor(colores[colorCount % colores.length]);
                    double midZ = (vertice0[2] + vertice1[2] + vertice2[2] + vertice3[2]) / 4;
                    g2d.fillPolygon3D(poly, midZ);
                    colorCount++;
                }
            }
        }
    }

    private void dibujarPuntos() {
        g2d.setColor(Color.WHITE);
        for (int i = 0; i < verticesTrasladados.length; i++) {
            double[] v = verticesTrasladados[i];
            double x = v[0];
            double y = v[1];
            double z = v[2];
            Point2D.Double p1 = punto3D_a_2D(x, y, z);
            g2d.fillCircle3D((int) p1.x, (int) p1.y, 2, (int) 2);
        }
    }

    @Override
    public void run() {
        int fps = 60;
        long tiempoPorFotograma = 1000 / fps;
        int sleepTime;

        long tiempoAnterior = System.currentTimeMillis();
        int contadorFPS = 0;

        while (true) {
            if (!isSeleccionado()) {
                try {
                    Thread.sleep(500);
                    continue;
                } catch (InterruptedException ex) {
                    Logger.getLogger(Cubo3D.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

            long inicio = System.currentTimeMillis();

            // CODIGO ----------------------------------------------------------
            dibujarCubo();

            Point2D.Double p1 = punto3D_a_2D(traslaciones[0] + origenCubo[0], traslaciones[1] + origenCubo[1], traslaciones[2] + origenCubo[2]);
            labelManager.actualizarEtiquetaObjeto(idObjeto, (int) (p1.x - Constantes.OFFSET_TAG_LABEL_WIDTH), (int) (p1.y - escala - Constantes.OFFSET_TAG_LABEL_HEIGHT));

            // -----------------------------------------------------------------
            long tiempoOperacion = System.currentTimeMillis() - inicio;

            if (System.currentTimeMillis() - tiempoAnterior >= 1000) {
                String newInformacion = "<html><div style='text-align: right;'>------------------- INFORMACION -------------------<br><br>"
                        + "ID OBJETO: #" + (idObjeto + 1) + "<br>"
                        + "FPS: " + contadorFPS + "<br><br>"
                        + "Puntos: " + mostrarPuntos + "<br>"
                        + "Lineas: " + mostrarLineas + "<br>"
                        + "Caras: " + mostrarCaras + "<br><br>"
                        + "Punto del objeto:<br>"
                        + "X -> " + (origenCubo[0] + traslaciones[0]) + " pixeles<br>"
                        + "Y -> " + (origenCubo[1] + traslaciones[1]) + " pixeles<br>"
                        + "Z -> " + (origenCubo[2] + traslaciones[2]) + " pixeles<br><br>"
                        + "Punto de fuga:<br>"
                        + "X -> " + puntoFuga[0] + " pixeles<br>"
                        + "Y -> " + puntoFuga[1] + " pixeles<br>"
                        + "Z -> " + puntoFuga[2] + " pixeles<br>"
                        + "FOV -> 250 pixeles<br><br>"
                        + "Ejes activos:<br>"
                        + "X -> " + animacionEjeX + "<br>"
                        + "Y -> " + animacionEjeY + "<br>"
                        + "Z -> " + animacionEjeZ + "<br><br>"
                        + "</div></html>";
                labelManager.actualizarEtiquetaInformacion(idObjeto, newInformacion);

                contadorFPS = 0;
                tiempoAnterior = System.currentTimeMillis();
            }
            contadorFPS++;

            sleepTime = (int) (tiempoPorFotograma - tiempoOperacion);
            if (sleepTime > 0) {
                try {
                    Thread.sleep(sleepTime);
                } catch (InterruptedException ex) {
                    Logger.getLogger(Cubo3D.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }
}
