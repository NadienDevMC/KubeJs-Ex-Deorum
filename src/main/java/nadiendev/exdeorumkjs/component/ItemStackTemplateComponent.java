package nadiendev.exdeorumkjs.component;

import com.mojang.serialization.Codec;
import dev.latvian.mods.kubejs.plugin.builtin.wrapper.ItemWrapper;
import dev.latvian.mods.kubejs.recipe.RecipeScriptContext;
import dev.latvian.mods.kubejs.recipe.component.RecipeComponent;
import dev.latvian.mods.kubejs.recipe.component.RecipeComponentType;
import dev.latvian.mods.kubejs.recipe.component.UniqueIdBuilder;
import dev.latvian.mods.kubejs.recipe.filter.RecipeMatchContext;
import dev.latvian.mods.kubejs.recipe.match.ItemMatch;
import dev.latvian.mods.kubejs.recipe.match.ReplacementMatchInfo;
import dev.latvian.mods.rhino.type.TypeInfo;
import nadiendev.exdeorumkjs.ExDeorumKJS;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemStackTemplate;
import org.jspecify.annotations.Nullable;

public record ItemStackTemplateComponent(ResourceKey<RecipeComponentType<?>> type) implements RecipeComponent<ItemStackTemplate> {
    public static final ItemStackTemplateComponent ITEM_STACK_TEMPLATE =
        new ItemStackTemplateComponent(RecipeComponentType.key(ExDeorumKJS.id("item_stack_template")));

    @Override
    public Codec<ItemStackTemplate> codec() {
        return ItemStackTemplate.CODEC;
    }

    @Override
    public TypeInfo typeInfo() {
        return ItemWrapper.TYPE_INFO;
    }

    @Override
    public boolean hasPriority(RecipeMatchContext cx, @Nullable Object from) {
        return from instanceof ItemStackTemplate || ItemWrapper.isItemStackLike(from);
    }

    @Override
    public ItemStackTemplate wrap(RecipeScriptContext cx, @Nullable Object from) {
        if (from instanceof ItemStackTemplate template) {
            return template;
        }

        ItemStack stack = (ItemStack) cx.cx().jsToJava(from, ItemWrapper.TYPE_INFO);
        return ItemStackTemplate.fromNonEmptyStack(stack);
    }

    @Override
    public boolean matches(RecipeMatchContext cx, ItemStackTemplate value, ReplacementMatchInfo match) {
        return match.match() instanceof ItemMatch m && m.matches(cx, value.create(), match.exact());
    }

    @Override
    public boolean isEmpty(ItemStackTemplate value) {
        return value == null;
    }

    @Override
    public void buildUniqueId(UniqueIdBuilder builder, ItemStackTemplate value) {
        if (value != null) {
            value.item().unwrapKey().ifPresent(builder::append);
        }
    }

    @Override
    public String toString() {
        return type.identifier().toString();
    }
}
