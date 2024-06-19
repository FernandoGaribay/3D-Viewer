package graficos;

import Interfaces.LabelManager;
import animaciones.Esferas;
import enums.Alternaciones;
import static enums.Alternaciones.ILUMINACION;
import static enums.Alternaciones.ROTACION;
import static enums.Alternaciones.TRASLACION;
import iluminacion.IluminacionPhong;
import java.awt.Color;
import java.awt.Polygon;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import utils.Constantes;

public class Objeto3D {

    // Contador estatico para el numero de Objetos3D
    protected static int contadorObjetos;
    protected final int idObjeto;
    public static boolean animacionSeleccionActiva;
    public static boolean animacionDeseleccionActiva;

    // SubObjetos
    protected ArrayList<Esferas> listaEsferas = new ArrayList<>();

    // Iluminacion Phong
    protected IluminacionPhong phong;
    protected float brilloEspecular;
    protected float aumentoBrillo;
    protected float[] origenLuz;
    protected float[] vectorNormal;
    protected Color colorAmbiente;
    protected Color colorLuz;

    // Objetos grafico para dibujar el Objeto3D 
    protected final MyGraphics g2d;
    protected LabelManager labelManager;

    // Coordenadas y transformaciones del cubo
    protected int numPuntos;
    protected double[] origenCubo;
    protected double[] puntoFuga;

    // Escala y transformaciones
    protected double escala;
    protected double aumentoEscala;
    protected double[] rotaciones; // Rotaciones en los ejes X, Y, Z
    protected int velRotacion;
    protected double[] traslaciones; // Traslaciones en los ejes X, Y, Z

    // Variables tag informacion
    protected int fpsActuales = 0;

    // Estado de visualizacion y animacion
    public Alternaciones estadoAlternacion;
    protected boolean sinNormales;
    protected boolean mostrarAnimacion;
    protected boolean mostrarOrigenLuz;
    protected boolean mostrarLuz;
    protected boolean mostrarPuntos;
    protected boolean mostrarLineas;
    protected boolean mostrarCaras;
    protected boolean animacionEjeX;
    protected boolean animacionEjeY;
    protected boolean animacionEjeZ;

    // Estado de seleccion
    protected boolean seleccionado;

    // Colores de las caras
    protected int contadorColores;
    protected Color[] colores;

    protected double[][] verticesTrasladados;
    protected ArrayList<double[]> vertices;
    protected ArrayList<double[]> normales;
    protected ArrayList<int[]> caras;
    protected ArrayList<int[]> normalesPorCara;

    static {
        contadorObjetos = 0;
        animacionSeleccionActiva = false;
        animacionDeseleccionActiva = false;
    }

    public Objeto3D(int frameWidth, int frameHeight, double[] origenCubo, double[] puntoFuga, LabelManager labelManager) {
        this.idObjeto = contadorObjetos++;

        this.g2d = new MyGraphics(frameWidth, frameHeight);
        this.labelManager = labelManager;
        this.origenCubo = origenCubo;
        this.puntoFuga = puntoFuga;

        initBanderas();
        initVariables();
    }

    private void initBanderas() {
        this.estadoAlternacion = Alternaciones.ROTACION;
        this.sinNormales = true;
        this.seleccionado = false;
        this.mostrarAnimacion = false;
        this.mostrarOrigenLuz = false;
        this.mostrarLuz = false;
        this.mostrarPuntos = false;
        this.mostrarLineas = true;
        this.mostrarCaras = false;
        this.animacionEjeX = false;
        this.animacionEjeY = true;
        this.animacionEjeZ = false;
    }

    private void initVariables() {
        this.brilloEspecular = 32.0f;
        this.aumentoBrillo = 0.5f;

        this.origenLuz = new float[]{450, 300, 650};
        this.vectorNormal = new float[]{0.0f, 0.0f, 1.0f};
        this.colorAmbiente = new Color(38, 38, 38);
        this.colorLuz = new Color(255, 255, 255);
        this.phong = new IluminacionPhong(colorAmbiente, origenLuz, colorLuz);

        this.escala = 100;
        this.numPuntos = 50;
        this.aumentoEscala = 10;
        this.velRotacion = 5;
        this.traslaciones = new double[3];
        this.rotaciones = new double[3];
    }

    public void initColores(int numColores) {
        Random rand = new Random();
        colores = new Color[numColores];
        contadorColores = 0;

        for (int i = 0; i < numColores; i++) {
            colores[i] = Color.getHSBColor(rand.nextFloat(), 1, 1);
        }

        for (Esferas listaEsfera : listaEsferas) {
            listaEsfera.initColores(numColores);
        }
    }

