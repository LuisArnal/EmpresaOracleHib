package repositories;

import dataBase.EmfSingleton;
import entities.DepartamentosEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.NoResultException;
import jakarta.persistence.Query;

import java.util.ArrayList;
import java.util.List;

//en la arquitectura de capas con patrón DAO (Data Access Object)
//en los repositorios accedemos al información persistida y la almacenamos en memoria
//haciendo uso de las clases de Java que la modelan en este caso de las clases del paquete DAO
public class DepartamentoRepository {
    EntityManagerFactory emf = EmfSingleton.getInstance().getEmf();
    //ConnectionBD conBD = new ConnectionBD();

    public ArrayList<DepartamentosEntity> listarDepartamentos(){
        //instanciamos los objetos DAO donde almacenar la información que se va a recuperar
        List<DepartamentosEntity> listadoDepartamentos = new ArrayList<>();
        //conectamos con la base de datos asegurando la desconexión con el try with resources
        try(EntityManager em = emf.createEntityManager()){
            //creamos la query de jpql para recuperar el listado:
            Query listarDepsquery = em.createQuery("from DepartamentosEntity ");
            listadoDepartamentos = listarDepsquery.getResultList();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return (ArrayList<DepartamentosEntity>) listadoDepartamentos;
    }

    public DepartamentosEntity depById(int id){
        //Entidad a devolver
        DepartamentosEntity departamentoById = new DepartamentosEntity();
        //conectamos con la base de datos asegurando la desconexión con el try with resources
        try(EntityManager em = emf.createEntityManager()){
            Query depQuery = em.createQuery("from DepartamentosEntity where deptNo = :pDepId");
            depQuery.setParameter("pDepId", id);
            departamentoById = (DepartamentosEntity) depQuery.getSingleResult();

        }catch (NoResultException e){
            departamentoById = null;
        }catch (Exception e) {
            throw new RuntimeException(e);
        }
        return departamentoById;
    }

    public boolean nombreDepExiste(String nombre){
        boolean existe = false;
        if (depByName(nombre)!=null){
            existe = true;
        }
        return existe;
    }
    public DepartamentosEntity depByName(String nombre){
        DepartamentosEntity dep = new DepartamentosEntity();
        try(EntityManager em = emf.createEntityManager()){
            Query depQuery = em.createQuery("from DepartamentosEntity where dnombre = :pDepNombre");
            depQuery.setParameter("pDepNombre", nombre);
            dep = (DepartamentosEntity) depQuery.getSingleResult();
        }catch (NoResultException e){
            dep = null;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return dep;
    }
}
