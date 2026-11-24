package nadiendev.exdeorumkjs.component;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import dev.latvian.mods.kubejs.item.OutputItem;
import dev.latvian.mods.kubejs.recipe.ItemMatch;
import dev.latvian.mods.kubejs.recipe.RecipeJS;
import dev.latvian.mods.kubejs.recipe.RecipeKey;
import dev.latvian.mods.kubejs.recipe.ReplacementMatch;
import dev.latvian.mods.kubejs.recipe.component.ComponentRole;
import dev.latvian.mods.kubejs.recipe.component.RecipeComponent;
import dev.latvian.mods.kubejs.recipe.component.RecipeComponentValue;
import dev.latvian.mods.kubejs.typings.desc.DescriptionContext;
import dev.latvian.mods.kubejs.typings.desc.TypeDescJS;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.TagParser;

import java.util.Map;

/**
 * A single item written as a plain registry id string, which is how Ex Deorum 1.20.1 stores the sieve
 * {@code result} and {@code mesh}, and the {@code result} of the hammer, crook and barrel recipes.
 * An item with NBT also writes a sibling {@code <key name>_nbt} string, which Ex Deorum reads as {@code result_nbt}.
 */
public final class ItemIdComponent implements RecipeComponent<OutputItem> {
    public static final ItemIdComponent RESULT = new ItemIdComponent(ComponentRole.OUTPUT);
    public static final ItemIdComponent MESH = new ItemIdComponent(ComponentRole.INPUT);

    private final ComponentRole role;

    private ItemIdComponent(ComponentRole role) {
        this.role = role;
    }

    @Override
    public String componentType() {
        return "exdeorum_item_id";
    }

    @Override
    public ComponentRole role() {
        return role;
    }

    @Override
    public Class<?> componentClass() {
        return OutputItem.class;
    }

    @Override
    public TypeDescJS constructorDescription(DescriptionContext ctx) {
        return TypeDescJS.STRING.or(ctx.javaType(OutputItem.class));
    }

    private static String idOf(OutputItem value) {
        return BuiltInRegistries.ITEM.getKey(value.item.getItem()).toString();
    }

    @Override
    public JsonElement write(RecipeJS recipe, OutputItem value) {
        return new JsonPrimitive(idOf(value));
    }

    @Override
    public void writeToJson(RecipeJS recipe, RecipeComponentValue<OutputItem> cv, JsonObject json) {
        json.addProperty(cv.key.name, idOf(cv.value));

        CompoundTag tag = cv.value.item.getTag();

        if (tag != null) {
            json.addProperty(cv.key.name + "_nbt", tag.toString());
        } else {
            json.remove(cv.key.name + "_nbt");
        }
    }

    @Override
    public void readFromJson(RecipeJS recipe, RecipeComponentValue<OutputItem> cv, JsonObject json) {
        JsonElement id = json.get(cv.key.name);

        if (id == null) {
            return;
        }

        cv.value = read(recipe, id);
        JsonElement nbt = json.get(cv.key.name + "_nbt");

        if (nbt != null && !cv.value.isEmpty()) {
            try {
                cv.value.item.setTag(TagParser.parseTag(nbt.getAsString()));
            } catch (Exception ignored) {
                // an unreadable NBT string is left for Ex Deorum to report when the recipe loads
            }
        }
    }

    @Override
    public void readFromMap(RecipeJS recipe, RecipeComponentValue<OutputItem> cv, Map<?, ?> map) {
        Object id = map.get(cv.key.name);

        if (id != null) {
            cv.value = read(recipe, id);
        }
    }

    @Override
    public boolean hasPriority(RecipeJS recipe, Object from) {
        return role.isOutput() ? recipe.outputItemHasPriority(from) : from instanceof OutputItem;
    }

    @Override
    public OutputItem read(RecipeJS recipe, Object from) {
        if (from instanceof JsonPrimitive primitive) {
            return OutputItem.of(primitive.getAsString());
        }

        return OutputItem.of(from);
    }

    @Override
    public boolean isInput(RecipeJS recipe, OutputItem value, ReplacementMatch match) {
        return role.isInput() && match instanceof ItemMatch m && !value.isEmpty() && m.contains(value.item);
    }

    @Override
    public boolean isOutput(RecipeJS recipe, OutputItem value, ReplacementMatch match) {
        return role.isOutput() && match instanceof ItemMatch m && !value.isEmpty() && m.contains(value.item);
    }

    @Override
    public String checkEmpty(RecipeKey<OutputItem> key, OutputItem value) {
        return value.isEmpty() ? "Item '" + key.name + "' can't be empty!" : "";
    }

    @Override
    public String toString() {
        return componentType() + "[" + role + "]";
    }
}
