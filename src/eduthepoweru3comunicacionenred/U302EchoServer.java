package eduthepoweru3comunicacionenred;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;

public class U302EchoServer {

    // Formato de fecha con año completo
    private static final SimpleDateFormat SDF = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

    // Método auxiliar para obtener la fecha y hora actual
    public static String getFecha() {
        return SDF.format(new Date());
    }

    public static void main(String[] args) {
        // Hilo demonio que simplemente indica que el servidor está activo
        Thread demonio = new Thread(() -> {
            System.out.println(getFecha() + " Servidor activo\n");
            while (true) {
                try {
                    Thread.sleep(5000);
                    System.out.println(getFecha() + " Servidor sigue activo...");
                } catch (InterruptedException e) {
                    System.err.println(getFecha() + " Hilo demonio interrumpido: " + e.getMessage());
                    break;
                }
            }
        });
        demonio.setDaemon(true);

        // Intentamos abrir el servidor en el puerto 1025
        try (ServerSocket server = new ServerSocket(1025)) {
            demonio.start();
            System.out.println(getFecha() + " Servidor escuchando en el puerto 1025");

            // Esperamos una conexión de cliente
            Socket socket = server.accept();
            System.out.println(getFecha() + " Cliente conectado desde " + socket.getInetAddress());

            // Comunicación con el cliente
            OutputStream out = socket.getOutputStream(); PrintStream printStream = new PrintStream(out);
            InputStream in = socket.getInputStream(); BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in));
            String line = null;
            while ((line = bufferedReader.readLine()) != null){
                System.out.println(getFecha() + " Recibido del cliente: " + line);
                printStream.println(line.toLowerCase()); }
        } catch (IOException e) { System.err.println(getFecha() + "Error al arrancar el servidor " + e.getMessage());
        }
    }
}