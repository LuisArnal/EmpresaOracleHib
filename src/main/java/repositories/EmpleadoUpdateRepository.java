package repositories;

import dataBase.EmfSingleton;
import entities.EmpleadosEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;

import java.sql.SQLException;

public class EmpleadoUpdateRepository {
    EntityManagerFactory emf = EmfSingleton.getInstance().getEmf();

    public boolean updateDepartamento(int idEmpleado, int idDepartamento) {
        boolean updateOK = false;

        return updateOK;
    }

    public boolean updateOficio(String nuevoOficio, int idEmpleado) throws SQLException {
        boolean updateOK = false;
        try(EntityManager em = emf.createEntityManager()){
            EntityTransaction tx = em.getTransaction();
            tx.begin();
            //recuperamos el objeto a modificar dentro del contexto de persistencia
            EmpleadosEntity emp = (EmpleadosEntity) em.createQuery("from EmpleadosEntity where empNo = :pId").
                    setParameter("pId", idEmpleado).getSingleResult();
            emp.setOficio(nuevoOficio);
            //cualquier modificación se guarda automáticamente al hacer el commit
            tx.commit();
            updateOK = true;
        }
        return updateOK;
    }
}
