package dataBase;

import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

public class EmfSingleton {
    //instancia del singleton de la factoría
    private static EmfSingleton emfSInstancia =
            new EmfSingleton();
    //unidad de persistencia donde están las entidades
    static private final String PERSISTENCE_UNIT_NAME = "default";
    //la factoría se define como privada
    private EntityManagerFactory emf = null;
    //método que devuelve la instancia del singleton que permite acceder a la factoría
    public static EmfSingleton getInstance() {
        return emfSInstancia;
    }

    private EmfSingleton() {
    }
    //los entity manager se crearán a partir de la factoría que devuelve este método
    public EntityManagerFactory getEmf() {
        //la creación de la factoría sólo se realiza la primera vez que se llama al método
        if (this.emf == null)
            this.emf = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);
        return this.emf;
    }
}
