"use client";

import { Student } from "@/app/types/Student.type";
import React from "react";
import StudentContext from "./StudentContext";

export default function StudentProvider({
  children,
}: {
  children: React.ReactNode;
}) {
  const [students, setStudents] = React.useState<Student[] | null>(null);
  const [error, setError] = React.useState<string | null>(null);
  const [student, setStudent] = React.useState<Student | null>(null);

  const getStudent = async (studentId: number) => {
    try {
      const response = await fetch(
        `http://localhost:8081/student-crm/api/students/${studentId}`,
      );

      if (response.status === 404) {
        throw new Error("Student not found");
      } else if (!response.ok) {
        throw new Error("Server error");
      }
      const data = (await response.json()) as Student;
      setStudent(data);
    } catch (error: any) {
      if (error instanceof TypeError)
        setError("Network error: Unable to reach the server");
      else setError(error.message);
    }
  };

  const getStudents = async () => {
    try {
      const response = await fetch(
        `http://localhost:8081/student-crm/api/students`,
      );
      if (!response.ok) {
        throw new Error("A server error has occurred while fetching students");
      }
      const data = (await response.json()) as Student[];
      setStudents(data);
    } catch (error: any) {
      if (error instanceof TypeError)
        setError("Network error: Unable to reach the server");
      else setError(error.message);
    }
  };

  return (
    <StudentContext.Provider
      value={{
        students,
        student,
        getStudents,
        getStudent,
        error,
      }}
    >
      {children}
    </StudentContext.Provider>
  );
}
