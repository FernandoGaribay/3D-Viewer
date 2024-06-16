package animaciones;

import java.awt.Color;
import java.awt.Polygon;
import java.awt.geom.Point2D;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JLabel;
import Interfaces.LabelManager;
import graficos.Cubo3D;
import graficos.Objeto3D;
import java.io.InputStream;
import java.util.ArrayList;
import modelos3D.LectorOBJ;
import modelos3D.Modelo3D;
import utils.Constantes;

public class AnimacionCilindro extends Objeto3D implements Runnable {

    private final Thread hiloCubo;

    private int numPuntosActual;
    private double[][] verticesTrasladadosCilindro;

    private ArrayList<double[]> verticesCilindro;
    private ArrayList<Point2D[]> carasCilindro;
    private ArrayList<Double> zMidCilindro;
    private double[] puntoOrbita = {450, 300, 700};

    private double anguloMaximo;
    private double anguloIncremento;

    public AnimacionCilindro(int frameWidth, int frameHeight, double[] origenCubo, double[] puntoFuga, LabelManager labelManager) {
        super(frameWidth, frameHeight, origenCubo, puntoFuga, labelManager);
        this.numPuntosActual = numPuntos;

        initEtiqueta();
        initVariables();
        initVertices();

        this.hiloCubo = new Thread(this);
        this.hiloCubo.start();
    }

    private void initEtiqueta() {
        JLabel etiquetaActual = new JLabel("Animacion Cilindro3D #" + (idObjeto + 1));
        this.labelManager.aniadirEtiqueta(etiquetaActual);
    }

    private void initVariables() {
        escala = 20;
        aumentoEscala = 5;
        animacionEjeX = true;

        Esferas sol = new Esferas(origenCubo, escala);
        Color[] solColores = new Color[]{
            new Color(161, 27, 8),
            new Color(192, 42, 10),
            new Color(255, 184, 55),
            new Color(234, 173, 55),
            new Color(255, 237, 205),
            new Color(255, 140, 21),};
        sol.colores = solColores;
        sol.setOrigenEsfera(0, 0, 0);
        sol.velocidadRotacionY = 0.2;
        sol.velocidadRotacionX = 0.05;
        listaEsferas.add(sol);

        Esferas mercurio = new Esferas(origenCubo, escala * 0.05);
        Color[] mercurioColores = new Color[]{
            new Color(159, 159, 159),
            new Color(116, 115, 125),
            new Color(144, 138, 113)};
        mercurio.colores = mercurioColores;
        mercurio.rotacionInversa = true;
        listaEsferas.add(mercurio);

        Esferas venus = new Esferas(origenCubo, escala * 0.11);
        Color[] venusColores = new Color[]{
            new Color(215, 200, 170),
            new Color(186, 148, 102),
            new Color(145, 104, 75)};
        venus.colores = venusColores;
        listaEsferas.add(venus);

        Esferas tierra = new Esferas(origenCubo, escala * 0.125);
        Color[] tierraColores = new Color[]{
            new Color(0, 122, 215),
            new Color(28, 100, 0),
            new Color(0, 144, 26)};
        tierra.colores = tierraColores;
        tierra.rotacionInversa = true;
        listaEsferas.add(tierra);

        Esferas marte = new Esferas(origenCubo, escala * 0.1);
        Color[] marteColores = new Color[]{
            new Color(89, 45, 37),
            new Color(235, 44, 22),
            new Color(182, 55, 35)};
        marte.colores = marteColores;
        listaEsferas.add(marte);

        Esferas jupiter = new Esferas(origenCubo, escala * 0.23);
        Color[] jupiterColores = new Color[]{
            new Color(227, 233, 233),
            new Color(224, 210, 143),
            new Color(175, 144, 107)};
        jupiter.colores = jupiterColores;
        listaEsferas.add(jupiter);
        
        Esferas saturno = new Esferas(origenCubo, escala * 0.19);
        Color[] saturnoColores = new Color[]{
            new Color(218, 188, 122),
            new Color(188, 145, 97),
            new Color(103, 89, 86)};
        saturno.colores = saturnoColores;
        saturno.rotacionInversa = true;
        listaEsferas.add(saturno);

        Esferas urano = new Esferas(origenCubo, escala * 0.13);
        Color[] uranoColores = new Color[]{
            new Color(226, 243, 241),
            new Color(87, 185, 223),
            new Color(12, 121, 169)};
        urano.colores = uranoColores;
        listaEsferas.add(urano);

        Esferas neptuno = new Esferas(origenCubo, escala * 0.12);
        Color[] neptunoColores = new Color[]{
            new Color(133, 212, 244),
            new Color(76, 122, 254),
            new Color(51, 57, 135)};
        neptuno.colores = neptunoColores;
        neptuno.rotacionInversa = true;
        listaEsferas.add(neptuno);

        Esferas pluton = new Esferas(origenCubo, escala * 0.06);
        Color[] plutonColores = new Color[]{
            new Color(203, 169, 140),
            new Color(163, 127, 102),
            new Color(121, 89, 62)};
        pluton.colores = plutonColores;
        listaEsferas.add(pluton);

        int angulo = 0;
        int yIncremento = -70;
        int radio = 130;
        for (int i = 1; i < listaEsferas.size(); i++) {
            int xTemp = (int) (radio * Math.cos(Math.toRadians(angulo)));
            int zTemp = (int) (radio * Math.sin(Math.toRadians(angulo)));
            int yTemp = yIncremento;

            listaEsferas.get(i).setOrigenEsfera(xTemp, yTemp, zTemp);

            angulo += 40;
            radio += (i <= listaEsferas.size() / 2) ? 35 : -35;
            yIncremento += 15;
        }

        mostrarPuntos = false;
        mostrarLineas = false;
        mostrarCaras = true;
        anguloMaximo = 2 * Math.PI;
        anguloIncremento = anguloMaximo / numPuntos;
    }

