package kr.vanilage.main

import net.kyori.adventure.text.Component
import net.kyori.adventure.title.Title
import org.bukkit.Bukkit
import org.bukkit.GameRule
import org.bukkit.event.Listener
import org.bukkit.plugin.java.JavaPlugin
import java.time.LocalDateTime
import kotlin.math.roundToLong

class Main : JavaPlugin(), Listener{
    override fun onEnable() {
        Bukkit.getPluginManager().registerEvents(this, this)

        Bukkit.getWorlds().forEach {
            it.setGameRule(GameRule.DO_DAYLIGHT_CYCLE, false)
        }

        Bukkit.getScheduler().runTaskTimer(this, Runnable {
            val nowTime = LocalDateTime.now()

            val nowSecond = (nowTime.hour * 3600) +
                    (nowTime.minute * 60) +
                    (nowTime.second)

            val nowTick = (nowSecond.toDouble() / 86400.0) * 24000.0

            val realTick = if (nowTick >= 6000)
            { nowTick - 6000 }
            else
            { nowTick + 18000 }

            val offset = this.config.getInt("offset")

            var offsetTick = realTick + offset

            if (offsetTick > 24000) offsetTick -= 24000

            Bukkit.getWorld("world")!!.time = offsetTick.roundToLong()

            Bukkit.getOnlinePlayers().forEach {
                it.sendActionBar(Component.text(offsetTick.toLong()))
            }
        }, 0, 1)
    }
}