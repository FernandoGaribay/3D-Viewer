package Interfaces;

import javax.swing.JLabel;

public interface LabelManager {
    void aniadirEtiqueta(JLabel tagLabel);
    void actualizarEtiquetaInformacion(int indice, String texto);
    void actualizarEtiquetaObjeto(int indice, int x, int y);
}
