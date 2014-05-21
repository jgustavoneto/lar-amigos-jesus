package persistence.service;

import generic.conn.DataSourcePostgress;
import generic.persistence.GenericDao;
import generic.utils.DateUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import data.entity.Ponto;

@generic.persistence.Service(dataBase = "lar-amigos")
@Repository
public class FrequenciaService extends GenericDao<Ponto> {

	public void excluir(Ponto objeto) {
		Connection conexao = null;
		String sql = "delete from cadastros.frequencia where id_paciente = "
				+ objeto.getPaciente().getId() + " and data = '"
				+ DateUtils.todayYyyyMMdd(objeto.getData()) + "'";

		try {

			conexao = DataSourcePostgress.getConnection();
			PreparedStatement pst = conexao.prepareStatement(sql);

			pst.execute();

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
	}
	
	@SuppressWarnings("unchecked")
	public List<Ponto> find(Ponto entidade) {

		// Retorna apenas a consulta sem uso de like em campos de texto, e
		// quando a consulta não tem resultados, não retorna nada.
		Session session = (Session) getEm().getDelegate();
		Criteria criteria = session.createCriteria(entidade.getClass(),"d");
		criteria.createAlias("d.paciente", "paciente");
		criteria.add(Restrictions.eq("d.mes", entidade.getMes()));
		criteria.add(Restrictions.eq("d.ano", entidade.getAno()));
		criteria.add(Restrictions.eq("paciente.id", entidade.getPaciente().getId()));
		return criteria.list();

	}

}
