package nadiendev.exdeorumkjs;

import net.minecraft.resources.Identifier;

public final class ExDeorumKJS {
    public static final String MOD_ID = "exdeorumkjs";
    public static final String EXDEORUM_ID = "exdeorum";
    public static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(MOD_ID);

    private ExDeorumKJS() {
    }

    public static Identifier id(String path) {
        return Identifier.fromNamespaceAndPath(MOD_ID, path);
    }

    public static Identifier exdeorum(String path) {
        return Identifier.fromNamespaceAndPath(EXDEORUM_ID, path);
    }
}
