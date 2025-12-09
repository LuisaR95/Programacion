package eduthepoweru4serviciosenred;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class U4P002SelfHealingServer {
    public static void main(String[] args) {
        while(true){
            try {
                arrancarServidor();
            } catch (Exception e) {
                System.err.println("Servidor fuera de servicio");
                System.err.println("Reiniciando en 2 segundos");
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException ex) {

                }
            }
        }
    }
    private static void arrancarServidor() {
        try (ServerSocket socket = new ServerSocket(2777)) {
            System.out.println("Escuchando en el puerto 27777");
            //Thread asesino - cierra a 5 segundos el server
            Thread killer = new Thread(() -> {
                System.out.println("Thread killer iniciandose");
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {

                }
                try {
                    socket.close();
                } catch (IOException e) {

                }
            });
            killer.start();
            while (true) {
                Socket sc = socket.accept();
                 Thread t = new Thread(() -> {
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
                 t.start();
            }
        } catch (IOException e){
            throw  new RuntimeException(e.getMessage());
        }
    }
}
