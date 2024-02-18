package com.oyosite.ticon.specutils.ink

import de.dafuqs.spectrum.energy.color.InkColor
import de.dafuqs.spectrum.energy.storage.SingleInkStorage
import de.dafuqs.spectrum.helpers.Support
import net.minecraft.text.Text

class ColorLockedInkStorage(capacity: Long, private val color: InkColor, amount: Long = 0): SingleInkStorage(capacity, color, amount) {

    override fun accepts(color: InkColor?): Boolean = color == this.color

    override fun addEnergy(color: InkColor, amount: Long): Long {
        if(color!=this.color)return 0
        return super.addEnergy(color, amount)
    }

    @Deprecated("Deprecated in Java")
    override fun setEnergy(colors: MutableMap<InkColor, Long>, total: Long) {
        val singleColorMap = mapOf(color to colors[color])
        super.setEnergy(singleColorMap, total)
    }

    override fun addTooltip(tooltip: MutableList<Text>?, includeHeader: Boolean) {
        if (includeHeader) {
            tooltip!!.add(Text.translatable(
                "item.specutils.${color.dyeColor.name.lowercase()}_ender_flask.tooltip",
                *arrayOf<Any>(Support.getShortenedNumberString(this.maxEnergy))
            ))
        }

        if (this.storedEnergy > 0L) {
            tooltip!!.add(Text.translatable(
                "spectrum.tooltip.ink_powered.bullet." + storedColor.toString().lowercase(),
                *arrayOf<Any>(Support.getShortenedNumberString(this.storedEnergy))
            ))
        }
    }
}