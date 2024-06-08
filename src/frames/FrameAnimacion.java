package frames;

import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import Interfaces.LabelManager;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.util.logging.Level;
import java.util.logging.Logger;
import utils.Constantes;

public final class FrameAnimacion extends JFrame implements LabelManager {

    private final PanelGraficos panelGraficos = new PanelGraficos(this);
    private static final ArrayList<JLabel> listaInfoLabels;
    private static final ArrayList<JLabel> listaTagLabels;
    private static JLabel labelInfoControles;
    private static JLabel labelInfoObjeto;
    private static JLabel labelInfoControlesPersistente;
    private static JLabel labelInfoObjetoPersistente;
    private static boolean controlesEnPantalla;
    private static boolean informacionEnPantalla;

    private static int xInicialLabels;
    private static int yInicialLabels;
    private static int xInicial;
    private static int yInicial;

    static {
        listaInfoLabels = new ArrayList<>();
        listaTagLabels = new ArrayList<>();
        controlesEnPantalla = false;
        xInicialLabels = 675;
        yInicialLabels = 25;
        xInicial = 0;
        yInicial = 0;
    }

    public FrameAnimacion() {
        setTitle("Animacion 3D");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 600);
        setResizable(false);
        setVisible(true);
        setLocationRelativeTo(null);
        setLayout(null);
        requestFocus();

