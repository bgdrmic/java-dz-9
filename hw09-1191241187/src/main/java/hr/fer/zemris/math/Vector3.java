package hr.fer.zemris.math;

public class Vector3 {

	private double x;
	private double y;
	private double z;
	
	public Vector3(double x, double y, double z) {
		super();
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public double norm() {
		return Math.sqrt(x*x + y*y + z*z);
	}
	
	public Vector3 normalized() {
		double norm = norm();
		return new Vector3(x / norm, y / norm, z / norm);
	}
	
	public Vector3 add(Vector3 other) {
		return new Vector3(x + other.x, y + other.y, z + other.z);
	}
	
	public Vector3 sub(Vector3 other) {
		return new Vector3(x - other.x, y - other.y, z - other.z);
	}
	
	public double dot(Vector3 other) {
		return x * other.x + y * other.y + z * other.z;
	}
	
	public Vector3 cross(Vector3 other) {
		return new Vector3(y * other.z - z * other.y, z * other.x - x * other.z, x * other.y - y * other.x);
	}
	
	public Vector3 scale(double s) {
		return new Vector3(x * s, y * s, z * s);
	}
	
	public double cosAngle(Vector3 other) {
		return this.dot(other) / (this.norm() * other.norm());
	}

	public double getX() {
		return x;
	}

	public double getY() {
		return y;
	}

	public double getZ() {
		return z;
	}
	
	public double[] toArray() {
		double a[] = {x, y, z};
		return a;
	}

	@Override
	public String toString() {
		return "(" + this.x + ", " + this.y + ", " + this.z + ")";
	}
	
	
}
