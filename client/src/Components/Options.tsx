import React from "react";
import { Aspect } from "../types";

interface Props {
  aspect: Aspect;
  setAspect: React.Dispatch<React.SetStateAction<Aspect>>;
}

function Options({ aspect, setAspect }: Props) {
  return (
    <div>
      <input
        type="number"
        value={aspect.width}
        onChange={(e) =>
          setAspect((a) => ({
            ...a,
            width: Math.round(parseInt(e.target.value)),
          }))
        }
      />
      <input
        type="number"
        value={aspect.height}
        onChange={(e) =>
          setAspect((a) => ({
            ...a,
            height: Math.round(parseInt(e.target.value)),
          }))
        }
      />
    </div>
  );
}

export default Options;
