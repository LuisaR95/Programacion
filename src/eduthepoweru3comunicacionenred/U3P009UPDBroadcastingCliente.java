package eduthepoweru3comunicacionenred;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class U3P009UPDBroadcastingCliente {

    public static void main(String[] args) {

        try (DatagramSocket ds = new DatagramSocket()) {

            String mensaje = "Mensaje de Broadcasting";
            byte[] data = mensaje.getBytes();

            // Dirección de broadcast válida para 10.0.0.0/8
            InetAddress host = InetAddress.getByName("10.255.255.255");

            DatagramPacket packet = new DatagramPacket(data, data.length, host, 8453);

            ds.setBroadcast(true);

            ds.send(packet);

            System.out.println("Broadcast enviado");

        } catch (IOException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }
}
