package graficos;

import java.awt.Color;
import java.awt.Polygon;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JLabel;
import Interfaces.LabelManager;
import utils.Constantes;

public class Cubo3D implements Runnable {

    private final int idCubo;
    private static int contadorCubos;
    private boolean seleccionado;
    private LabelManager labelManager;

    private MyGraphics g2d;
    private Thread hiloCubo;
    private int frameWidth, frameHeight;

    double[][] verticesTrasladados;
    private double[] origenCubo;
    private double[] puntoFuga;

    private int escala;
    private double[] rotaciones;
    private double[] traslaciones;

    private boolean mostrarAnimacion;
    private boolean traslacion;
    private boolean mostrarPuntos;
    private boolean mostrarLineas;
    private boolean mostrarCaras;
    private boolean animacionEjeX;
    private boolean animacionEjeY;
    private boolean animacionEjeZ;

    private double[][] vertices = {
        {1, 1, 1},
        {-1, 1, 1},
        {-1, -1, 1},
        {1, -1, 1},
        {1, 1, -1},
        {-1, 1, -1},
        {-1, -1, -1},
        {1, -1, -1}
    };

    private int[][] edges = {
        {0, 1}, {1, 2}, {2, 3}, {3, 0},
        {4, 5}, {5, 6}, {6, 7}, {7, 4},
        {0, 4}, {1, 5}, {2, 6}, {3, 7}
    };

    private int[][] caras = {
        {0, 1, 2, 3}, {4, 5, 6, 7},
        {0, 3, 7, 4}, {1, 2, 6, 5},
        {2, 6, 7, 3}, {1, 5, 4, 0}
    };

    static {
        contadorCubos = 0;
    }

    public Cubo3D(int frameWidth, int frameHeight, double[] origenCubo, double[] puntoFuga, LabelManager labelManager) {
        this.g2d = new MyGraphics(frameWidth, frameHeight);
        this.frameWidth = frameWidth;
        this.frameHeight = frameHeight;

        this.labelManager = labelManager;
        this.origenCubo = origenCubo;
        this.puntoFuga = puntoFuga;

        this.escala = 100;
        this.verticesTrasladados = new double[8][3];
        this.traslaciones = new double[3];
        this.rotaciones = new double[3];
        initBanderas();

        this.idCubo = contadorCubos++;
        this.seleccionado = false;

        Point2D.Double p1 = punto3D_a_2D(origenCubo[0], origenCubo[1], origenCubo[2]);
        JLabel infoHiloActual = new JLabel("FPS: 0");
        JLabel etiquetaActual = new JLabel("Cubo " + (idCubo + 1));
        this.labelManager.aniadirEtiqueta(etiquetaActual, infoHiloActual, (int) (p1.x - Constantes.OFFSET_INFO_LABEL_WIDTH), (int) (p1.y - escala - Constantes.OFFSET_INFO_LABEL_HEIGHT));

        this.hiloCubo = new Thread(this);
        this.hiloCubo.start();
    }

