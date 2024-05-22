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
import App from "./App.tsx";
import RootLayout from "./layouts/RootLayout.tsx";
import CollegeListingPage from "./pages/college/CollegeListingPage.tsx";
import SingleCollegePage from "./pages/college/SingleCollegePage.tsx";
import ProgramListingPage from "./pages/program/ProgramListingPage.tsx";

const router = createBrowserRouter([
  {
    path: "/",
    element: <ProtectedRoutes layout={<RootLayout />} />,
    children: [
      {
        index: true,
        element: <App />,
        errorElement: <ErrorPage />,
      },
      {
        path: "/college",
        element: <CollegeListingPage />,
        errorElement: <ErrorPage />,
      },
      {
        path: "/college/:collegeId",
        element: <SingleCollegePage />,
        errorElement: <ErrorPage />,
      },
      {
        path: "/program",
        element: <ProgramListingPage />,
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

const queryClient = new QueryClient();

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
