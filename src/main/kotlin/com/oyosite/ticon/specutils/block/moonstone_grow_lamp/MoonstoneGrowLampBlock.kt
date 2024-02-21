package com.oyosite.ticon.specutils.block.moonstone_grow_lamp

import com.oyosite.ticon.specutils.SpectralUtilities
import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.item.ItemPlacementContext
import net.minecraft.registry.Registries
import net.minecraft.registry.RegistryKeys
import net.minecraft.registry.tag.TagKey
import net.minecraft.state.StateManager
import net.minecraft.state.property.BooleanProperty
import net.minecraft.state.property.IntProperty
import net.minecraft.util.math.BlockPos
import net.minecraft.world.BlockView
import net.minecraft.world.World

class MoonstoneGrowLampBlock(settings: Settings) : Block(settings.luminance(::luminosity)) {
    constructor(settings: Settings.()->Unit): this(Settings.create().apply(settings))

    init {
        defaultState = defaultState.with(BRIGHTNESS, 0).with(OVERCHARGE, false)
    }

    override fun appendProperties(builder: StateManager.Builder<Block, BlockState>) { builder.add(BRIGHTNESS, OVERCHARGE) }

    override fun getPlacementState(ctx: ItemPlacementContext): BlockState? = defaultState.with(BRIGHTNESS, ctx.world.getReceivedRedstonePower(ctx.blockPos))
        .with(OVERCHARGE, ctx.world.getBlockState(ctx.blockPos.up()).isIn(OVERCHARGE_BLOCKS))

    override fun neighborUpdate(
        state: BlockState,
        world: World,
        pos: BlockPos,
        sourceBlock: Block?,
        sourcePos: BlockPos?,
        notify: Boolean
    ) {
        if(world.isClient)return
        val b = state[BRIGHTNESS]
        val r = world.getReceivedRedstonePower(pos)
        var finalizedState: BlockState? = null
        if(r!=b) finalizedState = state.with(BRIGHTNESS, r)

        val o = world.getBlockState(pos.up()).isIn(OVERCHARGE_BLOCKS)
        if(state[OVERCHARGE]!=o) finalizedState = (finalizedState?:state).with(OVERCHARGE, o)

        finalizedState?.let { world.setBlockState(pos, it, NOTIFY_LISTENERS) }
    }

    override fun getOpacity(state: BlockState, world: BlockView?, pos: BlockPos?): Int = 15 - state[BRIGHTNESS]

    companion object{
        val BRIGHTNESS: IntProperty = IntProperty.of("brightness", 0, 15)
        val OVERCHARGE: BooleanProperty = BooleanProperty.of("overcharge")

        val OVERCHARGE_BLOCKS = TagKey.of(RegistryKeys.BLOCK, SpectralUtilities.id("overcharge_blocks"))

        private fun luminosity(state: BlockState) = state[BRIGHTNESS]
    }

}