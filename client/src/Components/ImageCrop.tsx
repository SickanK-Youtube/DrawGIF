import React from "react";
import { useEffect } from "react";
import { useCallback } from "react";
import ReactCrop from "react-image-crop";
import { Dimensions } from "../types";

interface Props {
  dimensions: Dimensions;
  canvasPreview: React.RefObject<HTMLCanvasElement>;
  setImageData: React.Dispatch<React.SetStateAction<ImageData | null>>;
  crop: ReactCrop.Crop;
  setCrop: React.Dispatch<React.SetStateAction<ReactCrop.Crop>>;
  setIsCropped: React.Dispatch<React.SetStateAction<boolean>>;
  setCurrentDimensions: React.Dispatch<React.SetStateAction<Dimensions>>;
  completedCrop: ReactCrop.Crop | null;
  setCompletedCrop: React.Dispatch<React.SetStateAction<ReactCrop.Crop | null>>;
  imgRef: React.MutableRefObject<any>;
  imageSource: FileReader | null;
}

function ImageCrop({
  dimensions,
  canvasPreview,
  setImageData,
  crop,
  setCrop,
  setIsCropped,
  setCurrentDimensions,
  completedCrop,
  setCompletedCrop,
  imgRef,
  imageSource,
}: Props) {
  const onLoad = useCallback((img) => {
    imgRef.current = img;
  }, []);

  useEffect(() => {
    setCrop((c) => ({
      ...c,
      aspect: dimensions.width / dimensions.height,
    }));
  }, [dimensions]);

  useEffect(() => {
    if (crop.width !== 0) {
      setIsCropped(true);
    } else {
      setIsCropped(false);
    }

    if (completedCrop !== null) {
      const c = canvasPreview.current;
      const image = imgRef.current;
      if (c !== null && image instanceof HTMLImageElement) {
        const scaleX = image.naturalWidth / image.width;
        const scaleY = image.naturalHeight / image.height;
        c.width = crop.width as number;
        c.height = crop.height as number;

        setCurrentDimensions({
          width: dimensions.width,
          height: dimensions.height,
        });

        const ctx = c.getContext("2d");
        ctx?.drawImage(
          image,
          (crop.x as number) * scaleX,
          (crop.y as number) * scaleY,
          (crop.width as number) * scaleX,
          (crop.height as number) * scaleY,
          0,
          0,
          crop.width as number,
          crop.height as number
        );

        let data = ctx?.getImageData(
          0,
          0,
          (crop.width as number) * scaleX || 1,
          (crop.height as number) * scaleY || 1
        );
        if (data !== undefined) setImageData(data);
      }
    }
  }, [completedCrop]);

  return (
    <>
      <div>
        {typeof imageSource?.result === "string" ? (
          <ReactCrop
            src={imageSource.result}
            onChange={(c) => setCrop(c)}
            onImageLoaded={onLoad}
            crop={crop}
            onComplete={(c) => setCompletedCrop(c)}
            style={{ width: "100%" }}
          />
        ) : null}
      </div>

      <canvas
        ref={canvasPreview}
        style={{
          display: "none",
        }}
      ></canvas>
    </>
  );
}

export default ImageCrop;
