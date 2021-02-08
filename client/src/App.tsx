import React, {SyntheticEvent, useState} from 'react';
import './style.css';

function App() {
  const [imageSource, setImageSource] = useState<FileReader | null>(null);

  const handleUpload = (e: SyntheticEvent<HTMLInputElement>) => {
    let reader = new FileReader();
    reader.readAsDataURL(((e?.target as HTMLInputElement).files as FileList)[0]);
    setImageSource(reader);
  }

  console.log(imageSource)

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
