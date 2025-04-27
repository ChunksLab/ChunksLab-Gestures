/*
 * This file is part of ChunksLab-Gestures, licensed under the Apache License 2.0.
 *
 * Copyright (c) amownyy <amowny08@gmail.com>
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

package com.chunkslab.gestures.playeranimator.api.utils.math;

import lombok.AllArgsConstructor;
import org.bukkit.util.EulerAngle;

@AllArgsConstructor
public class Quaternion {

	private double x, y, z, w;

	public Quaternion(Quaternion copy) {
		this(copy.x, copy.y, copy.z, copy.w);
	}

	public static Quaternion fromEulerAngle(EulerAngle eulerAngle) {
		return fromEulerAngle(eulerAngle.getX(), eulerAngle.getY(), eulerAngle.getZ());
	}

	public static Quaternion fromEulerAngleDegree(double x, double y, double z) {
		return fromEulerAngle(Math.toRadians(x), Math.toRadians(y), Math.toRadians(z));
	}

	public static Quaternion fromEulerAngle(double x, double y, double z) {
		double cX = Math.cos(x * 0.5);
		double cY = Math.cos(y * -0.5);
		double cZ = Math.cos(z * 0.5);
		double sX = Math.sin(x * 0.5);
		double sY = Math.sin(y * -0.5);
		double sZ = Math.sin(z * 0.5);
		return new Quaternion(
				sX * cY * cZ + cX * sY * sZ,
				cX * sY * cZ - sX * cY * sZ,
				cX * cY * sZ + sX * sY * cZ,
				cX * cY * cZ - sX * sY * sZ
		);
	}

	public static Quaternion globalRotate(Quaternion origin, Quaternion delta) {
		return origin.altMul(delta);
	}

	public static Quaternion localRotate(Quaternion origin, Quaternion delta) {
		return globalRotate(delta, origin);
	}

	public Quaternion add(Quaternion other) {
		x += other.x();
		y += other.y();
		z += other.z();
		w += other.w();
		return this;
	}

	public Quaternion mul(double value) {
		x *= value;
		y *= value;
		z *= value;
		w *= value;
		return this;
	}

	public Quaternion mul(Quaternion other) {
		double aX = x();
		double aY = y();
		double aZ = z();
		double aW = w();
		double bX = other.x();
		double bY = other.y();
		double bZ = other.z();
		double bW = other.w();
		x = aW * bX + aX * bW + aY * bZ - aZ * bY;
		y = aW * bY - aX * bZ + aY * bW + aZ * bX;
		z = aW * bZ + aX * bY - aY * bX + aZ * bW;
		w = aW * bW - aX * bX - aY * bY - aZ * bZ;
		return this;
	}

	public Quaternion normalize() {
		double var0 = x() * x() + y() * y() + z() * z() + w() * w();
		if (var0 > 1e-6) {
			double sqrt = TMath.fastInvSqrt(var0);
			x *= sqrt;
			y *= sqrt;
			z *= sqrt;
			w *= sqrt;
		}else {
			x = 0;
			y = 0;
			z = 0;
			w = 0;
		}
		return this;
	}

	public double dot(Quaternion other) {
		return x * other.x + y * other.y + z * other.z + w * other.w;
	}

	public Quaternion altAdd(Quaternion other) {
		Quaternion c = copy();
		c.add(other);
		return c;
	}

	public Quaternion altMul(Quaternion other) {
		Quaternion c = copy();
		c.mul(other);
		return c;
	}

	public Quaternion altMul(double value) {
		Quaternion c = copy();
		c.mul(value);
		return c;
	}

	public Quaternion copy() {
		return new Quaternion(this);
	}

	public EulerAngle toEulerAngle() {

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
		if (Math.abs(m13) < 0.9999999) {
			ex = Math.atan2(-m23, m33);
			ez = Math.atan2(-m12, m11);
		} else {
			ex = Math.atan2(m32, m22);
			ez = 0;
		}


		return new EulerAngle(ex, -ey, ez);

	}

	public double x() {
		return this.x;
	}

	public double y() {
		return this.y;
	}

	public double z() {
		return this.z;
	}

	public double w() {
		return this.w;
	}

	@Override
	public String toString() {
		return "Quaternion{" +
				"x=" + x +
				", y=" + y +
				", z=" + z +
				", w=" + w +
				'}';
	}
}
