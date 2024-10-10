
import java.io.*;
import java.nio.file.*;
import java.util.*;

public class BibTeXMerger2 {

	public static void main(String[] args) {
		// Lista de rutas de archivos .bib que se deben combinar
		String[] bibFiles = {
				"C:/proyectoBib/combinedIEEE.bib",
				"C:/proyectoBib/combinedSAGE.bib",
				"C:/proyectoBib/combinedScienceDirect.bib",
				"C:/proyectoBib/combinedScopus.bib",
				"C:/proyectoBib/combinedTaylor y Francis.bib",
				 };

		// Ruta del archivo combinado
		String outputFilePath = "C:/proyectoBib/combinedTodosBib.bib";

		try {
			// Usar LinkedHashSet para evitar duplicados y mantener el orden
			Set<String> combinedEntries = new LinkedHashSet<>();

			// Leer cada archivo .bib y extraer las entradas
			for (String bibFile : bibFiles) {
				System.out.println("Procesando: " + bibFile);
				List<String> entries = extractEntries(Paths.get(bibFile));
				combinedEntries.addAll(entries);
			}

			// Escribir todas las entradas combinadas en un archivo .bib
			writeCombinedBib(combinedEntries, Paths.get(outputFilePath));
			System.out.println("Archivos .bib combinados exitosamente en: " + outputFilePath);

		} catch (IOException e) {
			System.err.println("Ocurrió un error: " + e.getMessage());
			e.printStackTrace();
		}
	}

	/**
	 * Extrae las entradas de un archivo .bib.
	 */
	private static List<String> extractEntries(Path bibFile) throws IOException {
		List<String> entries = new ArrayList<>();
		StringBuilder entryBuilder = new StringBuilder();
		boolean insideEntry = false;

		try (BufferedReader reader = Files.newBufferedReader(bibFile)) {
			String line;
			while ((line = reader.readLine()) != null) {
				line = line.trim();
				if (line.startsWith("@")) {
					if (insideEntry) {
						// Finalizar la entrada anterior
						entries.add(entryBuilder.toString());
						entryBuilder.setLength(0);
					}
					insideEntry = true;
				}

				if (insideEntry) {
					entryBuilder.append(line).append(System.lineSeparator());
					if (line.endsWith("}")) {
						// Finalizar la entrada actual
						entries.add(entryBuilder.toString());
						entryBuilder.setLength(0);
						insideEntry = false;
					}
				}
			}

			// Añadir última entrada si existe
			if (entryBuilder.length() > 0) {
				entries.add(entryBuilder.toString());
			}
		}

		return entries;
	}

	/**
	 * Escribe las entradas combinadas en un archivo .bib.
	 */
	private static void writeCombinedBib(Set<String> entries, Path outputFile) throws IOException {
		try (BufferedWriter writer = Files.newBufferedWriter(outputFile)) {
			for (String entry : entries) {
				writer.write(entry);
				writer.write(System.lineSeparator());
				writer.write(System.lineSeparator());
			}
		}
	}
}