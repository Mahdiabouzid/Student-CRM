import React, { useState, useCallback } from "react";
import { IconButton, Snackbar } from "@mui/material";
import { CloseIcon } from "next/dist/client/components/react-dev-overlay/internal/icons/CloseIcon";

export const useSnackbar = () => {
  const [open, setOpen] = useState(false);
  const [message, setMessage] = useState("");

  const showSnackbar = useCallback((msg: string) => {
    setMessage(msg);
    setOpen(true);
  }, []);

  const handleClose = useCallback(
    (_event: React.SyntheticEvent | Event, reason?: string) => {
      if (reason === "clickaway") {
        return;
      }
      setOpen(false);
    },
    [],
  );

  const snackbarElement = (
    <Snackbar
      open={open}
      autoHideDuration={3000}
      onClose={handleClose}
      message={message}
      action={
        <React.Fragment>
          <IconButton
            size="small"
            aria-label="close"
            color="inherit"
            onClick={handleClose}
          >
            <CloseIcon />
          </IconButton>
        </React.Fragment>
      }
    />
  );

  return { showSnackbar, snackbarElement };
};
