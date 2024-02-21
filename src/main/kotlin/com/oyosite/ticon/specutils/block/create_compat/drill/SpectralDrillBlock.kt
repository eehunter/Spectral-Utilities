package com.oyosite.ticon.specutils.block.create_compat.drill

import com.simibubi.create.AllBlocks
import com.simibubi.create.AllShapes
import com.simibubi.create.content.kinetics.base.DirectionalKineticBlock
import com.simibubi.create.content.kinetics.drill.DrillBlock
import com.simibubi.create.foundation.block.IBE
import com.simibubi.create.foundation.damageTypes.CreateDamageSources
import com.simibubi.create.foundation.placement.IPlacementHelper
import com.simibubi.create.foundation.placement.PlacementHelpers
import com.simibubi.create.foundation.placement.PlacementOffset
import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.block.ShapeContext
import net.minecraft.block.entity.BlockEntityType
import net.minecraft.entity.Entity
import net.minecraft.entity.ItemEntity
import net.minecraft.entity.ai.pathing.NavigationType
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.fluid.FluidState
import net.minecraft.fluid.Fluids
import net.minecraft.item.BlockItem
import net.minecraft.item.ItemPlacementContext
import net.minecraft.item.ItemStack
import net.minecraft.state.StateManager
import net.minecraft.state.property.Properties
import net.minecraft.util.ActionResult
import net.minecraft.util.Hand
import net.minecraft.util.annotation.MethodsReturnNonnullByDefault
import net.minecraft.util.hit.BlockHitResult
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Box
import net.minecraft.util.math.Direction
import net.minecraft.util.math.MathHelper
import net.minecraft.util.shape.VoxelShape
import net.minecraft.world.BlockView
import net.minecraft.world.World
import net.minecraft.world.WorldAccess
import net.minecraft.world.WorldView
import java.util.function.Predicate
import kotlin.math.abs
import kotlin.math.min

@Suppress("OVERRIDE_DEPRECATION")
class SpectralDrillBlock(properties: Settings) : DirectionalKineticBlock(properties), IBE<SpectralDrillBlockEntity> {

    init{
        defaultState = super.getDefaultState().with(Properties.WATERLOGGED, false)
    }

    override fun onEntityCollision(state: BlockState, worldIn: World?, pos: BlockPos, entityIn: Entity) {
        if (entityIn is ItemEntity) return
        if (!Box(pos).contract(.1).intersects(entityIn.boundingBox)) return

        withBlockEntityDo(worldIn, pos) { be: SpectralDrillBlockEntity ->
            if (be.speed == 0f) return@withBlockEntityDo
            entityIn.damage(CreateDamageSources.drill(worldIn), getDamage(be.speed).toFloat())//Perhaps the damage type should be different here?
        }
    }

    override fun getOutlineShape(state: BlockState, worldIn: BlockView?, pos: BlockPos, context: ShapeContext): VoxelShape = AllShapes.CASING_12PX[state.get(FACING)]

    override fun neighborUpdate(state: BlockState?, world: World?, pos: BlockPos?, sourceBlock: Block?, sourcePos: BlockPos?, notify: Boolean) {
        TODO()
    }

    override fun getRotationAxis(state: BlockState): Direction.Axis = state.get(FACING).axis

    override fun hasShaftTowards(world: WorldView?, pos: BlockPos?, state: BlockState, face: Direction?): Boolean = face == state[FACING].opposite

    override fun canPathfindThrough(state: BlockState?, world: BlockView?, pos: BlockPos?, type: NavigationType?): Boolean = false

    override fun getFluidState(state: BlockState): FluidState = if (state.get(Properties.WATERLOGGED)) Fluids.WATER.getStill(false) else Fluids.EMPTY.defaultState

    override fun appendProperties(builder: StateManager.Builder<Block, BlockState>) {
        builder.add(Properties.WATERLOGGED)
        super.appendProperties(builder)
    }

    override fun getStateForNeighborUpdate(
        state: BlockState, direction: Direction?, neighbourState: BlockState?,
        world: WorldAccess, pos: BlockPos?, neighbourPos: BlockPos?
    ): BlockState {
        if (state.get(Properties.WATERLOGGED)) world.scheduleFluidTick(pos, Fluids.WATER, Fluids.WATER.getTickRate(world))
        return state
    }

    override fun getPlacementState(context: ItemPlacementContext): BlockState {
        val fluidState = context.world.getFluidState(context.blockPos)
        return super.getPlacementState(context)!!
            .with(Properties.WATERLOGGED, fluidState.fluid === Fluids.WATER)
    }

    fun getDamage(speed: Float): Double {
        val speedAbs = abs(speed.toDouble()).toFloat()
        val sub1 = min((speedAbs / 16).toDouble(), 2.0)
        val sub2 = min((speedAbs / 32).toDouble(), 4.0)
        val sub3 = min((speedAbs / 64).toDouble(), 4.0)
        return MathHelper.clamp(sub1 + sub2 + sub3, 1.0, 10.0)
    }

    override fun getBlockEntityClass(): Class<SpectralDrillBlockEntity> = SpectralDrillBlockEntity::class.java

    override fun getBlockEntityType(): BlockEntityType<out SpectralDrillBlockEntity> {
        TODO("Not yet implemented")
    }

    override fun onUse(state: BlockState?, world: World?, pos: BlockPos?, player: PlayerEntity, hand: Hand?, ray: BlockHitResult?): ActionResult {
        val heldItem = player.getStackInHand(hand)

        val placementHelper = PlacementHelpers.get(placementHelperId)
        if (!player.isSneaking && player.canModifyBlocks()) {
            if (placementHelper.matchesItem(heldItem)) {
                placementHelper.getOffset(player, world, state, pos, ray).placeInWorld(world, heldItem.item as BlockItem, player, hand, ray)
                return ActionResult.SUCCESS
            }
        }

        return ActionResult.PASS
    }


    companion object{
        private val placementHelperId = PlacementHelpers.register(PlacementHelper())
    }


    private class PlacementHelper : IPlacementHelper {
        override fun getItemPredicate(): Predicate<ItemStack> =
            Predicate { (it.item as? BlockItem)?.block is SpectralDrillBlock }


        override fun getStatePredicate(): Predicate<BlockState> =
            Predicate { it.block is SpectralDrillBlock }


        override fun getOffset(player: PlayerEntity, world: World, state: BlockState, pos: BlockPos, ray: BlockHitResult): PlacementOffset {
            val directions = IPlacementHelper.orderedByDistanceExceptAxis(pos, ray.pos, state[FACING].axis) { world.getBlockState(pos.offset(it)).isReplaceable }
            return if (directions.isEmpty()) PlacementOffset.fail()
            else PlacementOffset.success(pos.offset(directions[0])) { it.with(FACING, state[FACING]) }
        }
    }


}