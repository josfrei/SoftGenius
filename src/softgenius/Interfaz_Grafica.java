package softgenius;

import javax.swing.SwingUtilities;

/**
 * Clase que maneja la interfaz gráfica de la aplicación SoftGenius.
 * 
 * Esta clase se encarga de inicializar la interfaz gráfica creando una instancia
 * de la clase Login en el hilo de despacho de eventos de Swing.
 * 
 * @author Grupo 1 - 1º DAM Colexio Karbo
 */
public class Interfaz_Grafica {
    
      /**
     * Constructor de la clase Interfaz_Grafica.
     * 
     * Este constructor utiliza SwingUtilities.invokeLater para asegurar que 
     * la creación de la interfaz gráfica se realice en el hilo de despacho de eventos de Swing.
     * Enlaza con Login()
     */
    Interfaz_Grafica(){
       SwingUtilities.invokeLater(() -> new Login());
    }
    
}
