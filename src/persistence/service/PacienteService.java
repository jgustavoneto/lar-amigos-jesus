package persistence.service;


import generic.persistence.GenericDao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Example;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import data.entity.Paciente;

@generic.persistence.Service(dataBase = "lar-amigos")
@Repository
public class PacienteService extends GenericDao<Paciente> {

	private AcompanhanteService acompanhanteService;

	@Override
	public Paciente salvar(Paciente paciente) throws Exception {
		/**
		paciente = super.salvar(paciente);
		*/
		paciente.setMolestias(null);
		paciente = super.salvar(paciente);
		
		return paciente;
	}
	
	public Paciente alterar(Paciente entidade) {
		/*Acompanhante acompanhante = entidade.getAcompanhante();
		Paciente paciente = super.salvar(entidade);
		paciente.setAcompanhante(acompanhante);
		paciente.getAcompanhante().setPaciente(paciente.getId());
		paciente.setAcompanhante(acompanhanteService.editar(paciente
				.getAcompanhante()));*/
		return null;
	}

	@Autowired
	public void setAcompanhanteService(AcompanhanteService acompanhanteService) {
		this.acompanhanteService = acompanhanteService;
	}

	public AcompanhanteService getAcompanhanteService() {
		return acompanhanteService;
	}
	
	@SuppressWarnings("unchecked")
	public List<Paciente> procurar(Paciente entidade) {
		Session session = (Session) getEm().getDelegate();
		Criteria criteria = session.createCriteria(entidade.getClass());
		criteria.addOrder(Order.asc("nome"));
		criteria.add(Example.create(entidade).enableLike(MatchMode.ANYWHERE));
		return criteria.list();
	}
	
	@SuppressWarnings("unchecked")
	public Boolean jaExisteMatricula(Integer matricula) {
		Session session = (Session) getEm().getDelegate();
		Criteria criteria = session.createCriteria(Paciente.class,"paciente");
		criteria.add(Restrictions.eq("paciente.matricula", matricula));
		List<Paciente> result = criteria.list();
		
		if(!result.isEmpty()){
			return true;
		}else{
			return false;
		}
		
	}
	
	public Boolean jaExisteNome(Paciente acolhido) {
		Session session = (Session) getEm().getDelegate();
		Criteria criteria = session.createCriteria(Paciente.class,"paciente");
		criteria.add(Restrictions.eq("paciente.nome", (acolhido.getNome()+"").trim().toUpperCase()));
		criteria.add(Restrictions.eq("paciente.mae", (acolhido.getMae()+"").trim().toUpperCase()));
		List<Paciente> result = criteria.list();
		
		if(!result.isEmpty()){
			return true;
		}else{
			return false;
		}
		
	}
	
	
}
