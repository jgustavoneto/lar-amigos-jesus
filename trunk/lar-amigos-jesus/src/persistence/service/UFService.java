package persistence.service;

import generic.persistence.GenericDao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import data.entity.UF;

@generic.persistence.Service(dataBase = "lar-amigos")
@Repository
public class UFService extends GenericDao<UF> {

	public List<UF> listar() {
		return super.listar();
	}

	@SuppressWarnings("unchecked")
	public List<UF> find(UF entidade) {

		Session session = (Session) getEm().getDelegate();
		Criteria criteria = session.createCriteria(entidade.getClass(), "uf");
		criteria.add(Restrictions.eq("uf.id", entidade.getId()));
		return criteria.list();

	}
	
	public UF get(UF entidade) {

		Session session = (Session) getEm().getDelegate();
		Criteria criteria = session.createCriteria(UF.class, "uf");
		criteria.add(Restrictions.eq("uf.id", entidade.getId()));
		
		List l = criteria.list();
		if (!l.isEmpty()) {
			return (UF)l.get(0);
		}
		
		return null;

	}
	
	@SuppressWarnings("unchecked")
	public List<UF> findBySigla(UF entidade) {

		Session session = (Session) getEm().getDelegate();
		Criteria criteria = session.createCriteria(entidade.getClass(), "uf");
		criteria.add(Restrictions.ilike("uf.sigla", entidade.getSigla()));
		return criteria.list();

	}

	public UF getBySigla(UF entidade) {

		List<UF> result = findBySigla(entidade);

		if (!result.isEmpty()) {
			return result.get(0);
		}

		return new UF();

	}

}
