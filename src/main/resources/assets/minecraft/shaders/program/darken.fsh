#version 120

uniform sampler2D DiffuseSampler;

varying vec2 texCoord;
varying vec2 oneTexel;

uniform vec2 InSize;

uniform float DarkenAmount;

void main() {
    vec4 smpl = texture2D(DiffuseSampler, texCoord + oneTexel);
    gl_FragColor = vec4(smpl.rgb * (1 / DarkenAmount), smpl.a);
}
