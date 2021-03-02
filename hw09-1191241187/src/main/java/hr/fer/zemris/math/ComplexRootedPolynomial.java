package hr.fer.zemris.math;

/**
 * A class whose instances represent polynomials with complex coefficients in rooted form.
 * @author Božidar Grgur Drmić
 *
 */
public class ComplexRootedPolynomial {

	/**
	 * leading coefficient.
	 */
	Complex leadingConstant;
	
	/**
	 * roots of this polynomial.
	 */
	Complex[] roots;
	
	/**
	 * Public constructor. Accepts roots and leading coefficient as arguments.
	 * @param constant - leading coefficient.
	 * @param roots - an array of roots.
	 */
	public ComplexRootedPolynomial(Complex constant, Complex ... roots) {
		this.leadingConstant = constant;
		this.roots = roots;
	}
	
	/**
	 * A method which calculates the value of this polynomial for some complex.
	 * @param z - parameter of this polynomial.
	 * @return p(z).
	 */
	public Complex apply(Complex z) {
		Complex result = Complex.ONE;
		result = result.mul(leadingConstant);
		for(int i = 0; i < roots.length; i++) {
			result = result.mul(z.sub(roots[i]));
		}
		
		return result;
	}
	
	/**
	 * A method which calculates the equivalent polynomial in standard form.
	 * @return this polynomial in standard form.
	 */
	public ComplexPolynomial toComplexPolynom() {
		Complex lead[] = {leadingConstant};
		ComplexPolynomial result = new ComplexPolynomial(lead);
		
		Complex factors[] = {leadingConstant, Complex.ONE};
		
		for(int i = 0; i < roots.length; i++) {
			factors[0] = roots[i].negate();
			result = result.multiply(new ComplexPolynomial(factors));
		}
		
		return result;
	}
	
	/**
	 * A method which finds the closest root to some complex and returns it's index.
	 * If there is no such root or the distance between it and the complex is too large
	 * -1 is returned.
	 * @param z - complex whose closest root is found
	 * @param treshold - maximum allowed distance.
	 * @return index of closest root.
	 */
	public int indexOfClosestRootFor(Complex z, double treshold) {
		double best = treshold;
		int res = -1;
		for(int i = 0; i < roots.length; i++) {
			double d = z.sub(roots[i]).module(); 
			if(d < best) {
				res = i;
				best = d; 
			}
		}

		return res;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("(" + leadingConstant.toString() + ")");
		for(int i = 0; i < roots.length; i++) {
			sb.append("(z - (" + roots[i].toString() + "))");
		}
		return sb.toString();
	}
}