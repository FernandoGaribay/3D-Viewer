package modelos3D;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class LectorOBJ {

    public static Modelo3D readObjFile(InputStream inputStream) {
        if (inputStream == null) {
            throw new IllegalArgumentException("Archivo no encontrado");
        }

        try (BufferedReader br = new BufferedReader(new InputStreamReader(inputStream))) {
            Modelo3D modelo = new Modelo3D();
            String line;

            while ((line = br.readLine()) != null) {
                if (line.startsWith("v ")) {
                    String[] parts = line.split("\\s+");

                    double x = Double.parseDouble(parts[1]);
                    double y = Double.parseDouble(parts[2]);
                    double z = Double.parseDouble(parts[3]);
                    double[] vertice = {x, y, z};
                    
                    modelo.addVertice(vertice);
                } else if (line.startsWith("vn ")) {
                    String[] parts = line.split("\\s+");

                    double x = Double.parseDouble(parts[1]);
                    double y = Double.parseDouble(parts[2]);
                    double z = Double.parseDouble(parts[3]);
                    double[] normal = {x, y, z};

                    modelo.addNormal(normal);
                } else if (line.startsWith("f ")) {
                    String[] parts = line.split("\\s+");
                    ArrayList<Integer> vertexIndices = new ArrayList<>();
                    ArrayList<Integer> normalIndices = new ArrayList<>();

                    for (int i = 1; i < parts.length; i++) {
                        String part = parts[i];
                        String[] subParts;

                        if (part.contains("//")) {
                            subParts = part.split("//");
                            vertexIndices.add(Integer.parseInt(subParts[0]) - 1);
                            normalIndices.add(Integer.parseInt(subParts[1]) - 1);
                        } else if (part.contains("/")) {
                            subParts = part.split("/");
                            vertexIndices.add(Integer.parseInt(subParts[0]) - 1);
                            if (subParts.length == 3) {
                                normalIndices.add(Integer.parseInt(subParts[2]) - 1);
                            } else if (subParts.length == 2 && !subParts[1].isEmpty()) {
//                                normalIndices.add(Integer.parseInt(subParts[1]) - 1); <------ = :'(
                                normalIndices.add(0);
                            }
                        } else {
                            vertexIndices.add(Integer.parseInt(part) - 1);
                        }
                    }
                    int[] faceVertices = vertexIndices.stream().mapToInt(Integer::intValue).toArray();
                    int[] faceNormals = normalIndices.stream().mapToInt(Integer::intValue).toArray();
                    modelo.addCara(faceVertices, faceNormals);
                }
            }
            return modelo;
        } catch (IOException e) {
            System.out.println("Archivo no encontrado");
            e.printStackTrace();
        }
        return null;
    }
}
