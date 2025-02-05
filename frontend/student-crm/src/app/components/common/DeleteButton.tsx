import DeleteIcon from "@mui/icons-material/Delete";
import {
  Button,
  Dialog,
  DialogActions,
  DialogContent,
  DialogContentText,
  DialogTitle,
  IconButton,
} from "@mui/material";
import React, { useState } from "react";

interface DeleteButtonProps {
  name: "student" | "course";
  id: number;
  onDelete: () => void;
}

export default function DeleteButton({
  id,
  onDelete,
  name,
}: DeleteButtonProps) {
  const [open, setOpen] = useState(false);

  const handleConfirm = async () => {
    const response = await fetch(
      `http://localhost:8081/student-crm/api/${name}s/delete/${id}`,
      { method: "DELETE" },
    );

    if (!response.ok) {
      console.log("error on delete");
    } else {
      setOpen(false);
      onDelete();
    }
  };

  const handleClose = () => {
    setOpen(false);
  };

  const handleDelete = () => {
    setOpen(true);
  };

  return (
    <>
      <IconButton aria-label="delete" onClick={handleDelete}>
        <DeleteIcon />
      </IconButton>
      <Dialog
        open={open}
        onClose={handleClose}
        aria-labelledby="alert-dialog-title"
        aria-describedby="alert-dialog-description"
      >
        <DialogTitle id="alert-dialog-title">{"Are you sure ?"}</DialogTitle>
        <DialogContent>
          <DialogContentText id="alert-dialog-description">
            Confirming this will delete the {name} with ID{" "}
            <span className="font-semibold">{id}</span> permanently.
          </DialogContentText>
        </DialogContent>
        <DialogActions>
          <Button onClick={handleClose}>Cancel</Button>
          <Button onClick={handleConfirm} autoFocus>
            Confirm
          </Button>
        </DialogActions>
      </Dialog>
    </>
  );
}
