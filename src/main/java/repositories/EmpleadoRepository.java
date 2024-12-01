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
            //creamos la query de jpql para recuperar el listado:
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
            Query depQuery = em.createQuery("select empNo from EmpleadosEntity where apellido = :pEmpNombre");
            depQuery.setParameter("pEmpNombre", apellido);
            idEmpleado = (int) depQuery.getSingleResult();
        }catch (NoResultException e){
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
            EntityTransaction tx = em.getTransaction();
            tx.begin();
            em.persist(nuevoEmpleado);
            tx.commit();
            mensaje = "Empleado " + nuevoEmpleado.getApellido() +" insertado";
        }catch (Exception e){
            mensaje = "Hubo un error. " + e;
        }
        return mensaje;
    }

    //en este caso propagamos el error para gestionar la excepción en el servicio y poder enviar el mensaje desde allí
    public boolean borrarEmpleado(int idEmpleado) throws SQLException {
        boolean borrado = false;

        return borrado;
    }

    public boolean empIdExiste(int idEmpleado) throws SQLException{
        boolean existe = true;
        try(EntityManager em = emf.createEntityManager()){
            Query idEmpQuery = em.createQuery("select apellido from EmpleadosEntity where empNo = :pIdEmp");
            idEmpQuery.setParameter("pIdEmp", idEmpleado);
            String ape = String.valueOf(idEmpQuery.getFirstResult());
        }catch(NoResultException e){
            existe = false;
        }
        return existe;
    }
}
