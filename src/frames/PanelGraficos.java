package frames;

import graficos.Cubo3D;
import java.awt.Color;
import java.awt.Graphics;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import Interfaces.LabelManager;
import animaciones.AnimacionCilindro;
import graficos.Carro3D;
import graficos.Objeto3D;
import graficos.Cilindro3D;
import graficos.Dona3D;
import graficos.Isla3D;
import graficos.Avion3D;
import graficos.Arania3D;
import graficos.Music3D;
import graficos.Corazon3D;
import graficos.Esfera3D;
import graficos.Trofeo3D;
import graficos.Superficie3D;
import java.util.ArrayList;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;
import utils.Constantes;

public class PanelGraficos extends JPanel implements Runnable, LabelManager {

    private Thread hiloPanelGraficos;
    private static final ArrayList<JLabel> listaTagLabels;

    private static final JLabel labelInfoControles;
    private static final JLabel labelInfoObjeto;
    private static final JLabel labelInfoControlesPersistente;
    private static final JLabel labelInfoObjetoPersistente;

    private double[] puntoFuga = {450, 300, 250};

    private Objeto3D objetoActual;
    private ArrayList<Objeto3D> listaCubos = new ArrayList<>();
    private int currentIndex = 0;

    static {
        listaTagLabels = new ArrayList<>();

        labelInfoControles = new JLabel("<html>--------------------- CONTROLES ---------------------<br><br>"
                + "ESPACIO -> Parar/Reanudar la animacion<br>"
                + "TAB -> Alternar Traslacion/Rotacion/Iluminacion<br>"
                + "SCROLL -> Aumentar/Disminuir la escala<br>"
                + "IZQUIERDA -> Anterior objeto<br>"
                + "DERECHA -> Siguiente objeto<br>"
                + "ARRIBA-> Aumentar escala<br>"
                + "ABAJO-> Disminuir escala<br>"
                + "CLICK IZQ -> Traslacion (X e Y)<br><br>"
                + "W -> Transformar para arriba<br>"
                + "A -> Transformar para la izquierda<br>"
                + "S -> Transformar para abajo<br>"
                + "D -> Transformar para la derecha<br>"
                + "Q -> Transformar para Z negativo<br>"
                + "E -> Transformar para Z positivo<br><br>"
                + "Z -> Activar/Desactivar Vertices<br>"
                + "X -> Activar/Desactivar Lineas<br>"
                + "C -> Activar/Desactivar Caras<br>"
                + "N -> Activar/Desactivar Origen Luz<br>"
                + "M -> Activar/Desactivar Luz<br><br>"
                + "P -> Generar paleta de colores<br>"
                + "O -> Cambiar numero de puntos<br><br>"
                + "1 -> Activar/Desactivar Eje X<br>"
                + "2 -> Activar/Desactivar Eje Y<br>"
                + "3 -> Activar/Desactivar Eje Z<br>"
                + "</html>");

        labelInfoObjeto = new JLabel("<html><div style='text-align: right;'>------------------- INFORMACION -------------------<br><br>"
                + "ID OBJETO: #1<br>"
                + "FPS: 60<br><br>"
                + "Puntos: Visibles<br>"
                + "Lineas: Visibles<br>"
                + "Caras: Invisibles<br><br>"
                + "Caracteristicas del objeto:<br>"
                + "Escala -> 200 pixeles<br>"
                + "X -> 450 pixeles<br>"
                + "Y -> 300 pixeles<br>"
                + "Z -> 700 pixeles<br><br>"
                + "Punto de fuga:<br>"
                + "X -> 450 pixeles<br>"
                + "Y -> 300 pixeles<br>"
                + "Z -> 250 pixeles<br>"
                + "FOV -> 250 pixeles<br><br>"
                + "Ejes activos:<br>"
                + "X (0°) -> Activado<br>"
                + "Y (0°) -> Activado<br>"
                + "Z (0°) -> Desactivado<br><br>"
                + "</div></html>");

        labelInfoControlesPersistente = new JLabel("<html>CTRL -> Ocultar/Mostrar controles<br></html>");
        labelInfoObjetoPersistente = new JLabel("<html>ALT -> Ocultar/Mostrar informacion<br></html>");
    }

