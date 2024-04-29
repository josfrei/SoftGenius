/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Idioma;

/**
 *
 * @author josef
 */
import Idioma.Idioma;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.util.ResourceBundle;
import javax.swing.JButton;
import javax.swing.JLabel;

public class GestorIdioma {
    
    private String idiomaActual = "Spanish"; // Idioma predeterminado
    
    public void cambiarIdioma(String nuevoIdioma, Component[] componentes) {
        idiomaActual = nuevoIdioma;
        actualizarComponentes(componentes);
    }
    
    private void actualizarComponentes(Component[] componentes) {
        for (Component componente : componentes) {
            if (componente instanceof JLabel) {
                actualizarLabel((JLabel) componente);
            } else if (componente instanceof JButton) {
                actualizarBoton((JButton) componente);
            } 
        }
    }
    
    private void actualizarLabel(JLabel label) {
        ResourceBundle resourceBundle = ResourceBundle.getBundle("Idioma." + idiomaActual);
        String textoClave = label.getText(); // Clave para obtener el texto en el archivo de propiedades
        String textoNuevo = resourceBundle.getString(textoClave);
        label.setText(textoNuevo);
    }
    
    private void actualizarBoton(JButton boton) {
        ResourceBundle resourceBundle = ResourceBundle.getBundle("Idioma." + idiomaActual);
        String textoClave = boton.getText(); // Clave para obtener el texto en el archivo de propiedades
        String textoNuevo = resourceBundle.getString(textoClave);
        boton.setText(textoNuevo);
    }
    
}