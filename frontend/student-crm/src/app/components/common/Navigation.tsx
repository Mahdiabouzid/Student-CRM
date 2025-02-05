"use client";

import {
  AppBar,
  Box,
  Button,
  Toolbar,
  Typography,
  IconButton,
  Menu,
  MenuItem,
} from "@mui/material";
import MenuIcon from "@mui/icons-material/Menu";
import Link from "next/link";
import React from "react";

export default function Navigation() {
  const [anchorEl, setAnchorEl] = React.useState<null | HTMLElement>(null);
  const open = Boolean(anchorEl);

  const handleClick = (event: React.MouseEvent<HTMLElement>) => {
    setAnchorEl(event.currentTarget);
  };

  const handleClose = () => {
    setAnchorEl(null);
  };

  return (
    <Box sx={{ flexGrow: 1 }}>
      <AppBar
        position="static"
        sx={{ backgroundColor: "#4b4848", px: { md: "5rem", xs: "2rem" } }}
      >
        <Toolbar sx={{ justifyContent: "space-between" }}>
          <IconButton
            size="large"
            edge="start"
            color="inherit"
            aria-label="menu"
            sx={{ mr: 2, display: { md: "none" } }} // Display menu icon only on small screens
            onClick={handleClick}
          >
            <MenuIcon />
          </IconButton>
          <Menu
            id="basic-menu"
            anchorEl={anchorEl}
            open={open}
            onClose={handleClose}
            MenuListProps={{
              "aria-labelledby": "basic-button",
            }}
          >
            <MenuItem onClick={handleClose}>
              <Link href={"/students"}>Students</Link>
            </MenuItem>
            <MenuItem onClick={handleClose}>
              <Link href={"/courses"}>Courses</Link>
            </MenuItem>
          </Menu>
          <Typography variant="h6" component="div">
            <Link href={"/"}>Student CRM</Link>
          </Typography>

          <Box
            sx={{ display: { xs: "none", md: "flex" }, alignItems: "center" }}
          >
            <Button
              href={"/students"}
              sx={{
                ml: 2,
                color: "white",
                display: "block",
                "&:hover": {
                  backgroundColor: "rgb(87,87,91)",
                },
              }}
            >
              Students
            </Button>
            <Button
              href={"/courses"}
              sx={{
                ml: 2,
                color: "white",
                display: "block",
                "&:hover": {
                  backgroundColor: "rgb(87,87,91)",
                },
              }}
            >
              Courses
            </Button>
          </Box>
        </Toolbar>
      </AppBar>
    </Box>
  );
}
