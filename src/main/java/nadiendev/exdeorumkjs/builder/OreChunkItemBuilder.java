package nadiendev.exdeorumkjs.builder;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import dev.latvian.mods.kubejs.item.custom.BasicItemJS;
import net.minecraft.resources.ResourceLocation;

import java.util.List;
import java.util.Map;

public class OreChunkItemBuilder extends BasicItemJS.Builder implements ExDeorumRecipeGen.Source {
    private transient String ore;
    private transient boolean generate = true;

    public OreChunkItemBuilder(ResourceLocation id) {
        super(id);
        ExDeorumRecipeGen.add(this);
    }

    /**
     * The ore tag the four chunks craft into. Defaults to {@code forge:ores/<name>}, derived from the
     * item id by dropping a trailing {@code _ore_chunk} or {@code _chunk}.
     */
    public OreChunkItemBuilder ore(String ore) {
        this.ore = ore;
        return this;
    }

    public OreChunkItemBuilder noRecipe() {
        this.generate = false;
        return this;
    }

    private String oreTag() {
        if (ore != null) {
            return ore.startsWith("#") ? ore.substring(1) : ore;
        }

        String path = id.getPath();

        if (path.endsWith("_ore_chunk")) {
            path = path.substring(0, path.length() - "_ore_chunk".length());
        } else if (path.endsWith("_chunk")) {
            path = path.substring(0, path.length() - "_chunk".length());
        }

        return "forge:ores/" + path;
    }

    /**
     * Ex Deorum crafts ore chunks through its {@code exdeorum:tag_result} wrapper, which swaps the
     * placeholder result for the preferred item of the ore tag and skips the recipe if the tag is empty.
     */
    @Override
    public void generateRecipes(Map<ResourceLocation, JsonElement> out) {
        if (!generate) {
            return;
        }

        String tag = oreTag();

        JsonObject key = new JsonObject();
        key.add("#", ExDeorumRecipeGen.ingredient(id.toString()));

        JsonObject inner = ExDeorumRecipeGen.shaped("misc", key, List.of("##", "##"), new ResourceLocation("minecraft:air"));

        JsonObject json = new JsonObject();
        ExDeorumRecipeGen.notEmptyTagCondition(json, tag);
        json.addProperty("type", "exdeorum:tag_result");
        json.addProperty("result_tag", tag);
        json.add("recipe", inner);

        out.put(ExDeorumRecipeGen.recipeId(id, "_from_chunks"), json);
    }
}
