import React from "react";

interface Props {
  setImageSource: React.Dispatch<React.SetStateAction<FileReader | null>>;
}

function FileBrowser({ setImageSource }: Props) {
  const handleChange = async (e: React.SyntheticEvent<HTMLInputElement>) => {
    let reader = new FileReader();

    await new Promise((resolve, _) => {
      reader.readAsDataURL(
        ((e?.target as HTMLInputElement).files as FileList)[0]
      );
      reader.onload = () => resolve(true);
    });

    setImageSource(reader);
  };

  return <input type="file" onChange={handleChange} />;
}

export default FileBrowser;
