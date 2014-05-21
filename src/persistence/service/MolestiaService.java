package persistence.service;

import generic.conn.DataSourcePostgress;
import generic.persistence.GenericDao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.faces.model.SelectItem;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Example;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import data.entity.Molestia;

@generic.persistence.Service(dataBase = "lar-amigos")
@Repository
public class MolestiaService extends GenericDao<Molestia> {

	@SuppressWarnings("unchecked")
	public List<Molestia> procurar(Molestia entidade) {
		Session session = (Session) getEm().getDelegate();
		Criteria criteria = session.createCriteria(entidade.getClass());
		criteria.add(Example.create(entidade).enableLike(MatchMode.ANYWHERE));
		criteria.addOrder(Order.asc("nome"));
		return criteria.list();
	}

	@SuppressWarnings("unchecked")
	public List<Molestia> getMolestiaByCategoria(Molestia molestia) {
		Session session = (Session) getEm().getDelegate();
		Criteria criteria = session.createCriteria(Molestia.class, "molestia");
		criteria.add(Restrictions.eq("molestia.categoria", molestia
				.getCategoria()));
		criteria.addOrder(Order.asc("molestia.nome"));
		return criteria.list();
	}

	public Molestia get(Molestia molestia) {
		Session session = (Session) getEm().getDelegate();
		Criteria criteria = session.createCriteria(Molestia.class, "molestia");
		criteria.add(Restrictions.eq("molestia.id", molestia.getId()));
		@SuppressWarnings("unchecked")
		List<Molestia> result = criteria.list();
		if (!result.isEmpty()) {
			return (Molestia) (criteria.list()).get(0);
		}
		return null;
	}
	
	@SuppressWarnings("unchecked")
	public Molestia getById(Molestia molestia) {

		List<Molestia> molestias = new ArrayList();

		Connection conexao = null;
		String sql = "select * from cadastros.molestias m where m.id = " + molestia.getId();

		try {

			conexao = DataSourcePostgress.getConnection();
			PreparedStatement pst = conexao.prepareStatement(sql);

			ResultSet r = pst.executeQuery();

			while (r.next()) {
				Molestia item = new Molestia();
				item.setId(r.getInt("id"));
				item.setDescricao(r.getString("descricao"));
				item.setNome(r.getString("nome"));
				item.setCategoria(r.getInt("id_categoria"));

				molestias.add(item);
			}

		} catch (Exception e) {
		} finally {
			if (conexao != null) {
				try {
					conexao.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		if(molestias.isEmpty()){
			return null;
		}
		return molestias.get(0);
	}

	public List<SelectItem> getByCategoria(Molestia molestia) {
		List<SelectItem> itens = new ArrayList<SelectItem>();
		List<Molestia> lista = getMolestiaByCategoria(molestia);

		for (Molestia m : lista) {
			itens.add(new SelectItem(m.getId(), m.getNome()));
		}

		return itens;
	}

	@SuppressWarnings("unchecked")
	public List<Molestia> getMolestiasPorAcolhido(Long codigoAcolhido) {

		List<Molestia> molestias = new ArrayList();

		Connection conexao = null;
		String sql = "select * from cadastros.molestias_pacientes mp "
				+ "inner join cadastros.molestias m on (mp.id_molestia = m.id) "
				+ "where id_paciente = " + codigoAcolhido;

		try {

			conexao = DataSourcePostgress.getConnection();
			PreparedStatement pst = conexao.prepareStatement(sql);

			ResultSet r = pst.executeQuery();

			while (r.next()) {
				Molestia molestia = new Molestia();
				molestia.setId(r.getInt("id_molestia"));
				molestia.setDescricao(r.getString("descricao"));
				molestia.setNome(r.getString("nome"));
				molestia.setCategoria(r.getInt("id_categoria"));

				molestias.add(molestia);
			}

		} catch (Exception e) {
		} finally {
			if (conexao != null) {
				try {
					conexao.close();
				} catch (SQLException e) {

					e.printStackTrace();
				}
			}
		}
		return molestias;
	}

	public Boolean existeEmBanco(Long codigoAcolhido, Integer codigoMolestia) {

		Integer quantidade = 0;

		Connection conexao = null;
		String sql = "select count(*) as qtd from cadastros.molestias_pacientes mp where id_paciente = "
				+ codigoAcolhido + " and id_molestia = " + codigoMolestia;

		try {

			conexao = DataSourcePostgress.getConnection();
			PreparedStatement pst = conexao.prepareStatement(sql);

			ResultSet r = pst.executeQuery();

			while (r.next()) {
				quantidade = r.getInt("qtd");
			}

		} catch (Exception e) {
		} finally {
			if (conexao != null) {
				try {
					conexao.close();
				} catch (SQLException e) {

					e.printStackTrace();
				}
			}
		}

		if (quantidade > 0) {
			return true;
		}

		return false;
	}
/*
	public Paciente salvar(Paciente obj) throws Exception {
		try {
			getEm().getTransaction().begin();
			entidade = getEm().merge(entidade);
			//getEm().getTransaction().commit();
			return entidade;
		} catch (Exception exception) {
			exception.printStackTrace();
			//getEm().getTransaction().rollback();
			throw new Exception(exception.getMessage());
		}
	}
*/
}
