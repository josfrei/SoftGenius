/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package TestIG;

import javax.swing.SwingUtilities;

/**
 *
 * @author Iago
 */
public class Interfaz_Grafica {
    
    
    Interfaz_Grafica(){
       SwingUtilities.invokeLater(() -> new Login());
    }
    
}
