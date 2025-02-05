"use client";

import React from "react";
import CourseContext from "./CourseContext";
import { Course } from "@/app/types/Course.type";

export default function CourseProvider({
  children,
}: {
  children: React.ReactNode;
}) {
  const [courses, setCourses] = React.useState<Course[] | null>(null);
  const [error, setError] = React.useState<string | null>(null);
  const [course, setCourse] = React.useState<Course | null>(null);

  const getCourse = async (courseId: number) => {
    try {
      const response = await fetch(
        `http://localhost:8081/student-crm/api/courses/${courseId}`,
      );

      if (response.status === 404) {
        throw new Error("Course not found");
      } else if (!response.ok) {
        throw new Error("Server error");
      }

      const data = (await response.json()) as Course;
      setCourse(data);
    } catch (error: any) {
      if (error instanceof TypeError)
        setError("Network error: Unable to reach the server");
      else setError(error.message);
    }
  };

  const getCourses = async () => {
    try {
      const response = await fetch(
        `http://localhost:8081/student-crm/api/courses`,
      );

      if (!response.ok) {
        throw new Error("A server error has occurred while fetching courses");
      }

      const data = (await response.json()) as Course[];
      setCourses(data);
    } catch (error: any) {
      if (error instanceof TypeError)
        setError("Network error: Unable to reach the server");
      else setError(error.message);
    }
  };

  return (
    <CourseContext.Provider
      value={{
        courses,
        course,
        getCourse,
        getCourses,
        error,
      }}
    >
      {children}
    </CourseContext.Provider>
  );
}
