package jsf.view;

import java.util.ArrayList;
import java.util.List;

import javax.faces.model.SelectItem;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import data.entity.CategoriaMolestia;

import persistence.service.CategoriaMolestiaService;

@Component("categoriaMolestiaBean")
@Scope("session")
public class CategoriaMolestiaBean {

	private CategoriaMolestiaService categoriaMolestiaService;

	public List<SelectItem> getCategorias() {

		List<SelectItem> itens = new ArrayList<SelectItem>();
		List<CategoriaMolestia> lista = categoriaMolestiaService.listar();

		for (CategoriaMolestia categoriaMolestia : lista) {
			itens.add(new SelectItem(categoriaMolestia.getId(),
					categoriaMolestia.getNome()));
		}

		return itens;
	}

	@Autowired
	public void setCategoriaMolestiaService(
			CategoriaMolestiaService categoriaMolestiaService) {
		this.categoriaMolestiaService = categoriaMolestiaService;
	}

	public CategoriaMolestiaService getCategoriaMolestiaService() {
		return categoriaMolestiaService;
	}

}
