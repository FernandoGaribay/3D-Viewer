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
import java.util.Random;
import utils.Constantes;

public class AnimacionSistemaSolar extends Objeto3D implements Runnable {

    private final Thread hiloCubo;

    private final double[] puntoOrbita = {450, 300, 700};

    private Random random;
    private int numEstrellas;
    private int[][] estrellas;

    private double escalaActual;

    public AnimacionSistemaSolar(int frameWidth, int frameHeight, double[] origenCubo, double[] puntoFuga, LabelManager labelManager) {
        super(frameWidth, frameHeight, origenCubo, puntoFuga, labelManager);

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
        numEstrellas = 40;
        estrellas = new int[numEstrellas][];
        random = new Random();
        escala = 20;
        escalaActual = escala;
        aumentoEscala = 0.5;
        animacionEjeX = true;

        for (int i = 0; i < estrellas.length; i++) {
            int x = (int) random.nextInt(800) + 50;
            int y = (int) random.nextInt(500) + 50;
            estrellas[i] = new int[]{
                x, y
            };
        }

        Esferas sol = new Esferas(origenCubo, puntoOrbita, 1);
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

        Esferas mercurio = new Esferas(origenCubo, puntoOrbita, 0.05);
        Color[] mercurioColores = new Color[]{
            new Color(159, 159, 159),
            new Color(116, 115, 125),
            new Color(144, 138, 113)};
        mercurio.colores = mercurioColores;
        mercurio.rotacionInversa = true;
        mercurio.velocidadOrbitar = 0.1;
        listaEsferas.add(mercurio);

        Esferas venus = new Esferas(origenCubo, puntoOrbita, 0.11);
        Color[] venusColores = new Color[]{
            new Color(215, 200, 170),
            new Color(186, 148, 102),
            new Color(145, 104, 75)};
        venus.colores = venusColores;
        venus.velocidadOrbitar = 0.15;
        listaEsferas.add(venus);

        Esferas tierra = new Esferas(origenCubo, puntoOrbita, 0.125);
        Color[] tierraColores = new Color[]{
            new Color(0, 122, 215),
            new Color(28, 100, 0),
            new Color(0, 144, 26)};
        tierra.colores = tierraColores;
        tierra.rotacionInversa = true;
        listaEsferas.add(tierra);

        Esferas marte = new Esferas(origenCubo, puntoOrbita, 0.1);
        Color[] marteColores = new Color[]{
            new Color(89, 45, 37),
            new Color(235, 44, 22),
            new Color(182, 55, 35)};
        marte.colores = marteColores;
        marte.velocidadOrbitar = 0.1;
        listaEsferas.add(marte);

        Esferas jupiter = new Esferas(origenCubo, puntoOrbita, 0.23);
        Color[] jupiterColores = new Color[]{
            new Color(227, 233, 233),
            new Color(224, 210, 143),
            new Color(175, 144, 107)};
        jupiter.colores = jupiterColores;
        listaEsferas.add(jupiter);

        Esferas saturno = new Esferas(origenCubo, puntoOrbita, 0.19);
        Color[] saturnoColores = new Color[]{
            new Color(218, 188, 122),
            new Color(188, 145, 97),
            new Color(103, 89, 86)};
        saturno.colores = saturnoColores;
        saturno.rotacionInversa = true;
        saturno.velocidadOrbitar = 0.09;
        listaEsferas.add(saturno);

        Esferas urano = new Esferas(origenCubo, puntoOrbita, 0.13);
        Color[] uranoColores = new Color[]{
            new Color(226, 243, 241),
            new Color(87, 185, 223),
            new Color(12, 121, 169)};
        urano.colores = uranoColores;
        urano.velocidadOrbitar = 1.6;
        listaEsferas.add(urano);

        Esferas neptuno = new Esferas(origenCubo, puntoOrbita, 0.12);
        Color[] neptunoColores = new Color[]{
            new Color(133, 212, 244),
            new Color(76, 122, 254),
            new Color(51, 57, 135)};
        neptuno.colores = neptunoColores;
        neptuno.rotacionInversa = true;
        neptuno.velocidadOrbitar = 1.4;
        listaEsferas.add(neptuno);

        Esferas pluton = new Esferas(origenCubo, puntoOrbita, 0.06);
        Color[] plutonColores = new Color[]{
            new Color(203, 169, 140),
            new Color(163, 127, 102),
            new Color(121, 89, 62)};
        pluton.colores = plutonColores;
        listaEsferas.add(pluton);

        int angulo = 0;
        int yIncremento = -150;
        int radio = 130;
        for (int i = 1; i < listaEsferas.size(); i++) {
            int xTemp = (int) (radio * Math.cos(Math.toRadians(angulo)));
            int zTemp = (int) (radio * Math.sin(Math.toRadians(angulo)));
            int yTemp = yIncremento;

            listaEsferas.get(i).setOrigenEsfera(xTemp, yTemp, zTemp);

            angulo += 40;
            radio += (i <= listaEsferas.size() / 2) ? escala * 2 : -escala * 2;
            yIncremento += escala * 1.65;
        }

        mostrarPuntos = false;
        mostrarLineas = false;
        mostrarCaras = true;
    }

    private void initVertices() {
        for (Esferas esfera : listaEsferas) {
            esfera.setEscala(escala);
            esfera.initVertices();
        }
    }

    private synchronized void dibujarCubo() {
        g2d.resetBuffer();
        dibujarEstrellas();
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
        if (mostrarOrigenLuz) {
            mostrarOrigenLuz();
        }
    }

    private void dibujarEstrellas() {
        g2d.setColor(Color.WHITE);
        for (int[] estrella : estrellas) {
            g2d.fillCircle3D(estrella[0], estrella[1], random.nextInt(2), 1500);
        }
    }

    private void orbitarPuntoCubo() {
        for (Esferas esfera : listaEsferas) {
            esfera.orbitarEsfera();
        }
    }

    private void transformarVertices() {
        try {
            for (Esferas esfera : listaEsferas) {
                esfera.transformarVertices(rotaciones, traslaciones);
            }
        } catch (Exception e) {
        }
    }

    private void dibujarPuntos() {
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
        for (Esferas esfera : listaEsferas) {
            contadorColores = 0;
            for (int[] cara : esfera.carasEsfera) {
                Polygon poly = new Polygon();
                float[][] vertices = new float[cara.length][];
                double midZIndez = 0;
                for (int i = 0; i < cara.length; i++) {
                    double[] vertice = esfera.verticesTrasladadosEsfera[cara[i]];
                    vertices[i] = arrayDoubleToFloat(vertice);
                    int xPoints = (int) (vertice[0]);
                    int yPoints = (int) (vertice[1]);
                    int zPoints = (int) (vertice[2]);
                    midZIndez += zPoints;
                    Point2D.Double punto = punto3D_a_2D(xPoints, yPoints, zPoints);
                    poly.addPoint((int) punto.x, (int) punto.y);
                }
                midZIndez /= cara.length;
                if (mostrarLuz) {
                    float[] color = phong.getIluminacionColor(esfera.colores[contadorColores % esfera.colores.length], brilloEspecular, vertices[0]);
                    g2d.setColor(new Color(color[0], color[1], color[2]));
                } else {
                    g2d.setColor(esfera.colores[contadorColores % esfera.colores.length]);
                }
                g2d.fillPolygon3D(poly, midZIndez);
                contadorColores++;
            }
        }
    }

    private void dibujarLineas() {
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

            if (escalaActual != escala) {
                initVertices();
                this.escalaActual = escala;
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
