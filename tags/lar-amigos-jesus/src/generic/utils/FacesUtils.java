package generic.utils;

import java.util.Iterator;
import java.util.ResourceBundle;

import javax.faces.application.Application;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

import org.springframework.context.ApplicationContext;
import org.springframework.web.jsf.FacesContextUtils;

import data.entity.Usuario;

/**
 * M�todos �teis para os managed beans.
 * 
 * @author Eric Dutra
 */
public class FacesUtils {

	/**
	 * Retorna o HttpServletRequest do contexto do jsf.
	 * 
	 * @return HttpServletRequest do contexto do jsf.
	 */
	public static HttpServletRequest getServletRequest() {
		return (HttpServletRequest) FacesContext.getCurrentInstance()
				.getExternalContext().getRequest();
	}

	/**
	 * Pega o login do usu�rio logado no sistema.
	 * 
	 * @return
	 */
	public static String getUser() {
		return getServletRequest().getUserPrincipal().getName();
	}

	public static String getRole() {
		return getServletRequest().getParameter("role");
	}

	@SuppressWarnings("unchecked")
	public static boolean isMessageErro() {

		Iterator i = FacesContext.getCurrentInstance().getMessages();

		if (i.hasNext()) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Mensagem enviada para o usu�rio dando uma informa��o positiva.
	 * 
	 * @param msg
	 *            Mensagem a ser enviada.
	 */
	public static void addInfoMessage(String msg) {
		addInfoMessage(null, msg);
	}

	/**
	 * Adiciona um mensagem de informa��o para um cliente (uma tag) espec�fico.
	 * 
	 * @param clientId
	 *            Id do cliente (tag).
	 * @param msg
	 *            Mensagem a ser adicionada.
	 */
	public static void addInfoMessage(String clientId, String msg) {
		FacesContext.getCurrentInstance().addMessage(clientId,
				new FacesMessage(FacesMessage.SEVERITY_INFO, msg, msg));
	}

	/**
	 * Mensagem enviada para o usu�rio dando uma informa��o de erro.
	 * 
	 * @param msg
	 *            Mensagem a ser enviada.
	 */
	public static void addErrorMessage(String msg) {
		addErrorMessage(null, msg);
	}

	/**
	 * Adiciona um mensagem de erro para um cliente (uma tag) espec�fico.
	 * 
	 * @param clientId
	 *            Id do cliente (tag).
	 * @param msg
	 *            Mensagem a ser adicionada.
	 */
	public static void addErrorMessage(String clientId, String msg) {
		FacesContext.getCurrentInstance().addMessage(clientId,
				new FacesMessage(FacesMessage.SEVERITY_ERROR, msg, msg));
	}

	/**
	 * Mensagem enviada para o usu�rio dando um aviso.
	 * 
	 * @param msg
	 *            Mensagem a ser enviada.
	 */
	public static void addWarnMessage(String msg) {
		addWarnMessage(null, msg);
	}

	/**
	 * Adiciona um mensagem de aviso para um cliente (uma tag) espec�fico.
	 * 
	 * @param clientId
	 *            Id do cliente (tag).
	 * @param msg
	 *            Mensagem a ser adicionada.
	 */
	public static void addWarnMessage(String clientId, String msg) {
		FacesContext.getCurrentInstance().addMessage(clientId,
				new FacesMessage(FacesMessage.SEVERITY_WARN, msg, msg));
	}

	/**
	 * Pega um valor espec�fico do arquivo de propriedades baseado no campo
	 * chave.
	 * 
	 * @param chave
	 *            Chave correspondente ao valor desejado.
	 * @return Valor baseado na chave.
	 */
	public static String getMessage(String chave) {
		Application ap = FacesContext.getCurrentInstance().getApplication();
		ResourceBundle r = ap.getResourceBundle(FacesContext
				.getCurrentInstance(), "msgResource");
		return r.getString(chave);
	}

	/**
	 * Pega um atributo que est� na sess�o da aplica��o.
	 * 
	 * @return Object
	 */
	public static Object pegaAtributoSessao(String name) {
		if (FacesContext.getCurrentInstance() != null) {
			HttpServletRequest request = getServletRequest();
			return request.getSession().getAttribute(name);
		}
		return null;
	}

	/**
	 * Pega o usu�rio que est� na sess�o da aplica��o.
	 * 
	 * @return Usu�rio
	 */
	public static Usuario pegaUsuarioSessao() {
		return (Usuario) pegaAtributoSessao("usuario");
	}

	/**
	 * Coloca um atributo na sess�o da aplica��o.
	 * 
	 * @param name
	 *            Nome que identificar� o objeto que est� na sess�o.
	 * @param obj
	 *            Objeto a ser colocado na sess�o.
	 */
	public static void colocaEmSessao(String name, Object obj) {
		HttpServletRequest request = getServletRequest();
		// Configurando o atributo na sess�o.
		request.getSession().setAttribute(name, obj);
	}

	/**
	 * Coloca o usu�rio do sistema na sess�o.
	 * 
	 * @param usuario
	 */
	public static void colocaUsuarioEmSessao(Usuario usuario) {
		colocaEmSessao("usuario", usuario);
	}

	/**
	 * 
	 * @return
	 */
	public static ApplicationContext getApplicationContext() {
		return FacesContextUtils.getWebApplicationContext(FacesContext
				.getCurrentInstance());
	}

	/**
	 * Pega uma inst�ncia de um bean configurado pelo spring.
	 * 
	 * @param bean
	 *            identificador do bean.
	 * @return
	 */
	public static Object getBean(String bean) {
		return getApplicationContext().getBean(bean);
	}

	public static void sendWarningMessage(String msg) {
		FacesContext.getCurrentInstance().addMessage(null,
				new FacesMessage(FacesMessage.SEVERITY_WARN, msg, null));
	}
	
	public static void sendInfoMessage(String msg) {
		FacesContext.getCurrentInstance().addMessage(null,
				new FacesMessage(FacesMessage.SEVERITY_INFO, msg, null));
	}
	
	public static void sendErrorMessage(String msg) {
		FacesContext.getCurrentInstance().addMessage(null,
				new FacesMessage(FacesMessage.SEVERITY_ERROR, msg, null));
	}
	
}