package graficos;

import java.awt.Color;
import java.awt.Polygon;
import java.awt.geom.Point2D;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JLabel;
import Interfaces.LabelManager;
import java.util.ArrayList;
import utils.Constantes;

public class PilaCubos3D extends Objeto3D implements Runnable {

    private final Thread hiloCubo;
    double[][] verticesTrasladados;

    private ArrayList<double[]> vertices = new ArrayList<>();
    double anguloMaximo = 2 * Math.PI;
    int numPuntos = 30;
    double anguloIncremento = anguloMaximo / numPuntos;

    Color[] strongColors = new Color[30];

    public PilaCubos3D(int frameWidth, int frameHeight, double[] origenCubo, double[] puntoFuga, LabelManager labelManager) {
        super(frameWidth, frameHeight, origenCubo, puntoFuga, labelManager);
        this.verticesTrasladados = new double[8][3];

        Point2D.Double p1 = punto3D_a_2D(origenCubo[0], origenCubo[1], origenCubo[2]);
        JLabel etiquetaActual = new JLabel("Cubo " + (idObjeto + 1));
        this.labelManager.aniadirEtiqueta(etiquetaActual, (int) (p1.x - Constantes.OFFSET_INFO_LABEL_WIDTH), (int) (p1.y - escala - Constantes.OFFSET_INFO_LABEL_HEIGHT));

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
        strongColors[0] = new Color(255, 0, 0); // Red
        strongColors[1] = new Color(0, 255, 0); // Green
        strongColors[2] = new Color(0, 0, 255); // Blue
        strongColors[3] = new Color(255, 255, 0); // Yellow
        strongColors[4] = new Color(0, 255, 255); // Cyan
        strongColors[5] = new Color(255, 0, 255); // Magenta
        strongColors[6] = new Color(255, 165, 0); // Orange
        strongColors[7] = new Color(128, 0, 128); // Purple
        strongColors[8] = new Color(128, 128, 0); // Olive
        strongColors[9] = new Color(128, 0, 0); // Maroon
        strongColors[10] = new Color(0, 128, 128); // Teal
        strongColors[11] = new Color(0, 0, 128); // Navy
        strongColors[12] = new Color(139, 0, 0); // Dark Red
        strongColors[13] = new Color(0, 100, 0); // Dark Green
        strongColors[14] = new Color(0, 0, 139); // Dark Blue
        strongColors[15] = new Color(85, 107, 47); // Dark Olive Green
        strongColors[16] = new Color(218, 165, 32); // Goldenrod
        strongColors[17] = new Color(244, 164, 96); // Sandy Brown
        strongColors[18] = new Color(46, 139, 87); // Sea Green
        strongColors[19] = new Color(72, 61, 139); // Dark Slate Blue
        strongColors[20] = new Color(205, 92, 92); // Indian Red
        strongColors[21] = new Color(255, 69, 0); // Orange Red
        strongColors[22] = new Color(124, 252, 0); // Lawn Green
        strongColors[23] = new Color(255, 20, 147); // Deep Pink
        strongColors[24] = new Color(255, 105, 180); // Hot Pink
        strongColors[25] = new Color(199, 21, 133); // Medium Violet Red
        strongColors[26] = new Color(75, 0, 130); // Indigo
        strongColors[27] = new Color(123, 104, 238); // Medium Slate Blue
        strongColors[28] = new Color(32, 178, 170); // Light Sea Green
        strongColors[29] = new Color(220, 20, 60); // Crimson

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
        if (mostrarCaras) {
            dibujarCaras();
        }
        if (mostrarLineas) {
            dibujarLineas();
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

    private void dibujarLineas() {
        int colorCount = 0;

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

                double midZ = (vertice0[2] + vertice1[2] + vertice2[2] + vertice3[2]) / 4;
                double dx = puntoFuga[0] - vertice0[0];
                double dy = puntoFuga[1] - vertice0[1];
                double dz = puntoFuga[2] - vertice0[2];

                // Dibujar líneas entre los puntos
//                g2d.drawLine((int) p0.getX(), (int) p0.getY(), (int) p1.getX(), (int) p1.getY());
//                g2d.drawLine((int) p1.getX(), (int) p1.getY(), (int) p3.getX(), (int) p3.getY());
//                g2d.drawLine((int) p3.getX(), (int) p3.getY(), (int) p2.getX(), (int) p2.getY());
//                g2d.drawLine((int) p2.getX(), (int) p2.getY(), (int) p0.getX(), (int) p0.getY());
                g2d.setColor(strongColors[colorCount % strongColors.length]);
                g2d.fillPolygon3D(poly, midZ);
                colorCount++;
            }
        }
    }

    private void dibujarCaras() {

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
