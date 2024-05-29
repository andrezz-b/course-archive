import Navbar from "@/components/Navbar";
import { Outlet } from "react-router-dom";
import { Toaster } from "@/components/ui/sonner.tsx";

const RootLayout = () => {
	return (
		<>
			<Navbar />
			<main className="container flex flex-col flex-grow">
				<Outlet />
			</main>
			<Toaster richColors position="top-center" closeButton />
		</>
	);
};

export default RootLayout;
