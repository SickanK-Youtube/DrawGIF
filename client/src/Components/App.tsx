import React, { useState } from "react";
import { Crop, Aspect, Size } from "../types/types";

import FileBrowser from "./FileBrowser";
import ImageCrop from "./ImageCrop";
import Options from "./Options";
import "../types/gifuct-js.d.ts";

import "../style.css";
import ImageUpload from "./ImageUpload";

function App() {
  const [image, setImage] = useState<string>("");
  const [crop, setCrop] = useState<Crop>({ x: 0, y: 0 });
  const [zoom, setZoom] = useState<number>(1);
  const [aspect, setAspect] = useState<Aspect>({ width: 1, height: 1 });
  const [size, setSize] = useState<Size>({ width: 1, height: 1 });

  return (
    <div className="App">
      <FileBrowser setImage={setImage} />

      <ImageCrop
        image={image}
        crop={crop}
        setCrop={setCrop}
        zoom={zoom}
        setZoom={setZoom}
        aspect={aspect}
        setSize={setSize}
      />

      <Options aspect={aspect} setAspect={setAspect} />
      <ImageUpload
        imageUrl={image}
        crop={crop}
        size={size}
        aspect={aspect}
        zoom={zoom}
      />
    </div>
  );
}

export default App;
