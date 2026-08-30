package nadiendev.exdeorumkjs.material;

import dev.latvian.mods.kubejs.item.ItemBuilder;
import nadiendev.exdeorumkjs.builder.HammerItemBuilder;
import nadiendev.exdeorumkjs.builder.MeshItemBuilder;
import nadiendev.exdeorumkjs.builder.OreChunkItemBuilder;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;

import java.util.function.Function;

public enum ItemKind {
    MESH("mesh", MeshItemBuilder::new),
    HAMMER("hammer", HammerItemBuilder.Normal::new),
    COMPRESSED_HAMMER("compressed_hammer", HammerItemBuilder.Compressed::new),
    ORE_CHUNK("ore_chunk", OreChunkItemBuilder::new);

    public final String id;
    private final Function<ResourceLocation, ? extends ItemBuilder> factory;

    ItemKind(String id, Function<ResourceLocation, ? extends ItemBuilder> factory) {
        this.id = id;
        this.factory = factory;
    }

    public ItemBuilder create(ResourceLocation id) {
        return factory.apply(id);
    }

    @Nullable
    public static ItemKind byId(String id) {
        for (ItemKind kind : values()) {
            if (kind.id.equals(id)) {
                return kind;
            }
        }

        return null;
    }
}
