package com.oyosite.ticon.specutils.item

import com.oyosite.ticon.specutils.component.ScoreboardComponentEntrypoint
import com.oyosite.ticon.specutils.component.StaticEnderInkStorageComponent
import com.oyosite.ticon.specutils.ink.ColorLockedInkStorage
import de.dafuqs.spectrum.energy.InkStorage
import de.dafuqs.spectrum.energy.color.InkColor
import de.dafuqs.spectrum.energy.storage.SingleInkStorage
import de.dafuqs.spectrum.items.energy.InkFlaskItem
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.fabricmc.fabric.api.item.v1.FabricItemSettings
import net.minecraft.client.item.TooltipContext
import net.minecraft.entity.Entity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.ItemStack
import net.minecraft.item.ItemUsageContext
import net.minecraft.scoreboard.Scoreboard
import net.minecraft.text.Text
import net.minecraft.util.ActionResult
import net.minecraft.util.Hand
import net.minecraft.util.TypedActionResult
import net.minecraft.world.World
import java.util.*

class EnderFlask(settings: FabricItemSettings, val color: InkColor): InkFlaskItem(settings, StaticEnderInkStorageComponent.inkCapacity) {


    var ItemStack.owner: UUID?
        get() = getSubNbt("ender_flask_data")?.takeIf { it.containsUuid("owner") }?.getUuid("owner")
        set(value) = getOrCreateSubNbt("ender_flask_data").let{dat -> value?.let { dat.putUuid("owner", it) }?: dat.remove("owner") }

    var ItemStack.ownerName: Text
        get() = getSubNbt("ender_flask_data")?.takeIf { it.contains("owner_name") }?.getString("owner_name")?.let(Text.Serializer::fromJson)?:Text.translatable("item.specutils.ender_flask.tooltip.unknown_player")//.let{Text.nbt("", true, Optional.empty()){ _-> listOf(it).stream() }} ?: Text.translatable("item.ender_flask.tooltip.unknown_player")
        set(value) { getOrCreateSubNbt("ender_flask_data").putString("owner_name", Text.Serializer.toJson(value)) }

    override fun getEnergyStorage(itemStack: ItemStack?): SingleInkStorage {
        val owner = itemStack?.owner?:return DUMMY_ENERGY_STORAGE
        return ScoreboardComponentEntrypoint.ENDER_FLASK[scoreboard?:return DUMMY_ENERGY_STORAGE][owner, color.dyeColor]?: DUMMY_ENERGY_STORAGE
    }

    override fun setEnergyStorage(itemStack: ItemStack?, storage: InkStorage?) {
        val owner = itemStack?.owner?:return
        var colorLock = storage as? ColorLockedInkStorage
        if(colorLock==null)colorLock = ColorLockedInkStorage(storage?.maxPerColor?:0, color, storage?.getEnergy(color)?:0)
        ScoreboardComponentEntrypoint.ENDER_FLASK[scoreboard?:return][owner, color.dyeColor] = colorLock
    }

    override fun use(world: World?, user: PlayerEntity, hand: Hand): TypedActionResult<ItemStack> {
        scoreboard = user.scoreboard
        val stack = user.getStackInHand(hand)
        stack.owner = user.uuid
        stack.ownerName = user.name
        return super.use(world, user, hand)
    }

    override fun inventoryTick(stack: ItemStack, world: World, entity: Entity, slot: Int, selected: Boolean) {
        scoreboard?: (entity as? PlayerEntity)?.fetchScoreboard()
    }

    @Suppress("RedundantCompanionReference")
    private fun PlayerEntity.fetchScoreboard(){
        Companion.scoreboard = this.scoreboard
    }

    @Environment(EnvType.CLIENT)
    override fun appendTooltip(stack: ItemStack, world: World?, tooltip: MutableList<Text>, context: TooltipContext) {
        if(stack.owner==null){
            tooltip.add(Text.translatable("item.specutils.ender_flask.tooltip.unlinked_0"))
            tooltip.add(Text.translatable("item.specutils.ender_flask.tooltip.unlinked_1"))
            return
        }
        tooltip.add(Text.translatable("item.specutils.ender_flask.tooltip.owner", stack.ownerName))

        if(getEnergyStorage(stack) !is ColorLockedInkStorage)return

        super.appendTooltip(stack, world, tooltip, context)
        tooltip.removeLast()

    }

    companion object{
        val DUMMY_ENERGY_STORAGE = SingleInkStorage(0)
        var scoreboard: Scoreboard? = null
    }


}