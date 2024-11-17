import org.openrndr.application
import org.openrndr.color.ColorRGBa
import org.openrndr.color.ColorRGBa.Companion.BLUE
import org.openrndr.color.ColorRGBa.Companion.CYAN
import org.openrndr.color.ColorRGBa.Companion.GREEN
import org.openrndr.color.ColorRGBa.Companion.MAGENTA
import org.openrndr.color.ColorRGBa.Companion.RED
import org.openrndr.color.ColorRGBa.Companion.YELLOW
import org.openrndr.color.ColorRGBa.Companion.fromHex
import org.openrndr.draw.*
import org.openrndr.extra.olive.oliveProgram
import org.openrndr.math.Vector2
import org.openrndr.shape.Rectangle


/**
 * Demoscene Talk - OPENRNDR Basic
 *
 * `application {}` creates and runs a synchronous OPENRNDR application using the
 * provided ApplicationBuilder.
 */
fun main() = application {
    println("${displays.size} displays")
    println("first().dimensions: ${displays.first().dimensions}")


    /**
     *  Then the `configure {}` block lets us setup window sizing and various
     *  characteristics.
     */
    configure {
        // "Preview" mode, I stick the window in lower right corner here.
        displays.first().let { first ->
            val fraction = 3
            display = first
            first.dimensions?.let { dim ->
                position = dim * (fraction-1) / fraction
                width = dim.x / 3
                height = dim.y / 3
            }
        }
        windowTransparent = true // Transparent so we can see code underneath.
        windowAlwaysOnTop = true // Keep the preview visible.
        hideWindowDecorations = true
    }

    /**
     * This is where the fun stuff starts.
     *
     * Everything in the `oliveProgram {}` block is live-coding enabled.
     */
    oliveProgram {
        val f = 3
        // Font sizes are relative to screen's density.
        //val font = loadFont("data/fonts/PressStart2P-Regular.ttf", 24.0)
        val font = loadFont("data/fonts/JetBrainsMono-Regular.ttf", 52.0/2)
        val bold = loadFont("data/fonts/JetBrainsMono-Bold.ttf", 52.0/2)

        extend {
            // Transparent background to help during livecoding.
            drawer.clear(.3,.3,.3,.3)

            /**
             * 'Internal' isolation, hot-reloadable
             *
             * This function won't pollute the top-level drawing state,
             * it sets up a 'push/pop' style of attribute stacking.
             */
            drawer.isolated {
                fontMap = font
                fill = ColorRGBa.BLACK

                // Writer allows for enhanced text manipulation.
                writer {
                    val msg = "Hello World!"

                    // When drawing text, measuring is useful.
                    // It allows us to use OpenRNDR's shape classes:
                    val textRect = Rectangle.fromCenter(
                        center = bounds.center.copy(y = height / 3.0), // NOTE text y pos
                        width = textWidth(msg),
                        height = fontMap?.height ?: .0
                    )
                    // To precisely lay out our text.
                    text(
                        text = msg,
                        x = textRect.x,
                        y = textRect.y + textRect.height
                    )
                }

                // We can also lay text out 'terminal style'
                writer {
                    newLine()
                    fill = MAGENTA
                    text("one-two-three-four-five")
                }

            }

            /**
             *  Not 'hot-reloadable', but important for long-term organization of
             *  more stable code.
             */
            drawer.isolated { externalIsolation(font,bold) }

            /**
             * Draws 3 circles.
             */
            drawer.isolated {// NOTE: Explain isolated push/pop.
                // Build colored circles
                val confColors = listOf(
                    fromHex("#ED6E2D99") to fromHex("#FFFFFF99"), // O/W
                    fromHex("#FFF9") to fromHex("#0009"), // P/G
                    // NOTE: Alpha unset defaults to fully opaque.
                    fromHex("#7954F5") to fromHex("#FFFFFF"), // O/W
                )
                // Add Circles [then hit re-run]
                val positions = (1..5 step 2).map { step ->
                    // Show live change to height
                    Vector2(step * width / 6.0, height / 4.0 * 3.0)
                }

                drawer.strokeWeight = 4.0

                // NOTE: Point out the local statefulness of `drawer` in this loop.
                positions.forEachIndexed { i, pos ->
                    confColors[i].let { (fc, sc) ->
                        drawer.fill = fc
                        drawer.stroke = sc
                        drawer.circle(pos, radius = width / 8.0)
                    }
                }
            }

            // TODO: investigate SVG support more.
            // val svg = loadSVG("data/svg/patterns.svg")
            // drawer.composition(svg)
        }

    }
}

// External functions are available, but won't be hot-loadable.
fun Drawer.externalIsolation(regular: FontMap, bold:FontMap) {
    fontMap = regular
    fill = ColorRGBa.BLACK

    // New writers 'reset' the terminal.
    writer {
        newLine()
        gaplessNewLine()
        cursor.y+=12
        fill = GREEN
        text("green")
        fill = BLUE
        text("blue")
        fill = RED
        fontMap = bold

        val cols = listOf(RED,GREEN,BLUE,YELLOW, CYAN)
        "1234567890".forEachIndexed { index, c ->
            fill = cols[index%cols.size]
            fontMap = if(index%2 >0) bold else regular
            text(c.toString())
        }
    }
}
