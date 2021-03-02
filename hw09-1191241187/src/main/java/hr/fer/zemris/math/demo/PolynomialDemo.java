package hr.fer.zemris.math.demo;

import hr.fer.zemris.math.*;

/**
 * A demo program for running an example which utilizes
 * ComplexPolynomial and ComplexRootedPolynomial classes.
 * @author Božidar Grgur Drmić
 *
 */
public class PolynomialDemo {

	/**
	 * Main class of this program. Runs the example.
	 * @param args - has no effect.
	 */
	public static void main(String[] args) {
		ComplexRootedPolynomial crp = new ComplexRootedPolynomial(new Complex(2,0), Complex.ONE, Complex.ONE_NEG, Complex.IM, Complex.IM_NEG);
		ComplexPolynomial cp = crp.toComplexPolynom();
		
		System.out.println(crp);
		System.out.println(cp);
		System.out.println(cp.derive());
	}

}
