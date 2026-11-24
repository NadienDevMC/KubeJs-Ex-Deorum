package nadiendev.exdeorumkjs.component;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import dev.latvian.mods.kubejs.recipe.RecipeJS;
import dev.latvian.mods.kubejs.recipe.component.RecipeComponent;
import dev.latvian.mods.kubejs.typings.desc.DescriptionContext;
import dev.latvian.mods.kubejs.typings.desc.TypeDescJS;
import dev.latvian.mods.kubejs.util.JsonIO;
import net.minecraft.world.level.block.state.BlockState;
import thedarkcolour.exdeorum.recipe.RecipeUtil;
import thedarkcolour.exdeorum.recipe.WeightedList;

import java.util.Map;

/**
 * A weighted list of block states, written as an array of objects with a "value" block state string
 * and a "weight". Scripts can pass that array, an array of block state strings (weight 1 each) or an
 * object that maps block state strings to weights.
 */
public final class WeightedBlockStatesComponent implements RecipeComponent<WeightedList<BlockState>> {
    public static final WeightedBlockStatesComponent WEIGHTED_BLOCK_STATES = new WeightedBlockStatesComponent();

    private WeightedBlockStatesComponent() {
    }

    public static WeightedList<BlockState> empty() {
        return WeightedList.<BlockState>builder().build();
    }

    @Override
    public String componentType() {
        return "exdeorum_weighted_block_states";
    }

    @Override
    public Class<?> componentClass() {
        return WeightedList.class;
    }

    @Override
    public TypeDescJS constructorDescription(DescriptionContext ctx) {
        return TypeDescJS.ANY;
    }

    @Override
    public JsonElement write(RecipeJS recipe, WeightedList<BlockState> value) {
        return value.toJson(RecipeUtil::writeBlockState);
    }

    @Override
    @SuppressWarnings("unchecked")
    public WeightedList<BlockState> read(RecipeJS recipe, Object from) {
        if (from instanceof WeightedList<?> list) {
            return (WeightedList<BlockState>) list;
        }

        JsonElement json = from instanceof JsonElement element ? element : JsonIO.of(from);
        WeightedList<BlockState> list = WeightedList.fromJson(normalize(json), element -> RecipeUtil.parseBlockState(element.getAsString()));

        if (list == null) {
            throw new IllegalArgumentException("Failed to read the weighted block states " + json);
        }

        return list;
    }

    private static JsonArray normalize(JsonElement json) {
        JsonArray result = new JsonArray();

        if (json instanceof JsonObject object) {
            for (Map.Entry<String, JsonElement> entry : object.entrySet()) {
                result.add(entry(entry.getKey(), entry.getValue().getAsInt()));
            }

            return result;
        }

        if (json instanceof JsonArray array) {
            for (JsonElement element : array) {
                if (element.isJsonPrimitive()) {
                    result.add(entry(element.getAsString(), 1));
                } else {
                    result.add(element);
                }
            }

            return result;
        }

        throw new IllegalArgumentException("Expected an array or an object of weighted block states, got " + json);
    }

    private static JsonObject entry(String value, int weight) {
        JsonObject entry = new JsonObject();
        entry.addProperty("value", value);
        entry.addProperty("weight", weight);
        return entry;
    }

    @Override
    public String toString() {
        return componentType();
    }
}
