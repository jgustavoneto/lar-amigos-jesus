package persistence.service;

import generic.persistence.GenericDao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import data.entity.Localidade;
import data.entity.UF;

@generic.persistence.Service(dataBase = "lar-amigos")
@Repository
public class LocalidadeService extends GenericDao<Localidade> {

	public List<Localidade> listar() {
		return super.listar();
	}

	@SuppressWarnings("unchecked")
	public List<Localidade> findByUF(Localidade entidade) {

		Session session = (Session) getEm().getDelegate();
		Criteria criteria = session.createCriteria(Localidade.class, "l");
		criteria.add(Restrictions
				.eq("l.siglaEstado", entidade.getSiglaEstado()));
		List l = criteria.list();

		return l;

	}

	@SuppressWarnings("unchecked")
	public List<Localidade> findByLocalidade(Localidade entidade) {

		Session session = (Session) getEm().getDelegate();
		Criteria criteria = session.createCriteria(entidade.getClass(), "l");
		criteria.add(Restrictions.eq("l.id", entidade.getId()));
		List l = criteria.list();

		return l;

	}

	@SuppressWarnings("unchecked")
	public Localidade get(Localidade entidade) {

		Session session = (Session) getEm().getDelegate();
		Criteria criteria = session.createCriteria(entidade.getClass(), "l");
		criteria.add(Restrictions.eq("l.id", entidade.getId()));
		List<Localidade> l = criteria.list();

		if (!l.isEmpty()) {
			return l.get(0);
		}

		return entidade;

	}

}
