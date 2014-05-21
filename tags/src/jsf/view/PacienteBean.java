package jsf.view;

import generic.conn.DataSourcePostgress;
import generic.report.Report;
import generic.report.ReportJSF;
import generic.utils.FacesUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import javax.faces.model.SelectItem;
import javax.servlet.ServletContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import persistence.service.LocalidadeService;
import persistence.service.LogradouroService;
import persistence.service.MolestiaPacienteService;
import persistence.service.MolestiaService;
import persistence.service.PacienteService;
import persistence.service.UFService;
import data.entity.Localidade;
import data.entity.Logradouro;
import data.entity.Molestia;
import data.entity.MolestiaPaciente;
import data.entity.Paciente;
import data.entity.UF;

@Component("pacienteBean")
@Scope("session")
public class PacienteBean {

	private Boolean procurandoLogradouro;

	public void destravar(ActionEvent event) {
		setProcurandoLogradouro(false);
	}

	public void travarEstadoCidade(ActionEvent event) {
		setProcurandoLogradouro(true);
	}

	private DataModel localidadesModal = new ListDataModel(
			new ArrayList<Localidade>());

	private Localidade localidade = new Localidade();

	private String msgAcolhidoOk;

	private String msgAcolhidoRepetido;

	private Molestia molestia = new Molestia();

	@SuppressWarnings("unused")
	private List<SelectItem> itens;

	public List<SelectItem> getItens() {

		List<SelectItem> result = molestiaService.getByCategoria(getMolestia());

		return result;
	}

	public void procurarLocalidadeNascimento(ActionEvent evt) {
		/*
		 * if(getPaciente().getLocalNascimento()!=null){
		 * localidade.setNome(getPaciente().getLocalNascimento().toUpperCase());
		 * } localidade.setSiglaEstado(getPaciente().getUfNascimento());
		 * List<Localidade> localidades = localidadeService.find(localidade); if
		 * (!localidades.isEmpty() && localidades.size() == 1) {
		 * getPaciente().setLocalNascimentoOk(true); } else {
		 * getPaciente().setLocalNascimentoOk(false); }
		 */
	}

	public void selecionarLocalidadeNascimento(ActionEvent evt) {
		getPaciente().setLocalNascimento(
				((Localidade) localidadesModal.getRowData()).getNome());
	}

	@SuppressWarnings("unused")
	private List<SelectItem> molestias;

	public List<SelectItem> getMolestias() {

		// List<SelectItem> itens = new ArrayList<SelectItem>();
		List<SelectItem> itens = molestiaService.getByCategoria(molestia);

		return itens;
	}

	public void recarregarMolestias(ActionEvent event) {
		// @SuppressWarnings("unused")
		// List<SelectItem> result = molestiaService.getByCategoria(molestia);

	}

	public void preparaInclusaoMolestia(ActionEvent event) {
		setAdicionandoMolestia(true);
	}

	/**
	 * TESTES
	 * 
	 * @param event
	 */
	/*
	public void adicionarMolestia(ActionEvent event) {
		MolestiaPaciente molestia = new MolestiaPaciente();
		molestia.setCodigoPaciente(getPaciente().getId());
		molestia.setMolestia(getMolestia());

		try {
			molestia = molestiaPacienteService.salvar(molestia);
		} catch (Exception e) {
			e.printStackTrace();
		}

		getPaciente().getMolestias().add(molestia.getMolestia());
		// setAdicionandoMolestia(false);

		// adicionar molestiaPaciente e depois incluir na lista.

		setAdicionandoMolestia(false);
	}*/

	private List<Molestia> molestiasIncluidas = new ArrayList<Molestia>();
	private List<Molestia> molestiasRemovidas = new ArrayList<Molestia>();

	@SuppressWarnings("unchecked")
	public void incluirMolestia(ActionEvent event) {
		MolestiaPaciente molestia = new MolestiaPaciente();
		molestia.setCodigoPaciente(getPaciente().getId());

		// setMolestia(molestiaService.get(molestia));
		setMolestia(molestiaService.getById(getMolestia()));

		molestia.setMolestia(getMolestia());
		/**
		 * try { molestia = molestiaPacienteService.salvar(molestia); } catch
		 * (Exception e) { e.printStackTrace(); }
		 */
		// getMolestiasIncluidas().add(molestia.getMolestia());
		// getPaciente().getMolestias().add(molestia.getMolestia());
		((ArrayList<Molestia>) getMolestiasPaciente().getWrappedData())
				.add(molestia.getMolestia());
		setAdicionandoMolestia(false);

		// Se existir na lista de molestias que serão removidas, remover.
		Molestia removida = molestiaService.getById(molestia.getMolestia());
		molestiasRemovidas.remove(removida);
	}

