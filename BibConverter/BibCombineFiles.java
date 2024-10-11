
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

public class BibCombineFiles {

    public static void main(String[] args){
        String rootDirectory = "./";
        String outputFilePath = "../combinados.bib";

        combineBibFiles(rootDirectory, outputFilePath);
        System.out.println("Cantidad de articulos encontrados: " + contador);
    }

    private static String removeEmails(String text) {
        // Expresión regular para detectar correos electrónicos
        String emailRegex = "[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,7}";

        // Crear el patrón
        Pattern pattern = Pattern.compile(emailRegex);

        // Reemplazar todas las coincidencias de correos electrónicos con una cadena vacía
        Matcher matcher = pattern.matcher(text);
        return matcher.replaceAll("");
    }

    private static int controlador = 0;
    private static int contador = 0;
    public static void combineBibFiles(String rootDirectory, String outputFilePath){
        
        try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(outputFilePath), StandardCharsets.UTF_8)) {
            // Stream para recorrer todos los archivos .bib en el directorio y subdirectorios
            try (Stream<Path> paths = Files.walk(Paths.get(rootDirectory))) {
                paths
                    .filter(Files::isRegularFile)  // Filtrar solo archivos
                    .filter(path -> path.toString().endsWith(".bib"))  // Filtrar solo archivos .bib
                    .forEach(path -> {
                        try {
                            // Leer cada archivo .bib con UTF-8 y escribir su contenido en el archivo combinado
                            Files.lines(path, StandardCharsets.UTF_8).forEach(line -> {
                                try {
                                    line = removeEmails(line);
                                    if(line.contains("@")){
                                        contador++;
                                        if(controlador==1){
                                            writer.write("db={"+path.getName(2)+"},");
                                            writer.newLine();
                                            controlador--;
                                        }
                                        controlador++;
                                    }
                                    writer.write(line);
                                    writer.newLine();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            });
                            writer.write(System.lineSeparator());  // Añadir un salto de línea entre archivos
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    });
            }
            System.out.println("Archivos combinados correctamente en " + outputFilePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }  
}
