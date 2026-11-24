// Ex Deorum KJS - custom content examples.
// Everything Ex Deorum can be extended with is registered through ExDeorumEvents.registry(...).

// --- Blocks ------------------------------------------------------------------------------------
// .craftedFrom(main, slab) generates the crafting recipe with Ex Deorum's own layout.
// .mineableWith(tool) generates the mineable tag - without it a needsCorrectTool block never drops.
// The loot table is always generated, so the block drops itself when broken.

ExDeorumEvents.registry('sieve', event => {
    event.create('obsidian')
        .soundType('stone').strength(4.0).needsCorrectTool(true).mineableWith('pickaxe')
        .craftedFrom('minecraft:obsidian', 'minecraft:polished_blackstone_slab')
})

ExDeorumEvents.registry('compressed_sieve', event => {
    event.create('obsidian')
        .soundType('stone').strength(4.0).needsCorrectTool(true).mineableWith('pickaxe')
        .craftedFrom('minecraft:obsidian')
})

ExDeorumEvents.registry('barrel', event => {
    event.create('obsidian')
        .soundType('stone').strength(4.0).needsCorrectTool(true).mineableWith('pickaxe')
        .mapColor('black').fireproof(true)
        .craftedFrom('minecraft:obsidian', 'minecraft:polished_blackstone_slab')
})

ExDeorumEvents.registry('lava_crucible', event => {
    event.create('obsidian')
        .soundType('stone').strength(4.0).needsCorrectTool(true).mineableWith('pickaxe')
        .mapColor('black')
        .craftedFrom('minecraft:obsidian', 'minecraft:polished_blackstone_slab')
})

ExDeorumEvents.registry('water_crucible', event => {
    event.create('jade')
        .soundType('stone').strength(4.0).needsCorrectTool(true).mineableWith('pickaxe')
        .mapColor('green')
        .craftedFrom('minecraft:emerald_block', 'minecraft:polished_blackstone_slab')
})

// --- Items -------------------------------------------------------------------------------------
// These borrow existing textures so the examples are visible without drawing anything.
// For your own items drop a PNG at kubejs/assets/kubejs/textures/item/<name>.png and skip .texture().
//
// A mesh is the one exception: it needs a SECOND texture for how it looks lying inside the sieve,
// looked up by item id, so .texture() cannot redirect it. It has to be a real file at
// kubejs/assets/kubejs/textures/item/mesh/copper_mesh.png.

ExDeorumEvents.registry('mesh', event => {
    event.create('copper_mesh')
        .material('#forge:ingots/copper')
        .texture('exdeorum:item/iron_mesh')
        .displayName('Copper Mesh')
})

ExDeorumEvents.registry('hammer', event => {
    event.create('copper_hammer')
        .material('#forge:ingots/copper')
        .texture('exdeorum:item/iron_hammer')
        .displayName('Copper Hammer')
})

ExDeorumEvents.registry('compressed_hammer', event => {
    // no .material() needed: derives nine of kubejs:copper_hammer
    event.create('compressed_copper_hammer')
        .texture('exdeorum:item/compressed_iron_hammer')
        .displayName('Compressed Copper Hammer')
})

ExDeorumEvents.registry('ore_chunk', event => {
    // no .ore() needed: derives #forge:ores/tungsten from the item name
    event.create('tungsten_ore_chunk')
        .texture('exdeorum:item/iron_ore_chunk')
        .displayName('Tungsten Ore Chunk')
})
