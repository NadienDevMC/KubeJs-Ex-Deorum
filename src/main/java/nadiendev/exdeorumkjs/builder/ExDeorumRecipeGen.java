package nadiendev.exdeorumkjs.builder;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import nadiendev.exdeorumkjs.ExDeorumKJS;
import net.minecraft.resources.ResourceLocation;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public final class ExDeorumRecipeGen {
    public interface Source {
        void generateRecipes(Map<ResourceLocation, JsonElement> out);
    }

    private static final List<Source> SOURCES = new ArrayList<>();

    private ExDeorumRecipeGen() {
    }

    public static void add(Source source) {
        SOURCES.add(source);
    }

    public static void injectInto(Map<ResourceLocation, JsonElement> recipes) {
        for (Source source : SOURCES) {
            try {
                source.generateRecipes(recipes);
            } catch (Exception ex) {
                ExDeorumKJS.LOGGER.error("Failed to generate a recipe for a custom Ex Deorum item", ex);
            }
        }
    }

    public static ResourceLocation recipeId(ResourceLocation item, String suffix) {
        return ResourceLocation.fromNamespaceAndPath(ExDeorumKJS.MOD_ID, item.getNamespace() + "/" + item.getPath() + suffix);
    }

    /**
     * Accepts either a tag (leading {@code #}) or a plain item id, the way an ingredient is written
     * in a script, and turns it into the JSON shape an ingredient uses.
     */
    public static JsonObject ingredient(String value) {
        JsonObject json = new JsonObject();

        if (value.startsWith("#")) {
            json.addProperty("tag", value.substring(1));
        } else {
            json.addProperty("item", value);
        }

        return json;
    }

    public static JsonObject shaped(String category, JsonObject key, List<String> pattern, ResourceLocation result) {
        JsonObject json = new JsonObject();
        json.addProperty("type", "minecraft:crafting_shaped");
        json.addProperty("category", category);
        json.add("key", key);

        JsonArray rows = new JsonArray();
        pattern.forEach(rows::add);
        json.add("pattern", rows);

        JsonObject resultJson = new JsonObject();
        resultJson.addProperty("id", result.toString());
        resultJson.addProperty("count", 1);
        json.add("result", resultJson);

        return json;
    }

    public static void notEmptyTagCondition(JsonObject recipe, String tag) {
        JsonObject tagEmpty = new JsonObject();
        tagEmpty.addProperty("type", "neoforge:tag_empty");
        tagEmpty.addProperty("tag", tag);

        JsonObject not = new JsonObject();
        not.addProperty("type", "neoforge:not");
        not.add("value", tagEmpty);

        JsonArray conditions = new JsonArray();
        conditions.add(not);
        recipe.add("neoforge:conditions", conditions);
    }
}
