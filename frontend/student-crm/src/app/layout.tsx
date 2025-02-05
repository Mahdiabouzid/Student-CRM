import type { Metadata } from "next";
import { Inter } from "next/font/google";
import "./globals.css";
import React from "react";
import Navigation from "@/app/components/common/Navigation";
import StudentProvider from "@/app/utils/StudentProvider";
import CourseProvider from "@/app/utils/CourseProvider";

const inter = Inter({ subsets: ["latin"] });

export const metadata: Metadata = {
  title: "Student CRM",
};

export default function RootLayout({
  children,
}: Readonly<{
  children: React.ReactNode;
}>) {
  return (
    <html lang="en">
      <body className={inter.className}>
        <Navigation />
        <StudentProvider>
          <CourseProvider>
            <main className={"flex justify-center min-h-screen items-center"}>
              {children}
            </main>
          </CourseProvider>
        </StudentProvider>
      </body>
    </html>
  );
}
