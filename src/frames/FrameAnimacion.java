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
import java.awt.event.KeyListener;
import java.util.logging.Level;
import java.util.logging.Logger;

public final class FrameAnimacion extends JFrame implements LabelManager {

    private final PanelGraficos panelGraficos = new PanelGraficos(this);
    private static final ArrayList<JLabel> listaInfoLabels;
    private static final ArrayList<JLabel> listaTagLabels;
    private static JLabel labelInfoVisible;
    private static JLabel labelInfoOculta;
    private static boolean controlesEnPantalla;

    private static int xInicialLabels;
    private static int yInicialLabels;

    static {
        listaInfoLabels = new ArrayList<>();
        listaTagLabels = new ArrayList<>();
        controlesEnPantalla = false;
        xInicialLabels = 675;
        yInicialLabels = 25;
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
        panelGraficos.setBounds(0, 0, 900, 600);
        add(panelGraficos);

        for (int i = 0; i < listaInfoLabels.size(); i++) {
            JLabel tempLabel = listaInfoLabels.get(i);
            tempLabel.setHorizontalAlignment(SwingConstants.TRAILING);
            tempLabel.setForeground(Color.WHITE);
            panelGraficos.add(tempLabel);
        }

        labelInfoVisible = new JLabel("<html>--------------------- CONTROLES ---------------------<br><br>"
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
        labelInfoVisible.setForeground(Color.WHITE);
        labelInfoVisible.setVerticalAlignment(SwingConstants.TOP);
        labelInfoVisible.setBounds(-250, 20, 250, 575);
        panelGraficos.add(labelInfoVisible);
        
        labelInfoOculta = new JLabel("<html> ESC -> Ocultar/Mostrar informacion<br></html>");
        labelInfoOculta.setForeground(Color.WHITE);
        labelInfoOculta.setBounds(20, 570, 250, 10);
        panelGraficos.add(labelInfoOculta);
    }

    public void initEventos() {
        panelGraficos.setFocusTraversalKeysEnabled(false);

        panelGraficos.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int x = e.getX();
                int y = e.getY();
                System.out.println("Coordenadas del clic: (" + x + ", " + y + ")");
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
                    case KeyEvent.VK_SPACE:
                        System.out.println("ESPACIO Presionado");
                        break;
                    case KeyEvent.VK_TAB:
                        System.out.println("TAB Presionado");
                        break;
                    case KeyEvent.VK_W:
                        System.out.println("W Presionado");
                        break;
                    case KeyEvent.VK_A:
                        System.out.println("A Presionado");
                        break;
                    case KeyEvent.VK_S:
                        System.out.println("S Presionado");
                        break;
                    case KeyEvent.VK_D:
                        System.out.println("D Presionado");
                        break;
                    case KeyEvent.VK_Q:
                        System.out.println("Q Presionado");
                        break;
                    case KeyEvent.VK_E:
                        System.out.println("E Presionado");
                        break;
                    case KeyEvent.VK_Z:
                        System.out.println("Z Presionado");
                        break;
                    case KeyEvent.VK_X:
                        System.out.println("X Presionado");
                        break;
                    case KeyEvent.VK_C:
                        System.out.println("C Presionado");
                        break;
                    case KeyEvent.VK_1:
                        System.out.println("1 Presionado");
                        break;
                    case KeyEvent.VK_2:
                        System.out.println("2 Presionado");
                        break;
                    case KeyEvent.VK_3:
                        System.out.println("3 Presionado");
                        break;
                }
            }
        });

        panelGraficos.setFocusable(true);
        panelGraficos.requestFocusInWindow();
    }

    @Override
    public void aniadirEtiqueta(JLabel tagLabel, JLabel infoLabel, int x, int y) {
        JLabel tempLabel = infoLabel;
        tempLabel.setHorizontalAlignment(SwingConstants.TRAILING);
        tempLabel.setBounds(x, y, 200, 10);
        tempLabel.setForeground(Color.WHITE);
        listaInfoLabels.add(tempLabel);
        panelGraficos.add(tempLabel);

        JLabel tempLabel2 = tagLabel;
        tempLabel2.setHorizontalAlignment(SwingConstants.TRAILING);
        tempLabel2.setBounds(xInicialLabels, yInicialLabels, 200, 10);
        tempLabel2.setForeground(Color.WHITE);
        listaTagLabels.add(tempLabel2);
        panelGraficos.add(tempLabel2);

        panelGraficos.repaint();
        yInicialLabels += 20;
    }

    @Override
    public void actualizarEtiqueta(int indice, String texto) {
        if (indice >= 0 && indice < listaInfoLabels.size()) {
            JLabel label = listaInfoLabels.get(indice);
            label.setText(texto);
            panelGraficos.repaint();
        }
    }

    private void ocultarControles() {
        Thread thread = new Thread(() -> {
            int tempX = labelInfoVisible.getX();

            while (tempX > -labelInfoVisible.getWidth()) {
                tempX -= 10;
                panelGraficos.getCube(10);
                labelInfoVisible.setLocation(tempX, labelInfoVisible.getY());
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
            int tempX = labelInfoVisible.getX();

            while (tempX < 20) {
                tempX += 10;
                panelGraficos.getCube(-10);
                labelInfoVisible.setLocation(tempX, labelInfoVisible.getY());
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
