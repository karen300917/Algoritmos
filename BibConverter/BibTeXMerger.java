import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.stream.Collectors;

public class BibTeXMerger {

	public static void main(String[] args) {
		// Directorio donde se encuentran los archivos .bib
		String sourceDirectory = "C:/proyectoBib/Taylor y Francis";

		// Ruta del archivo .bib combinado
		String outputFilePath = "C://proyectoBib//combinedTaylor y Francis.bib";


		try {
			List<Path> bibFiles = listBibFiles(Paths.get(sourceDirectory));

			// Usar LinkedHashSet para mantener el orden y evitar duplicados
			Set<String> combinedEntries = new LinkedHashSet<>();

			for (Path bibFile : bibFiles) {
				System.out.println("Procesando: " + bibFile.toString());
				List<String> entries = extractEntries(bibFile);
				combinedEntries.addAll(entries);
			}

			writeCombinedBib(combinedEntries, Paths.get(outputFilePath));
			System.out.println("Archivos .bib combinados exitosamente en: " + outputFilePath);

		} catch (IOException e) {
			System.err.println("Ocurrió un error: " + e.getMessage());
			e.printStackTrace();
		}
	}

	/**
	 * Lista todos los archivos .bib en el directorio dado y sus subdirectorios.
	 */
	private static List<Path> listBibFiles(Path directory) throws IOException {
		if (!Files.exists(directory)) {
			throw new FileNotFoundException("El directorio " + directory.toString() + " no existe.");
		}

		return Files.walk(directory).filter(path -> !Files.isDirectory(path))
				.filter(path -> path.toString().toLowerCase().endsWith(".bib")).collect(Collectors.toList());
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