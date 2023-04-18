package utils

import org.openrndr.Program
import org.openrndr.color.ColorRGBa
import org.openrndr.draw.loadFont
import org.openrndr.math.Vector2

/**
 * Stateful class to record and show
 * FPS and other metrics on screen, at a
 * given frequency to not impact performance.
 */
data class StatsAggregate(
    var min: Double = .0,
    var max: Double = .0,
    var total: Double = .0,
    var count: Int = 0,
    var lastSeconds:Double = .0
) {
    private var infoText = "--"
    private var fpsText = "--"
    private val font = loadFont("data/fonts/default.otf", 16.0)

    private fun clear() {
        min = .0; max = .0; total = .0; count = 0
    }

    private fun delta(seconds: Double): Double {
        return (seconds - lastSeconds).also { lastSeconds = seconds }
    }

    fun Program.drawFPS(pos: Vector2, extra: () -> String = { "" }) {
        drawer.fill = ColorRGBa.GREEN
        drawer.fontMap = font
        val deltaTime =  delta(seconds)
        if (count > 200) {
            val fpsStr = "%.2f".format(count / total).padStart(5, '0')
            val minStr = "%.3f".format(min).padStart(5, '0')
            val maxStr = "%.3f".format(max).padStart(5, '0')
            fpsText = "[agg] FPS: $fpsStr [$minStr,$maxStr] ${extra()}"
            clear()
        } else {
            if (deltaTime < min || min == 0.0) min = deltaTime
            if (deltaTime > max) max = deltaTime
            total += deltaTime
            count++
        }
        drawer.text(fpsText, pos)
    }

    /**
     * Utility to render arbitrary info text from main loop, at given frequency.
     */
    fun Program.drawInfo(pos: Vector2, freq: Int, info: () -> String) {
        drawer.fill = ColorRGBa.GREEN
        drawer.fontMap = font
        if( count % freq == 0 ) infoText = info()
        drawer.text(infoText, pos)
    }
}

