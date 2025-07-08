package informes;

import javax.swing.JOptionPane;

/**
 *
 * @author Grupo 1 - 1º DAM Colexio Karbo
 */
public class InformacionBotonInfo {
    
     public void showInformation_ES() {
        String informationText = "<html><body style='width: 400px;'>"
                + "<h2>Instrucciones para imprimir datos</h2>"
                + "<h3>Interfaz Principal</h3>"
                + "<p>Al iniciar la aplicación, verás una ventana principal que muestra una tabla con información de las tablas.</p>"
                + "<h4>Paginación de Registros</h4>"
                + "<ul>"
                + "<li><b>Siguiente y Anterior:</b> Puedes navegar entre las páginas de registros utilizando los botones \"Siguiente\" y \"Anterior\".</li>"
                + "<li><b>Primera página y Última página:</b> Los botones \"Primera página\" y \"Última página\" te llevan a la primera y última página de registros, respectivamente.</li>"
                + "</ul>"
                + "<h4>Búsqueda</h4>"
                + "<p>Localiza un registro en concreto utilizando el campo de búsqueda en la parte superior. Para buscar selecciona el criterio utilizando los botones de radio.</p>"
                + "<h4>Imprime:</h4>"
                + "<ul>"
                + "<li><b>Tabla:</b> Selecciona la base de datos y la tabla elegida y pulsa \"Imprimir tabla\"</li>"
                + "<li><b>Registro:</b> Selecciona una fila en la tabla y haz clic en el botón \"Imprimir registro\".</li>"
                + "<li><b>Días de vacaciones:</b> Selecciona un registro en la tabla y haz clic en el botón \"Imprimir vacaciones\".</li>"
                + "</ul>"
                + "<p>¡Eso es todo! Esperamos que disfrutes utilizando nuestra aplicación de gestión de empleados. Si tienes alguna pregunta o problema, no dudes en contactar con nuestro equipo de soporte.</p>"
                + "</body></html>";

        JOptionPane.showMessageDialog(null, informationText, "Información", JOptionPane.INFORMATION_MESSAGE);
    }

    public void showInformation_EN() {
        String informationText = "<html><body style='width: 400px;'>"
                + "<h2>Instructions for Printing Data</h2>"
                + "<h3>Main Interface</h3>"
                + "<p>Upon launching the application, you will see a main window displaying a table with information from the databases.</p>"
                + "<h4>Record Pagination</h4>"
                + "<ul>"
                + "<li><b>Next and Previous:</b> You can navigate between record pages using the \"Next\" and \"Previous\" buttons.</li>"
                + "<li><b>First Page and Last Page:</b> The \"First Page\" and \"Last Page\" buttons take you to the first and last record pages, respectively.</li>"
                + "</ul>"
                + "<h4>Search</h4>"
                + "<p>Locate a specific record using the search field at the top. To search, select the criteria using the radio buttons.</p>"
                + "<h4>Print:</h4>"
                + "<ul>"
                + "<li><b>Table:</b> Select the database and chosen table, then press \"Print table\".</li>"
                + "<li><b>Record:</b> Select a row in the table and click the \"Print record\" button.</li>"
                + "<li><b>Vacation Days:</b> Select a record in the table and click the \"Print vacation\" button.</li>"
                + "</ul>"
                + "<p>That's it! We hope you enjoy using our employee management application. If you have any questions or issues, feel free to contact our support team.</p>"
                + "</body></html>";

        JOptionPane.showMessageDialog(null, informationText, "Information", JOptionPane.INFORMATION_MESSAGE);
    }
}
