import { fireEvent, render, screen, waitFor } from "@testing-library/react";
import EditButton from "@/app/components/common/EditButton";
import { Student } from "@/app/types/Student.type";
import { Course } from "@/app/types/Course.type";

//mock StudentForm component
jest.mock("@/app/components/student/StudentForm", () => ({
  __esModule: true,
  default: ({ handleCloseModal }: { handleCloseModal: () => void }) => {
    return (
      <div>
        <p>Student form</p>
        <button onClick={handleCloseModal}>Submit</button>
      </div>
    );
  },
}));

//mock CourseForm component
jest.mock("@/app/components/course/CourseForm", () => ({
  __esModule: true,
  default: ({ handleCloseModal }: { handleCloseModal: () => void }) => {
    return (
      <div>
        <p>Course form</p>
        <button onClick={handleCloseModal}>Submit</button>
      </div>
    );
  },
}));

describe("EditButton", () => {
  const mockOnUpdate = jest.fn();
  const student: Student = {
    id: 1,
    firstName: "test",
    lastName: "test",
    email: "test@test.com",
    courses: [],
  };

  const course: Course = {
    id: 1,
    courseName: "testCourse",
    students: [],
  };

  afterEach(() => {
    jest.clearAllMocks();
  });

  test("opens modal and renders student form correctly for student entity", () => {
    render(<EditButton entityToBeUpdated={student} onUpdate={mockOnUpdate} />);

    fireEvent.click(screen.getByText("edit"));
    expect(screen.getByText("Update Student")).toBeInTheDocument();
    expect(screen.getByText("Student form")).toBeInTheDocument();
  });

  test("opens modal and renders course form correctly for course entity", () => {
    render(<EditButton entityToBeUpdated={course} onUpdate={mockOnUpdate} />);

    fireEvent.click(screen.getByText("edit"));
    expect(screen.getByText("Update Course")).toBeInTheDocument();
    expect(screen.getByText("Course form")).toBeInTheDocument();
  });

  test("close student form modal when clicking on the update button", async () => {
    render(<EditButton entityToBeUpdated={student} onUpdate={mockOnUpdate} />);

    fireEvent.click(screen.getByText("edit"));

    await waitFor(() => {
      expect(screen.getByText("Update Student")).toBeInTheDocument();
    });

    fireEvent.click(screen.getByText("Submit"));

    await waitFor(() => {
      expect(screen.queryByText("Update Student")).not.toBeInTheDocument();
    });

    expect(mockOnUpdate).toHaveBeenCalled();
  });

  test("close course form modal when clicking on the update button", async () => {
    render(<EditButton entityToBeUpdated={course} onUpdate={mockOnUpdate} />);

    fireEvent.click(screen.getByText("edit"));

    await waitFor(() => {
      expect(screen.getByText("Update Course")).toBeInTheDocument();
    });

    fireEvent.click(screen.getByText("Submit"));

    await waitFor(() => {
      expect(screen.queryByText("Update Course")).not.toBeInTheDocument();
    });

    expect(mockOnUpdate).toHaveBeenCalled();
  });
});
