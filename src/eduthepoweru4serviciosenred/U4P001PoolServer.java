package eduthepoweru4serviciosenred;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class U4P001PoolServer {
    public static void main(String[] args) {
        ExecutorService pool = Executors.newFixedThreadPool(1);

        try (ServerSocket socket = new ServerSocket(2777)) {
            System.out.println("Escuchando en el puerto 27777");
            while (true) {
                Socket sc = socket.accept();
                pool.submit(() -> {
                    try (BufferedReader br = new BufferedReader(new InputStreamReader(sc.getInputStream()));
                         PrintWriter pw = new PrintWriter(sc.getOutputStream(), true)) {
                        String line = null;
                        while ((line = br.readLine()) != null) {
                            System.out.println("Recibido de cliente: " + line);
                            pw.println(line.toLowerCase());
                        }
                    } catch (IOException ex) {
                        System.err.println("Error" + ex.getMessage());
                    }

                });
            }
        } catch (IOException e){
            System.err.println("Error en el servidor" + e.getMessage());
        }
    }

}
