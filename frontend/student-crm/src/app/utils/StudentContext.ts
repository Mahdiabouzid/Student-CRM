"use client";

import React from "react";
import { Student } from "@/app/types/Student.type";

const StudentContext = React.createContext({
  students: null as Student[] | null,
  student: null as Student | null,
  getStudents: () => {},
  getStudent: (studentId: number) => {},
  error: null as string | null,
});

export default StudentContext;
