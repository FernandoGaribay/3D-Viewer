package Interfaces;

import javax.swing.JLabel;

public interface ManejadorDeInformacion {
    void aniadirEtiqueta(JLabel label);
    void actualizarEtiqueta(int indice, String texto);
}
