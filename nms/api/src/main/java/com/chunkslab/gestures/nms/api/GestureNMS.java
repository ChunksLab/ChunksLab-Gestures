package com.chunkslab.gestures.nms.api;

import lombok.Getter;
import lombok.Setter;

public abstract class GestureNMS {

    public static GestureNMS api;

    protected String version;
    @Getter @Setter
    protected CameraNMS cameraNMS;
    @Getter @Setter
    protected MountNMS mountNMS;
    @Getter @Setter
    protected ThirdPersonNMS thirdPersonNMS;
    @Getter @Setter
    protected WardrobeNMS wardrobeNMS;

}