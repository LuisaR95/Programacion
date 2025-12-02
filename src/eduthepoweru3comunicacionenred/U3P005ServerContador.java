package eduthepoweru3comunicacionenred;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.atomic.AtomicInteger;

public class U3P005ServerContador {

    private static AtomicInteger contador = new AtomicInteger(0);

    static class GestorClientesContador implements Runnable {
        private Socket clientSocket;
        private String cliente;

        private static final String CONTADOR_ACTUALIZADO = "Contador actualizado: ";
        private static final String VALOR_CONTADOR = "Valor: ";

        public GestorClientesContador(Socket clientSocket) {
            this.clientSocket = clientSocket;
        }

        @Override
        public void run() {
            cliente = clientSocket.getInetAddress() + ":" + clientSocket.getPort();
            System.out.println("[" + Thread.currentThread().getName() + "] Conectado: " + cliente);

            try (PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
                 BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()))) {

                String comando;
                out.println("Bienvenido. Comandos disponibles: INC, DEC, GET, BYE");

                while ((comando = in.readLine()) != null) {

                    String processed = comando.trim().toLowerCase();
                    String respuesta = switch (processed) {
                        case "inc" -> "Contador actualizado: Valor: " + incrementarContador();
                        case "dec" -> "Contador actualizado: Valor: " + decrementarContador();
                        case "get" -> "Valor: " + getContador();
                        case "bye" -> "Bye";
                        default -> "Comando no válido. Use INC, DEC, GET o BYE.";
                    };

                    out.println(respuesta);

                    if (processed.equals("bye"))
                        break;
                }

            } catch (IOException e) {
                System.err.println("Error en la conexión: " + e.getMessage());
            } finally {
                try {
                    clientSocket.close();
                } catch (IOException ignored) {}
                System.out.println("Conexión cerrada con " + cliente);
            }
        }   // ← AQUÍ SE CIERRA LA CLASE GESTORCLIENTESCONTADOR
    }

    // Métodos estáticos del contador
    public static int getContador() {
        return contador.get();
    }

    public static int incrementarContador() {
        return contador.incrementAndGet();
    }

    public static int decrementarContador() {
        return contador.decrementAndGet();
    }

    public static void main(String[] args) {

        try (ServerSocket serverSocket = new ServerSocket(4000)) {

            System.out.println("Servidor de Contador iniciado en el puerto 4000");

            while (true) {
                Socket clientSocket = serverSocket.accept();
                Thread thread = new Thread(new GestorClientesContador(clientSocket));
                thread.start();
            }

        } catch (IOException e) {
            System.err.println("Error al iniciar el servidor: " + e.getMessage());
        }
    }
}