	@SuppressWarnings( { "unchecked" })
	private void salvarAlteracoesMolestias() {
		// Adicionar moléstias caso não existam em banco
		List<Molestia> molestias = ((ArrayList<Molestia>) getMolestiasPaciente()
				.getWrappedData());

		try {
			for (Molestia m : molestias) {
				@SuppressWarnings("unused")
				Molestia existe = molestiaService.get(m);
				Boolean teste = molestiaService.existeEmBanco(getPaciente()
						.getId(), m.getId());
				if (teste == false) {
					MolestiaPaciente nova = new MolestiaPaciente();
					nova.setMolestia(m);
					nova.setCodigoPaciente(getPaciente().getId());
					// molestiaService.salvar(molestia);
					molestiaPacienteService.salvar(nova);
				}
			}

			for (Molestia molestia : molestiasRemovidas) {
				Molestia existe = molestiaService.get(molestia);
				if (existe != null) {
					// molestiaService.excluir(molestia);molestiaPacienteService
					MolestiaPaciente molestiaPaciente = new MolestiaPaciente();
					molestiaPaciente.setCodigoPaciente(getPaciente().getId());
					molestiaPaciente.setMolestia(existe);

					molestiaPaciente = molestiaPacienteService
							.get(molestiaPaciente);

					molestiaPacienteService.excluir(molestiaPaciente);
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		molestiasRemovidas = new ArrayList<Molestia>();
		setAdicionandoMolestia(false);
		// Remover moléstias caso exista em banco
	}

	@SuppressWarnings("unchecked")
	public void removerMolestia(ActionEvent event) {
		Molestia molestia = (Molestia) molestiasPaciente.getRowData();
		((ArrayList<Molestia>) getMolestiasPaciente().getWrappedData())
				.remove(molestia);

		molestia = molestiaService.get(molestia);

		molestiasRemovidas.add(molestia);
	}

	public List<Molestia> getMolestiasIncluidas() {
		return molestiasIncluidas;
	}

	public void setMolestiasIncluidas(List<Molestia> molestiasIncluidas) {
		this.molestiasIncluidas = molestiasIncluidas;
	}

	public void cancelarInclusaoMolestia(ActionEvent event) {
		setMolestia(new Molestia());
		setAdicionandoMolestia(false);
	}

	private Boolean adicionandoMolestia = false;

	private String mensagem = "";

	private String erro = "";

	private Integer phaseControl = 0;

	private Report fichaPaciente = new Report("ficha-paciente.jasper");

	private Boolean consultando = true;

	private Boolean criando = false;

	private Boolean alterando = false;

	private Paciente paciente = new Paciente();

	private DataModel molestiasPaciente = new ListDataModel(
			new ArrayList<Molestia>());

	private Paciente selecionado = new Paciente();

	@SuppressWarnings("unused")
	private Boolean criandoOuAlterando = false;

	private DataModel pacientes = new ListDataModel(new ArrayList<Paciente>());
	private DataModel selecionados = new ListDataModel(
			new ArrayList<Paciente>());

	private PacienteService pacienteService;

	private UFService ufService;

	private LocalidadeService localidadeService;

	private LogradouroService logradouroService;

	private MolestiaPacienteService molestiaPacienteService;

	private MolestiaService molestiaService;

	public void atualizarEstado(ActionEvent evt) {

		try {
			UF filtroUF = new UF();
			filtroUF.setId(getPaciente().getUf());

			List<UF> result = ufService.find(filtroUF);

			if (!result.isEmpty()) {
				paciente.setEstado(result.get(0).getSigla());
			}
		} catch (Exception e) {

		}

	}

	public void atualizarEstadoNascimento(ActionEvent evt) {

		UF filtroUF = new UF();
		filtroUF.setId(getPaciente().getUfNascimento());

		List<UF> result = ufService.find(filtroUF);

		if (!result.isEmpty()) {
			paciente.setEstadoNascimento(result.get(0).getSigla());
		}

	}

	public List<SelectItem> getLocalidades() {
		
		/*

		List<SelectItem> itens = new ArrayList<SelectItem>();

		Localidade filtro = new Localidade();

		UF uf = new UF();
		uf.setId(getPaciente().getUf());
		uf = ufService.get(uf);

		if (uf != null) {

			filtro.setSiglaEstado(uf.getSigla());
			filtro = localidadeService.get(filtro);

			List<Localidade> result = localidadeService.findByUF(filtro);

			for (Localidade localidade : result) {
				if (localidade.getSiglaEstado().equalsIgnoreCase(
						getPaciente().getEstado())
						| getPaciente().getEstado() == null
						| "".equals(getPaciente().getEstado()))
					itens.add(new SelectItem(localidade.getId(), localidade
							.getNome()));
			}

		}

		return itens;
		
		*/
		
		List<SelectItem> itens = new ArrayList<SelectItem>();

		// Obtem o UF nascimento.
		UF uf = new UF();
		uf.setId(getPaciente().getUf());
		uf = ufService.get(uf);

		if (uf != null) {

			Localidade localidade = new Localidade();
			localidade.setSiglaEstado(uf.getSigla());
			List<Localidade> localidades = localidadeService
					.findByUF(localidade);

			for (Localidade itemLocalidade : localidades) {
				itens.add(new SelectItem(itemLocalidade.getId(), itemLocalidade
						.getNome()));
			}
		}
		return itens;
	}

	public List<SelectItem> getLocalidadesNascimento() {

		List<SelectItem> itens = new ArrayList<SelectItem>();

		// Obtem o UF nascimento.
		UF uf = new UF();
		uf.setId(getPaciente().getUfNascimento());
		uf = ufService.get(uf);

		if (uf != null) {

			Localidade localidade = new Localidade();
			localidade.setSiglaEstado(uf.getSigla());
			List<Localidade> localidades = localidadeService
					.findByUF(localidade);

			for (Localidade itemLocalidade : localidades) {
				itens.add(new SelectItem(itemLocalidade.getId(), itemLocalidade
						.getNome()));
			}
		}
		return itens;
	}

	public String iniciarGerenciamento() {
		setMsgAcolhidoOk("");
		setMsgAcolhidoRepetido("");
		setErro("");
		setMensagem("");
		setPaciente(new Paciente());
		getPacientes().setWrappedData(new ArrayList<Paciente>());
		setCriando(false);
		setAlterando(false);
		setConsultando(true);

		setAdicionandoMolestia(false);

		return "tela-pacientes";
	}

	public void procurarLogradouro(ActionEvent evt) {

		if (getPaciente().getCep() != null && !"".equals(paciente.getCep())) {
			List<Logradouro> ruas = logradouroService.findByCEP(getPaciente()
					.getCepSemFormatacao());

			if (!ruas.isEmpty()) {
				getPaciente().setRua(ruas.get(0).getNome());
				getPaciente().setLogradouro(ruas.get(0).getId());
			}
		}

		if (getPaciente().getRua() != null && !"".equals(paciente.getRua())) {
			List<Logradouro> ruas = logradouroService
					.findByLogradouro(getPaciente().getRua().trim()
							.toUpperCase());

			if (!ruas.isEmpty()) {
				getPaciente().setRua(ruas.get(0).getNome());
				getPaciente().setLogradouro(ruas.get(0).getId());
				getPaciente().setCep(ruas.get(0).getCep());

				UF uf = new UF();
				uf.setSigla(ruas.get(0).getEstado());
				uf = ufService.getBySigla(uf);
				getPaciente().setUf(uf.getId());

				Localidade localidade = new Localidade();
				localidade.setId(ruas.get(0).getLocalidade());
				localidade = localidadeService.get(localidade);
				getPaciente().setLocalidade(localidade.getId());

			}
		}

	}

	public void atualizarLocalidade(ActionEvent evt) {
		Localidade localidade = new Localidade();
		localidade.setId(getPaciente().getLocalidade());
		List<Localidade> localidades = localidadeService.find(localidade);

		if (!localidades.isEmpty()) {
			getPaciente().setCidade(localidades.get(0).getNome());
		}
	}

	public void atualizarLocalidadeNascimento(ActionEvent evt) {
		Localidade localidade = new Localidade();
		localidade.setId(getPaciente().getLocalidadeNascimento());
		List<Localidade> localidades = localidadeService.find(localidade);

		if (!localidades.isEmpty()) {
			getPaciente().setLocalNascimento(localidades.get(0).getNome());
		}
	}

	public void prepararAlteracao(ActionEvent event) {
		setMsgAcolhidoOk("");
		setMsgAcolhidoRepetido("");
		setErro("");
		setMensagem("");
		setCriando(false);
		setAlterando(true);
		setConsultando(false);
		setAdicionandoMolestia(false);
		setPaciente((Paciente) pacientes.getRowData());
		carregarMolestias(event);
	}

	public void novo(ActionEvent evt) {
		setMsgAcolhidoOk("");
		setMsgAcolhidoRepetido("");
		setErro("");
		setMensagem("");
		setPhaseControl(0);
		setPaciente(new Paciente());
		setCriando(true);
		setConsultando(false);
	}

	public void cancelar(ActionEvent evt) {
		setMsgAcolhidoOk("");
		setMsgAcolhidoRepetido("");
		setErro("");
		setMensagem("");
		setPaciente(new Paciente());
		setCriando(false);
		setAlterando(false);
		setConsultando(true);
	}

	public void limpar(ActionEvent evt) {
		setMsgAcolhidoOk("");
		setMsgAcolhidoRepetido("");
		setErro("");
		setMensagem("");
		setPaciente(new Paciente());
		getPacientes().setWrappedData(new ArrayList<Paciente>());
	}

	@Autowired
	public void setPacienteService(PacienteService pacienteService) {
		this.pacienteService = pacienteService;
	}

	public PacienteService getPacienteService() {
		return pacienteService;
	}

	public void setPacientes(DataModel pacientes) {
		this.pacientes = pacientes;
	}

	public DataModel getPacientes() {
		// pacientes.setWrappedData(pacienteService.listar());
		return pacientes;
	}

	public void setPaciente(Paciente paciente) {
		this.paciente = paciente;
	}

	public Paciente getPaciente() {
		return paciente;
	}

	public void consultar(ActionEvent evt) {
		if (paciente.getNome() != null) {
			paciente.setNome(paciente.getNome().toUpperCase());
		}
		if (paciente.getMatricula() == 0) {
			paciente.setMatricula(null);
		}
		List<Paciente> result = pacienteService.procurar(paciente);
		pacientes.setWrappedData(result);

		if (result.isEmpty()) {

			FacesUtils
					.sendWarningMessage("Nenhum paciente/residente foi encontrado com esse nome!");

		}
	}

	public void verificarMatricula(ActionEvent evt) {
		if (pacienteService.jaExisteMatricula(paciente.getMatricula())) {
			setErro("* Matrícula já cadastrada no sistema!");
			setMensagem("");
		} else {
			setMensagem("* Matrícula válida");
			setErro("");
		}
	}

	public void verificarNomeAcolhido(ActionEvent evt) {
		if (pacienteService.jaExisteNome(paciente)) {
			setMsgAcolhidoRepetido("* Nome do acolhido já consta no sistema!");
			setMsgAcolhidoOk("");
		} else {
			setMsgAcolhidoOk("* OK");
			setMsgAcolhidoRepetido("");
		}
	}

	// ---- cadastro ---- //
	@SuppressWarnings("unchecked")
	public void salvar(ActionEvent event) {

		try {
			paciente = pacienteService.salvar(paciente);

			((ArrayList<Paciente>) pacientes.getWrappedData()).add(paciente);
			setPaciente(new Paciente());

			setConsultando(true);
			setAlterando(false);
			setCriando(false);

			FacesUtils.sendInfoMessage("Operação realizada com sucesso!");
		} catch (Exception e) {
			FacesUtils
					.sendInfoMessage("Ocorreu um erro ao cadastrar paciente, verifique se a matrícula está duplicada.");
		}

	}

	@SuppressWarnings("unchecked")
	public String salvar() {

		try {
			paciente = pacienteService.salvar(paciente);
			((ArrayList<Paciente>) pacientes.getWrappedData()).add(paciente);
			setPaciente(new Paciente());

			setConsultando(true);
			setAlterando(false);
			setCriando(false);

			FacesUtils.sendInfoMessage("Operação realizada com sucesso!");
		} catch (Exception e) {
			FacesUtils
					.sendInfoMessage("Ocorreu um erro ao cadastrar paciente, verifique se a matrícula está duplicada.");
		}

		limparMensagens();

		return null;
	}

	private void limparMensagens() {
		setMsgAcolhidoOk("");
		setMsgAcolhidoRepetido("");
		setErro("");
		setMensagem("");
	}

	public void carregarMolestias(ActionEvent event) {
		List<Molestia> molestias = molestiaService
				.getMolestiasPorAcolhido(getPaciente().getId());
		// getPaciente().setMolestias(molestias);
		getMolestiasPaciente().setWrappedData(molestias);
	}

	@SuppressWarnings("unchecked")
	public void alterar(ActionEvent event) {
		
		int index = ((ArrayList<Paciente>) pacientes.getWrappedData())
				.indexOf(paciente);
		paciente.setLogradouro(null);
		//paciente = pacienteService.editar(paciente);
		
		try {
			//paciente.setMolestia(null);
			paciente = pacienteService.salvar(paciente);
		} catch (Exception e) {
			paciente = pacienteService.editar(paciente);
		}
		
		((ArrayList<Paciente>) pacientes.getWrappedData()).set(index, paciente);

	
		salvarAlteracoesMolestias();
		FacesUtils.sendInfoMessage("Alteração efetuada com sucesso!");

	}

	@SuppressWarnings("unchecked")
	public void select(ValueChangeEvent evt) {

		// Obtém selecionado
		Paciente selected = (Paciente) pacientes.getRowData();

		if (evt.getNewValue().equals(true)) {
			((ArrayList<Paciente>) selecionados.getWrappedData()).add(selected);
		} else {
			((ArrayList<Paciente>) selecionados.getWrappedData())
					.remove(selected);
		}

	}

	@SuppressWarnings("unchecked")
	public void selecionar(ActionEvent evt) {

		Paciente selecionado = (Paciente) pacientes.getRowData();

		if (selecionado.getSelecionado()) {
			((ArrayList<Paciente>) selecionados.getWrappedData())
					.add(selecionado);
		} else {
			((ArrayList<Paciente>) selecionados.getWrappedData())
					.remove(selecionado);
		}

	}

	@SuppressWarnings("unchecked")
	public void removerSelecionados(ActionEvent event) {
		pacienteService.excluirTodos(((ArrayList<Paciente>) selecionados
				.getWrappedData()));

		List<Paciente> itens = ((ArrayList<Paciente>) selecionados
				.getWrappedData());

		for (Paciente obj : itens) {
			((ArrayList<Paciente>) pacientes.getWrappedData()).remove(obj);
		}

		selecionados.setWrappedData(new ArrayList<Paciente>());
		FacesUtils.sendInfoMessage("Remoção efetuada com sucesso");
	}

	public void cancelarRemocao(ActionEvent evt) {
		selecionados.setWrappedData(new ArrayList<Paciente>());
	}

	public void setSelecionados(DataModel selecionados) {
		this.selecionados = selecionados;
	}

	public DataModel getSelecionados() {
		return selecionados;
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

	public void setCriandoOuAlterando(Boolean criandoOuAlterando) {
		this.criandoOuAlterando = criandoOuAlterando;
	}

	public Boolean getCriandoOuAlterando() {
		return (criando || alterando);
	}

	public void setSelecionado(Paciente selecionado) {
		this.selecionado = selecionado;
	}

	public Paciente getSelecionado() {
		return selecionado;
	}

	private String name = "/WEB-INF/relatorios/ficha-paciente.jasper";

	public void emitirRelatorio(ActionEvent event) {
		try {
			FacesContext context = FacesContext.getCurrentInstance();
			ServletContext sc = (ServletContext) context.getExternalContext()
					.getContext();
			Map<String, Object> parameters = new HashMap<String, Object>();

			parameters.put("id_paciente", ((Paciente) pacientes.getRowData())
					.getId());
			// parameters.put("BRASAO", sc.getRealPath("/img/brasao.jpg"));
			parameters.put("SUBREPORT_DIR", sc
					.getRealPath("/WEB-INF/relatorios/"));
			parameters.put("REPORT_CONNECTION", DataSourcePostgress
					.getConnection());
			ReportJSF.gerarRelatorio(parameters, sc.getRealPath(name),
					"Relatorio", "pdf");
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@SuppressWarnings( { "unchecked", "static-access" })
	public void print(ActionEvent event) {

		// fichaPaciente.setPathLogo();
		fichaPaciente.getPath("WEB-INF/relatorios/img/larlogo.jpg");

		FacesContext context = FacesContext.getCurrentInstance();

		ServletContext sc = (ServletContext) context.getExternalContext()
				.getContext();

		@SuppressWarnings("unused")
		HashMap map = new HashMap();
		// map.put("id_paciente", 6);

		HashMap parameters = new HashMap<String, Object>();

		parameters.put("id_paciente", ((Paciente) pacientes.getRowData())
				.getId());
		parameters.put("img", fichaPaciente
				.getPath("WEB-INF/relatorios/img/larlogo-old.jpg"));//larlogo
		parameters.put("SUBREPORT_DIR", fichaPaciente
				.getPath("WEB-INF/relatorios/"));
		// parameters.put("BRASAO", sc.getRealPath("/img/brasao.jpg"));
		// parameters.put("SUBREPORT_DIR", sc
		// .getRealPath("/WEB-INF/relatorios/"));

		try {
			fichaPaciente.print(event, parameters, "ficha-paciente.pdf");
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Autowired
	public void setUfService(UFService ufService) {
		this.ufService = ufService;
	}

	public UFService getUfService() {
		return ufService;
	}

	@Autowired
	public void setLocalidadeService(LocalidadeService localidadeService) {
		this.localidadeService = localidadeService;
	}

	public LocalidadeService getLocalidadeService() {
		return localidadeService;
	}

	@Autowired
	public void setLogradouroService(LogradouroService logradouroService) {
		this.logradouroService = logradouroService;
	}

	public LogradouroService getLogradouroService() {
		return logradouroService;
	}

	public void setPhaseControl(Integer phaseControl) {
		this.phaseControl = phaseControl;
	}

	public Integer getPhaseControl() {
		return phaseControl;
	}

	public void setMensagem(String mensagem) {
		this.mensagem = mensagem;
	}

	public String getMensagem() {
		return mensagem;
	}

	public void setErro(String erro) {
		this.erro = erro;
	}

	public String getErro() {
		return erro;
	}

	@Autowired
	public void setMolestiaPacienteService(
			MolestiaPacienteService molestiaPacienteService) {
		this.molestiaPacienteService = molestiaPacienteService;
	}

	public MolestiaPacienteService getMolestiaPacienteService() {
		return molestiaPacienteService;
	}

	public void setAdicionandoMolestia(Boolean adicionandoMolestia) {
		this.adicionandoMolestia = adicionandoMolestia;
	}

	public Boolean getAdicionandoMolestia() {
		return adicionandoMolestia;
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

	public void setMolestias(List<SelectItem> molestias) {
		this.molestias = molestias;
	}

	public void setMolestiasPaciente(DataModel molestiasPaciente) {
		this.molestiasPaciente = molestiasPaciente;
	}

	public DataModel getMolestiasPaciente() {
		return molestiasPaciente;
	}

	public void setMolestiasRemovidas(List<Molestia> molestiasRemovidas) {
		this.molestiasRemovidas = molestiasRemovidas;
	}

	public List<Molestia> getMolestiasRemovidas() {
		return molestiasRemovidas;
	}

	public void setMsgAcolhidoOk(String msgAcolhidoOk) {
		this.msgAcolhidoOk = msgAcolhidoOk;
	}

	public String getMsgAcolhidoOk() {
		return msgAcolhidoOk;
	}

	public void setMsgAcolhidoRepetido(String msgAcolhidoRepetido) {
		this.msgAcolhidoRepetido = msgAcolhidoRepetido;
	}

	public String getMsgAcolhidoRepetido() {
		return msgAcolhidoRepetido;
	}

	public void setLocalidade(Localidade localidade) {
		this.localidade = localidade;
	}

	public Localidade getLocalidade() {
		return localidade;
	}

	public void setLocalidadesModal(DataModel localidadesModal) {
		this.localidadesModal = localidadesModal;
	}

	public DataModel getLocalidadesModal() {
		return localidadesModal;
	}

	public void setProcurandoLogradouro(Boolean procurandoLogradouro) {
		this.procurandoLogradouro = procurandoLogradouro;
	}

	public Boolean getProcurandoLogradouro() {
		return procurandoLogradouro;
	}

}
