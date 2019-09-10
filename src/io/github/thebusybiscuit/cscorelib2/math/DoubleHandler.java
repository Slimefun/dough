package io.github.thebusybiscuit.cscorelib2.math;

import java.text.DecimalFormat;

public final class DoubleHandler {
	
	private DoubleHandler() {}
	
	/**
	 * This method returns a very fancy representation of Doubles.
	 *
	 * {@code 1000 		-> 1K}
	 * {@code 32000		-&gt; 32K}
	 * {@code 232 000 000	-&gt; 2.32M}
	 * (and so on)
	 * 
	 * @param d	The double that should be formatted
	 * @return	A fancy representation of this double
	 */
	public static String getFancyDouble(double d) {
		DecimalFormat format = new DecimalFormat("##.##");
		
		double d2 = d / 1000000000000000d;
		if (d2 > 1.0) return format.format(d2).replace(",", ".") + "Q";
		
		d2 = d / 1000000000000d;
		if (d2 > 1.0) return format.format(d2).replace(",", ".") + "T";
		
		d2 = d / 1000000000d;
		if (d2 > 1.0) return format.format(d2).replace(",", ".") + "B";
		
		d2 = d / 1000000d;
		if (d2 > 1.0) return format.format(d2).replace(",", ".") + "M";
		
		d2 = d / 1000d;
		if (d2 > 1.0) return format.format(d2).replace(",", ".") + "K";
		
		return format.format(d).replace(",", ".");
	}
	
	public static double fixDouble(double amount, int digits) {
		if (digits == 0) return (int) amount;
		StringBuilder format = new StringBuilder("##");
		for (int i = 0; i < digits; i++) {
			if (i == 0) format.append(".");
			format.append("#");
		}
		return Double.valueOf(new DecimalFormat(format.toString()).format(amount).replace(",", "."));
	}
	
	public static double fixDouble(double amount) {
		return fixDouble(amount, 2);
	}

}
