package Primerexamen;//Una biblioteca tiene 3 ejemplares de un libro. Los lectores deben esperar si no hay libros disponibles.

import java.util.concurrent.Semaphore;

class Biblioteca {
    private final Semaphore ejemplares;

    public Biblioteca(int cantidadLibros) {
        this.ejemplares = new Semaphore(cantidadLibros);
    }

    public void tomarLibro(String lector) {
        try {
            System.out.println(lector + " intenta tomar un libro...");
            ejemplares.acquire();
            System.out.println(lector + " tomó un libro 📖");
            Thread.sleep((long) (Math.random() * 3000)); // leyendo
            System.out.println(lector + " devolvió el libro.");
            ejemplares.release();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

public class BibliotecaDemo {
    public static void main(String[] args) {
        Biblioteca biblioteca = new Biblioteca(3); // 3 libros disponibles

        for (int i = 1; i <= 6; i++) {
            String lector = "Lector " + i;
            new Thread(() -> biblioteca.tomarLibro(lector)).start();
        }
    }
}
