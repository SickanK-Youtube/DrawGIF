import { Aspect, PixelData } from "./types";

export function convertToPixelData(pixels: Buffer): PixelData[] {
    let pixelData: PixelData[] = [];

    let i = 0;
    while(i < pixels.length) {
        pixelData.push([pixels[i], pixels[i+1], pixels[i+2], pixels[i+3]]);
        i += 4;
    }

    return pixelData;
};

export function determineResize(aspect: Aspect) {
    return {width: 128 * aspect.width, height: 128 * aspect.height };
};