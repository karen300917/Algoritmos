import java.io.*;
import java.nio.file.*;


public class BibConverter {

	// Método para leer el contenido de un archivo y devolverlo como una cadena
	public static String readFile(String path) throws IOException {
		return new String(Files.readAllBytes(Paths.get(path)));
	}

	// Método para procesar todos los archivos .bib de una carpeta
	public static void convertBibFiles(String folderPath, String outputPath) {
		File folder = new File(folderPath);
		File[] listOfFiles = folder.listFiles();

		try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputPath))) {
			for (File file : listOfFiles) {
				if (file.isFile() && file.getName().endsWith(".bib")) {
					// Leer el contenido del archivo .bib
					String content = readFile(file.getPath());

					// Escribir el contenido en el archivo de salida
					writer.write("Contenido del archivo: " + file.getName() + "\n");
					writer.write(content);
					writer.write("\n\n");
					System.out.println("Procesado: " + file.getName());
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		// Ruta de la carpeta que contiene los archivos .bib
		 //String folderPath = "C:/proyectoBib/Taylor y Francis";

		// Ruta del archivo de salida
		//String outputPath = "C:/proyectoBib/combinedTaylor y Francis.bib";

		// Procesar los archivos .bib
		 //convertBibFiles(folderPath, outputPath);

		String filePath = "C:/proyectoBib/combinedIEEE.bib"; // Cambia la ruta por la ubicación de tu archivo
																		 							
		int entryCount = 0;

		try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
			String line;
			while ((line = br.readLine()) != null) {
				// Cada entrada en un archivo .bib empieza con @
				if (line.trim().startsWith("@")) {
					entryCount++;
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		System.out.println("Total de entradas en el archivo .bib: " + entryCount);
	}
}