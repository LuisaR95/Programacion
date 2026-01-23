package eduthepoweru5seguridad;

import javax.net.ssl.*;
import java.io.*;
import java.net.UnknownHostException;
import java.security.*;
import java.security.cert.CertificateException;

public class U5E07ClienteTLS {


    private static final String TRUST_STORE = "C:\\Users\\AlumnoAfternoon\\Documents\\Programacion\\resources\\cliente-truststore.jks";
    private static final char[] TRUST_STORE_PASSWORD = "changeit".toCharArray();
    private static final int PORT = 8443;
    private static final String HOST = "localhost";

    public static void main(String[] args) {
        try {
            // 1. Accedemos a la TRUSTSTORE
            KeyStore ks = KeyStore.getInstance("PKCS12");
            try (FileInputStream fis = new FileInputStream(TRUST_STORE)){
                ks.load(fis, TRUST_STORE_PASSWORD);
            } catch (CertificateException e) {
                throw new RuntimeException(e);
            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (NoSuchAlgorithmException e) {
                throw new RuntimeException(e);
            }

            // 2. TrustManagerFactory para validar el certificado que nos ha enviado el servidor
            TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            tmf.init(ks);

            // 3. Creamos un contexto SSL
            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, tmf.getTrustManagers(), null);

            // 4. Crear SSLSocketFactory
            SSLSocketFactory ssf = sslContext.getSocketFactory();
            try (SSLSocket sc = (SSLSocket) ssf.createSocket(HOST, PORT);
                 BufferedReader br = new BufferedReader(new InputStreamReader(sc.getInputStream()));
                 PrintWriter pw = new PrintWriter(new OutputStreamWriter(sc.getOutputStream()), true)){
                pw.println("¡Hola!");
                System.out.println(br.readLine());
            }
        } catch (KeyStoreException e) {
            throw new RuntimeException(e);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        } catch (KeyManagementException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}