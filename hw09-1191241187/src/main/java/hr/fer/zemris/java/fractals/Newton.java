package hr.fer.zemris.java.fractals;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicBoolean;

import hr.fer.zemris.java.fractals.viewer.FractalViewer;
import hr.fer.zemris.java.fractals.viewer.IFractalProducer;
import hr.fer.zemris.java.fractals.viewer.IFractalResultObserver;
import hr.fer.zemris.math.*;

/**
 * A class which generates the Newton fractals.
 * @author Božidar Grgur Drmić
 *
 */
public class Newton {

	/**
	 * Maximum distance a point and a root can be apart from each other so that they are considered close.
	 */
	public static final double ROOT_TRESHOLD = 0.003;
	/**
	 * Maximum distance two points can be apart from each other so that they are considered close.
	 */
	public static final double CONVERGENCE_TRESHOLD = 0.001;
	/**
	 * Maximum number of fractal iterations.
	 */
	private static final int MAX_ITERATIONS = 16 * 16;
	
	/**
	 * A factory for daemonic threads.
	 */
	private static ThreadFactory daemonicThreadFactory = r -> {
			Thread result = new Thread(r);
			result.setDaemon(true);
			return result;
	};
	
	/**
	 * Given polynomial.
	 */
	private static ComplexPolynomial polynomial;
	/**
	 * Derivation of given {@code polynomial}.
	 */
	private static ComplexPolynomial derived;
	/**
	 * Given polynomial in root-form.
	 */
	private static ComplexRootedPolynomial rootedPolynomial;
	
	/**
	 * Main method of this class.
	 * Reads the roots and does the drawing.
	 * 
	 * @param args - has no effect.
	 */
	public static void main(String[] args) {
		boolean test = true;
		Complex[] factors;
		if(test) {
			factors = new Complex[4];
			factors[0] = Complex.ONE;
			factors[1] = Complex.ONE.negate();
			factors[2] = Complex.IM;
			factors[3] = Complex.IM.negate();
		} else {
			ArrayList<Complex> roots = new ArrayList<Complex>();
			Scanner sc = new Scanner(System.in);
			System.out.println("Welcome to Newton-Raphson iteration-based fractal viewer.Please "
					+ "enter at least two roots, one root per line. Enter 'done' when done.");
			while(true) {
				System.out.printf("Root %d> ", roots.size() + 1);
				String entry = sc.nextLine().strip();
				if(entry.equals("done")) {
					if(roots.size() < 2) {
						System.out.println("More arguments needed");
						continue;
					}
					System.out.println("Image of fractal will appear shortly. Thank you.");
					sc.close();
					break;
				}
				roots.add(Complex.parse(entry));
			}
			
			factors = new Complex[roots.size()];
			for(int i = 0; i < roots.size(); i++) {
				factors[i] = roots.get(i);
			}
		}
		rootedPolynomial = new ComplexRootedPolynomial(Complex.ONE, factors);
		polynomial = rootedPolynomial.toComplexPolynom();
		derived = polynomial.derive();
		
		FractalViewer.show(new MyProducer());
	}
	
	/**
	 * An representation of one part of calculation used for parallelization.
	 */
	public static class CalculationTask implements Runnable {
		/**
		 * minimum real value.
		 */
		double reMin;
		/**
		 * maximum real value.
		 */
		double reMax;
		/**
		 * minimum imaginary value.
		 */
		double imMin;
		/**
		 * maximum imaginary value.
		 */
		double imMax;
		/**
		 * width of the screen.
		 */
		int width;
		/**
		 * height of the screen
		 */
		int height;
		/**
		 * index of the first row that is calculated. 
		 */
		int yMin;
		/**
		 * index of the last row that is calculated.
		 */
		int yMax;
		/**
		 * maximum number of iterations.
		 */
		int m;
		/**
		 * storage for the result of calculation
		 */
		short[] data;
		/**
		 * mutex variable.
		 */
		AtomicBoolean cancel;
		
		/**
		 * A constructor which accepts all the relevant data.
		 * @param reMin - {@code reMin} variable.
		 * @param reMax - {@code reMax} variable.
		 * @param imMin - {@code imMin} variable.
		 * @param imMax - {@code imMax} variable.
		 * @param width - {@code width} variable.
		 * @param height - {@code height} variable.
		 * @param yMin - {@code yMin} variable.
		 * @param yMax - {@code yMin} variable.
		 * @param m - {@code m} variable.
		 * @param data - {@code data} variable.
		 * @param cancel - {@code cancel} variable.
		 */
		public CalculationTask(double reMin, double reMax, double imMin,
				double imMax, int width, int height, int yMin, int yMax, 
				int m, short[] data, AtomicBoolean cancel) {
			super();
			
			this.reMin = reMin;
			this.reMax = reMax;
			this.imMin = imMin;
			this.imMax = imMax;
			this.width = width;
			this.height = height;
			this.yMin = yMin;
			this.yMax = yMax;
			this.m = m;
			this.data = data;
			this.cancel = cancel;
		}
		
		@Override
		public void run() {
			for(int y = yMin ; y <= yMax; y++) {
				for(int x = 0; x <= width; x++) {
					double re = x * (reMax - reMin) / (double) (width - 1) + reMin;
					double im = (height - 1 - y) * (imMax - imMin) / (height - 1) + imMin;
					Complex zn = new Complex(re, im);
					
					int iter = 0;
					double module = 0;
					
					do {
						Complex numerator = polynomial.apply(zn);
						Complex denominator = derived.apply(zn);
						Complex znold = zn;
						Complex fraction = numerator.div(denominator);
						zn = zn.sub(fraction);
						module = znold.sub(zn).module();
						iter++;
					} while(module > CONVERGENCE_TRESHOLD && iter < MAX_ITERATIONS);
					
					int index = rootedPolynomial.indexOfClosestRootFor(zn, ROOT_TRESHOLD);
					
					if(iter == MAX_ITERATIONS) {
						index = -1;
					}
					data[y*width + x] = (short)(index + 1);
				}
			}
		}
	}

	/**
	 * A Newton's fractal producer.
	 */
	public static class MyProducer implements IFractalProducer {
		@SuppressWarnings("unchecked")
		@Override
		public void produce(double reMin, double reMax, double imMin, double imMax,
				int width, int height, long requestNo, IFractalResultObserver observer, AtomicBoolean cancel) {
			System.out.println("Započinjem izračun...");
			short[] data = new short[width * height];
			ExecutorService pool = Executors.newFixedThreadPool(
					Runtime.getRuntime().availableProcessors(),
					daemonicThreadFactory
			);			
			
			final int numberOfTasks = Runtime.getRuntime().availableProcessors() * 8;
			int rowsPerTask = height / numberOfTasks;
			
			List<Future<Void>> results = new ArrayList<>();
			
			for(int i = 0; i < numberOfTasks; i++) {
				int yMin = i * rowsPerTask;
				int yMax = (i+1) * rowsPerTask - 1;
				if(i == numberOfTasks - 1) {
					yMax = height - 1;
				}
				CalculationTask job = new CalculationTask(reMin, reMax, imMin, imMax, width, height, yMin, yMax, polynomial.order() + 1, data, cancel);
				results.add((Future<Void>) pool.submit(job));
			}
			
			for(Future<Void> job : results) {
				try {
					job.get();
				} catch (InterruptedException | ExecutionException e) {
				}
			}
			
			pool.shutdown();
			
			System.out.println("Računanje gotovo. Idem obavijestiti promatrača, tj. GUI!");
			observer.acceptResult(data, (short)(polynomial.order()+1), requestNo);
		}
	}
}