        initComponentes();
        initEventos();
    }

    public void initComponentes() {
        panelGraficos.setLayout(null);
        panelGraficos.setBounds(0, 0, Constantes.FRAME_WIDTH, Constantes.FRAME_HEIGHT);
        add(panelGraficos);

        for (int i = 0; i < listaInfoLabels.size(); i++) {
            JLabel tempLabel = listaInfoLabels.get(i);
            tempLabel.setHorizontalAlignment(SwingConstants.TRAILING);
            tempLabel.setForeground(Color.WHITE);
            panelGraficos.add(tempLabel);
        }

        labelInfoControles = new JLabel("<html>--------------------- CONTROLES ---------------------<br><br>"
                + "ESPACIO -> Parar/Reanudar la animacion<br>"
                + "TAB -> Alternar traslacion/Rotacion<br>"
                + "SCROLL -> Aumentar/Disminuir la escala<br><br>"
                + "Click Izq -> Rotacion (Ejes activados)<br>"
                + "Click Der -> Traslacion (X e Y)<br><br>"
                + "W -> Transformar para arriba<br>"
                + "A -> Transformar para la izquierda<br>"
                + "S -> Transformar para abajo<br>"
                + "D -> Transformar para la derecha<br>"
                + "Q -> Transformar para Z negativo<br>"
                + "E -> Transformar para Z positivo<br><br>"
                + "Z -> Activar/Desactivar Puntos<br>"
                + "X -> Activar/Desactivar Lineas<br>"
                + "C -> Activar/Desactivar Caras<br><br>"
                + "1 -> Activar/Desactivar Eje X<br>"
                + "2 -> Activar/Desactivar Eje Y<br>"
                + "3 -> Activar/Desactivar Eje Z<br>"
                + "</html>");
        labelInfoControles.setForeground(Color.WHITE);
        labelInfoControles.setVerticalAlignment(SwingConstants.TOP);
        labelInfoControles.setBounds(-250, 20, 250, 575);
        panelGraficos.add(labelInfoControles);

        labelInfoControlesPersistente = new JLabel("<html>ESC -> Ocultar/Mostrar controles<br></html>");
        labelInfoControlesPersistente.setForeground(Color.WHITE);
        labelInfoControlesPersistente.setBounds(20, 570, 250, 10);
        panelGraficos.add(labelInfoControlesPersistente);

        labelInfoObjeto = new JLabel("<html><div style='text-align: right;'>------------------- INFORMACION -------------------<br><br>"
                + "ID OBJETO: #1<br>"
                + "FPS: 60<br><br>"
                + "Puntos: Visibles<br>"
                + "Lineas: Visibles<br>"
                + "Caras: Invisibles<br><br>"
                + "Punto de origen:<br>"
                + "X -> 450 pixeles<br>"
                + "Y -> 300 pixeles<br>"
                + "Z -> 700 pixeles<br><br>"
                + "Punto de fuga:<br>"
                + "X -> 450 pixeles<br>"
                + "Y -> 300 pixeles<br>"
                + "Z -> 250 pixeles<br>"
                + "FOV -> 250 pixeles<br><br>"
                + "Ejes activos:<br>"
                + "X -> Activado<br>"
                + "Y -> Activado<br>"
                + "Z -> Desactivado<br><br>"
                + "</div></html>");
        labelInfoObjeto.setForeground(Color.WHITE);
        labelInfoObjeto.setVerticalAlignment(SwingConstants.TOP);
        labelInfoObjeto.setHorizontalAlignment(SwingConstants.TRAILING); // Cambiado a RIGHT
        labelInfoObjeto.setBounds(905, 20, 250, 575);
        panelGraficos.add(labelInfoObjeto);

        labelInfoObjetoPersistente = new JLabel("<html>CTRL -> Ocultar/Mostrar informacion<br></html>");
        labelInfoObjetoPersistente.setForeground(Color.WHITE);
        labelInfoObjetoPersistente.setHorizontalAlignment(SwingConstants.TRAILING);
        labelInfoObjetoPersistente.setBounds(625, 570, 250, 10);
        panelGraficos.add(labelInfoObjetoPersistente);
    }

    public void initEventos() {
        panelGraficos.setFocusTraversalKeysEnabled(false);

        panelGraficos.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                xInicial = e.getX();
                yInicial = e.getY();
                System.out.println("Coordenadas del clic: (" + xInicial + ", " + yInicial + ")");
            }
        });

        panelGraficos.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                int dx = e.getX() - xInicial;
                int dy = e.getY() - yInicial;

                xInicial = e.getX();
                yInicial = e.getY();

                panelGraficos.setRotacionTransformacionMouse(dx, dy);
            }
        });

        panelGraficos.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                int keyCode = e.getKeyCode();
                switch (keyCode) {
                    case KeyEvent.VK_ESCAPE:
                        if (controlesEnPantalla) {
                            ocultarControles();
                        } else {
                            mostrarControles();
                        }
                        controlesEnPantalla = !controlesEnPantalla;
                        break;
                    case KeyEvent.VK_CONTROL:
                        if (informacionEnPantalla) {
                            ocultarInformacion();
                        } else {
                            mostrarInformacion();
                        }
                        informacionEnPantalla = !informacionEnPantalla;
                        break;
                    case KeyEvent.VK_SPACE:
                        panelGraficos.setMostrarAnimacion();
                        break;
                    case KeyEvent.VK_TAB:
                        panelGraficos.setRotacionTransformacion();
                        break;
                    case KeyEvent.VK_W:
                        panelGraficos.setRotacionTransformacionArriba();
                        break;
                    case KeyEvent.VK_A:
                        panelGraficos.setRotacionTransformacionIzquierda();
                        break;
                    case KeyEvent.VK_S:
                        panelGraficos.setRotacionTransformacionAbajo();
                        break;
                    case KeyEvent.VK_D:
                        panelGraficos.setRotacionTransformacionDerecha();
                        break;
                    case KeyEvent.VK_Q:
                        panelGraficos.setRotacionTransformacionZNegativa();
                        break;
                    case KeyEvent.VK_E:
                        panelGraficos.setRotacionTransformacionZPositiva();
                        break;
                    case KeyEvent.VK_Z:
                        panelGraficos.setMostrarPuntos();
                        break;
                    case KeyEvent.VK_X:
                        panelGraficos.setMostrarLineas();
                        break;
                    case KeyEvent.VK_C:
                        panelGraficos.setMostrarCaras();
                        break;
                    case KeyEvent.VK_1:
                        panelGraficos.setEjeXAnimacion();
                        break;
                    case KeyEvent.VK_2:
                        panelGraficos.setEjeYAnimacion();
                        break;
                    case KeyEvent.VK_3:
                        panelGraficos.setEjeZAnimacion();
                        break;
                }
            }
        });

        panelGraficos.addMouseWheelListener(new MouseWheelListener() {
            @Override
            public void mouseWheelMoved(MouseWheelEvent e) {
                int notches = e.getWheelRotation();
                if (notches < 0) {
                    panelGraficos.setEscala(10);
                } else {
                    panelGraficos.setEscala(-10);
                }
            }
        });

        panelGraficos.setFocusable(true);
        panelGraficos.requestFocusInWindow();
    }

    @Override
    public void aniadirEtiqueta(JLabel tagLabel, JLabel infoLabel, int x, int y) {
        JLabel tempLabel2 = tagLabel;
        tempLabel2.setHorizontalAlignment(SwingConstants.TRAILING);
        tempLabel2.setBounds(x, y, Constantes.TAG_LABEL_WIDTH, Constantes.TAG_LABEL_HEIGHT);
        tempLabel2.setForeground(Color.WHITE);
        listaTagLabels.add(tempLabel2);
        panelGraficos.add(tempLabel2);

        JLabel tempLabel = infoLabel;
        tempLabel.setHorizontalAlignment(SwingConstants.TRAILING);
        tempLabel.setBounds(xInicialLabels, yInicialLabels, Constantes.INFO_LABEL_WIDTH, Constantes.INFO_LABEL_HEIGHT);
        tempLabel.setForeground(Color.WHITE);
        listaInfoLabels.add(tempLabel);
        panelGraficos.add(tempLabel);

        panelGraficos.repaint();
        yInicialLabels += 20;
    }

    @Override
    public void actualizarEtiquetaInformacion(int indice, String texto) {
        if (indice >= 0 && indice < listaInfoLabels.size()) {
            listaInfoLabels.get(indice).setText(texto);

            panelGraficos.repaint();
        }
    }

    @Override
    public void actualizarEtiquetaObjeto(int indice, int x, int y) {
        if (indice >= 0 && indice < listaTagLabels.size()) {
            listaTagLabels.get(indice).setLocation(x, y);
            panelGraficos.repaint();
        }
    }

    private void ocultarControles() {
        Thread thread = new Thread(() -> {
            int tempX = labelInfoControles.getX();

            while (tempX > -labelInfoControles.getWidth()) {
                tempX -= 10;
                panelGraficos.trasladarCubos(10);
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

    private void ocultarInformacion() {
        Thread thread = new Thread(() -> {
            int tempX = labelInfoObjeto.getX();
            int panelWidth = panelGraficos.getWidth();

            while (tempX < panelWidth) {
                tempX += 10;
                panelGraficos.trasladarCubos(-10); 
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

    private void mostrarControles() {
        Thread thread = new Thread(() -> {
            int tempX = labelInfoControles.getX();

            while (tempX < 20) {
                tempX += 10;
                panelGraficos.trasladarCubos(-10);
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

    private void mostrarInformacion() {
        Thread thread = new Thread(() -> {
            int tempX = labelInfoObjeto.getX();

            while (tempX > 625) {
                tempX -= 10;
                panelGraficos.trasladarCubos(10);
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
}