    private void initVertices() {
        verticesCilindro = new ArrayList<>();
        carasCilindro = new ArrayList<>();
        for (double alpha = 0; alpha < anguloMaximo; alpha += anguloIncremento) {
            for (double beta = 0; beta < anguloMaximo; beta += anguloIncremento) {
                double[] vertice = new double[3];
                vertice[0] = (2 + Math.cos(alpha)) * Math.cos(beta);
                vertice[1] = (2 + Math.cos(alpha)) * Math.sin(beta);
                vertice[2] = alpha;
                verticesCilindro.add(vertice);
            }
        }
        verticesTrasladadosCilindro = new double[verticesCilindro.size()][3];

        for (Esferas esfera : listaEsferas) {
            esfera.initVertices();
        }
    }

    private synchronized void dibujarCubo() {
        g2d.resetBuffer();
        transformarVertices();

        if (mostrarAnimacion) {
            orbitarPuntoCubo();
            rotaciones[0] += (animacionEjeX) ? 1 : 0;
            rotaciones[1] += (animacionEjeY) ? 1 : 0;
            rotaciones[2] += (animacionEjeZ) ? 1 : 0;
        }
        if (mostrarCaras) {
            dibujarCaras();
        }
        if (mostrarLineas) {
            dibujarLineas();
        }
        if (mostrarPuntos) {
            dibujarPuntos();
        }
    }

    private void orbitarPuntoCubo() {
        for (Esferas esfera : listaEsferas) {
            double[] puntoRelativo = {
                esfera.origenEsfera[0] - puntoOrbita[0],
                esfera.origenEsfera[1] - puntoOrbita[1],
                esfera.origenEsfera[2] - puntoOrbita[2]
            };

            int angulo = (esfera.rotacionInversa) ? -1 : 1;
            puntoRelativo = rotarY(puntoRelativo, angulo);

            esfera.origenEsfera[0] = puntoRelativo[0] + puntoOrbita[0];
            esfera.origenEsfera[1] = puntoRelativo[1] + puntoOrbita[1];
            esfera.origenEsfera[2] = puntoRelativo[2] + puntoOrbita[2];
        }
    }

