package generic.conn;

import generic.persistence.Service;

import javax.persistence.EntityManager;
import javax.persistence.Persistence;

public class GlobalEntityManager {

	private static EntityManager em;

	private GlobalEntityManager() {

	}

	@SuppressWarnings("static-access")
	public void setEm(EntityManager em) {
		this.em = em;
	}

	public static EntityManager getEm(Service service) {

		if (em == null) {
			em = Persistence.createEntityManagerFactory(service.dataBase())
			.createEntityManager();
		} else if (!em.isOpen()){
			em = Persistence.createEntityManagerFactory(service.dataBase())
			.createEntityManager();
		}

		return em;
	}

}
