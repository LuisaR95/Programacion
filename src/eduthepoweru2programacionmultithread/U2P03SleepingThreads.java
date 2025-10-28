package eduthepoweru2programacionmultithread;

public class U2P03SleepingThreads implements Runnable{

    @Override
    public void run() {

        String nombreThread = Thread.currentThread().getName();

        // dormir un thread
        System.out.println("[" + nombreThread + "] " + "Iniciando ejecución");
        try {
            Thread.sleep(Long.MAX_VALUE);
        } catch (Exception e) {
           System.out.println("[" + nombreThread + "] " + "Despertando uno");
        }

        while (!Thread.interrupted()){}
            System.out.println("[" + nombreThread + "] " + "Despertando dos");
        }
        public static void main(String[] args) {
            Thread t1 = new Thread(new U2P03SleepingThreads(), "SleepingThread");
            t1.start();

            String nombreThread = Thread.currentThread().getName();

            System.out.println("[" + nombreThread + "] " + "Iniciando ejecución");

            for (int i = 0; i < 2; i++){
                System.out.println("[" + nombreThread + "] " + "Sleeping Thread 5 sec");
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                System.out.println("[" + nombreThread + "] " + "Despertando al Thread que esta durmiendo");
                t1.interrupt();
        }
    }
}
