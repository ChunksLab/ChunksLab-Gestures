/*
 * This file is part of ChunksLab-Gestures, licensed under the Apache License 2.0.
 *
 * Copyright (c) Amownyy <amowny08@gmail.com>
 * Copyright (c) contributors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.chunkslab.gestures.playeranimator.api.utils;

import com.chunkslab.gestures.playeranimator.api.utils.math.TMath;
import org.bukkit.util.EulerAngle;
import org.bukkit.util.Vector;

class Q2 {

	private double w;
	private final Vector vec;

	public Q2(double w, Vector vec) {
		this.w = w;
		this.vec = vec;

	}

	public static Q2 toQuaternionDegree(double x, double y, double z) {
		return toQuaternion(new EulerAngle(Math.toRadians(x), Math.toRadians(y), Math.toRadians(z)));
	}

	public static Q2 toQuaternion(EulerAngle e) {

		double cosX = Math.cos(e.getX() * 0.5);
		double cosY = Math.cos(e.getY() * -0.5);
		double cosZ = Math.cos(e.getZ() * 0.5);

		double sinX = Math.sin(e.getX() * 0.5);
		double sinY = Math.sin(e.getY() * -0.5);
		double sinZ = Math.sin(e.getZ() * 0.5);

		double w = cosX * cosY * cosZ - sinX * sinY * sinZ;
		Vector vec = new Vector(
				sinX * cosY * cosZ + cosX * sinY * sinZ,
				cosX * sinY * cosZ - sinX * cosY * sinZ,
				cosX * cosY * sinZ + sinX * sinY * cosZ);

		return new Q2(w, vec);

	}

	public static EulerAngle toEuler(Q2 q) {

		double x = q.getVec().getX(), y = q.getVec().getY(), z = q.getVec().getZ(), w = q.getW();
		double x2 = x + x, y2 = y + y, z2 = z + z;
		double xx = x * x2, xy = x * y2, xz = x * z2;
		double yy = y * y2, yz = y * z2, zz = z * z2;
		double wx = w * x2, wy = w * y2, wz = w * z2;

		double ex, ey, ez,
			m11 = 1 - (yy + zz),
			m12 = xy - wz,
			m13 = xz + wy,
			m22 = 1 - (xx + zz),
			m23 = yz - wx,
			m32 = yz + wx,
			m33 = 1 - (xx + yy);

		ey = Math.asin(TMath.clamp(m13, -1, 1));
		if (Math.abs(m13) < 0.99999) {
			ex = Math.atan2(-m23, m33);
			ez = Math.atan2(-m12, m11);
		} else {
			ex = Math.atan2(m32, m22);
			ez = 0;
		}

		return new EulerAngle(ex, -ey, ez);

	}

	public static Q2 multiply(Q2 a, Q2 b) {

		double aX = a.getVec().getX(), aY = a.getVec().getY(), aZ = a.getVec().getZ(), aW = a.getW();
		double bX = b.getVec().getX(), bY = b.getVec().getY(), bZ = b.getVec().getZ(), bW = b.getW();

		double w = aW * bW - aX * bX - aY * bY - aZ * bZ;
		Vector vec = new Vector(
				aX * bW + aW * bX + aY * bZ - aZ * bY,
				aY * bW + aW * bY + aZ * bX - aX * bZ,
				aZ * bW + aW * bZ + aX * bY - aY * bX);

		return new Q2(w, vec);
		
	}
	
	public static Q2 multiply(Q2 a, double b) {
		return new Q2(a.getW() * b, a.getVec().multiply(b));
	}
	
	public static double dot(Q2 a, Q2 b) {
		return a.getW() * b.getW() + a.getVec().dot(b.getVec());
	}
	
	public static Q2 add(Q2 a, Q2 b) {
		return new Q2(a.getW() + b.getW(), a.getVec().add(b.getVec()));
	}
	
	public static Q2 subtract(Q2 a, Q2 b) {
		return new Q2(a.getW() - b.getW(), a.getVec().subtract(b.getVec()));
	}
	
	// Global Rotation: previous, change
	public static EulerAngle combine(EulerAngle origin, EulerAngle delta) {
		return toEuler(multiply(toQuaternion(origin), toQuaternion(delta)));
	}

	public static EulerAngle lerp(EulerAngle a, EulerAngle b, double t) {
		return new EulerAngle(
				TMath.lerp(a.getX(), b.getX(), t),
				TMath.lerp(a.getY(), b.getY(), t),
				TMath.lerp(a.getZ(), b.getZ(), t)
		);
	}

	public static EulerAngle splineLerp(EulerAngle a, EulerAngle b, EulerAngle c, EulerAngle d, double ratio) {
		double t0 = 0, t1 = 1, t2 = 2, t3 = 3;
		double t = (t2 - t1) * ratio + t1;
		EulerAngle a1 = add(multiply(a, (t1 - t) / (t1 - t0)), multiply(b, (t - t0) / (t1 - t0)));
		EulerAngle a2 = add(multiply(b, (t2 - t) / (t2 - t1)), multiply(c, (t - t1) / (t2 - t1)));
		EulerAngle a3 = add(multiply(c, (t3 - t) / (t3 - t2)), multiply(d, (t - t2) / (t3 - t2)));

		EulerAngle b1 = add(multiply(a1, (t2 - t) / (t2 - t0)), multiply(a2, (t - t0) / (t2 - t0)));
		EulerAngle b2 = add(multiply(a2, (t3 - t) / (t3 - t1)), multiply(a3, (t - t1) / (t3 - t1)));

		return add(multiply(b1, (t2 - t) / (t2 - t1)), multiply(b2, (t - t1) / (t2 - t1)));
	}

	public static EulerAngle slerp(EulerAngle a, EulerAngle b, double t) {
		Q2 qA = toQuaternion(a);
		Q2 qB = toQuaternion(b);

		double dot = dot(qA, qB);
		if(dot < 0) {
			qB.multiply(-1);
			dot = -dot;
		}

		if(dot > 0.9995) {
			qA.multiply(1 - t);
			qB.multiply(t);
		}else {
			double theta = Math.acos(dot);
			double sinTheta = Math.sin(theta);
			double sinA = Math.sin((1 - t) * theta);
			double sinB = Math.sin(t * theta);

			qA.multiply(sinA / sinTheta);
			qB.multiply(sinB / sinTheta);
		}
		Q2 result = add(qA, qB);
		result.normalize();
		return toEuler(result);
	}
	
	public void multiply(double b) {
		w *= b;
		vec.multiply(b);
	}
	
	public void normalize() {
		double norm = Math.sqrt(w * w + vec.getX() * vec.getX() + vec.getY() * vec.getY() + vec.getZ() * vec.getZ());
		w /= norm; 
		vec.multiply(1.0/norm);		
	}
	
	public double getW() {
		return w;
	}

	public Vector getVec() {
		return vec;
	}

	public String toFormula() {
		return "[" + w + "+" + vec.getX() + "i+" + vec.getY() + "j+" + vec.getZ() + "k" + "]";
	}

	private static EulerAngle add(EulerAngle a, EulerAngle b) {
		return new EulerAngle(a.getX() + b.getX(), a.getY() + b.getY(), a.getZ() + b.getZ());
	}

	private static EulerAngle multiply(EulerAngle a, double m) {
		return new EulerAngle(a.getX() * m, a.getY() * m, a.getZ() * m);
	}

}
