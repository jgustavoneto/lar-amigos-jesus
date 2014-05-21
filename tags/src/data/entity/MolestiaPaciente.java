package data.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "molestias_pacientes", schema = "cadastros")
public class MolestiaPaciente {
	
	@Id
	@GeneratedValue (strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@ManyToOne (fetch = FetchType.LAZY)
	@JoinColumn (name = "id_molestia")
	private Molestia molestia = new Molestia();
	
	@Column (name = "id_paciente")
	private Long codigoPaciente;

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getId() {
		return id;
	}

	public void setMolestia(Molestia molestia) {
		this.molestia = molestia;
	}

	public Molestia getMolestia() {
		return molestia;
	}

	public void setCodigoPaciente(Long codigoPaciente) {
		this.codigoPaciente = codigoPaciente;
	}

	public Long getCodigoPaciente() {
		return codigoPaciente;
	}
	
}
