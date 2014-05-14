package jsf.view;

import java.util.ArrayList;
import java.util.List;

import javax.faces.model.SelectItem;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import persistence.service.UFService;
import data.entity.UF;

@Component("ufBean")
@Scope("session")
public class UFBean {

	private UFService ufService;

	public List<SelectItem> getEstados() {

		List<SelectItem> ufs = new ArrayList<SelectItem>();

		try {

			List<UF> lista = ufService.listar();

			for (UF uf : lista) {
				ufs.add(new SelectItem(uf.getId(), uf.getSigla()));
			}
		} catch (Exception e) {
			// TODO: handle exception
		}

		return ufs;
	}

	public List<SelectItem> getEstadosBySigla() {

		List<SelectItem> ufs = new ArrayList<SelectItem>();
		List<UF> lista = ufService.listar();

		for (UF uf : lista) {
			ufs.add(new SelectItem(uf.getSigla(), uf.getSigla()));
		}

		return ufs;
	}

	@Autowired
	public void setUfService(UFService ufService) {
		this.ufService = ufService;
	}

	public UFService getUfService() {
		return ufService;
	}

}
