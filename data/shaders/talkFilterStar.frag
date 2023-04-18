/**
See https://github.com/openrndr/openrndr-tutorials/tree/master/shader-shadertoy-001
and https://github.com/openrndr/openrndr-tutorials/tree/master/shader-shadertoy-002
*/
#version 330

// list uniforms which are used by this shader
uniform vec3      iResolution;           // viewport resolution (in pixels)
uniform float     iTime;                 // shader playback time (in seconds)

uniform sampler2D tex0;
uniform vec2 textureSize0;

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

/**
From: [https://www.shadertoy.com/howto]

"Where fragCoord contains the pixel coordinates for which the shader
needs to compute a color. The coordinates are in pixel units,
ranging from 0.5 to resolution-0.5, over the rendering surface,
where the resolution is passed to the shader through the iResolution
uniform (see below)."

see also [https://iquilezles.org/articles/distfunctions/]
and [https://iquilezles.org/articles/distfunctions2d/]

seems promising: [https://www.ronja-tutorials.com/post/034-2d-sdf-basics/]

*/

float opUnion( float d1, float d2 ) { return min(d1,d2); }

float opSubtraction( float d1, float d2 ) { return max(-d1,d2); }

float opIntersection( float d1, float d2 ) { return max(d1,d2); }

// from [https://www.shadertoy.com/view/3tGBWR]
vec2 rotate(vec2 uv, float th) {
    return mat2(cos(th), sin(th), -sin(th), cos(th)) * uv;
}

float sdCircle( vec2 p, float r )
{
    return length(p) - r;
}

float sdStar5(in vec2 p, in float r, in float rf)
{
    const vec2 k1 = vec2(0.809016994375, -0.587785252292);
    const vec2 k2 = vec2(-k1.x,k1.y);
    p.x = abs(p.x);
    p -= 2.0*max(dot(k1,p),0.0)*k1;
    p -= 2.0*max(dot(k2,p),0.0)*k2;
    p.x = abs(p.x);
    p.y -= r;
    vec2 ba = rf*vec2(-k1.y,k1.x) - vec2(0,1);
    float h = clamp( dot(p,ba)/dot(ba,ba), 0.0, r );
    return length(p-ba*h) * sign(p.y*ba.x-p.x*ba.y);
}

// Step 3 - 2d SDF Shader
void mainImage( out vec4 O, in vec2 u)
{
    // p calculated in fractional coordinates where 1 => iResolution.y
    vec2 p = (2.0*u-iResolution.xy)/iResolution.y;
    vec2 center = iResolution.xy / 2.0;

    // Normalized pixel coordinates (from 0 to 1)
    u -= iResolution.xy / 2.0;

    float dist = sdStar5(rotate(p+vec2(-1.05,-0.02),iTime), 0.315*abs(sin(iTime)), 2.55);
//    float dCircle = sdStar5(p+vec2(-1.05,-0.02), 0.315, 2.55);
//    float dist = sdCircle(p-vec2(-16./10.,1.), 0.25);
//    float dist = opUnion(dStar,dCircle);
    // Time varying pixel color
    // vec3 col = 0.5 + 0.5*cos(iTime+uv.xyx+vec3(0,2,4));

    float c = (dist>0.0)?0.:1.;
    // Output to screen
    O = (dist>0.0)?vec4(0.):vec4(0.4,0.4,0.,0.25);
}

// ----------------------------
//  SHADERTOY CODE ENDS HERE  -
// ----------------------------
