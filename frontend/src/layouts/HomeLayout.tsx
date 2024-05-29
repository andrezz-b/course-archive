import { Outlet } from "react-router-dom";

const HomeLayout = () => {
	return (
		<div className="w-full py-4">
			<Outlet />
		</div>
	);
};

export default HomeLayout;
