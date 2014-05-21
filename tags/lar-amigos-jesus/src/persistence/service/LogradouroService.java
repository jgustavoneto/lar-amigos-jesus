package persistence.service;

import generic.persistence.GenericDao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import data.entity.Logradouro;

@generic.persistence.Service(dataBase = "lar-amigos")
@Repository
public class LogradouroService extends GenericDao<Logradouro> {

	@SuppressWarnings("unchecked")
	public List<Logradouro> findByCEP(String cep) {

		Session session = (Session) getEm().getDelegate();
		Criteria criteria = session.createCriteria(Logradouro.class, "l");
		criteria.add(Restrictions.eq("l.cep", cep));
		List<Logradouro> l = criteria.list();
		
		return l;

	}

	@SuppressWarnings("unchecked")
	public List<Logradouro> findByLogradouro(String logradouro) {

		Session session = (Session) getEm().getDelegate();
		Criteria criteria = session.createCriteria(Logradouro.class, "l");
		criteria.add(Restrictions.ilike("l.nome", logradouro));
		List<Logradouro> l = criteria.list();
		
		return l;

	}

}
