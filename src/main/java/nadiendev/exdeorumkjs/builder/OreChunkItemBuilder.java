package nadiendev.exdeorumkjs.builder;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import dev.latvian.mods.kubejs.item.ItemBuilder;
import net.minecraft.resources.Identifier;
import org.jspecify.annotations.Nullable;

import java.util.Map;

public class OreChunkItemBuilder extends ItemBuilder implements ExDeorumRecipeGen.Source {
    private transient @Nullable String ore;
    private transient boolean generate = true;

    public OreChunkItemBuilder(Identifier id) {
        super(id);
        ExDeorumRecipeGen.add(this);
    }

    /**
     * The ore tag the four chunks craft into. Defaults to {@code c:ores/<name>}, derived from the
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

        return "c:ores/" + path;
    }

    @Override
    public void generateRecipes(Map<Identifier, JsonElement> out) {
        if (!generate) {
            return;
        }

        String tag = oreTag();

        JsonObject json = new JsonObject();
        ExDeorumRecipeGen.notEmptyTagCondition(json, tag);
        json.addProperty("type", "exdeorum:ore_chunk");
        json.addProperty("ore", tag);
        json.add("ore_chunk", ExDeorumRecipeGen.ingredient(id.toString()));

        out.put(ExDeorumRecipeGen.recipeId(id, "_from_chunks"), json);
    }
}
