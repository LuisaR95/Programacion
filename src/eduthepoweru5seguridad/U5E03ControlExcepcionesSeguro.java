package eduthepoweru5seguridad;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class U5E03ControlExcepcionesSeguro {

    private static final Logger LOG = Logger.getLogger(U5E03ControlExcepcionesSeguro.class.getName());
    static Scanner sc;

    public static void main(String[] args) {
        sc = new Scanner(System.in);

        System.out.println("=== U5E03 SEGURO: Gestión de Errores ===");
        System.out.println("1) Leer fichero config.txt");
        System.out.println("2) Parsear JSON (muy simple)");
        System.out.println("3) Conectar a BD (simulada)");
        System.out.print("> ");

        int option = 0;

        try {
            String input = sc.nextLine().trim();
            option = Integer.parseInt(input);

            // Llamamos una sola vez y guardamos el resultado
            String resultado = procesarOpcion(option);
            System.out.println("Resultado: " + resultado);

        } catch (NumberFormatException e) {
            System.err.println("Opción no válida: debe ser un número.");

        } catch (IllegalArgumentException e) {
            System.err.println("ERROR [ERR_INPUT] solicitud con opcion " + option + ": " + e.getMessage());
            LOG.log(Level.WARNING, "Error en la solicitud " + option + ": " + e.getMessage());

        } catch (IOException e) {
            System.err.println("ERROR [ERR_ES]: No se pudo acceder al recurso solicitado.");
            LOG.log(Level.WARNING, "Error técnico en opción " + option + ": " + e.getMessage());
        } catch (RuntimeException e) {
            System.err.println("ERROR [ERR_UNKNOWN]: error desconocido" );
            LOG.log(Level.SEVERE, "Error inesperado en la ejecución " + option + ": " + e.getMessage(),e);
        } finally {
            sc.close();
        }
    }

    private static String readConfigInsecure(String filename) throws IOException {
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line = br.readLine();
            return (line != null) ? line : "";
        }
    }

    private static String procesarOpcion(int option) throws IOException {
        String respuesta = "";

        if (option == 1) {
            System.out.println("Leyendo config.txt...");
            String content = readConfigInsecure("config.txt");
            if (content.isBlank()) {
                respuesta = "Archivo de configuración vacío";
            } else {
                respuesta = "Configuración cargada: " + content;
            }

        } else if (option == 2) {
            System.out.print("JSON: ");
            String json = sc.nextLine();

            if (!json.trim().startsWith("{")) {
                throw new IllegalArgumentException("JSON inválido: debe empezar con '{'");
            }
            respuesta = "JSON validado correctamente";

        } else if (option == 3) {
            throw new IOException("Fallo de red");

        } else {
            throw new IllegalArgumentException("Opción fuera de rango (1-3)");
        }

        return respuesta;
    }
}
