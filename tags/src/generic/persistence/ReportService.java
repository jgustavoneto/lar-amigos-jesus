package generic.persistence;

import generic.report.Extensao;
import generic.report.FacadeReport;

import java.io.Serializable;
import java.util.Map;

import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.apache.myfaces.renderkit.html.util.AddResource;
import org.apache.myfaces.renderkit.html.util.AddResourceFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class ReportService implements Serializable {

    private static final long serialVersionUID = -4154822090445611695L;

    public static final String REPORT_TOKEN = "REPORT_DATA";
   
    private static int parameter = 1;
   
    private FacadeReport facadeReport;

    public void execute( Map<String, Object> parameters, String report, Extensao formato  )  {
       
        FacesContext faces = FacesContext.getCurrentInstance();
        Map<String, Object> session = faces.getExternalContext() .getSessionMap();
       
        byte[] data = null;
           
        if( formato.equals( Extensao.PDF ) ){
            data = facadeReport.executePdf( report, parameters );
        }else if( formato.equals( Extensao.HTML ) ){
            ServletContext ctx = ((HttpServletRequest)faces.getExternalContext().getRequest()).getSession(false).getServletContext();
            //Imagens ficarão no diretório raiz da aplicação
            String dest = ctx.getRealPath("/");
            data = facadeReport.executeHtml( report, "report_" + parameter, dest, parameters );
        }else if( formato.equals( Extensao.XLS ) ){
            data = facadeReport.executeXls(report, parameters);
        }else if( formato.equals( Extensao.ODT ) ){
            data = facadeReport.executeODT(report, parameters);
        }         

        session.put(ReportService.REPORT_TOKEN, data);

        montaPopup( formato );
    }
   
    private void montaPopup( Extensao formato ) {

        FacesContext faces = FacesContext.getCurrentInstance();

        final String actionUrl = faces.getExternalContext().getRequestContextPath() + "/writerReport?formato="+ formato.name().toLowerCase() + "&parameter=" + (parameter++ % 100000);

        /*       
          final String javascriptText = "window.open('"+ actionUrl +
          "', '_blank', 'dependent=yes, menubar=no, toolbar=no, location=no, status=no, "
          + "scrollbars=yes, resizable=no, width=" + 900 + ", height=" +
          600 + ", titlebar=no');"; */

        final String js = "var janela;"
                + "janela=window.open('" + actionUrl
                + "','_blank','dependent=yes, menubar=no, toolbar=no, location=no, status=no, scrollbars=yes, resizable=yes');"
                + "janela.resizeTo(window.screen.availWidth,window.screen.availHeight);  "
                + "janela.focus();";

        AddResource addResources = AddResourceFactory.getInstance(faces);

        addResources.addInlineScriptAtPosition(faces, AddResource.HEADER_BEGIN, js);
    }

    public FacadeReport getFacadeReport() {
        return facadeReport;
    }

    @Autowired
    public void setFacadeReport(FacadeReport facadeReport) {
        this.facadeReport = facadeReport;
    }
}
