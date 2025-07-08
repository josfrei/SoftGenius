package informes;

import idioma.Idiomas;
import java.awt.Desktop;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.swing.JOptionPane;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType0Font;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;

/**
 * Método para generar el informe de vacaciones tomando como datos nombre, apellido,
 * identificadión, etc.
 * 
 * @author Grupo 1 - 1º DAM Colexio Karbo
 */
public class InformeVacaciones {

    private String idiomaActual = "Spanish";

    /**
     * Obtenemos el idioma actual para luego imprimir el informe en el idioma correspondiente.
     * @return
     * @throws SQLException 
     */
    public String obtenerIdiomaActual() throws SQLException {
        Idiomas idiomas = new Idiomas();
        return idiomas.obtenerIdiomaActual();
    }

    /**
     * Método que genera informe de vacaciones.
     * Se le pasa por parámetros los siguientes datos.
     * @param selectedCity
     * @param selectedSeccion
     * @param nombreString
     * @param apellidosString
     * @param dniString
     * @param vacaciones_totalesString
     * @param vacaciones_disfrutadasString
     * @throws SQLException 
     */
    public void informeVacaciones(String selectedCity, String selectedSeccion, String nombreString, String apellidosString, String dniString, String vacaciones_totalesString, String vacaciones_disfrutadasString) throws SQLException {

        idiomaActual = obtenerIdiomaActual();

        // Convertir las cadenas a enteros y realizar la resta
        int vacacionesTotales = 0;
        int vacacionesDisfrutadas = 0;

        // Verificar que las cadenas no estén vacías antes de convertirlas a enteros
        if (!vacaciones_totalesString.isEmpty()) {
            vacacionesTotales = Integer.parseInt(vacaciones_totalesString);
        }

        if (!vacaciones_disfrutadasString.isEmpty()) {
            vacacionesDisfrutadas = Integer.parseInt(vacaciones_disfrutadasString);
        }

        // Calcular las vacaciones restantes
        int vacacionesRestantes = vacacionesTotales - vacacionesDisfrutadas;

        try (PDDocument document = new PDDocument()) {
            PDPage page = new PDPage();
            document.addPage(page);

            // Definir los márgenes
            float margin = 50;
            float paddingTop = 125; // Ajusta este valor para aumentar el espacio superior
            float width = page.getMediaBox().getWidth() - 2 * margin;
            float startX = margin;
            float startY = page.getMediaBox().getHeight() - margin - paddingTop; // Ajuste para agregar espacio superior

            // Cargar la fuente TTF desde un archivo
            PDType0Font font;
            try (InputStream fontStream = new FileInputStream(new File("fuente.ttf"))) {
                font = PDType0Font.load(document, fontStream);
            } catch (IOException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(null, "Error al cargar la fuente", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Cargar la imagen
            InputStream imageStream = TablaGenerarInformes.class.getResourceAsStream("Logo.jpeg"); // Asegúrate de que la ruta es correcta
            if (imageStream == null) {
                throw new IOException("No se pudo encontrar el archivo de imagen");
            }

            // Leer los bytes de la imagen
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = imageStream.read(buffer)) != -1) {
                baos.write(buffer, 0, bytesRead);
            }
            byte[] imageBytes = baos.toByteArray();
            baos.close();

            // Crear el objeto de imagen
            PDImageXObject image = PDImageXObject.createFromByteArray(document, imageBytes, "imagen");

            try (PDPageContentStream contentStream = new PDPageContentStream(document, page, PDPageContentStream.AppendMode.APPEND, true)) {
                contentStream.setFont(font, 12);
                contentStream.beginText();
                contentStream.newLineAtOffset(startX, startY);

                String[] lines;
                if (idiomaActual.equals("English")) {
                    // Crear el contenido del PDF con espaciados personalizados
                    lines = new String[]{
                        selectedCity + " " + new SimpleDateFormat("dd/MM/yyyy HH:mm").format(new Date()), // Fecha actual
                        "Employee: " + nombreString + " " + apellidosString,
                        "Identification document: " + dniString,
                        "",
                        "Subject: notification of vacations pending enjoyment.",
                        "",
                        "Dear " + nombreString + ":",
                        "By means of this present and following your request, we are writing to you with the aim of notifying you of the vacation days that remain to be enjoyed as of today.",
                        "",
                        "According to our records, you have enjoyed " + vacacionesDisfrutadas + " vacation days during the current year from the " + vacaciones_totalesString + " days available to you as of today. Therefore, you have " + vacacionesRestantes + " vacation days left. ",
                        "We remind you that it is important to plan your vacations in advance and communicate them to your direct supervisor to ensure correct organization of work and the well-being of the team.",
                        "We remain at your disposal for any questions or additional clarification you may need.",
                        "",
                        "Sincerely,",
                        "",
                        "Department " + selectedSeccion + " of SoftGenius Corporation S.A."
                    };
                } else {
                    lines = new String[]{
                        selectedCity + " " + new SimpleDateFormat("dd/MM/yyyy HH:mm").format(new Date()), // Fecha actual
                        "Empleado: " + nombreString + " " + apellidosString,
                        "Documento de identidad: " + dniString,
                        "",
                        "Asunto: notificación de vacaciones pendientes de disfrute.",
                        "",
                        "Estimado/a " + nombreString + ":",
                        "Por medio de la presente y tras su solicitud, nos dirigimos a usted con el ánimo de notificarle los días de vacaciones que le restan por disfrutar a la fecha de hoy.",
                        "",
                        "Según nuestros registros, usted ha disfrutado de " + vacaciones_disfrutadasString + " días de vacaciones durante el presente año de " + vacaciones_totalesString + " días que dispone a fecha de hoy. Por lo tanto, le restan " + vacacionesRestantes + " días de vacaciones.",
                        "Le recordamos que es importante planificar sus vacaciones con antelación y comunicarlas a su supervisor directo para asegurar una correcta organización del trabajo y el bienestar del equipo.",
                        "Quedamos a su disposición para cualquier duda o aclaración adicional que pueda necesitar.",
                        "",
                        "Atentamente,",
                        "",
                        "Departamento " + selectedSeccion + " de SoftGenius Corporation S.A."
                    };
                }
                // Definir los espaciados personalizados para líneas específicas
                float[] lineSpacings = {
                    20f, // Ciudad [fecha y hora]
                    10f, // Empleado: [Nombre y apellido]
                    10f, // DNI: [DNI]
                    20f, // Departamento: [Departamento]
                    30f, // Espacio extra antes del Asunto
                    20f, // Asunto: notificación de vacaciones pendientes de disfrute.
                    30f, // Espacio extra antes de Estimado/a
                    20f, // Estimado/a [Nombre del Empleado]:
                    15f, // Por medio de la presente y tras su solicitud, nos dirigimos a usted con el ánimo de notificarle los días de vacaciones que le restan por disfrutar a la fecha de hoy.
                    30f, // Espacio extra después de este párrafo
                    15f, // Según nuestros registros, usted ha disfrutado de [días disfrutados] días de vacaciones durante el presente año de [días totales] días que dispone a fecha de hoy. Por lo tanto, le restan [días restantes] días de vacaciones.
                    15f, // Le recordamos que es importante planificar...
                    20f, // Quedamos a su disposición...
                    30f, // Espacio extra antes de Atentamente,
                    20f, // Atentamente,
                    60f, // Espacio extra
                    // Espacio extra antes de Departamento de RRHH...
                    20f // Departamento de RRHH de SoftGenius Corporation S.A.
                };

                for (int i = 0; i < lines.length; i++) {
                    List<String> wrappedLines = wrapText(lines[i], font, 12, width);
                    for (String wrappedLine : wrappedLines) {
                        contentStream.showText(wrappedLine);
                        contentStream.newLineAtOffset(0, -lineSpacings[i]); // Usar el espaciado personalizado
                    }
                    if (wrappedLines.size() > 0) {
                        contentStream.newLineAtOffset(0, -lineSpacings[i]); // Espacio adicional si la línea se ha envuelto
                    }
                }

                contentStream.endText();

                // Redimensionar la imagen para que sea más pequeña
                float maxWidth = 100; // Máximo ancho permitido para la imagen
                float maxHeight = 100; // Máximo alto permitido para la imagen
                float imageWidth = image.getWidth();
                float imageHeight = image.getHeight();

                // Calcular el factor de escala para ajustar la imagen al tamaño máximo
                float scale = Math.min(maxWidth / imageWidth, maxHeight / imageHeight);

                // Aplicar el factor de escala a la anchura y altura de la imagen
                imageWidth *= scale;
                imageHeight *= scale;

                // Calcular la posición en la esquina derecha superior
                float imageX = page.getMediaBox().getWidth() - imageWidth - margin;
                float imageY = page.getMediaBox().getHeight() - imageHeight - margin - 10; // Mover la imagen hacia abajo en 20 unidades

                // Dibujar la imagen en la posición calculada
                contentStream.drawImage(image, imageX, imageY, imageWidth, imageHeight);
            }

            String userProfile = System.getenv("USERPROFILE");

            // Crear directorio si no existe
            File directoryVacances = new File(userProfile + File.separator + "Documents" + File.separator + "Informes_Softgenius");
            if (!directoryVacances.exists()) {
                directoryVacances.mkdirs(); // Crear directorio si no existe
            }

            //Nombre archivo
            String fileName = "informe_" + new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date()) + "_vacaciones_" + dniString + ".pdf";
            // Construir la ruta del archivo
            String filePath = directoryVacances.getPath() + File.separator + fileName;

            try (OutputStream outputStream = new FileOutputStream(filePath)) {
                // Guardar el archivo
                document.save(outputStream);

                // Abrir el archivo de informe después de que el usuario acepte el mensaje de éxito
                try {
                    File informeFile = new File(filePath);
                    if (informeFile.exists()) {
                        Desktop.getDesktop().open(informeFile);
                    } else {
                        System.out.println("El archivo de informe no existe.");
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }

                System.out.println("PDF guardado en: " + filePath);
            } catch (IOException e) {
                System.out.println("Fallo: " + e);
            }

        } catch (IOException e) {
            System.out.println("Fallo 2 : " + e);
        } catch (Exception e) {
            System.out.println("Fallo 3: " + e);
        }
    }

    private static List<String> wrapText(String text, PDType0Font font, float fontSize, float width) throws IOException {
        List<String> lines = new ArrayList<>();
        int lastSpace = -1;
        while (text.length() > 0) {
            int spaceIndex = text.indexOf(' ', lastSpace + 1);
            if (spaceIndex < 0) {
                spaceIndex = text.length();
            }
            String subString = text.substring(0, spaceIndex);
            float size = fontSize * font.getStringWidth(subString) / 1000;
            if (size > width) {
                if (lastSpace < 0) {
                    lastSpace = spaceIndex;
                }
                subString = text.substring(0, lastSpace);
                lines.add(subString);
                text = text.substring(lastSpace).trim();
                lastSpace = -1;
            } else if (spaceIndex == text.length()) {
                lines.add(text);
                text = "";
            } else {
                lastSpace = spaceIndex;
            }
        }
        return lines;
    }
}
