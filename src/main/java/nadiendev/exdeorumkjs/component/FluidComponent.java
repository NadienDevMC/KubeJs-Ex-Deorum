package nadiendev.exdeorumkjs.component;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.mojang.serialization.Codec;
import com.mojang.serialization.JsonOps;
import dev.latvian.mods.kubejs.recipe.RecipeScriptContext;
import dev.latvian.mods.kubejs.recipe.component.RecipeComponent;
import dev.latvian.mods.kubejs.recipe.component.RecipeComponentType;
import dev.latvian.mods.kubejs.util.JsonUtils;
import dev.latvian.mods.rhino.type.TypeInfo;
import nadiendev.exdeorumkjs.ExDeorumKJS;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.FluidStackTemplate;
import net.neoforged.neoforge.fluids.crafting.SizedFluidIngredient;
import org.jspecify.annotations.Nullable;

import java.util.List;

public record FluidComponent(ResourceKey<RecipeComponentType<?>> type) implements RecipeComponent<Fluid> {
    public static final FluidComponent FLUID =
        new FluidComponent(RecipeComponentType.key(ExDeorumKJS.id("fluid")));

    @Override
    public Codec<Fluid> codec() {
        return BuiltInRegistries.FLUID.byNameCodec();
    }

    @Override
    public TypeInfo typeInfo() {
        return TypeInfo.of(Fluid.class).or(TypeInfo.STRING);
    }

    @Override
    public Fluid wrap(RecipeScriptContext cx, @Nullable Object from) {
        if (from instanceof Fluid fluid) {
            return fluid;
        }

        if (from instanceof FluidStack stack) {
            return stack.getFluid();
        }

        if (from instanceof FluidStackTemplate template) {
            return template.fluid().value();
        }

        if (from instanceof SizedFluidIngredient sized) {
            List<Holder<Fluid>> fluids = sized.ingredient().fluids();

            if (!fluids.isEmpty()) {
                return fluids.getFirst().value();
            }
        }

        JsonElement json = JsonUtils.of(cx.cx(), from);

        if (json instanceof JsonPrimitive primitive && primitive.isString()) {
            return byId(primitive.getAsString());
        }

        if (json instanceof JsonObject object) {
            JsonElement id = object.has("fluid") ? object.get("fluid") : object.get("id");

            if (id != null && id.isJsonPrimitive()) {
                return byId(id.getAsString());
            }
        }

        return BuiltInRegistries.FLUID.byNameCodec().parse(JsonOps.INSTANCE, json).getOrThrow();
    }

    private static Fluid byId(String id) {
        Identifier location = Identifier.parse(id);
        Fluid fluid = BuiltInRegistries.FLUID.getValue(location);

        if (fluid == null || (fluid == Fluids.EMPTY && !location.equals(Identifier.withDefaultNamespace("empty")))) {
            throw new IllegalArgumentException("Unknown fluid '" + id + "'");
        }

        return fluid;
    }

    @Override
    public boolean isEmpty(Fluid value) {
        return value == null;
    }

    @Override
    public String toString() {
        return type.identifier().toString();
    }
}
