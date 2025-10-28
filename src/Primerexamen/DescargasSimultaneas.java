class DescargaArchivo extends Thread {
    private String nombreArchivo;

    public DescargaArchivo(String nombreArchivo) {
        this.nombreArchivo = nombreArchivo;
    }

    public void run() {
        System.out.println("Iniciando descarga: " + nombreArchivo);
        try {
            Thread.sleep((int)(Math.random() * 3000)); // simula tiempo de descarga
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("Descarga finalizada: " + nombreArchivo);
    }
}

public class DescargasSimultaneas {
    public static void main(String[] args) throws InterruptedException {
        String[] archivos = {"archivo1.zip", "archivo2.zip", "archivo3.zip"};

        Thread[] hilos = new Thread[archivos.length];
        for (int i = 0; i < archivos.length; i++) {
            hilos[i] = new DescargaArchivo(archivos[i]);
            hilos[i].start();
        }

        for (Thread hilo : hilos) {
            hilo.join(); // espera a que todos terminen
        }

        System.out.println("Todos los archivos descargados.");
    }
}

