package eduthepoweru2programacionmultithread;

// Implementación de Runnable
class ThreadImplements implements Runnable {
    @Override
    public void run() {
        System.out.println("🧵 [Implements] Nombre del thread: " + Thread.currentThread().getName());
        System.out.println("🧵 [Implements] ID del thread: " + Thread.currentThread().threadId());
    }
}

// Herencia de Thread
class ThreadExtends extends Thread {
    @Override
    public void run() {
        System.out.println("🧵 [Extends] Nombre del thread: " + Thread.currentThread().getName());
        System.out.println("🧵 [Extends] ID del thread: " + Thread.currentThread().threadId());
    }
}

public class U2P01CreacionThread {

    public static void main(String[] args) {
        // 1. Usando lambda
        Thread t1 = new Thread(() -> {
            System.out.println("🧵 [Lambda] Nombre del thread: " + Thread.currentThread().getName());
            System.out.println("🧵 [Lambda] ID del thread: " + Thread.currentThread().threadId());
        }, "Thread Lambda");

        // 2. Usando herencia
        Thread t2 = new ThreadExtends();
        t2.setName("Thread Extends");

        // 3. Usando implementación Runnable en un bucle
        for (int i = 0; i < 5; i++) {
            Thread t = new Thread(new ThreadImplements(), "Thread Implements " + i);
            t.start();
        }

        // Lanzar t1 y t2
        t1.start();
        t2.start();
    }
}
