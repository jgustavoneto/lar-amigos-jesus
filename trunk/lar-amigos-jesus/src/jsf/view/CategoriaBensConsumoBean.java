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

import persistence.service.CategoriaBensConsumoService;
import data.entity.BemConsumo;
import data.entity.CategoriaBemConsumo;
import data.entity.Molestia;

@Component("categoriaBensConsumoBean")
@Scope("session")
public class CategoriaBensConsumoBean extends GenericBean implements CRUD {

	private CategoriaBemConsumo categoria = new CategoriaBemConsumo();

	private CategoriaBemConsumo selecionado = new CategoriaBemConsumo();

	private DataModel categoriasBemConsumo = new ListDataModel(
			new ArrayList<CategoriaBemConsumo>());

	private ArrayList<SelectItem> categorias = new ArrayList<SelectItem>();

	private DataModel selecionados = new ListDataModel(
			new ArrayList<CategoriaBemConsumo>());

	private CategoriaBensConsumoService categoriaBensConsumoService;

	public CategoriaBensConsumoService getCategoriaBensConsumoService() {
		return categoriaBensConsumoService;
	}

	@Autowired
	public void setCategoriaBensConsumoService(
			CategoriaBensConsumoService categoriaBensConsumoService) {
		this.categoriaBensConsumoService = categoriaBensConsumoService;
	}

	public List<SelectItem> getCategorias() {

		List<SelectItem> itens = new ArrayList<SelectItem>();
		List<CategoriaBemConsumo> lista = categoriaBensConsumoService.listar();

		for (CategoriaBemConsumo categoria : lista) {
			itens.add(new SelectItem(categoria.getId(), categoria.getNome()));
		}

		return itens;
	}

	public void setCategoria(CategoriaBemConsumo categoria) {
		this.categoria = categoria;
	}

	public CategoriaBemConsumo getCategoria() {
		return categoria;
	}

	public void setSelecionado(CategoriaBemConsumo selecionado) {
		this.selecionado = selecionado;
	}

	public CategoriaBemConsumo getSelecionado() {
		return selecionado;
	}

	public void setCategoriasBemConsumo(DataModel categoriasBemConsumo) {
		this.categoriasBemConsumo = categoriasBemConsumo;
	}

	public DataModel getCategoriasBemConsumo() {
		return categoriasBemConsumo;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void alterar(ActionEvent event) {
		int index = ((ArrayList<BemConsumo>) categoriasBemConsumo
				.getWrappedData()).indexOf(categoria);
		categoria = categoriaBensConsumoService.editar(categoria);
		((ArrayList<CategoriaBemConsumo>) categoriasBemConsumo.getWrappedData())
				.set(index, categoria);
		cancelar(event);
		FacesUtils.sendInfoMessage("Alteração efetuada com sucesso!");
	}

	public void novo(ActionEvent event) {
		setCategoria(new CategoriaBemConsumo());
		super.novo(event);
	}

	@Override
	public void consultar(ActionEvent event) {
		if (categoria != null && categoria.getNome() != null)
			categoria.setNome(categoria.getNome().trim().toUpperCase());

		List<CategoriaBemConsumo> result = categoriaBensConsumoService
				.procurar(categoria);
		categoriasBemConsumo.setWrappedData(result);

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
		setCategoria((CategoriaBemConsumo) categoriasBemConsumo.getRowData());
	}

	@SuppressWarnings("unchecked")
	@Override
	public void removerSelecionados(ActionEvent event) {
		categoriaBensConsumoService
				.excluirTodos(((ArrayList<CategoriaBemConsumo>) selecionados
						.getWrappedData()));

		List<CategoriaBemConsumo> itens = ((ArrayList<CategoriaBemConsumo>) selecionados
				.getWrappedData());

		for (CategoriaBemConsumo obj : itens) {
			((ArrayList<CategoriaBemConsumo>) categoriasBemConsumo
					.getWrappedData()).remove(obj);
		}

		selecionados.setWrappedData(new ArrayList<CategoriaBemConsumo>());
		FacesUtils.sendInfoMessage("Remoção efetuada com sucesso");
	}

	@SuppressWarnings("unchecked")
	@Override
	public void salvar(ActionEvent event) {
		CategoriaBemConsumo selecionado = (CategoriaBemConsumo) categoriasBemConsumo
				.getRowData();
		if (selecionado.getSelecionado()) {
			((ArrayList<CategoriaBemConsumo>) selecionados.getWrappedData())
					.add(selecionado);
		} else {
			((ArrayList<CategoriaBemConsumo>) selecionados.getWrappedData())
					.remove(selecionado);
		}
	}

	/**
	 * @param event
	 */
	public void limpar(ActionEvent event) {
		setCategoria(new CategoriaBemConsumo());
		getCategoriasBemConsumo().setWrappedData(
				new ArrayList<CategoriaBemConsumo>());
	}
}
