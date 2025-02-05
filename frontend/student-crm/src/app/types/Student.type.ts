import * as Yup from "yup";

export type Student = {
  id: number;
  firstName: string;
  lastName: string;
  email: string;
  courses: CourseStudentResponse[];
};

type CourseStudentResponse = {
  id: number;
  courseName: string;
};

export const isStudent = (entity: unknown): entity is Student => {
  return (entity as Student).firstName !== undefined;
};

export const studentValidationSchema = Yup.object().shape({
  firstName: Yup.string()
    .required("First name is required")
    .min(3, "First name should have at least 3 characters")
    .max(35, "First name should have at max 35 characters")
    .trim()
    .test(
      "notBlank",
      "First name should not be blank",
      (value) => value.trim() !== "",
    ),
  lastName: Yup.string()
    .required("Last name is required")
    .min(3, "Last name should have at least 3 characters")
    .max(35, "Last name should have at max 35 characters")
    .trim()
    .test(
      "notBlank",
      "Last name should not be blank",
      (value) => value.trim() !== "",
    ),
  email: Yup.string()
    .email("Invalid email address")
    .required("Email is required"),
});
