package com.chunkslab.gestures.playeranimator.api.utils.math;

import org.bukkit.util.EulerAngle;
import org.bukkit.util.Vector;

public class Offset {

	public static Vector getRelativeLocation(Vector origin, EulerAngle angle, Vector offset) {
		return origin.add(getRelativeLocation(angle, offset));
	}

	public static Vector getRelativeLocation(Vector origin, EulerAngle angle, double oX, double oY, double oZ) {
		return getRelativeLocation(origin, angle, new Vector(oX, oY, oZ));
	}

	public static Vector getRelativeLocation(EulerAngle angle, double oX, double oY, double oZ) {
		return getRelativeLocation(angle, new Vector(oX, oY, oZ));
	}

	public static Vector getRelativeLocation(EulerAngle angle, Vector offset) {
		return rotateRoll(rotateYaw(rotatePitch(offset, angle.getX()), angle.getY()), angle.getZ());
		
	}

	//Rz
	public static Vector rotateRoll(Vector vec, double roll) {

		double sin = Math.sin(roll);
		double cos = Math.cos(roll);
		double x = vec.getX() * cos + vec.getY() * sin;
		double y = -vec.getX() * sin + vec.getY() * cos;

		return vec.setX(x).setY(y);
		
	}

	//Rx
	public static Vector rotatePitch(Vector vec, double pitch) {

		double sin = Math.sin(pitch);
		double cos = Math.cos(pitch);
		double y = vec.getY() * cos - vec.getZ() * sin;
		double z = vec.getY() * sin + vec.getZ() * cos;

		return vec.setY(y).setZ(z);
		
	}

	//Ry
	public static Vector rotateYaw(Vector vec, double yaw) {

		double sin = Math.sin(yaw);
		double cos = Math.cos(yaw);
		double x = vec.getX() * cos - vec.getZ() * sin;
		double z = vec.getX() * sin + vec.getZ() * cos;

		return vec.setX(x).setZ(z);
			
	}

	public static Vector lerp(Vector a, Vector b, double ratio) {
		b = b.clone();
		return b.subtract(a).multiply(ratio).add(a);
	}

	public static Vector lerp(Vector a, Vector b, Vector c, Vector d, double ratio) {
		double t0 = 0, t1 = 1, t2 = 2, t3 = 3;
		double t = (t2 - t1) * ratio + t1;
		Vector a1 = a.clone().multiply((t1 - t) / (t1 - t0)).add(b.clone().multiply((t - t0) / (t1 - t0)));
		Vector a2 = b.clone().multiply((t2 - t) / (t2 - t1)).add(c.clone().multiply((t - t1) / (t2 - t1)));
		Vector a3 = c.clone().multiply((t3 - t) / (t3 - t2)).add(d.clone().multiply((t - t2) / (t3 - t2)));

		Vector b1 = a1.clone().multiply((t2 - t) / (t2 - t0)).add(a2.clone().multiply((t - t0) / (t2 - t0)));
		Vector b2 = a2.clone().multiply((t3 - t) / (t3 - t1)).add(a3.clone().multiply((t - t1) / (t3 - t1)));

		return b1.multiply((t2 - t) / (t2 - t1)).add(b2.multiply((t - t1) / (t2 - t1)));
	}

}
