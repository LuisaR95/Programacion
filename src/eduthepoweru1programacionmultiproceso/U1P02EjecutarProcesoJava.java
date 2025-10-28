package eduthepoweru1programacionmultiproceso;

import java.io.File;
import java.io.IOException;

public class U1P02EjecutarProcesoJava {

    private static final String JAVA = "java";
    private static final String VERSION = "-version";

    public static void main(String[] args) {

        ProcessBuilder pb = new ProcessBuilder(JAVA, VERSION);

        // Método 1: Redireccionar directamente a la consola
        // pb.redirectOutput(ProcessBuilder.Redirect.INHERIT);
        // pb.redirectError(ProcessBuilder.Redirect.INHERIT);

        /*try {
            Process p = pb.start();

            /* Método 2: Leer la salida con BufferedReader
            BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String line;
            while ((line = br.readLine()) != null) {
                System.out.println(line);
            }

            // Leer la salida de error también (java -version usualmente escribe en stderr)
            BufferedReader brError = new BufferedReader(new InputStreamReader(p.getErrorStream()));
            while ((line = brError.readLine()) != null) {
                System.err.println(line);
            }

            // Esperar a que el proceso termine (opcional pero recomendable)
            int exitCode = p.waitFor();
            System.out.println("El proceso terminó con código: " + exitCode);

        } catch (IOException | InterruptedException e) {
            System.err.println("Error al iniciar el proceso: " + JAVA + " " + VERSION);
            e.printStackTrace();
        }*/

        // Opcion 3 volcar salida a fichero
        pb.redirectOutput(new File("./resources/Salida.txt"));
        pb.redirectError(new File("./resources/Error.txt"));
        try {
            pb.start();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


    }


}
