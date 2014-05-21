package persistence.service;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Example;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.springframework.stereotype.Repository;

import generic.persistence.GenericDao;
import data.entity.UnidadeEncaminhamento;

@generic.persistence.Service(dataBase = "lar-amigos")
@Repository
public class EncaminhamentoService extends GenericDao<UnidadeEncaminhamento>{
	
	@SuppressWarnings("unchecked")
	public List<UnidadeEncaminhamento> procurar(UnidadeEncaminhamento entidade) {
		Session session = (Session) getEm().getDelegate();
		Criteria criteria = session.createCriteria(entidade.getClass());
		criteria.add(Example.create(entidade).enableLike(MatchMode.ANYWHERE));
		criteria.addOrder(Order.asc("nome"));
		return criteria.list();
	}

}
