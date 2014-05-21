package generic.report;

import generic.conn.DataSourcePostgress;

import java.util.HashMap;

import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.servlet.ServletContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;

public class Report {

	private static String nomeJasper;
	
	private static String pathLogo;

	public Report(String jasper) {
		nomeJasper = jasper;
	}

	@SuppressWarnings("unchecked")
	public void print(ActionEvent event, HashMap parametros, String pdf)
			throws Exception {

		// List<Pedido> list = dao.listarTudo(Pedido.class);

		// parametros
		// Map parametros = new HashMap();
		parametros
				.put("REPORT_CONNECTION", DataSourcePostgress.getConnection());

		try {

			// JRBeanCollectionDataSource ds = new
			// JRBeanCollectionDataSource(list);

			JasperPrint impressao = JasperFillManager.fillReport(getPath(),
					parametros);
			// JasperFillManager.fillReport

			if (impressao != null) {
				byte[] bytes = JasperExportManager.exportReportToPdf(impressao);
				byte[] arquivo = bytes;

				download(arquivo, pdf);
			}

		} catch (Exception e) {
			System.out.println(e.getMessage());
		}

	}

	// Devolve o path padrao dos reports
	public static String getPath() {
		ServletContext servletContext = (ServletContext) getFacesContext()
				.getExternalContext().getContext();
		return servletContext.getRealPath("WEB-INF/relatorios/"+nomeJasper);
	}

	public static String getPath(String path) {
		ServletContext servletContext = (ServletContext) getFacesContext()
				.getExternalContext().getContext();
		return servletContext.getRealPath(path);
	}
	
	// obtem o faces context da aplicacao
	public static FacesContext getFacesContext() {
		return FacesContext.getCurrentInstance();
	}

	public static void download(byte[] arquivo, String nome) throws Exception {
		HttpServletResponse response = getServletResponse();
		response.setContentType("application/pdf");
		response.setContentLength(arquivo.length);
		response.addHeader("Content-Disposition", "attachment; filename="
				+ "\"" + nome + "\"");
		ServletOutputStream outputStream = response.getOutputStream();
		outputStream.write(arquivo, 0, arquivo.length);
		outputStream.flush();
		outputStream.close();
		getFacesContext().responseComplete();
	}

	// Devolve o response para o download
	public static HttpServletResponse getServletResponse() {
		return (HttpServletResponse) getFacesContext().getExternalContext()
				.getResponse();
	}

	public static void setPathLogo(String pathLogo) {
		Report.pathLogo = pathLogo;
	}

	public static String getPathLogo() {
		return pathLogo;
	}
}
