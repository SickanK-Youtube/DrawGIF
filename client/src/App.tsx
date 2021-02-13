import dataUriToBuffer from "data-uri-to-buffer";
import React, {
  SyntheticEvent,
  useState,
  useRef,
  useEffect,
  ChangeEvent,
} from "react";
import ReactCrop from "react-image-crop";
import "react-image-crop/dist/ReactCrop.css";
import "./style.css";
type PixelData = [number, number, number, number];
interface Dimensions {
  width: number;
  height: number;
}

// 1. Choose picture <- Done!
// 2. Display picture <- Done!
// 3. Choose a width and height <- Done!
// 4. Use that width and height to crop <- Done!
// 5. On "complete" resize and get pixel data
// 6. On send: send the pixel data

function App() {
  const [imageSource, setImageSource] = useState<FileReader | null>(null);

  const [dimension, setDimension] = useState<Dimensions>({
    width: 16,
    height: 9,
  });
  const [crop, setCrop] = useState<ReactCrop.Crop>({
    aspect: dimension.width / dimension.height,
  });

  const [completed, setCompleted] = useState<boolean>(false);

  const [pixelData, setPixelData] = useState<PixelData[]>([[0, 0, 0, 0]]);
  const canvas = useRef<HTMLCanvasElement>(null);

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

  useEffect(() => {
    setCrop((c) => ({
      ...c,
      aspect: dimension.width / dimension.height,
    }));
  }, [dimension]);

  return (
    <div className="App">
      <input type="file" onChange={handleUpload} />
      <div>
        {typeof imageSource?.result === "string" ? (
          <ReactCrop
            src={imageSource.result}
            onChange={(c) => setCrop(c)}
            crop={crop}
            onComplete={() => setCompleted((c) => !c)}
            style={{ width: "100%" }}
          />
        ) : null}
      </div>
      <canvas style={{ width: "20px" }} ref={canvas}></canvas>
      <div>
        <input
          type="number"
          onChange={(e) =>
            setDimension((d) => ({
              ...d,
              width: Math.round(parseInt(e.target.value)),
            }))
          }
        />
        <input
          type="number"
          onChange={(e) =>
            setDimension((d) => ({
              ...d,
              height: Math.round(parseInt(e.target.value)),
            }))
          }
        />
      </div>
    </div>
  );
}

export default App;

// useEffect(() => {
//   let c = canvas.current;
//   if (c !== null) {
//     c.width = 1920;
//     c.height = 1080;
//     let ctx = c.getContext("2d");
//     let image = new Image();
//     image.onload = () => {
//       ctx?.drawImage(image, 0, 0);
//       let data = ctx?.getImageData(0, 0, 1920, 1080);
//       console.log(data);
//     };

//     if (typeof imageSource?.result === "string")
//       image.src = imageSource.result;
//   }
// }, [imageSource]);
