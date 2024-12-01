package menus;

import dto.EmpleadoDTO;
import libs.Leer;
import libs.UserData;
import services.DepartamentoService;
import services.EmpleadoServices;
import services.EmpleadosXMLServices;

import java.nio.file.Path;
import java.util.Scanner;

public class MenuPrincipal {
    private boolean salir = false;
    private Scanner sc = new Scanner(System.in);
    private MenuActualizar menuActualizar = new MenuActualizar();
    private DepartamentoService departamentoService = new DepartamentoService();
    private EmpleadoServices empleadoServices = new EmpleadoServices();
    private EmpleadosXMLServices empleadosXMLServices = new EmpleadosXMLServices();

    public void muestraMenu() {
        String opcion;
        do {
            System.out.println("Elige una opcion:");
            //a partir de aquí los servicios al usuario
            System.out.println("1. Listar departamentos.");
            System.out.println("2. Listar empleados.");
            System.out.println("3. Insertar empleado.");
            System.out.println("4. Borrar empleado.");
            System.out.println("5. Actualizar empleado");
            System.out.println("6. Insertar empleados desde archivo XML");
            System.out.println("0. Salir");
            opcion = this.pideOpcion();
            this.procesaOpcion(opcion);
        } while (!salir);
    }

    private String pideOpcion() {
        return this.sc.nextLine();
    }

    private void procesaOpcion(String opcion) {
        switch (opcion) {
            case "0" -> salir = true;
            case "1" -> {
                for (String dep: departamentoService.listarDepartamentos()) {
                    System.out.println(dep);
                }
            }
            case "2" ->{
                for (EmpleadoDTO emp: empleadoServices.listarEmpleados()){
                    System.out.println(emp.getApellido() + ". Oficio: " + emp.getOficio()
                            + ". Departamento: " + emp.getNombreDep());
                }
            }
            case "3" ->{
                //Definimos una clase empleado diferente que se ajusta a las necesidades de intercambio
                //de información entre la interfaz de usuario y el servicio: DTO (Data Transfer Object)
                //Modelamos la información que el usuario introduce.
                EmpleadoDTO nuevoEmpleado = new EmpleadoDTO();
                nuevoEmpleado.setIdEmpleado(Leer.pedirEntero("Introduce el identificador del empleado:"));
                nuevoEmpleado.setApellido(Leer.pedirCadena("Introduce el apellido del empleado:"));
                nuevoEmpleado.setOficio(Leer.pedirCadena("Introduce el oficio del empleado:"));
                nuevoEmpleado.setApeDir(Leer.pedirCadena("Introduce el apellido de su director:"));
                nuevoEmpleado.setSalario(Leer.pedirDouble("Introduce su salario:"));
                nuevoEmpleado.setComision(Leer.pedirDouble("Introduce su comisión:"));
                nuevoEmpleado.setNombreDep(Leer.pedirCadena(("Introduce el nombre del departamento al que pertenece:")));
                //La interfaz entre el servicio y el usuario envía un DTO y recibe un String
                System.out.println(empleadoServices.insertarEmpleado(nuevoEmpleado));
            }
            case "4" -> {
                String mensaje = empleadoServices.borrarEmpleado(Leer.pedirCadena("Introduce el apellido del empleado:"));
                if (mensaje.startsWith("Existen varios empleados con ese apellido")){
                    //el ususario debe introducir un entero de 4 dígitos
                    int idEmpleado = Leer.pedirEntero(mensaje, "^\\d{4}$");
                    //Comprobar que el usuario introduce uno de los IDs que se proponen
                    String idEmpleadoUser = String.valueOf(idEmpleado);
                    while (!mensaje.contains(idEmpleadoUser)) {
                        idEmpleado= Leer.pedirEntero("Identificador incorrecto, por favor introduzca uno de los identificadores válidos. ", "^[0-9]{4}$");
                        idEmpleadoUser = String.valueOf(idEmpleado);
                    }
                    System.out.println(empleadoServices.borrarEmpleadoById(idEmpleado));
                }
            }
            case "5" ->{
                System.out.println("Introduzca el ID del empleado a actualizar: ");
                for (EmpleadoDTO emp: empleadoServices.listarEmpleados()){
                    System.out.println("ID del empleado: "+emp.getIdEmpleado()+". Apellido: "+emp.getApellido() + ". Oficio: " + emp.getOficio()
                            + ". Departamento: " + emp.getNombreDep());
                }
                //como no nos fiamos del usuario chequeamos que ha introducido correctamente el id
                boolean idOk = false;
                int idEmpleado  = Leer.pedirEntero("");
                while (!idOk) {
                    for (EmpleadoDTO emp: empleadoServices.listarEmpleados()){
                        if (emp.getIdEmpleado() == idEmpleado){idOk = true;}
                    }
                    if (!idOk){
                        idEmpleado = Leer.pedirEntero("Introduzca un id correcto: ");
                    }
                }
                menuActualizar.muestraMenu(idEmpleado);
            }
            case "6" ->{
                Path p = UserData.pedirRutaXML("Introduce la ruta del archivo xml a cargar: ");
                System.out.println(empleadosXMLServices.cargarEmpleados(p));
            }
            default -> System.out.println("Opción incorrecta");
        }
    }
}
