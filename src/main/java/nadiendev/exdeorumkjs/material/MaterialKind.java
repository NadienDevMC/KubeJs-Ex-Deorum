package nadiendev.exdeorumkjs.material;

import thedarkcolour.exdeorum.material.AbstractCrucibleMaterial;
import thedarkcolour.exdeorum.material.AbstractMaterial;
import thedarkcolour.exdeorum.material.BarrelMaterial;
import thedarkcolour.exdeorum.material.CompressedSieveMaterial;
import thedarkcolour.exdeorum.material.DefaultMaterials;

import thedarkcolour.exdeorum.material.MaterialParser;
import thedarkcolour.exdeorum.material.MaterialRegistry;
import thedarkcolour.exdeorum.material.SieveMaterial;


import java.util.function.Function;

public enum MaterialKind {
    BARREL("barrel", DefaultMaterials.BARRELS, BarrelMaterial::readFromJson),
    SIEVE("sieve", DefaultMaterials.SIEVES, SieveMaterial::readFromJson),
    COMPRESSED_SIEVE("compressed_sieve", DefaultMaterials.COMPRESSED_SIEVES, CompressedSieveMaterial::readFromJson),
    LAVA_CRUCIBLE("lava_crucible", DefaultMaterials.LAVA_CRUCIBLES, parser -> AbstractCrucibleMaterial.readFromJson(parser, KJSLavaCrucibleMaterial::new)),
    WATER_CRUCIBLE("water_crucible", DefaultMaterials.WATER_CRUCIBLES, parser -> AbstractCrucibleMaterial.readFromJson(parser, KJSWaterCrucibleMaterial::new));

    public final String id;
    private final MaterialRegistry<?> registry;
    private final Function<MaterialParser, ? extends AbstractMaterial> reader;

    MaterialKind(String id, MaterialRegistry<?> registry, Function<MaterialParser, ? extends AbstractMaterial> reader) {
        this.id = id;
        this.registry = registry;
        this.reader = reader;
    }

    public MaterialRegistry<?> registry() {
        return registry;
    }

    public AbstractMaterial read(MaterialParser parser) {
        return reader.apply(parser);
    }

    public static MaterialKind byId(String id) {
        for (MaterialKind kind : values()) {
            if (kind.id.equals(id)) {
                return kind;
            }
        }

        return null;
    }
}
