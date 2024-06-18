package modelos3D;

import java.util.ArrayList;

public class Modelo3D {

    private ArrayList<double[]> vertices = new ArrayList<>();
    private ArrayList<int[]> caras = new ArrayList<>();
    private ArrayList<double[]> normales = new ArrayList<>();
    private ArrayList<int[]> normalesPorCara = new ArrayList<>();

    public Modelo3D() {

    }

    public void addVertice(double[] vertice) {
        this.vertices.add(vertice);
    }

    public void addCara(int[] cara, int[] normalesCara) {
        caras.add(cara);
        normalesPorCara.add(normalesCara);
    }
    
    public void addNormal(double[] normal) {
        this.normales.add(normal);
    }
    
    public ArrayList<double[]> getVertices() {
        return vertices;
    }

    public void setVertices(ArrayList<double[]> vertices) {
        this.vertices = vertices;
    }

    public ArrayList<int[]> getCaras() {
        return caras;
    }

    public void setCaras(ArrayList<int[]> caras) {
        this.caras = caras;
    }

    public ArrayList<double[]> getNormales() {
        return normales;
    }

    public void setNormales(ArrayList<double[]> normales) {
        this.normales = normales;
    }

    public ArrayList<int[]> getNormalesPorCara() {
        return normalesPorCara;
    }

    public void setNormalesPorCara(ArrayList<int[]> normalesPorCara) {
        this.normalesPorCara = normalesPorCara;
    }
    
}
