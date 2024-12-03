package services;

import dto.EmpleadoDTO;

import entities.EmpleadosEntity;
import repositories.DepartamentoRepository;
import repositories.EmpleadoRepository;

import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;

public class EmpleadoServices {
    private EmpleadoRepository empleadoRepository = new EmpleadoRepository();
    private DepartamentoRepository departamentoRepository = new DepartamentoRepository();
    //para listar empleados recuperamos un listado de DAOs
    //y generamos un array de Strings, que es lo que requiere la interfaz del usuario en este caso.
    //los objetos a transmitir al usuario (DTO: Data Transfer Object) son strings
    public ArrayList<EmpleadoDTO> listarEmpleados()  {
        ArrayList<EmpleadoDTO> empleadosDTO = new ArrayList<EmpleadoDTO>();
        ArrayList<EmpleadosEntity> empleadosEntities;
        try {
            empleadosEntities = empleadoRepository.listarEmpleados();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        EmpleadoDTO empAux;
        //aquí hemos seleccionado mostrar esa información al usuario, pero podría ser otra
        //según definamos nuestros DTOs
        for (EmpleadosEntity empleado : empleadosEntities){
            empAux = new EmpleadoDTO();
            empAux.setApellido(empleado.getApellido());
            empAux.setIdEmpleado(empleado.getEmpNo());
            empAux.setOficio(empleado.getOficio());
            empAux.setNombreDep(empleado.getDepartamentosByDeptNo().getDnombre());
            empleadosDTO.add(empAux);
        }
        return empleadosDTO;
    }

    public String insertarEmpleado(EmpleadoDTO nuevoEmp){
        //devolvemos un mensaje
        String mensaje="";
        //si se cumplen las restricciones
        boolean restriccionOK = true;
        //llamamos al repositorio pasándole un DAO
        EmpleadosEntity empleadoEntity = new EmpleadosEntity();
        //comprueba si se va a poder hacer la inserción antes de llamar al repositorio
        //las restricciones de la base de datos deben cumplirse
        //comprobamos que el director existe
        try {
            int idDirector = empleadoRepository.empleadoByName(nuevoEmp.getApeDir());
            if (idDirector==0){
                restriccionOK = false;
                mensaje ="El empleado director no existe.\n";
            }
            //comprobamos que el departamento existe
            if (!departamentoRepository.nombreDepExiste(nuevoEmp.getNombreDep())){
                mensaje += "El departamento no existe.\n";
                restriccionOK = false;
            }
            //si se cumplen las restricciones de la base de datos y de la lógica de negocio (en este caso
            //departamento y director no pueden ser null)
            //generamos el DAO a enviar al repositorio para que pueda generar la consulta de inserción
            if (restriccionOK){
                empleadoEntity.setEmpNo(nuevoEmp.getIdEmpleado());
                empleadoEntity.setApellido(nuevoEmp.getApellido());
                empleadoEntity.setOficio(nuevoEmp.getOficio());
                empleadoEntity.setDir(idDirector);
                empleadoEntity.setFechaAlt(Date.valueOf(LocalDate.now()));
                empleadoEntity.setSalario(nuevoEmp.getSalario());
                empleadoEntity.setComision(nuevoEmp.getComision());
                empleadoEntity.setDepartamentosByDeptNo(departamentoRepository.depByName(nuevoEmp.getNombreDep()));
                mensaje = empleadoRepository.insertEmpleado(empleadoEntity);
            }
        } catch (SQLException e) {
            mensaje ="Error SQL: "+ e;
        }
        return mensaje;
    }

    public String borrarEmpleado(String apellidoEmpleado){
        String mensaje="El empleado no se ha podido borrar. ";

        ArrayList<Integer> idsEmpleados = empleadoRepository.empleadosByName(apellidoEmpleado);
        if (idsEmpleados.isEmpty()){
            mensaje ="El empleado no existe.\n";
        } else if (idsEmpleados.size()==1) {
            if (empleadoRepository.borrarEmpleado(idsEmpleados.getFirst())){
                mensaje ="El empleado se ha borrado.";
            }
        }else {
            mensaje = "Existen varios empleados con ese apellido. Elija el empleado a borrar: \n";
            for (Integer i:idsEmpleados) {
                mensaje += i.toString() + "\n";
            }
        }
        return mensaje;
    }
    public String borrarEmpleadoById(int idEmpleado){
        String mensaje="El empleado no se ha borrado. ";
        if(empleadoRepository.borrarEmpleado(idEmpleado)){
            mensaje = "Empleado "+ idEmpleado +" borrado.";
        }
        return mensaje;
    }
}
