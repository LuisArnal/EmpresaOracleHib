package repositories;

import dataBase.EmfSingleton;
import entities.EmpleadosEntity;
import jakarta.persistence.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EmpleadoRepository {
    EntityManagerFactory emf = EmfSingleton.getInstance().getEmf();
    DepartamentoRepository departamentoRepository = new DepartamentoRepository();
    public ArrayList<EmpleadosEntity> listarEmpleados() throws SQLException{
        //instanciamos los objetos DAO donde almacenar la información que se va a recuperar
        List<EmpleadosEntity> listadoEmpleados = new ArrayList<EmpleadosEntity>();
        try(EntityManager em = emf.createEntityManager()){
            //si no vamos a modificar la base de datos no es necesario usar una transacción
            //creamos la query de jpql para recuperar el listado:
            //el uso de from devuelve objetos completos
            //en jpql no usamos los nombres de las tablas sino de las clases asociadas
            Query listarEmpsquery = em.createQuery("from EmpleadosEntity ");
            listadoEmpleados = listarEmpsquery.getResultList();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return (ArrayList<EmpleadosEntity>) listadoEmpleados;
    }

    //en este caso la comprobación devuelve cero si el usuario no existe, eso lo gestionará el servicio
    public int empleadoByName(String apellido) throws SQLException {
        int idEmpleado = 0;
        try (EntityManager em = emf.createEntityManager()) {
            //en lugar de los campos de las tablas usamos los atributos de las clases
            //las sentencias pueden tener variables que se definen con ":"
            Query depQuery = em.createQuery("select empNo from EmpleadosEntity where apellido = :pEmpNombre");
            depQuery.setParameter("pEmpNombre", apellido);
            idEmpleado = (int) depQuery.getSingleResult();
        }catch (NoResultException e){
            //si la consulta no devuelve ningún valor se genera una excepción
            idEmpleado=0;
        }catch (Exception e) {
            throw new RuntimeException(e);
        }
        return idEmpleado;
    }

    //si tenemos en cuenta que el apellido se puede repetir, empleadoByName debería devolver un array

    public ArrayList<Integer> empleadosByName(String apellido)  {
        List<Integer> idsEmpleados = new ArrayList<>();
        try(EntityManager em = emf.createEntityManager()){
            Query idsEmpQuery = em.createQuery("select empNo from EmpleadosEntity where apellido = :pApellido");
            idsEmpQuery.setParameter("pApellido", apellido);
            idsEmpleados = idsEmpQuery.getResultList();
        }catch (Exception e){
            System.out.println(e.toString());
        }
        return (ArrayList<Integer>) idsEmpleados;
    }

    public String insertEmpleado(EmpleadosEntity nuevoEmpleado)  {
        //en este caso devolvemos un mensaje diciendo si el empleado se ha podido insertar o no
        String mensaje = "";
        try(EntityManager em = emf.createEntityManager()){
            //como vamos a modificar la base de datos iniciamos una transacción
            EntityTransaction tx = em.getTransaction();
            tx.begin();
            //sólo funcionará si se cumplen las restricciones y las etiquetas se ajustan al diseño de la BD
            em.persist(nuevoEmpleado);
            //los cambios se ejecutan sobre la base de datos al hacer el commit.
            tx.commit();
            mensaje = "Empleado " + nuevoEmpleado.getApellido() +" insertado";
        }catch (Exception e){
            mensaje = "Hubo un error. " + e;
        }
        return mensaje;
    }

    public boolean borrarEmpleado(int idEmpleado){
        boolean borrado = false;
        try(EntityManager em = emf.createEntityManager()){
            EntityTransaction tx = em.getTransaction();
            tx.begin();
            //EmpleadosEntity emp = empleadoById(idEmpleado);
            //Es necesario recuperar el objeto desde el contexto en el que se va a borrar
            //No se puede recuperar desde otro contexto y luego hacer un merge.
            EmpleadosEntity emp = (EmpleadosEntity) em.createQuery("from EmpleadosEntity where empNo = :pId").
                    setParameter("pId", idEmpleado).getSingleResult();
            em.remove(emp);
            //al hacer el commit se actualiza en la base de datos.
            tx.commit();
            borrado = true;
        }catch(Exception e){
            System.out.println(e);;
        }
        return borrado;
    }


    public EmpleadosEntity empleadoById(int idEmpleado){
        EmpleadosEntity empById;
        try(EntityManager em = emf.createEntityManager()){
            Query queryEmpById = em.createQuery("from EmpleadosEntity where empNo = :pIdEmp");
            queryEmpById.setParameter("pIdEmp", idEmpleado);
            empById = (EmpleadosEntity) queryEmpById.getSingleResult();
        }catch(NoResultException e){
            empById = null;
        }
        return empById;
    }
    public boolean empIdExiste(int idEmpleado) throws SQLException{
        boolean existe = true;
        try(EntityManager em = emf.createEntityManager()){
            Query idEmpQuery = em.createQuery("select apellido from EmpleadosEntity where empNo = :pIdEmp");
            idEmpQuery.setParameter("pIdEmp", idEmpleado);
            String ape = (String) idEmpQuery.getSingleResult();
        }catch(NoResultException e){
            existe = false;
        }
        return existe;
    }
}
