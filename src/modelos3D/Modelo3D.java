package modelos3D;

import java.util.ArrayList;

public class Modelo3D {

    private ArrayList<double[]> vertices = new ArrayList<>();
    private ArrayList<int[]> caras = new ArrayList<>();

    public Modelo3D(){
        
    }
    
    public void addVertice(double[] vertice) {
        this.vertices.add(vertice);
    }
    
    public void addCara(int[] cara) {
        this.caras.add(cara);
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
}
