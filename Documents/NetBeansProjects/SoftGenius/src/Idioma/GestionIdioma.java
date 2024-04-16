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
public class GestionIdioma extends Properties {

    private static final long serialVersionUID = 1L;

    public GestionIdioma(String idioma) {

        //Modificar si quieres añadir mas idiomas
        //Cambia el nombre de los ficheros o añade los necesarios
        switch (idioma) {
            case "Español":
                getProperties("espanol.properties");
                break;
            case "Inglés":
                getProperties("ingles.properties");
                break;
            default:
                getProperties("espanol.properties");
        }

    }

    private void getProperties(String idioma) {
        try {
            this.load(getClass().getResourceAsStream(idioma));
        } catch (IOException ex) {

        }
    }
}
