#version 120

uniform sampler2D DiffuseSampler;

varying vec2 texCoord;
varying vec2 oneTexel;

uniform vec2 InSize;

uniform int KernelSize;

void main() {
//    vec4 smpl = texture2D(DiffuseSampler, texCoord + oneTexel);
//    gl_FragColor = vec4(smpl.rgb * (1 / DarkenAmount), smpl.a);

    // Generate a 2D Gaussian kernel
    vec2 kernel[KernelSize];
    float sigma = 1.0;
    float r, s = 2.0 * sigma * sigma;
    float sum = 0.0;
    for (int x = -KernelSize; x <= KernelSize; x++) {
        for (int y = -KernelSize; y <= KernelSize; y++) {
            r = sqrt(x * x + y * y);
            kernel[x + KernelSize][y + KernelSize] = (exp(-(r * r) / s)) / (M_PI * s);
            sum += kernel[x + KernelSize][y + KernelSize];

        }
    }

    // Normalize the kernel
    for (int x = 0; x < KernelSize; x++) {
        for (int y = 0; y < KernelSize; y++) {
            kernel[x][y] /= sum;
        }
    }

    // Convolve the kernel with the image
    vec4 texSum = vec4(0.0);
    float maxA = 0.0;

    for (int x = -KernelSize; x <= KernelSize; x++) {
        for (int y = -KernelSize; y <= KernelSize; y++) {
            vec4 tex = texture2D(DiffuseSampler, texCoord + oneTexel * vec2(x, y));
            texSum += tex * kernel[x + KernelSize][y + KernelSize];
            maxA = max(maxA, tex.a);
        }
    }

    gl_FragColor = vec4(texSum.rgb, maxA);
}