    private void transformarVertices() {
        try {
//            verticesTrasladadosCilindro = new double[verticesCilindro.size()][3];
//            carasCilindro = new ArrayList<>();
//            zMidCilindro = new ArrayList<>();

//            for (int i = 0; i < verticesCilindro.size(); i++) {
//                double[] vertice = verticesCilindro.get(i);
//                vertice = rotarX(vertice, rotaciones[0]);
//                vertice = rotarY(vertice, rotaciones[1]);
////                vertice = rotarZ(vertice, rotaciones[2]);
//
//                double[] trasladado = {
//                    (vertice[0] * escala) + traslaciones[0] + origenCubo[0],
//                    (vertice[1] * escala) + traslaciones[1] + origenCubo[1],
//                    (vertice[2] * escala) + traslaciones[2] + origenCubo[2]
//                };
//                verticesTrasladadosCilindro[i] = trasladado;
//            }
//            contadorColores = 0;
//            for (int i = 0; i < numPuntos - 1; i++) {
//                for (int j = 0; j < numPuntos; j++) {
//                    int index0 = i * numPuntos + j;
//                    int index1 = (i + 1) * numPuntos + j;
//                    int index2 = i * numPuntos + (j + 1) % numPuntos;
//                    int index3 = (i + 1) * numPuntos + (j + 1) % numPuntos;
//
//                    double[] vertice0 = verticesTrasladadosCilindro[index0];
//                    double[] vertice1 = verticesTrasladadosCilindro[index1];
//                    double[] vertice2 = verticesTrasladadosCilindro[index2];
//                    double[] vertice3 = verticesTrasladadosCilindro[index3];
//
//                    Point2D p0 = punto3D_a_2D(vertice0[0], vertice0[1], vertice0[2]);
//                    Point2D p1 = punto3D_a_2D(vertice1[0], vertice1[1], vertice1[2]);
//                    Point2D p2 = punto3D_a_2D(vertice2[0], vertice2[1], vertice2[2]);
//                    Point2D p3 = punto3D_a_2D(vertice3[0], vertice3[1], vertice3[2]);
//
//                    carasCilindro.add(new Point2D[]{p0, p1, p2, p3});
//
//                    double midZ = (vertice0[2] + vertice1[2] + vertice2[2] + vertice3[2]) / 4;
//                    zMidCilindro.add(midZ);
//                }
//            }
            for (Esferas esfera : listaEsferas) {
                esfera.transformarVertices(rotaciones, traslaciones);
            }
        } catch (Exception e) {
        }
    }

    private void dibujarPuntos() {
//        g2d.setColor(Color.WHITE);
//        for (double[] v : verticesTrasladadosCilindro) {
//            double x = v[0];
//            double y = v[1];
//            double z = v[2];
//            Point2D.Double p1 = punto3D_a_2D(x, y, z);
//            g2d.fillCircle3D((int) p1.x, (int) p1.y, 2, (int) 2);
//        }

        g2d.setColor(Color.WHITE);
        for (Esferas esfera : listaEsferas) {
            for (int i = 0; i < esfera.verticesTrasladadosEsfera.length; i++) {
                double[] v = esfera.verticesTrasladadosEsfera[i];
                double x = v[0];
                double y = v[1];
                double z = v[2];
                Point2D.Double p1 = punto3D_a_2D(x, y, z);
                g2d.fillCircle3D((int) p1.x, (int) p1.y, 2, (int) 2);
            }
        }
    }