    public PanelGraficos() {
        SwingUtilities.invokeLater(() -> {
            initComponentes();
            this.setBackground(new Color(38, 38, 38));
            this.setLayout(null);
            this.setFocusable(true);
            this.requestFocus();
            this.requestFocusInWindow();

            double[] origenCubo = {450, 300, 700};

            Cilindro3D cubo2 = new Cilindro3D(getWidth(), getHeight(), origenCubo, puntoFuga, this);
            listaCubos.add(cubo2);

            Cubo3D cubo = new Cubo3D(getWidth(), getHeight(), origenCubo, puntoFuga, this);
            listaCubos.add(cubo);

            AnimacionCilindro animacionCilindro = new AnimacionCilindro(getWidth(), getHeight(), origenCubo, puntoFuga, this);
            listaCubos.add(animacionCilindro);

            Esfera3D esfera = new Esfera3D(getWidth(), getHeight(), origenCubo, puntoFuga, this);
            listaCubos.add(esfera);

            Carro3D carro = new Carro3D(getWidth(), getHeight(), origenCubo, puntoFuga, this);
            listaCubos.add(carro);

            Isla3D isla = new Isla3D(getWidth(), getHeight(), origenCubo, puntoFuga, this);
            listaCubos.add(isla);

            Trofeo3D render = new Trofeo3D(getWidth(), getHeight(), origenCubo, puntoFuga, this);
            listaCubos.add(render);

            Corazon3D corazon = new Corazon3D(getWidth(), getHeight(), origenCubo, puntoFuga, this);
            listaCubos.add(corazon);

            Music3D music = new Music3D(getWidth(), getHeight(), origenCubo, puntoFuga, this);
            listaCubos.add(music);

            Arania3D arania = new Arania3D(getWidth(), getHeight(), origenCubo, puntoFuga, this);
            listaCubos.add(arania);

            Avion3D avion = new Avion3D(getWidth(), getHeight(), origenCubo, puntoFuga, this);
            listaCubos.add(avion);

            Dona3D cubo3 = new Dona3D(getWidth(), getHeight(), origenCubo, puntoFuga, this);
            listaCubos.add(cubo3);

            Superficie3D cubo4 = new Superficie3D(getWidth(), getHeight(), origenCubo, puntoFuga, this);
            listaCubos.add(cubo4);

            if (!listaCubos.isEmpty()) {
                objetoActual = listaCubos.get(currentIndex);
                objetoActual.setSeleccionado(true);
            }

            this.hiloPanelGraficos = new Thread(this);
            this.hiloPanelGraficos.start();
        });
    }

    private void initComponentes() {
        labelInfoControles.setForeground(Color.WHITE);
        labelInfoControles.setVerticalAlignment(SwingConstants.TOP);
        labelInfoControles.setBounds(-250, 20, 250, 575);
        add(labelInfoControles);

        labelInfoControlesPersistente.setForeground(Color.WHITE);
        labelInfoControlesPersistente.setBounds(20, 570, 250, 10);
        add(labelInfoControlesPersistente);

        labelInfoObjeto.setForeground(Color.WHITE);
        labelInfoObjeto.setVerticalAlignment(SwingConstants.TOP);
        labelInfoObjeto.setHorizontalAlignment(SwingConstants.TRAILING);
        labelInfoObjeto.setBounds(905, 20, 250, 575);
        add(labelInfoObjeto);

        labelInfoObjetoPersistente.setForeground(Color.WHITE);
        labelInfoObjetoPersistente.setHorizontalAlignment(SwingConstants.TRAILING);
        labelInfoObjetoPersistente.setBounds(625, 570, 250, 10);
        add(labelInfoObjetoPersistente);
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);

