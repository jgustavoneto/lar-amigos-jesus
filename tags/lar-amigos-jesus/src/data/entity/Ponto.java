package data.entity;

import java.util.Calendar;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "frequencia", schema = "cadastros")
public class Ponto {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Transient
	private Integer dia;

	@Transient
	private String descricao;

	private Date data;

	private Integer mes;

	private Integer ano;

	@OneToOne(cascade = { CascadeType.PERSIST, CascadeType.REMOVE })
	@JoinColumn(name = "id_paciente")
	private Paciente paciente = new Paciente();

	@Transient
	private Boolean esteMes = false;

	@Transient
	private Boolean hoje = false;

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getId() {
		return id;
	}

	public void setData(Date data) {
		this.data = data;
	}

	public Date getData() {
		return data;
	}

	public void setMes(Integer mes) {
		this.mes = mes;
	}

	public Integer getMes() {
		return mes;
	}

	public void setAno(Integer ano) {
		this.ano = ano;
	}

	public Integer getAno() {
		return ano;
	}

	public void setPaciente(Paciente paciente) {
		this.paciente = paciente;
	}

	public Paciente getPaciente() {
		return paciente;
	}

	public void setDia(Integer dia) {
		this.dia = dia;
	}

	public Integer getDia() {
		return dia;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setEsteMes(Boolean esteMes) {
		this.esteMes = esteMes;
	}

	public Boolean getEsteMes() {
		return esteMes;
	}

	public void setHoje(Boolean hoje) {
		this.hoje = hoje;
	}

	public Boolean getHoje() {
		return hoje;
	}

	@Transient
	private String descricaoMesCorrente;
	
	
	/**
	 * Obtém a descrição do mês atual se não tiver nada informado no field mes.
	 * 
	 * @return
	 */
	public String getDescricaoMesCorrente() {

		int esteMes = 1;

		if (getMes() != null & getMes() != 0) {
			esteMes = getMes();
		} else {
			Calendar c = Calendar.getInstance();
			esteMes = c.get(Calendar.MONTH);
		}

		if (esteMes == 1)
			return "Janeiro";
		if (esteMes == 2)
			return "Fevereiro";
		if (esteMes == 3)
			return "Março";
		if (esteMes == 4)
			return "Abril";
		if (esteMes == 5)
			return "Maio";
		if (esteMes == 6)
			return "Junho";
		if (esteMes == 7)
			return "Julho";
		if (esteMes == 8)
			return "Agosto";
		if (esteMes == 9)
			return "Setembro";
		if (esteMes == 10)
			return "Outubro";
		if (esteMes == 11)
			return "Novembro";
		if (esteMes == 12)
			return "Dezembro";

		return descricaoMesCorrente;
	}

	public void setDescricaoMesCorrente(String descricaoMesCorrente) {
		this.descricaoMesCorrente = descricaoMesCorrente;
	}

	@Transient
	private Boolean mostrar;
	
	public Boolean getMostrar(){
		
		if(getDia()==null & getEsteMes()){
			return true;
		}
		
		return false;
	}
	
}
