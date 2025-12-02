package eduthepoweru3comunicacionenred;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

class Validacion {

    public static int validarPuerto(String[] args){

        if(args.length != 1){
            throw  new IllegalArgumentException("Uso: java U3P00EchoServer <puerto>");
        }

        int puerto;
        // 1. Manejo de la posible NumberFormatException (si args[0] no es un entero)
        try {
            puerto = Integer.parseInt(args[0]);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("El puerto proporcionado (" + args[0] + ") no es un número entero válido.");
        }

        // 2. Validar que el puerto esté en el rango
        if(puerto < 1024 || puerto > 65535){
            throw  new IllegalArgumentException("El puerto debe ser entre 1024 y 65535.");
        }

        return puerto;
    }
}

public class U3P00EchoServer {
    public static void main(String[] args) {
        int puerto = 0;
        try{
            puerto = Validacion.validarPuerto(args);
        }catch (Exception e){
            System.err.println("Error en el puerto: " + e.getMessage());
            System.exit(1);
        }


        // 3. El segundo bloque try-catch solo debe ejecutarse si la validación fue exitosa (puerto > 0 o != -1).
        // En este caso, si la validación falla, System.exit(1) detiene el programa, por lo que podemos continuar.
        try (ServerSocket servidor = new ServerSocket(puerto)) {
            System.out.println("Servidor esperando conexiones en el puerto: " + puerto);
            Socket socket = servidor.accept();
            System.out.println("Cliente conectado: " + socket.getInetAddress() + ":" + socket.getPort());
            InputStream in = socket.getInputStream();
            BufferedReader bf = new BufferedReader(new InputStreamReader(in));
            // cuando de control nos lo de
            PrintWriter pw = new PrintWriter(socket.getOutputStream(), true);
            Scanner sc = new Scanner(System.in);
            String msg;
            do {
                msg = sc.nextLine().trim();
                pw.println(msg);
            } while(!msg.equalsIgnoreCase("/salir"));
            String entrada;
            //Leer informacion mientras el servidor whois
            while ((entrada= bf.readLine()) != null) {
                System.out.println("Recibido de cliente: " + entrada);
                pw.println(entrada.toLowerCase().trim());
                System.out.println("Devuelto por el srvidor");
            }
            // Implementa aquí la lógica del servidor (while(true), accept(), etc.)
        } catch (IOException e) {
            System.err.println("Error al iniciar el servidor en el puerto " + puerto + ".");
            System.err.println("Causa: El puerto podría estar en uso.");
        }
    }
}