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

package com.chunkslab.gestures.playeranimator.api.utils.math;

import org.bukkit.util.EulerAngle;
import org.bukkit.util.Vector;

public class TMath {

	public static double clamp(double value, double min, double max) {
		return Math.min(Math.max(value, min), max);
	}

	public static float fastInvSqrt(float value) {
		float half = 0.5f * value;
		int rawBits = Float.floatToIntBits(value);
		rawBits = 1597463007 - (rawBits >> 1);
		value = Float.intBitsToFloat(rawBits);
		value *= 1.5 - half * value * value;
		return value;
	}

	public static double fastInvSqrt(double value) {
		double half = 0.5 * value;
		long rawBits = Double.doubleToRawLongBits(value);
		rawBits = 6910469410427058090L - (rawBits >> 1);
		value = Double.longBitsToDouble(rawBits);
		value *= 1.5 - half * value * value;
		return value;
	}

	// ROTATIONS
	public static EulerAngle makeAngle(double x, double y, double z) {
		return new EulerAngle(Math.toRadians(x), Math.toRadians(y), Math.toRadians(z));
	}

	public static EulerAngle globalRotate(EulerAngle origin, EulerAngle delta) {
		return Quaternion.globalRotate(Quaternion.fromEulerAngle(origin), Quaternion.fromEulerAngle(delta)).toEulerAngle();
	}

	public static EulerAngle localRotate(EulerAngle origin, EulerAngle delta) {
		return globalRotate(delta, origin);
	}

	// LERPS
	public static double lerp(double a, double b, double t) {
		return (1 - t) * a + t * b;
	}

	public static double lerp(double a, double b, double aT, double bT) {
		return aT * a + bT * b;
	}

	public static Vector lerp(Vector a, Vector b, double t) {
		return new Vector(
				lerp(a.getX(), b.getX(), t),
				lerp(a.getY(), b.getY(), t),
				lerp(a.getZ(), b.getZ(), t)
		);
	}

	public static Vector lerp(Vector a, Vector b, double aT, double bT) {
		return new Vector(
				lerp(a.getX(), b.getX(), aT, bT),
				lerp(a.getY(), b.getY(), aT, bT),
				lerp(a.getZ(), b.getZ(), aT, bT)
		);
	}

	public static Vector smoothLerp(Vector a, Vector b, Vector c, Vector d, double t) {
		double t0 = 0, t1 = 1, t2 = 2, t3 = 3;
		t = (t2 - t1) * t + t1;

		Vector a1 = lerp(a, b, (t1 - t) / (t1 - t0), (t - t0) / (t1 - t0));
		Vector a2 = lerp(b, c, (t2 - t) / (t2 - t1), (t - t1) / (t2 - t1));
		Vector a3 = lerp(c, d, (t3 - t) / (t3 - t2), (t - t2) / (t3 - t2));

		Vector b1 = lerp(a1, a2, (t2 - t) / (t2 - t0), (t - t0) / (t2 - t0));
		Vector b2 = lerp(a2, a3, (t3 - t) / (t3 - t1), (t - t1) / (t3 - t1));

		return lerp(b1, b2, (t2 - t) / (t2 - t1), (t - t1) / (t2 - t1));
	}

	public static EulerAngle lerp(EulerAngle a, EulerAngle b, double t) {
		return new EulerAngle(
				TMath.lerp(a.getX(), b.getX(), t),
				TMath.lerp(a.getY(), b.getY(), t),
				TMath.lerp(a.getZ(), b.getZ(), t)
		);
	}

	public static EulerAngle lerp(EulerAngle a, EulerAngle b, double aT, double bT) {
		return new EulerAngle(
				TMath.lerp(a.getX(), b.getX(), aT, bT),
				TMath.lerp(a.getY(), b.getY(), aT, bT),
				TMath.lerp(a.getZ(), b.getZ(), aT, bT)
		);
	}

	public static EulerAngle smoothLerp(EulerAngle a, EulerAngle b, EulerAngle c, EulerAngle d, double t) {
		double t0 = 0, t1 = 1, t2 = 2, t3 = 3;
		t = (t2 - t1) * t + t1;

		EulerAngle a1 = lerp(a, b, (t1 - t) / (t1 - t0), (t - t0) / (t1 - t0));
		EulerAngle a2 = lerp(b, c, (t2 - t) / (t2 - t1), (t - t1) / (t2 - t1));
		EulerAngle a3 = lerp(c, d, (t3 - t) / (t3 - t2), (t - t2) / (t3 - t2));

		EulerAngle b1 = lerp(a1, a2, (t2 - t) / (t2 - t0), (t - t0) / (t2 - t0));
		EulerAngle b2 = lerp(a2, a3, (t3 - t) / (t3 - t1), (t - t1) / (t3 - t1));

		return lerp(b1, b2, (t2 - t) / (t2 - t1), (t - t1) / (t2 - t1));
	}

	public static EulerAngle slerp(EulerAngle a, EulerAngle b, double t) {
		return slerp(Quaternion.fromEulerAngle(a), Quaternion.fromEulerAngle(b), t).toEulerAngle();
	}

	public static Quaternion slerp(Quaternion a, Quaternion b, double t) {
		return slerp(a, b, 1 - t, t);
	}

	public static Quaternion slerp(Quaternion a, Quaternion b, double aT, double bT) {
		double diff = a.dot(b), absDiff = Math.abs(diff);

		if(absDiff > 0.9995) {
			double theta = Math.acos(absDiff);
			double sinTheta = Math.sin(theta);

			aT = Math.sin(theta * aT) / sinTheta;
			bT = Math.sin(theta * bT) / sinTheta;
			if (diff < 0)
				aT *= -1;
		}
		// Quaternion result = new Quaternion(a.x() * aT + b.x() * bT,a.y() * aT + b.y() * bT,a.z() * aT + b.z() * bT,a.w() * aT + b.w() * bT);
		return a.altMul(aT).add(b.altMul(bT)).normalize();
	}

	public static Quaternion smoothSlerp(Quaternion a, Quaternion b, Quaternion c, Quaternion d, double t) {
		double t0 = 0, t1 = 1, t2 = 2, t3 = 3;
		t = (t2 - t1) * t + t1;

		Quaternion a1 = slerp(a, b, (t1 - t) / (t1 - t0), (t - t0) / (t1 - t0));
		Quaternion a2 = slerp(b, c, (t2 - t) / (t2 - t1), (t - t1) / (t2 - t1));
		Quaternion a3 = slerp(c, d, (t3 - t) / (t3 - t2), (t - t2) / (t3 - t2));

		Quaternion b1 = slerp(a1, a2, (t2 - t) / (t2 - t0), (t - t0) / (t2 - t0));
		Quaternion b2 = slerp(a2, a3, (t3 - t) / (t3 - t1), (t - t1) / (t3 - t1));

		return slerp(b1, b2, (t2 - t) / (t2 - t1), (t - t1) / (t2 - t1));
	}

	// To Strings
	public static String toString(EulerAngle angle) {
		return String.format("[%s, %s, %s]", Math.toDegrees(angle.getX()), Math.toDegrees(angle.getY()), Math.toDegrees(angle.getZ()));
	}

}
