package eduthepoweru2programacionmultithread;

import java.io.File;
import java.io.IOException;

public class U1P01EjecutarProceso {

    public static void main(String[] args) {
        U1P01EjecutarProceso p = new U1P01EjecutarProceso();
        p.launcher("C:\\Program Files\\FileZilla FTP Client\\filezilla.exe");
    }

    public void launcher(String programa){
        File file = new File(programa);
        if (!file.exists()) {
            System.err.println("El archivo no existe: " + programa);
            return;
        }

        ProcessBuilder pb = new ProcessBuilder(programa);
        try {
            pb.start();
        } catch (IOException e) {
            System.err.println("Error al iniciar el proceso: " + programa);
            e.printStackTrace();
        }
    }
}