    private void initBanderas() {
        this.mostrarAnimacion = false;
        this.traslacion = false;
        this.mostrarPuntos = true;
        this.mostrarLineas = true;
        this.mostrarCaras = false;
        this.animacionEjeX = true;
        this.animacionEjeY = false;
        this.animacionEjeZ = false;
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

    Color[] colores = {
        new Color(204, 204, 204),
        new Color(153, 204, 0),
        new Color(255, 102, 102),
        new Color(204, 51, 255),
        new Color(255, 204, 204),
        new Color(0, 51, 255),};
    int colorsCOunt = 0;

    private void dibujarCaras() {
        for (int[] face : caras) {
            g2d.setColor(colores[colorsCOunt % 6]);
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
            colorsCOunt++;
        }
    }

    private double[] rotarX(double[] point, double angle) {
        double[] result = new double[3];
        result[0] = point[0];
        result[1] = point[1] * Math.cos(Math.toRadians(angle)) - point[2] * Math.sin(Math.toRadians(angle));
        result[2] = point[1] * Math.sin(Math.toRadians(angle)) + point[2] * Math.cos(Math.toRadians(angle));
        return result;
    }

    private double[] rotarY(double[] point, double angle) {
        double[] result = new double[3];
        result[0] = point[0] * Math.cos(Math.toRadians(angle)) + point[2] * Math.sin(Math.toRadians(angle));
        result[1] = point[1];
        result[2] = -point[0] * Math.sin(Math.toRadians(angle)) + point[2] * Math.cos(Math.toRadians(angle));
        return result;
    }

    private double[] rotarZ(double[] point, double angle) {
        double[] result = new double[3];
        result[0] = point[0] * Math.cos(Math.toRadians(angle)) - point[1] * Math.sin(Math.toRadians(angle));
        result[1] = point[0] * Math.sin(Math.toRadians(angle)) + point[1] * Math.cos(Math.toRadians(angle));
        result[2] = point[2];
        return result;
    }

    private Point2D.Double punto3D_a_2D(double x, double y, double z) {
        double u = -puntoFuga[2] / (z - puntoFuga[2]);

        double px = puntoFuga[0] + (x - puntoFuga[0]) * u;
        double py = puntoFuga[1] + (y - puntoFuga[1]) * u;

//        double xf = puntoFuga[0];
//        double yf = puntoFuga[1];
//        double zf = puntoFuga[2];
//        double distanciaFocal = 500; // 250
//
//        double px = (distanciaFocal * -(x - xf)) / (z - zf) + xf;
//        double py = (distanciaFocal * -(y - yf)) / (z - zf) + yf;
        return new Point2D.Double(px, py);
    }

    public synchronized BufferedImage getBuffer() {
        return g2d.getBuffer();
    }

    public void trasladarX(int distancia) {
        this.traslaciones[0] += distancia;
    }

    public void trasladarY(int distancia) {
        this.traslaciones[1] += distancia;
    }

    public void trasladarZ(int distancia) {
        this.traslaciones[2] += distancia;
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
            labelManager.actualizarEtiquetaObjeto(idCubo, (int) (p1.x - Constantes.OFFSET_TAG_LABEL_WIDTH), (int) (p1.y - escala - Constantes.OFFSET_TAG_LABEL_HEIGHT));

            // -----------------------------------------------------------------
            long tiempoOperacion = System.currentTimeMillis() - inicio;

            if (System.currentTimeMillis() - tiempoAnterior >= 1000) {
                String newTexto = (seleccionado) ? "-> FPS: " + contadorFPS : " FPS: " + contadorFPS;
                labelManager.actualizarEtiquetaInformacion(idCubo, newTexto);

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

    public void setSeleccionado(boolean seleccionado) {
        this.seleccionado = seleccionado;
    }

    public boolean isSeleccionado() {
        return this.seleccionado;
    }

    public void setMostrarAnimacion() {
        this.mostrarAnimacion = !mostrarAnimacion;
    }

    public void setEscala(int escala) {
        this.escala += escala;
    }

    public void setRotacionTransformacion() {
        this.traslacion = !traslacion;
        System.out.println("Traslacion : " + traslacion);
    }

    public void setRotacionTransformacionMouse(int x, int y) {
        traslaciones[0] -= x;
        traslaciones[1] -= y;
    }

    public void setRotacionTransformacionArriba() {
        if (traslacion) {
            trasladarY(10);
        } else {
            rotaciones[0] += 5;
            rotarX(puntoFuga, escala);
        }
    }

    public void setRotacionTransformacionAbajo() {
        if (traslacion) {
            trasladarY(-10);
        } else {
            rotaciones[0] -= 5;
            rotarX(puntoFuga, escala);
        }
    }

    public void setRotacionTransformacionIzquierda() {
        if (traslacion) {
            trasladarX(10);
        } else {
            rotaciones[1] -= 5;
            rotarX(puntoFuga, escala);
        }
    }

    public void setRotacionTransformacionDerecha() {
        if (traslacion) {
            trasladarX(-10);
        } else {
            rotaciones[1] += 5;
            rotarX(puntoFuga, escala);
        }
    }

    public void setRotacionTransformacionZPositiva() {
        if (traslacion) {
            trasladarZ(10);
        } else {
            rotaciones[2] += 5;
            rotarX(puntoFuga, escala);
        }
    }

    public void setRotacionTransformacionZNegativa() {
        if (traslacion) {
            trasladarZ(-10);
        } else {
            rotaciones[2] -= 5;
            rotarX(puntoFuga, escala);
        }
    }

    public void setMostrarPuntos() {
        this.mostrarPuntos = !mostrarPuntos;
    }

    public void setMostrarLineas() {
        this.mostrarLineas = !mostrarLineas;
    }

    public void setMostrarCaras() {
        this.mostrarCaras = !mostrarCaras;
    }

    public void setEjeXAnimacion() {
        this.animacionEjeX = !animacionEjeX;
    }

    public void setEjeYAnimacion() {
        this.animacionEjeY = !animacionEjeY;
    }

    public void setEjeZAnimacion() {
        this.animacionEjeZ = !animacionEjeZ;
    }

}
