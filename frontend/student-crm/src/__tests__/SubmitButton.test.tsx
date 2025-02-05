import React from "react";
import { render, screen } from "@testing-library/react";
import SubmitButton from "../app/components/common/SubmitButton";

describe("SubmitButton", () => {
  it("renders the button with the correct text", () => {
    render(<SubmitButton>Submit</SubmitButton>);
    const button = screen.getByRole("button", { name: "Submit" });
    expect(button).toBeInTheDocument();
  });

  it("has the correct type attribute", () => {
    render(<SubmitButton>Submit</SubmitButton>);
    const button = screen.getByRole("button", { name: "Submit" });
    expect(button).toHaveAttribute("type", "submit");
  });

  it("applies the correct styles and class names", () => {
    render(<SubmitButton>Submit</SubmitButton>);
    const button = screen.getByRole("button", { name: "Submit" });

    // Check class names
    expect(button).toHaveClass("w-full md:w-1/2 m-5");

    // Check hover styles
    // Note: Testing hover styles in Jest is not straightforward because Jest doesn't run in a browser environment.
    // However, we can still verify that the sx prop contains the correct styles.
    expect(button).toHaveStyle("background-color: rgb(87, 87, 91)");
  });
});
