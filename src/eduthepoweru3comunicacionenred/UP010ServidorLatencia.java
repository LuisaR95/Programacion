package eduthepoweru3comunicacionenred;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class UP010ServidorLatencia {
    public static void main(String[] args) {
        try (DatagramSocket ds = new DatagramSocket(2300)) {
            System.out.println("Iniciando servidor");
            byte[] buf = new byte[1024];
            DatagramPacket dp;
            while (true) {
                dp = new DatagramPacket(buf, buf.length);
                ds.receive(dp);
                String mensaje = new String(dp.getData(), 0, dp.getLength());
                System.out.println("Recibido: " + mensaje);
                String respuesta = "Pong";
                byte[] buf2 = respuesta.getBytes();
                InetAddress ip = dp.getAddress();
                int port = dp.getPort();
                DatagramPacket dp2 = new DatagramPacket(buf2, buf2.length, ip, port);
                ds.send(dp2);
            }
        } catch (IOException e){
            System.out.println("Error: " + e.getMessage());

        }
    }
}
