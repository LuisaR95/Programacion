package eduthepoweru3comunicacionenred;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class UP008UDPServer {
    public static void main(String[] args) {
        try (DatagramSocket socket = new DatagramSocket()) {

            String ping = "ping";
            byte[] buf = ping.getBytes(); // Datos reales a enviar

            InetAddress inetAddress = InetAddress.getByName("localhost");
            int port = 2300;

            // Paquete de envío (se puede reutilizar en el bucle)
            DatagramPacket dp = new DatagramPacket(buf, buf.length, inetAddress, port);

            for (int i = 0; i < 100; i++) {

                long tiempoInicio = System.nanoTime();

                // Enviar ping
                socket.send(dp);

                // Preparar recepción
                byte[] buf2 = new byte[1024];
                DatagramPacket dp2 = new DatagramPacket(buf2, buf2.length);

                // Recibir pong
                socket.receive(dp2);

                long tiempoFinal = System.nanoTime();

                System.out.println("Numero de envio: " + (i + 1) +
                        " latencia: " + String.format("%.2f",
                        (tiempoFinal - tiempoInicio) / 1_000_000.0) + " ms");
            }

        } catch (Exception e) {
            System.err.println("Error en el cliente de latencia: " + e.getMessage());
        }
    }
}
