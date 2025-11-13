package PracticaExamen;

class Libro {
    private final String titulo;
    public Libro(String titulo) { this.titulo = titulo; }
    public String getTitulo() { return titulo; }
}

class Estudiante implements Runnable {
    private final String nombre;
    private final Libro libro1;
    private final Libro libro2;

    public Estudiante(String nombre, Libro libro1, Libro libro2) {
        this.nombre = nombre;
        this.libro1 = libro1;
        this.libro2 = libro2;
    }

    @Override
    public void run() {
        Libro primero = libro1.hashCode() < libro2.hashCode() ? libro1 : libro2;
        Libro segundo = libro1.hashCode() < libro2.hashCode() ? libro2 : libro1;

        synchronized (primero) {
            System.out.println("📖 " + nombre + " tomó " + primero.getTitulo());
            try { Thread.sleep(100); } catch (InterruptedException ignored) {}
            synchronized (segundo) {
                System.out.println("✅ " + nombre + " está leyendo ambos libros.");
            }
        }
    }
}

public class U2P16BibliotecaHashCode {
    public static void main(String[] args) {
        Libro l1 = new Libro("Matemáticas");
        Libro l2 = new Libro("Física");

        Thread e1 = new Thread(new Estudiante("Ana", l1, l2));
        Thread e2 = new Thread(new Estudiante("Luis", l2, l1));

        e1.start();
        e2.start();
    }
}
