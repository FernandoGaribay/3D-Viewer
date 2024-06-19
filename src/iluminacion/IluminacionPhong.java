package iluminacion;

import java.awt.Color;

public class IluminacionPhong {

    private float[] colorModeloNormalizado;
    private float[] colorAmbienteNormalizado;
    private float[] colorLuzNormalizado;

    private float brilloEspecular;
    private float[] origenLuz;
    private float[] vectorLuz;
    private float[] vertice;

    private float[] vectorNormal = new float[]{ // vector default normalizado
        0,
        0,
        1
    };

    public IluminacionPhong(Color colorAmbiente, float[] origenLuz, Color colorLuz) {
        this.colorAmbienteNormalizado = normalizeColor(colorAmbiente);
        this.colorLuzNormalizado = normalizeColor(colorLuz);

        this.origenLuz = origenLuz;
    }

    public float[] getColorIluminacion(Color colorModelo, float brilloEspectacular, float[]... vertice) {
        this.brilloEspecular = brilloEspectacular;
        float[] colorFinal = new float[3];

        for (float[] v : vertice) {
            this.colorModeloNormalizado = normalizeColor(colorModelo);
            this.vectorLuz = getLightVector(v);
            this.vertice = v;

            float[] vectorAmbient = getVectorAmbient();
            float[] vectorDiffuce = getVectorDiffuce();
            float[] vectorEspecular = getVectorEspecular();

            colorFinal[0] += Math.min(1.0f, (vectorAmbient[0] + vectorDiffuce[0] + vectorEspecular[0]));
            colorFinal[1] += Math.min(1.0f, (vectorAmbient[1] + vectorDiffuce[1] + vectorEspecular[1]));
            colorFinal[2] += Math.min(1.0f, (vectorAmbient[2] + vectorDiffuce[2] + vectorEspecular[2]));
        }
        int size = vertice.length;
        colorFinal[0] /= size;
        colorFinal[1] /= size;
        colorFinal[2] /= size;

        return colorFinal;
    }

    public float[] getColorIluminacion(Color modelColor, float brilloEspectacular, float[][] vertice, float[][] normal) {
        float[] finalColor = new float[3];

        for (int i = 0; i < vertice.length; i++) {
            this.colorModeloNormalizado = normalizeColor(modelColor);
            this.vectorLuz = getLightVector(vertice[i]);
            this.vertice = vertice[i];
            this.vectorNormal = normal[i];

            float[] vectorAmbient = getVectorAmbient();
            float[] vectorDiffuce = getVectorDiffuce();
            float[] vectorEspecular = getVectorEspecular();

            finalColor[0] += Math.min(1.0f, (vectorAmbient[0] + vectorDiffuce[0] + vectorEspecular[0]));
            finalColor[1] += Math.min(1.0f, (vectorAmbient[1] + vectorDiffuce[1] + vectorEspecular[1]));
            finalColor[2] += Math.min(1.0f, (vectorAmbient[2] + vectorDiffuce[2] + vectorEspecular[2]));
        }
        int size = vertice.length;
        finalColor[0] /= size;
        finalColor[1] /= size;
        finalColor[2] /= size;

        return finalColor;
    }

    private float[] getVectorAmbient() {
        float[] ambientVector = new float[]{
            colorModeloNormalizado[0] * colorAmbienteNormalizado[0],
            colorModeloNormalizado[1] * colorAmbienteNormalizado[1],
            colorModeloNormalizado[2] * colorAmbienteNormalizado[2]
        };

        return ambientVector;
    }

    public float[] getVectorDiffuce() {
        float[] vectorDiffuce = new float[3];

        float dotProduct = Math.max(0, dot(vectorNormal, vectorLuz));
        vectorDiffuce[0] = colorModeloNormalizado[0] * colorLuzNormalizado[0] * dotProduct;
        vectorDiffuce[1] = colorModeloNormalizado[1] * colorLuzNormalizado[1] * dotProduct;
        vectorDiffuce[2] = colorModeloNormalizado[2] * colorLuzNormalizado[2] * dotProduct;

        return vectorDiffuce;
    }

    public float[] getVectorEspecular() {
        float[] reflectVector = reflect(vectorLuz, vectorNormal);
        float[] viewVector = normalize(new float[]{(float) -vertice[0], (float) -vertice[1], (float) -vertice[2]});
        float viewAngle = dot(viewVector, reflectVector);

        float specularStrength = (float) Math.pow(Math.max(0, viewAngle), brilloEspecular);

        float[] vectorEspecular = new float[]{
            Math.min(1.0f, colorLuzNormalizado[0] * specularStrength),
            Math.min(1.0f, colorLuzNormalizado[1] * specularStrength),
            Math.min(1.0f, colorLuzNormalizado[2] * specularStrength)
        };
        return vectorEspecular;
    }

    private static float dot(float[] vec1, float[] vec2) {
        return vec1[0] * vec2[0] + vec1[1] * vec2[1] + vec1[2] * vec2[2];
    }

    private static float[] normalize(float[] vector) {
        float length = (float) Math.abs(Math.sqrt(vector[0] * vector[0] + vector[1] * vector[1] + vector[2] * vector[2]));
        if ((length - 1) < 0.001) {
            return vector;
        }
        return new float[]{vector[0] / length, vector[1] / length, vector[2] / length};
    }

    private float[] getLightVector(float[] vertice) {
        float[] lightVector = normalize(new float[]{
            origenLuz[0] - vertice[0],
            origenLuz[1] - vertice[1],
            origenLuz[2] - vertice[2]
        });

        return lightVector;
    }

    private static float[] reflect(float[] incident, float[] normal) {
        float dotProduct = 2.0f * dot(incident, normal);
        float[] reflect = new float[]{
            incident[0] - dotProduct * normal[0],
            incident[1] - dotProduct * normal[1],
            incident[2] - dotProduct * normal[2]
        };
        return normalize(reflect);
    }

    private float[] normalizeColor(Color color) {
        float[] normalizedColor = new float[3];
        normalizedColor[0] = color.getRed() / 255.0f;
        normalizedColor[1] = color.getGreen() / 255.0f;
        normalizedColor[2] = color.getBlue() / 255.0f;
        return normalizedColor;
    }

}
