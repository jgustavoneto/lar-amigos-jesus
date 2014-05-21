package jsf.view;

import generic.utils.DateUtils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import data.entity.Paciente;
import data.entity.Ponto;

import persistence.service.FrequenciaService;
import persistence.service.PacienteService;

@Component("frequenciaBean")
@Scope("session")
public class FrequenciaBean {

	@SuppressWarnings("unused")
	private Integer MAXIMO_GRID = 35;

	private Ponto ponto = new Ponto();

	private FrequenciaService frequenciaService;

	private PacienteService pacienteService;

	private DataModel frequencia = new ListDataModel(new ArrayList<Ponto>());
	private DataModel pacientes = new ListDataModel(new ArrayList<Paciente>());
	
	public void limpar(ActionEvent evt){
		ponto = new Ponto();
		frequencia.setWrappedData(new ArrayList<Ponto>());
		pacientes.setWrappedData(new ArrayList<Ponto>());
	}

	public void marcar(ActionEvent event) {

		Ponto novoPonto = (Ponto) frequencia.getRowData();

		if (novoPonto.getId() != null) {

			List<Ponto> l = frequenciaService.find(novoPonto);

			if (!l.isEmpty()) {
				novoPonto = l.get(0);
			}

			frequenciaService.excluir(novoPonto);
			// novoPonto.setPaciente(null);
			((Ponto) frequencia.getRowData()).setId(null);

		} else {
			// adiciona
			Calendar calendar = Calendar.getInstance();
			calendar.set(Calendar.DAY_OF_MONTH, novoPonto.getDia());

			if (ponto.getMes() != null) {
				if (ponto.getMes() != 0)
					calendar.set(Calendar.MONTH, ponto.getMes()-1 );
				else
					calendar.set(Calendar.MONTH, ponto.getMes()-1 );
			}

			if (ponto.getAno() != null) {
				calendar.set(Calendar.YEAR, ponto.getAno());
			}

			Date data = new Date();
			data.setTime(calendar.getTimeInMillis());
			data.setMonth(data.getMonth()+1);
			novoPonto.setData(data);
			
			novoPonto.setPaciente(ponto.getPaciente());
			//novoPonto.setMes(novoPonto.getMes()-1);

		//	novoPonto.setMes(data.getMonth() + 1);
		//	novoPonto.setAno((data.getYear() + 1900));

			try {
				novoPonto = frequenciaService.salvar(novoPonto);
			} catch (Exception e) {
				e.printStackTrace();
			}

			//((Ponto) frequencia.getRowData()).setAno(novoPonto.getAno());
			((Ponto) frequencia.getRowData()).setData(novoPonto.getData());
			((Ponto) frequencia.getRowData()).setDescricao(DateUtils
					.getDayOfWeek(novoPonto.getData()));
			((Ponto) frequencia.getRowData()).setId(novoPonto.getId());
			//((Ponto) frequencia.getRowData()).setMes(novoPonto.getMes());
			((Ponto) frequencia.getRowData()).setPaciente(novoPonto
					.getPaciente());
		}

	}

	
	public void marcar2(ActionEvent event){
		
	}
	
	public void consultar(ActionEvent event) {

		ponto.getPaciente()
				.setNome(ponto.getPaciente().getNome().toUpperCase());

		List<Paciente> result = pacienteService.procurar(ponto.getPaciente());

		if (result.isEmpty()) {
			FacesContext
					.getCurrentInstance()
					.addMessage(
							null,
							new FacesMessage(
									FacesMessage.SEVERITY_WARN,
									"Nenhum paciente/residente foi encontrado com esse nome!",
									null));
		} else if (result.size() > 1) {
			pacientes.setWrappedData(result);
		} else {
			pacientes.setWrappedData(result);
			ponto.setPaciente(result.get(0));

			if (ponto.getAno() == 0) { // bug catalina
				ponto.setAno(null);
			}
			if (ponto.getMes() == 0) {
				ponto.setMes(0);
			}
			
			Ponto aux = new Ponto();
			aux.setAno(ponto.getAno());
			aux.setPaciente(ponto.getPaciente());
			
			if(ponto.getMes()!=null){
				//ponto.setMes(ponto.getMes()+1);
				aux.setMes(ponto.getMes()+1);
			}
			
			List<Ponto> dias = frequenciaService.find(aux);
			List<Ponto> grid = montarGridFrequencia(dias);
			frequencia.setWrappedData(grid);
			
			ponto.setMes(ponto.getMes());
		}
	}

