package eduthepoweru5seguridad;

import java.util.Scanner;
import java.util.regex.Pattern;

public class U5E02ValidacionEntradaDatosSeguro {

    private static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        System.out.println("=== U5E02 SEGURO: Registro con validación ===");

        String username = prompt("Username", 50);
        if (!Validador.validarUserName(username)) {
            System.out.println("Usuario no valido");
            sc.close();
            System.exit(1);
        }

        String age = prompt("Edad", 2);
        if(Validador.validarEdad(age)==0){
            System.out.println("Edad no valida");
            sc.close();
            System.exit(1);
        }

        String email = prompt("Email", 320);
        if (!Validador.validarEmail(email)) {
            System.out.println("Email no valido");
            sc.close();
            System.exit(1);
        }

        String password = prompt("Password", 20);
        if (!Validador.validarPassword(password)) {
            System.out.println("Password no valido");
            sc.close();
            System.exit(1);
        }

        System.out.println("\n[REGISTRO OK - SEGURO]");
        System.out.println("username=" + username);
        System.out.println("edad=" + age);
        System.out.println("email=" + email);

        // No se imprime el password en claro por ser datos sensibles
        //System.out.println("password=" + password);

        sc.close();
    }

    private static String prompt (String texto, int longitud) {
        System.out.println(texto);
        String respuesta = sc.nextLine();

        if (respuesta.length() > longitud) {
            respuesta = respuesta.substring(0, longitud);
        }
        return respuesta;
    }

    static class Validador{
        private static final Pattern USERNAME = Pattern.compile("^[A-Za-z0-9_]{3,50}$");
        private static final Pattern EMAIL = Pattern.compile("^[^\\s@]{2,64}@[A-Za-z0-9.-]{1,68}\\.[A-Za-z]{2,187}$");

        public static boolean validarUserName(String username) {
            return USERNAME.matcher(username).matches();
        }

        public static int validarEdad(String edad) {
            int edadInt = 0;
            try {
                edadInt = Integer.parseInt(edad);
                if (edadInt < 18 || edadInt > 99) {
                    edadInt = 0;
                }
            }catch (NumberFormatException e){
                System.out.println("Edad no valida");
            }
            return edadInt;
        }

        public static boolean validarEmail(String email) {
            return EMAIL.matcher(email).matches();
        }

        public static boolean validarPassword(String password) {
            boolean min = false;
            boolean may = false;
            boolean num = false;
            boolean sign = false;

            if(password.length() < 8){
                return false;
            }
            char[] letras = password.toCharArray();

            for(int i = 0; i < letras.length && !(min && may && num && sign); i++){
                char letra = letras[i];
                if(Character.isLowerCase(letra)){
                    min = true;
                } else if (Character.isUpperCase(letra)) {
                    may = true;
                } else if (Character.isDigit(letra)) {
                    num = true;
                }else {
                    sign = true;
                }
            }
            return min && may && num && sign;
        }
    }
}