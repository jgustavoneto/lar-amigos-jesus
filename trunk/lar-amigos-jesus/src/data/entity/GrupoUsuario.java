package data.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Table;

@Entity
@Table(name = "grupos_usuarios", schema = "seguranca")
public class GrupoUsuario {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	//@JoinColumn(name = "id_usuario")
	//private Usuario usuario = new Usuario();

	//@JoinColumn(name = "id_grupo")
	//private Grupo grupo = new Grupo();

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	//public Usuario getUsuario() {
	//	return usuario;
	//}

	//public void setUsuario(Usuario usuario) {
	//	this.usuario = usuario;
	//}

	//public Grupo getGrupo() {
	//	return grupo;
	//}

	//public void setGrupo(Grupo grupo) {
	//	this.grupo = grupo;
	//}

}
