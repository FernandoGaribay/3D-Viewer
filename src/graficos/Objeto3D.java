package graficos;

import Interfaces.LabelManager;
import java.awt.Color;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;

public class Objeto3D {

    // Contador estatico para el numero de Objetos3D
    protected static int contadorObjetos;
    protected final int idObjeto;

    // Objetos grafico para dibujar el Objeto3D 
    protected final MyGraphics g2d;
    protected LabelManager labelManager;

    // Coordenadas y transformaciones del cubo
    protected double[] origenCubo;
    protected double[] puntoFuga;

    // Escala y transformaciones
    protected int escala;
    protected double[] rotaciones; // Rotaciones en los ejes X, Y, Z
    protected double[] traslaciones; // Traslaciones en los ejes X, Y, Z

    // Estado de visualizacion y animacion
    protected boolean mostrarAnimacion;
    protected boolean traslacion;
    protected boolean mostrarPuntos;
    protected boolean mostrarLineas;
    protected boolean mostrarCaras;
    protected boolean animacionEjeX;
    protected boolean animacionEjeY;
    protected boolean animacionEjeZ;

    // Estado de seleccion
    protected boolean seleccionado;

    // Colores de las caras
    protected Color[] colores = {
        new Color(204, 204, 204),
        new Color(153, 204, 0),
        new Color(255, 102, 102),
        new Color(204, 51, 255),
        new Color(255, 204, 204),
        new Color(0, 51, 255),};
    protected int contadorColores = 0;

    static {
        contadorObjetos = 0;
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
        this.seleccionado = false;
        this.mostrarAnimacion = false;
        this.traslacion = false;
        this.mostrarPuntos = true;
        this.mostrarLineas = true;
        this.mostrarCaras = false;
        this.animacionEjeX = true;
        this.animacionEjeY = false;
        this.animacionEjeZ = false;
    }

    private void initVariables() {
        this.escala = 100;
        this.traslaciones = new double[3];
        this.rotaciones = new double[3];
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
//        double distanciaFocal = 500; // 250
//
//        double px = (distanciaFocal * -(x - xf)) / (z - zf) + xf;
//        double py = (distanciaFocal * -(y - yf)) / (z - zf) + yf;
        return new Point2D.Double(px, py);
    }

// <editor-fold defaultstate="collapsed" desc="Metodos de transformacion">
    public void trasladarX(int distancia) {
        this.traslaciones[0] += distancia;
    }

    public void trasladarY(int distancia) {
        this.traslaciones[1] += distancia;
    }

    public void trasladarZ(int distancia) {
        this.traslaciones[2] += distancia;
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
// </editor-fold>
    
// <editor-fold defaultstate="collapsed" desc="Metodos para el estado de visualizacion y animacion">
    public void setMostrarAnimacion() {
        this.mostrarAnimacion = !mostrarAnimacion;
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
    
// <editor-fold defaultstate="collapsed" desc="Metodos para el estado de seleccion">
    public void setSeleccionado(boolean seleccionado) {
        this.seleccionado = seleccionado;
    }

    public boolean isSeleccionado() {
        return this.seleccionado;
    }
// </editor-fold>
    
    // Metodo para regresar el buffer
    public synchronized BufferedImage getBuffer() {
        return g2d.getBuffer();
    }
}
