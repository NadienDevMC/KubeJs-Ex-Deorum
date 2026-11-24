package nadiendev.exdeorumkjs.builder;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import dev.latvian.mods.kubejs.item.custom.HandheldItemBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import thedarkcolour.exdeorum.item.CompressedHammerItem;
import thedarkcolour.exdeorum.item.HammerItem;
import thedarkcolour.exdeorum.tag.EItemTags;

import java.util.List;
import java.util.Map;

public abstract class HammerItemBuilder extends HandheldItemBuilder implements ExDeorumRecipeGen.Source {
    protected transient String material;
    protected transient String handle = "#forge:rods/wooden";

    public HammerItemBuilder(ResourceLocation id, ResourceLocation tag) {
        // Attack damage and speed are fixed by Ex Deorum's hammer item, the baselines only fill the builder fields.
        super(id, 1F, -2.8F);
        tag(tag);
        ExDeorumRecipeGen.add(this);
    }

    /**
     * For a normal hammer, the head material. For a compressed hammer, the normal hammer that nine
     * of go into it - that one defaults to the same id without the {@code compressed_} prefix.
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

    public static class Normal extends HammerItemBuilder {
        public Normal(ResourceLocation id) {
            super(id, EItemTags.HAMMERS.location());
        }

        @Override
        public Item createObject() {
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
    }

    public static class Compressed extends HammerItemBuilder {
        private transient boolean generate = true;

        public Compressed(ResourceLocation id) {
            super(id, EItemTags.COMPRESSED_HAMMERS.location());
        }

        @Override
        public Item createObject() {
            return new CompressedHammerItem(toolTier, createItemProperties());
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
            return new ResourceLocation(id.getNamespace(), path.startsWith("compressed_") ? path.substring("compressed_".length()) : path).toString();
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
