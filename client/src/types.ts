export type PixelData = [number, number, number, number];

export interface Dimensions {
  width: number;
  height: number;
};

export interface Size extends Dimensions {};

export enum Zoom {
    CLOSEST = 128,
    CLOSE =  256,
    NORMAL = 512,
    FAR = 1024,
    FARTHEST = 2048,
};