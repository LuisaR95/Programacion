package eduthepoweru2programacionmultithread;

public class U2P04CondicionDeCarrera {

        private static int contador = 0;

        // bloqueo synchronized -> bloquea la clase
        private static  synchronized void incrementarContador(int num){
            contador += num;
        }
        public static synchronized int getContador(){
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
