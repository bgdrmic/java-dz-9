package hr.fer.zemris.math;

/**
 * A class whose instances represent polynomials with complex coefficients in standard form.
 * @author Božidar Grgur Drmić
 *
 */
public class ComplexPolynomial {

	/**
	 * coefficients of this polynomial.
	 */
	private Complex[] coefficients;
	
	/**
	 * A constructor which accepts coefficients as parameter.
	 * @param factors - coefficients
	 */
	public ComplexPolynomial(Complex ...factors) {
		this.coefficients = factors;
	}
	
	/**
	 * A method which calculates the order of this polynomial.
	 * @return the order.
	 */
	public short order() {
		return (short) (coefficients.length - 1);
	}
	
	/**
	 * A method which multiplies current polynomial with some other and returns their product. 
	 * @param p - other polynomial
	 * @return the product of these polynomials.
	 */
	public ComplexPolynomial multiply(ComplexPolynomial p) {
		Complex[] factors = new Complex[this.order() + p.order() + 1];
		
		for(int i = 0; i < factors.length; i++) {
			factors[i] = Complex.ZERO;
			for(int j = 0; j <= i && j <= this.order(); j++) {
				if(i-j > p.order()) continue;
				factors[i] = factors[i].add(this.coefficients[j].mul(p.coefficients[i-j]));
			}
		}
		
		return new ComplexPolynomial(factors);
	}
	
	/**
	 * A method which calculates the derivative of the current polynomial.
	 * @return the derivative.
	 */
	public ComplexPolynomial derive() {
		Complex[] factors = new Complex[this.order()];
		for(int i = 0; i < factors.length; i++) {
			factors[i] = coefficients[i+1].mul(new Complex(i+1, 0));
		}
		return new ComplexPolynomial(factors);
	}
	
	/**
	 * A method which calculates the value of this polynomial for some complex.
	 * @param z - parameter of this polynomial.
	 * @return p(z).
	 */
	public Complex apply(Complex z) {
		Complex result = Complex.ZERO;
		for(int i = coefficients.length-1; i >= 0; i--) {
			result = result.mul(z).add(coefficients[i]);
		}
		
		return result;
	}

	@Override
	public String toString() {
		if(coefficients.length == 0) {
			return "0";
		}
		StringBuilder sb = new StringBuilder();
		for(int i = coefficients.length-1; i >= 0; i--) {
			if(i != coefficients.length - 1) {
				sb.append(" + ");
			}
			sb.append("(" + coefficients[i].toString() + ") * z^" + i);
		}
		return sb.toString();
	}
	
}
