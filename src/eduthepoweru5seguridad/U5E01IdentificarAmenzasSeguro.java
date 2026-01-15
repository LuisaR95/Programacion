package eduthepoweru5seguridad;

import java.nio.file.Path;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;


/**

 * OBJETIVO (INSEGURO):

 *  - Simular un "servicio" que recibe una petición por consola.

 *  - Mostrar errores típicos: no valida comando, no limita longitud, no controla rutas,

 *    y registra en logs tal cual la entrada del usuario.

 *

 * CÓMO SE HA DESARROLLADO:

 *  - Se lee una línea y se "interpreta" como: comando + argumento.

 *  - Se imprime y se actúa sin validación (esto sería peligroso si se llamara a Runtime.exec()).

 *

 * IMPORTANTE:

 *  - No ejecutamos comandos reales por seguridad, pero este patrón es el que lleva a inyecciones.

 */

public class U5E01IdentificarAmenzasSeguro {

    private static final int LOGITUD_MAXIMO = 100;
    private static final Set<String> COMANDOS_PERMITIDOS = Set.of("list", "read");
    private static final Path DIRECTORIO_BASE = Path.of("resources").toAbsolutePath();


    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);


        System.out.println("=== CE1 INSEGURO: Servicio de eco (con validación) ===");

        // Problemas al ejecutar cualquier cosa - comandos estricos
        System.out.println("Escribe una petición tipo: list  | read <ruta> )");

        System.out.print("> ");


        String request = sc.nextLine();


        // 1. Evitar ataque DoS limitando longitud de la solicitud recibida
        if (request.length() > LOGITUD_MAXIMO) {
            System.out.println("Longitud superada");
            sc.close();
            System.exit(1);
        }

        //2. evitar log forgin

        System.out.println("[LOG] Request recibida: " + formatear(request));


        String[] parts = request.trim().split("\\s+", 2);

        String cmd = parts.length > 0 ? parts[0].toLowerCase() : "";

        String arg = parts.length == 2 ? parts[1] : "";


        //3. Solo permitimos list y read

        if (!COMANDOS_PERMITIDOS.contains(cmd)) {
            System.out.println("ERROR: comando no permitido");
            sc.close();
            System.exit(1);
        }

        if ("list".equalsIgnoreCase(cmd)) {

            System.out.println("OK: listando recursos..." + DIRECTORIO_BASE + "(Simulando");

        } else if ("read".equalsIgnoreCase(cmd)) {

            // 4. Corregimos el path traversal

            if (arg.isBlank()) {
                System.out.println("ERROR: comando no permitido");


        } else {
            Path ruta = DIRECTORIO_BASE.resolve(arg);
            if (ruta.startsWith(DIRECTORIO_BASE)) {
                System.out.println("Error acceso no permitido");
            }
        }
        System.out.println("OK: leyendo fichero: " + arg + " (simulado)");

    }


        sc.close();

    }

    // evitar logs
    private static String formatear(String request) {
        return  request.replaceAll("\\p{Cntrl}"," ");
    }

}