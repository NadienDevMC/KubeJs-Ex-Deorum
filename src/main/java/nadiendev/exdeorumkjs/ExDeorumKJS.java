package nadiendev.exdeorumkjs;

import net.minecraft.resources.ResourceLocation;

public final class ExDeorumKJS {
    public static final String MOD_ID = "exdeorumkjs";
    public static final String EXDEORUM_ID = "exdeorum";
    public static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(MOD_ID);

    private ExDeorumKJS() {
    }

    public static ResourceLocation id(String path) {
        return ResourceLocation.fromNamespaceAndPath(MOD_ID, path);
    }

    public static ResourceLocation exdeorum(String path) {
        return ResourceLocation.fromNamespaceAndPath(EXDEORUM_ID, path);
    }
}
