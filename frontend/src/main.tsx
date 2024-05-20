import React from "react";
import ReactDOM from "react-dom/client";
import { createBrowserRouter, RouterProvider } from "react-router-dom";
import { QueryClient, QueryClientProvider } from "@tanstack/react-query";
import { ReactQueryDevtools } from "@tanstack/react-query-devtools";

import "./index.css";

import { ThemeProvider } from "./context/ThemeProvider.tsx";
import { AuthProvider } from "./context/AuthProvider.tsx";

import ProtectedRoutes from "./layouts/ProtectedRoutes.tsx";
import LoginPage from "./pages/LoginPage.tsx";
import RegisterPage from "./pages/RegisterPage.tsx";
import ErrorPage from "./pages/ErrorPage.tsx";
import App from "./App.tsx";
import RootLayout from "./layouts/RootLayout.tsx";
import CollegeListingPage from "./pages/college/CollegeListingPage.tsx";
import SingleCollegePage from "./pages/college/SingleCollegePage.tsx";

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
    ],
  },
  {
    path: "/",
    element: <RootLayout />,
    children: [
      {
        path: "/login",
        element: <LoginPage />,
        errorElement: <ErrorPage />,
      },
      {
        path: "/register",
        element: <RegisterPage />,
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
