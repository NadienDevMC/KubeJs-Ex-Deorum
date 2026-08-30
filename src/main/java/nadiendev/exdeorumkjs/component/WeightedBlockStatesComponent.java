package nadiendev.exdeorumkjs.component;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.serialization.Codec;
import com.mojang.serialization.JsonOps;
import dev.latvian.mods.kubejs.recipe.RecipeScriptContext;
import dev.latvian.mods.kubejs.recipe.component.RecipeComponent;
import dev.latvian.mods.kubejs.recipe.component.RecipeComponentType;
import dev.latvian.mods.kubejs.util.JsonUtils;
import dev.latvian.mods.rhino.type.TypeInfo;
import nadiendev.exdeorumkjs.ExDeorumKJS;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.block.state.BlockState;
import org.jspecify.annotations.Nullable;
import thedarkcolour.exdeorum.recipe.RecipeUtil;
import thedarkcolour.exdeorum.recipe.WeightedList;

import java.util.Map;

public record WeightedBlockStatesComponent(ResourceKey<RecipeComponentType<?>> type) implements RecipeComponent<WeightedList<BlockState>> {
    public static final WeightedBlockStatesComponent WEIGHTED_BLOCK_STATES =
        new WeightedBlockStatesComponent(RecipeComponentType.key(ExDeorumKJS.id("weighted_block_states")));

    private static final Codec<WeightedList<BlockState>> CODEC =
        WeightedList.codec(Codec.STRING.xmap(RecipeUtil::parseBlockState, RecipeUtil::writeBlockState));

    @Override
    public Codec<WeightedList<BlockState>> codec() {
        return CODEC;
    }

    @Override
    public TypeInfo typeInfo() {
        return TypeInfo.RAW_LIST.or(TypeInfo.RAW_MAP);
    }

    @Override
    @SuppressWarnings("unchecked")
    public WeightedList<BlockState> wrap(RecipeScriptContext cx, @Nullable Object from) {
        if (from instanceof WeightedList<?> list) {
            return (WeightedList<BlockState>) list;
        }

        return CODEC.parse(JsonOps.INSTANCE, normalize(JsonUtils.of(cx.cx(), from))).getOrThrow();
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
    public boolean allowEmpty() {
        return true;
    }

    @Override
    public boolean isEmpty(WeightedList<BlockState> value) {
        return value == null || value.isEmpty();
    }

    @Override
    public String toString() {
        return type.identifier().toString();
    }
}
