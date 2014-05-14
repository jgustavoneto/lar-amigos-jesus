package jsf.view;

import generic.report.Report;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.model.DataModel;
import javax.faces.model.SelectItem;
import javax.servlet.ServletContext;

import org.richfaces.model.impl.ListDataModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import persistence.service.CategoriaMolestiaService;
import persistence.service.MolestiaService;
import data.entity.Molestia;

@Component("relatoriosBean")
@Scope("session")
public class RelatoriosBean {

	private Report acolhidosPorMolestias = new Report(
			"pacientes-por-molestias.jasper");

	private Molestia molestia = new Molestia();

	private MolestiaService molestiaService;

	private CategoriaMolestiaService categoriaService;

	private DataModel molestias = new ListDataModel(new ArrayList());

	public void atualizarMolestias(ActionEvent event) {

		molestias.setWrappedData(molestiaService
				.getMolestiaByCategoria(molestia));

		if (molestias == null || molestias.getRowCount() == 0) {
			molestias.setWrappedData(molestiaService.listar());
		}

	}

	@SuppressWarnings("unchecked")
	public List<SelectItem> getMolestias() {
		List<SelectItem> itens = new ArrayList<SelectItem>();
		List<Molestia> lista = (ArrayList<Molestia>) molestias.getWrappedData();

		for (Molestia m : lista) {
			SelectItem item = new SelectItem(m.getId(), m.getNome());
			itens.add(item);
		}
		return itens;
	}

	public void setMolestia(Molestia molestia) {
		this.molestia = molestia;
	}

	public Molestia getMolestia() {
		return molestia;
	}

	@Autowired
	public void setMolestiaService(MolestiaService molestiaService) {
		this.molestiaService = molestiaService;
	}

	public MolestiaService getMolestiaService() {
		return molestiaService;
	}

	@Autowired
	public void setCategoriaService(CategoriaMolestiaService categoriaService) {
		this.categoriaService = categoriaService;
	}

	public CategoriaMolestiaService getCategoriaService() {
		return categoriaService;
	}

	public void setMolestias(DataModel molestias) {
		this.molestias = molestias;
	}
	
	public void limpar(ActionEvent event){
		this.setMolestia(new Molestia());		
	}

	@SuppressWarnings( { "static-access", "unchecked" })
	public void viewReport(ActionEvent event) {

		FacesContext context = FacesContext.getCurrentInstance();
		ServletContext sc = (ServletContext) context.getExternalContext()
				.getContext();
		@SuppressWarnings("unused")
		HashMap map = new HashMap();
		HashMap parameters = new HashMap<String, Object>();

		parameters.put("id_categoria", (molestia.getCategoria() == 0 ? null
				: molestia.getCategoria()));
		parameters.put("id_molestia", (molestia.getId() == 0 ? null : molestia
				.getId()));

		parameters.put("TOP_IMG", acolhidosPorMolestias
				.getPath("WEB-INF/relatorios/img/larlogo-old.jpg"));// larlogo
		// parameters.put("SUBREPORT_DIR", acolhidosPorMolestias
		// .getPath("WEB-INF/relatorios/"));
		try {
			acolhidosPorMolestias.print(event, parameters,
					"pacientes-por-molestias.pdf");
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
