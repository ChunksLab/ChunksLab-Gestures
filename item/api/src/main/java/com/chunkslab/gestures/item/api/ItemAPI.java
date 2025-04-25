package com.chunkslab.gestures.item.api;

import lombok.Getter;
import lombok.Setter;

public abstract class ItemAPI {

    public static ItemAPI api;

    protected String hookName;
    @Getter @Setter
    protected ItemHook itemHook;

}