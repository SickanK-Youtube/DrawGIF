export type PixelData = [number, number, number, number];

export interface Crop {
  x: number,
  y: number,
};

export interface Aspect {
  width: number,
  height: number,
};

export interface Size extends Aspect {};