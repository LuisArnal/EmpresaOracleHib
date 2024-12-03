package menus;

import jdk.swing.interop.SwingInterOpUtils;
import libs.Leer;
import services.EmpleadoUpdateServices;

import java.util.Scanner;

public class MenuActualizar {
    private boolean salir = false;
    private Scanner sc = new Scanner(System.in);

    private EmpleadoUpdateServices empUpServices = new EmpleadoUpdateServices();
    public void muestraMenu(int idEmpleado){
        String opcion;
        do {
            System.out.println("Elige una opcion:");
            System.out.println("1. Actualizar el oficio.");
            System.out.println("2. Actualizar el salario.");
            System.out.println("3. Actualizar la comisión.");
            System.out.println("4. Actualizar el director.");
            System.out.println("5. Actualizar el departamento.");
            System.out.println("6. Volver al menú principal");
            opcion = this.pideOpcion();
            this.procesaOpcion(opcion, idEmpleado);
        } while (!salir);
    }

    private String pideOpcion() {
        return this.sc.nextLine();
    }

    private void procesaOpcion(String opcion, int idEmpleado) {
        switch (opcion) {
            case "0" -> salir = true;
            case "1" -> {
                String nuevoOficio = Leer.pedirCadena("Introduce el nuevo oficio.");
                System.out.println(empUpServices.updateOficio(nuevoOficio, idEmpleado));
            }
            case "6" -> {
                MenuPrincipal menuPrincipal = new MenuPrincipal();
                menuPrincipal.muestraMenu();
            }
            default -> System.out.println("Opcion invalida");
        }
    }
}
