package frames;

import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import Interfaces.LabelManager;

public final class FrameAnimacion extends JFrame implements LabelManager {

    private final PanelGraficos panelGraficos = new PanelGraficos(this);
    private static final ArrayList<JLabel> listaLabels;
    private static JLabel labelInfo;

    private static int xInicialLabels;
    private static int yInicialLabels;

    static {
        listaLabels = new ArrayList<>();
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

        initComponentes();
        initEventos();
    }

    public void initComponentes() {
        panelGraficos.setLayout(null);
        panelGraficos.setBounds(0, 0, 900, 600);
        add(panelGraficos);

        for (int i = 0; i < listaLabels.size(); i++) {
            JLabel tempLabel = listaLabels.get(i);
            tempLabel.setHorizontalAlignment(SwingConstants.TRAILING);
            tempLabel.setForeground(Color.WHITE);
            panelGraficos.add(tempLabel);
        }

        labelInfo = new JLabel("<html>--------------------- CONTROLES ---------------------<br><br>"
                + "ESPACIO -> Parar/Reanudar la animacion<br>"
                + "TAB -> Alternar traslacion/Rotacion<br>"
                + "SCROLL -> Aumentar/Disminuir la escala<br><br>"
                + "Click Izq -> Rotacion (Ejes activados)<br>"
                + "SCROLL -> Traslacion (X e Y)<br><br>"
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
                + "<br>"
                + "<br>"
                + "<br>"
                + "<br>"
                + "<br>"
                + "<br>"
                + "<br>"
                + "<br>"
                + " ESC -> Ocultar informacion<br>"
                + "----------------------------------------------------------------------"
                + "</html>");
        labelInfo.setForeground(Color.WHITE);
        labelInfo.setVerticalAlignment(SwingConstants.TOP);
        labelInfo.setBounds(20, 20, 250, 575);
        panelGraficos.add(labelInfo);
    }

    public void initEventos() {
        panelGraficos.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int x = e.getX();
                int y = e.getY();
                System.out.println("Coordenadas del clic: (" + x + ", " + y + ")");
            }
        });
    }

    @Override
    public void aniadirEtiqueta(JLabel jLabel) {
        JLabel tempLabel = jLabel;
        tempLabel.setHorizontalAlignment(SwingConstants.TRAILING);
        tempLabel.setBounds(xInicialLabels, yInicialLabels, 200, 10);
        tempLabel.setForeground(Color.WHITE);
        listaLabels.add(tempLabel);
        panelGraficos.add(tempLabel);
        panelGraficos.repaint();

        yInicialLabels += 20;
    }

    @Override
    public void actualizarEtiqueta(int indice, String texto) {
        if (indice >= 0 && indice < listaLabels.size()) {
            JLabel label = listaLabels.get(indice);
            label.setText(texto);
            panelGraficos.repaint();
        }
    }
}
