/**
See https://github.com/openrndr/openrndr-tutorials/tree/master/shader-shadertoy-001
and https://github.com/openrndr/openrndr-tutorials/tree/master/shader-shadertoy-002
*/
#version 330

// list uniforms which are used by this shader
uniform vec3      iResolution;           // viewport resolution (in pixels)
uniform float     iTime;                 // shader playback time (in seconds)
//uniform float     iTimeDelta;            // render time (in seconds)
//uniform int       iFrame;                // shader playback frame
//uniform float     iChannelTimeN;         // channel playback time (in seconds)
//uniform vec3      iChannelResolutionN;   // channel resolution (in pixels)
//uniform vec4      iMouse;                // mouse pixel coords. xy: current (if MLB down), zw: click
//uniform sampler2D iChannelN;             // input channel. XX = 2D/Cube
//uniform sampler2D iChannel0;             // input channel. XX = 2D/Cube
//uniform vec4      iDate;                 // (year, month, day, time in seconds)
//uniform float     iSampleRate;           // sound sample rate (i.e., 44100)

// -- default output
out vec4 o_color;

void mainImage(out vec4 fragColor, in vec2 fragCoord);

void main() {
    mainImage(o_color, gl_FragCoord.xy);
}

// ------------------------------
//  SHADERTOY CODE BEGINS HERE  -
// ------------------------------

void mainImage( out vec4 fragColor, in vec2 fragCoord )
{
    // Normalized pixel coordinates (from 0 to 1)
    vec2 uv = fragCoord/iResolution.xy;
    vec2 center = iResolution.xy / 2.0;
    // vec3 col = 0.5 + 0.5*cos(iTime+uv.xyx+vec3(0,2,4));
    vec4 col = vec4(0.0,0.0,0.0,1.0);
    float steps = 8.0; // 'multisampling factor' if you will. [probably wrong terminology]

    // Think of these for loops as sampling multiple points around our fragCoord.
    for (float x = -(steps / 2.0); x < (steps / 2.0); x++) {
        for (float y = -(steps / 2.0); y < (steps / 2.0); y++) {
            //
            float d = distance(fragCoord + vec2(x, y) / steps, center);
            float dist = sqrt(sqrt(d) * 100.0);
            // modulo gives us 'black or white'?
            col += floor(mod(dist - iTime * 3.0, 2.0));
        }
    }

    col /= steps * steps;

    // Output to screen
    fragColor = col * (1.0 - (distance(fragCoord,center)/iResolution.x));
}

// ----------------------------
//  SHADERTOY CODE ENDS HERE  -
// ----------------------------