    private void dibujarCaras() {
//        for (int i = 0; i < carasCilindro.size(); i++) {
//            Point2D[] cara = carasCilindro.get(i);
//
//            Polygon poly = new Polygon();
//
//            Point2D pp0 = cara[0];
//            Point2D pp1 = cara[1];
//            Point2D pp2 = cara[2];
//            Point2D pp3 = cara[3];
//
//            poly.addPoint((int) pp0.getX(), (int) pp0.getY());
//            poly.addPoint((int) pp1.getX(), (int) pp1.getY());
//            poly.addPoint((int) pp2.getX(), (int) pp2.getY());
//            poly.addPoint((int) pp3.getX(), (int) pp3.getY());
//
//            g2d.setColor(colores[contadorColores % colores.length]);
//            double midZ = zMidCilindro.get(i);
//            g2d.fillPolygon3D(poly, midZ);
//            contadorColores++;
//        }

        for (Esferas esfera : listaEsferas) {
            contadorColores = 0;
            for (int[] cara : esfera.carasEsfera) {
                Polygon poly = new Polygon();
                double midZIndez = 0;
                for (int i = 0; i < cara.length; i++) {
                    double[] vertice = esfera.verticesTrasladadosEsfera[cara[i]];
                    int xPoints = (int) (vertice[0]);
                    int yPoints = (int) (vertice[1]);
                    int zPoints = (int) (vertice[2]);
                    midZIndez += zPoints;
                    Point2D.Double punto = punto3D_a_2D(xPoints, yPoints, zPoints);
                    poly.addPoint((int) punto.x, (int) punto.y);
                }
                midZIndez /= cara.length;
                g2d.setColor(esfera.colores[contadorColores++ % esfera.colores.length]);
                g2d.fillPolygon3D(poly, midZIndez);
            }
        }
    }

    private void dibujarLineas() {
//        for (Point2D[] cara : carasCilindro) {
//            Point2D pp0 = cara[0];
//            Point2D pp1 = cara[1];
//            Point2D pp2 = cara[2];
//            Point2D pp3 = cara[3];
//
//            g2d.setColor(Color.WHITE);
//            g2d.drawLine((int) pp0.getX(), (int) pp0.getY(), (int) pp1.getX(), (int) pp1.getY());
//            g2d.drawLine((int) pp1.getX(), (int) pp1.getY(), (int) pp3.getX(), (int) pp3.getY());
//            g2d.drawLine((int) pp3.getX(), (int) pp3.getY(), (int) pp2.getX(), (int) pp2.getY());
//            g2d.drawLine((int) pp2.getX(), (int) pp2.getY(), (int) pp0.getX(), (int) pp0.getY());
//        }

        for (Esferas esfera : listaEsferas) {
            g2d.setColor(Color.WHITE);
            for (int[] cara : esfera.carasEsfera) {
                Polygon poly = new Polygon();
                for (int i = 0; i < cara.length - 1; i++) {
                    double[] vertice = esfera.verticesTrasladadosEsfera[cara[i]];
                    int xPoints = (int) (vertice[0]);
                    int yPoints = (int) (vertice[1]);
                    int zPoints = (int) (vertice[2]);
                    Point2D.Double punto = punto3D_a_2D(xPoints, yPoints, zPoints);
                    poly.addPoint((int) punto.x, (int) punto.y);

                    double[] vertice2 = esfera.verticesTrasladadosEsfera[cara[i + 1]];
                    int xPoints2 = (int) (vertice2[0]);
                    int yPoints2 = (int) (vertice2[1]);
                    int zPoints2 = (int) (vertice2[2]);
                    Point2D.Double punto2 = punto3D_a_2D(xPoints2, yPoints2, zPoints2);
                    poly.addPoint((int) punto2.x, (int) punto2.y);

                    g2d.drawLine((int) punto.x, (int) punto.y, (int) punto2.x, (int) punto2.y);
                }
            }
        }
    }

    @Override
    public void run() {
        int fps = 60;
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

            if (numPuntosActual != numPuntos) {
                initVertices();
                this.numPuntosActual = numPuntos;
            }

            if (System.nanoTime() - tiempoAnteriorLabel >= 250000000) { // 250 ms en nanosegundos
                labelManager.actualizarEtiquetaInformacion(idObjeto, getInformacionObjeto());
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
