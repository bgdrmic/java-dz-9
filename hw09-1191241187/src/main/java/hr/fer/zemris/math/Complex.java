package hr.fer.zemris.math;

import java.lang.Math;

/**
 * Complex is a class which represents an unmodifiable complex number.
 * Each instance contains two private variables:
 * 	real - real part value
 *  imaginary - imaginary part value
 * @author Božidar Grgur Drmić
 *
 */
public class Complex {

	/**
	 * A complex number equal to 0.
	 */
	public static final Complex ZERO = new Complex(0,0);
	/**
	 * A complex number equal to 1.
	 */
	public static final Complex ONE = new Complex(1,0);
	/**
	 * A complex number equal to -1.
	 */
	public static final Complex ONE_NEG = new Complex(-1,0);
	/**
	 * A complex number equal to i.
	 */
	public static final Complex IM = new Complex(0,1);
	/**
	 * A complex number equal to -i.
	 */
	public static final Complex IM_NEG = new Complex(0,-1);
	
	/**
	 * A non-static private variable which represents real part value of a complex.
	 */
	private double real;
	
	/**
	 * A non-static private variable which represents imaginary part value of a complex.
	 */
	private double imaginary;
	
	/**
	 * Public constructor. Creates a new complex number equal to 0.
	 */
	public Complex() {
		this(0, 0);
	}
	
	/**
	 * Public constructor which accepts two arguments: real part and imaginary part.
	 * @param real real part value.
	 * @param imaginary imaginary part value.
	 */
	public Complex(double real, double imaginary) {
		super();
		this.real = real;
		this.imaginary = imaginary;
	}
	
	/**
	 * A public static factory method which creates a complex with imaginary part
	 * set to zero and real part set to some value.
	 * 
	 * @param real A value the real part is to be set to.
	 * @return Returns a new complex with imaginary part zero.
	 */
	public static Complex fromReal(double real) {
		return new Complex(real, 0);
	}
	
	/**
	 * A public static factory method which creates a complex with real part
	 * set to zero and imaginary part set to some value.
	 * 
	 * @param imaginary A value the imaginary part is to be set to.
	 * @return Returns a new complex with real part zero.
	 */
	public static Complex fromImaginary(double imaginary) {
		return new Complex(0, imaginary);
	}
	
	/**
	 * A public static factory method which creates a complex which is uniquely
	 * defined by its angle and magnitude.
	 * 
	 * @param magnitude A magnitude of a complex.
	 * @param angle An angle of a complex.
	 * @return Returns a new complex uniquely defined by parameters.
	 */
	public static Complex fromMagnitudeAndAngle(double magnitude, double angle) {
		if(magnitude < 0) {
			throw new IllegalArgumentException();
		}
		return new Complex(magnitude * Math.cos(angle), magnitude * Math.sin(angle));
	}
	
	/**
	 * A getter method for the real part of a complex.
	 * 
	 * @return Returns the real part of a complex.
	 */
	public double getReal() {
		return real;
	}

	/**
	 * A getter method for the imaginary part of a complex.
	 * 
	 * @return Returns the imaginary part of a complex.
	 */
	public double getImaginary() {
		return imaginary;
	}

	/**
	 * A getter method for the magnitude of a complex.
	 * 
	 * @return Returns the magnitude of a complex.
	 */
	public double module() {
		return Math.sqrt(real*real + imaginary*imaginary);
	}

	/**
	 * A getter method for the angle of a complex.
	 * 
	 * @return Returns the angle of a complex.
	 */
	public double getAngle() {
		double angle = Math.atan(imaginary / real);
		if(real < 0) {
			angle += Math.PI;
		} else if(imaginary < 0) {
			angle += 2 * Math.PI;
		}
		return angle;
	}
	
	/**
	 * A method which adds some other complex to the current complex and returns
	 * the result as a newly created complex.
	 * 
	 * @param c The other complex number
	 * @return Returns the sum of two complexes.
	 */
	public Complex add(Complex c) {
		double real = this.real + c.real;
		double imaginary = this.imaginary + c.imaginary;
		return new Complex(real, imaginary);
	}
	
	/**
	 * A method which subtracts some other complex from the current complex
	 * and returns the result as a newly created complex.
	 * 
	 * @param c The other complex number
	 * @return Returns the subtraction of two complexes.
	 */
	public Complex sub(Complex c) {
		double real = this.real - c.real;
		double imaginary = this.imaginary - c.imaginary;
		return new Complex(real, imaginary);
	}
	
