package data.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "estadia", schema = "cadastros")
public class Estadia {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(name = "data_inicio")
	private Date inicio;

	@Column(name = "data_fim")
	private Date fim;

	@Column(name = "id_paciente")
	private Long paciente;
	
	@Transient
	private Boolean selecionado;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Date getInicio() {
		return inicio;
	}

	public void setInicio(Date inicio) {
		this.inicio = inicio;
	}

	public Date getFim() {
		return fim;
	}

	public void setFim(Date fim) {
		this.fim = fim;
	}

	public Long getPaciente() {
		return paciente;
	}

	public void setPaciente(Long paciente) {
		this.paciente = paciente;
	}

	public void setSelecionado(Boolean selecionado) {
		this.selecionado = selecionado;
	}

	public Boolean getSelecionado() {
		return selecionado;
	}

}
