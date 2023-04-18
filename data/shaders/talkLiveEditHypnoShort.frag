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

void mainImage(out vec4 O, in vec2 u)
{
    // Normalized pixel coordinates (from 0 to 1)
    u -= iResolution.xy / 2.0;

    //vec2 u =  iResolution.xy / 2.0;
    float dist = pow(dot(u, u), 1./8.) * 10.;
    float c = smoothstep(-2., 2., sin(3.14159*(dist - iTime * 3.))*sqrt(dist));

    // Time varying pixel color
    // vec3 col = 0.5 + 0.5*cos(iTime+uv.xyx+vec3(0,2,4));

    // Output to screen
    O = vec4(c*(1.- length(u)/iResolution.x));

}

// ----------------------------
//  SHADERTOY CODE ENDS HERE  -
// ----------------------------
