"use client";

import React, { useEffect } from "react";
import DataTable, {
  StyledTableCell,
  StyledTableRow,
} from "@/app/components/common/DataTable";
import Link from "next/link";
import { Course } from "@/app/types/Course.type";
import DeleteButton from "@/app/components/common/DeleteButton";
import { Button } from "@mui/material";
import EditButton from "@/app/components/common/EditButton";
import CourseContext from "@/app/utils/CourseContext";
import { useSnackbar } from "@/app/hooks/useSnackbar";

export default function CourseTable() {
  const { courses, getCourses, error } = React.useContext(CourseContext);
  const columns = ["ID", "Course name", "Students"];
  const { showSnackbar, snackbarElement } = useSnackbar();

  useEffect(() => {
    getCourses();
  }, []);

  if (!courses && !error) {
    return <div className={"text-xl text-center"}>Loading...</div>;
  }

  if (error && !courses) {
    return <div className={"text-xl text-center"}>{error}</div>;
  }

  const onDelete = () => {
    showSnackbar(`Course deleted successfully !`);
    getCourses();
  };

  const setRow = (course: Course) => (
    <StyledTableRow key={course.id}>
      <StyledTableCell component="th" scope="row" className={"font-bold"}>
        {course.id}
      </StyledTableCell>
      <StyledTableCell align="left">{course.courseName}</StyledTableCell>
      <StyledTableCell align="left">
        {course.students.map((student, index) => (
          <span key={index} className={"mr-4"}>
            {`${student.firstName} ${student.lastName}.`}
          </span>
        ))}
        <span>
          <Link
            href={{
              pathname: `/courses/${course.id}/addStudent`,
              query: { courseName: course.courseName },
            }}
            className="font-small text-blue-600 dark:text-blue-500 hover:underline"
          >
            (manage students)
          </Link>
        </span>
      </StyledTableCell>
      <StyledTableCell align="right">
        <EditButton entityToBeUpdated={course} onUpdate={getCourses} />
        <DeleteButton name={"course"} id={course.id} onDelete={onDelete} />
      </StyledTableCell>
    </StyledTableRow>
  );

  return (
    <>
      <DataTable
        columns={columns}
        button={
          <Button
            href="/courses/create"
            variant="contained"
            size={"medium"}
            sx={{
              backgroundColor: "#8E8886",
              "&:hover": {
                backgroundColor: "rgb(87,87,91)",
              },
            }}
          >
            Add Course
          </Button>
        }
        name={"course"}
        data={courses!!}
        setRow={setRow}
      />

      {snackbarElement}
    </>
  );
}
