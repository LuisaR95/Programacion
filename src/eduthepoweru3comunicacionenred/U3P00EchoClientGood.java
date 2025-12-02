

import java.io.*;
import java.net.Socket;

public class U3P00EchoClientGood {
    public static void main(String[] args) {
        try(Socket socket = new Socket("localhost", 4000)){
            InputStream in = socket.getInputStream();
            OutputStream out = socket.getOutputStream();

            BufferedReader bf = new BufferedReader(new InputStreamReader(in));
            PrintWriter pw = new PrintWriter(out, true);

            pw.println("Esto es una prueba");
            System.out.println("Escuchando por el servidor: " + bf.readLine());
        }catch (IOException e){
            System.err.println("Error en la conexión con el servidor: " + e.getMessage());
        }
        System.out.println("Comunicación finalizada");
    }
}