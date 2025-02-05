import React from "react";
import { useSnackbar } from "@/app/hooks/useSnackbar";
import fetchMock from "jest-fetch-mock";
import { fireEvent, render, waitFor, screen } from "@testing-library/react";
import AddButton from "@/app/components/common/AddButton";

jest.mock("@/app/hooks/useSnackbar");

const mockShowSnackbar = jest.fn();
const mockSnackbarElement = <div data-testid={"snackbar"}>Mock snackbar</div>;

//mock useSnackbar hook
(useSnackbar as jest.Mock).mockReturnValue({
  showSnackbar: mockShowSnackbar,
  snackbarElement: mockSnackbarElement,
});

describe("AddButton", () => {
  const mockOnUpdate = jest.fn();

  beforeEach(() => {
    jest.clearAllMocks();
    fetchMock.resetMocks();
  });

  test("renders AddButton and handles addStudent method for student", async () => {
    fetchMock.mockResponseOnce(JSON.stringify({}), { status: 204 });

    render(
      <AddButton
        courseId={1}
        studentId={1}
        method={"addStudent"}
        onUpdate={mockOnUpdate}
        name={"Student"}
      />,
    );

    fireEvent.click(screen.getByText("ADD Student"));

    await waitFor(() => {
      expect(fetchMock).toHaveBeenCalledWith(
        "http://localhost:8081/student-crm/api/courses/addStudent?courseId=1&studentId=1",
        { method: "PUT" },
      );
    });

    expect(mockShowSnackbar).toHaveBeenCalledWith("Student added!");
    expect(mockOnUpdate).toHaveBeenCalled();
    expect(screen.getByTestId("snackbar")).toBeInTheDocument();
  });

  test("renders AddButton and handles removeStudent method for student", async () => {
    fetchMock.mockResponseOnce(JSON.stringify({}), { status: 204 });

    render(
      <AddButton
        courseId={1}
        studentId={1}
        method={"removeStudent"}
        onUpdate={mockOnUpdate}
        name={"Student"}
      />,
    );

    fireEvent.click(screen.getByText("Remove Student"));

    await waitFor(() => {
      expect(fetchMock).toHaveBeenCalledWith(
        "http://localhost:8081/student-crm/api/courses/removeStudent?courseId=1&studentId=1",
        { method: "PUT" },
      );
    });

    expect(mockShowSnackbar).toHaveBeenCalledWith("Student removed!");
    expect(mockOnUpdate).toHaveBeenCalled();
    expect(screen.getByTestId("snackbar")).toBeInTheDocument();
  });

  test("shows error snackbar on API failure", async () => {
    fetchMock.mockResponseOnce(JSON.stringify({}), { status: 500 });

    render(
      <AddButton
        courseId={1}
        studentId={1}
        method={"removeStudent"}
        onUpdate={mockOnUpdate}
        name={"Student"}
      />,
    );

    fireEvent.click(screen.getByText("Remove Student"));

    await waitFor(() => {
      expect(fetchMock).toHaveBeenCalledWith(
        "http://localhost:8081/student-crm/api/courses/removeStudent?courseId=1&studentId=1",
        { method: "PUT" },
      );
    });

    expect(mockShowSnackbar).toHaveBeenCalledWith("Error 500");
    expect(screen.getByTestId("snackbar")).toBeInTheDocument();
  });

  test("renders AddButton for course", async () => {
    fetchMock.mockResponseOnce(JSON.stringify({}), { status: 204 });

    render(
      <AddButton
        courseId={1}
        studentId={1}
        method={"addStudent"}
        onUpdate={mockOnUpdate}
        name={"Course"}
      />,
    );

    fireEvent.click(screen.getByText("ADD Course"));

    await waitFor(() => {
      expect(fetchMock).toHaveBeenCalledWith(
        "http://localhost:8081/student-crm/api/courses/addStudent?courseId=1&studentId=1",
        { method: "PUT" },
      );
    });

    expect(mockShowSnackbar).toHaveBeenCalledWith("Course added!");
    expect(mockOnUpdate).toHaveBeenCalled();
    expect(screen.getByTestId("snackbar")).toBeInTheDocument();
  });
});
