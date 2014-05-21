package generic.report;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;

import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * Respons�vel pela gera��o de relat�rios para managed beans.
 * @author samuelsoares
 *
 */
public class ReportJSF {

	/**
	 * Gera relat�rios para p�ginas jsf.
	 * @param parameters Par�metros para o relat�rio.
	 * @param pathReport Caminho do .jasper do relat�rio.
	 * @param name Nome desejado para o relat�rio.
	 * @param format Formato para gera��o do relat�rio: pdf, html ou xls.
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
			//Imagens ficar�o no diret�rio raiz da aplica��o 
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
