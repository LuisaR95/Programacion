package eduthepoweru1programacionmultiproceso;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class U1P04EjecutarContadorVocal {

    private static final String[] VOCALES = {"a", "e", "i", "o", "u"};
    private static final String JAVA = "java";
    private static final String CP = "-cp";
    private static final String CLASSPATH = "C:\\Users\\AlumnoAfternoon\\Documents\\Programacion\\ProgramacionMultiproceso\\out\\production\\PSP";
    private static final String CLASE = "edu.thepower.u1.programacionmultiproceso.U1P04ContadorVocal"; // ← CORREGIDO
    private static final String ARCHIVO = "C:\\Users\\AlumnoAfternoon\\Documents\\Programacion\\ProgramacionMultiproceso\\resources\\Vocales.txt";
    private static final String SALIDA = "./salida/";
    private static final String EXTENSIONTXT = ".txt";

    public static void main(String[] args) {
        List<Process> procesos = new ArrayList<>();

        File f = new File(SALIDA);
        if (f.exists() || f.mkdir()) {
            System.out.println("Directorio de salida disponible");
        } else {
            System.err.println("Error al crear el directorio de salida");
            return;
        }

        // 1ª parte: lanzar los procesos
        for (String vocal : VOCALES) {
            ProcessBuilder pb = new ProcessBuilder(JAVA, CP, CLASSPATH, CLASE, vocal, ARCHIVO);

            pb.redirectOutput(new File(SALIDA + vocal + EXTENSIONTXT));
            pb.redirectError(ProcessBuilder.Redirect.INHERIT);

            try {
                procesos.add(pb.start());
            } catch (IOException e) {
                System.err.println("Error al iniciar proceso para vocal '" + vocal + "': " + e.getMessage());
            }
        }

        System.out.println("Procesos lanzados correctamente. Esperando finalización...");

        for (Process proceso : procesos) {
            try {
                //metodo para acabar el proceso
                proceso.waitFor();
            } catch (InterruptedException e) {
                throw new RuntimeException("Error esperando proceso: " + e.getMessage());
            }
        }

        System.out.println("Todos los procesos han finalizado.");

        // 2ª parte: leer resultados
        int contadorTotal = 0;

        for (String vocal : VOCALES) {
            try (BufferedReader br = new BufferedReader(new FileReader(SALIDA + vocal + EXTENSIONTXT))) {
                String linea = br.readLine();
                int numero = Integer.parseInt(linea.trim());
                contadorTotal += numero;
                System.out.println(vocal + ": " + numero);
            } catch (NumberFormatException e) {
                System.err.println("El archivo " + SALIDA + vocal + EXTENSIONTXT + " no contiene un número válido: " + e.getMessage());
            } catch (FileNotFoundException e) {
                System.err.println("No se encontró el archivo de salida para la vocal '" + vocal + "'");
            } catch (IOException e) {
                System.err.println("Error de lectura en archivo: " + e.getMessage());
            }
        }

        System.out.println("\nTotal de vocales contadas: " + contadorTotal);
    }
}