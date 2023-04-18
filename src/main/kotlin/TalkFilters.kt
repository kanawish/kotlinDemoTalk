import org.openrndr.Program
import org.openrndr.application
import org.openrndr.color.ColorRGBa
import org.openrndr.draw.*
import org.openrndr.extra.imageFit.imageFit
import org.openrndr.extra.minim.minim
import shaders.ShadertoyFilter
import utils.StatsAggregate
import utils.buildRenderTarget
import utils.vec2

/**
 * Kotlin Conf talk content [part 03: Filters, render targets, clipping]
 *
 * NOTE: Investigate compositors if time permits.
 *
 * - Put a star on the milk bottle.
 * - Text ribbon render target, scroll.
 * - Make a quick-n-dirty color horizon shader, to be improved later.
 */

fun main() = application {
    // NOTE: More succinct, but note that oliveProgram {} won't 'see' changes here.
    fun Program.updateShaderX(shader: ShadertoyFilter) {
        shader.iTime = seconds
        shader.iResolution = drawer.bounds.dimensions.vector3()
        shader.iFrame += 1
    }

    configure {
        width = 640; height = 400
    }

    program {
        // NOTE: Step 5 / one past fairlight slide:
        // OPENRNDR 'extra' module, exposes https://github.com/ddf/Minim java audio lib.
        val minim = minim()
        val player = minim.loadFile("data/music/NANOU.mp3")
//        if( !player.isPlaying ) player.play()

        // NOTE: Step 0
        val stats = StatsAggregate()

        // NOTE: Step 1, load and display images.
        val gpm1Img = loadImage("data/images/GPM_1_Glass-building.png")
        val gpm2Img = loadImage("data/images/GPM_2_Milk-bottle.png")
        val gpm3Img = loadImage("data/images/GPM_3_Brick-building.png")

        // NOTE: Step 1.5 the fix for pixel blur.
        listOf(gpm1Img,gpm2Img,gpm3Img).forEach { img ->
            img.filter(MinifyingFilter.NEAREST, MagnifyingFilter.NEAREST)
        }

        // NOTE: Step 2 load sky shader, prep RT, add ST update and layer in extend { }
        // NOTE: IMPORTANT, EXPLAIN THE "DEFAULT" SHADER, SHOW GRADIENTS, EXPLAIN 'FRAGMENT' as col=f()

        val skyShader = ShadertoyFilter(url = "file:data/shaders/talkFilterSimpleSky.frag")
        val skyRt = renderTarget(width, height) {
            colorBuffer()
            depthBuffer()
        }

        // NOTE: _Quickly_ look at ShadertoyFilter code along side .frag to explain uniforms and parameters, iTime...
        // NOTE: Step 3 Another Shader layer, introducing 2D SDF functions with rotating star.
        val starShader = ShadertoyFilter(url = "file:data/shaders/talkFilterStar.frag")
        val starRt = renderTarget(width, height) {
            colorBuffer()
            depthBuffer()
        }

        // NOTE: Step 4 scroller
        val font = loadFont("data/fonts/PressStart2P-Regular.ttf", 24.0)
        val ribbonText = "Here is the scroller text ribbon for our example..."
        var rtScrollRibbon:RenderTarget? = null
        drawer.isolated {
            fontMap = font
            drawer.writer {
                drawer.fontMap?.height?.toInt()?.let { fontHeight ->
                    rtScrollRibbon = buildRenderTarget(
                        textWidth(ribbonText).toInt() + 4,
                        fontHeight + 6
                    ).also { rt ->
                        drawer.isolatedWithTarget(rt) {
                            ortho(rt)
                            text(ribbonText,.0,rt.height-2.0)
                        }
                        rt.colorBuffer(0).filter(MinifyingFilter.NEAREST, MagnifyingFilter.NEAREST)
                    }
                }
            }
        }
        val scrollShader = ShadertoyFilter(url="file:data/shaders/talkFilterScroller.frag").apply {
            rtScrollRibbon?.colorBuffer(0)?.let { cb ->
                cb.wrapU = WrapMode.REPEAT
                cb.wrapV = WrapMode.REPEAT

                iChannel0 = cb
                iChannelResolution0 = cb.bounds.dimensions.vector3()
            }
        }
        val scrollerRt = renderTarget(width, height) {
            colorBuffer()
            depthBuffer()
        }

        extend {
            drawer.clear(ColorRGBa.TRANSPARENT)
            drawer.isolated {
                updateShaderX(skyShader) // NOTE: Step 2
                skyShader.apply(
                    skyRt.colorBuffer(0),
                    skyRt.colorBuffer(0)
                )
                image(skyRt.colorBuffer(0))

                updateShaderX(scrollShader) // NOTE: Step 4
                scrollShader.apply(
                    scrollerRt.colorBuffer(0),
                    scrollerRt.colorBuffer(0)
                )
                image(scrollerRt.colorBuffer(0))

                imageFit(gpm1Img, drawer.bounds) // Step 1

                updateShaderX(starShader) // NOTE: Step 3
                starShader.apply(
                    starRt.colorBuffer(0),
                    starRt.colorBuffer(0)
                )
                image(starRt.colorBuffer(0))

                imageFit(gpm2Img, drawer.bounds) // Step 1

                // note: if you want to show pre-proc. ribbon.
                // rtScrollRibbon?.apply { image(colorBuffer(0),.0,10.0) }

                scale(2.0) // NOTE: many ways to scale...
                image(gpm3Img) // Step 1
            }

            // NOTE: Stats display, probably a good candidate for an 'extension'?
            stats.run { drawInfo(vec2(16, height - 32), 10) { "${width}x$height" } }
            stats.run { drawFPS(vec2(16, height - 16)) }
        } // extend
    } // program
}