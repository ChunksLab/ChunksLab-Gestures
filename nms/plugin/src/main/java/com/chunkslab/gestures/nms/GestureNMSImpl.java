package com.chunkslab.gestures.nms;

import com.chunkslab.gestures.nms.api.*;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.reflect.Constructor;

public class GestureNMSImpl extends GestureNMS {

    public static GestureNMS initialize(JavaPlugin plugin) {
        if(api == null)
            api = new GestureNMSImpl();

        return api;
    }

    private GestureNMSImpl() {
        getVersion();
        applyNMS();
    }

    protected void getVersion() {
        version = Bukkit.getServer().getBukkitVersion().split("-")[0];
    }

    private void applyNMS() {
        String packageName;
        switch (version) {
            case "1.17.1" -> packageName = "v1_17_R1";
            case "1.18.1", "1.18" -> packageName = "v1_18_R1";
            case "1.18.2" -> packageName = "v1_18_R2";
            case "1.19.1", "1.19.2" -> packageName = "v1_19_R1";
            case "1.19.3" -> packageName = "v1_19_R2";
            case "1.19.4" -> packageName = "v1_19_R3";
            case "1.20", "1.20.1" -> packageName = "v1_20_R1";
            case "1.20.2" -> packageName = "v1_20_R2";
            case "1.20.3", "1.20.4" -> packageName = "v1_20_R3";
            case "1.20.5", "1.20.6" -> packageName = "v1_20_R4";
            case "1.21", "1.21.1" -> packageName = "v1_21_R1";
            case "1.21.2", "1.21.3" -> packageName = "v1_21_R2";
            case "1.21.4" -> packageName = "v1_21_R3";
            default -> throw new IllegalStateException("Version not supported");
        }
        try {
            // Camera NMS
            Class<?> cameraClazz = Class.forName("com.chunkslab.gestures.nms." + packageName + ".CameraImpl");
            Constructor<?> cameraConstructor = cameraClazz.getDeclaredConstructor();
            cameraConstructor.setAccessible(true);
            setCameraNMS((CameraNMS) cameraConstructor.newInstance());
            // Mount NMS
            Class<?> mountClazz = Class.forName("com.chunkslab.gestures.nms." + packageName + ".MountImpl");
            Constructor<?> mountConstructor = mountClazz.getDeclaredConstructor();
            mountConstructor.setAccessible(true);
            setMountNMS((MountNMS) mountConstructor.newInstance());
            // ThirdPerson NMS
            Class<?> thirdPersonClazz = Class.forName("com.chunkslab.gestures.nms." + packageName + ".ThirdPersonImpl");
            Constructor<?> thirdPersonConstructor = thirdPersonClazz.getDeclaredConstructor();
            thirdPersonConstructor.setAccessible(true);
            setThirdPersonNMS((ThirdPersonNMS) thirdPersonConstructor.newInstance());
            // Wardrobe NMS
            Class<?> wardrobeClazz = Class.forName("com.chunkslab.gestures.nms." + packageName + ".WardrobeImpl");
            Constructor<?> wardrobeConstructor = wardrobeClazz.getDeclaredConstructor();
            wardrobeConstructor.setAccessible(true);
            setWardrobeNMS((WardrobeNMS) wardrobeConstructor.newInstance());
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException("Failed to initialize nms", e);
        }
    }

}