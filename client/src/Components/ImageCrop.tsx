import React, { SetStateAction } from "react";
import { useEffect } from "react";
import Cropper from "react-easy-crop";
import { Area } from "react-easy-crop/types";
import { Aspect, Crop, Size } from "../types/types";

interface Props {
  image: string;
  crop: Crop;
  setCrop: React.Dispatch<SetStateAction<Crop>>;
  zoom: number;
  setZoom: React.Dispatch<SetStateAction<number>>;
  aspect: Aspect;
  setSize: React.Dispatch<SetStateAction<Size>>;
}

function ImageCrop({
  image,
  crop,
  setCrop,
  zoom,
  setZoom,
  aspect,
  setSize,
}: Props) {
  return (
    <>
      <div className="crop">
        <Cropper
          image={image}
          crop={crop}
          zoom={zoom}
          aspect={aspect.width / aspect.height}
          onCropComplete={(_: Area, a: Area) => setSize(a)}
          onCropChange={(c: Crop) => setCrop(c)}
          onZoomChange={(z: number) => setZoom(z)}
        />
      </div>
    </>
  );
}

export default ImageCrop;
