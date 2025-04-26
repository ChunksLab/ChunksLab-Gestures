package com.chunkslab.gestures.paper;

import com.chunkslab.gestures.GesturesPlugin;
import xyz.xenondevs.invui.InvUI;

public class PaperGesturesPlugin extends GesturesPlugin {
    @Override
    public void onEnable() {
        super.onEnable();
        InvUI.getInstance().setPlugin(this);
    }
}