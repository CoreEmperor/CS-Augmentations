package net.corespring.csaugmentations.Utility;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraftforge.client.settings.KeyConflictContext;
import org.lwjgl.glfw.GLFW;

public class KeyBinding {
    private static final String KEY_CATEGORY_CSAUGMENTATIONS = "key.category.csaugmentations.augmentation";

    private static final String KEY_ZOOM = "key.csaugmentations.zoom";
    public static final KeyMapping ZOOM_KEY = new KeyMapping(KEY_ZOOM, KeyConflictContext.IN_GAME,
            InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_C, KEY_CATEGORY_CSAUGMENTATIONS);
    private static final String KEY_NVG = "key.csaugmentations.nvg";
    public static final KeyMapping NVG_KEY = new KeyMapping(KEY_NVG, KeyConflictContext.IN_GAME,
            InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_KP_3, KEY_CATEGORY_CSAUGMENTATIONS);
    private static final String KEY_DISABLE_ARMS = "key.csaugmentations.disable_arms";
    public static final KeyMapping ARMS_KEY = new KeyMapping(KEY_DISABLE_ARMS, KeyConflictContext.IN_GAME,
            InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_KP_1, KEY_CATEGORY_CSAUGMENTATIONS);
    private static final String KEY_DISABLE_LEGS = "key.csaugmentations.disable_legs";
    public static final KeyMapping LEGS_KEY = new KeyMapping(KEY_DISABLE_LEGS, KeyConflictContext.IN_GAME,
            InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_KP_2, KEY_CATEGORY_CSAUGMENTATIONS);
    private static final String KEY_SPINE = "key.csaugmentations.activate_spine";
    public static final KeyMapping SPINE_KEY = new KeyMapping(KEY_SPINE, KeyConflictContext.IN_GAME,
            InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_V, KEY_CATEGORY_CSAUGMENTATIONS);

}
