/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Config;

import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import TestIG.LeftPanelMPv2;

/**
 * Clase creada para el cambio del logo por parte de los niveles 0 y 1(admin y empresario).
 * @author josef
 */
public class CambioImagenCorporativa {

    /**
     * Método para cambiar la imagen del logo.
     * @param panel
     * @param rutaImagen 
     */
    public void cambiarImagenLogo(LeftPanelMPv2 panel, String rutaImagen) {
        try {
            ImageIcon nuevaIconLogo = new ImageIcon(rutaImagen);
            Image nuevaImagen = nuevaIconLogo.getImage().getScaledInstance(200, 150, Image.SCALE_SMOOTH);
            nuevaIconLogo = new ImageIcon(nuevaImagen);
            panel.lblLogo.setIcon(nuevaIconLogo);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Este método guarda la imagen escogida en la carpeta recursos para así estar más localizada.
     * @param imagenSeleccionada 
     */
    public void guardarImagenEnCarpeta(File imagenSeleccionada) {
        try {
            // Carpeta donde se guardará la imagen
            File carpetaDestino = new File("recursos/");

            // Copiar la imagen seleccionada a la carpeta destino
            Files.copy(imagenSeleccionada.toPath(), new File(carpetaDestino, imagenSeleccionada.getName()).toPath());

            System.out.println("Imagen guardada en: " + carpetaDestino.getAbsolutePath());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Método para llamar desde el botón que causa la acción del cambio de logo.
     * @param panel 
     */
    private void accionCambiarLogo(LeftPanelMPv2 panel) {
        // Carpeta inicial para iniciar la búsqueda de la imagen.
        File carpetaRecursos = new File("recursos/");

        JFileChooser fileChooser = new JFileChooser(carpetaRecursos); // Creo JFileChooser
        int resultado = fileChooser.showOpenDialog(panel); // Aparece ventana de selección de archivo

        if (resultado == JFileChooser.APPROVE_OPTION) { // Si el usuario selecciona un archivo
            File archivoSeleccionado = fileChooser.getSelectedFile(); // Obtener el archivo seleccionado
            String rutaImagen = archivoSeleccionado.getAbsolutePath(); // Obtener la ruta del archivo

            // Llamar al método cambiarImagenLogo con la ruta de la imagen seleccionada
            cambiarImagenLogo(panel, rutaImagen);

            // Guardar la imagen seleccionada en la carpeta Recursos para tenerlo a mano siempre
            guardarImagenEnCarpeta(archivoSeleccionado);
        }
    }

}
