package animaciones;

import java.awt.Color;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Random;
import modelos3D.LectorOBJ;
import modelos3D.Modelo3D;

public class Esferas {

    private Modelo3D modelo;
    public Color[] colores;

    public double escala;
    public double multiplicadorEscala;

    public double velocidadRotacionX = 1;
    public double velocidadRotacionY = 1;
    public double velocidadRotacionZ = 1;
    public double velocidadOrbitar = 1;

    public double[][] verticesTrasladadosEsfera;
    public ArrayList<double[]> verticesEsfera;
    public ArrayList<int[]> carasEsfera;

    public boolean rotacionInversa = false;
    public double[] puntoOrbita;
    public double[] origenCubo;
    public double[] origenEsfera;
    
    public Esferas(double[] origenCubo, double[] puntoOrbita, double multiplicadorEscala) {
        this.modelo = new Modelo3D();
        this.origenCubo = origenCubo;
        this.puntoOrbita = puntoOrbita;
        this.multiplicadorEscala = multiplicadorEscala;
    }

    public void initColores(int numColores) {
        colores = new Color[numColores];

        Random rand = new Random();
        for (int i = 0; i < numColores; i++) {
            colores[i] = Color.getHSBColor(rand.nextFloat(), 1, 1);
        }
    }

    public void initVertices() {
        ClassLoader classLoader = getClass().getClassLoader();
        InputStream inputStream = classLoader.getResourceAsStream("recursos/esfera.obj");

        modelo = LectorOBJ.readObjFile(inputStream);
        verticesEsfera = modelo.getVertices();
        carasEsfera = modelo.getCaras();
        verticesTrasladadosEsfera = new double[verticesEsfera.size()][3];
    }

    public void orbitarEsfera() {
        double[] puntoRelativo = {
            origenEsfera[0] - puntoOrbita[0],
            origenEsfera[1] - puntoOrbita[1],
            origenEsfera[2] - puntoOrbita[2]
        };

        double angulo = (rotacionInversa) ? -velocidadOrbitar : velocidadOrbitar;
        puntoRelativo = rotarY(puntoRelativo, angulo);

        origenEsfera[0] = puntoRelativo[0] + puntoOrbita[0];
        origenEsfera[1] = puntoRelativo[1] + puntoOrbita[1];
        origenEsfera[2] = puntoRelativo[2] + puntoOrbita[2];
    }

    public void transformarVertices(double[] rotaciones, double[] traslaciones) {
        for (int i = 0; i < verticesEsfera.size(); i++) {
            double[] vertice = verticesEsfera.get(i);
            vertice = rotarX(vertice, rotaciones[0] * velocidadRotacionX);
            vertice = rotarY(vertice, rotaciones[1] * velocidadRotacionY);
            vertice = rotarZ(vertice, rotaciones[2] * velocidadRotacionZ);
            verticesTrasladadosEsfera[i] = vertice;

            double[] trasladado = {
                (vertice[0] * escala) + origenEsfera[0] + traslaciones[0],
                (vertice[1] * escala) + origenEsfera[1] + traslaciones[1],
                (vertice[2] * escala) + origenEsfera[2] + traslaciones[2]
            };
            verticesTrasladadosEsfera[i] = trasladado;
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

    public void setOrigenEsfera(int x, int y, int z) {
        origenEsfera = new double[]{
            origenCubo[0] - x,
            origenCubo[1] + y,
            origenCubo[2] - z
        };
    }

    public void setEscala(double escala) {
        this.escala = escala * multiplicadorEscala;
    }
}
