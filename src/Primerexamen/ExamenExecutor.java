import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ExamenExecutor {
    public static void main(String[] args) {
        ExecutorService pool = Executors.newFixedThreadPool(3);

        for(int i=1; i<=5; i++) {
            int num = i;
            pool.submit(() -> {
                System.out.println("Tarea " + num + " ejecutándose");
                try { Thread.sleep(1000); } catch (InterruptedException e) {}
            });
        }

        pool.shutdown();
    }
}

