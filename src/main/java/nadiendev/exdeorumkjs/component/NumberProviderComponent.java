package nadiendev.exdeorumkjs.component;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import dev.latvian.mods.kubejs.recipe.RecipeJS;
import dev.latvian.mods.kubejs.recipe.component.RecipeComponent;
import dev.latvian.mods.kubejs.typings.desc.DescriptionContext;
import dev.latvian.mods.kubejs.typings.desc.TypeDescJS;
import dev.latvian.mods.kubejs.util.JsonIO;
import net.minecraft.world.level.storage.loot.LootDataType;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.NumberProvider;

/**
 * Reads and writes the vanilla loot number provider that Ex Deorum uses for {@code result_amount}.
 * A plain number is a constant, anything else is parsed as a number provider object.
 */
public final class NumberProviderComponent implements RecipeComponent<NumberProvider> {
    public static final NumberProviderComponent NUMBER_PROVIDER = new NumberProviderComponent();

    private NumberProviderComponent() {
    }

    @Override
    public String componentType() {
        return "exdeorum_number_provider";
    }

    @Override
    public Class<?> componentClass() {
        return NumberProvider.class;
    }

    @Override
    public TypeDescJS constructorDescription(DescriptionContext ctx) {
        return TypeDescJS.NUMBER.or(ctx.javaType(NumberProvider.class));
    }

    @Override
    public boolean hasPriority(RecipeJS recipe, Object from) {
        return from instanceof NumberProvider || from instanceof Number || from instanceof JsonPrimitive json && json.isNumber();
    }

    @Override
    @SuppressWarnings("deprecation")
    public JsonElement write(RecipeJS recipe, NumberProvider value) {
        return LootDataType.PREDICATE.parser().toJsonTree(value);
    }

    @Override
    @SuppressWarnings("deprecation")
    public NumberProvider read(RecipeJS recipe, Object from) {
        if (from instanceof NumberProvider provider) {
            return provider;
        }

        if (from instanceof Number number) {
            return ConstantValue.exactly(number.floatValue());
        }

        JsonElement json = from instanceof JsonElement element ? element : JsonIO.of(from);

        if (json instanceof JsonPrimitive primitive && primitive.isNumber()) {
            return ConstantValue.exactly(primitive.getAsFloat());
        }

        if (json instanceof JsonObject) {
            NumberProvider provider = LootDataType.PREDICATE.parser().fromJson(json, NumberProvider.class);

            if (provider != null) {
                return provider;
            }
        }

        throw new IllegalArgumentException("Expected a number or a number provider object, got " + json);
    }

    @Override
    public String toString() {
        return componentType();
    }
}
