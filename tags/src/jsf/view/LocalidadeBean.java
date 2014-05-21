package jsf.view;

import java.util.ArrayList;
import java.util.List;

import javax.faces.model.SelectItem;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import data.entity.Localidade;

import persistence.service.LocalidadeService;

@Component("localidadeBean")
@Scope("session")
public class LocalidadeBean {
	
	private Integer uf;
	
	private String estado;

	private String localidade;

	private LocalidadeService localidadeService = new LocalidadeService();

	public List<SelectItem> getLocalidades() {

		List<SelectItem> itens = new ArrayList<SelectItem>();
		List<Localidade> result = localidadeService.listar();

		for (Localidade localidade : result) {
			if (localidade.getSiglaEstado().equalsIgnoreCase(estado)
					| estado == null | "".equals(estado))
				itens.add(new SelectItem(localidade.getId(),
						localidade.getSiglaEstado()));
		}

		return itens;
	}

	@Autowired
	public void setLocalidadeService(LocalidadeService localidadeService) {
		this.localidadeService = localidadeService;
	}

	public LocalidadeService getLocalidadeService() {
		return localidadeService;
	}

	public void setLocalidade(String localidade) {
		this.localidade = localidade;
	}

	public String getLocalidade() {
		return localidade;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public String getEstado() {
		return estado;
	}

	public void setUf(Integer uf) {
		this.uf = uf;
	}

	public Integer getUf() {
		return uf;
	}

}
