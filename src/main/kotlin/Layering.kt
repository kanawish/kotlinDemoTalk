import kotlinx.coroutines.flow.MutableStateFlow
import org.openrndr.application
import org.openrndr.color.ColorRGBa
import org.openrndr.draw.*
import org.openrndr.extra.olive.oliveProgram
import shaders.ShadertoyFilter
import utils.StatsAggregate
import utils.plusAssign
import utils.vec2

fun main() = application {
    data class State(val modeSelect:Int=1)
    val stateStore = MutableStateFlow(State())

    configure {
        width = 640; height = 400
    }

    oliveProgram {
        // Metrics
        val stats = StatsAggregate()
        stateStore.also { store ->
            keyboard.keyDown.listen { keyEvent ->
                when (val name = keyEvent.name) {
                    "1", "2", "3", "4", "5", "6" -> store += { copy(modeSelect = name.toInt()) }
                    else -> {}
                }
            }
        }

        // Sky shader
        val skyShader = ShadertoyFilter(url = "file:data/shaders/talkFilterSimpleSky.frag")
        val skyRt = renderTarget(width, height) {
            colorBuffer()
            depthBuffer()
        }

        val fr1Img = loadImage("data/ps/Five_Roses.png").apply {
            filter(MinifyingFilter.NEAREST, MagnifyingFilter.NEAREST)
            wrapU = WrapMode.REPEAT
            wrapV = WrapMode.REPEAT
        }

        // Ray march shader
        val rayShader = ShadertoyFilter(url = "file:data/shaders/talkRaymarch.frag")
        val rayRt = renderTarget(width, height) {
            colorBuffer()
            depthBuffer()
        }

        // CRT Effect
        val crtShader = ShadertoyFilter(url="file:data/shaders/talkCrt.frag")
        val finalRt = renderTarget(width, height) {
            colorBuffer()
            depthBuffer()
        }

        extend {
            drawer.clear(ColorRGBa.TRANSPARENT)

            when(stateStore.value.modeSelect) {
                1 -> skyRt
                2 -> rayRt
                else -> null
            }?.colorBuffer(0)?.let(drawer::image)

            stats.run { drawInfo(vec2(16, height - 32), 10) { "${width}x$height" } }
            stats.run { drawFPS(vec2(16, height - 16)) }
        }
    }
}