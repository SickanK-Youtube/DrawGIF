import React, { useState } from "react";
import Jimp from "jimp";
import { v4 as uuidv4 } from "uuid";
import { Aspect, Crop, Size } from "../types";
import { determineResize } from "../utils";

interface Props {
  imageUrl: string;
  aspect: Aspect;
  crop: Crop;
  size: Size;
  zoom: number;
}

function ImageUpload({ imageUrl, aspect, crop, size, zoom }: Props) {
  const [name, setName] = useState<string>("");
  const [isUploading, setIsUploading] = useState<boolean>(false);

  const handleClick = async (_: React.SyntheticEvent<HTMLButtonElement>) => {
    setIsUploading(true);

    if (!isUploading) {
      let image = await Jimp.read(imageUrl);
      let resize = determineResize(aspect);
      let x = (image.getWidth() * zoom) / 2 - crop.x - size.width / 2;
      let y = (image.getHeight() * zoom) / 2 - crop.y - size.height / 2;

      image.crop(x, y, size.width, size.height);
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
          widthFrames: aspect.width,
          heightFrames: aspect.height,
          pixelData: image.bitmap.data.join(","),
          type: "IMG",
        }),
      })
        .then((res) => res.json())
        .then((d) => console.log(d))
        .catch((e) => {
          console.log(e);
        })
        .finally(() => {
          setIsUploading(false);
        });
    }
  };

  return (
    <>
      <input
        type="text"
        onChange={(e) => setName(e.target.value)}
        value={name}
      />
      <button onClick={handleClick}>Upload</button>
    </>
  );
}

export default ImageUpload;
