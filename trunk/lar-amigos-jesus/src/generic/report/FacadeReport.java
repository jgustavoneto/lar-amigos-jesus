package generic.report;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Locale;
import java.util.Map;

import javax.annotation.Resource;
import javax.sql.DataSource;

import org.springframework.stereotype.Repository;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.JasperRunManager;
import net.sf.jasperreports.engine.export.JExcelApiExporter;
import net.sf.jasperreports.engine.export.JRHtmlExporter;
import net.sf.jasperreports.engine.export.JRHtmlExporterParameter;
import net.sf.jasperreports.engine.export.JRXlsAbstractExporterParameter;
import net.sf.jasperreports.engine.export.oasis.JROdtExporter;
import net.sf.jasperreports.engine.util.JRLoader;

/**
 * Fachada para gera��o de relat�rios
 * 
 * @author M�rcio Marconi, Samuel Soares
 * @since 04/06/2008
 * @version 1.0
 * @version 2.0, 06/01/2009
 * 
 */
@Repository
public class FacadeReport {

	@Resource
	private DataSource dataSource;

	private Connection conexao;

	/**
	 * Gera um relat�rio e retorna seus bytes.
	 * 
	 * @param report
	 *            Caminho completo do relat�rio a ser executado.
	 * @param parameters
	 *            Parametros do relat�rio.
	 * @return Stream contendo o relat�rio gerado.
	 */
	public byte[] executePdf(String report, Map<String, Object> parameters) {

		if (report == null)
			throw new IllegalArgumentException(
					"Defina um relat�rio para ser utilizado pelo FacadeReport");

		setConnection(parameters);

		Object jrData = parameters.get("JR_DATA_SOURCE");

		byte[] stream = null;

		try {
			JasperReport relatorio = (JasperReport) JRLoader.loadObject(report);
			parameters.put("REPORT_LOCALE", new Locale("pt", "BR"));

			if (jrData != null && jrData instanceof JRDataSource) {// Verifica
																	// se foi
																	// passado
																	// uma lista
																	// para
																	// preencher
																	// o
																	// relat�rio,
																	// em vez de
																	// preencher
																	// via sql
				stream = JasperRunManager.runReportToPdf(relatorio, parameters,
						(JRDataSource) parameters.get("JR_DATA_SOURCE"));

				return stream;
			} else {
				stream = JasperRunManager.runReportToPdf(relatorio, parameters,
						conexao);
				return stream;
			}
		} catch (Exception e) {
			throw new IllegalArgumentException(
					"Ocorreu um erro na tentativa de gerar o relat�rio", e);
		} finally {
			if (conexao != null) {
				try {
					if (!conexao.isClosed())
						conexao.close();
				} catch (Exception e) {
					throw new RuntimeException(
							"Ocorreu um erro na tentativa de fechar a conex�o.");
				}
			}
		}
	}

	/**
	 * Gera um relat�rio e escreve seu conte�do em um stream.
	 * 
	 * @param out
	 *            Stream onde o relat�rio ser� escrito.
	 * @param report
	 *            Caminho completo do relat�rio a ser executado.
	 * @param parameters
	 *            Par�metros do relat�rio.
	 */
	public void executePdf(OutputStream out, String report,
			Map<String, Object> parameters) {
		byte[] data = this.executePdf(report, parameters);
		if (data == null || data.length == 0)
			return;

		try {
			out.write(data, 0, data.length);
			out.flush();
			out.close();
		} catch (IOException e) {
			throw new RuntimeException(
					"Ocorreu um erro na tentativa de escrita do relat�rio no stream de sa�da");
		}
	}

	/**
	 * Gera um relat�rio em formato html, salva suas imagens em disco e rotorna
	 * seus bytes.
	 * 
	 * @param report
	 *            Relat�rio a ser executado.
	 * @param nomeRelatorio
	 *            Nome do relat�rio a ser executado para criar pasta de suas
	 *            imagens.
	 * @param dest
	 *            Caminho completo da pasta onde o relat�rio ser� salvo.
	 * @param parameters
	 *            Par�metros do relat�rio.
	 * @return Stream contendo o relat�rio gerado.
	 */
	protected byte[] runReportToHtml(JasperReport source, String nomeRelatorio,
			String dest, Map<String, Object> parameters) throws Exception {

		JasperPrint jasperPrint = null;

		// Verifica se foi passado uma lista para preencher o relat�rio, em vez
		// de preencher via sql
		if (parameters.get("JR_DATA_SOURCE") != null
				&& (parameters.get("JR_DATA_SOURCE") instanceof JRDataSource)) {
			jasperPrint = JasperFillManager.fillReport(source, parameters,
					(JRDataSource) parameters.get("JR_DATA_SOURCE"));
		} else
			jasperPrint = JasperFillManager.fillReport(source, parameters,
					conexao);

		ByteArrayOutputStream baos = new ByteArrayOutputStream();

		JRHtmlExporter exporter = new JRHtmlExporter();

		exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
		exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, baos);

