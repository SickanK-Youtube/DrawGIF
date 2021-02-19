import React, { useState, useRef, useEffect, useCallback } from "react";

import { Dimensions } from "../types";
import ReactCrop from "react-image-crop";
import "react-image-crop/dist/ReactCrop.css";

import FileBrowser from "./FileBrowser";
import ImageUpload from "./ImageUpload";
import Options from "./Options";
import ImageCrop from "./ImageCrop";

import "../style.css";

function App() {
  const defaultDimensions = {
    width: 1,
    height: 1,
  };

  const [isUploading, setIsUploading] = useState<boolean>(false);
  const [imageSource, setImageSource] = useState<FileReader | null>(null);
  const [imageData, setImageData] = useState<ImageData | null>(null);
  const [dimensions, setDimensions] = useState<Dimensions>(defaultDimensions);
  const [currentDimensions, setCurrentDimensions] = useState<Dimensions>(
    defaultDimensions
  );

  const [isCropped, setIsCropped] = useState<boolean>(false);
  const [crop, setCrop] = useState<ReactCrop.Crop>({
    aspect: dimensions.width / dimensions.height,
  });
  const [completedCrop, setCompletedCrop] = useState<ReactCrop.Crop | null>(
    null
  );

  const imgRef = useRef<any>(null);
  const canvasPreview = useRef<HTMLCanvasElement>(null);

  return (
    <div className="App">
      <FileBrowser setImageSource={setImageSource} />

      <ImageCrop
        dimensions={dimensions}
        canvasPreview={canvasPreview}
        setImageData={setImageData}
        crop={crop}
        setCrop={setCrop}
        setIsCropped={setIsCropped}
        setCurrentDimensions={setCurrentDimensions}
        completedCrop={completedCrop}
        setCompletedCrop={setCompletedCrop}
        imgRef={imgRef}
        imageSource={imageSource}
      />

      <Options dimensions={dimensions} setDimensions={setDimensions} />

      <ImageUpload
        imageData={imageData}
        canvasPreview={canvasPreview}
        currentDimensions={currentDimensions}
        isUploading={isUploading}
        setIsUploading={setIsUploading}
      />
    </div>
  );
}

export default App;
