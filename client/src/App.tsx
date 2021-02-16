import dataUriToBuffer from "data-uri-to-buffer";
import React, {
  SyntheticEvent,
  useState,
  useRef,
  useEffect,
  useCallback,
} from "react";
import ReactCrop from "react-image-crop";
import "react-image-crop/dist/ReactCrop.css";
import "./style.css";
import { Dimensions, Size } from "./types";
import { convertToPixelData, determineResize } from "./utils";
import Jimp from "jimp";

function App() {
  const [imageSource, setImageSource] = useState<FileReader | null>(null);
  const [dimensions, setDimensions] = useState<Dimensions>({
    width: 1,
    height: 1,
  });
  const [currentDimensions, setCurrentDimensions] = useState<Dimensions>({
    width: 1,
    height: 1,
  });
  const [crop, setCrop] = useState<ReactCrop.Crop>({
    aspect: dimensions.width / dimensions.height,
  });
  const [completedCrop, setCompletedCrop] = useState<ReactCrop.Crop | null>(
    null
  );
  const [isCropped, setIsCropped] = useState<boolean>(false);
  const [isUploading, setIsUploading] = useState<boolean>(false);
  const [imageData, setImageData] = useState<ImageData | null>(null);

  const imgRef = useRef<any>(null);
  const canvasPreview = useRef<HTMLCanvasElement>(null);

  const handleFileUpload = async (e: SyntheticEvent<HTMLInputElement>) => {
    let reader = new FileReader();

    await new Promise((resolve, _) => {
      reader.readAsDataURL(
        ((e?.target as HTMLInputElement).files as FileList)[0]
      );
      reader.onload = () => resolve(true);
    });

    setImageSource(reader);
  };

  const handleUpload = async (e: SyntheticEvent<HTMLButtonElement>) => {
    setIsUploading(true);
    let c = canvasPreview?.current;
    let resize = determineResize(dimensions);
    if (c !== null && imageData !== null && !isUploading) {
      if (c.toDataURL().length > 20) {
        try {
          let image = await Jimp.read(c.toDataURL());
          image.resize(resize.width, resize.height);
          console.log(image.bitmap.data.length / 4);
          console.log(resize);

          fetch("http://localhost:4567/addImage", {
            method: "POST",
            headers: {
              Accept: "application/json",
              "Content-Type": "application/json",
            },
            body: JSON.stringify({
              width: resize.width,
              height: resize.height,
              widthFrames: currentDimensions.width,
              heightFrames: currentDimensions.height,
              pixelData: image.bitmap.data.join(","),
            }),
          })
            .then((res) => res.json())
            .then((d) => console.log(d))
            .then(() => {
              console.log("test");
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
    <div className="App">
      <input type="file" onChange={handleFileUpload} />
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
      <div>
        <input
          type="number"
          value={dimensions.width}
          onChange={(e) =>
            setDimensions((d) => ({
              ...d,
              width: Math.round(parseInt(e.target.value)),
            }))
          }
        />
        <input
          type="number"
          value={dimensions.height}
          onChange={(e) =>
            setDimensions((d) => ({
              ...d,
              height: Math.round(parseInt(e.target.value)),
            }))
          }
        />
      </div>
      <canvas
        ref={canvasPreview}
        style={{
          display: "none",
        }}
      ></canvas>
      <button onClick={handleUpload}>Upload</button>
    </div>
  );
}

export default App;
