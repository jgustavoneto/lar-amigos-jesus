package jsf.view;

import javax.faces.event.ActionEvent;

public interface CRUD {
	
	public void salvar(ActionEvent event);
	public void alterar(ActionEvent event);
	public void consultar(ActionEvent event);
	
	public void novo(ActionEvent event);
	public void prepararAlteracao(ActionEvent event);
	public void removerSelecionados(ActionEvent event);

}
