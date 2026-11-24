package nadiendev.exdeorumkjs.recipe;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import dev.latvian.mods.kubejs.recipe.RecipeJS;
import dev.latvian.mods.kubejs.recipe.RecipeKey;
import dev.latvian.mods.kubejs.recipe.RecipeTypeFunction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

/**
 * Ex Deorum 1.20.1 has no dedicated ore chunk recipe type: a modded ore block made from four chunks is the
 * {@code exdeorum:tag_result} recipe, a 2x2 shaped recipe whose result is picked from an item tag when the
 * recipe loads. This recipe keeps the {@code exdeorum:ore_chunk(ore, oreChunk)} script API and writes that
 * wrapper when it is serialized.
 */
public class OreChunkKubeRecipe extends RecipeJS {
    public static final ResourceLocation TAG_RESULT = new ResourceLocation("exdeorum", "tag_result");

    private final RecipeKey<TagKey<Item>> ore;
    private final RecipeKey<?> chunk;

    public OreChunkKubeRecipe(RecipeKey<TagKey<Item>> ore, RecipeKey<?> chunk) {
        this.ore = ore;
        this.chunk = chunk;
    }

    @Override
    public void serialize() {
        super.serialize();

        JsonElement tag = json.remove(ore.name);
        JsonElement ingredient = json.remove(chunk.name);

        if (tag == null || ingredient == null) {
            return;
        }

        JsonObject key = new JsonObject();
        key.add("#", ingredient);

        JsonArray pattern = new JsonArray();
        pattern.add("##");
        pattern.add("##");

        // Ex Deorum swaps this placeholder item for the preferred item of the tag
        JsonObject result = new JsonObject();
        result.addProperty("item", "minecraft:air");

        JsonObject recipe = new JsonObject();
        recipe.addProperty("type", "minecraft:crafting_shaped");
        recipe.addProperty("category", "misc");
        recipe.add("key", key);
        recipe.add("pattern", pattern);
        recipe.add("result", result);

        json.add("result_tag", tag);
        json.add("recipe", recipe);
    }

    @Override
    public RecipeTypeFunction getSerializationTypeFunction() {
        RecipeTypeFunction function = type.event.getRecipeFunction(TAG_RESULT.toString());
        return function == null ? type : function;
    }

    @Override
    public ResourceLocation getOrCreateId() {
        if (id == null) {
            TagKey<Item> tag = getValue(ore);
            String path = tag == null ? null : tag.location().getPath().replace('/', '_');

            if (path == null) {
                return super.getOrCreateId();
            }

            id = type.event.takeId(this, "exdeorum:kjs/ore_chunk_", path);
        }

        return id;
    }
}
