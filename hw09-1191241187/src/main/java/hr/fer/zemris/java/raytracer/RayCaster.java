package hr.fer.zemris.java.raytracer;

import java.util.concurrent.atomic.AtomicBoolean;

import hr.fer.zemris.java.raytracer.model.*;
import hr.fer.zemris.java.raytracer.viewer.*;

/**
 * A class which represents a ray-tracer for rendering of 3D scenes.
 * @author Božidar Grgur Drmić
 *
 */
public class RayCaster {

	/**
	 * If two doubles differs by a value lower than this they are conseidered equal.
	 */
	public static final double EPSILON = 1E-9;
	
	/**
	 * Main method of this class.
	 * Starts the rendering.
	 * @param args - has no effect.
	 */
	public static void main(String[] args) {
			RayTracerViewer.show(getIRayTracerProducer(),
								new Point3D(10,0,0),
								new Point3D(0,0,0),
								new Point3D(0,0,10),
								20, 20);
	}
	
	/**
	 * A method which creates an implementation of IRayTracerProducer which is suitable
	 * for this task.
	 * @return new IRayTracerProducer
	 */
	private static IRayTracerProducer getIRayTracerProducer() {
		return new IRayTracerProducer() {
			
			@Override
			public void produce(Point3D eye, Point3D view, Point3D viewUp, double horizontal,
								double vertical, int width, int height, long requestNo,
								IRayTracerResultObserver observer, AtomicBoolean cancel) {
		
				System.out.println("Započinjem izračune...");
				
				short[] red = new short[width*height];
				short[] green = new short[width*height];
				short[] blue = new short[width*height];
				
				Point3D og = view.sub(eye).modifyNormalize();
				Point3D vuv = viewUp.normalize();
				
				Point3D j = vuv.sub(og.scalarMultiply(vuv.scalarProduct(og))).modifyNormalize();
				Point3D i = og.vectorProduct(j).modifyNormalize();
				
				Point3D screenCorner = view.sub(i.scalarMultiply(horizontal / (double) 2))
										.add(j.scalarMultiply(vertical / (double) 2));
				
				Scene scene = RayTracerViewer.createPredefinedScene();
				short[] rgb = new short[3];
				int offset = 0;
				for(int y = 0; y < height; y++) {
					for(int x = 0; x < width; x++) {
						Point3D screenPoint = screenCorner.add(i.scalarMultiply(horizontal * x / (double) (width - 1)))
														  .sub(j.scalarMultiply(vertical * y / (double) (height - 1)));
						Ray ray = Ray.fromPoints(eye, screenPoint);
						tracer(scene, ray, rgb);
						red[offset] = rgb[0] > 255 ? 255 : rgb[0];
						green[offset] = rgb[1] > 255 ? 255 : rgb[1];
						blue[offset] = rgb[2] > 255 ? 255 : rgb[2];
						offset++;
					}
				}
				System.out.println("Izračuni gotovi...");
				observer.acceptResult(red, green, blue, requestNo);
				System.out.println("Dojava gotova...");
			}
			
			/**
			 * A method which sets the colors to appropriate value
			 * depending on the first object this ray stumbles upon.
			 * @param scene - scene which is colored.
			 * @param ray - ray which is followed
			 * @param rgb - color
			 */
			private void tracer(Scene scene, Ray ray, short[] rgb) {
				RayIntersection closest = null;
				for(var object : scene.getObjects()) {
					var intersection = object.findClosestRayIntersection(ray);
					if(intersection != null && intersection.getDistance() >= 0 &&
							(closest == null || intersection.getDistance() - EPSILON < closest.getDistance())) {
						closest = intersection;
					}
				}

				rgb[0] = 15;
				rgb[1] = 15;
				rgb[2] = 15;
				
				if(closest == null) {
					rgb[0] = 0;
					rgb[1] = 0;
					rgb[2] = 0;
					return;
				}
				
				for(var light : scene.getLights()) {
					var lightRay = Ray.fromPoints(light.getPoint(), closest.getPoint());
					RayIntersection closestToLight = null;
					for(var object : scene.getObjects()) {
						var intersection = object.findClosestRayIntersection(lightRay);
						if(intersection != null && intersection.getDistance() >= 0 &&
								(closestToLight == null || intersection.getDistance() - EPSILON < closestToLight.getDistance())) {
							closestToLight = intersection;
						}
					}
					
					Point3D l = light.getPoint().sub(closest.getPoint());
					double distance = l.norm();
					l.modifyNormalize();
					
					if(distance - closestToLight.getDistance() > 2*EPSILON) {
						continue;
					}
					
					double coef = l.scalarProduct(closest.getNormal());
					if(coef < EPSILON) {
						coef = 0;
					}
					
					rgb[0] += Math.round(light.getR() * closest.getKdr() * coef);
					rgb[1] += Math.round(light.getG() * closest.getKdg() * coef);
					rgb[2] += Math.round(light.getB() * closest.getKdb() * coef);
					coef = ray.start.sub(closest.getPoint()).modifyNormalize()
								  .scalarProduct(closest.getNormal().scalarMultiply(2 * closest.getNormal().scalarProduct(l))
												  .modifySub(l)
								);
					coef = Math.pow(coef, closest.getKrn());
					if(coef < 0) {
						coef = 0;
					}
					rgb[0] +=  Math.round(light.getR() * closest.getKrr() * coef);
					rgb[1] +=  Math.round(light.getG() * closest.getKrg() * coef);
					rgb[2] +=  Math.round(light.getB() * closest.getKrb() * coef);
				}
			}
		};
	}
	
}
