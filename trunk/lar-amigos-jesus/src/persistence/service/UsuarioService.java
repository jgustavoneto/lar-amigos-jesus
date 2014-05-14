package persistence.service;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import generic.persistence.GenericDao;
import generic.security.Md5;

import org.springframework.stereotype.Repository;

import data.entity.Usuario;

@generic.persistence.Service(dataBase = "lar-amigos")
@Repository
public class UsuarioService extends GenericDao<Usuario> {

	public Usuario login(Usuario usuario){
		
		try {
			usuario.setSenha(Md5.MD5(usuario.getSenha()));
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		
		List<Usuario> usuarios =  find(usuario);
		if(!usuarios.isEmpty()){
			
			return usuarios.get(0);
		}
		
		return null;
	}
	
}
