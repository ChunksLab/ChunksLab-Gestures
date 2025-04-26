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

import java.lang.reflect.Field;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class FieldUtils {

	private static final Map<Class<?>, ConcurrentHashMap<String, Field>> fieldMap = new ConcurrentHashMap<>();

	public static void unlockField(Class<?> clazz, String field) {
		try {
			Field f = clazz.getDeclaredField(field);
			f.setAccessible(true);

			ConcurrentHashMap<String, Field> map = fieldMap.get(clazz);
			if(map == null)
				map = new ConcurrentHashMap<>();
			map.put(field, f);
			fieldMap.put(clazz, map);
		} catch (NoSuchFieldException | SecurityException | IllegalArgumentException e) {
			e.printStackTrace();
		}
	}

	public static Field getField(Class<?> clazz, String field) {
		if(!fieldMap.containsKey(clazz) || ! fieldMap.get(clazz).containsKey(field))
			unlockField(clazz, field);
		return fieldMap.get(clazz).get(field);
	}

}