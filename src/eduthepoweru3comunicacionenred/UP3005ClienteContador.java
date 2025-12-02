package eduthepoweru3comunicacionenred;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class UP3005ClienteContador {

    private static final String SERVER_IP = "localhost";
    private static final int PORT = 4000;

    public static void main(String[] args) {

        try (Socket socket = new Socket(SERVER_IP, PORT);
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             Scanner scanner = new Scanner(System.in)) {

            // 🟢 Mostrar mensaje de bienvenida del servidor
            System.out.println(in.readLine());

            String userInput;

            System.out.println("Escriba un comando (INC, DEC, GET, BYE):");

            do {
                System.out.print("> ");
                userInput = scanner.nextLine();

                out.println(userInput);
                System.out.println(in.readLine());

            } while (!userInput.trim().equalsIgnoreCase("bye"));

        } catch (IOException e) {
            System.err.println("Error de conexión: " + e.getMessage());
        }
    }
}
