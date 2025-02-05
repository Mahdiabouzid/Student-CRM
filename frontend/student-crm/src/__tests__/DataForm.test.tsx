import { fireEvent, render, screen } from "@testing-library/react";
import DataForm from "@/app/components/common/DataForm";

describe("DataForm", () => {
  const mockHandleSubmit = jest.fn();

  test("renders the form correctly", () => {
    render(
      <DataForm handleSubmit={mockHandleSubmit}>
        <input data-testid={"mock-input"} />
      </DataForm>,
    );

    const inputElement = screen.getByTestId("mock-input");
    const formElement = screen.getByRole("form");

    expect(formElement).toBeInTheDocument();
    expect(inputElement).toBeInTheDocument();
  });

  test("calls handle submit when the form is submitted", () => {
    render(
      <DataForm handleSubmit={mockHandleSubmit}>
        <input data-testid={"mock-input"} />
        <button type={"submit"} data-testid={"submit-button"}>
          Submit
        </button>
      </DataForm>,
    );

    const formElement = screen.getByRole("form");

    fireEvent.submit(formElement);

    expect(mockHandleSubmit).toHaveBeenCalled();
  });
});
