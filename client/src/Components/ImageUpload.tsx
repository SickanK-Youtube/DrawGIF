import React, { useState, useRef } from "react";
import { v4 as uuidv4 } from "uuid";
import getImagePixels from "../imageHandler";
import { Aspect, Crop, Size } from "../types/types";

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
      try {
        let imageData = await getImagePixels(
          imageUrl,
          aspect,
          crop,
          size,
          zoom
        );

        let pixels = "";

        if (imageData.type === "image/gif") {
          pixels = imageData.pixels.join(";");
        } else {
          pixels = imageData.pixels[0];
        }

        fetch("http://localhost:4567/addImage", {
          method: "POST",
          headers: {
            Accept: "application/json",
            "Content-Type": "application/json",
          },
          body: JSON.stringify({
            id: uuidv4(),
            name: name.trim().replace(" ", "-"),
            width: imageData.width,
            height: imageData.height,
            widthFrames: aspect.width,
            heightFrames: aspect.height,
            pixelData: pixels,
            type: imageData.type === "image/gif" ? "GIF" : "IMG",
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
      } catch (e) {
        console.error(e);
        setIsUploading(false);
      }
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
