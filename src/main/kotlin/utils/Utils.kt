package utils
import kotlinx.coroutines.flow.MutableStateFlow
import org.openrndr.Program
import org.openrndr.color.ColorRGBa
import org.openrndr.draw.Drawer
import org.openrndr.draw.renderTarget
import org.openrndr.math.Vector2
import org.openrndr.shape.Circle

fun Program.fW(fraction:Double):Double = (fraction*width)
fun Program.fW(fraction:Float):Double = (fraction*width).toDouble()
fun Program.fH(fraction:Double):Double = (fraction*height)
fun Program.fH(fraction:Float):Double = (fraction*height).toDouble()

fun Program.centerH() = width / 2.0
fun Program.centerV() = height / 2.0

fun Drawer.fW(fraction:Double):Double = (fraction*width)
fun Drawer.fW(fraction:Float):Double = (fraction*width).toDouble()
fun Drawer.fH(fraction:Double):Double = (fraction*height)
fun Drawer.fH(fraction:Float):Double = (fraction*height).toDouble()

fun Drawer.centerH() = width / 2.0
fun Drawer.centerV() = height / 2.0

fun Drawer.sizePair(): Pair<Double, Double> {
    val w = width.toDouble()
    val h = height.toDouble()
    return Pair(w, h)
}

fun Drawer.size(): Vector2 =
    Vector2(width.toDouble(), height.toDouble())

fun Program.buildRenderTarget(w:Int=width, h:Int=height, depth:Boolean = true) = renderTarget(w, h) {
    colorBuffer()
    if( depth ) depthBuffer()
}

fun Program.rndCircles() {
    drawer.fill = ColorRGBa.WHITE
    drawer.stroke = ColorRGBa.BLACK
    drawer.strokeWeight = 1.0

    val circles = List(5) {
        Circle(
            (Math.random() * width / 3) + width * 1 / 3,
            (Math.random() * height / 3) + height * 1 / 3,
            Math.random() * 10.0 + 10.0
        )
    }
    drawer.circles(circles)
}

fun vec2(x:Int, y:Int) = Vector2(x.toDouble(), y.toDouble())
fun vec2(x:Double, y:Int) = Vector2(x, y.toDouble())
fun vec2(x:Int, y:Double) = Vector2(x.toDouble(), y)

operator fun <T> MutableStateFlow<T>.plusAssign(intent:T.()->T) {
    value = intent(value)
}
