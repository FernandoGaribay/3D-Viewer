package Graficos;

import java.awt.Color;
import java.awt.Polygon;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;

public class MyGraphics {

    private HashMap<Point2D.Double, Double> zBuffer;
    private BufferedImage buffer;
    private Color color;
    private int WIDTH;
    private int HEIGHT;

    public MyGraphics(int width, int height) {
        this.buffer = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        this.zBuffer = new HashMap<>();
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

    public void drawLine3D(Point2D.Double p1, double z1, Point2D.Double p2, double z2, Point2D.Double puntoFuga) {
        Point2D.Double p1Proj = p1;
        Point2D.Double p2Proj = p2;

        int x0 = (int) p1Proj.getX();
        int y0 = (int) p1Proj.getY();
        double z0 = z1;
        int x1 = (int) p2Proj.getX();
        int y1 = (int) p2Proj.getY();
        double z1Proj = z2;

        int dx = Math.abs(x1 - x0);
        int dy = Math.abs(y1 - y0);
        int sx = x0 < x1 ? 1 : -1;
        int sy = y0 < y1 ? 1 : -1;
        int err = dx - dy;

        double dz = dx > dy ? Math.abs(z1Proj - z0) / dx : Math.abs(z1Proj - z0) / dy;
        if (z1Proj < z0) {
            dz = -dz;
        }

        while (true) {
            if (x0 >= 0 && x0 < WIDTH && y0 >= 0 && y0 < HEIGHT) {
                putPixel(x0, y0, z0);
            }

            if (x0 == x1 && y0 == y1) {
                break;
            }
            int e2 = 2 * err;
            if (e2 > -dy) {
                err -= dy;
                x0 += sx;
                z0 += dz;  // Update z along x
            }
            if (e2 < dx) {
                err += dx;
                y0 += sy;
                z0 += dz;  // Update z along y
            }
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
        fillPolygon(poly);
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
        int[] xPoints = poly.xpoints;
        int[] yPoints = poly.ypoints;
        int nPoints = poly.npoints;

        int minY = Arrays.stream(yPoints).min().orElse(0);
        int maxY = Arrays.stream(yPoints).max().orElse(buffer.getHeight() - 1);

        for (int y = minY; y <= maxY; y++) {
            int[] nodeX = new int[nPoints];
            int nodes = 0;
            int j = nPoints - 1;
            for (int i = 0; i < nPoints; i++) {
                if (yPoints[i] < y && yPoints[j] >= y || yPoints[j] < y && yPoints[i] >= y) {
                    nodeX[nodes++] = (xPoints[i] + (y - yPoints[i]) * (xPoints[j] - xPoints[i]) / (yPoints[j] - yPoints[i]));
                }
                j = i;
            }
            Arrays.sort(nodeX, 0, nodes);
            for (int i = 0; i < 1; i += 2) {
                if (nodeX[i] >= buffer.getWidth()) {
                    break;
                }
                if (nodeX[i + 1] > 0) {
                    if (nodeX[i] < 0) {
                        nodeX[i] = 0;
                    }
                    if (nodeX[i + 1] >= buffer.getWidth()) {
                        nodeX[i + 1] = buffer.getWidth() - 1;
                    }
                    for (int x = nodeX[i]; x <= nodeX[i + 1]; x++) {
                        putPixel(x, y);
                    }
                }
            }
        }
    }

    public void fillPolygon(Polygon poly, double midZIndex) {
        int[] xPoints = poly.xpoints;
        int[] yPoints = poly.ypoints;
        int nPoints = poly.npoints;

        int minY = Arrays.stream(yPoints).min().orElse(0);
        int maxY = Arrays.stream(yPoints).max().orElse(buffer.getHeight() - 1);

        for (int y = minY; y <= maxY; y++) {
            int[] nodeX = new int[nPoints];
            int nodes = 0;
            int j = nPoints - 1;
            for (int i = 0; i < nPoints; i++) {
                if (yPoints[i] < y && yPoints[j] >= y || yPoints[j] < y && yPoints[i] >= y) {
                    nodeX[nodes++] = (xPoints[i] + (y - yPoints[i]) * (xPoints[j] - xPoints[i]) / (yPoints[j] - yPoints[i]));
                }
                j = i;
            }
            Arrays.sort(nodeX, 0, nodes);
            for (int i = 0; i < nodes; i += 2) {
                if (nodeX[i] >= buffer.getWidth()) {
                    break;
                }
                if (nodeX[i + 1] > 0) {
                    if (nodeX[i] < 0) {
                        nodeX[i] = 0;
                    }
                    if (nodeX[i + 1] >= buffer.getWidth()) {
                        nodeX[i + 1] = buffer.getWidth() - 1;
                    }
                    for (int x = nodeX[i]; x <= nodeX[i + 1]; x++) {
                        Point2D.Double zBufferPoint = new Point2D.Double(x, y);
                        putPixel(x, y, midZIndex);
                    }
                }
            }
        }
    }

    public void fillPolygonDisordered(Polygon poly) {
        Point2D.Double[] puntosOrdenados = MyGraphicsUtils.ordenarPuntos(poly);
        fillPolygon(MyGraphicsUtils.arrayPoint2DToPolygon(puntosOrdenados));

        color = Color.red;
        Point2D.Double puntoCentral = MyGraphicsUtils.getPuntoCentral(poly);
        fillRect((int) (puntoCentral.x), (int) (puntoCentral.y), (int) (puntoCentral.x + 5), (int) (puntoCentral.y + 5));
    }

    private void putPixel(int x, int y) {
        if (x >= 0 && x < buffer.getWidth() && y >= 0 && y < buffer.getHeight()) {
            buffer.setRGB(x, y, color.getRGB());
        }
    }

    private void putPixel(int x, int y, double z) {
        if (x >= 0 && x < buffer.getWidth() && y >= 0 && y < buffer.getHeight()) {
            Point2D.Double point = new Point2D.Double(x, y);
            if (!zBuffer.containsKey(point) || zBuffer.get(point) > z) {
                zBuffer.put(point, z);
                buffer.setRGB(x, y, color.getRGB());
            }
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
        zBuffer = new HashMap<>();
    }

    public BufferedImage getBuffer() {
        return this.buffer;
    }

    public HashMap<Point2D.Double, Double> getzBuffer() {
        return zBuffer;
    }

    public void setzBuffer(HashMap<Point2D.Double, Double> zBuffer) {
        this.zBuffer = zBuffer;
    }

    public int getColorAt(int x, int y) {
        return buffer.getRGB(x, y);
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

    public static Point2D.Double[] polygonToArrayPoint2D(Polygon poly) {
        int nPoints = poly.npoints;

        Point2D.Double[] points = new Point2D.Double[nPoints];
        for (int i = 0; i < nPoints; i++) {
            points[i] = new Point2D.Double(poly.xpoints[i], poly.ypoints[i]);
        }

        return points;
    }

    public static Polygon arrayPoint2DToPolygon(Point2D.Double[] points) {
        int nPoints = points.length;

        Polygon tempPoly = new Polygon();
        for (int i = 0; i < nPoints; i++) {
            tempPoly.addPoint((int) points[i].getX(), (int) points[i].getY());
        }

        return tempPoly;
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
