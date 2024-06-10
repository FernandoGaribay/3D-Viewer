package frames;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JFrame;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import utils.Constantes;

public final class FrameAnimacion extends JFrame {

    private final PanelGraficos panelGraficos = new PanelGraficos();

    private static boolean controlesEnPantalla;
    private static boolean informacionEnPantalla;

    private static int xInicial;
    private static int yInicial;

    static {
        controlesEnPantalla = false;
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

        initComponentes();
        initEventos();
    }

    public void initComponentes() {
        panelGraficos.setBounds(0, 0, Constantes.FRAME_WIDTH, Constantes.FRAME_HEIGHT);
        add(panelGraficos);
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
                    case KeyEvent.VK_ALT:
                        if (informacionEnPantalla) {
                            panelGraficos.ocultarInformacion();
                        } else {
                            panelGraficos.mostrarInformacion();
                        }
                        informacionEnPantalla = !informacionEnPantalla;
                        break;
                    case KeyEvent.VK_CONTROL:
                        if (controlesEnPantalla) {
                            panelGraficos.ocultarControles();
                        } else {
                            panelGraficos.mostrarControles();
                        }
                        controlesEnPantalla = !controlesEnPantalla;
                        break;
                    case KeyEvent.VK_SPACE:
                        panelGraficos.setMostrarAnimacion();
                        break;
                    case KeyEvent.VK_TAB:
                        panelGraficos.setRotacionTransformacion();
                        break;
                    case KeyEvent.VK_RIGHT:
                        panelGraficos.siguienteElemento();
                        break;
                    case KeyEvent.VK_LEFT:
                        panelGraficos.anteriorElemento();
                        break;
                    case KeyEvent.VK_UP:
                        panelGraficos.setEscala(true);
                        break;
                    case KeyEvent.VK_DOWN:
                        panelGraficos.setEscala(false);
                        break;
                    case KeyEvent.VK_P:
                        panelGraficos.generarColores();
                        break;
                    case KeyEvent.VK_O:
                        panelGraficos.generarPuntos();
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
                    panelGraficos.setEscala(true);
                } else {
                    panelGraficos.setEscala(false);
                }
            }
        });
    }
}
