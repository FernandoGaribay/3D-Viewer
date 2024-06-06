package frames;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JFrame;

public class FrameAnimacion extends JFrame {

    private static final PanelGraficos panelGraficos;

    static {
        panelGraficos = new PanelGraficos();
    }

    public FrameAnimacion() {
        setTitle("Animacion 3D");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 600);
        setResizable(false);
        setLocationRelativeTo(null);
        setVisible(true);

        panelGraficos.setBounds(0, 0, 900, 600);
        add(panelGraficos);

        panelGraficos.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                // Obtener las coordenadas del clic
                int x = e.getX();
                int y = e.getY();
                // Imprimir las coordenadas en la consola
                System.out.println("Coordenadas del clic: (" + x + ", " + y + ")");
            }
        });
    }

}
