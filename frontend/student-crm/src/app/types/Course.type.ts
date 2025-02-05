import { Student } from "@/app/types/Student.type";
import * as Yup from "yup";

export type Course = {
  id: number;
  courseName: string;
  students: Student[];
};

export const isCourse = (entity: unknown): entity is Course => {
  return (entity as Course).courseName !== undefined;
};

export const courseValidationSchema = Yup.object().shape({
  courseName: Yup.string()
    .required("Course name is required")
    .trim()
    .min(3, "Course name should have at least 3 characters")
    .max(35, "Course name should have at max 35 characters")
    .test(
      "notBlank",
      "Course name should not be blank",
      (value) => value.trim() !== "",
    ),
});
