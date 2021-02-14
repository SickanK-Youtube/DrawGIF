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
import { PixelData, Dimensions } from "./types";
import { convertToPixelData, determineResize } from "./utils";

// 1. Choose picture <- Done!
// 2. Display picture <- Done!
// 3. Choose a width and height <- Done!
// 4. Use that width and height to crop <- Done!
// 5. On "complete" resize and get pixel data
// 6. On send: send the pixel data

function App() {
  const [imageSource, setImageSource] = useState<FileReader | null>(null);
  const [dimensions, setDimensions] = useState<Dimensions>({
    width: 16,
    height: 9,
  });
  const [currentDimensions, setCurrentDimensions] = useState<Dimensions>({
    width: 16,
    height: 9,
  });
  const [crop, setCrop] = useState<ReactCrop.Crop>({
    unit: "%",
    aspect: dimensions.width / dimensions.height,
  });
  const [completedCrop, setCompletedCrop] = useState<ReactCrop.Crop | null>(
    null
  );
  const [isCropped, setIsCropped] = useState<boolean>(false);
  const imgRef = useRef<any>(null);
  const canvasPreview = useRef<HTMLCanvasElement>(null);

  const [pixelData, setPixelData] = useState<PixelData[]>([[0, 0, 0, 0]]);

  const handleUpload = async (e: SyntheticEvent<HTMLInputElement>) => {
    let reader = new FileReader();

    await new Promise((resolve, reject) => {
      reader.readAsDataURL(
        ((e?.target as HTMLInputElement).files as FileList)[0]
      );
      reader.onload = () => resolve(true);
    });

    setImageSource(reader);
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
        console.log(data);
        console.log(
          determineResize(
            {
              width: (crop.width as number) * scaleX,
              height: crop.height as number,
            },
            dimensions
          )
        );
        //console.log(convertToPixelData(data?.data as Uint8ClampedArray));
      }
    }
  }, [completedCrop]);

  return (
    <div className="App">
      <input type="file" onChange={handleUpload} />
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
          width: Math.round(completedCrop?.width ?? 0),
          height: Math.round(completedCrop?.height ?? 0),
        }}
      ></canvas>
    </div>
  );
}

export default App;
