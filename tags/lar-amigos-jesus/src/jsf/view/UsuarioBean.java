package jsf.view;

import generic.security.Security;

import java.util.Calendar;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import persistence.service.UsuarioService;
import data.entity.Usuario;

@Component("usuarioBean")
@Scope("session")
public class UsuarioBean {

	private Boolean primeiroAcesso = true;

	private Usuario usuario = new Usuario();

	private UsuarioService usuarioService;

	public void logout(ActionEvent evt) {
		usuario = new Usuario();
	}

	public String logout() {
		return "tela-login";
	}

	private Boolean bloqueio;

	public void login(ActionEvent evt) {

		setPrimeiroAcesso(false);

		if (usuario.getUsuario() != null)
			usuario.setUsuario(usuario.getUsuario().toUpperCase());
		usuario = usuarioService.login(usuario);

		if (usuario != null) {
			setUsuario(usuario);
		} else {
			setUsuario(new Usuario());
		}
	}

	public String login() {

		if (usuario != null && usuario.getId() != null) {
			return "tela-admin";
		}
		FacesContext
				.getCurrentInstance()
				.addMessage(
						null,
						new FacesMessage(
								FacesMessage.SEVERITY_ERROR,
								"Usuário e/ou senha inválidos, tente efetuar autenticação novamente!",
								null));
		return "tela-login";
	}

	public String acessar() {

		if (Security.tryToUse() >= 1) {
			FacesContext
					.getCurrentInstance()
					.addMessage(
							null,
							new FacesMessage(
									FacesMessage.SEVERITY_ERROR,
									"O tempo de uso desta versão expirou, favor entrar em contato com o administrador do sistema.",
									null));
			setUsuario(new Usuario());
			return null;
		}

		setPrimeiroAcesso(false);

		if (usuario.getUsuario() != null)
			usuario.setUsuario(usuario.getUsuario().toUpperCase());
		usuario = usuarioService.login(usuario);

		if (usuario != null) {
			setUsuario(usuario);
		} else {
			FacesContext
					.getCurrentInstance()
					.addMessage(
							null,
							new FacesMessage(
									FacesMessage.SEVERITY_ERROR,
									"Usuário e/ou senha inválidos, tente efetuar autenticação novamente!",
									null));
			setUsuario(new Usuario());
			return null;
		}

		return "tela-admin";
	}

	public void limpar(ActionEvent evt) {
		usuario = new Usuario();
	}

	@Autowired
	public void setUsuarioService(UsuarioService usuarioService) {
		this.usuarioService = usuarioService;
	}

	public UsuarioService getUsuarioService() {
		return usuarioService;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void carregar(ActionEvent evt) {
		System.out.println("x");
	}

	public void setPrimeiroAcesso(Boolean primeiroAcesso) {
		this.primeiroAcesso = primeiroAcesso;
	}

	public Boolean getPrimeiroAcesso() {
		return primeiroAcesso;
	}

	public void setBloqueio(Boolean bloqueio) {
		this.bloqueio = bloqueio;
	}

	public Boolean getBloqueio() {
		if (!primeiroAcesso && usuario.getId() != null) {
			return false;
		}

		return true;
	}

	private String welcomeMessage = "";

	public String getWelcomeMessage() {

		try {

			if (welcomeMessage.length() != 0) {
				return welcomeMessage;
			}

			Calendar calendar = Calendar.getInstance();

			if (calendar.get(Calendar.HOUR_OF_DAY) < 12
					& calendar.get(Calendar.HOUR_OF_DAY) >= 0) {
				welcomeMessage += "BOM DIA "
						+ getUsuario().getNome().toUpperCase() + "!";
			} else if (calendar.get(Calendar.HOUR_OF_DAY) >= 12
					& calendar.get(Calendar.HOUR_OF_DAY) < 18) {
				welcomeMessage += "BOA TARDE "
						+ getUsuario().getNome().toUpperCase() + "!";
			} else if (calendar.get(Calendar.HOUR_OF_DAY) >= 18) {
				welcomeMessage += "BOA NOITE "
						+ getUsuario().getNome().toUpperCase() + "!";
			}

		} catch (Exception e) {
			return "Seja bem vindo(a)!";
		}

		return welcomeMessage;
	}

}
