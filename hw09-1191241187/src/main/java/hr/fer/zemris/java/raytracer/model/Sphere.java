package hr.fer.zemris.java.raytracer.model;

/**
 * A representation of spherical graphical objects.
 * @author Božidar Grgur Drmić
 *
 */
public class Sphere extends GraphicalObject {	

	/**
	 * Two double are considered equal if their difference is smaller than this constant.
	 */
	private static final double EPSILON = 1E-9; 
	
	/**
	 * center of a sphere.
	 */
	private Point3D center;
	/**
	 * radius of the sphere.
	 */
	private double radius;
	/**
	 * diffusion parameter for color red.
	 */
	private double kdr;
	/**
	 * diffusion parameter for color green.
	 */
	private double kdg;
	/**
	 * diffusion parameter for color blue.
	 */
	private double kdb;
	/**
	 * reflection parameter for color red.
	 */
	private double krr;
	/**
	 * reflection parameter for color green.
	 */
	private double krg;
	/**
	 * reflection parameter for color blue.
	 */
	private double krb;
	/**
	 * percentage of light that is reflected.
	 */
	private double krn;
	
	/**
	 * A constructor which accepts all the relevant data.
	 * @param center - {@code center} variable.
	 * @param radius - {@code radius} variable.
	 * @param kdr - {@code kdr} variable.
	 * @param kdg - {@code kdg} variable.
	 * @param kdb - {@code kdb} variable.
	 * @param krr - {@code krr} variable.
	 * @param krg - {@code krg} variable.
	 * @param krb - {@code krb} variable.
	 * @param krn - {@code krn} variable.
	 */
	public Sphere(Point3D center, double radius, double kdr, double kdg,
			double kdb, double krr, double krg, double krb, double krn) {
		super();
		this.center = center;
		this.radius = radius;
		this.kdr = kdr;
		this.kdg = kdg;
		this.kdb = kdb;
		this.krr = krr;
		this.krg = krg;
		this.krb = krb;
		this.krn = krn;
	}	
	
	/**
	 * A method which finds the first intersection some ray hits on this object.
	 * Returns {@code null} if there is none.
	 * @param ray - ray
	 * @return intersection.
	 */
	public RayIntersection findClosestRayIntersection(Ray ray) {
		
		double a = ray.direction.scalarProduct(ray.start.sub(center));
		double disc = a * a + radius * radius - Math.pow(ray.start.sub(center).norm(), 2);
		
		if(disc < -EPSILON) {
			return null;
		}
		
		if(disc < EPSILON) {
			double distance = -a;
			return new SphereRayIntersection(ray.start.add(ray.direction.scalarMultiply(distance)),
											distance, true, this);
		}
		
		disc = Math.sqrt(disc);
		double distance = -a - disc;
		double d2 = -a + disc;
		boolean outer = Math.signum(distance) == Math.signum(d2);
		if(distance < 0) distance = d2;
		return new SphereRayIntersection(ray.start.add(ray.direction.scalarMultiply(distance)),
										distance, outer, this);
	}
	
	/**
	 * A model for intersection between a ray and a sphere.
	 */
	private class SphereRayIntersection extends RayIntersection {

		/**
		 * Sphere which is observed.
		 */
		private Sphere sphere;
		
		/**
		 * A constructor which accepts all the relevant data.
		 * @param point - point at which they intersect.
		 * @param distance - distance between ray's source and intersection point.
		 * @param outer - a boolean denoting whether the ray comes from outside or inside of the sphere.
		 * @param sphere - sphere which is intersected.
		 */
		protected SphereRayIntersection(Point3D point, double distance, boolean outer, Sphere sphere) {
			super(point, distance, outer);
			this.sphere = sphere;
		}

		@Override
		public Point3D getNormal() {
			return this.getPoint().sub(sphere.center).modifyNormalize();
		}

		@Override
		public double getKdr() {
			return sphere.kdr;
		}

		@Override
		public double getKdg() {
			return sphere.kdg;
		}

		@Override
		public double getKdb() {
			return sphere.kdb;
		}

		@Override
		public double getKrr() {
			return sphere.krr;
		}

		@Override
		public double getKrg() {
			return sphere.krg;
		}

		@Override
		public double getKrb() {
			return sphere.krb;
		}

		@Override
		public double getKrn() {
			return sphere.krn;
		}
	}
	
}