	@SuppressWarnings("deprecation")
	private List<Ponto> montarGridFrequencia(List<Ponto> dias) {
		List<Ponto> result = new ArrayList<Ponto>();

		Integer maximo = 0;
		Date primeiroDiaDesteMes = new Date();

		if (ponto.getMes() != null & ponto.getAno() != null) {
			Calendar c = Calendar.getInstance();
			c.set(Calendar.DATE, 1);
			c.set(Calendar.MONTH, ponto.getMes());
			c.set(Calendar.YEAR, ponto.getAno());
			primeiroDiaDesteMes = c.getTime();
			maximo = DateUtils.getDiasNoMes(c.getTime());
		} else {
			maximo = DateUtils.getDiasNesteMes();

			Calendar c = Calendar.getInstance();
			c.setTime(new Date());
			getPonto().setMes(c.get(Calendar.MONTH));
			getPonto().setAno(c.get(Calendar.YEAR));
		}
		// Integer diasCompletarMesPassado = MAXIMO_GRID - maximo;

		primeiroDiaDesteMes.setDate(1);
		Integer completarGrid = DateUtils
				.getDayOfWeekAsNumber(primeiroDiaDesteMes);
		Integer diasCompletarMesPassado = completarGrid - 1; // diminui o
																// domingo.

		// QUANTIDADE DIAS MES PASSADO e pega os ultimos dias.
		Integer qtdDiasMesPassado = DateUtils.getDiasNoMes(DateUtils
				.getLastMonth(primeiroDiaDesteMes));

		Integer DIF = (qtdDiasMesPassado - diasCompletarMesPassado) + 1;

		Date hoje = primeiroDiaDesteMes;
		hoje = DateUtils.getLastMonth(hoje);
		for (int i = DIF; i <= qtdDiasMesPassado; i++) {

			hoje.setDate(i);

			String descricao = DateUtils.getDayOfWeek(hoje);

			Ponto x = new Ponto();
			x.setDia(i);
			x.setDescricao(descricao);
			x.setEsteMes(false);
			// MAX_DIAS_MES_PASSADO--;
			result.add(x);
		}
		hoje = new Date();
		
		hoje.setDate(1);
		hoje.setMonth(getPonto().getMes());
		hoje.setYear(getPonto().getAno()-1900);
		
		for (int i = 1; i <= maximo; i++) {
			Ponto p = new Ponto();
			p.setDia(i);
			hoje.setDate(i);
			p.setDescricao(DateUtils.getDayOfWeek(hoje));
			for (Ponto ponto : dias) {
				Date data = ponto.getData();
				Calendar c = Calendar.getInstance();
				c.setTimeInMillis(data.getTime());
				if (c.get(Calendar.DAY_OF_MONTH) == i) {
					p.setAno(ponto.getAno());
					p.setData(ponto.getData());
					p.setId(ponto.getId());
					p.setMes(ponto.getMes());
					p.setPaciente(ponto.getPaciente());
				}
			}
			p.setEsteMes(true);
			if (p.getDia() == (DateUtils.getDayOfWeekAsNumber(new Date())))
				p.setHoje(true);
			result.add(p);

		}

		return result;

	}

	@Autowired
	public void setFrequenciaService(FrequenciaService frequenciaService) {
		this.frequenciaService = frequenciaService;
	}

	@Autowired
	public void setPacienteService(PacienteService pacienteService) {
		this.pacienteService = pacienteService;
	}

	public FrequenciaService getFrequenciaService() {
		return frequenciaService;
	}

	public void setFrequencia(DataModel frequencia) {
		this.frequencia = frequencia;
	}

	public DataModel getFrequencia() {
		return frequencia;
	}

	public void setPonto(Ponto ponto) {
		this.ponto = ponto;
	}

	public Ponto getPonto() {
		return ponto;
	}

	public void setPacientes(DataModel pacientes) {
		this.pacientes = pacientes;
	}

	public DataModel getPacientes() {
		return pacientes;
	}

	public PacienteService getPacienteService() {
		return pacienteService;
	}
	
	@SuppressWarnings("deprecation")
	public String iniciar(){
		getFrequencia().setWrappedData(new ArrayList<Ponto>());
		getPacientes().setWrappedData(new ArrayList<Paciente>());
		ponto.setPaciente(new Paciente());
		Calendar calendar = Calendar.getInstance();
		Date data = new Date();
		getPonto().setMes(data.getMonth());
		getPonto().setAno(calendar.get(Calendar.YEAR));
		return "tela-frequencia";
	}
	
	public void selecionarPaciente(ActionEvent event){
	
		ponto.setPaciente((Paciente)getPacientes().getRowData());
		Calendar calendar = Calendar.getInstance();
		
		ponto.setAno(calendar.get(Calendar.YEAR));
		ponto.setMes(calendar.get(Calendar.MONTH));
		
		Ponto filtroPonto = new Ponto();
		//Calendar c = Calendar.getInstance();
		filtroPonto.setMes(ponto.getMes()+1);
		filtroPonto.setAno(ponto.getAno());
		filtroPonto.setPaciente((Paciente)getPacientes().getRowData());
		
		List<Ponto> dias = frequenciaService.find(filtroPonto);
		
		//ponto.setMes(calendar.get(Calendar.MONTH));
		
		List<Ponto> grid = montarGridFrequencia(dias);
		frequencia.setWrappedData(grid);
	} // 0:27:08 a 0:31:35

}
