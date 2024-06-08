package graficos;

import java.awt.Color;
import java.awt.Polygon;
import java.awt.geom.Point2D;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JLabel;
import Interfaces.LabelManager;
import utils.Constantes;

public class Cubo3D extends Objeto3D implements Runnable {

    private final Thread hiloCubo;
    protected double[][] verticesTrasladados;

    private final double[][] vertices = {
        {1, 1, 1},
        {-1, 1, 1},
        {-1, -1, 1},
        {1, -1, 1},
        {1, 1, -1},
        {-1, 1, -1},
        {-1, -1, -1},
        {1, -1, -1}
    };

    private final int[][] edges = {
        {0, 1}, {1, 2}, {2, 3}, {3, 0},
        {4, 5}, {5, 6}, {6, 7}, {7, 4},
        {0, 4}, {1, 5}, {2, 6}, {3, 7}
    };

    private final int[][] caras = {
        {0, 1, 2, 3}, {4, 5, 6, 7},
        {0, 3, 7, 4}, {1, 2, 6, 5},
        {2, 6, 7, 3}, {1, 5, 4, 0}
    };

    public Cubo3D(int frameWidth, int frameHeight, double[] origenCubo, double[] puntoFuga, LabelManager labelManager) {
        super(frameWidth, frameHeight, origenCubo, puntoFuga, labelManager);
        this.verticesTrasladados = new double[8][3];

        Point2D.Double p1 = punto3D_a_2D(origenCubo[0], origenCubo[1], origenCubo[2]);
        JLabel infoHiloActual = new JLabel("FPS: 0");
        JLabel etiquetaActual = new JLabel("Cubo " + (idObjeto + 1));
        this.labelManager.aniadirEtiqueta(etiquetaActual, infoHiloActual, (int) (p1.x - Constantes.OFFSET_INFO_LABEL_WIDTH), (int) (p1.y - escala - Constantes.OFFSET_INFO_LABEL_HEIGHT));

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
        for (int i = 0; i < vertices.length; i++) {
            double[] vertice = vertices[i];
            vertice = rotarX(vertice, rotaciones[0]);
            vertice = rotarY(vertice, rotaciones[1]);
            vertice = rotarZ(vertice, rotaciones[2]);
            verticesTrasladados[i] = vertice;
        }

        for (int i = 0; i < vertices.length; i++) {
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
            g2d.fillCircle3D((int) p1.x, (int) p1.y, 4, (int) z);
        }
    }

    private void dibujarLineas() {
        for (int[] edge : edges) {
            double x0 = verticesTrasladados[edge[0]][0];
            double y0 = verticesTrasladados[edge[0]][1];
            double z0 = verticesTrasladados[edge[0]][2];

            double x1 = verticesTrasladados[edge[1]][0];
            double y1 = verticesTrasladados[edge[1]][1];
            double z1 = verticesTrasladados[edge[1]][2];

            Point2D.Double p1 = punto3D_a_2D(x0, y0, z0);
            Point2D.Double p2 = punto3D_a_2D(x1, y1, z1);

            g2d.setColor(Color.WHITE);
            g2d.drawLine((int) p1.x, (int) p1.y, (int) p2.x, (int) p2.y);
        }
    }

    private void dibujarCaras() {
        for (int[] face : caras) {
            g2d.setColor(colores[contadorColores % 6]);
            Polygon poly = new Polygon();
            double midZIndex = 0;
            for (int i = 0; i < face.length; i++) {
                int index = face[i];
                double[] vertex = verticesTrasladados[index];
                Point2D p = punto3D_a_2D(vertex[0], vertex[1], vertex[2]);

                double dx = puntoFuga[0] - vertex[0];
                double dy = puntoFuga[1] - vertex[1];
                double dz = puntoFuga[2] - vertex[2];
                midZIndex += Math.sqrt(dx * dx + dy * dy + dz * dz);
                poly.addPoint((int) p.getX(), (int) p.getY());
            }
            midZIndex = midZIndex / 4;
            g2d.fillPolygon3D(poly, midZIndex);
            contadorColores++;
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
            long inicio = System.currentTimeMillis();

            // CODIGO ----------------------------------------------------------
            dibujarCubo();

            Point2D.Double p1 = punto3D_a_2D(traslaciones[0] + origenCubo[0], traslaciones[1] + origenCubo[1], traslaciones[2] + origenCubo[2]);
            labelManager.actualizarEtiquetaObjeto(idObjeto, (int) (p1.x - Constantes.OFFSET_TAG_LABEL_WIDTH), (int) (p1.y - escala - Constantes.OFFSET_TAG_LABEL_HEIGHT));

            // -----------------------------------------------------------------
            long tiempoOperacion = System.currentTimeMillis() - inicio;

            if (System.currentTimeMillis() - tiempoAnterior >= 1000) {
                String newTexto = (seleccionado) ? "-> FPS: " + contadorFPS : " FPS: " + contadorFPS;
                labelManager.actualizarEtiquetaInformacion(idObjeto, newTexto);

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
