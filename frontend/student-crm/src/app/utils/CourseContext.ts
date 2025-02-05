import React from "react";
import { Course } from "@/app/types/Course.type";

const CourseContext = React.createContext({
  courses: null as Course[] | null,
  course: null as Course | null,
  getCourses: () => {},
  getCourse: (courseId: number) => {},
  error: null as string | null,
});

export default CourseContext;
