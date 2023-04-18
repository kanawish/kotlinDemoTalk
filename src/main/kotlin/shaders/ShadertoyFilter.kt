package shaders

import org.openrndr.draw.ColorBuffer
import org.openrndr.draw.Filter
import org.openrndr.draw.filterWatcherFromUrl
import org.openrndr.math.Vector3
import org.openrndr.math.Vector4

/**
 * The ShadertoyFilter, allows for import of ShaderToy fragment shaders,
 * with minimal boilerplate added to the original environment.
 *
 * Adapted from https://github.com/openrndr/openrndr-tutorials/blob/f54dda660130e6e69133741ae47989f1805bb28e/shader-shadertoy-002/src/main/kotlin/Example.kt#L10
 *
 * Supports live-coding & reload.
 *
 */
class ShadertoyFilter(
    url: String = "file:data/shaders/defaultShader.frag"
) : Filter(null, filterWatcherFromUrl(url)) {
    var iResolution: Vector3 by parameters          // viewport resolution (in pixels)
    var iTime: Double by parameters                 // shader playback time (in seconds)
    var iTimeDelta: Double by parameters            // render time (in seconds)
    var iFrame: Int by parameters                   // shader playback frame
    var iChannelTime0: Double by parameters         // channel playback time (in seconds)
    var iChannelTime1: Double by parameters
    var iChannelTime2: Double by parameters
    var iChannelTime3: Double by parameters
    var iChannelResolution0: Vector3 by parameters   // channel resolution (in pixels)
    var iChannelResolution1: Vector3 by parameters
    var iChannelResolution2: Vector3 by parameters
    var iChannelResolution3: Vector3 by parameters
    var iMouse: Vector4 by parameters               // mouse pixel coords. xy: current (if MLB down), zw: click
    var iChannel0: ColorBuffer by parameters        // input channel. XX = 2D/Cube
    var iChannel1: ColorBuffer by parameters
    var iChannel2: ColorBuffer by parameters
    var iChannel3: ColorBuffer by parameters
    var iDate: Vector4 by parameters                // (year, month, day, time in seconds)
    var iSampleRate: Double by parameters           // sound sample rate (i.e., 44100)

    init {
        iFrame = 0
        iMouse = Vector4(0.0)
    }
}