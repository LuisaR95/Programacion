package eduthepoweru4serviciosenred;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.atomic.AtomicInteger;

public class U4P003ServidorWebSencillo {

    private static final String HTML_1 = """
            <html>
                <head>
                    <title>Servidor Web Sencillo</title>
                </head>
                <body>
                    <h1>Hola mundo!</h1>
                    <p>Eres el visitante número: """;

    private static final String HTML_2 = """
                    </p>
                </body>
            </html>
            """;

    private static AtomicInteger cont = new AtomicInteger();
    private static final int PUERTO = 8080;

    public static void main(String[] args) {
        try (ServerSocket socket = new ServerSocket(PUERTO)) {
            System.out.println("Escuchando en el puerto: " + PUERTO);

            while (true) {
                Socket sc = socket.accept();
                // Incrementamos el número de visitas en uno
                cont.incrementAndGet();
                Thread t = new Thread(() -> atenderSolicitud(sc));
                t.start();
            }
        } catch (IOException e) {
            System.err.println("Error en el servidor: " + e.getMessage());
        }
    }

    private static void atenderSolicitud(Socket sc) {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(sc.getInputStream()));
             PrintWriter pw = new PrintWriter(sc.getOutputStream())) {

            String line = br.readLine();
            System.out.println(line);

            // Devolviendo HTML
            StringBuilder respuesta = new StringBuilder();
            respuesta.append(HTML_1).append(cont.get()).append(HTML_2);

            String body = respuesta.toString();

            pw.println("HTTP/1.1 200 OK");
            pw.println("Content-Type: text/html;charset=UTF-8");
            pw.println("Content-Length: " + body.getBytes().length);
            pw.println();
            pw.println(body);
            pw.flush();

        } catch (IOException ex) {
            System.err.println("Error: " + ex.getMessage());
        }
    }
}
