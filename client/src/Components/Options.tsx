import React from "react";
import { Aspect } from "../types/types";

interface Props {
  aspect: Aspect;
  setAspect: React.Dispatch<React.SetStateAction<Aspect>>;
}

function Options({ aspect, setAspect }: Props) {
  return (
    <div className="options-wh">
      <div>
        <p className="text">Width</p>
        <input
          className="input"
          type="number"
          value={aspect.width}
          onChange={(e) =>
            setAspect((a) => ({
              ...a,
              width: Math.round(parseInt(e.target.value)),
            }))
          }
        />
      </div>
      <div>
        <p className="text">Height</p>
        <input
          className="input"
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
    </div>
  );
}

export default Options;
