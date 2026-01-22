package eduthepoweru5seguridad;


import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.util.logging.Logger;

public class U5E05CifradoAsimetrioRSA {
    //ir mostrando las trazas por consola
    private static final Logger LOG = Logger.getLogger(U5E05CifradoAsimetrioRSA.class.getName());
    private static final String NOMBRE_CERTIFICADO = "C:\\Users\\AlumnoAfternoon\\Documents\\Programacion\\resourses\\servidor.crt";
    private static final String ARCHIVO_CIFRADO = "salida.bin";

    public static void main(String[] args) {
        try {
            // paso numero 1: accerder al certificado que tenemos guardado en servidor.crt
            CertificateFactory cf = CertificateFactory.getInstance("X.509");
            Certificate cert;
            try(FileInputStream fis = new FileInputStream(NOMBRE_CERTIFICADO)) {
                cert = cf.generateCertificate(fis);
            }
            LOG.info("Se ha accedido al certificado");

            // paso 2: extraer clave pública del certificado
            PublicKey pk = cert.getPublicKey();
            LOG.info("Recuperada clave pública");

            // paso tres: Creación del mensaje a cifrar
            String text = "Lo que querais, escribid lo que os de la gana";
            byte[] textoPlano = text.getBytes();

            // paso 4: cifrado del mensaje
            Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            cipher.init(Cipher.ENCRYPT_MODE, pk);
            byte[] textoCifrado = cipher.doFinal(textoPlano);
            LOG.info("Mensaje cifrado");

            // Paso 5: guardamos en disco el mensaje cifrado
            try (FileOutputStream fos = new FileOutputStream(ARCHIVO_CIFRADO)) {
                fos.write(textoCifrado);
            }
            LOG.info("Mensaje cifrado y almacenado");
            System.out.println("El mensaje ha sido cifrado y almacenado en el archivo " + ARCHIVO_CIFRADO);

        } catch (CertificateException e) {
            throw new RuntimeException(e);
        }catch(IOException e){
            throw new RuntimeException(e);
        } catch (InvalidKeyException e) {
            throw new RuntimeException(e);
        } catch (NoSuchPaddingException e) {
            throw new RuntimeException(e);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        } catch (IllegalBlockSizeException e) {
            throw new RuntimeException(e);
        } catch (BadPaddingException e) {
            throw new RuntimeException(e);
        }
    }

}
