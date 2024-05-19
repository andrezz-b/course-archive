import { Link, NavLink } from "react-router-dom";
import ThemeToggle from "./ThemeToggle";
import { Button } from "./ui/button";
import { Separator } from "./ui/separator";
import { Sheet, SheetClose, SheetContent, SheetHeader, SheetTitle, SheetTrigger } from "./ui/sheet";
import { Menu, Package } from "lucide-react";

const Navbar = () => {
  return (
    <nav className="flex justify-center items-center w-full border-b border-accent py-4 min-w-[350px] min-h-[60px]">
      <div className="flex justify-between items-center flex-grow px-6 md:max-w-[75%]">
        <div className="flex items-center gap-3">
          <Package />
          <h1 className="text-xl font-bold">Course Archive</h1>
        </div>
        <div className="hidden md:flex items-center">
          <ul className="flex items-center space-x-6 h-10">
            <li>
              <ThemeToggle />
            </li>
            <Separator orientation="vertical" />
            <li>
              <Link to="/login">
                <Button variant="outline">Login</Button>
              </Link>
            </li>
            <li>
              <Link to="/register">
                <Button>Sign Up</Button>
              </Link>
            </li>
          </ul>
        </div>
        <Sheet>
          <SheetTrigger asChild className="block md:hidden">
            <Button variant="outline" className="p-1 border-none h-auto">
              <Menu />
            </Button>
          </SheetTrigger>
          <SheetContent className="w-[55vw]" side="left">
            <SheetHeader className="mb-4">
              <SheetTitle className="text-left">Course Archive</SheetTitle>
            </SheetHeader>
            <div className="flex flex-col gap-4">
              <NavLink
                to="/register"
                className={({ isActive }) => (isActive ? "text-primary font-semibold" : undefined)}
              >
                <SheetClose>Sign Up</SheetClose>
              </NavLink>
              <NavLink
                to="/login"
                className={({ isActive }) => (isActive ? "text-primary font-semibold" : undefined)}
              >
                <SheetClose>Login</SheetClose>
              </NavLink>
              <ThemeToggle />
            </div>
          </SheetContent>
        </Sheet>
      </div>
    </nav>
  );
};

export default Navbar;
