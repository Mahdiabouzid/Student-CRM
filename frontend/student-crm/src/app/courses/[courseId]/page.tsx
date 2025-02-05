"use client";

import React, { useEffect } from "react";
import Link from "next/link";
import CourseContext from "@/app/utils/CourseContext";

export default function Page({ params }: { params: { courseId: number } }) {
  const { course, getCourse, error } = React.useContext(CourseContext);

  useEffect(() => {
    getCourse(params.courseId);
  }, [params.courseId]);

  if (error && !course) {
    return <div className={"text-xl text-center"}>{error}</div>;
  }

  if (!course && !error) {
    return <div className={"text-xl text-center"}>Loading...</div>;
  }

  if (course!!.students.length === 0) {
    return (
      <div>
        <h1 className={"text-3xl text-center my-8"}>{course?.courseName}</h1>
        <h2 className={"text-xl font-semibold"}>
          This course has currently no students.
        </h2>
      </div>
    );
  }

  return (
    <div className={"flex flex-col h-96 w-3/4 gap-y-10"}>
      <h1 className={"text-3xl text-center"}>{course?.courseName}</h1>
      <h2 className={"text-xl font-semibold"}>Enrolled students: </h2>
      <div className={"w-1/2"}>
        <ul className={"list-disc text-gray-400"}>
          {course!!.students.map((student, index) => (
            <li key={index} className={"my-2"}>
              <div className={"mx-2 flex w-full justify-between"}>
                {student.firstName} {student.lastName}
                <Link
                  href={`/students/${student.id}`}
                  className={
                    "font-small text-blue-600 dark:text-blue-500 hover:underline"
                  }
                >
                  {student.email}
                </Link>
              </div>
            </li>
          ))}
        </ul>
      </div>
    </div>
  );
}
