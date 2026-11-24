package nadiendev.exdeorumkjs.component;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import dev.latvian.mods.kubejs.fluid.EmptyFluidStackJS;
import dev.latvian.mods.kubejs.fluid.FluidLike;
import dev.latvian.mods.kubejs.fluid.FluidStackJS;
import dev.latvian.mods.kubejs.recipe.RecipeJS;
import dev.latvian.mods.kubejs.recipe.ReplacementMatch;
import dev.latvian.mods.kubejs.recipe.component.ComponentRole;
import dev.latvian.mods.kubejs.recipe.component.RecipeComponent;
import dev.latvian.mods.kubejs.recipe.component.RecipeComponentValue;
import dev.latvian.mods.kubejs.typings.desc.DescriptionContext;
import dev.latvian.mods.kubejs.typings.desc.TypeDescJS;
import dev.latvian.mods.kubejs.util.JsonIO;

import java.util.Map;

/**
 * A fluid with an amount. Ex Deorum 1.20.1 stores these as two sibling properties, the fluid id under
 * the key name and the amount in millibuckets under {@code <key name>_amount}, for example
 * {@code fluid} and {@code fluid_amount}, or {@code base_fluid} and {@code base_fluid_amount}.
 */
public final class FluidAmountComponent implements RecipeComponent<FluidStackJS> {
    public static final FluidAmountComponent FLUID_AMOUNT = new FluidAmountComponent();

    private FluidAmountComponent() {
    }

    @Override
    public String componentType() {
        return "exdeorum_fluid_amount";
    }

    @Override
    public ComponentRole role() {
        return ComponentRole.INPUT;
    }

    @Override
    public Class<?> componentClass() {
        return FluidStackJS.class;
    }

    @Override
    public TypeDescJS constructorDescription(DescriptionContext ctx) {
        return TypeDescJS.STRING.or(ctx.javaType(FluidStackJS.class));
    }

    @Override
    public JsonElement write(RecipeJS recipe, FluidStackJS value) {
        return new JsonPrimitive(value.getId());
    }

    @Override
    public void writeToJson(RecipeJS recipe, RecipeComponentValue<FluidStackJS> cv, JsonObject json) {
        json.addProperty(cv.key.name, cv.value.getId());
        json.addProperty(cv.key.name + "_amount", (int) cv.value.getAmount());
    }

    @Override
    public void readFromJson(RecipeJS recipe, RecipeComponentValue<FluidStackJS> cv, JsonObject json) {
        JsonElement id = json.get(cv.key.name);

        if (id != null) {
            JsonElement amount = json.get(cv.key.name + "_amount");
            cv.value = FluidStackJS.of(id.getAsString()).withAmount(amount == null ? 1000L : amount.getAsLong());
        }
    }

    @Override
    public void readFromMap(RecipeJS recipe, RecipeComponentValue<FluidStackJS> cv, Map<?, ?> map) {
        Object id = map.get(cv.key.name);

        if (id != null) {
            FluidStackJS stack = read(recipe, id);

            if (map.get(cv.key.name + "_amount") instanceof Number amount) {
                stack = stack.withAmount(amount.longValue());
            }

            cv.value = stack;
        }
    }

    @Override
    public boolean hasPriority(RecipeJS recipe, Object from) {
        return from instanceof FluidStackJS;
    }

    @Override
    public FluidStackJS read(RecipeJS recipe, Object from) {
        Object source = from instanceof Map<?, ?> ? JsonIO.of(from) : from;
        FluidStackJS stack = FluidStackJS.of(source);

        if (stack.kjs$isEmpty()) {
            throw new IllegalArgumentException("Unknown or empty fluid '" + from + "'");
        }

        return stack;
    }

    @Override
    public boolean isInput(RecipeJS recipe, FluidStackJS value, ReplacementMatch match) {
        return match instanceof FluidLike like && value.matches(like);
    }

    @Override
    public String checkEmpty(dev.latvian.mods.kubejs.recipe.RecipeKey<FluidStackJS> key, FluidStackJS value) {
        return value == EmptyFluidStackJS.INSTANCE || value.kjs$isEmpty() ? "Fluid '" + key.name + "' can't be empty!" : "";
    }

    @Override
    public String toString() {
        return componentType();
    }
}
