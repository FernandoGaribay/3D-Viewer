package graficos;

import java.awt.Color;
import java.awt.Polygon;
import java.awt.geom.Point2D;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JLabel;
import Interfaces.LabelManager;
import java.io.InputStream;
import java.util.ArrayList;
import modelos3D.LectorOBJ;
import modelos3D.Modelo3D;
import utils.Constantes;

public class Isla3D extends Objeto3D implements Runnable {

    private final Thread hiloCubo;

    private Modelo3D modelo = new Modelo3D();
    private double[][] verticesTrasladados;
    private ArrayList<double[]> vertices;
    private ArrayList<int[]> caras;

    public Isla3D(int frameWidth, int frameHeight, double[] origenCubo, double[] puntoFuga, LabelManager labelManager) {
        super(frameWidth, frameHeight, origenCubo, puntoFuga, labelManager);
        escala = 1.5;
        aumentoEscala = 0.3;

        initColores(20);
        initEtiqueta();
        initVertices();

        this.hiloCubo = new Thread(this);
        this.hiloCubo.start();
    }

    private void initEtiqueta() {
        JLabel etiquetaActual = new JLabel("Isla #" + (idObjeto + 1));
        this.labelManager.aniadirEtiqueta(etiquetaActual);
    }

    private void initVertices() {
        ClassLoader classLoader = getClass().getClassLoader();
        InputStream inputStream = classLoader.getResourceAsStream("recursos/isla.obj");

        modelo = LectorOBJ.readObjFile(inputStream);
        vertices = modelo.getVertices();
        caras = modelo.getCaras();

        System.out.println("Caras Size: " + caras.size());
        verticesTrasladados = new double[vertices.size()][3];
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
        try {
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
        } catch (Exception e) {
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

    private void dibujarCaras() {
        contadorColores = 0;
        for (int[] cara : caras) {
            Polygon poly = new Polygon();
            double midZIndez = 0;
            for (int i = 0; i < cara.length; i++) {
                double[] vertice = verticesTrasladados[cara[i]];
                int xPoints = (int) (vertice[0]);
                int yPoints = (int) (vertice[1]);
                int zPoints = (int) (vertice[2]);
                midZIndez += zPoints;
                Point2D.Double punto = punto3D_a_2D(xPoints, yPoints, zPoints);
                poly.addPoint((int) punto.x, (int) punto.y);
            }
            midZIndez /= cara.length;
            g2d.setColor(colores[contadorColores++ % colores.length]);
            g2d.fillPolygon3D(poly, midZIndez);
        }
    }

    private void dibujarLineas() {
        g2d.setColor(Color.WHITE);
        for (int[] cara : caras) {
            Polygon poly = new Polygon();
            for (int i = 0; i < cara.length - 1; i++) {
                double[] vertice = verticesTrasladados[cara[i]];
                int xPoints = (int) (vertice[0]);
                int yPoints = (int) (vertice[1]);
                int zPoints = (int) (vertice[2]);
                Point2D.Double punto = punto3D_a_2D(xPoints, yPoints, zPoints);
                poly.addPoint((int) punto.x, (int) punto.y);

                double[] vertice2 = verticesTrasladados[cara[i + 1]];
                int xPoints2 = (int) (vertice2[0]);
                int yPoints2 = (int) (vertice2[1]);
                int zPoints2 = (int) (vertice2[2]);
                Point2D.Double punto2 = punto3D_a_2D(xPoints2, yPoints2, zPoints2);
                poly.addPoint((int) punto2.x, (int) punto2.y);

                g2d.drawLine((int) punto.x, (int) punto.y, (int) punto2.x, (int) punto2.y);
            }
        }
    }

    @Override
    public void run() {
        int fps = 60;
        int fpsActuales = 0;
        int contadorFPS = 0;

        long tiempoPorFotograma = 1000000000 / fps; // En nanosegundos
        long sleepTime;

        long tiempoAnteriorFPS = System.nanoTime();
        long tiempoAnteriorLabel = System.nanoTime();

        while (true) {
            if (!isSeleccionado()) {
                try {
                    Thread.sleep(250);
                    g2d.resetBuffer();
                    continue;
                } catch (InterruptedException ex) {
                    Logger.getLogger(Cubo3D.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

            long inicio = System.nanoTime();

            // CODIGO ----------------------------------------------------------
            dibujarCubo();

            Point2D.Double p1 = punto3D_a_2D(traslaciones[0] + origenCubo[0], traslaciones[1] + origenCubo[1], traslaciones[2] + origenCubo[2]);
            labelManager.actualizarEtiquetaObjeto(idObjeto, (int) (p1.x - Constantes.OFFSET_TAG_LABEL_WIDTH), (int) (p1.y - escala - Constantes.OFFSET_TAG_LABEL_HEIGHT));

            // -----------------------------------------------------------------
            long tiempoOperacion = System.nanoTime() - inicio;

            if (System.nanoTime() - tiempoAnteriorLabel >= 250000000) { // 250 ms en nanosegundos
                String newInformacion = "<html><div style='text-align: right;'>------------------- INFORMACION -------------------<br><br>"
                        + "ID OBJETO: #" + (idObjeto + 1) + "<br>"
                        + "FPS: " + fpsActuales + "<br><br>"
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
                        + "X (" + (rotaciones[0] % 360) + "°) -> " + animacionEjeX + "<br>"
                        + "Y (" + (rotaciones[1] % 360) + "°) -> " + animacionEjeY + "<br>"
                        + "Z (" + (rotaciones[2] % 360) + "°) -> " + animacionEjeZ + "<br><br>"
                        + "</div></html>";
                labelManager.actualizarEtiquetaInformacion(idObjeto, newInformacion);
                tiempoAnteriorLabel = System.nanoTime();
            }

            if (System.nanoTime() - tiempoAnteriorFPS >= 1000000000) { // 1 segundo en nanosegundos
                fpsActuales = contadorFPS;
                contadorFPS = 0;
                tiempoAnteriorFPS = System.nanoTime();
            }
            contadorFPS++;

            sleepTime = (tiempoPorFotograma - tiempoOperacion) / 1000000; // Convertir a milisegundos
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
