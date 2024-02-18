package com.oyosite.ticon.specutils.component

import com.oyosite.ticon.specutils.ink.ColorLockedInkStorage
import de.dafuqs.spectrum.energy.InkStorage
import de.dafuqs.spectrum.energy.color.InkColor
import de.dafuqs.spectrum.energy.storage.SingleInkStorage
import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent
import net.minecraft.nbt.NbtCompound
import net.minecraft.scoreboard.Scoreboard
import net.minecraft.server.MinecraftServer
import net.minecraft.util.DyeColor
import net.minecraft.world.WorldProperties
import java.util.UUID

class StaticEnderInkStorageComponent() : AutoSyncedComponent {
    constructor(scoreboard: Scoreboard, server: MinecraftServer?): this()

    private val PLAYER_ENDER_INK_STORAGE: MutableMap<UUID, Array<SingleInkStorage?>> = mutableMapOf()


    operator fun get(player: UUID, color: DyeColor) = PLAYER_ENDER_INK_STORAGE.getOrPut(player){ arrayOfNulls(16) }.let {arr-> arr[color.ordinal] ?: (ColorLockedInkStorage(inkCapacity, InkColor.of(color)).also {arr[color.ordinal] = it}) }

    operator fun set(player: UUID, color: DyeColor, storage: ColorLockedInkStorage?) = PLAYER_ENDER_INK_STORAGE.getOrPut(player){ arrayOfNulls(16) }.set(color.ordinal, storage)

    override fun readFromNbt(tag: NbtCompound) {
        tag.keys.forEach {
            val playerTag = tag.getCompound(it)
            val uuid = UUID.fromString(it)
            val storages = arrayOfNulls<SingleInkStorage?>(16).apply { PLAYER_ENDER_INK_STORAGE[uuid] = this }
            for(i in 0 until 16) {
                if(!playerTag.contains(i.toString()))continue
                storages[i] = ColorLockedInkStorage(409600L, InkColor.of(DyeColor.byId(i)), playerTag.getLong(i.toString()))
            }
        }
    }

    override fun writeToNbt(tag: NbtCompound) {
        PLAYER_ENDER_INK_STORAGE.forEach { player, inkData ->
            val playerTag = NbtCompound()
            inkData.forEachIndexed { i, it ->
                it?.getEnergy(InkColor.of(DyeColor.byId(i)))?.run{playerTag.putLong(i.toString(), this)}
            }
            tag.put(player.toString(), playerTag)
        }
    }

    companion object{
        const val inkCapacity = 409600L
    }
}