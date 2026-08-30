package nadiendev.exdeorumkjs.builder;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import dev.latvian.mods.kubejs.item.custom.HandheldItemBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import org.jetbrains.annotations.Nullable;
import thedarkcolour.exdeorum.item.HammerItem;
import thedarkcolour.exdeorum.tag.EItemTags;

import java.util.List;
import java.util.Map;

public class HammerItemBuilder extends HandheldItemBuilder implements ExDeorumRecipeGen.Source {
    protected transient @Nullable String material;
    protected transient String handle = "#c:rods/wooden";

    public HammerItemBuilder(ResourceLocation id, ResourceLocation tag) {
        super(id, 5F, -3F);
        tag(new ResourceLocation[]{tag});
        ExDeorumRecipeGen.add(this);
    }

    /**
     * For a normal hammer, the head material. For a compressed hammer, the normal hammer that nine
     * of go into it — that one defaults to the same id without the {@code compressed_} prefix.
     */
    public HammerItemBuilder material(String material) {
        this.material = material;
        return this;
    }

    public HammerItemBuilder handle(String handle) {
        this.handle = handle;
        return this;
    }

    public HammerItemBuilder noRecipe() {
        this.material = null;
        return this;
    }

    @Override
    public Item createObject() {
        itemAttributeModifiers = net.minecraft.world.item.DiggerItem.createAttributes(toolTier, attackDamageBaseline, speedBaseline);
        return new HammerItem(toolTier, createItemProperties());
    }

    @Override
    public void generateRecipes(Map<ResourceLocation, JsonElement> out) {
        if (material == null) {
            return;
        }

        JsonObject key = new JsonObject();
        key.add("m", ExDeorumRecipeGen.ingredient(material));
        key.add("s", ExDeorumRecipeGen.ingredient(handle));

        out.put(ExDeorumRecipeGen.recipeId(id, ""), ExDeorumRecipeGen.shaped("equipment", key, List.of(" m ", " sm", "s  "), id));
    }

    public static class Normal extends HammerItemBuilder {
        public Normal(ResourceLocation id) {
            super(id, EItemTags.HAMMERS.location());
        }
    }

    public static class Compressed extends HammerItemBuilder {
        private transient boolean generate = true;

        public Compressed(ResourceLocation id) {
            super(id, EItemTags.COMPRESSED_HAMMERS.location());
        }

        @Override
        public HammerItemBuilder noRecipe() {
            this.generate = false;
            return this;
        }

        private String baseHammer() {
            if (material != null) {
                return material;
            }

            String path = id.getPath();
            return ResourceLocation.fromNamespaceAndPath(id.getNamespace(), path.startsWith("compressed_") ? path.substring("compressed_".length()) : path).toString();
        }

        @Override
        public void generateRecipes(Map<ResourceLocation, JsonElement> out) {
            if (!generate) {
                return;
            }

            JsonObject key = new JsonObject();
            key.add("#", ExDeorumRecipeGen.ingredient(baseHammer()));

            out.put(ExDeorumRecipeGen.recipeId(id, ""), ExDeorumRecipeGen.shaped("equipment", key, List.of("###", "###", "###"), id));
        }
    }
}
