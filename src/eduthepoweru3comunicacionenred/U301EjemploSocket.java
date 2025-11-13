package eduthepoweru3comunicacionenred;

import java.io.*;
import java.net.Socket;

public class U301EjemploSocket {
    public static void main(String[] args) {
        try (Socket socket = new Socket("whois.internic.net", 43)){
            //controla lo que envia y lo que recibe
            //2 bytes es un caracter

            OutputStream out = socket.getOutputStream();
            //Objeto que facilita escribir caracteres
            PrintWriter pw = new PrintWriter(out, true);
            //Stream de entrada
            InputStream in = socket.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            //Enviamos el dominio para el que queremos quw whois identifique al propietario
            pw.println("google.com");
            String line = null;
            //Leer informacion mientras el servidor whois
            while ((line = br.readLine()) != null) {
                System.out.println(line);
            }
        } catch (IOException e) {
            System.err.println("Error al acceder al servidor");
        }
    }
}