	/**
	 * A method which multiplies the current and some other complex
	 * and returns the result as a newly created complex.
	 * 
	 * @param c The other complex number.
	 * @return Returns the product of two complexes.
	 */
	public Complex mul(Complex c) {
		double real = this.real * c.real - this.imaginary * c.imaginary;
		double imaginary = this.real * c.imaginary + this.imaginary * c.real;
		
		return new Complex(real, imaginary);
	}
	
	/**
	 * A method which divides the current complex by some other complex
	 * and returns the result as a newly created complex.
	 * 
	 * @param c The other complex number.
	 * @return Returns the division of two complexes.
	 */
	public Complex div(Complex c) {
		double scale = c.imaginary * c.imaginary + c.real * c.real;
		double real = this.real * c.real + this.imaginary * c.imaginary;
		double imaginary = this.imaginary * c.real - this.real * c.imaginary;
		
		return new Complex(real / scale, imaginary / scale);
	}
	
	/**
	 * A method which creates a new complex which is a negative
	 * of the current complex and returns it.
	 * @return negative complex.
	 */
	public Complex negate() {
		return new Complex(-this.real, -this.imaginary);
	}
	
	/**
	 * Calculates the current complex to the power of n.
	 * Throws IllegalArgumentException if n < 0.
	 * 
	 * @param n the power which is to be calculated.
	 * @return Returns the current complex to the power of n.
	 * @throws IllegalArgumentException if n < 0.
	 */
	public Complex power(int n) {
		if(n < 0) {
			throw new IllegalArgumentException();
		}
		Complex power = new Complex(1, 0);
		for(int i = 0; i < n; i++) {
			power = power.mul(this);
		}
		return power;
	}
	
	/**
	 * Calculates the n-th roots of the current complex.
	 * Returns the array of n n-th roots of the current complex.
	 * Throws IllegalArgumentException if n <= 0
	 * or if the current complex equals zero.
	 * 
	 * @param n root which is to be calculated.
	 * @return Returns the array of n n-th roots of the current complex.
	 * @throws IllegalArgumentException if n <= 0
	 * 		   or if the current complex equals zero.
	 */
	public Complex[] root(int n) {
		if(n <= 0 || (real == 0 && imaginary == 0)) {
			throw new IllegalArgumentException();
		}
		
		Complex[] root = new Complex[n];
		double magnitude = Math.pow(this.real, (double) 1/n);
		for(int i = 0; i < n; i++) {
			root[i] = fromMagnitudeAndAngle(magnitude, (this.getAngle() + 2 * i * Math.PI) / n);
		}
		return root;
	}
	
	/**
	 * A method which parses a string into a complex and returns that complex.
	 * @param entry - string which is parsed.
	 * @return the parsed complex.
	 * @throws NumberFormatException if the format is wrong.
	 */
	public static Complex parse(String entry) {
		boolean sign = entry.charAt(0) == '-';
		if(entry.charAt(0) == '+' || sign) {
			entry = entry.substring(1);
		}
		
		String[] parts = entry.split("[+-]");
		if(parts.length == 1) {
			if(entry.contains("i")) {
				if(parts[0].strip().equals("i")) {
					entry = entry.replace('i', '1');
				}
				double im = Double.parseDouble(entry.replaceAll("i", ""));
				if(sign) {
					im = -im;
				}
				return new Complex(0, im);
			}
			
			double re  = Double.parseDouble(entry);
			if(sign) {
				re = -re;
			}
			return new Complex(re, 0);
		}
		if(parts.length != 2) {
			throw new NumberFormatException("Wrong format");
		}
		
		double re, im;
		re = Double.parseDouble(parts[0]);
		if(sign) {
			re = -re;
		}
		im = Double.parseDouble(parts[1].replaceAll("i", ""));
		if(entry.contains("-")) {
			im = -im;
		}
		return new Complex(re, im);
	}
	
	/**
	 * Parses the current complex number to String.
	 * @return Returns the string format of the current complex.
	 */
	public String toString() {
		if(imaginary == 0) {
			return String.valueOf(this.real);
		}
		if(real == 0) {
			return String.valueOf(this.imaginary) + "i";
		}
		if(imaginary > 0) {
			return String.valueOf(this.real) + "+" + String.valueOf(this.imaginary) + "i";			
		}
		return String.valueOf(this.real) + String.valueOf(this.imaginary) + "i";
	}
}
