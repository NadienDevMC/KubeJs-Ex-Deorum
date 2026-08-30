package nadiendev.exdeorumkjs.component;

import com.google.gson.JsonElement;
import com.mojang.serialization.Codec;
import com.mojang.serialization.JsonOps;
import dev.latvian.mods.kubejs.recipe.RecipeScriptContext;
import dev.latvian.mods.kubejs.recipe.component.RecipeComponent;
import dev.latvian.mods.kubejs.recipe.component.RecipeComponentType;
import dev.latvian.mods.kubejs.util.JsonUtils;
import dev.latvian.mods.rhino.type.TypeInfo;
import nadiendev.exdeorumkjs.ExDeorumKJS;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.NumberProvider;
import net.minecraft.world.level.storage.loot.providers.number.NumberProviders;

public record NumberProviderComponent(RecipeComponentType<?> type) implements RecipeComponent<NumberProvider> {
    public static final RecipeComponentType<NumberProvider> NUMBER_PROVIDER =
        RecipeComponentType.unit(ExDeorumKJS.id("number_provider"), NumberProviderComponent::new);

    @Override
    public Codec<NumberProvider> codec() {
        return NumberProviders.CODEC;
    }

    @Override
    public TypeInfo typeInfo() {
        return TypeInfo.of(NumberProvider.class).or(TypeInfo.NUMBER).or(TypeInfo.RAW_MAP);
    }

    @Override
    public NumberProvider wrap(RecipeScriptContext cx, Object from) {
        if (from instanceof NumberProvider provider) {
            return provider;
        }

        if (from instanceof Number number) {
            return ConstantValue.exactly(number.floatValue());
        }

        JsonElement json = JsonUtils.of(cx.cx(), from);

        if (json.isJsonPrimitive() && json.getAsJsonPrimitive().isNumber()) {
            return ConstantValue.exactly(json.getAsFloat());
        }

        return NumberProviders.CODEC.parse(JsonOps.INSTANCE, json).getOrThrow();
    }

    @Override
    public boolean isEmpty(NumberProvider value) {
        return value == null;
    }

    @Override
    public String toString() {
        return type.toString();
    }
}
