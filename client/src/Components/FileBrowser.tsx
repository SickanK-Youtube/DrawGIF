import React from "react";

interface Props {
  setImage: React.Dispatch<React.SetStateAction<string>>;
}

function FileBrowser({ setImage }: Props) {
  const handleChange = async (e: React.SyntheticEvent<HTMLInputElement>) => {
    let reader = new FileReader();

    await new Promise((resolve, _) => {
      reader.readAsDataURL(
        ((e?.target as HTMLInputElement).files as FileList)[0]
      );
      reader.onload = () => resolve(true);
    });

    setImage(reader.result as string);
  };

  return (
    <div className="button">
      New
      <input className="button-file" type="file" onChange={handleChange} />
    </div>
  );
}

export default FileBrowser;
