package eduthepoweru2programacionmultithread;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class U2P02ContarVocalesThread implements Runnable {

    private char vocal;
    private String archivo;
    private String salida;

    private static final Map<Character, Character> VOCALES_ACENTUADAS = new HashMap<>();

    static {
        VOCALES_ACENTUADAS.put('a', 'á');
        VOCALES_ACENTUADAS.put('e', 'é');
        VOCALES_ACENTUADAS.put('i', 'í');
        VOCALES_ACENTUADAS.put('o', 'ó');
        VOCALES_ACENTUADAS.put('u', 'ú');
    }

    public U2P02ContarVocalesThread(char vocal, String archivo, String salida) {
        this.vocal = vocal;
        this.archivo = archivo;
        this.salida = salida;
    }

    @Override
    public void run() {
        int contador = 0;
        System.out.println("[" + Thread.currentThread().getName() + "] iniciando contador Vocal '" + vocal);
        Character acentuada = VOCALES_ACENTUADAS.get(vocal);

        try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                linea = linea.toLowerCase();
                for (char c : linea.toCharArray()) {
                    if (c == vocal || (acentuada != null && c == acentuada)) {
                        contador++;
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("❌ Error leyendo archivo: " + e.getMessage());
            return;
        }

        System.out.println("[" + Thread.currentThread().getName() + "] finalizada cuenta vocal '" + vocal + "' encontrada: " + contador + " veces.");

        try (BufferedWriter out = new BufferedWriter(new FileWriter(salida + vocal + ".txt"))) {
            out.write(String.valueOf(contador));
        } catch (IOException e) {
            System.err.println("Error al escribir el archivo " + vocal + ".txt");
        }
    }

    public static void main(String[] args) {
        final String ARCHIVO = "C:\\Users\\AlumnoAfternoon\\Documents\\Programacion\\ProgramacionMultiproceso\\resources\\Vocales.txt";
        final String SALIDA = "./salida/";

        File f = new File("salida");
        if (f.mkdir()) {
            System.out.println("Directorio de salida disponible");
        } else {
            System.out.println("El directorio ya existía. Se borrarán los archivos anteriores...");
            for (File a : f.listFiles()) {
                a.delete();
            }
        }

        List<Thread> hilos = new ArrayList<>();

        for (char v : VOCALES_ACENTUADAS.keySet()) {
            Thread t = new Thread(new U2P02ContarVocalesThread(v, ARCHIVO, SALIDA), "Thread-" + v);
            t.start();
            hilos.add(t);
        }

        // Esperar a que terminen todos los hilos
        for (Thread t : hilos) {
            try {
                t.join();
            } catch (InterruptedException e) {
                System.err.println("Error esperando hilo: " + t.getName());
            }
        }

        // 🔽 Esta parte va fuera del for, cuando TODOS los hilos ya terminaron
        int contadorTotal = 0;

        for (char v : VOCALES_ACENTUADAS.keySet()) {
            try (BufferedReader br = new BufferedReader(new FileReader(SALIDA + v + ".txt"))) {
                String linea = br.readLine();
                int numero = Integer.parseInt(linea.trim());
                contadorTotal += numero;
                System.out.println(v + ": " + numero);
            } catch (NumberFormatException e1) {
                System.err.println("El archivo " + SALIDA + v + ".txt" + " no contiene un número válido: " + e1.getMessage());
            } catch (FileNotFoundException e2) {
                System.err.println("No se encontró el archivo de salida para la vocal '" + v + "'");
            } catch (IOException er) {
                System.err.println("Error de lectura en archivo: " + er.getMessage());
            }
        }

        System.out.println("\nTotal de vocales contadas: " + contadorTotal);
    }
}
