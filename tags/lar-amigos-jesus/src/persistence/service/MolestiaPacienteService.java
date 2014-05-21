package persistence.service;

import generic.persistence.GenericDao;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import data.entity.Molestia;
import data.entity.MolestiaPaciente;

@generic.persistence.Service(dataBase = "lar-amigos")
@Repository
public class MolestiaPacienteService extends GenericDao<MolestiaPaciente>{
	
	@SuppressWarnings("unchecked")
	public List<Molestia> find(Integer codigoPaciente) {

		// Retorna apenas a consulta sem uso de like em campos de texto, e
		// quando a consulta não tem resultados, não retorna nada.
		Session session = (Session) getEm().getDelegate();
		Criteria criteria = session.createCriteria(MolestiaPaciente.class,"mp");
		criteria.add(Restrictions.eq("mp.codigoPaciente", codigoPaciente));
		
		List<MolestiaPaciente> lista = criteria.list();
		List<Molestia> result = new ArrayList<Molestia>();
		
		for (MolestiaPaciente object : lista) {
			result.add(object.getMolestia());
		}
		
		return result;

	}
	
	@SuppressWarnings("unchecked")
	public MolestiaPaciente get(MolestiaPaciente molestiaPaciente) {
		Session session = (Session) getEm().getDelegate();
		Criteria criteria = session.createCriteria(MolestiaPaciente.class, "molestiaPaciente");
		criteria.add(Restrictions.eq("molestiaPaciente.codigoPaciente", molestiaPaciente.getCodigoPaciente()));
		criteria.add(Restrictions.eq("molestiaPaciente.molestia.id", molestiaPaciente.getMolestia().getId()));
		List<MolestiaPaciente> result = criteria.list();
		if (!result.isEmpty()) {
			return (MolestiaPaciente) (criteria.list()).get(0);
		}
		return null;
	}

}
