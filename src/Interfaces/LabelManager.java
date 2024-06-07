package Interfaces;

import javax.swing.JLabel;

public interface LabelManager {
    void aniadirEtiqueta(JLabel tagLabel, JLabel infoLabel, int x, int y);
    void actualizarEtiquetaInformacion(int indice, String texto);
    void actualizarEtiquetaObjeto(int indice, int x, int y);
}