        listaTagLabels.get(objetoActual.getIdObjeto()).setForeground(new Color(255, 255, 255));
        for (Objeto3D cubo : listaCubos) {
            g.drawImage(cubo.getBuffer(), 0, 0, null);
        }
    }

    public void siguienteElemento() {
        if (Objeto3D.animacionSeleccionActiva || Objeto3D.animacionDeseleccionActiva) {
            return;
        }
        if (currentIndex < listaCubos.size() - 1) {
            objetoActual.iniciarAnimacionDeseleccionado();

            currentIndex++;
            objetoActual = listaCubos.get(currentIndex);
            objetoActual.iniciarAnimacionSeleccionado();
            Objeto3D.animacionSeleccionActiva = true;
        } else {
            currentIndex = -1;
            siguienteElemento();
        }
    }

    public void anteriorElemento() {
        if (Objeto3D.animacionSeleccionActiva || Objeto3D.animacionDeseleccionActiva) {
            return;
        }
        if (currentIndex > 0) {
            objetoActual.iniciarAnimacionDeseleccionado();
            currentIndex--;
            objetoActual = listaCubos.get(currentIndex);
            objetoActual.iniciarAnimacionSeleccionado();
        } else {
            currentIndex = listaCubos.size();
            anteriorElemento();
        }
    }

    public void setMostrarAnimacion() {
        objetoActual.setMostrarAnimacion();
    }

    public void generarColores() {
        String input = JOptionPane.showInputDialog("Ingrese el número de colores a generar:");

        if (input != null) {
            try {
                int numColores = Integer.parseInt(input);
                objetoActual.initColores(numColores);
            } catch (NumberFormatException e) {
            }
        }
    }

    public void generarPuntos() {
        String input = JOptionPane.showInputDialog("Ingrese el número de puntos a generar:");

        if (input != null) {
            try {
                int numPuntos = Integer.parseInt(input);
                objetoActual.setNumPuntos(numPuntos);
            } catch (NumberFormatException e) {
            }
        }
    }

    public void setEscala(boolean positivo) {
        double aumentoEscala = (positivo) ? objetoActual.getAumentoEscala() : -objetoActual.getAumentoEscala();
        objetoActual.setEscala(aumentoEscala);
    }

    public void setAlternacionRTI() {
        objetoActual.setAlternacionRTI();
    }

    public void setRotacionTransformacionMouse(int x, int y) {
        objetoActual.setRotacionTransformacionMouse(x, y);
    }

    public void setRotacionTransformacionArriba() {
        objetoActual.setRotacionTransformacionArriba();
    }

    public void setRotacionTransformacionAbajo() {
        objetoActual.setRotacionTransformacionAbajo();
    }

    public void setRotacionTransformacionIzquierda() {
        objetoActual.setRotacionTransformacionIzquierda();
    }

    public void setRotacionTransformacionDerecha() {
        objetoActual.setRotacionTransformacionDerecha();
    }

    public void setRotacionTransformacionZPositiva() {
        objetoActual.setRotacionTransformacionZPositiva();
    }

    public void setRotacionTransformacionZNegativa() {
        objetoActual.setRotacionTransformacionZNegativa();
    }
    
    public void setMostrarOrigenLuz(){
        objetoActual.setMostrarOrigenLuz();
    }
    
    public void setMostrarLuz(){
        objetoActual.setMostrarLuz();
    }

    public void setMostrarPuntos() {
        objetoActual.setMostrarPuntos();
    }

    public void setMostrarLineas() {
        objetoActual.setMostrarLineas();
    }

    public void setMostrarCaras() {
        objetoActual.setMostrarCaras();
    }

    public void setEjeXAnimacion() {
        objetoActual.setEjeXAnimacion();
    }

    public void setEjeYAnimacion() {
        objetoActual.setEjeYAnimacion();
    }

    public void setEjeZAnimacion() {
        objetoActual.setEjeZAnimacion();
    }

    public void trasladarCubos(int d) {
        for (Objeto3D cubo : listaCubos) {
            cubo.trasladarX(d);
        }
    }

    public void ocultarControles() {
        Thread thread = new Thread(() -> {
            int tempX = labelInfoControles.getX();

            while (tempX > -labelInfoControles.getWidth()) {
                tempX -= 10;
                trasladarCubos(10);
                labelInfoControles.setLocation(tempX, labelInfoControles.getY());
                try {
                    Thread.sleep(16);
                } catch (InterruptedException ex) {
                    Logger.getLogger(FrameAnimacion.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        thread.start();
    }

    public void ocultarInformacion() {
        Thread thread = new Thread(() -> {
            int tempX = labelInfoObjeto.getX();
            int panelWidth = getWidth();

            while (tempX < panelWidth) {
                tempX += 10;
                trasladarCubos(-10);
                labelInfoObjeto.setLocation(tempX, labelInfoObjeto.getY());
                try {
                    Thread.sleep(16);
                } catch (InterruptedException ex) {
                    Logger.getLogger(FrameAnimacion.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        thread.start();
    }

    public void mostrarControles() {
        Thread thread = new Thread(() -> {
            int tempX = labelInfoControles.getX();

            while (tempX < 20) {
                tempX += 10;
                trasladarCubos(-10);
                labelInfoControles.setLocation(tempX, labelInfoControles.getY());
                try {
                    Thread.sleep(16);
                } catch (InterruptedException ex) {
                    Logger.getLogger(FrameAnimacion.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        thread.start();
    }

    public void mostrarInformacion() {
        Thread thread = new Thread(() -> {
            int tempX = labelInfoObjeto.getX();

            while (tempX > 625) {
                tempX -= 10;
                trasladarCubos(10);
                labelInfoObjeto.setLocation(tempX, labelInfoObjeto.getY());
                try {
                    Thread.sleep(16);
                } catch (InterruptedException ex) {
                    Logger.getLogger(FrameAnimacion.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        thread.start();
    }

    @Override
    public void run() {
        while (true) {
            repaint();
            try {
                Thread.sleep(16);
            } catch (InterruptedException ex) {
                Logger.getLogger(PanelGraficos.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    @Override
    public void aniadirEtiqueta(JLabel tagLabel) {
        JLabel tempLabel2 = tagLabel;
        tempLabel2.setHorizontalAlignment(SwingConstants.TRAILING);
        tempLabel2.setBounds(450, 30, Constantes.TAG_LABEL_WIDTH, Constantes.TAG_LABEL_HEIGHT);
        tempLabel2.setForeground(new Color(38, 38, 38));
        listaTagLabels.add(tempLabel2);
        add(tempLabel2);
    }

    @Override
    public void actualizarEtiquetaInformacion(int indice, String texto) {
        labelInfoObjeto.setText(texto);
    }

    @Override
    public void actualizarEtiquetaObjeto(int indice, int x, int y) {
        listaTagLabels.get(indice).setLocation(x, 30);
    }
}
