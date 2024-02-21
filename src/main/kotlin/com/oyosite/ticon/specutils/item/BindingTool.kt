package com.oyosite.ticon.specutils.item

import com.oyosite.ticon.specutils.block.LinkableBlockEntity
import net.fabricmc.fabric.api.item.v1.FabricItemSettings
import net.minecraft.client.item.TooltipContext
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.item.ItemUsageContext
import net.minecraft.text.Text
import net.minecraft.util.ActionResult
import net.minecraft.util.Hand
import net.minecraft.util.TypedActionResult
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World

class BindingTool(settings: Settings) : Item(settings) {
    constructor(settings: FabricItemSettings.()->Unit): this(FabricItemSettings().apply(settings))

    var ItemStack.target: BlockPos?
        get() = getSubNbt("target_data")?.run{BlockPos(getInt("x"),getInt("y"),getInt("z"))}
        set(value) {value?.apply{getOrCreateSubNbt("target_data").run{putInt("x", x);putInt("y",y);putInt("z",z)}}?:removeSubNbt("target_data")}

    override fun useOnBlock(context: ItemUsageContext): ActionResult {
        val pos = context.blockPos
        val be = context.world.getBlockEntity(pos)
        (be as? LinkableBlockEntity)?.let{
            if(context.stack.target?.let(it::canBind) == false) return ActionResult.FAIL
            it.targetPos = context.stack.target
            return ActionResult.SUCCESS
        }

        context.stack.target = pos

        return ActionResult.SUCCESS
    }

    override fun use(world: World, user: PlayerEntity, hand: Hand): TypedActionResult<ItemStack> {
        val stack = user.getStackInHand(hand)
        if(user.isSneaking)stack.target = null
        return TypedActionResult.pass(stack)
    }

    override fun appendTooltip(
        stack: ItemStack,
        world: World?,
        tooltip: MutableList<Text>,
        context: TooltipContext
    ) {
        super.appendTooltip(stack, world, tooltip, context)
        val pos = stack.target
        if(pos == null) tooltip.add(Text.translatable("item.specutils.binding_tool.tooltip.unlinked"))
        else tooltip.add(Text.translatable("item.specutils.binding_tool.tooltip.linked_to", pos.x, pos.y, pos.z))

    }
}