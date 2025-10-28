package eduthepoweru2programacionmultithread;

import java.util.concurrent.locks.ReentrantLock;

public class U2P04CondicionCarreraLock {

    private static int contador = 0;
    // Los objetos de tipo reentralock nos permiten bloquear un troxo de codigo hasta que se ejecute
    // El bloque se hace con lock y el debloqueo con unlock y nos garantiza la exclusion mutua
    private static ReentrantLock candado = new ReentrantLock();


    private static  void incrementarContador(int num){

        // meter el unlock en un finally
        candado.lock();
        try {
            contador += num;
        }finally {
            candado.unlock();
        }



    }
    public static int getContador(){
        return contador;
    }
    public static void main(String[] args) {
        final int ITERACIONES = 1_000_000;
        final int VALOR = 10;

        Thread incrementador = new Thread(() -> {
            System.out.println("Iniciando ejecucion incrementar");
            for (int i = 0; i < ITERACIONES; i++){
                incrementarContador(VALOR);
            }
            System.out.println("Acabando ejecucion incrementar");
        });

        Thread decrementar = new Thread(() -> {
            System.out.println("Iniciando ejecucion decrementar");
            for (int i = 0; i < ITERACIONES; i++){
                incrementarContador(-VALOR);
            }
            System.out.println("Acabando ejecucion decrementar");
        });

        incrementador.start();
        decrementar.start();
//Esperar a que terminaran los threads
        try {
            incrementador.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        try {
            decrementar.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        System.out.println("El valor final de contador es: " + getContador());
    }
}


