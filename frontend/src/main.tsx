import React from "react";
import ReactDOM from "react-dom/client";
import { createBrowserRouter, RouterProvider } from "react-router-dom";
import { QueryClient, QueryClientProvider } from "@tanstack/react-query";
import { ReactQueryDevtools } from "@tanstack/react-query-devtools";

import "./index.css";

import { ThemeProvider } from "./context/ThemeProvider.tsx";
import { AuthProvider } from "./context/AuthProvider.tsx";

import ProtectedRoutes from "./layouts/ProtectedRoutes.tsx";
import ErrorPage from "./pages/ErrorPage.tsx";
import RootLayout from "./layouts/RootLayout.tsx";
import CollegeListingPage from "./pages/college/CollegeListingPage.tsx";
import ProgramListingPage from "./pages/program/ProgramListingPage.tsx";
import HomeLayout from "@/layouts/HomeLayout.tsx";
import { ApiError } from "@/api/config/ApiError.ts";

const router = createBrowserRouter([
  {
    element: <ProtectedRoutes layout={<RootLayout />} />,
    children: [
      {
        element: <HomeLayout />,
        children: [
          {
            path: "/college",
            element: <CollegeListingPage />,
            errorElement: <ErrorPage />,
          },
          {
            path: "/college/:collegeId",
            async lazy() {
              const component = await import("./pages/college/CollegePage.tsx");
              return { Component: component.default };
            },
            errorElement: <ErrorPage />,
          },
          {
            path: "/program",
            element: <ProgramListingPage />,
            errorElement: <ErrorPage />,
          },
          {
            path: "/course",
            async lazy() {
              const component = await import("./pages/course/CourseListingPage.tsx");
              return { Component: component.default };
            },
            errorElement: <ErrorPage />,
          },
          {
            path: "/course/:courseId",
            async lazy() {
              const component = await import("./layouts/CourseTitleLayout.tsx");
              return { Component: component.default };
            },
            errorElement: <ErrorPage />,
            children: [
              {
                index: true,
                async lazy() {
                  const component = await import("./pages/course/CoursePage.tsx");
                  return { Component: component.default };
                },
                errorElement: <ErrorPage />,
              },
              {
                path: "course-year/:courseYearId",
                async lazy() {
                  const component = await import("./pages/course/CourseYearPage.tsx");
                  return { Component: component.default };
                },
                errorElement: <ErrorPage />,
              },
            ],
          },
        ],
      },
      {
        index: true,
        element: <div>Home</div>,
        errorElement: <ErrorPage />,
      },
      {
        path: "/admin",
        async lazy() {
          const layout = await import("./layouts/AdminLayout.tsx");
          return { Component: layout.default };
        },
        errorElement: <ErrorPage />,
        children: [
          {
            index: true,
            element: <div>Admin Home</div>,
            errorElement: <ErrorPage />,
          },
          {
            path: "college",
            async lazy() {
              const component = await import("./pages/college/AdminCollegeListingPage.tsx");
              return { Component: component.default };
            },
            errorElement: <ErrorPage />,
          },
          {
            path: "program",
            async lazy() {
              const component = await import("./pages/program/AdminProgramListingPage.tsx");
              return { Component: component.default };
            },
            errorElement: <ErrorPage />,
          },
          {
            path: "course",
            async lazy() {
              const component = await import("./pages/course/AdminCourseListingPage.tsx");
              return { Component: component.default };
            },
            errorElement: <ErrorPage />,
          },
          {
            path: "user/:userId",
            async lazy() {
              const component = await import("./layouts/AdminUserLayout.tsx");
              return { Component: component.default };
            },
            errorElement: <ErrorPage />,
            children: [
              {
                index: true,
                element: <div>Admin User Home</div>,
                errorElement: <ErrorPage />,
              },
              {
                path: "college",
                async lazy() {
                  const component = await import("./pages/user/AdminUserCollegePage.tsx");
                  return { Component: component.default };
                },
                errorElement: <ErrorPage />,
              },
              {
                path: "program",
                async lazy() {
                  const component = await import("./pages/user/AdminUserProgramPage.tsx");
                  return { Component: component.default };
                },
                errorElement: <ErrorPage />,
              },
              {
                path: "course",
                async lazy() {
                  const component = await import("./pages/user/AdminUserCoursePage.tsx");
                  return { Component: component.default };
                },
                errorElement: <ErrorPage />,
              },
            ],
          },
          {
            path: "course/:courseId",
            async lazy() {
              const component = await import("./layouts/CourseTitleLayout.tsx");
              return { Component: component.default };
            },
            errorElement: <ErrorPage />,
            children: [
              {
                index: true,
                async lazy() {
                  const component = await import("./pages/course/AdminCoursePage.tsx");
                  return { Component: component.default };
                },
                errorElement: <ErrorPage />,
              },
              {
                path: "course-year/:courseYearId",
                async lazy() {
                  const component = await import("./pages/course/AdminCourseYearPage.tsx");
                  return { Component: component.default };
                },
                errorElement: <ErrorPage />,
              },
            ],
          },
          {
            path: "user",
            async lazy() {
              const component = await import("./pages/user/AdminUserListingPage.tsx");
              return { Component: component.default };
            },
            errorElement: <ErrorPage />,
          },
        ],
      },
    ],
  },
  {
    element: <RootLayout />,
    children: [
      {
        path: "/login",
        async lazy() {
          const component = await import("./pages/LoginPage.tsx");
          return { Component: component.default };
        },
        errorElement: <ErrorPage />,
      },
      {
        path: "/register",
        async lazy() {
          const component = await import("./pages/RegisterPage.tsx");
          return { Component: component.default };
        },
        errorElement: <ErrorPage />,
      },
    ],
  },
]);

const queryClient = new QueryClient({
  defaultOptions: {
    queries: {
      retry: (failureCount, error) => {
        if (error instanceof ApiError && error.getStatusCode() === 403) {
          return false;
        }
        return failureCount < 3;
      },
      throwOnError: true,
    },
  },
});

ReactDOM.createRoot(document.getElementById("root")!).render(
  <React.StrictMode>
    <QueryClientProvider client={queryClient}>
      <AuthProvider>
        <ThemeProvider defaultTheme="dark" storageKey="vite-ui-theme">
          <RouterProvider router={router} />
        </ThemeProvider>
      </AuthProvider>
      <ReactQueryDevtools initialIsOpen={false} />
    </QueryClientProvider>
  </React.StrictMode>,
);
