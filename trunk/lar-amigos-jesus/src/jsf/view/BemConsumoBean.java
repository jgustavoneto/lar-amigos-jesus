package jsf.view;

import generic.utils.FacesUtils;

import java.util.ArrayList;
import java.util.List;

import javax.faces.event.ActionEvent;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import persistence.service.BemConsumoService;

import data.entity.BemConsumo;
import data.entity.Molestia;

@Component("bemConsumoBean")
@Scope("session")
public class BemConsumoBean extends GenericBean implements CRUD {

	private BemConsumoService bemConsumoService;

	public BemConsumoService getBemConsumoService() {
		return bemConsumoService;
	}

	@Autowired
	public void setBemConsumoService(BemConsumoService bemConsumoService) {
		this.bemConsumoService = bemConsumoService;
	}

	private BemConsumo bemConsumo = new BemConsumo();

	private BemConsumo selecionado = new BemConsumo();

	private DataModel bensConsumo = new ListDataModel(new ArrayList<Molestia>());

	private DataModel selecionados = new ListDataModel(
			new ArrayList<Molestia>());

	public void setSelecionado(BemConsumo selecionado) {
		this.selecionado = selecionado;
	}

	public BemConsumo getSelecionado() {
		return selecionado;
	}

	public void setBemConsumo(BemConsumo bemConsumo) {
		this.bemConsumo = bemConsumo;
	}

	public BemConsumo getBemConsumo() {
		return bemConsumo;
	}

	public void setBensConsumo(DataModel bensConsumo) {
		this.bensConsumo = bensConsumo;
	}

	public DataModel getBensConsumo() {
		return bensConsumo;
	}

	public void setSelecionados(DataModel selecionados) {
		this.selecionados = selecionados;
	}

	public DataModel getSelecionados() {
		return selecionados;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void alterar(ActionEvent event) {
		int index = ((ArrayList<BemConsumo>) bensConsumo.getWrappedData())
				.indexOf(bemConsumo);
		bemConsumo = bemConsumoService.editar(bemConsumo);
		((ArrayList<BemConsumo>) bensConsumo.getWrappedData()).set(index,
				bemConsumo);
		cancelar(event);
		FacesUtils.sendInfoMessage("Alteração efetuada com sucesso!");
	}

	@Override
	public void novo(ActionEvent event) {
		setBemConsumo(new BemConsumo());
		super.novo(event);
	}

	@Override
	public void consultar(ActionEvent event) {

		if (bemConsumo != null && bemConsumo.getNome() != null)
			bemConsumo.setNome(bemConsumo.getNome().trim().toUpperCase());

		List<BemConsumo> result = bemConsumoService.procurar(bemConsumo);
		bensConsumo.setWrappedData(result);

		if (result.isEmpty()) {
			FacesUtils
					.sendWarningMessage("Nenhum produto foi encontrado com esse nome!");
		}
	}

	@Override
	public void prepararAlteracao(ActionEvent event) {
		setCriando(false);
		setAlterando(true);
		setConsultando(false);
		setBemConsumo((BemConsumo) bensConsumo.getRowData());
	}

	@SuppressWarnings("unchecked")
	@Override
	public void removerSelecionados(ActionEvent event) {
		bemConsumoService.excluirTodos(((ArrayList<BemConsumo>) selecionados
				.getWrappedData()));

		List<BemConsumo> itens = ((ArrayList<BemConsumo>) selecionados
				.getWrappedData());

		for (BemConsumo obj : itens) {
			((ArrayList<BemConsumo>) bensConsumo.getWrappedData()).remove(obj);
		}

		selecionados.setWrappedData(new ArrayList<BemConsumo>());
		FacesUtils.sendInfoMessage("Remoção efetuada com sucesso");
	}
	
	/**
	 * @param event
	 */
	public void cancelarRemocao(ActionEvent event) {
		selecionados.setWrappedData(new ArrayList<BemConsumo>());
	}

	@SuppressWarnings("unchecked")
	@Override
	public void salvar(ActionEvent event) {
		try {
			bemConsumo = bemConsumoService.salvar(bemConsumo);
			((ArrayList<BemConsumo>) bensConsumo.getWrappedData())
					.add(bemConsumo);
			setBemConsumo(new BemConsumo());
			FacesUtils.sendInfoMessage("Operação realizada com sucesso!");
			cancelar(event);
		} catch (Exception e) {
			e.printStackTrace();
			FacesUtils
					.sendErrorMessage("Ocorreu um erro ao tentar salvar registro!");
		}

	}
	
	@SuppressWarnings("unchecked")
	public void selecionar(ActionEvent evt) {
		BemConsumo selecionado = (BemConsumo) bensConsumo.getRowData();
		if (selecionado.getSelecionado()) {
			((ArrayList<BemConsumo>) selecionados.getWrappedData())
					.add(selecionado);
		} else {
			((ArrayList<BemConsumo>) selecionados.getWrappedData())
					.remove(selecionado);
		}
	}

	/**
	 * @param event
	 */
	public void limpar(ActionEvent event) {
		setBemConsumo(new BemConsumo());
		getBensConsumo().setWrappedData(new ArrayList<BemConsumo>());
	}

}
