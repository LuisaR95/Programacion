package eduthepoweru2programacionmultithread;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class U1P04ContadorVocal {

    private static final Map<Character, Character> VOCALES;

    static {
        VOCALES = new HashMap<>();
        VOCALES.put('a', 'á');
        VOCALES.put('e', 'é');
        VOCALES.put('i', 'í');
        VOCALES.put('o', 'ó');
        VOCALES.put('u', 'ú');
    }

    private void contarVocal(char vocal, String archivo) {
        int contador = 0;

        try (BufferedReader in = new BufferedReader(new FileReader(archivo))) {
            String line;
            while ((line = in.readLine()) != null) {
                line = line.toLowerCase();
                for (int i = 0; i < line.length(); i++) {
                    char c = line.charAt(i);
                    Character acentuada = VOCALES.get(vocal);
                    if (c == vocal || (acentuada != null && c == acentuada)) {
                        contador++;
                    }
                }
            }
        } catch (FileNotFoundException e) {
            System.err.println("Archivo no encontrado: " + archivo);
            throw new RuntimeException(e);
        } catch (IOException e) {
            System.err.println("Error de lectura de archivo: " + archivo);
            throw new RuntimeException(e);
        }

        // Salida estándar: el número, lo captura el proceso principal
        System.out.println(contador);
    }

    public static void main(String[] args) {
        if (args.length < 2) {
            System.out.println("0");
            return;
        }

        U1P04ContadorVocal v = new U1P04ContadorVocal();
        v.contarVocal(args[0].charAt(0), args[1]);
    }
}