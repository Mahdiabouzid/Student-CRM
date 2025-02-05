"use client";

import React, { useContext, useEffect } from "react";
import Link from "next/link";
import StudentContext from "@/app/utils/StudentContext";

export default function Page({ params }: { params: { studentId: number } }) {
  const { student, getStudent, error } = useContext(StudentContext);

  useEffect(() => {
    getStudent(params.studentId);
  }, [params.studentId]);

  if (error && !student) {
    return <div className={"text-xl text-center"}>{error}</div>;
  }

  if (!student && !error) {
    return <div className={"text-xl text-center"}>Loading...</div>;
  }

  if (student?.courses.length === 0) {
    return (
      <div>
        <h1 className={"text-3xl text-center my-8"}>
          {student.firstName} {student.lastName}
        </h1>
        <h2 className={"text-xl font-semibold"}>
          This student is currently not enrolled in any course.
        </h2>
      </div>
    );
  }

  return (
    <div className={"flex flex-col h-96 w-3/4 gap-y-10"}>
      <h1 className={"text-3xl text-center capitalize"}>
        {student?.firstName} {student?.lastName}
      </h1>
      <h2 className={"text-xl font-semibold"}>Courses: </h2>
      <div className={"w-1/2"}>
        <ul className={"list-disc text-gray-400"}>
          {student!!.courses.map((course, index) => (
            <li key={index} className={"my-2"}>
              <Link
                href={`/courses/${course.id}`}
                className={
                  "font-small text-blue-600 dark:text-blue-500 hover:underline"
                }
              >
                {course.courseName}
              </Link>
            </li>
          ))}
        </ul>
      </div>
    </div>
  );
}
