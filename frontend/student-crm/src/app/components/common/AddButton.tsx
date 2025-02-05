import React from "react";
import { Button } from "@mui/material";
import { useSnackbar } from "@/app/hooks/useSnackbar";

interface AddButtonProps {
  courseId: number;
  studentId: number;
  method: "addStudent" | "removeStudent";
  onUpdate: () => void;
  name: "Student" | "Course";
}

export default function AddButton({
  courseId,
  studentId,
  method,
  onUpdate,
  name,
}: AddButtonProps) {
  const { showSnackbar, snackbarElement } = useSnackbar();

  const handleClick = async () => {
    const response = await fetch(
      `http://localhost:8081/student-crm/api/courses/${method}?courseId=${courseId}&studentId=${studentId}`,
      { method: "PUT" },
    );
    if (response.ok) {
      const message =
        method === "addStudent" ? `${name} added!` : `${name} removed!`;
      showSnackbar(message);
      onUpdate();
    } else {
      showSnackbar(`Error ${response.status}`);
    }
  };

  return (
    <>
      {method === "addStudent" ? (
        <Button onClick={handleClick} variant="contained">
          ADD {name}
        </Button>
      ) : (
        <Button onClick={handleClick} variant="contained" color="warning">
          Remove {name}
        </Button>
      )}
      {snackbarElement}
    </>
  );
}
