import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class U3P004Cliente {
    private static final String SERVER_IP = "localhost"; // Dirección local del servidor
    private static final int PORT = 12345;

    public static void main(String[] args) {
        System.out.println("Intentando conectar al servidor en " + SERVER_IP + ":" + PORT);

        // Crea y abre el socket para conectarse al servidor.
        try (Socket socket = new Socket(SERVER_IP, PORT);
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             Scanner scanner = new Scanner(System.in)) {

            System.out.println("Conexión establecida. Escriba un comando (INC, DEC, GET, BYE):");

            // Recibir el mensaje de bienvenida del servidor
            System.out.println("Servidor: " + in.readLine());

            String userInput;
            String serverResponse;

            // Bucle principal para leer comandos del usuario
            while (true) {
                System.out.print("> ");
                userInput = scanner.nextLine();

                if (userInput.isBlank()) {
                    continue; // Ignorar líneas vacías
                }

                // Enviar el comando al servidor
                out.println(userInput);

                // Leer la respuesta del servidor
                if ((serverResponse = in.readLine()) != null) {
                    System.out.println("Servidor: " + serverResponse);

                    // Si el servidor confirma el cierre, salir
                    if (serverResponse.contains("Adios")) {
                        break;
                    }
                } else {
                    System.out.println("Conexión con el servidor perdida.");
                    break;
                }
            }

        } catch (IOException e) {
            System.err.println("Error de conexión: Asegúrese de que el servidor esté corriendo. Mensaje: " + e.getMessage());
        }
    }
}