		File file = new File(dest + "/" + nomeRelatorio + "_imagens");// Pasta
																		// onde
																		// ficam
																		// as
																		// imagens
																		// do
																		// relat�rio
		exporter.setParameter(JRHtmlExporterParameter.IMAGES_DIR, file);
		exporter.setParameter(JRHtmlExporterParameter.IS_OUTPUT_IMAGES_TO_DIR,
				true);

		// O caminho do arquivo px ser� [dest]/[nomeRelatorio]_imagens/px
		String img = nomeRelatorio + "_imagens/";
		exporter.setParameter(JRHtmlExporterParameter.IMAGES_URI, img);
		exporter.exportReport();

		return baos.toByteArray();
	}

	/**
	 * Gera um relat�rio em formato html, salva suas imagens e retorna seus
	 * bytes.
	 * 
	 * @param report
	 *            Relat�rio a ser executado.
	 * @param nomeRelatorio
	 *            Nome do relat�rio a ser executado para criar pasta de suas
	 *            imagens.
	 * @param dest
	 *            Caminho completo da pasta onde as imagens do relat�rio ser�o
	 *            salvas.
	 * @param parameters
	 *            Par�metros do relat�rio.
	 * @return Stream contendo o relat�rio gerado.
	 */
	public byte[] executeHtml(String report, String nomeRelatorio, String dest,
			Map<String, Object> parameters) {

		if (report == null)
			throw new IllegalArgumentException(
					"Defina um relat�rio para ser utilizado pelo FacadeReport");

		if (nomeRelatorio == null)
			throw new IllegalArgumentException(
					"Defina um nome de relat�rio para ser utilizado pelo FacadeReport");

		if (dest == null)
			throw new IllegalArgumentException(
					"Defina um destino de relat�rio para ser utilizado pelo FacadeReport");

		setConnection(parameters);

		byte[] stream = null;

		try {
			JasperReport relatorio = (JasperReport) JRLoader.loadObject(report);
			parameters.put("REPORT_LOCALE", new Locale("pt", "BR"));
			stream = this.runReportToHtml(relatorio, nomeRelatorio, dest,
					parameters);
			return stream;

		} catch (Exception e) {
			throw new IllegalArgumentException(
					"Ocorreu um erro na tentativa de gerar o relat�rio", e);
		} finally {
			if (conexao != null) {
				try {
					if (!conexao.isClosed())
						conexao.close();
				} catch (Exception e) {
					throw new RuntimeException(
							"Ocorreu um erro na tentativa de fechar a conex�o.");
				}
			}
		}
	}

	/**
	 * Gera um relat�rio e escreve seu conte�do em um stream.
	 * 
	 * @param out
	 *            Stream onde o relat�rio ser� escrito.
	 * @param report
	 *            Caminho completo do relat�rio a ser executado.
	 * @param dest
	 *            Caminho completo da pasta onde as imagens do relat�rio ser�o
	 *            salvas.
	 * @param nomeRelatorio
	 *            nome do relat�rio a ser executado para criar pasta de suas
	 *            imagens.
	 * @param parameters
	 *            Par�metros do relat�rio.
	 */
	public void executeHtml(OutputStream out, String report,
			String nomeRelatorio, String dest, Map<String, Object> parameters) {
		byte[] data = this.executeHtml(report, nomeRelatorio, dest, parameters);
		if (data == null || data.length == 0)
			return;

		try {
			out.write(data, 0, data.length);
			out.flush();
			out.close();
		} catch (IOException e) {
			throw new RuntimeException(
					"Ocorreu um erro na tentativa de escrita do relat�rio no stream de sa�da");
		}
	}

	/**
	 * Gera um relat�rio e retorna seus bytes.
	 * 
	 * @param report
	 *            Caminho completo do relat�rio a ser executado.
	 * @param parameters
	 *            Parametros do relat�rio.
	 * @return Stream contendo o relat�rio gerado.
	 */
	public byte[] executeXls(String report, Map<String, Object> parameters) {

		if (report == null)
			throw new IllegalArgumentException(
					"Defina um relat�rio para ser utilizado pelo FacadeReport");

		setConnection(parameters);

		try {
			JasperPrint jasperPrint = null;

			JasperReport relatorio = (JasperReport) JRLoader.loadObject(report);

			// Verifica se foi passado uma lista para preencher o relat�rio, em
			// vez de preencher via sql
			if (parameters.get("JR_DATA_SOURCE") != null
					&& (parameters.get("JR_DATA_SOURCE") instanceof JRDataSource)) {
				jasperPrint = JasperFillManager.fillReport(relatorio,
						parameters, (JRDataSource) parameters
								.get("JR_DATA_SOURCE"));
			} else
				jasperPrint = JasperFillManager.fillReport(relatorio,
						parameters, conexao);

			ByteArrayOutputStream baos = new ByteArrayOutputStream();

			// JRXlsExporter exporter = new JRXlsExporter();
			JExcelApiExporter exporter = new JExcelApiExporter();

			exporter
					.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
			exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, baos);
			exporter.setParameter(
					JRXlsAbstractExporterParameter.IS_ONE_PAGE_PER_SHEET,
					Boolean.FALSE);

			exporter.exportReport();

			return baos.toByteArray();
		} catch (Exception e) {
			throw new IllegalArgumentException(
					"Ocorreu um erro na tentativa de gerar o relat�rio", e);
		} finally {
			if (conexao != null) {
				try {
					if (!conexao.isClosed())
						conexao.close();
				} catch (Exception e) {
					throw new RuntimeException(
							"Ocorreu um erro na tentativa de fechar a conex�o.");
				}
			}
		}
	}

	/**
	 * Gera um relat�rio e escreve seu conte�do em um stream.
	 * 
	 * @param out
	 *            Stream onde o relat�rio ser� escrito.
	 * @param report
	 *            Caminho completo do relat�rio a ser executado.
	 * @param parameters
	 *            Par�metros do relat�rio.
	 */
	public void executeXls(OutputStream out, String report,
			Map<String, Object> parameters) {
		byte[] data = this.executeXls(report, parameters);
		if (data == null || data.length == 0)
			return;

		try {
			out.write(data, 0, data.length);
			out.flush();
			out.close();
		} catch (IOException e) {
			throw new RuntimeException(
					"Ocorreu um erro na tentativa de escrita do relat�rio no stream de sa�da.");
		}
	}

	/**
	 * Gera um relat�rio e retorna seus bytes.
	 * 
	 * @param report
	 *            Caminho completo do relat�rio a ser executado.
	 * @param parameters
	 *            Parametros do relat�rio.
	 * @return Stream contendo o relat�rio gerado.
	 */
	public byte[] executeODT(String report, Map<String, Object> parameters) {

		if (report == null)
			throw new IllegalArgumentException(
					"Defina um relat�rio para ser utilizado pelo FacadeReport");

		setConnection(parameters);

		try {
			JasperPrint jasperPrint = null;

			JasperReport relatorio = (JasperReport) JRLoader.loadObject(report);

			// Verifica se foi passado uma lista para preencher o relat�rio, em
			// vez de preencher via sql
			if (parameters.get("JR_DATA_SOURCE") != null
					&& (parameters.get("JR_DATA_SOURCE") instanceof JRDataSource)) {
				jasperPrint = JasperFillManager.fillReport(relatorio,
						parameters, (JRDataSource) parameters
								.get("JR_DATA_SOURCE"));
			} else
				jasperPrint = JasperFillManager.fillReport(relatorio,
						parameters, conexao);

			ByteArrayOutputStream baos = new ByteArrayOutputStream();

			JROdtExporter exporter = new JROdtExporter();

			exporter
					.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
			exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, baos);

			exporter.exportReport();

			return baos.toByteArray();
		} catch (Exception e) {
			throw new IllegalArgumentException(
					"Ocorreu um erro na tentativa de gerar o relat�rio", e);
		} finally {
			if (conexao != null) {
				try {
					if (!conexao.isClosed())
						conexao.close();
				} catch (Exception e) {
					throw new RuntimeException(
							"Ocorreu um erro na tentativa de fechar a conex�o.");
				}
			}
		}
	}

	/**
	 * Gera um relat�rio e escreve seu conte�do em um stream.
	 * 
	 * @param out
	 *            Stream onde o relat�rio ser� escrito.
	 * @param report
	 *            Caminho completo do relat�rio a ser executado.
	 * @param parameters
	 *            Par�metros do relat�rio.
	 */
	public void executeODT(OutputStream out, String report,
			Map<String, Object> parameters) {
		byte[] data = this.executeODT(report, parameters);
		if (data == null || data.length == 0)
			return;

		try {
			out.write(data, 0, data.length);
			out.flush();
			out.close();
		} catch (IOException e) {
			throw new RuntimeException(
					"Ocorreu um erro na tentativa de escrita do relat�rio no stream de sa�da.");
		}
	}

	/**
	 * Verifica e configura fonte de dados. Mantendo compatibilidade com
	 * conex�es passadas por par�metro ou pelo spring
	 * 
	 * @param parameters
	 * @throws SQLException
	 */
	private void setConnection(Map<String, Object> parameters) {
		Object jrData = parameters.get("JR_DATA_SOURCE");
		boolean noDataSource = false;
		if (jrData == null || !(jrData instanceof JRDataSource)) {
			noDataSource = true;
		}

		if (parameters.get("CONEXAO") != null) {
			conexao = (Connection) parameters.get("CONEXAO");
			noDataSource = false;
		} else if (dataSource != null && jrData == null) {
			try {
				conexao = dataSource.getConnection();
				noDataSource = false;
			} catch (Exception e) {
				throw new IllegalArgumentException(
						"Ocorreu um erro na tentativa de abrir a conex�o para o relat�rio.",
						e);
			}
		}

		if (noDataSource) {
			throw new IllegalArgumentException(
					"Defina um data source para ser utilizado pelo FacadeReport");
		}
	}

	public DataSource getDataSource() {
		return dataSource;
	}

	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}
}
