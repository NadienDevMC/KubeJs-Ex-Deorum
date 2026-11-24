package nadiendev.exdeorumkjs;

import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.fml.common.Mod;

@Mod(ExDeorumKJS.MOD_ID)
public final class ExDeorumKJS {
    public static final String MOD_ID = "exdeorumkjs";
    public static final String EXDEORUM_ID = "exdeorum";
    public static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(MOD_ID);

    // Forge needs a @Mod class for the mod to load; all the work happens in the KubeJS plugin.
    public ExDeorumKJS() {
    }

    public static ResourceLocation id(String path) {
        return new ResourceLocation(MOD_ID, path);
    }

    public static ResourceLocation exdeorum(String path) {
        return new ResourceLocation(EXDEORUM_ID, path);
    }
}
