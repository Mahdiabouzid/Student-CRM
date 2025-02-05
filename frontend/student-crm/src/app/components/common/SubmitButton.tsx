import React from "react";
import { Button } from "@mui/material";

export default function SubmitButton({
  children,
}: {
  children: string[] | string;
}) {
  return (
    <Button
      type={"submit"}
      variant="contained"
      size={"medium"}
      className="w-full md:w-1/2 m-5"
      id={"submit"}
      sx={{
        backgroundColor: "#8E8886",
        "&:hover": {
          backgroundColor: "rgb(87,87,91)",
        },
      }}
    >
      {children}
    </Button>
  );
}
