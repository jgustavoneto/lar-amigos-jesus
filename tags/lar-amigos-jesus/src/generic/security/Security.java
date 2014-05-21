package generic.security;

import java.util.Calendar;
import java.util.Date;

public class Security {

	/**
	 * Valor negativo ou igual a zero é intervalo válido.
	 */
	public static int tryToUse() {

		Calendar inspira = Calendar.getInstance();
		inspira.set(Calendar.MONTH, 10);
		inspira.set(Calendar.DAY_OF_MONTH, 24);
		inspira.set(Calendar.YEAR, 2014);

		Calendar calendar = Calendar.getInstance();
		Date data = calendar.getTime();

		return data.compareTo(inspira.getTime());
	}

	public static void tryAcessOrLock() {

		Calendar inspira = Calendar.getInstance();
		inspira.set(Calendar.MONTH, 1);
		inspira.set(Calendar.DAY_OF_MONTH, 1);
		inspira.set(Calendar.YEAR, 2014);

		Calendar calendar = Calendar.getInstance();
		Date data = calendar.getTime();

		if (data.compareTo(inspira.getTime()) > 0) {
			System.exit(0);
		}
	}

	public static void main(String[] args) {
		System.out.println(Security.tryToUse());
	}

}
