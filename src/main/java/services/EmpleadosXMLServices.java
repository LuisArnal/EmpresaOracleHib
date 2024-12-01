package services;

import dto.EmpleadoXML;
import dto.EmpleadosXML;
import entities.EmpleadosEntity;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Unmarshaller;
import repositories.DepartamentoRepository;
import repositories.EmpleadoRepository;

import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;

public class EmpleadosXMLServices {
    EmpleadoRepository empleadoRepository = new EmpleadoRepository();
    DepartamentoRepository departamentoRepository = new DepartamentoRepository();
    public String cargarEmpleados(Path ruta){
        StringBuilder mensaje = new StringBuilder();
        if (Files.isReadable(ruta)){
            EmpleadosXML empleadosXML;
            ArrayList<EmpleadosEntity> empleadosCarga = new ArrayList<EmpleadosEntity>();
            EmpleadosEntity empleadoDAO = new EmpleadosEntity();
            JAXBContext jaxbContext = null;
            try{
                jaxbContext = JAXBContext.newInstance(EmpleadosXML.class);
                Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
                empleadosXML = (EmpleadosXML) unmarshaller.unmarshal(ruta.toFile());
                //transformamos de XML a DAO para poder hacer las inserciones en la BD con el repositorio
                for (EmpleadoXML emp: empleadosXML.getEmpleados()){
                    empleadoDAO = new EmpleadosEntity();
                    empleadoDAO.setEmpNo(emp.getIdEmpleado());
                    empleadoDAO.setApellido(emp.getApellido());
                    empleadoDAO.setOficio(emp.getOficio());
                    empleadoDAO.setFechaAlt(Date.valueOf(LocalDate.now()));
                    empleadoDAO.setSalario(emp.getSalario());
                    empleadoDAO.setComision(emp.getComision());
                    empleadoDAO.setDepartamentosByDeptNo(departamentoRepository.depById(emp.getIdDepto()));
                    empleadosCarga.add(empleadoDAO);
                }
                //comprobamos si cumplen las restricciones y sólo insertamos los que las cumplen
                for (EmpleadosEntity emp: empleadosCarga){
                    if (empleadoRepository.empIdExiste(emp.getEmpNo())){
                        mensaje.append("El empleado no se insertará porque ya existe un empleado con ese id");
                    }
                }
            } catch (JAXBException e) {
                mensaje = new StringBuilder("El archivo XML no tiene la estructura adecuada");
            } catch (SQLException e) {
                mensaje = new StringBuilder("Error SQL: " + e);
            }
        }
        return mensaje.toString();
    }
}
