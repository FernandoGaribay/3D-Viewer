package Interfaces;

import javax.swing.JLabel;

public interface LabelManager {
    void aniadirEtiqueta(JLabel tagLabel, JLabel infoLabel, int x, int y);
    void actualizarEtiqueta(int indice, String texto);
}
