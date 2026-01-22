package eduthepoweru5seguridad;

import java.security.KeyStore;
import java.security.KeyStoreException;
import java.util.logging.Logger;

public class U5E05CifradoAsimetricoRSAServidor {
    private static final Logger LOG = Logger.getLogger(U5E05CifradoAsimetricoRSAServidor.class.getName());
    private static String ALMACEN_CLAVES = "C:\\Users\\AlumnoAfternoon\\Documents\\Programacion\\resourses\\servidor.jks";
    private static String ARCHIVO_CIFRADO = "salida.bin";
    private static char[] STORE_PASS = "changeit".toCharArray();
    private static String KEY_ALIAS = "servisor";
    private static char[] KEY_PASS = "changeit".toCharArray();

    public static void main(String[] args) {
        // 1. Accreder al almacen de claves (servidor.jks)
        KeyStore ks = KeyStore.getInstance("JKS");
        try ()

        } catch (KeyStoreException e) {
            throw new RuntimeException(e);
        }
    }
}
