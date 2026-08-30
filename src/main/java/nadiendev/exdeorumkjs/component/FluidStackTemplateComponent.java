package nadiendev.exdeorumkjs.component;

import com.mojang.serialization.Codec;
import dev.latvian.mods.kubejs.fluid.FluidWrapper;
import dev.latvian.mods.kubejs.recipe.RecipeScriptContext;
import dev.latvian.mods.kubejs.recipe.component.RecipeComponent;
import dev.latvian.mods.kubejs.recipe.component.RecipeComponentType;
import dev.latvian.mods.kubejs.recipe.component.UniqueIdBuilder;
import dev.latvian.mods.kubejs.recipe.filter.RecipeMatchContext;
import dev.latvian.mods.kubejs.recipe.match.FluidMatch;
import dev.latvian.mods.kubejs.recipe.match.ReplacementMatchInfo;
import dev.latvian.mods.rhino.type.TypeInfo;
import nadiendev.exdeorumkjs.ExDeorumKJS;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.FluidStackTemplate;
import net.neoforged.neoforge.fluids.crafting.FluidIngredient;
import net.neoforged.neoforge.fluids.crafting.SizedFluidIngredient;
import org.jspecify.annotations.Nullable;

import java.util.List;

public record FluidStackTemplateComponent(ResourceKey<RecipeComponentType<?>> type) implements RecipeComponent<FluidStackTemplate> {
    public static final FluidStackTemplateComponent FLUID_STACK_TEMPLATE =
        new FluidStackTemplateComponent(RecipeComponentType.key(ExDeorumKJS.id("fluid_stack_template")));

    @Override
    public Codec<FluidStackTemplate> codec() {
        return FluidStackTemplate.CODEC;
    }

    @Override
    public TypeInfo typeInfo() {
        return FluidWrapper.TYPE_INFO;
    }

    @Override
    public boolean hasPriority(RecipeMatchContext cx, @Nullable Object from) {
        return from instanceof FluidStackTemplate || from instanceof SizedFluidIngredient || from instanceof FluidIngredient || from instanceof FluidStack || from instanceof Fluid;
    }

    @Override
    public FluidStackTemplate wrap(RecipeScriptContext cx, @Nullable Object from) {
        if (from instanceof FluidStackTemplate template) {
            return template;
        }

        if (from instanceof SizedFluidIngredient sized) {
            List<Holder<Fluid>> fluids = sized.ingredient().fluids();

            if (!fluids.isEmpty()) {
                return new FluidStackTemplate(fluids.getFirst(), sized.amount());
            }
        }

        FluidStack stack = (FluidStack) cx.cx().jsToJava(from, FluidWrapper.TYPE_INFO);
        return FluidStackTemplate.fromNonEmptyStack(stack);
    }

    @Override
    public boolean matches(RecipeMatchContext cx, FluidStackTemplate value, ReplacementMatchInfo match) {
        return match.match() instanceof FluidMatch m && m.matches(cx, value.create(), match.exact());
    }

    @Override
    public boolean isEmpty(FluidStackTemplate value) {
        return value == null;
    }

    @Override
    public void buildUniqueId(UniqueIdBuilder builder, FluidStackTemplate value) {
        if (value != null) {
            value.fluid().unwrapKey().ifPresent(builder::append);
        }
    }

    @Override
    public String toString() {
        return type.identifier().toString();
    }
}
