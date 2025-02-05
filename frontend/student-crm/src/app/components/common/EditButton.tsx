import { isStudent, Student } from "@/app/types/Student.type";
import { Course, isCourse } from "@/app/types/Course.type";
import React from "react";
import { Box, Button, Modal, Typography } from "@mui/material";
import StudentForm from "@/app/components/student/StudentForm";
import CourseForm from "@/app/components/course/CourseForm";

interface EditButtonProps<T extends Course | Student> {
  entityToBeUpdated: T;
  onUpdate: () => void;
}

const style = {
  position: "absolute" as "absolute",
  top: "50%",
  left: "50%",
  transform: "translate(-50%, -50%)",
  width: { md: 600, sm: 600, xs: 350 },
  bgcolor: "background.paper",
  boxShadow: 24,
  p: 4,
  items: "center",
};

export default function EditButton<T extends Student | Course>({
  entityToBeUpdated,
  onUpdate,
}: EditButtonProps<T>) {
  const [open, setOpen] = React.useState(false);

  const handleClose = () => {
    onUpdate();
    setOpen(false);
  };

  const handleClick = () => {
    setOpen(true);
  };

  const getForm = () => {
    if (isStudent(entityToBeUpdated)) {
      return (
        <StudentForm
          toUpdateStudent={entityToBeUpdated}
          handleCloseModal={handleClose}
        />
      );
    } else if (isCourse(entityToBeUpdated)) {
      return (
        <CourseForm
          courseToBeUpdated={entityToBeUpdated}
          handleCloseModal={handleClose}
        />
      );
    }
  };

  return (
    <>
      <Button variant="outlined" size={"small"} onClick={handleClick}>
        edit
      </Button>
      <Modal open={open} onClose={handleClose} aria-labelledby="modal-title">
        <Box sx={style}>
          <Typography
            id="modal-title"
            variant="h6"
            component="h2"
            className={"my-5"}
          >
            Update {isStudent(entityToBeUpdated) ? "Student" : "Course"}
          </Typography>
          {getForm()}
        </Box>
      </Modal>
    </>
  );
}
