package data.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table (name = "molestias", schema = "cadastros")
public class Molestia {
	
	@Transient
	private Boolean noPersisted;
	
	@Id
	@GeneratedValue (strategy = GenerationType.IDENTITY)
	private Integer id;
	
	private String sigla;
	
	private String nome;
	
	private String descricao;
	
	@Column (name = "id_categoria")
	private Integer categoria;
	
	@Transient
	private Boolean selecionado;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getSigla() {
		return sigla;
	}

	public void setSigla(String sigla) {
		this.sigla = sigla;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public void setSelecionado(Boolean selecionado) {
		this.selecionado = selecionado;
	}

	public Boolean getSelecionado() {
		return selecionado;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setCategoria(Integer categoria) {
		this.categoria = categoria;
	}

	public Integer getCategoria() {
		return categoria;
	}

	public void setNoPersisted(Boolean noPersisted) {
		this.noPersisted = noPersisted;
	}

	public Boolean getNoPersisted() {
		return noPersisted;
	}

}
