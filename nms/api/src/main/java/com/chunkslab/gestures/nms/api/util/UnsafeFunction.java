package com.chunkslab.gestures.nms.api.util;

@FunctionalInterface
public interface UnsafeFunction<K, T> {
    public T apply(K var1) throws Exception;
}
