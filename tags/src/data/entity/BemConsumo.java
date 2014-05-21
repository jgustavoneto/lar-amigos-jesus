package data.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "bens_consumo", schema = "cadastros")
public class BemConsumo {
	
	@Transient
	private Boolean selecionado;
	
	@Id
	@GeneratedValue (strategy = GenerationType.IDENTITY)
	private Integer id;
	
	private String nome;
	
	private String descricao;
	
	@Column (name = "id_categoria")
	private Integer categoria;
	
	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getId() {
		return id;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getNome() {
		return nome;
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

	public void setSelecionado(Boolean selecionado) {
		this.selecionado = selecionado;
	}

	public Boolean getSelecionado() {
		return selecionado;
	}

}
