"use client";

import React, { useEffect } from "react";
import DataTable, {
  StyledTableCell,
  StyledTableRow,
} from "@/app/components/common/DataTable";
import AddButton from "@/app/components/common/AddButton";
import { Course } from "@/app/types/Course.type";
import CourseContext from "@/app/utils/CourseContext";

export default function Page({ params }: { params: { studentId: number } }) {
  const { courses, getCourses, error } = React.useContext(CourseContext);
  const columns = ["ID", "Course name"];

  useEffect(() => {
    getCourses();
  }, []);

  if (error && !courses) {
    return <div className={"text-xl"}>{error}</div>;
  }

  if (!courses && !error) {
    return <div className={"text-xl"}>Loading...</div>;
  }

  const isStudentInCourse = (course: Course): boolean => {
    return course.students.some(
      (student) => student.id === parseInt(String(params.studentId)),
    );
  };

  const setRow = (course: Course) => {
    return (
      <StyledTableRow key={course.id}>
        <StyledTableCell component="th" scope="row">
          {course.id}
        </StyledTableCell>
        <StyledTableCell align="left">{course.courseName}</StyledTableCell>

        <StyledTableCell align="right">
          {isStudentInCourse(course) ? (
            <AddButton
              name={"Course"}
              courseId={course.id}
              studentId={params.studentId}
              method={"removeStudent"}
              onUpdate={getCourses}
            />
          ) : (
            <AddButton
              name={"Course"}
              courseId={course.id}
              studentId={params.studentId}
              method={"addStudent"}
              onUpdate={getCourses}
            />
          )}
        </StyledTableCell>
      </StyledTableRow>
    );
  };

  return (
    <div className={"w-10/12"}>
      <DataTable
        columns={columns}
        data={courses!!}
        setRow={setRow}
        name={"course"}
      />
    </div>
  );
}
