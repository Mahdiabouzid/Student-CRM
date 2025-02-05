import {
  Paper,
  Table,
  TableBody,
  TableCell,
  tableCellClasses,
  TableContainer,
  TableHead,
  TableRow,
} from "@mui/material";
import TablePagination from "@mui/material/TablePagination";
import React, { useEffect, useState } from "react";
import { styled } from "@mui/system";
import { isStudent, Student } from "@/app/types/Student.type";
import { Course, isCourse } from "@/app/types/Course.type";
import Searchbar from "@/app/components/common/Searchbar";

interface TableProps {
  columns: string[];
  ariaLabel?: string;
  data: Array<Student | Course>;
  setRow: (s: any) => React.ReactNode;
  name: "student" | "course";
  button?: React.ReactNode;
}

export const StyledTableCell = styled(TableCell)(() => ({
  [`&.${tableCellClasses.head}`]: {
    backgroundColor: "rgba(0,0,0,0.8)",
    color: "#FFF",
  },
  [`&.${tableCellClasses.body}`]: {
    fontSize: 14,
  },
}));

export const StyledTableRow = styled(TableRow)(() => ({
  "&:nth-of-type(odd)": {
    backgroundColor: "#F0e0e0",
  },
  // hide last border
  "&:last-child td, &:last-child th": {
    border: 0,
  },
}));

export default function DataTable({
  ariaLabel,
  setRow,
  columns,
  data,
  name,
  button,
}: TableProps) {
  const [page, setPage] = useState(0);
  const [rowsPerPage, setRowsPerPage] = useState(5);
  const [filteredData, setFilteredData] =
    useState<Array<Student | Course>>(data);

  useEffect(() => {
    setFilteredData(data);
  }, [data]);

  const handleChangePage = (
    _event: React.MouseEvent<HTMLButtonElement> | null,
    newPage: number,
  ) => {
    setPage(newPage);
  };

  const handleChangeRowsPerPage = (
    event: React.ChangeEvent<HTMLInputElement | HTMLTextAreaElement>,
  ) => {
    setRowsPerPage(parseInt(event.target.value, 10));
    setPage(0);
  };

  const handleSearch = (event: React.ChangeEvent<HTMLInputElement>) => {
    const value = event.target.value.toLowerCase();
    if (value) {
      setFilteredData(
        data.filter((item) => {
          if (isStudent(item)) {
            return (
              item.firstName.toLowerCase().includes(value) ||
              item.lastName.toLowerCase().includes(value)
            );
          } else if (isCourse(item)) {
            return item.courseName.toLowerCase().includes(value);
          }
        }),
      );
    } else {
      setFilteredData(data);
    }
  };

  return (
    <>
      <div className={"flex justify-between w-full items-center my-8 px-10"}>
        <Searchbar name={name} handleSearch={handleSearch} />
        {button}
      </div>
      {data.length === 0 || filteredData.length === 0 ? (
        <h1 className={"text-xl text-center"}>No data found.</h1>
      ) : (
        <TableContainer component={Paper} className={"my-16"}>
          <Table sx={{ minWidth: 700 }} aria-label={ariaLabel}>
            <TableHead>
              <TableRow>
                {columns.map((column) => (
                  <StyledTableCell align="left" key={column}>
                    {column}
                  </StyledTableCell>
                ))}
                <StyledTableCell align="right">Action</StyledTableCell>
              </TableRow>
            </TableHead>
            <TableBody>
              {filteredData
                .slice(page * rowsPerPage, page * rowsPerPage + rowsPerPage)
                .map((s) => setRow(s))}
            </TableBody>
          </Table>
          <TablePagination
            component="div"
            count={filteredData.length}
            page={page}
            onPageChange={handleChangePage}
            rowsPerPage={rowsPerPage}
            rowsPerPageOptions={[5, 10, 25]}
            onRowsPerPageChange={handleChangeRowsPerPage}
          />
        </TableContainer>
      )}
    </>
  );
}
