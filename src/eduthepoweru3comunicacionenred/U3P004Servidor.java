import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class U3P004Servidor {
    private static final int PORT = 12345;
    private static final U3P004Contador sharedContador = new U3P004Contador();

    public static void main(String[] args) {
        System.out.println("Servidor de Contador iniciado en el puerto " + PORT);

        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            while (true) {
                // Esperar y aceptar nuevas conexiones de clientes
                Socket clientSocket = serverSocket.accept();
                System.out.println("Nuevo cliente conectado desde: " + clientSocket.getInetAddress());

                //Este objeto recibe el socket único (para saber con quién hablar)
                // y el contador compartido (para saber qué modificar).
                ClientHandler handler = new ClientHandler(clientSocket, sharedContador);
                new Thread(handler).start();
            }
        } catch (IOException e) {
            System.err.println("Error al iniciar o ejecutar el servidor: " + e.getMessage());
        }
    }

// Declara una clase interna dentro de U3P004Servidor. El uso de private static significa que la clase es
// autocontenida y no necesita una instancia del servidor para existir
//implements Runnable: Esto es crucial. Significa que ClientHandler define una tarea que puede ser ejecutada
// por un hilo de Java (Thread)
    private static class ClientHandler implements Runnable {
        //Es el canal de comunicación exclusivo para este cliente. Cada cliente que se conecta recibe un Socket
        // único de serverSocket.accept(
        private final Socket clientSocket;
        //Es la referencia al recurso compartido. Es la única instancia de la clase U3P004Contador que
        // utilizan todos los hilos del servidor.
        private final U3P004Contador contador;

        public ClientHandler(Socket socket, U3P004Contador contador) {
            this.clientSocket = socket;
            this.contador = contador;
        }

        @Override
        public void run() {
            // Usar try-with-resources para asegurar el cierre automático de los streams y el socket
            try (PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
                 BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                 clientSocket) {

                String inputLine;
                out.println("Bienvenido. Comandos disponibles: INC, DEC, GET, BYE");

                // Bucle de comunicación con el cliente
                while ((inputLine = in.readLine()) != null) {
                    // Procesamiento del comando: quitar espacios y convertir a minúsculas
                    String processedCommand = inputLine.trim().toLowerCase();
                    String response;

                    // Solo necesitamos la primera palabra como comando
                    String command = processedCommand.split("\\s+")[0];

                    // Usamos un switch para procesar el comando
                    response = switch (command) {
                        case "inc" -> "Nuevo valor: " + contador.inc();
                        case "dec" -> "Nuevo valor: " + contador.dec();
                        case "get" -> "Valor actual: " + contador.get();
                        case "bye" -> {
                            // Comando de cierre
                            yield "Adios. Conexión cerrada.";
                        }
                        default -> "Comando no válido. Use INC, DEC, GET o BYE.";
                    };

                    // Enviar la respuesta al cliente
                    out.println(response);

                    // Si el comando fue BYE, salir del bucle
                    if (command.equals("bye")) {
                        break;
                    }
                }

            } catch (IOException e) {
                System.out.println("Cliente desconectado inesperadamente: " + e.getMessage());
            } finally {
                System.out.println("Conexión con " + clientSocket.getInetAddress() + " cerrada.");
            }
        }
    }
}