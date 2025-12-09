package eduthepoweru3comunicacionenred;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketTimeoutException;

public class UP010ClienteLatencia {
    public static void main(String[] args) {
        try (DatagramSocket socket = new DatagramSocket()) {

            // Timeout de 3 segundos para evitar bloqueos eternos
            socket.setSoTimeout(3000);

            String ping = "ping";
            InetAddress inetAddress = InetAddress.getByName("localhost");
            int port = 2300;

            for (int i = 0; i < 100; i++) {
                try {
                    // Preparamos el mensaje "ping"
                    byte[] buf = ping.getBytes();
                    DatagramPacket dp = new DatagramPacket(buf, buf.length, inetAddress, port);

                    long tiempoInicio = System.nanoTime();

                    // Enviamos el ping
                    socket.send(dp);

                    // Preparamos el buffer para la respuesta
                    byte[] buf2 = new byte[1024];
                    DatagramPacket dp2 = new DatagramPacket(buf2, buf2.length);

                    // Esperamos el pong
                    socket.receive(dp2);

                    long tiempoFinal = System.nanoTime();

                    double latenciaMs = (tiempoFinal - tiempoInicio) / 1_000_000.0;

                    System.out.println(
                            "Número de envío: " + (i + 1) +
                                    " | Latencia: " + String.format("%.2f", latenciaMs) + " ms"
                    );

                } catch (SocketTimeoutException e) {
                    System.out.println("Tiempo de espera agotado en el envío " + (i + 1));
                }
            }

        } catch (Exception e) {
            System.err.println("Error en el cliente de latencia: " + e.getMessage());
        }
    }
}
