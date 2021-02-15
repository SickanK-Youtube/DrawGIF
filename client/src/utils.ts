import { monitorEventLoopDelay } from "perf_hooks";
import ReactCrop from "react-image-crop";
import { PixelData, Dimensions, Size, Zoom } from "./types";

export function convertToPixelData(pixels: Buffer): PixelData[] {
    let pixelData: PixelData[] = [];

    let i = 0;
    while(i < pixels.length) {
        pixelData.push([pixels[i], pixels[i+1], pixels[i+2], pixels[i+3]]);
        i += 4;
    }

    return pixelData;
};

export function determineResize(size: Size, dimensions: Dimensions): [Zoom, Size] {
    let density: Zoom = Zoom.CLOSEST;

    let densityPercent = 100000;
    for(let level in Zoom) {
        if(!isNaN(Number(level))){
            let densityCalc = (size.width * size.height) / (dimensions.width * Number(level) * dimensions.height * Number(level));
            if(densityCalc < 1) densityCalc = 1 / densityCalc;

            if(densityPercent > densityCalc) {
                density = Number(level);
                densityPercent = densityCalc;
            }
        }
    }

    let resize: Size = {width: density * dimensions.width, height: density * dimensions.height }
    
    return [density, resize];
};