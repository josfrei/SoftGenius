package idioma;

import java.io.IOException;
import java.util.Properties;

/**
 * Clase para gestionar las propiedades del idioma. Extiende la clase Properties
 * para manejar las propiedades del idioma. Permite cargar y gestionar archivos
 * de propiedades de idioma para diferentes idiomas.
 *
 * @author Grupo 1 - 1º curso DAM Colexio Karbo.
 */
public class Idioma extends Properties {

    private static final long serialVersionUID = 1L;

    /**
     * Constructor de la clase Idioma. Carga las propiedades por defecto primero
     * y luego las propiedades del idioma especificado, sobrescribiendo
     * cualquier propiedad por defecto si es necesario.
     *
     * @param idioma El idioma para el cual cargar las propiedades.
     */
    public Idioma(String idioma) {

        switch (idioma) {
            case "Spanish":
                getProperties("Spanish_es_ES.properties");
                break;
            case "English":
                getProperties("English.properties");
                break;

            default:
                getProperties("Spanish_es_ES.properties");
        }
    }

    /**
     * Método privado para cargar las propiedades desde un archivo de
     * propiedades.
     *
     * @param fileName El nombre del archivo de propiedades a cargar.
     */
    private void getProperties(String idioma) {
        try {
            this.load(getClass().getResourceAsStream(idioma));
        } catch (IOException ex) {

        }
    }
}
