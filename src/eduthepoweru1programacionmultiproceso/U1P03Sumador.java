package eduthepoweru1programacionmultiproceso;

public class U1P03Sumador {

    // Método privado que suma desde num1 hasta num2, sin importar el orden
    private void sumar(int num1, int num2) {
        int resultado = 0;

        // Intercambiar si num1 es mayor que num2
        if (num1 > num2){
            int aux = num1;
            num1 = num2;
            num2 = aux;
        }

        for (int i = num1; i <= num2; i++) {
            resultado += i;
        }

        System.out.println("La suma de los números entre " + num1 + " y " + num2 + " es: " + resultado);
    }

    public static void main(String[] args) {
       U1P03Sumador sumador = new U1P03Sumador();

        sumador.sumar(Integer.parseInt(args[0]),Integer.parseInt(args[1]));



    }
}