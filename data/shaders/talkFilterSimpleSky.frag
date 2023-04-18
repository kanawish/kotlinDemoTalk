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


// Dig deeper: https://thebookofshaders.com/06/

// Placeholder from
// https://www.shadertoy.com/view/3sfyRB
#define COLOR vec3(.001,.011,.05) // Original blue

// Step 2 - Prebaked blue sky shader with default starter.
void mainImage(out vec4 fragColor, in vec2 fragCoord )
{
    vec2 uv = fragCoord/iResolution.xy;

    // Adjusted to look at blue parts of gradient
    uv.y *= 0.5;
    uv.y += 0.6;
    vec3 blue = pow(COLOR,uv.yyy);
    vec3 orange = 1.-blue;
    vec3 sky = blue*pow(orange,vec3(8.))*16.;

    fragColor = vec4(pow(sky,vec3(.4545)),1.0);
}

// ----------------------------
//  SHADERTOY CODE ENDS HERE  -
// ----------------------------

// ----------------------------
//  PRE-BAKE BELOW  -
// ----------------------------

// Taken from Shadertoy, lost the original link.
void prebakedSky( out vec4 fragColor, in vec2 fragCoord )
{
    vec2 uv = fragCoord/iResolution.xy;

    // Adjusted to look at blue parts of gradient
    uv.y *= 0.5;
    uv.y += 0.6;
    vec3 blue = pow(COLOR,uv.yyy);
    vec3 orange = 1.-blue;
    vec3 sky = blue*pow(orange,vec3(8.))*16.;

    fragColor = vec4(pow(sky,vec3(.4545)),1.0);
}

