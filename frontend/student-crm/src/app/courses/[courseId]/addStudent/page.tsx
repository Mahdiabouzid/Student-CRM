"use client";

import React, { useContext, useEffect } from "react";
import { Student } from "@/app/types/Student.type";
import DataTable, {
  StyledTableCell,
  StyledTableRow,
} from "@/app/components/common/DataTable";
import AddButton from "@/app/components/common/AddButton";
import Link from "next/link";
import StudentContext from "@/app/utils/StudentContext";

export default function Page({
  params,
  searchParams,
}: {
  params: { courseId: number };
  searchParams: { courseName: string };
}) {
  const columns = ["ID", "First name", "Last name"];
  const { students, getStudents, error } = useContext(StudentContext);

  useEffect(() => {
    getStudents();
  }, []);

  if (!students && !error) {
    return <div className={"text-xl"}>Loading...</div>;
  }

  if (error && !students) {
    return <div className={"text-xl"}>{error}</div>;
  }

  const isStudentInCourse = (student: Student): boolean => {
    return student.courses.some(
      (course) => course.id === parseInt(String(params.courseId)),
    );
  };

  const setRow = (student: Student) => {
    return (
      <StyledTableRow key={student.id}>
        <StyledTableCell component="th" scope="row">
          {student.id}
        </StyledTableCell>
        <StyledTableCell align="left">{student.firstName}</StyledTableCell>
        <StyledTableCell align="left">{student.lastName}</StyledTableCell>

        <StyledTableCell align="right">
          {isStudentInCourse(student) ? (
            <AddButton
              name={"Student"}
              courseId={params.courseId}
              studentId={student.id}
              method={"removeStudent"}
              onUpdate={getStudents}
            />
          ) : (
            <AddButton
              name={"Student"}
              courseId={params.courseId}
              studentId={student.id}
              method={"addStudent"}
              onUpdate={getStudents}
            />
          )}
        </StyledTableCell>
      </StyledTableRow>
    );
  };

  return (
    <div className={"my-10 w-10/12"}>
      <h1 className={"text-3xl text-center"}>
        Course:{" "}
        <Link href={`/courses/${params.courseId}`}>
          {searchParams.courseName}
        </Link>
      </h1>
      <DataTable
        name={"student"}
        columns={columns}
        data={students!!}
        setRow={setRow}
      />
    </div>
  );
}
