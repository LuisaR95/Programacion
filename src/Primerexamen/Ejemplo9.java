package Primerexamen;//Calcular la suma de los cuadrados de 1 a 1,000,000 usando un flujo paralelo.

import java.util.stream.IntStream;

public class Ejemplo9 {
    public static void main(String[] args) {
        long inicio = System.currentTimeMillis();

        long suma = IntStream.rangeClosed(1, 1_000_000)
                .parallel() // Paraleliza el stream
                .mapToLong(x -> x * x)
                .sum();

        long fin = System.currentTimeMillis();

        System.out.println("Suma total: " + suma);
        System.out.println("Tiempo: " + (fin - inicio) + " ms");
    }
}