    protected double[] rotarX(double[] point, double angle) {
        double[] result = new double[3];
        result[0] = point[0];
        result[1] = point[1] * Math.cos(Math.toRadians(angle)) - point[2] * Math.sin(Math.toRadians(angle));
        result[2] = point[1] * Math.sin(Math.toRadians(angle)) + point[2] * Math.cos(Math.toRadians(angle));
        return result;
    }

    protected double[] rotarY(double[] point, double angle) {
        double[] result = new double[3];
        result[0] = point[0] * Math.cos(Math.toRadians(angle)) + point[2] * Math.sin(Math.toRadians(angle));
        result[1] = point[1];
        result[2] = -point[0] * Math.sin(Math.toRadians(angle)) + point[2] * Math.cos(Math.toRadians(angle));
        return result;
    }

    protected double[] rotarZ(double[] point, double angle) {
        double[] result = new double[3];
        result[0] = point[0] * Math.cos(Math.toRadians(angle)) - point[1] * Math.sin(Math.toRadians(angle));
        result[1] = point[0] * Math.sin(Math.toRadians(angle)) + point[1] * Math.cos(Math.toRadians(angle));
        result[2] = point[2];
        return result;
    }

    protected Point2D.Double punto3D_a_2D(double x, double y, double z) {
        double u = -puntoFuga[2] / (z - puntoFuga[2]);

        double px = puntoFuga[0] + (x - puntoFuga[0]) * u;
        double py = puntoFuga[1] + (y - puntoFuga[1]) * u;

//        double xf = puntoFuga[0];
//        double yf = puntoFuga[1];
//        double zf = puntoFuga[2];
//        double distanciaFocal = 250; // 250
//
//        double px = (distanciaFocal * -(x - xf)) / (z - zf) + xf;
//        double py = (distanciaFocal * -(y - yf)) / (z - zf) + yf;
        return new Point2D.Double(px, py);
    }

// <editor-fold defaultstate="collapsed" desc="Metodos de transformacion">
    public void trasladarLuzX(int distancia) {
        this.origenLuz[0] += distancia;
    }

    public void trasladarLuzY(int distancia) {
        this.origenLuz[1] += distancia;
    }

