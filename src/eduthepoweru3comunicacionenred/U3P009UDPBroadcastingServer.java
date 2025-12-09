package eduthepoweru3comunicacionenred;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class U3P009UDPBroadcastingServer {

    public static void main(String[] args) {

        try (DatagramSocket ds = new DatagramSocket(8453)) {

            System.out.println("Escuchando en el puerto: " + ds.getLocalPort());

            byte[] buffer = new byte[1024];
            DatagramPacket dtgp = new DatagramPacket(buffer, buffer.length);
            ds.receive(dtgp);
            String mensaje = new String(dtgp.getData(), 0, dtgp.getLength());

            System.out.println("Recibido: " + mensaje);


        } catch (IOException e){
            System.err.println("Error: " + e.getMessage());
        }
    }
}
