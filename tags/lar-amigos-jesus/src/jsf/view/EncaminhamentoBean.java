package jsf.view;

import generic.utils.FacesUtils;

import java.util.ArrayList;
import java.util.List;

import javax.faces.event.ActionEvent;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import javax.faces.model.SelectItem;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import persistence.service.EncaminhamentoService;
import data.entity.UnidadeEncaminhamento;

@Component("encaminhamentoBean")
@Scope("session")
public class EncaminhamentoBean {

	private Boolean consultando = true;

	private Boolean criando = false;

	private Boolean alterando = false;

	@SuppressWarnings("unused")
	private Boolean criandoOuAlterando = false;

	private UnidadeEncaminhamento unidade = new UnidadeEncaminhamento();

	private UnidadeEncaminhamento selecionado = new UnidadeEncaminhamento();

	private DataModel unidades = new ListDataModel(
			new ArrayList<UnidadeEncaminhamento>());
	
	private DataModel selecionados = new ListDataModel(
			new ArrayList<UnidadeEncaminhamento>());

	private EncaminhamentoService encaminhamentoService;
	
	public void consultar(ActionEvent evt) {
		if (unidade.getNome() != null) {
			unidade.setNome(unidade.getNome().toUpperCase());
		}
		if (unidade.getSigla() != null) {
			unidade.setSigla(unidade.getSigla().toUpperCase());
		}
		
		List<UnidadeEncaminhamento> result = encaminhamentoService.procurar(unidade);
		unidades.setWrappedData(result);

		if (result.isEmpty()) {
			FacesUtils.sendWarningMessage("Nenhuma unidade de encaminhamento/hospital foi encontrada com esse nome!");
		}
	}

	@Autowired
	public void setEncaminhamentoService(
			EncaminhamentoService encaminhamentoService) {
		this.encaminhamentoService = encaminhamentoService;
	}

	public EncaminhamentoService getEncaminhamentoService() {
		return encaminhamentoService;
	}

	public void setUnidade(UnidadeEncaminhamento unidade) {
		this.unidade = unidade;
	}

	public UnidadeEncaminhamento getUnidade() {
		return unidade;
	}

	public Boolean getConsultando() {
		return consultando;
	}

	public void setConsultando(Boolean consultando) {
		this.consultando = consultando;
	}

	public Boolean getCriando() {
		return criando;
	}

	public void setCriando(Boolean criando) {
		this.criando = criando;
	}

	public Boolean getAlterando() {
		return alterando;
	}

	public void setAlterando(Boolean alterando) {
		this.alterando = alterando;
	}

	public UnidadeEncaminhamento getSelecionado() {
		return selecionado;
	}

	public void setSelecionado(UnidadeEncaminhamento selecionado) {
		this.selecionado = selecionado;
	}

	public void setCriandoOuAlterando(Boolean criandoOuAlterando) {
		this.criandoOuAlterando = criandoOuAlterando;
	}

	public Boolean getCriandoOuAlterando() {
		return (criando || alterando);
	}

	public void setUnidades(DataModel unidades) {
		this.unidades = unidades;
	}

	public DataModel getUnidades() {
		return unidades;
	}

	public void setSelecionados(DataModel selecionados) {
		this.selecionados = selecionados;
	}

	public DataModel getSelecionados() {
		return selecionados;
	}
	
	public void limpar(ActionEvent evt) {
		setUnidade(new UnidadeEncaminhamento());
		getUnidades().setWrappedData(new ArrayList<UnidadeEncaminhamento>());
	}
	
	public void cancelar(ActionEvent evt) {
		setUnidade(new UnidadeEncaminhamento());
		setCriando(false);
		setAlterando(false);
		setConsultando(true);
	}
	
	public void novo(ActionEvent evt) {
		setUnidade(new UnidadeEncaminhamento());
		setCriando(true);
		setConsultando(false);
	}
	
	@SuppressWarnings("unchecked")
	public void salvar(ActionEvent event) {

		try {
			unidade = encaminhamentoService.salvar(unidade);
			((ArrayList<UnidadeEncaminhamento>) unidades.getWrappedData()).add(unidade);
			setUnidade(new UnidadeEncaminhamento());
			setConsultando(true);
			setAlterando(false);
			setCriando(false);
			FacesUtils.sendInfoMessage("Operação realizada com sucesso!");
		} catch (Exception e) {
			FacesUtils.sendErrorMessage("Ocorreu um erro ao tentar salvar registro!");
		}

	}
	
	@SuppressWarnings("unchecked")
	public void alterar(ActionEvent event) {

		int index = ((ArrayList<UnidadeEncaminhamento>) unidades.getWrappedData())
				.indexOf(unidade);
		unidade = encaminhamentoService.editar(unidade);
		((ArrayList<UnidadeEncaminhamento>) unidades.getWrappedData()).set(index, unidade);

		setUnidade(new UnidadeEncaminhamento());
		setConsultando(true);
		setAlterando(false);
		setCriando(false);

		FacesUtils.sendInfoMessage("Alteração efetuada com sucesso!");

	}
	
	public void prepararAlteracao(ActionEvent event) {
		setCriando(false);
		setAlterando(true);
		setConsultando(false);
		setUnidade((UnidadeEncaminhamento) unidades.getRowData());
	}
	
	@SuppressWarnings("unchecked")
	public void selecionar(ActionEvent evt) {
		UnidadeEncaminhamento selecionado = (UnidadeEncaminhamento) unidades.getRowData();
		if (selecionado.getSelecionado()) {
			((ArrayList<UnidadeEncaminhamento>) selecionados.getWrappedData())
					.add(selecionado);
		} else {
			((ArrayList<UnidadeEncaminhamento>) selecionados.getWrappedData())
					.remove(selecionado);
		}
	}
	
	@SuppressWarnings("unchecked")
	public void removerSelecionados(ActionEvent event) {
		encaminhamentoService.excluirTodos(((ArrayList<UnidadeEncaminhamento>) selecionados
				.getWrappedData()));
		
		List<UnidadeEncaminhamento> itens = ((ArrayList<UnidadeEncaminhamento>) selecionados
				.getWrappedData());
		
		for (UnidadeEncaminhamento obj : itens) {
			((ArrayList<UnidadeEncaminhamento>) unidades
					.getWrappedData()).remove(obj);
		}
		
		selecionados.setWrappedData(new ArrayList<UnidadeEncaminhamento>());
		FacesUtils.sendInfoMessage("Remoção efetuada com sucesso");
	}
	
	public void cancelarRemocao(ActionEvent evt) {
		selecionados.setWrappedData(new ArrayList<UnidadeEncaminhamento>());
	}
	
	@SuppressWarnings("unused")
	private List<SelectItem> itens;
	
	public List<SelectItem> getItens(){
		
		List<SelectItem> itens = new ArrayList<SelectItem>();
		List<UnidadeEncaminhamento> unidades = encaminhamentoService.listar();
		
		for (UnidadeEncaminhamento unidadeEncaminhamento : unidades) {
			SelectItem item = new SelectItem(unidadeEncaminhamento.getId(),unidadeEncaminhamento.getNome());
			itens.add(item);
		}
		
		return itens;
	}

	public void setItens(List<SelectItem> itens) {
		this.itens = itens;
	}

}
