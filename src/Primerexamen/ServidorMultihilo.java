import java.io.*;
import java.net.*;

class ClienteHandler extends Thread {
    private Socket cliente;

    public ClienteHandler(Socket cliente) {
        this.cliente = cliente;
    }

    public void run() {
        try (BufferedReader in = new BufferedReader(new InputStreamReader(cliente.getInputStream()));
             PrintWriter out = new PrintWriter(cliente.getOutputStream(), true)) {

            String mensaje = in.readLine();
            System.out.println("Recibido: " + mensaje);
            out.println("Mensaje recibido: " + mensaje);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

public class ServidorMultihilo {
    public static void main(String[] args) throws IOException {
        ServerSocket server = new ServerSocket(5000);
        System.out.println("Servidor escuchando en puerto 5000...");

        while (true) {
            Socket cliente = server.accept();
            new ClienteHandler(cliente).start();
        }
    }
}

