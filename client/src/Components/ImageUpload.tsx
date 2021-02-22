import React, { useState } from "react";
import Jimp from "jimp";
import { determineResize } from "../utils";
import { Dimensions } from "../types";
import { v4 as uuidv4 } from "uuid";

interface Props {
  currentDimensions: Dimensions;
  isUploading: boolean;
  setIsUploading: React.Dispatch<React.SetStateAction<boolean>>;
  canvasPreview: React.RefObject<HTMLCanvasElement>;
  imageData: ImageData | null;
}

function ImageUpload({
  canvasPreview,
  imageData,
  currentDimensions,
  isUploading,
  setIsUploading,
}: Props) {
  const [name, setName] = useState<string>("");

  const handleChange = async (_: React.SyntheticEvent<HTMLButtonElement>) => {
    setIsUploading(true);
    let c = canvasPreview?.current;
    let resize = determineResize(currentDimensions);
    if (c !== null && imageData !== null && !isUploading) {
      if (c.toDataURL().length > 20) {
        try {
          let image = await Jimp.read(c.toDataURL());
          image.resize(resize.width, resize.height);

          fetch("http://localhost:4567/addImage", {
            method: "POST",
            headers: {
              Accept: "application/json",
              "Content-Type": "application/json",
            },
            body: JSON.stringify({
              id: uuidv4(),
              name: name.trim().replace(" ", "-"),
              width: resize.width,
              height: resize.height,
              widthFrames: currentDimensions.width,
              heightFrames: currentDimensions.height,
              pixelData: image.bitmap.data.join(","),
              type: "IMAGE",
            }),
          })
            .then((res) => res.json())
            .then((d) => console.log(d))
            .then(() => {
              setIsUploading(false);
            })
            .catch((e) => {
              console.log(e);
              setIsUploading(false);
            });
        } catch (error) {
          console.log(error);
        }
      }
    }
  };

  return (
    <>
      <input
        type="text"
        value={name}
        onChange={(e) => setName(e.target.value)}
      />
      <button onClick={handleChange}>Upload</button>
    </>
  );
}

export default ImageUpload;
