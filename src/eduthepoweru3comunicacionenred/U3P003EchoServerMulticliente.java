package eduthepoweru3comunicacionenred;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Clase de utilidad para validar el puerto pasado como argumento.
 */

class GestorCliente implements Runnable {

    // Se mantiene como final para asegurar inmutabilidad y fácil acceso
    private final Socket socket;

    // Constructor para recibir el Socket del cliente al crear el hilo
    public GestorCliente(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {

        System.out.println("Hilo de servicio iniciado para: " + socket.getInetAddress() + ":" + socket.getPort());

        // Usamos try-with-resources para asegurar que todos los flujos y el Socket se cierren al finalizar.
        try (
                // Flujo de entrada para leer lo que envía el cliente
                BufferedReader bf = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                // Flujo de salida para enviar la respuesta (autoFlush=true)
                PrintWriter pw = new PrintWriter(socket.getOutputStream(), true);
                // Esto garantiza que el Socket se cierre correctamente
                Socket clientSocket = this.socket
        ) {
            String entrada;

            // Bucle de comunicación: lee la entrada del cliente hasta que la conexión se cierre (readLine() devuelve null)
            while ((entrada = bf.readLine()) != null) {
                System.out.println("Recibido de cliente (" + clientSocket.getPort() + "): " + entrada);

                // Lógica del servidor de ECO: devuelve el mensaje en minúsculas
                pw.println(entrada.toLowerCase().trim());
            }

        } catch (IOException e) {
            // Este error ocurre si la conexión se interrumpe de forma inesperada (ej. el cliente se cierra forzosamente)
            System.err.println("Error de E/S con el cliente " + socket.getInetAddress() + ": " + e.getMessage());
        } finally {
            System.out.println("Cliente desconectado: " + socket.getInetAddress() + ":" + socket.getPort());
        }
    }
}

/**
 * Clase principal que inicializa el ServerSocket y lanza un nuevo hilo (GestorCliente)
 * por cada conexión de cliente.
 */
class U3P00EchoServerMulticliente {

    public static void main(String[] args) {
        int puerto = 0;

        // 1. Validación de Puerto
        try {
            puerto = Validacion.validarPuerto(args);
        } catch (IllegalArgumentException e) {
            System.err.println("Error en el puerto: " + e.getMessage());
            System.exit(1);
        }

        // 2. Bloque Principal del Servidor
        try (ServerSocket servidor = new ServerSocket(puerto)) {
            System.out.println("Servidor Echo multihilo esperando conexiones en el puerto: " + puerto);

            // Bucle infinito (while(true)) para aceptar clientes continuamente (esencial para un servidor)
            while (true) {
                System.out.println("Esperando nueva conexión...");

                // Bloquea la ejecución hasta que llega un nuevo cliente
                Socket socket = servidor.accept();

                // 3. Crear un nuevo hilo para gestionar la comunicación con este cliente
                GestorCliente gestor = new GestorCliente(socket);
                Thread hiloCliente = new Thread(gestor);
                hiloCliente.start(); // Inicia la ejecución del hilo
            }
        } catch (IOException e) {
            // Este error solo ocurre si el ServerSocket no se puede abrir (puerto en uso o permisos).
            System.err.println("Error fatal al iniciar el servidor en el puerto " + puerto + ".");
            System.err.println("Causa: El puerto podría estar en uso o permiso denegado.");
            System.exit(1);
        }
    }
}