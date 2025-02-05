"use client";

import React, { useContext, useEffect } from "react";
import { Student } from "@/app/types/Student.type";
import Link from "next/link";
import DataTable, {
  StyledTableCell,
  StyledTableRow,
} from "@/app/components/common/DataTable";
import DeleteButton from "@/app/components/common/DeleteButton";
import EditButton from "@/app/components/common/EditButton";
import { Button } from "@mui/material";
import StudentContext from "@/app/utils/StudentContext";
import { useSnackbar } from "@/app/hooks/useSnackbar";

export default function StudentTable() {
  const columns = ["ID", "First name", "Last name", "Email", "Courses"];
  const { students, getStudents, error } = useContext(StudentContext);
  const { showSnackbar, snackbarElement } = useSnackbar();

  useEffect(() => {
    getStudents();
  }, []);

  if (!students && !error) {
    return <div className={"text-xl text-center"}>Loading...</div>;
  }

  if (error && !students) {
    return <div className={"text-xl text-center"}>{error}</div>;
  }

  const onDelete = () => {
    showSnackbar(`Student deleted successfully !`);
    getStudents();
  };

  const setRow = (student: Student) => (
    <StyledTableRow key={student.id}>
      <StyledTableCell component="th" scope="row">
        {student.id}
      </StyledTableCell>
      <StyledTableCell align="left">{student.firstName}</StyledTableCell>
      <StyledTableCell align="left">{student.lastName}</StyledTableCell>
      <StyledTableCell align="left">{student.email}</StyledTableCell>
      <StyledTableCell align="left">
        {student.courses.map((course, index) => (
          <span key={index} className={"mr-2"}>
            <Link
              href={`/courses/${course.id}`}
              className="font-small text-blue-600 hover:underline"
            >
              {course.courseName}
            </Link>
          </span>
        ))}
        <span>
          <Link
            href={`/students/${student.id}/addCourse`}
            className="font-small text-blue-800 hover:underline"
          >
            (manage courses)
          </Link>
        </span>
      </StyledTableCell>
      <StyledTableCell align="right">
        <EditButton onUpdate={getStudents} entityToBeUpdated={student} />
        <DeleteButton name={"student"} id={student.id} onDelete={onDelete} />
      </StyledTableCell>
    </StyledTableRow>
  );

  return (
    <>
      <DataTable
        columns={columns}
        button={
          <Button
            href="/students/create"
            variant="contained"
            size={"medium"}
            sx={{
              backgroundColor: "#8E8886",
              "&:hover": {
                backgroundColor: "rgb(87,87,91)",
              },
            }}
          >
            Add Student
          </Button>
        }
        data={students as Student[]}
        setRow={setRow}
        name={"student"}
      />

      {snackbarElement}
    </>
  );
}
