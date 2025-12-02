package eduthepoweru2programacionmultithread;

public class   U2P05DeadLockCorregido {
    // Dos objetos que usaremos como bloqueos (locks)
    private static final Object obj1 = new Object();
    private static final Object obj2 = new Object();

    public static void main(String[] args) {
        // Primer hilo que intenta bloquear primero obj1 y luego obj2
        Thread t1 = new Thread(() -> {
            synchronized (obj1) {
                System.out.println("Thread 1: bloqueó obj1");
                try { Thread.sleep(100); } catch (InterruptedException e) {}
                synchronized (obj2) {
                    System.out.println("Thread 1: bloqueó obj2");
                }
            }
        });

        Thread t2 = new Thread(() -> {
            synchronized (obj2) {
                System.out.println("Thread 2: bloqueó obj2");
                try { Thread.sleep(100); } catch (InterruptedException e) {}
                synchronized (obj1) {
                    System.out.println("Thread 2: bloqueó obj1");
                }
            }
        });

        t1.start();
        t2.start();
        //Ejecucion de los thraeds bloquea
    }
}



