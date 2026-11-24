package nadiendev.exdeorumkjs.material;

import dev.latvian.mods.kubejs.KubeJS;
import dev.latvian.mods.kubejs.event.StartupEventJS;
import dev.latvian.mods.kubejs.item.ItemBuilder;
import dev.latvian.mods.kubejs.registry.RegistryInfo;
import dev.latvian.mods.rhino.util.HideFromJS;
import nadiendev.exdeorumkjs.ExDeorumKJS;
import net.minecraft.resources.ResourceLocation;
import thedarkcolour.exdeorum.material.AbstractMaterial;
import thedarkcolour.exdeorum.material.MaterialParser;
import thedarkcolour.exdeorum.material.MaterialRegistry;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class MaterialRegistryKubeEvent extends StartupEventJS {
    private static final Path SOURCE = Path.of("kubejs", "startup_scripts");

    public record Registered(AbstractMaterial material, MaterialKind kind, MaterialBuilder builder, String mineableWith) {
    }

    private static final List<Registered> REGISTERED = new ArrayList<>();

    private final MaterialKind kind;
    private final ItemKind itemKind;
    private final List<MaterialBuilder> builders = new ArrayList<>();

    public static List<Registered> registered() {
        return REGISTERED;
    }

    public MaterialRegistryKubeEvent(MaterialKind kind) {
        this.kind = kind;
        this.itemKind = null;
    }

    public MaterialRegistryKubeEvent(ItemKind itemKind) {
        this.kind = null;
        this.itemKind = itemKind;
    }

    public Object create(String name) {
        if (itemKind != null) {
            ResourceLocation id = new ResourceLocation(KubeJS.appendModId(name));
            ItemBuilder builder = itemKind.create(id);
            RegistryInfo.ITEM.addBuilder(builder);
            return builder;
        }

        MaterialBuilder builder = new MaterialBuilder(name);
        builders.add(builder);
        return builder;
    }

    @HideFromJS
    @SuppressWarnings({"rawtypes", "unchecked"})
    public void registerAll() {
        for (MaterialBuilder builder : builders) {
            try {
                AbstractMaterial material = kind.read(new MaterialParser(builder.json, SOURCE.resolve(builder.name + ".json"), kind.registry()));

                if (material == null) {
                    ExDeorumKJS.LOGGER.error("Failed to create {} material '{}' from {}", kind.id, builder.name, builder.json);
                    continue;
                }

                ((MaterialRegistry) kind.registry()).register(builder.name, material);
                String tool = builder.mineableWith;

                if (tool == null && builder.json.has("needs_correct_tool") && builder.json.get("needs_correct_tool").getAsBoolean()) {
                    tool = "pickaxe";
                    ExDeorumKJS.LOGGER.info("{} material '{}' needs the correct tool but no mineableWith(...) was given, defaulting to pickaxe", kind.id, builder.name);
                }

                REGISTERED.add(new Registered(material, kind, builder, tool));
            } catch (Exception ex) {
                ExDeorumKJS.LOGGER.error("Failed to register {} material '{}' from {}", kind.id, builder.name, builder.json, ex);
            }
        }
    }
}
