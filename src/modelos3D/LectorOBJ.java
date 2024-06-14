package modelos3D;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class LectorOBJ {

    public static Modelo3D readObjFile(String filename) {
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            // Declara un BufferedReader que lee del archivo cuyo nombre se pasa como argumento.
            // El try-with-resources asegura que el BufferedReader se cierre automáticamente.

            Modelo3D modelo = new Modelo3D();
            String line;
            // Declara una variable para almacenar cada línea que se lea del archivo.

            while ((line = br.readLine()) != null) {
                // Bucle que lee cada línea del archivo hasta que no haya más líneas (es decir, readLine devuelve null).

                if (line.startsWith("v ")) {
                    // Si la línea comienza con "v ", indica que define un vértice.

                    String[] parts = line.split("\\s+");
                    // Divide la línea en partes usando espacios en blanco como separador. 
                    // "\\s+" es una expresión regular que significa uno o más espacios en blanco.

                    double x = Double.parseDouble(parts[1]);
                    double y = Double.parseDouble(parts[2]);
                    double z = Double.parseDouble(parts[3]);
                    // Convierte las partes que representan las coordenadas x, y y z a valores de tipo double.
                    double[] vertice = {x, y, z};
                    modelo.addVertice(vertice);
                    // Crea un nuevo objeto Point3D con las coordenadas leídas y lo añade a la lista de vértices.
                } else if (line.startsWith("f ")) {
                    // Si la línea comienza con "f ", indica que define una cara (face).

                    String[] parts = line.split("\\s+");
                    // Divide la línea en partes usando espacios en blanco como separador.

                    int[] face = new int[parts.length - 1];
                    // Crea un array de enteros para almacenar los índices de los vértices que forman la cara.
                    // El tamaño del array es igual al número de partes menos uno porque la primera parte es "f".

                    for (int i = 1; i < parts.length; i++) {
                        // Itera a través de las partes que contienen los índices de los vértices (comienza en 1 para saltar "f").

                        String part = parts[i];
                        // Almacena la parte actual en una variable.

                        if (part.contains("//")) {
                            face[i - 1] = Integer.parseInt(part.split("//")[0]) - 1;
                            // Si la parte contiene "//", divide por "//" y toma la primera parte, 
                            // la convierte a entero y le resta 1 (para convertir de índice 1 a 0).

                        } else if (part.contains("/")) {
                            face[i - 1] = Integer.parseInt(part.split("/")[0]) - 1;
                            // Si la parte contiene "/", divide por "/" y toma la primera parte,
                            // la convierte a entero y le resta 1 (para convertir de índice 1 a 0).

                        } else {
                            face[i - 1] = Integer.parseInt(part) - 1;
                            // Si la parte no contiene "/", simplemente la convierte a entero y le resta 1 (para convertir de índice 1 a 0).
                        }
                    }
                    modelo.addCara(face);
                    // Añade el array de índices de los vértices a la lista de caras.
                }
            }
            return modelo;
        } catch (IOException e) {
            System.out.println("Archivo no encontrado");
            e.printStackTrace();
            // Captura cualquier excepción de entrada/salida que pueda ocurrir y muestra la traza de la pila.
        }
        return null;
    }
}
