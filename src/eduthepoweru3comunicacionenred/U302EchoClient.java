package eduthepoweru3comunicacionenred;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class U302EchoClient {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        try ( Socket socket = new Socket("localhost", 1025)) {
            OutputStream out = socket.getOutputStream();
            PrintWriter pw = new PrintWriter(out, true);
            InputStream in = socket.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            String entrada;
            do {
                entrada = sc.nextLine();
                pw.println(entrada);
                String line = null;

                    System.out.println("Recibido del servicor" + br.readLine());

            }   while(!entrada.equals("0"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
