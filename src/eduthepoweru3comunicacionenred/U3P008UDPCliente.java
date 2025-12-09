package eduthepoweru3comunicacionenred;

import java.io.IOException;
import java.net.*;

public class U3P008UDPCliente {
    public static void main(String[] args) {

        try (DatagramSocket ds = new DatagramSocket()) {

            // OPCIONAL: timeout para evitar bloqueos
            ds.setSoTimeout(3000); // 3 segundos

            String mensaje = "¡Hola Mundo, mañana hará un día soleado!";
            byte[] data = mensaje.getBytes();

            // IP destino
            InetAddress host = InetAddress.getByName("localhost");
            int puerto = 2100;

            DatagramPacket paquete = new DatagramPacket(data, data.length, host, puerto);

            try {
                // Enviar mensaje al servidor
                ds.send(paquete);

                // Recibir ACK del servidor
                byte[] buffer = new byte[1024];
                DatagramPacket paqueteRespuesta = new DatagramPacket(buffer, buffer.length);

                ds.receive(paqueteRespuesta);

                String mensajeRecibido = new String(paqueteRespuesta.getData(),
                        0,
                        paqueteRespuesta.getLength());

                System.out.println("Mensaje recibido: " + mensajeRecibido);

            } catch (SocketTimeoutException e) {
                System.out.println("Socket timeout (no hubo respuesta del servidor): " + e.getMessage());
            }

        } catch (IOException e) {
            System.out.println("Error al intentar comunicarse con el servidor: " + e.getMessage());
        }
    }
}
