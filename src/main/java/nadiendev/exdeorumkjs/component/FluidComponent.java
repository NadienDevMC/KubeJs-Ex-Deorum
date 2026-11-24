package nadiendev.exdeorumkjs.component;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import dev.latvian.mods.kubejs.fluid.FluidStackJS;
import dev.latvian.mods.kubejs.fluid.FluidLike;
import dev.latvian.mods.kubejs.recipe.RecipeJS;
import dev.latvian.mods.kubejs.recipe.ReplacementMatch;
import dev.latvian.mods.kubejs.recipe.component.ComponentRole;
import dev.latvian.mods.kubejs.recipe.component.RecipeComponent;
import dev.latvian.mods.kubejs.typings.desc.DescriptionContext;
import dev.latvian.mods.kubejs.typings.desc.TypeDescJS;
import dev.latvian.mods.kubejs.util.JsonIO;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.fluids.FluidStack;

/**
 * A fluid that is written as a plain registry id string, which is how Ex Deorum 1.20.1 stores
 * {@code result_fluid}, {@code base_fluid} and {@code additive_fluid}.
 */
public final class FluidComponent implements RecipeComponent<Fluid> {
    public static final FluidComponent INPUT = new FluidComponent(ComponentRole.INPUT);
    public static final FluidComponent OUTPUT = new FluidComponent(ComponentRole.OUTPUT);

    private final ComponentRole role;

    private FluidComponent(ComponentRole role) {
        this.role = role;
    }

    @Override
    public ComponentRole role() {
        return role;
    }

    @Override
    public boolean isInput(RecipeJS recipe, Fluid value, ReplacementMatch match) {
        return role.isInput() && match instanceof FluidLike like && FluidStackJS.of(value).matches(like);
    }

    @Override
    public boolean isOutput(RecipeJS recipe, Fluid value, ReplacementMatch match) {
        return role.isOutput() && match instanceof FluidLike like && FluidStackJS.of(value).matches(like);
    }

    @Override
    public String componentType() {
        return "exdeorum_fluid";
    }

    @Override
    public Class<?> componentClass() {
        return Fluid.class;
    }

    @Override
    public TypeDescJS constructorDescription(DescriptionContext ctx) {
        return TypeDescJS.STRING.or(ctx.javaType(Fluid.class));
    }

    @Override
    public JsonElement write(RecipeJS recipe, Fluid value) {
        return new JsonPrimitive(BuiltInRegistries.FLUID.getKey(value).toString());
    }

    @Override
    public Fluid read(RecipeJS recipe, Object from) {
        return of(from);
    }

    public static Fluid of(Object source) {
        Object from = source;

        if (from instanceof Fluid fluid) {
            return fluid;
        }

        if (from instanceof FluidStack stack) {
            return stack.getFluid();
        }

        if (from instanceof FluidStackJS stack) {
            return stack.getFluid();
        }

        if (from instanceof java.util.Map<?, ?>) {
            from = JsonIO.of(from);
        }

        String id;

        if (from instanceof JsonObject object) {
            JsonElement fluid = object.has("fluid") ? object.get("fluid") : object.get("id");

            if (fluid == null || !fluid.isJsonPrimitive()) {
                throw new IllegalArgumentException("Expected a fluid id, got " + object);
            }

            id = fluid.getAsString();
        } else if (from instanceof JsonPrimitive primitive) {
            id = primitive.getAsString();
        } else {
            id = String.valueOf(from);
        }

        // Accept the KubeJS "<id> <amount>" notation as well, the amount is dropped here
        id = id.trim().split(" ", 2)[0];
        ResourceLocation location = new ResourceLocation(id);
        Fluid fluid = BuiltInRegistries.FLUID.get(location);

        if (fluid == Fluids.EMPTY) {
            throw new IllegalArgumentException("Unknown fluid '" + id + "'");
        }

        return fluid;
    }

    @Override
    public String toString() {
        return componentType();
    }
}