    public void trasladarLuzZ(int distancia) {
        this.origenLuz[2] += distancia;
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

    public void setEscala(double escala) {
        this.escala += escala;
    }

    public void setBrillo(double brilloEspecular) {
        this.brilloEspecular += brilloEspecular;
    }

    public void setAlternacionRTI() {
        switch (estadoAlternacion) {
            case ROTACION:
                estadoAlternacion = Alternaciones.TRASLACION;
                break;
            case TRASLACION:
                estadoAlternacion = Alternaciones.ILUMINACION;
                break;
            case ILUMINACION:
                estadoAlternacion = Alternaciones.ROTACION;
                break;
        }
        System.out.println("estadoAlternacion : " + estadoAlternacion);
    }

    public void setConSinNormales() {
        this.sinNormales = !sinNormales;
    }

    public void setRotacionTransformacionMouse(int x, int y) {
        traslaciones[0] -= x;
        traslaciones[1] -= y;
    }

    public void setTraslacionLuzMouse(int x, int y) {
        origenLuz[0] -= x;
        origenLuz[1] -= y;
    }

    public void setRotacionTransformacionArriba() {
        switch (estadoAlternacion) {
            case ROTACION:
                rotaciones[0] += velRotacion;
                rotarX(puntoFuga, escala);
                break;
            case TRASLACION:
                trasladarY(10);
                break;
            case ILUMINACION:
                trasladarLuzY(5);
                break;
        }
    }

    public void setRotacionTransformacionAbajo() {
        switch (estadoAlternacion) {
            case ROTACION:
                rotaciones[0] -= velRotacion;
                rotarX(puntoFuga, escala);
                break;
            case TRASLACION:
                trasladarY(-10);
                break;
            case ILUMINACION:
                trasladarLuzY(-5);
                break;
        }
    }

    public void setRotacionTransformacionIzquierda() {
        switch (estadoAlternacion) {
            case ROTACION:
                rotaciones[1] -= velRotacion;
                rotarX(puntoFuga, escala);
                break;
            case TRASLACION:
                trasladarX(10);
                break;
            case ILUMINACION:
                trasladarLuzX(5);
                break;
        }
    }

    public void setRotacionTransformacionDerecha() {
        switch (estadoAlternacion) {
            case ROTACION:
                rotaciones[1] += velRotacion;
                rotarX(puntoFuga, escala);
                break;
            case TRASLACION:
                trasladarX(-10);
                break;
            case ILUMINACION:
                trasladarLuzX(-5);
                break;
        }
    }

    public void setRotacionTransformacionZPositiva() {
        switch (estadoAlternacion) {
            case ROTACION:
                rotaciones[2] -= velRotacion;
                rotarX(puntoFuga, escala);
                break;
            case TRASLACION:
                trasladarZ(-10);
                break;
            case ILUMINACION:
                trasladarLuzZ(5);
                break;
        }
    }

    public void setRotacionTransformacionZNegativa() {
        switch (estadoAlternacion) {
            case ROTACION:
                rotaciones[2] += velRotacion;
                rotarX(puntoFuga, escala);
                break;
            case TRASLACION:
                trasladarZ(10);
                break;
            case ILUMINACION:
                trasladarLuzZ(-5);
                break;
        }
    }
// </editor-fold>

// <editor-fold defaultstate="collapsed" desc="Banderas para el estado de visualizacion y animacion">
    public void setMostrarAnimacion() {
        this.mostrarAnimacion = !mostrarAnimacion;
    }

    public void setMostrarOrigenLuz() {
        this.mostrarOrigenLuz = !mostrarOrigenLuz;
    }

    public void setMostrarLuz() {
        this.mostrarLuz = !mostrarLuz;
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
// </editor-fold>

// <editor-fold defaultstate="collapsed" desc="Metodos para el dibujado de iluminacion">
    public void mostrarOrigenLuz() {
        Point2D pLight = punto3D_a_2D(origenLuz[0], origenLuz[1], origenLuz[2]);
        g2d.setColor(Color.BLACK);
        g2d.fillCircle((int) pLight.getX(), (int) pLight.getY(), 6);
        g2d.setColor(Color.WHITE);
        g2d.fillCircle((int) pLight.getX(), (int) pLight.getY(), 4);
    }

    public double calcularMidZIndez(int[] cara, Polygon poly) {
        double midZIndez = 0;
        for (int indice : cara) {
            double[] vertice = verticesTrasladados[indice];
            int xPoints = (int) vertice[0];
            int yPoints = (int) vertice[1];
            int zPoints = (int) vertice[2];
            midZIndez += zPoints;
            Point2D.Double punto = punto3D_a_2D(xPoints, yPoints, zPoints);
            poly.addPoint((int) punto.x, (int) punto.y);
        }
        return midZIndez / cara.length;
    }

    public void dibujarConLuz(Polygon poly, double midZIndez, int[] cara) {
        float[][] v = convertirVertices(cara);
        float[] color = phong.getColorIluminacion(colores[contadorColores++ % colores.length], brilloEspecular, v[0]);
        g2d.setColor(new Color(color[0], color[1], color[2]));
        g2d.fillPolygon3D(poly, midZIndez);
    }

    public void dibujarConLuz(Polygon poly, double midZIndez, int[] cara, int[] normalesCara) {
        float[][] v = convertirVertices(cara);
//        System.out.println("v: " + v.length);
        float[][] n = convertirNormales(normalesCara);
//        System.out.println("n: " + n.length);
        float[] color = phong.getColorIluminacion(colores[contadorColores++ % colores.length], brilloEspecular, v[0], n[0]);
        g2d.setColor(new Color(color[0], color[1], color[2]));
        g2d.fillPolygon3D(poly, midZIndez);
    }

    public float[][] convertirNormales(int[] normalesCara) {
        float[][] n = new float[normalesCara.length][];

        for (int i = 0; i < normalesCara.length; i++) {
            double[] normal = normales.get(normalesCara[i]);
            n[i] = arrayDoubleToFloat(normal);
        }
        return n;
    }

    public float[][] convertirVertices(int[] cara) {
        float[][] v = new float[cara.length][];
        for (int i = 0; i < cara.length; i++) {
            double[] vertice = verticesTrasladados[cara[i]];
            v[i] = arrayDoubleToFloat(vertice);
        }
        return v;
    }

    public void dibujarSinLuz(Polygon poly, double midZIndez) {
        g2d.setColor(colores[contadorColores++ % colores.length]);
        g2d.fillPolygon3D(poly, midZIndez);
    }

// </editor-fold>
    
// <editor-fold defaultstate="collapsed" desc="Banderas para el estado de seleccion">
    public void setSeleccionado(boolean seleccionado) {
        this.seleccionado = seleccionado;
    }

    public boolean isSeleccionado() {
        return this.seleccionado;
    }
// </editor-fold>

    protected String getInformacionObjeto() {
        String newInformacion = "<html><div style='text-align: right;'>------------------- INFORMACION -------------------<br><br>"
                + "ID OBJETO: #" + (idObjeto + 1) + "<br>"
                + "FPS: " + fpsActuales + "<br><br>"
                + "Banderas del objeto:<br>"
                + "Origen Luz: " + mostrarOrigenLuz + "<br>"
                + "Puntos: " + mostrarPuntos + "<br>"
                + "Lineas: " + mostrarLineas + "<br>"
                + "Caras: " + mostrarCaras + "<br>"
                + "Luz: " + mostrarLuz + "<br>"
                + "Brillo: " + brilloEspecular + "<br><br>"
                + "Coordenadas del objeto:<br>"
                + "Escala -> " + String.format("%.2f", escala) + " pixeles<br>"
                + "X -> " + traslaciones[0] + " pixeles<br>"
                + "Y -> " + traslaciones[1] + " pixeles<br>"
                + "Z -> " + traslaciones[2] + " pixeles<br><br>"
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
        return newInformacion;
    }

    public void iniciarAnimacionSeleccionado() {
        Thread hiloAnimacion = new Thread(() -> {
            animacionSeleccionActiva = true;

            int tiempoAnimacion = Constantes.TIEMPO_ANIMACION_SELECCION;
            long tiempoPorFotograma = tiempoAnimacion / 60;
            int numIteraciones = (int) (tiempoAnimacion / tiempoPorFotograma);
            long tiempoInicio = System.currentTimeMillis();
            long tiempoTranscurrido = 0;

            final double escalaOriginal = escala;
            double incrementoEscala = escalaOriginal / numIteraciones;

            final double[] traslacionesOriginales = new double[traslaciones.length];
            System.arraycopy(traslaciones, 0, traslacionesOriginales, 0, traslaciones.length);
            double incrementoTraslacionX = (Constantes.TRANSFORMACION_ANIMACION_SELECCION_X) / numIteraciones;

            setSeleccionado(true);
            escala = 0;
            traslaciones[0] += Constantes.TRANSFORMACION_ANIMACION_SELECCION_X;
            while (tiempoTranscurrido <= tiempoAnimacion) {
                tiempoTranscurrido = System.currentTimeMillis() - tiempoInicio;

                escala += incrementoEscala;
                traslaciones[0] -= incrementoTraslacionX;

                try {
                    Thread.sleep(tiempoPorFotograma);
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
            }
            escala = escalaOriginal;
            System.arraycopy(traslacionesOriginales, 0, traslaciones, 0, traslaciones.length);

            animacionSeleccionActiva = false;
        });
        hiloAnimacion.start();
    }

    public void iniciarAnimacionDeseleccionado() {
        Thread hiloAnimacion = new Thread(() -> {
            animacionDeseleccionActiva = true;

            int tiempoAnimacion = Constantes.TIEMPO_ANIMACION_SELECCION;
            long tiempoPorFotograma = tiempoAnimacion / 60;
            int numIteraciones = (int) (tiempoAnimacion / tiempoPorFotograma);
            long tiempoInicio = System.currentTimeMillis();
            long tiempoTranscurrido = 0;

            final double escalaOriginal = escala;
            double incrementoEscala = escalaOriginal / numIteraciones;

            final double[] traslacionesOriginales = new double[traslaciones.length];
            System.arraycopy(traslaciones, 0, traslacionesOriginales, 0, traslaciones.length);
            double incrementoTraslacionX = (Constantes.TRANSFORMACION_ANIMACION_SELECCION_X) / numIteraciones;

            while (tiempoTranscurrido <= tiempoAnimacion) {
                tiempoTranscurrido = System.currentTimeMillis() - tiempoInicio;

                escala -= incrementoEscala;
                traslaciones[0] -= incrementoTraslacionX;

                try {
                    Thread.sleep(tiempoPorFotograma);
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
            }
            setSeleccionado(false);

            try {
                Thread.sleep(100);
            } catch (InterruptedException ex) {
                Logger.getLogger(Objeto3D.class.getName()).log(Level.SEVERE, null, ex);
            }
            escala = escalaOriginal;
            System.arraycopy(traslacionesOriginales, 0, traslaciones, 0, traslaciones.length);

            animacionDeseleccionActiva = false;
        });
        hiloAnimacion.start();
    }

    public static float[] arrayDoubleToFloat(double[] doubleArray) {
        float[] floatArray = new float[doubleArray.length];

        for (int i = 0; i < doubleArray.length; i++) {
            floatArray[i] = (float) doubleArray[i];
        }

        return floatArray;
    }

    // Metodo para regresar el buffer
    public synchronized BufferedImage getBuffer() {
        return g2d.getBuffer();
    }

    public int getIdObjeto() {
        return idObjeto;
    }

    public double getAumentoEscala() {
        return aumentoEscala;
    }

    public void setAumentoEscala(double aumentoEscala) {
        this.aumentoEscala = aumentoEscala;
    }

    public float getAumentoBrillo() {
        return aumentoBrillo;
    }

    public void setAumentoBrillo(float aumentoBrillo) {
        this.aumentoBrillo = aumentoBrillo;
    }

    public void setNumPuntos(int numPuntos) {
        this.numPuntos = numPuntos;
    }

}
