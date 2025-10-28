package eduthepoweru1programacionmultiproceso;

import java.io.IOException;
import java.util.Random;

public class U1P03EjecutarSumador {
    private static final String JAVA = "java";
    private static final String CP = "-cp";
    private static final String CLASSPATH = "C:\\Users\\AlumnoAfternoon\\Documents\\Programacion\\ProgramacionMultiproceso\\out\\production\\PSP";
    private static final String CLASE = "edu.thepower.u1programacionmultiproceso.U1P03Sumador";
    private static final int    NUM_PROCESOS = 5;

    public static void main(String[] args) {

        Random r = new Random();
        for(int i=0; i<NUM_PROCESOS;i++) {


            ProcessBuilder pb = new ProcessBuilder(JAVA, CP, CLASSPATH, CLASE, String.valueOf(r.nextInt(0,100)),String.valueOf(r.nextInt(0,100)));
            try {
                pb.redirectOutput(ProcessBuilder.Redirect.INHERIT);
                pb.redirectError(ProcessBuilder.Redirect.INHERIT);
                pb.start();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        System.out.println("***Finalizado programa principal");
    }
}
