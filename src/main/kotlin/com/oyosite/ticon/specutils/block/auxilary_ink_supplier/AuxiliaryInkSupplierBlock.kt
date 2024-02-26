package com.oyosite.ticon.specutils.block.auxilary_ink_supplier

import de.dafuqs.spectrum.blocks.FluidLogging
import de.dafuqs.spectrum.blocks.InWorldInteractionBlock
import de.dafuqs.spectrum.energy.InkStorageItem
import net.minecraft.block.Block
import net.minecraft.block.BlockRenderType
import net.minecraft.block.BlockState
import net.minecraft.block.ShapeContext
import net.minecraft.block.entity.BlockEntity
import net.minecraft.block.entity.BlockEntityTicker
import net.minecraft.block.entity.BlockEntityType
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.fluid.FluidState
import net.minecraft.state.StateManager
import net.minecraft.util.ActionResult
import net.minecraft.util.Hand
import net.minecraft.util.hit.BlockHitResult
import net.minecraft.util.math.BlockPos
import net.minecraft.util.shape.VoxelShape
import net.minecraft.util.shape.VoxelShapes
import net.minecraft.world.BlockView
import net.minecraft.world.World
import kotlin.math.max

@Suppress("OVERRIDE_DEPRECATION")
open class AuxiliaryInkSupplierBlock(settings: Settings) : InWorldInteractionBlock(settings.luminance(::luminance)) {

    constructor(settings: Settings.()->Unit): this(Settings.create().apply(settings))

    init {
        defaultState = defaultState.with(FluidLogging.ANY_INCLUDING_NONE, FluidLogging.State.NOT_LOGGED)
    }
    override fun getRenderType(state: BlockState): BlockRenderType = BlockRenderType.MODEL

    override fun createBlockEntity(pos: BlockPos, state: BlockState): BlockEntity = AuxiliaryInkSupplierBlockEntity(pos, state)

    override fun appendProperties(builder: StateManager.Builder<Block, BlockState>) { builder.add(FluidLogging.ANY_INCLUDING_NONE) }

    override fun getFluidState(state: BlockState): FluidState = state[FluidLogging.ANY_INCLUDING_NONE].fluidState

    override fun isShapeFullCube(state: BlockState?, world: BlockView?, pos: BlockPos?): Boolean = false

    override fun getOpacity(state: BlockState?, world: BlockView?, pos: BlockPos?): Int = 0

    override fun getOutlineShape(state: BlockState?, world: BlockView?, pos: BlockPos?, context: ShapeContext?): VoxelShape = SHAPE

    override fun onUse(state: BlockState, world: World, pos: BlockPos, player: PlayerEntity, hand: Hand?, hit: BlockHitResult?): ActionResult {
        if(world.isClient)return ActionResult.SUCCESS
        val be = world.getBlockEntity(pos) as? AuxiliaryInkSupplierBlockEntity ?: return ActionResult.CONSUME
        val stack = player.getStackInHand(hand)

        //(stack.item as? InkStorageItem<*>)?.let{item: InkStorageItem<*> ->
        if(stack.isEmpty || stack.item is InkStorageItem<*>)if(this.exchangeStack(world, pos, player, hand, stack, be, 0)){
            be.inventoryChanged()
            be.setOwner(player)
        }
        //}

        return ActionResult.CONSUME
    }

    @Suppress("Unchecked_Cast")
    override fun <T : BlockEntity?> getTicker(
        world: World?,
        state: BlockState?,
        type: BlockEntityType<T>?
    ): BlockEntityTicker<T>? = world?.takeIf { !it.isClient }?.let{ BlockEntityTicker(AuxiliaryInkSupplierBlockEntity::tick) as? BlockEntityTicker<T> }



    companion object{
        fun luminance(state: BlockState) = max(state[FluidLogging.ANY_INCLUDING_NONE].luminance, 5)

        private val BASE_SHAPE: VoxelShape = createCuboidShape(4.0, 0.0, 4.0, 12.0, 10.0, 12.0)
        private val TOP_SHAPE: VoxelShape = createCuboidShape(3.0, 10.0, 3.0, 13.0, 14.0, 13.0)
        var SHAPE: VoxelShape = VoxelShapes.union(BASE_SHAPE, TOP_SHAPE)
    }

}