import Link from "next/link";

export default function Home() {
  return (
    <div className="flex min-h-screen flex-col items-center justify-evenly">
      <div className={"flex flex-col items-center"}>
        <h1
          className={
            "mb-4 text-4xl font-extrabold leading-none tracking-tight text-gray-700 md:text-5xl lg:text-6xl my-2 text-center"
          }
        >
          Welcome to your Student CRM
        </h1>
        <p
          className={
            "mb-6 text-lg font-normal text-gray-400 lg:text-xl sm:px-16 xl:px-48 capitalize my-2 text-center md:w-4/5"
          }
        >
          your customer relationship management service to avoid tons of
          paperwork at the university
        </p>
      </div>
      <div
        className={
          "flex flex-col md:flex-row w-full justify-evenly items-center"
        }
      >
        <Link
          href={"/students"}
          className={
            "bg-transparent border border-gray-700 py-5 px-14 rounded-full text-gray-700 text-xl hover:bg-gray-500 hover:border-transparent hover:text-white mb-4 md:mb-0 transition ease-in-out"
          }
        >
          Manage Students
        </Link>
        <Link
          href={"/courses"}
          className={
            "bg-transparent border border-gray-700 py-5 px-14 rounded-full text-gray-700 text-xl hover:bg-gray-500 hover:border-transparent hover:text-white mb-4 md:mb-0 transition ease-in-out"
          }
        >
          Manage Courses
        </Link>
      </div>
    </div>
  );
}
