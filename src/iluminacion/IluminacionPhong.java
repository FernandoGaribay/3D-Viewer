package iluminacion;

import java.awt.Color;

public class IluminacionPhong {

    private float[] modelColorNormalized;
    private float[] ambientColorNormalized;
    private float[] lightColorNormalized;

    private float[] lightPosition;
    private float[] lightVector;
    private float[] vertice;

    private float[] normalVector = new float[]{ // vector normalizado
        0,
        0,
        1
    };

    public IluminacionPhong(Color ambientColor, float[] lightPosition, Color lightColor) {
        this.ambientColorNormalized = normalizeColor(ambientColor);
        this.lightColorNormalized = normalizeColor(lightColor);

        this.lightPosition = lightPosition;
    }

    public float[] getIluminacionColor(Color modelColor, float[] vertice) {
        this.modelColorNormalized = normalizeColor(modelColor);
        this.lightVector = getLightVector(vertice);
        this.vertice = vertice;

        float[] vectorAmbient = getVectorAmbient();
        float[] vectorDiffuce = getVectorDiffuce();
        float[] vectorEspecular = getVectorEspecular();

        float[] finalColor = new float[]{
            Math.min(1.0f, (vectorAmbient[0] + vectorDiffuce[0] + vectorEspecular[0])),
            Math.min(1.0f, (vectorAmbient[1] + vectorDiffuce[1] + vectorEspecular[1])),
            Math.min(1.0f, (vectorAmbient[2] + vectorDiffuce[2] + vectorEspecular[2]))
        };

        return finalColor;
    }

    public float[] getIluminacionColor(Color modelColor, float[]... vertice) {
        float[] finalColor = new float[3];

        for (float[] v : vertice) {
            this.modelColorNormalized = normalizeColor(modelColor);
            this.lightVector = getLightVector(v);
            this.vertice = v;

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

    public float[] getIluminacionColor(Color modelColor, float[][] vertice, float[][] normal) {
        float[] finalColor = new float[3];

        for (int i = 0; i < vertice.length; i++) {
            this.modelColorNormalized = normalizeColor(modelColor);
            this.lightVector = getLightVector(vertice[i]);
            this.vertice = vertice[i];
            this.normalVector = normal[i];

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
            modelColorNormalized[0] * ambientColorNormalized[0],
            modelColorNormalized[1] * ambientColorNormalized[1],
            modelColorNormalized[2] * ambientColorNormalized[2]
        };

        return ambientVector;
    }

    public float[] getVectorDiffuce() {
        float[] vectorDiffuce = new float[3];

        float dotProduct = Math.max(0, dot(normalVector, lightVector));
        vectorDiffuce[0] = modelColorNormalized[0] * lightColorNormalized[0] * dotProduct;
        vectorDiffuce[1] = modelColorNormalized[1] * lightColorNormalized[1] * dotProduct;
        vectorDiffuce[2] = modelColorNormalized[2] * lightColorNormalized[2] * dotProduct;

        return vectorDiffuce;
    }

    public float[] getVectorEspecular() {
        float[] reflectVector = reflect(lightVector, normalVector);
        float[] viewVector = normalize(new float[]{(float) -vertice[0], (float) -vertice[1], (float) -vertice[2]});
        float viewAngle = dot(viewVector, reflectVector);

        float shininess = 32.0f; // Coeficiente de brillo especular
        float specularStrength = (float) Math.pow(Math.max(0, viewAngle), shininess);

        float[] vectorEspecular = new float[]{
            Math.min(1.0f, lightColorNormalized[0] * specularStrength),
            Math.min(1.0f, lightColorNormalized[1] * specularStrength),
            Math.min(1.0f, lightColorNormalized[2] * specularStrength)
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
            lightPosition[0] - vertice[0],
            lightPosition[1] - vertice[1],
            lightPosition[2] - vertice[2]
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
