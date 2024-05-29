import Navbar from "@/components/Navbar";
import { Outlet } from "react-router-dom";
import { Toaster } from "@/components/ui/sonner.tsx";

const RootLayout = () => {
	return (
		<>
			<Navbar />
			<Outlet />
			<Toaster richColors position="top-center" closeButton />
		</>
	);
};

export default RootLayout;
