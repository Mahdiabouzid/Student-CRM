"use client";

import { StudentCreationRequest } from "@/app/types/StudentCreationRequest";
import { TextField } from "@mui/material";
import { useRouter } from "next/navigation";
import React, { useState } from "react";
import { Student, studentValidationSchema } from "@/app/types/Student.type";
import DataForm from "@/app/components/common/DataForm";
import * as Yup from "yup";
import SubmitButton from "@/app/components/common/SubmitButton";

interface StudentFormProps {
  toUpdateStudent?: Student;
  handleCloseModal?: () => void;
}

interface FormErrors {
  firstName: string;
  lastName: string;
  email: string;
  serverError: string;
}

export default function StudentForm({
  toUpdateStudent,
  handleCloseModal,
}: StudentFormProps) {
  const router = useRouter();

  const [student, setStudent] = useState<StudentCreationRequest>(
    toUpdateStudent
      ? {
          firstName: toUpdateStudent.firstName,
          lastName: toUpdateStudent.lastName,
          email: toUpdateStudent.email,
        }
      : {
          firstName: "",
          lastName: "",
          email: "",
        },
  );

  const [errors, setErrors] = useState<FormErrors>({
    firstName: "",
    lastName: "",
    email: "",
    serverError: "",
  });

  const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const { name, value } = e.target;
    setStudent({ ...student, [name]: value });
  };

  const handleSubmit = async (event: React.FormEvent) => {
    event.preventDefault();

    try {
      await studentValidationSchema.validate(student, { abortEarly: false });

      const method = toUpdateStudent ? "PUT" : "POST";
      const path = toUpdateStudent
        ? `http://localhost:8081/student-crm/api/students/edit/${toUpdateStudent.id}`
        : "http://localhost:8081/student-crm/api/students/create";

      const response = await fetch(path, {
        method: method,
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify(student),
      });

      if (response.status === 409) {
        setErrors({ ...errors, serverError: "Email is already in use !" });
      } else if (!response.ok) {
        setErrors({ ...errors, serverError: "Server error" });
      } else {
        if (handleCloseModal) {
          handleCloseModal();
        }
        router.push("/students");
      }
    } catch (error) {
      if (error instanceof Yup.ValidationError) {
        const validationErrors: Partial<FormErrors> = {};
        error.inner.forEach((err) => {
          // @ts-ignore
          validationErrors[err.path] = err.message;
        });
        setErrors(validationErrors as FormErrors);
      }
    }
  };

  return (
    <div>
      {errors.serverError !== "" && (
        <p className="text-red-500 text-sm m-2">{errors.serverError}</p>
      )}
      <DataForm handleSubmit={handleSubmit}>
        <div>
          <TextField
            error={!!errors.firstName}
            name="firstName"
            value={student.firstName}
            id="firstName"
            label="First name"
            onChange={handleChange}
            helperText={errors.firstName}
          />
        </div>

        <div>
          <TextField
            error={!!errors.lastName}
            name="lastName"
            id="lastName"
            label="Last name"
            value={student.lastName}
            onChange={handleChange}
            helperText={errors.lastName}
          />
        </div>

        <div>
          <TextField
            error={!!errors.email}
            name="email"
            id="email"
            label={"Email"}
            value={student.email}
            onChange={handleChange}
            helperText={errors.email}
          />
        </div>
        <SubmitButton>
          {toUpdateStudent ? "Update" : "Create"} Student
        </SubmitButton>
      </DataForm>
    </div>
  );
}
