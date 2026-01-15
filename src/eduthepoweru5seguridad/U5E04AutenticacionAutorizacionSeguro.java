package edu.thepower.u5seguridad;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;

public class U5E04AutenticacionAutorizacionSeguro {

    enum Rol { USER, ADMIN }

    static class User {
        private static final Random RANDOM = new Random();

        String username;
        int passwordValue;
        int salt;
        Rol role;

        public User(String username, String password, Rol role) {
            this.username = username;
            this.role = role;
            this.salt = getSalt();
            this.passwordValue = getPasswordValue(password, salt);
        }

        private static int getSalt() {
            return RANDOM.nextInt(1000);
        }

        private static int getPasswordValue(String password, int salt) {
            return (password + salt).hashCode();
        }

        private boolean checkPassword(String password) {
            return passwordValue == getPasswordValue(password, salt);
        }
    }

    // clase inner
    static class Sesion {
        String username;
        Rol role;

        public Sesion(String username, Rol role) {
            this.username = username;
            this.role = role;
        }
    }

    public static class AutenticacionYValidacion {

        private final String CREDENCIALES_INCORRECTAS = "Credenciales incorrectas";
        Map<String, User> users;

        public AutenticacionYValidacion(Map<String, User> users) {
            this.users = users;
        }

        public Sesion login(String username, String password) {
            Sesion sesion = null;
            User user = users.get(username);

            if (user != null && user.checkPassword(password)) {
                sesion = new Sesion(username, user.role);
            } else {
                System.out.println(CREDENCIALES_INCORRECTAS);
            }

            return sesion;
        }

        public boolean validarPermisos(Sesion sesion, Rol rolRequerido) {
            boolean permitido = false;

            if (sesion != null) {
                if (sesion.role == Rol.ADMIN) {
                    permitido = true;
                } else if (sesion.role == rolRequerido) {
                    permitido = true;
                }
            }

            return permitido;
        }
    }

    public static void main(String[] args) {

        Map<String, User> users = new HashMap<>();
        users.put("admin", new User("admin", "Admin123!", Rol.ADMIN));
        users.put("ana", new User("ana", "Ana123!!aa", Rol.USER));

        AutenticacionYValidacion autenticacion = new AutenticacionYValidacion(users);
        Scanner sc = new Scanner(System.in);

        Sesion sesion = null;

        System.out.println("=== CE4 SEGURO: Login + Roles ===");

        while (sesion == null) {
            System.out.print("Usuario: ");
            String u = sc.nextLine();

            System.out.print("Password: ");
            String p = sc.nextLine();

            sesion = autenticacion.login(u, p);
        }

        System.out.println("Login OK. Rol=" + sesion.role);

        System.out.println("1) Ver perfil");
        System.out.println("2) Ver lista de usuarios (ADMIN)");
        System.out.println("3) Apagar servicio (ADMIN)");
        System.out.print("> ");

        try {
            int opt = Integer.parseInt(sc.nextLine());

            if (opt == 1) {
                System.out.println("Perfil de " + sesion.username + " (rol=" + sesion.role + ")");

            } else if (opt == 2) {

                if (autenticacion.validarPermisos(sesion, Rol.ADMIN)) {
                    System.out.println("Usuarios: " + users.keySet());
                } else {
                    System.err.println("No tienes permisos");
                }

            } else if (opt == 3) {

                if (autenticacion.validarPermisos(sesion, Rol.ADMIN)) {
                    System.out.println("Servicio apagado (simulado).");
                } else {
                    System.err.println("No tienes permisos");
                }

            } else {
                System.out.println("Opción inválida.");
            }

        } catch (NumberFormatException e) {
            System.err.println("Opción numérica inválida");
        }

        sc.close();
    }
}
