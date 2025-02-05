"use client";

import { useRouter } from "next/navigation";
import React, { useState } from "react";
import { CourseRequest } from "@/app/types/CourseRequest";
import { Course, courseValidationSchema } from "@/app/types/Course.type";
import { TextField } from "@mui/material";
import DataForm from "@/app/components/common/DataForm";
import * as Yup from "yup";
import SubmitButton from "@/app/components/common/SubmitButton";

interface CourseFormProps {
  courseToBeUpdated?: Course;
  handleCloseModal?: () => void;
}

interface FormErrors {
  courseName: string;
  serverError: string;
}

export default function CourseForm({
  courseToBeUpdated,
  handleCloseModal,
}: CourseFormProps) {
  const router = useRouter();
  const [course, setCourse] = useState<CourseRequest>(
    courseToBeUpdated
      ? {
          courseName: courseToBeUpdated.courseName,
        }
      : { courseName: "" },
  );
  const [errors, setErrors] = useState<FormErrors>({
    courseName: "",
    serverError: "",
  });

  const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const { name, value } = e.target;
    setErrors({ ...errors, [name]: false });
    setCourse({ ...course, [name]: value });
  };

  const handleSubmit = async (event: React.FormEvent) => {
    event.preventDefault();

    try {
      await courseValidationSchema.validate(course, { abortEarly: false });

      const method = courseToBeUpdated ? "PUT" : "POST";
      const path = courseToBeUpdated
        ? `http://localhost:8081/student-crm/api/courses/edit/${courseToBeUpdated.id}`
        : "http://localhost:8081/student-crm/api/courses/create";

      const response = await fetch(path, {
        method: method,
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify(course),
      });

      if (response.status === 409) {
        setErrors({
          ...errors,
          serverError: `Course ${course.courseName} already exists !`,
        });
      } else if (!response.ok) {
        setErrors({ ...errors, serverError: "Server error" });
      } else {
        if (handleCloseModal) {
          handleCloseModal();
        }
        router.push("/courses");
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
            error={!!errors.courseName}
            name="courseName"
            value={course.courseName}
            id="courseName"
            label="Course name"
            onChange={handleChange}
            helperText={errors.courseName}
          />
        </div>
        <SubmitButton>
          {courseToBeUpdated ? "Update" : "Create"} Course
        </SubmitButton>
      </DataForm>
    </div>
  );
}
