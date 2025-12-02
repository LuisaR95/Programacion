
public class U3P004Contador {
    // volatile asegura que el hilo que lee el contador siempre vea el valor más reciente
    private volatile int valor = 0;

// Synchronized Garantiza que, si múltiples clientes llaman a inc() casi
// al mismo tiempo, solo uno de ellos podrá ejecutar el código dentro de inc() a la vez.
    public synchronized int inc() {
        return ++valor;
    }


    public synchronized int dec() {
        return --valor;
    }


    public synchronized int get() {
        return valor;
    }
}