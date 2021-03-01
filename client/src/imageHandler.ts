import FileType from "file-type/browser";
import Jimp from "jimp";
import { Aspect, Crop, ImagePixels, PixelData } from "./types/types";
import { determineResize } from "./utils";
import { parseGIF, decompressFrames, ParsedFrame } from "gifuct-js";
import { Size } from "react-easy-crop/types";

async function getImageMime(imageUrl: string): Promise<string> {
  const response = await fetch(imageUrl);
  if (response.body === null) throw new Error("body is null");

  const type = await FileType.fromStream(response.body);
  if (type === undefined) throw new Error("type is undefined");

  return type?.mime;
}

async function handleGif(
  imageUrl: string,
  aspect: Aspect,
  crop: Crop,
  size: Size,
  zoom: number
): Promise<[string[], number]> {
  const pixels: string[] = [];
  const mainCanvas: HTMLCanvasElement = document.createElement("canvas");
  const frameCanvas: HTMLCanvasElement = document.createElement("canvas");
  const mainContext = mainCanvas.getContext("2d");
  const frameContext = frameCanvas.getContext("2d");

  let gifData = await fetch(imageUrl);
  let gif = parseGIF(await gifData.arrayBuffer());
  let gifFrames: ParsedFrame[] = decompressFrames(gif, true);

  let resize = determineResize(aspect);
  let img = new Image(resize.width, resize.height);
  mainCanvas.width = resize.width;
  mainCanvas.height = resize.height;

  for (let frame of gifFrames) {
    frameCanvas.width = frame.dims.width;
    frameCanvas.height = frame.dims.height;
    frameContext?.putImageData(
      new ImageData(frame.patch, frame.dims.width, frame.dims.height),
      0,
      0
    );

    let image = await Jimp.read(frameCanvas.toDataURL());
    let x = image.getWidth() / 2 - crop.x / zoom - size.width / 2;
    let y = image.getHeight() / 2 - crop.y / zoom - size.height / 2;

    image.crop(x, y, size.width, size.height);
    image.resize(resize.width, resize.height);

    img.src = await image.getBase64Async(Jimp.MIME_PNG);

    img.onload = () => {
      mainContext?.drawImage(img, 0, 0, resize.width, resize.height);
    };

    let pixelData = mainContext
      ?.getImageData(0, 0, resize.width, resize.height)
      .data.join(",");

    if (pixelData !== undefined) pixels.push(pixelData);
  }

  let delays = gifFrames.map((frame) => frame.delay);
  let averageDelay =
    delays.reduce((a: number, b: number) => a + b) / delays.length;

  return [pixels, averageDelay];
}

async function handleImage(
  imageUrl: string,
  aspect: Aspect,
  crop: Crop,
  size: Size,
  zoom: number
): Promise<string> {
  try {
    let image = await Jimp.read(imageUrl);

    let resize = determineResize(aspect);
    let x = image.getWidth() / 2 - crop.x / zoom - size.width / 2;
    let y = image.getHeight() / 2 - crop.y / zoom - size.height / 2;

    image.crop(x, y, size.width, size.height);
    image.resize(resize.width, resize.height);

    return image.bitmap.data.join(",");
  } catch (error) {
    console.error(error);
  }

  return "";
}

export default async function getImagePixels(
  imageUrl: string,
  aspect: Aspect,
  crop: Crop,
  size: Size,
  zoom: number
): Promise<ImagePixels> {
  const imageMime = await getImageMime(imageUrl);
  const allowedMimes = [
    "image/jpeg",
    "image/gif",
    "image/png",
    "image/apng",
    "image/webp",
  ];

  if (allowedMimes.filter((mime: string) => imageMime === mime).length === 0) {
    throw new Error("Unsupported file type");
  }

  let pixels: string[] = [];
  let delay = 0;

  if (imageMime === "image/gif") {
    let gifResponse = await handleGif(imageUrl, aspect, crop, size, zoom);
    pixels = gifResponse[0];
    delay = Math.round(gifResponse[1]);
  } else {
    pixels = [await handleImage(imageUrl, aspect, crop, size, zoom)];
  }

  let resize = determineResize(aspect);

  return {
    type: imageMime,
    pixels,
    width: resize.width,
    height: resize.height,
    delay,
  };
}
