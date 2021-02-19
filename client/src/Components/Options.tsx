import React from "react";
import { Dimensions } from "../types";

interface Props {
  dimensions: Dimensions;
  setDimensions: React.Dispatch<React.SetStateAction<Dimensions>>;
}

function Options({ dimensions, setDimensions }: Props) {
  return (
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
  );
}

export default Options;
