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
 * Métodos úteis para os managed beans.
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
	 * Pega o login do usuário logado no sistema.
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
	 * Mensagem enviada para o usuário dando uma informação positiva.
	 * 
	 * @param msg
	 *            Mensagem a ser enviada.
	 */
	public static void addInfoMessage(String msg) {
		addInfoMessage(null, msg);
	}

	/**
	 * Adiciona um mensagem de informação para um cliente (uma tag) específico.
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
	 * Mensagem enviada para o usuário dando uma informação de erro.
	 * 
	 * @param msg
	 *            Mensagem a ser enviada.
	 */
	public static void addErrorMessage(String msg) {
		addErrorMessage(null, msg);
	}

	/**
	 * Adiciona um mensagem de erro para um cliente (uma tag) específico.
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
	 * Mensagem enviada para o usuário dando um aviso.
	 * 
	 * @param msg
	 *            Mensagem a ser enviada.
	 */
	public static void addWarnMessage(String msg) {
		addWarnMessage(null, msg);
	}

	/**
	 * Adiciona um mensagem de aviso para um cliente (uma tag) específico.
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
	 * Pega um valor específico do arquivo de propriedades baseado no campo
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
	 * Pega um atributo que está na sessão da aplicação.
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
	 * Pega o usuário que está na sessão da aplicação.
	 * 
	 * @return Usuário
	 */
	public static Usuario pegaUsuarioSessao() {
		return (Usuario) pegaAtributoSessao("usuario");
	}

	/**
	 * Coloca um atributo na sessão da aplicação.
	 * 
	 * @param name
	 *            Nome que identificará o objeto que está na sessão.
	 * @param obj
	 *            Objeto a ser colocado na sessão.
	 */
	public static void colocaEmSessao(String name, Object obj) {
		HttpServletRequest request = getServletRequest();
		// Configurando o atributo na sessão.
		request.getSession().setAttribute(name, obj);
	}

	/**
	 * Coloca o usuário do sistema na sessão.
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
	 * Pega uma instância de um bean configurado pelo spring.
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