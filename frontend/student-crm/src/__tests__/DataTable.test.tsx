import { Student } from "@/app/types/Student.type";
import { fireEvent, render, screen, waitFor } from "@testing-library/react";
import DataTable, {
  StyledTableCell,
  StyledTableRow,
} from "@/app/components/common/DataTable";
import { Course } from "@/app/types/Course.type";
import React from "react";

// Mock Searchbar component
jest.mock("@/app/components/common/Searchbar", () => ({
  __esModule: true,
  default: ({
    handleSearch,
    name,
  }: {
    handleSearch: (event: React.ChangeEvent<HTMLInputElement>) => void;
    name: string;
  }) => (
    <input
      type="search"
      placeholder={`search ${name}`}
      onChange={handleSearch}
    />
  ),
}));

describe("DataTable", () => {
  const mockStudentData: Array<Student> = Array.of(
    {
      id: 1,
      firstName: "John",
      lastName: "test",
      email: "test@test.com",
      courses: [],
    },
    {
      id: 2,
      firstName: "Doe",
      lastName: "test2",
      email: "test2@test.com",
      courses: [],
    },
    {
      id: 3,
      firstName: "test",
      lastName: "test",
      email: "test@test.com",
      courses: [],
    },
    {
      id: 4,
      firstName: "Yellow",
      lastName: "test2",
      email: "test2@test.com",
      courses: [],
    },
    {
      id: 5,
      firstName: "Black",
      lastName: "test",
      email: "test@test.com",
      courses: [],
    },
    {
      id: 6,
      firstName: "Orange",
      lastName: "test2",
      email: "test2@test.com",
      courses: [],
    },
  );

  const mockColumns = ["ID", "First name", "Last name", "Email", "Courses"];
  const mockSetRow = (item: Student | Course) => (
    <StyledTableRow key={item.id}>
      <StyledTableCell>
        {(item as Student).firstName || (item as Course).courseName}
      </StyledTableCell>
    </StyledTableRow>
  );

  test("renders DataTable correctly and shows student data correctly", () => {
    render(
      <DataTable
        columns={mockColumns}
        data={mockStudentData}
        setRow={mockSetRow}
        name={"student"}
      />,
    );

    expect(screen.getByRole("table")).toBeInTheDocument();

    mockColumns.map((value) =>
      expect(screen.getByText(value)).toBeInTheDocument(),
    );

    mockStudentData.map((student) => {
      if (student.firstName === 'Orange')
        return
      return expect(screen.getByText(student.firstName)).toBeInTheDocument()
    }
    );

    expect(screen.getByText("Action")).toBeInTheDocument();
  });

  test("Displays no data found when data is empty", () => {
    render(
      <DataTable
        columns={mockColumns}
        data={[]}
        setRow={mockSetRow}
        name={"student"}
      />,
    );

    expect(screen.getByText("No data found.")).toBeInTheDocument();
  });

  test("filters data correctly based on user input", async () => {
    render(
      <DataTable
        columns={mockColumns}
        data={mockStudentData}
        setRow={mockSetRow}
        name={"student"}
      />,
    );

    fireEvent.change(screen.getByPlaceholderText("search student"), {
      target: { value: "John" },
    });
    await waitFor(() => {
      expect(screen.getByText("John")).toBeInTheDocument();
      expect(screen.queryByText("Doe")).not.toBeInTheDocument();
    });
  });

  test('handles pagination correctly', () => {
    render(
      <DataTable
        columns={mockColumns}
        data={mockStudentData}
        setRow={mockSetRow}
        name="student"
      />
    );

    fireEvent.click(screen.getByLabelText('Go to next page'));

    expect(screen.getByText('Orange')).toBeInTheDocument();
    expect(screen.queryByText('Doe')).not.toBeInTheDocument();
  });

  test('changes rows per page correctly', async () => {
    render(
      <DataTable
        columns={mockColumns}
        data={mockStudentData}
        setRow={mockSetRow}
        name="student"
      />
    );

    // Open the rows per page selector
    fireEvent.mouseDown(screen.getByLabelText('Rows per page:'));

    // Select a new rows per page value
    fireEvent.click(screen.getByText('10'));

    await waitFor(() => {
      expect(screen.getAllByRole('row')).toHaveLength(7); // 6 rows of data + 1 header row
    });
  });
});
