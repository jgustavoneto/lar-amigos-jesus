package jsf.view;

import javax.faces.event.ActionEvent;

public class GenericBean {
	
	private Boolean consultando = true;

	private Boolean criando = false;

	private Boolean alterando = false;
	
	@SuppressWarnings("unused")
	private Boolean criandoOuAlterando = false;
	
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

	public void cancelar(ActionEvent evt) {
		setCriando(false);
		setAlterando(false);
		setConsultando(true);
	}

	public void novo(ActionEvent evt) {
		setCriando(true);
		setConsultando(false);
	}

}
