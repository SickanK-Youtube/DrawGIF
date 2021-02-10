import dataUriToBuffer from 'data-uri-to-buffer';
import React, {SyntheticEvent, useState} from 'react';
import './style.css';

function App() {
  const [imageSource, setImageSource] = useState<FileReader | null>(null);

  const handleUpload = async (e: SyntheticEvent<HTMLInputElement>) => {
    let reader = new FileReader();

    await new Promise((resolve, reject) => {
      reader.readAsDataURL(((e?.target as HTMLInputElement).files as FileList)[0]);
      reader.onload = () => resolve(true);
    });

    if(typeof reader.result === "string"){
      let buffer = dataUriToBuffer(reader.result);
    }

    setImageSource(reader);
  }


  return (
    <div className="App">
      <input type="file" onChange={handleUpload}/>
      { typeof imageSource?.result === "string" ? 
          <img src={imageSource.result} />
          : null
      }
    </div>
  );
}

export default App;
