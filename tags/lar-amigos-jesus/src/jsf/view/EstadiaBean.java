package jsf.view;

import java.util.ArrayList;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import persistence.service.EstadiaService;

import data.entity.Estadia;
import data.entity.Paciente;

@Component("estadiaBean")
@Scope("session")
public class EstadiaBean {

	private Estadia estadia = new Estadia();

	private Paciente paciente = new Paciente();

	private Boolean consultando = true;

	private Boolean criando = false;

	private Boolean alterando = false;

	private EstadiaService estadiaService;

	public void prepararAlteracao(ActionEvent event) {

		setCriando(false);
		setAlterando(true);
		setConsultando(false);

		setEstadia((Estadia) estadias.getRowData());

	}

	@SuppressWarnings("unchecked")
	public void selecionar(ActionEvent evt) {

		Estadia selecionado = (Estadia) estadias.getRowData();

		if (selecionado.getSelecionado()) {
			((ArrayList<Estadia>) selecionados.getWrappedData()).add(selecionado);
		} else {
			((ArrayList<Estadia>) selecionados.getWrappedData())
					.remove(selecionado);
		}

	}

	public void limpar(ActionEvent evt) {
		Estadia estadia = new Estadia();
		estadia.setPaciente(paciente.getId());
		setEstadia(estadia);
	}

	public void alterar(ActionEvent event) {

		estadia = estadiaService.editar(estadia);
		List<Estadia> result = estadiaService.listar();
		estadias.setWrappedData(result);

		setEstadia(new Estadia());
		setConsultando(true);
		setAlterando(false);
		setCriando(false);
	}

	public void novo(ActionEvent evt) {
		setEstadia(new Estadia());
		setCriando(true);
		setAlterando(false);
		setConsultando(false);
	}

	public void cancelar(ActionEvent evt) {
		Estadia estadia = new Estadia();
		estadia.setPaciente(paciente.getId());
		setEstadia(estadia);
		setCriando(false);
		setAlterando(false);
		setConsultando(true);
	}
	
	public String voltar() {
		Estadia estadia = new Estadia();
		estadia.setPaciente(paciente.getId());
		setEstadia(estadia);
		setCriando(false);
		setAlterando(false);
		setConsultando(true);
		return "tela-pacientes";
	}

	public String ver() {

		Estadia estadia = new Estadia();
		estadia.setPaciente(paciente.getId());
		List<Estadia> result = estadiaService.find(estadia);
		estadias.setWrappedData(result);

		return "tela-estadia";
	}

	@SuppressWarnings("unchecked")
	public void removerSelecionados(ActionEvent event) {
		estadiaService.excluirTodos(((ArrayList<Estadia>) selecionados
				.getWrappedData()));
		List<Estadia> itens = ((ArrayList<Estadia>) selecionados
				.getWrappedData());
		for (Estadia obj : itens) {
			((ArrayList<Estadia>) estadias.getWrappedData()).remove(obj);
		}
		selecionados.setWrappedData(new ArrayList<Estadia>());
	}

	@SuppressWarnings("unchecked")
	public void salvar(ActionEvent evt) {
		estadia.setPaciente(paciente.getId());
		boolean erro = false;
		try {
			estadiaService.salvar(estadia);
		} catch (Exception e) {
			erro = true;
			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR,
							"O paciente já se encontra na casa", null));
		}

		if (!erro) {
			((ArrayList<Estadia>) estadias.getWrappedData()).add(estadia);
			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_INFO,
							"Registro incluído com sucesso!", null));
		}

		setConsultando(true);
		setCriando(false);
		setAlterando(false);
		Estadia estadia = new Estadia();
		estadia.setPaciente(paciente.getId());
		setEstadia(estadia);

	}

	public void cancelarRemocao(ActionEvent evt) {
		selecionados.setWrappedData(new ArrayList<Paciente>());
	}

	@SuppressWarnings("unused")
	private Boolean criandoOuAlterando = false;

	private DataModel estadias = new ListDataModel(new ArrayList<Paciente>());
	private DataModel selecionados = new ListDataModel(
			new ArrayList<Paciente>());

	public DataModel getSelecionados() {
		return selecionados;
	}

	public void setSelecionados(DataModel selecionados) {
		this.selecionados = selecionados;
	}

	public void setEstadias(DataModel estadias) {
		this.estadias = estadias;
	}

	public DataModel getEstadias() {
		return estadias;
	}

	public void setConsultando(Boolean consultando) {
		this.consultando = consultando;
	}

	public Boolean getConsultando() {
		return consultando;
	}

	public void setCriando(Boolean criando) {
		this.criando = criando;
	}

	public Boolean getCriando() {
		return criando;
	}

	public void setAlterando(Boolean alterando) {
		this.alterando = alterando;
	}

	public Boolean getAlterando() {
		return alterando;
	}

	public Boolean getCriandoOuAlterando() {
		return (criando || alterando);
	}

	public void setPaciente(Paciente paciente) {
		this.paciente = paciente;
	}

	public Paciente getPaciente() {
		return paciente;
	}

	@Autowired
	public void setEstadiaService(EstadiaService estadiaService) {
		this.estadiaService = estadiaService;
	}

	public EstadiaService getEstadiaService() {
		return estadiaService;
	}

	public void setEstadia(Estadia estadia) {
		this.estadia = estadia;
	}

	public Estadia getEstadia() {
		return estadia;
	}

}
