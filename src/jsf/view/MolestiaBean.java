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

import persistence.service.MolestiaService;
import data.entity.Molestia;

@Component("molestiaBean")
@Scope("session")
public class MolestiaBean {

	private Boolean consultando = true;

	private Boolean criando = false;

	private Boolean alterando = false;

	@SuppressWarnings("unused")
	private Boolean criandoOuAlterando = false;

	private Molestia molestia = new Molestia();

	private Molestia selecionado = new Molestia();

	private DataModel molestias = new ListDataModel(new ArrayList<Molestia>());

	private DataModel selecionados = new ListDataModel(
			new ArrayList<Molestia>());

	private MolestiaService molestiaService;

	public void consultar(ActionEvent evt) {
		if (molestia.getNome() != null) {
			molestia.setNome(molestia.getNome().toUpperCase());
		}
		if (molestia.getSigla() != null) {
			molestia.setSigla(molestia.getSigla().toUpperCase());
		}
		
		if("".equals(molestia.getNome()))
			molestia.setNome(null);
		if("".equals(molestia.getDescricao()))
			molestia.setDescricao(null);
		if("".equals(molestia.getSigla()))
			molestia.setSigla(null);
		

		List<Molestia> result = molestiaService.procurar(molestia);
		molestias.setWrappedData(result);

		if (result.isEmpty()) {
			FacesUtils
					.sendWarningMessage("Nenhuma moléstia/doença foi encontrada com esse nome!");
		}
	}
	
	

	@Autowired
	public void setMolestiaService(MolestiaService molestiaService) {
		this.molestiaService = molestiaService;
	}

	public MolestiaService getMolestiaService() {
		return molestiaService;
	}

	public void setMolestia(Molestia molestia) {
		this.molestia = molestia;
	}

	public Molestia getMolestia() {
		return molestia;
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

	public void setCriandoOuAlterando(Boolean criandoOuAlterando) {
		this.criandoOuAlterando = criandoOuAlterando;
	}

	public Boolean getCriandoOuAlterando() {
		return (criando || alterando);
	}

	public Molestia getSelecionado() {
		return selecionado;
	}

	public void setSelecionado(Molestia selecionado) {
		this.selecionado = selecionado;
	}

	public void setMolestias(DataModel molestias) {
		this.molestias = molestias;
	}

	public DataModel getMolestias() {
		return molestias;
	}

	public void setSelecionados(DataModel selecionados) {
		this.selecionados = selecionados;
	}

	public DataModel getSelecionados() {
		return selecionados;
	}

	public void limpar(ActionEvent evt) {
		setMolestia(new Molestia());
		getMolestias().setWrappedData(new ArrayList<Molestia>());
	}

	public void cancelar(ActionEvent evt) {
		setMolestia(new Molestia());
		setCriando(false);
		setAlterando(false);
		setConsultando(true);
	}

	public void novo(ActionEvent evt) {
		setMolestia(new Molestia());
		setCriando(true);
		setConsultando(false);
	}

	@SuppressWarnings("unchecked")
	public void salvar(ActionEvent event) {

		try {
			molestia = molestiaService.salvar(molestia);
			((ArrayList<Molestia>) molestias.getWrappedData()).add(molestia);
			setMolestia(new Molestia());
			setConsultando(true);
			setAlterando(false);
			setCriando(false);
			FacesUtils.sendInfoMessage("Operação realizada com sucesso!");
		} catch (Exception e) {
			e.printStackTrace();
			FacesUtils
					.sendErrorMessage("Ocorreu um erro ao tentar salvar registro!");
		}

	}

	@SuppressWarnings("unchecked")
	public void alterar(ActionEvent event) {

		int index = ((ArrayList<Molestia>) molestias.getWrappedData())
				.indexOf(molestia);
		molestia = molestiaService.editar(molestia);
		((ArrayList<Molestia>) molestias.getWrappedData()).set(index, molestia);

		setMolestia(new Molestia());
		setConsultando(true);
		setAlterando(false);
		setCriando(false);

		FacesUtils.sendInfoMessage("Alteração efetuada com sucesso!");

	}

	public void prepararAlteracao(ActionEvent event) {
		setCriando(false);
		setAlterando(true);
		setConsultando(false);
		setMolestia((Molestia) molestias.getRowData());
	}
	
	@SuppressWarnings("unchecked")
	public void selecionar(ActionEvent evt) {
		Molestia selecionado = (Molestia) molestias.getRowData();
		if (selecionado.getSelecionado()) {
			((ArrayList<Molestia>) selecionados.getWrappedData())
					.add(selecionado);
		} else {
			((ArrayList<Molestia>) selecionados.getWrappedData())
					.remove(selecionado);
		}
	}
	
	@SuppressWarnings("unchecked")
	public void removerSelecionados(ActionEvent event) {
		molestiaService.excluirTodos(((ArrayList<Molestia>) selecionados
				.getWrappedData()));
		
		List<Molestia> itens = ((ArrayList<Molestia>) selecionados
				.getWrappedData());
		
		for (Molestia obj : itens) {
			((ArrayList<Molestia>) molestias
					.getWrappedData()).remove(obj);
		}
		
		selecionados.setWrappedData(new ArrayList<Molestia>());
		FacesUtils.sendInfoMessage("Remoção efetuada com sucesso");
	}
	
	public void cancelarRemocao(ActionEvent evt) {
		selecionados.setWrappedData(new ArrayList<Molestia>());
	}
	
	@SuppressWarnings("unused")
	private List<SelectItem> itens;
	
	public List<SelectItem> getItens(){
		
		List<SelectItem> itens = new ArrayList<SelectItem>();
		List<Molestia> molestias = molestiaService.listar();
		
		for (Molestia m : molestias) {
			SelectItem item = new SelectItem(m.getId(),m.getNome());
			itens.add(item);
		}
		
		return itens;
	}

	public void setItens(List<SelectItem> itens) {
		this.itens = itens;
	}
}
