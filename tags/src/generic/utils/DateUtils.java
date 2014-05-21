package generic.utils;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import org.joda.time.DateTime;

public class DateUtils {

	public static Integer getDayOfWeekAsNumber(Date data) {
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(data.getTime());
		return cal.get(Calendar.DAY_OF_WEEK);
	}

	public static Integer getDiasNesteMes() {
		Calendar cal = Calendar.getInstance();
		return cal.getActualMaximum(Calendar.DAY_OF_MONTH);
	}

	@SuppressWarnings("deprecation")
	public static Integer getDiasNoMes(Date data) {
		
		/*
		 # Date dtInicio = //Data menor  
# Date dtFim = //Data maior  
# int dias;  
# for (dias = 0; dtFim.after(dyInicio); dias++) {  
#     dtFim.setDate(dtFim.getDate - 1);  
# }  
		 * */
		
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(data.getTime());
		cal.set(Calendar.DATE, 1);
		
		int mes = cal.get(Calendar.MONTH);
		int pMes = mes;
		int dia = 1;
		
		Date aux = new Date();
		aux = (Date) data.clone();
		aux.setDate(dia);
		
		while(pMes==mes){
			dia++;
			aux.setDate(dia);
			pMes = aux.getMonth();
		}
		
		return (dia-1);
	}

	public static String getDayOfWeek(Date data) {
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(data.getTime());

		Integer dia = cal.get(Calendar.DAY_OF_WEEK);

		if (dia == 0) {
			return "Domingo";
		}

		String aux = "";

		switch (dia) {
		case 1:
			aux = "D";
			break;

		case 2:
			aux = "S";
			break;

		case 3:
			aux = "T";
			break;

		case 4:
			aux = "Q";
			break;

		case 5:
			aux = "Q";
			break;

		case 6:
			aux = "S";
			break;

		case 7:
			aux = "S";
			break;

		}

		return aux;
	}

	public static Date getLastMonth(Date data) {
		
		Date aux = (Date) data.clone();

		if (aux.getMonth() == 0) {
			aux.setMonth(11);
		} else {
			aux.setMonth(data.getMonth() - 1);
		}

		return aux;
	}

	public static void main(String[] args) {
		Date x = new Date();
		x.setMonth(0);
		System.out.println(getDiasNoMes(x));
	}

	public static String todayYyyyMMdd(Date data) {

		// Date hoje = new Date();

		String dia = "" + data.getDate();
		String mes = "" + (data.getMonth() + 1);
		String ano = "" + (data.getYear() + 1900);

		return ano + "-" + mes + "-" + dia;

	}

	public String mes() {
		return null;
	}

	public String ultimoDiaMes(Integer Ano, Integer Mes){
		Calendar cal = new GregorianCalendar(Ano, (Mes - 1), 1);
		return    Ano.toString() + "-"+ Mes.toString()+ "-"+ cal.getActualMaximum(Calendar.DAY_OF_MONTH);
	}
}
