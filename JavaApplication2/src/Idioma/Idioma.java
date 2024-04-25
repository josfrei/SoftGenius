/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Idioma;

import java.io.IOException;
import java.util.Properties;

/**
 *
 * @author josef
 */
public class Idioma extends Properties {

    private static final long serialVersionUID = 1L;

    public Idioma(String idioma) {

        //Modificar si quieres añadir mas idiomas
        //Cambia el nombre de los ficheros o añade los necesarios
        switch (idioma) {
            case "Spanish":
                getProperties("Spanish.properties");
                break;
            case "English":
                getProperties("English.properties");
                break;
                
            default:
                getProperties("Spanish.properties");
        }

    }

    private void getProperties(String idioma) {
        try {
            this.load(getClass().getResourceAsStream(idioma));
        } catch (IOException ex) {

        }
    }
}
