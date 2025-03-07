package com.chunkslab.gestures.nms.api.util;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Method;

public class ReflectionUtils {

    @Nullable
    public static Method getDeclaredMethod(final Class<?> clazz, Class<?> returnType, final String[] possibleMethodNames, final Class<?>... parameterTypes) {
        outer:
        for (Method method : clazz.getDeclaredMethods()) {
            if (method.getParameterCount() != parameterTypes.length) {
                continue;
            }
            Class<?>[] types = method.getParameterTypes();
            for (int i = 0; i < types.length; i++) {
                if (types[i] != parameterTypes[i]) {
                    continue outer;
                }
            }
            for (String name : possibleMethodNames) {
                if (name.equals(method.getName())) {
                    if (returnType.isAssignableFrom(method.getReturnType())) {
                        return setAccessible(method);
                    }
                }
            }
        }
        return null;
    }

    @Nullable
    public static Method getDeclaredMethod(final Class<?> clazz, Class<?> returnType, final Class<?>... parameterTypes) {
        outer:
        for (Method method : clazz.getDeclaredMethods()) {
            if (method.getParameterCount() != parameterTypes.length) {
                continue;
            }
            Class<?>[] types = method.getParameterTypes();
            for (int i = 0; i < types.length; i++) {
                if (types[i] != parameterTypes[i]) {
                    continue outer;
                }
            }
            if (returnType.isAssignableFrom(method.getReturnType())) return setAccessible(method);
        }
        return null;
    }

    @NotNull
    public static <T extends AccessibleObject> T setAccessible(@NotNull final T o) {
        o.setAccessible(true);
        return o;
    }

}
