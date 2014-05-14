package generic.persistence;

import generic.conn.GlobalEntityManager;

import java.lang.reflect.ParameterizedType;
import java.util.Iterator;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Persistence;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Example;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;

public class GenericDao<E> {

	private EntityManager em;

	protected synchronized EntityManager getEm() {
		/*
		 * em = Persistence.createEntityManagerFactory("lar-amigos")
		 * .createEntityManager();
		 * 
		 * if (em == null || !em.isOpen()) { em =
		 * Persistence.createEntityManagerFactory("lar-amigos")
		 * .createEntityManager(); } return em;
		 */
		Service service = (Service) this.getClass()
				.getAnnotation(Service.class);

		/*
		 * if (em == null || !em.isOpen()) { em =
		 * Persistence.createEntityManagerFactory(service.dataBase())
		 * .createEntityManager(); }
		 */

		em = GlobalEntityManager.getEm(service);

		return em;
	}

	protected void setEm(EntityManager em) {
		this.em = em;
	}

	public void excluir(E entidade) {
		try {
			getEm().getTransaction().begin();
			getEm().remove(entidade);
			getEm().getTransaction().commit();
		} catch (Exception exception) {
			exception.printStackTrace();
			getEm().getTransaction().rollback();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see genericcrud.persistence.dao.CrudDao#excluirTodos(java.util.List)
	 */
	@SuppressWarnings("unchecked")
	public void excluirTodos(List<E> itens) {
		for (Iterator iterator = itens.iterator(); iterator.hasNext();) {
			E e = (E) iterator.next();
			excluir(e);
		}
	}

	public E recarregar(E entidade) {
		try {
			getEm().refresh(entidade);
			return entidade;
		} catch (Exception exception) {
			getEm().getTransaction().rollback();
		}
		return entidade;
	}

	public E salvar(E entidade) throws Exception {
		try {
			getEm().getTransaction().begin();
			entidade = getEm().merge(entidade);
			getEm().getTransaction().commit();
			return entidade;
		} catch (Exception exception) {
			exception.printStackTrace();
			//getEm().getTransaction().rollback();
			throw new Exception(exception.getMessage());
		}
	}

	public E editar(E entidade) {
		try {
			getEm().getTransaction().begin();
			getEm().persist(entidade);
			getEm().getTransaction().commit();
			return entidade;
		} catch (Exception exception) {
			exception.printStackTrace();
			getEm().getTransaction().rollback();
		}
		return entidade;
	}

	@SuppressWarnings("unchecked")
	public List<E> listar() {
		try {
			// getEm().getTransaction().begin();
			String query = "Select e From " + getNomeEntidade() + " e";
			return getEm().createQuery(query).getResultList();
		} catch (Exception exception) {
			exception.printStackTrace();
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	private String getNomeEntidade() {
		ParameterizedType type = (ParameterizedType) this.getClass()
				.getGenericSuperclass();
		return ((Class) type.getActualTypeArguments()[0]).getSimpleName();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * genericcrud.persistence.dao.CrudDao#buscarPorExemplo(java.lang.Object)
	 */
	@SuppressWarnings("unchecked")
	public List<E> find(E entidade) {

		// Retorna apenas a consulta sem uso de like em campos de texto, e
		// quando a consulta não tem resultados, não retorna nada.
		Session session = (Session) getEm().getDelegate();
		Criteria criteria = session.createCriteria(entidade.getClass());
		criteria.add(Example.create(entidade));
		List l = criteria.list();
		// getEm().close();
		return l;

	}

	/**
	 * Procura por exemplo, porém quando não tem resultados, retorna todos os
	 * registros.
	 * 
	 * @param entidade
	 * @return list
	 */
	public List<E> procurar(E entidade) {
		Session session = (Session) getEm().getDelegate();
		Criteria criteria = session.createCriteria(entidade.getClass());
		criteria.add(Example.create(entidade).enableLike(MatchMode.ANYWHERE));
		
		return criteria.list();
	}
}
