import fetchMock from "jest-fetch-mock";
import { fireEvent, render, screen, waitFor } from "@testing-library/react";
import DeleteButton from "@/app/components/common/DeleteButton";

describe("DeleteButton", () => {
  const mockOnDelete = jest.fn();

  beforeEach(() => {
    jest.clearAllMocks();
    fetchMock.resetMocks();
  });

  test("opens dialog when clicking on the delete icon", () => {
    render(<DeleteButton name={"student"} id={1} onDelete={mockOnDelete} />);

    fireEvent.click(screen.getByLabelText("delete"));

    expect(screen.getByRole("dialog")).toBeInTheDocument();
  });

  test("dialog closes when clicking cancel", async () => {
    render(<DeleteButton name={"student"} id={1} onDelete={mockOnDelete} />);

    fireEvent.click(screen.getByLabelText("delete"));
    fireEvent.click(screen.getByText("Cancel"));

    await waitFor(() => {
      expect(screen.queryByRole("dialog")).not.toBeInTheDocument();
    });
  });

  test("API is called when clicking confirm and dialog closes on success", async () => {
    fetchMock.mockResponseOnce(JSON.stringify({}), { status: 204 });
    render(<DeleteButton name={"student"} id={1} onDelete={mockOnDelete} />);

    fireEvent.click(screen.getByLabelText("delete"));
    fireEvent.click(screen.getByText("Confirm"));

    await waitFor(() => {
      expect(fetchMock).toHaveBeenCalledWith(
        "http://localhost:8081/student-crm/api/students/delete/1",
        { method: "DELETE" },
      );
      expect(screen.queryByRole("dialog")).not.toBeInTheDocument();
    });

    expect(mockOnDelete).toHaveBeenCalled();
  });

  test("API is called when clicking confirm and dialog does not close on error", async () => {
    fetchMock.mockResponseOnce(JSON.stringify({}), { status: 500 });
    render(<DeleteButton name={"student"} id={1} onDelete={mockOnDelete} />);

    fireEvent.click(screen.getByLabelText("delete"));
    fireEvent.click(screen.getByText("Confirm"));

    await waitFor(() => {
      expect(fetchMock).toHaveBeenCalledWith(
        "http://localhost:8081/student-crm/api/students/delete/1",
        { method: "DELETE" },
      );
      expect(screen.queryByRole("dialog")).toBeInTheDocument();
    });

    expect(mockOnDelete).not.toHaveBeenCalled();
  });
});
