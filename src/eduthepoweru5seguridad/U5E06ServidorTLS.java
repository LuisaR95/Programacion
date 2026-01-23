package eduthepoweru5seguridad;

import javax.net.ssl.*;
import javax.swing.*;
import java.io.*;
import java.security.*;
import java.security.cert.CertificateException;

public class U5E06ServidorTLS {
    private static final String NOMBRE_ALMACEN = "C:\\Users\\AlumnoAfternoon\\Documents\\Programacion\\resources\\servidor.jks";
    private static final char[] PASS_ALMACEN = "changeit".toCharArray();
    private static final char[] PASS_CLAVE = "changeit".toCharArray();

    public static void main(String[] args) {
        //1. Servidor accede a la keystore

        try {
            KeyStore ks = KeyStore.getInstance("PKCS12");
            try (FileInputStream fis = new FileInputStream(NOMBRE_ALMACEN)) {
                ks.load(fis,PASS_ALMACEN);
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (CertificateException e) {
                throw new RuntimeException(e);
            } catch (NoSuchAlgorithmException e) {
                throw new RuntimeException(e);
            }

            //2.El protocolo accede al almacen
            KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
            kmf.init(ks, PASS_ALMACEN);

            //3. Creamos un contexto SSL
            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(kmf.getKeyManagers(),null,null);

            //4. Creamos una SSL ServerSocket Factory
            SSLServerSocketFactory sslServerSocketFactory = sslContext.getServerSocketFactory();
            try(SSLServerSocket serverSocket = (SSLServerSocket) sslServerSocketFactory.createServerSocket(8443)){
                System.out.println("Iniciando servidor y esperando conexiones en puerto 8443. ");
                try (SSLSocket socket = (SSLSocket) serverSocket.accept();
                     BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                     PrintWriter pw = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()) , true);
                ){
                    String linea = br.readLine();
                    pw.println("Devuelto por el servidor: "+ linea.toUpperCase());
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        } catch (KeyStoreException e) {
            throw new RuntimeException(e);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        } catch (UnrecoverableKeyException e) {
            throw new RuntimeException(e);
        } catch (KeyManagementException e) {
            throw new RuntimeException(e);
        }

    }
}