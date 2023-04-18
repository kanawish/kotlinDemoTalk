import org.openrndr.application
import org.openrndr.color.ColorRGBa
import org.openrndr.color.ColorRGBa.Companion.fromHex
import org.openrndr.draw.isolated
import org.openrndr.draw.loadFont
import org.openrndr.extra.olive.oliveProgram
import org.openrndr.math.Vector2
import org.openrndr.shape.Rectangle
import org.openrndr.shape.ShapeContour
import org.openrndr.shape.contour
import org.openrndr.writer

/**
 * Demoscene Talk - OPENRNDR Basic
 */
fun main() = application {
    configure {
        width = 640
        height = 400
    }
    oliveProgram {
        val font = loadFont("data/fonts/PressStart2P-Regular.ttf", 24.0)

        extend {
            drawer.clear(fromHex("#D5D5D5"))

            drawer.isolated {
                fontMap = font
                fill = ColorRGBa.BLACK
                // Writer allows for enhanced text manipulation.
                writer {
                    val msg = "Hello KotlinConf!"
                    // When drawing text, measuring is useful.
                    // It allows us to use OpenRNDR's shape classes:
                    val textRect = Rectangle.fromCenter(
                        center = bounds.center.copy(y=height/3.0), // NOTE text y pos
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
            }

            drawer.isolated {
                val l = 16.0
                translate(bounds.center.x-(16*2.5), (bounds.height/4) - 5*l) // NOTE 1 recenter
                drawer.fill = fromHex("#7954F5") // NOTE 2 purple
                drawer.stroke = null

                // NOTE: Explain contours briefly
                val c = contour {
                    moveTo(Vector2(l,.0))
                    lineTo(Vector2(l, l))
                    lineTo(Vector2(.0, l))
                    lineTo(anchor)
                    close()
                }
                contour(c)

                val points = listOf(
                    Vector2(1*l,2*l),
                    Vector2(3*l,4*l),
                    Vector2(4*l,4*l),
                    Vector2(3*l,3*l),
                    Vector2(2*l,3*l),
                    Vector2(5*l,.0),
                    Vector2(3*l,.0)
                )
                contour(
                    ShapeContour.fromPoints(points, closed = true)
                )
            }

            drawer.isolated {// NOTE: Explain isolated push/pop.
                // Build colored circles
                val confColors = listOf(
                    fromHex("#ED6E2D") to fromHex("#FFFFFF"), // O/W
                    fromHex("#FFF") to fromHex("#000"), // P/G
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

        }

    }
}
