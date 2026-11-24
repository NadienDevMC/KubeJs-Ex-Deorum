// Ex Deorum KJS - recipe examples.
// None of these recipes exist in Ex Deorum, so every one of them adds new content.
// Minecraft 1.20.1 / Forge / KubeJS 6. Tags use the forge: namespace.

ServerEvents.recipes(event => {

    // exdeorum:sieve
    event.recipes.exdeorum.sieve('minecraft:prismarine_shard', 'minecraft:clay', 'exdeorum:string_mesh').binomial(3, 0.2).id('exdeorum:examples/sieve/prismarine_shard')
    event.recipes.exdeorum.sieve('minecraft:prismarine_crystals', 'minecraft:clay', 'exdeorum:diamond_mesh').chance(0.90).byHandOnly(true).id('exdeorum:examples/sieve/prismarine_crystals')

    // exdeorum:compressed_sieve
    event.recipes.exdeorum.compressed_sieve('minecraft:amethyst_shard', 'exdeorum:compressed_cobblestone', 'exdeorum:iron_mesh').binomial(9, 0.12).id('exdeorum:examples/compressed_sieve/amethyst_shard')

    // exdeorum:hammer
    event.recipes.exdeorum.hammer('minecraft:amethyst_shard', 'minecraft:amethyst_block').constant(4).id('exdeorum:examples/hammer/amethyst_shard')
    event.recipes.exdeorum.hammer('minecraft:gravel', 'minecraft:tuff').uniform(1, 2).id('exdeorum:examples/hammer/gravel')

    // exdeorum:compressed_hammer
    event.recipes.exdeorum.compressed_hammer('minecraft:coarse_dirt', 'exdeorum:compressed_dirt').constant(9).id('exdeorum:examples/compressed_hammer/coarse_dirt')

    // exdeorum:crook
    event.recipes.exdeorum.crook('minecraft:brown_mushroom', 'minecraft:brown_mushroom_block', 0.3).id('exdeorum:examples/crook/brown_mushroom')
    event.recipes.exdeorum.crook('minecraft:red_mushroom', 'minecraft:red_mushroom_block', 0.3).id('exdeorum:examples/crook/red_mushroom')

    // exdeorum:crucible_heat_source
    event.recipes.exdeorum.crucible_heat_source('minecraft:magma_block', 3).id('nadiendev:testmagmablock')
    event.recipes.exdeorum.crucible_heat_source('minecraft:furnace', 4).lit(true).id('nadiendev:testfurnace')

    // exdeorum:lava_crucible
    event.recipes.exdeorum.lava_crucible(Fluid.of('minecraft:lava', 500), 'minecraft:magma_block').id('nadiendev:testlava')

    // exdeorum:water_crucible
    event.recipes.exdeorum.water_crucible(Fluid.of('minecraft:water', 250), 'minecraft:dried_kelp_block').id('exdeorum:examples/water_crucible/dried_kelp_block')

    // exdeorum:barrel_compost
    event.recipes.exdeorum.barrel_compost('minecraft:honeycomb', 100).id('nadiendev:testhoneycomb')
    event.recipes.exdeorum.barrel_compost('minecraft:brown_mushroom_block', 250).id('nadiendev:testmushrooom')

    // exdeorum:barrel_mixing
    event.recipes.exdeorum.barrel_mixing('minecraft:calcite', 'minecraft:bone_meal', Fluid.of('minecraft:water', 1000)).id('nadiendev:testcalcite')

    // exdeorum:barrel_fluid_mixing
    event.recipes.exdeorum.barrel_fluid_mixing('minecraft:clay', Fluid.of('exdeorum:witch_water', 1000), 'minecraft:water').id('nadiendev:testlaaaavaaa')

    // exdeorum:barrel_fluid_transformation
    event.recipes.exdeorum.barrel_fluid_transformation('minecraft:lava', 'exdeorum:witch_water', 'minecraft:magma_block', 2400, 0xD45A12, {
        'minecraft:basalt': 60,
        'minecraft:blackstone': 40
    }).id('nadiendev:testbarrelfluidtransform')

    // exdeorum:ore_chunk
    event.recipes.exdeorum.ore_chunk('#forge:ores/copper', 'exdeorum:copper_ore_chunk').id('nadiendev:testorechunks')
})
