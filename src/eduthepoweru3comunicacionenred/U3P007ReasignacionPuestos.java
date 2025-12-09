package eduthepoweru3comunicacionenred;

import java.util.*;

public class U3P007ReasignacionPuestos {

    private static final String[] nombres = {
            "Genesisis", "Pablo", "Luisa", "Alejandro", "Sergio G.",
            "Mario", "Astrid", "Esteban", "Victor", "Claudia",
            "Sergio M", "Marcos", "David", "Sebastian", "Aaron", "Johan"
    };

    private static final int MAX_ALUMNOS = nombres.length;

    private static final List<String> alumnos = new ArrayList<>();
    private static final List<Integer> puestos = new ArrayList<>();
    private static final Map<Integer, String> asignaciones = new TreeMap<>();

    public static void main(String[] args) {

        for (int i = 0; i < MAX_ALUMNOS; i++) {
            alumnos.add(nombres[i]);
            puestos.add(i + 1);
        }

        System.out.println("Reasignando puestos...");
        Collections.shuffle(alumnos);
        Collections.shuffle(puestos);

        System.out.println("Resultado:\n");

        Scanner sc = new Scanner(System.in);

        for (int i = 0; i < MAX_ALUMNOS; i++) {
            System.out.print("El puesto para " + alumnos.get(i) +
                    " es... (Pulsa ENTER): ");
            sc.nextLine();

            System.out.println(puestos.get(i) + "\n");
            asignaciones.put(puestos.get(i), alumnos.get(i));
        }

        System.out.println("Sorteo finalizado. Resultado: \n");
        for (Map.Entry<Integer, String> entry : asignaciones.entrySet()) {
            System.out.println(entry.getKey() + " - " + entry.getValue());
        }
    }
}
