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
uniform vec3      iChannelResolution0;   // channel resolution (in pixels)
//uniform vec4      iMouse;                // mouse pixel coords. xy: current (if MLB down), zw: click
//uniform sampler2D iChannelN;             // input channel. XX = 2D/Cube
uniform sampler2D iChannel0;             // input channel. XX = 2D/Cube
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

/*
Workflow:
- starting with https://www.shadertoy.com/view/ltKyWh
- found out about https://en.wikipedia.org/wiki/Intercept_theorem
- comments pointed out a few improvements

First, hook in the texture for scroll text to iChannel0.

*/
#define CUTOFF 278

// NOTE: Not enough time to go in detail during talk, but playing with vars
//   can help build an intuitive understanding of the relatively simple
//   vector math behind this.

// Step 4 - Rough sky scroller effect shader
void mainImage( out vec4 fragColor, in vec2 fragCoord )
{
    vec2 uv = (fragCoord - .5 * iResolution.xy+vec2(0,275)) / iResolution.y ;
    uv /= vec2(10., 2.15); // Sizing
    vec3 viewDir = normalize(vec3(uv, 0.08));
    vec2 planarUV = viewDir.xz/abs(viewDir.y);

    float depth = planarUV.y;
    planarUV += vec2(0.0, iTime*.136).yx;
    // NOTE: One spot that good to mess around with values...
    planarUV *= vec2(2.220,-20.0); // Flip text axis
    planarUV += vec2(1,3.280);

    vec4 source = texture(iChannel0, planarUV);
    vec3 albedo = texture(iChannel0, planarUV).rgb;
    float light = 0.35/(depth*depth)*viewDir.z;

    fragColor = vec4(light*albedo,light*source.a);

    // NOTE: But, what's up with uv up there?
    // NOTE: For folks like me who are rusty with vector manips,
    // NOTE: one way to quick / dirty 'debug' & grok what's going on:
//    fragColor = vec4(abs(uv.x), .0, abs(uv.y), 1.);

    // NOTE: Other trick, also,
    // NOTE: Using color meter to see if things come out right...
    vec2 debug = mod(vec2(10.), uv*10.)/5;
//    fragColor = vec4(abs(debug.x), .0, abs(debug.y),1.);
}

void original( out vec4 fragColor, in vec2 fragCoord ) {
    vec2 uv = ((fragCoord/iResolution.xy)-0.5)*vec2(iResolution.x/iResolution.y,1.0);

    vec3 viewDir = normalize(vec3(uv,0.5));

    vec2 planarUV = viewDir.xz/abs(viewDir.y);

    float depth = planarUV.y;
    planarUV += vec2(0.0, iTime*.6);

    vec3 albedo = texture( iChannel0, planarUV).rgb;

    float light = 2.5/(depth*depth)*viewDir.z;

    fragColor = vec4(light*albedo,1.0);
}

// ----------------------------
//  SHADERTOY CODE ENDS HERE  -
// ----------------------------
