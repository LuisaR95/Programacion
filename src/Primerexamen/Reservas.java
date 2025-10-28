package Primerexamen;

import java.util.concurrent.locks.*;

class Vuelo {
    private int asientosDisponibles;
    private final Lock lock = new ReentrantLock();

    public Vuelo(int total) {
        this.asientosDisponibles = total;
    }

    public void reservar(String pasajero) {
        lock.lock();
        try {
            if (asientosDisponibles > 0) {
                System.out.println(pasajero + " reservando asiento...");
                Thread.sleep(300);
                asientosDisponibles--;
                System.out.println(pasajero + " confirmó su reserva. Asientos restantes: " + asientosDisponibles);
            } else {
                System.out.println(pasajero + " no pudo reservar: vuelo lleno ❌");
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }
}

public class Reservas {
    public static void main(String[] args) {
        Vuelo vuelo = new Vuelo(4);

        for (int i = 1; i <= 8; i++) {
            String nombre = "Pasajero " + i;
            new Thread(() -> vuelo.reservar(nombre)).start();
        }
    }
}
