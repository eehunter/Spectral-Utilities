package com.oyosite.ticon.specutils.block.auxilary_ink_supplier

import com.oyosite.ticon.specutils.SpectralUtilities
import com.oyosite.ticon.specutils.block.BlockRegistry
import com.oyosite.ticon.specutils.block.LinkableBlockEntity
import com.oyosite.ticon.specutils.item.EnderFlask
import com.oyosite.ticon.specutils.item.EnderFlask.Companion.owner
import com.oyosite.ticon.specutils.item.EnderFlask.Companion.ownerName
import de.dafuqs.spectrum.blocks.InWorldInteractionBlockEntity
import de.dafuqs.spectrum.blocks.energy.ColorPickerBlockEntity
import de.dafuqs.spectrum.energy.InkStorage
import de.dafuqs.spectrum.energy.InkStorageBlockEntity
import de.dafuqs.spectrum.energy.InkStorageItem
import de.dafuqs.spectrum.energy.color.InkColor
import de.dafuqs.spectrum.interfaces.PlayerOwned
import net.minecraft.block.BlockState
import net.minecraft.block.entity.BlockEntityType
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.nbt.NbtCompound
import net.minecraft.nbt.NbtElement
import net.minecraft.registry.RegistryKeys
import net.minecraft.registry.tag.TagKey
import net.minecraft.text.Text
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World
import java.util.*


open class AuxiliaryInkSupplierBlockEntity(type: BlockEntityType<*>, pos: BlockPos, state: BlockState) : InWorldInteractionBlockEntity(type, pos, state, 1), PlayerOwned, LinkableBlockEntity {
    constructor(pos: BlockPos, state: BlockState): this(BlockRegistry.AUXILIARY_INK_SUPPLIER_TYPE, pos, state)

    private var ownerUUID: UUID? = null
    private var ownerName: Text? = null

    override var targetPos: BlockPos? = null

    override fun canBind(target: BlockPos): Boolean = this.pos.getSquaredDistance(target)<=9

    override fun getOwnerUUID(): UUID? = this.ownerUUID

    override fun setOwner(playerEntity: PlayerEntity) {
        this.ownerUUID = playerEntity.uuid
        ownerName = playerEntity.name
        this.markDirty()
    }


    override fun writeNbt(nbt: NbtCompound) {
        super.writeNbt(nbt)
        PlayerOwned.writeOwnerUUID(nbt, this.ownerUUID)
        ownerName?.let { nbt.putString("ownerName", Text.Serializer.toJson(ownerName)) }
        targetPos?.let { pos ->
            val p = NbtCompound()
            p.putInt("x",pos.x)
            p.putInt("y",pos.y)
            p.putInt("z",pos.z)
            nbt.put("pos", p)
        }
    }

    override fun readNbt(nbt: NbtCompound) {
        super.readNbt(nbt)
        this.ownerUUID = PlayerOwned.readOwnerUUID(nbt)
        if(nbt.contains("ownerName", NbtElement.STRING_TYPE.toInt()))ownerName = Text.Serializer.fromJson(nbt.getString("ownerName"))
        if(nbt.contains("pos", NbtElement.COMPOUND_TYPE.toInt())){
            val p = nbt.getCompound("pos")
            targetPos = BlockPos(p.getInt("x"), p.getInt("y"), p.getInt("z"))
        } else {targetPos = null}
    }

    fun serverTick(world: World, blockPos: BlockPos, blockState: BlockState) {
        val heldStack = getStack(0)
        val storageItem = (heldStack.item as? InkStorageItem<*>)?:return
        val inkStorage = storageItem.getEnergyStorage(heldStack)
        val targetPos = this.targetPos?:return
        val targetBe = world.getBlockEntity(targetPos)?:return
        val targetStorage = (targetBe as? InkStorageBlockEntity<*>)?.energyStorage?:return
        val targetState: BlockState = world.getBlockState(targetPos)
        val canReceive = targetState.isIn(INK_RECEIVERS)
        val canProvide = targetState.isIn(INK_PROVIDERS)

        if(canReceive && storageItem.drainability.canDrain(false)){
            val transferredAmount = InkStorage.transferInk(inkStorage, targetStorage)
            if (transferredAmount > 0L) {
                storageItem.setEnergyStorage(heldStack, inkStorage)
            }

            targetBe.setInkDirty()
            targetBe.markDirty()
        }

        if(canProvide){
            var transferredAmount = 0L
            var colorPredicate: (InkColor)->Boolean = {true}
            if(targetBe is ColorPickerBlockEntity) colorPredicate = {targetBe.selectedColor?.equals(it)?:true}
            for(inkColor in InkColor.all().filter(colorPredicate)) transferredAmount+=InkStorage.transferInk(targetStorage, inkStorage, inkColor)
            if(transferredAmount>0)storageItem.setEnergyStorage(heldStack, inkStorage)

            targetBe.setInkDirty()
            targetBe.markDirty()
        }

        if(storageItem is EnderFlask && ownerUUID != null){
            heldStack.owner = ownerUUID
            heldStack.ownerName = ownerName?:Text.empty()
        }





    }




    companion object{
        val INK_RECEIVERS = TagKey.of(RegistryKeys.BLOCK, SpectralUtilities.id("ink_receivers"))
        val INK_PROVIDERS = TagKey.of(RegistryKeys.BLOCK, SpectralUtilities.id("ink_providers"))

        fun tick(world: World, blockPos: BlockPos, blockState: BlockState, inkSupplier: AuxiliaryInkSupplierBlockEntity){
            inkSupplier.serverTick(world, blockPos, blockState)
        }
    }

}