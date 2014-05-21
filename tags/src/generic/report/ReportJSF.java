package generic.report;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;

import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * Responsável pela geração de relatórios para managed beans.
 * @author samuelsoares
 *
 */
public class ReportJSF {

	/**
	 * Gera relatórios para páginas jsf.
	 * @param parameters Parâmetros para o relatório.
	 * @param pathReport Caminho do .jasper do relatório.
	 * @param name Nome desejado para o relatório.
	 * @param format Formato para geração do relatório: pdf, html ou xls.
	 * @throws IOException
	 */
	public static void gerarRelatorio( Map<String, Object> parameters, String pathReport, String name, String format ) throws IOException{
		
		HttpServletResponse resp = (HttpServletResponse)FacesContext.getCurrentInstance().getExternalContext().getResponse();
		HttpServletRequest req = (HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest();;
		OutputStream out        =  (OutputStream) resp.getOutputStream();

		FacadeReport facadeReport = new FacadeReport();
		
		if( format.equals( "pdf" ) ){
			resp.setHeader("Content-Disposition"," inline; filename=" + name + ".pdf");
			resp.setContentType("application/pdf");
			facadeReport.executePdf( out, pathReport, parameters );
		}else if( format.equals( "html" ) ){
			resp.setHeader("Content-Disposition"," inline; filename=" + name + ".html");
			resp.setContentType("text/html");
			ServletContext ctx = req.getSession(false).getServletContext();
			//Imagens ficarão no diretório raiz da aplicação 
			String dest = ctx.getRealPath("/");
			facadeReport.executeHtml( out, pathReport, name, dest, parameters );
		}else if( format.equals( "xls" ) ){
			resp.setHeader("Content-Disposition"," inline; filename=" + name + ".xls");
			resp.setContentType("application/vnd.ms-excel");
			facadeReport.executeXls( out, pathReport, parameters );
		}
		
		FacesContext.getCurrentInstance().renderResponse();
		FacesContext.getCurrentInstance().responseComplete();
	}
}
