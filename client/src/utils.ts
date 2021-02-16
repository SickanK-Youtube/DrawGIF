import { monitorEventLoopDelay } from "perf_hooks";
import ReactCrop from "react-image-crop";
import { PixelData, Dimensions, Size } from "./types";

export function convertToPixelData(pixels: Buffer): PixelData[] {
    let pixelData: PixelData[] = [];

    let i = 0;
    while(i < pixels.length) {
        pixelData.push([pixels[i], pixels[i+1], pixels[i+2], pixels[i+3]]);
        i += 4;
    }

    return pixelData;
};

export function determineResize(dimensions: Dimensions) {
    return {width: 128 * dimensions.width, height: 128 * dimensions.height };
};