package Graficos;

import java.awt.Color;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.util.Arrays;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.Queue;

public class MyGraphics {

    private BufferedImage buffer;
    private Color color;
    private int WIDTH;
    private int HEIGHT;

    public MyGraphics(int width, int height) {
        this.buffer = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        this.color = new Color(0, 0, 0);
        this.WIDTH = width;
        this.HEIGHT = height;
    }

    public void drawLine(int x0, int y0, int x1, int y1) {
        int dx = x1 - x0;
        int dy = y1 - y0;

        int pasos = Math.max(Math.abs(dx), Math.abs(dy));

        float xIncremento = (float) dx / pasos;
        float yIncremento = (float) dy / pasos;

        float x = x0;
        float y = y0;

        for (int i = 0; i <= pasos; i++) {
            putPixel(Math.round(x), Math.round(y));
            x += xIncremento;
            y += yIncremento;
        }
    }

    public void drawRect(int x0, int y0, int x1, int y1) {
        drawLine(x0, y0, x1, y0);
        drawLine(x0, y0, x0, y1);
        drawLine(x1, y0, x1, y1);
        drawLine(x0, y1, x1, y1);
    }

    public void fillRect(int x0, int y0, int x1, int y1) {
        Polygon poly = new Polygon();
        poly.addPoint(x0, y0);
        poly.addPoint(x1, y0);
        poly.addPoint(x1, y1);
        poly.addPoint(x0, y1);

        Point2D.Double puntoCentral = MyGraphicsUtils.getPuntoCentral(poly);
        drawLine(x0, y0, x1, y0);
        drawLine(x0, y0, x0, y1);
        drawLine(x1, y0, x1, y1);
        drawLine(x0, y1, x1, y1);
        funcionRelleno((int) puntoCentral.x, (int) puntoCentral.y);
    }

    public void drawPolygon(Polygon poly) {
        int[] xPuntos = poly.xpoints;
        int[] yPuntos = poly.ypoints;

        for (int i = 0; i < xPuntos.length; i++) {
            int x0 = (int) xPuntos[i];
            int y0 = (int) yPuntos[i];
            int x1 = (int) xPuntos[(i + 1) % xPuntos.length];
            int y1 = (int) yPuntos[(i + 1) % yPuntos.length];
            drawLine(x0, y0, x1, y1);
        }
    }

    public void fillPolygon(Polygon poly) {
        Point2D.Double puntoCentral = MyGraphicsUtils.getPuntoCentral(poly);
        Point2D.Double[] puntos = MyGraphicsUtils.polygonToArrayPoit2D(poly);

        for (int i = 0; i < puntos.length; i++) {
            int x0 = (int) puntos[i].getX();
            int y0 = (int) puntos[i].getY();
            int x1 = (int) puntos[(i + 1) % puntos.length].getX();
            int y1 = (int) puntos[(i + 1) % puntos.length].getY();
            drawLine(x0, y0, x1, y1);
        }
        funcionRelleno((int) puntoCentral.x, (int) puntoCentral.y);

        color = Color.red;
        fillRect((int) (puntoCentral.x), (int) (puntoCentral.y), (int) (puntoCentral.x + 5), (int) (puntoCentral.y + 5));
    }

    public void fillPolygonDisordered(Polygon poly) {
        Point2D.Double puntoCentral = MyGraphicsUtils.getPuntoCentral(poly);
        Point2D[] puntosOrdenados = MyGraphicsUtils.ordenarPuntos(poly);

        for (int i = 0; i < puntosOrdenados.length; i++) {
            int x0 = (int) puntosOrdenados[i].getX();
            int y0 = (int) puntosOrdenados[i].getY();
            int x1 = (int) puntosOrdenados[(i + 1) % puntosOrdenados.length].getX();
            int y1 = (int) puntosOrdenados[(i + 1) % puntosOrdenados.length].getY();
            drawLine(x0, y0, x1, y1);
        }
        funcionRelleno((int) puntoCentral.x, (int) puntoCentral.y);

        color = Color.red;
        fillRect((int) (puntoCentral.x), (int) (puntoCentral.y), (int) (puntoCentral.x + 5), (int) (puntoCentral.y + 5));
    }

    public void funcionRelleno(int x, int y) {
        putPixel(x, y);

        Queue<Point> colaPixeles = new LinkedList<>();
        colaPixeles.add(new Point(x, y));

        while (!colaPixeles.isEmpty()) {
            Point pixelActual = colaPixeles.poll();
            int tempX = pixelActual.x;
            int tempY = pixelActual.y;

            aniadirPixel(tempX, tempY + 1, colaPixeles); // Arriba
            aniadirPixel(tempX + 1, tempY, colaPixeles); // Derecha
            aniadirPixel(tempX, tempY - 1, colaPixeles); // Abajo
            aniadirPixel(tempX - 1, tempY, colaPixeles); // Izquierda
        }
    }

    private void aniadirPixel(int x, int y, Queue<Point> queue) {
        if (x >= 0 && x < buffer.getWidth() && y >= 0 && y < buffer.getHeight() && buffer.getRGB(x, y) != color.getRGB()) {
            putPixel(x, y);
            queue.add(new Point(x, y));
        }
    }

    private void putPixel(int x, int y) {
        if (x >= 0 && x < buffer.getWidth() && y >= 0 && y < buffer.getHeight()) {
            buffer.setRGB(x, y, color.getRGB());
        }
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public void resetBuffer() {
        buffer = new BufferedImage(HEIGHT, WIDTH, BufferedImage.TYPE_INT_ARGB);
    }

    public BufferedImage getBuffer() {
        return this.buffer;
    }

    public void getColorAt(int x, int y) {
        System.out.println(buffer.getRGB(x, y));
    }
}

class MyGraphicsUtils {

    public static Point2D.Double getPuntoCentral(Polygon poly) {
        Point2D.Double puntoCentral = new Point2D.Double();
        double sumX = 0;
        double sumY = 0;
        int[] polyX = poly.xpoints;
        int[] polyY = poly.ypoints;
        int n = poly.npoints;

        for (int i = 0; i < n; i++) {
            sumX += polyX[i];
            sumY += polyY[i];
        }

        puntoCentral.x = sumX / n;
        puntoCentral.y = sumY / n;

        return puntoCentral;
    }

    public static Point2D.Double[] polygonToArrayPoit2D(Polygon poly) {
        int nPoints = poly.npoints;
        Point2D.Double[] points = new Point2D.Double[nPoints];

        for (int i = 0; i < nPoints; i++) {
            points[i] = new Point2D.Double(poly.xpoints[i], poly.ypoints[i]);
        }

        return points;
    }

    // algoritmo de Graham Scan
    public static Point2D.Double[] ordenarPuntos(Polygon poly) {
        Point2D.Double centroide = getPuntoCentral(poly);
        Point2D.Double[] puntos = new Point2D.Double[poly.npoints];
        for (int i = 0; i < puntos.length; i++) {
            puntos[i] = new Point2D.Double(poly.xpoints[i], poly.ypoints[i]);
        }

        Arrays.sort(puntos, new Comparator<Point2D.Double>() {
            @Override
            public int compare(Point2D.Double a, Point2D.Double b) {
                double angleA = Math.atan2(a.getY() - centroide.getY(), a.getX() - centroide.getX());
                double angleB = Math.atan2(b.getY() - centroide.getY(), b.getX() - centroide.getX());
                return Double.compare(angleA, angleB);
            }
        });

        return puntos;
    }
}
