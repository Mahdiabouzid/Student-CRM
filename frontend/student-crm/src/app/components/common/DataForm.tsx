import { Box } from "@mui/material";
import React from "react";

interface DataFormProps {
  handleSubmit: (event: React.FormEvent) => void;
  children: React.ReactNode;
}

export default function DataForm({ children, handleSubmit }: DataFormProps) {
  return (
    <Box
      component="form"
      role={"form"}
      sx={{ "& .MuiTextField-root": { m: 2, width: { md: "50ch" } } }}
      className="flex flex-col items-center"
      onSubmit={handleSubmit}
    >
      {children}
    </Box>
  );
}
