import { Link, NavLink } from "react-router-dom";
import ThemeToggle from "./ThemeToggle";
import { Button } from "./ui/button";
import { Separator } from "./ui/separator";
import { Sheet, SheetClose, SheetContent, SheetHeader, SheetTitle, SheetTrigger } from "./ui/sheet";
import { ChevronDown, LogOut, Menu, Package } from "lucide-react";
import { memo } from "react";
import useAuth from "@/hooks/useAuth";
import useLogout from "@/hooks/useLogout";
import {
  DropdownMenu,
  DropdownMenuContent,
  DropdownMenuItem,
  DropdownMenuLabel,
  DropdownMenuSeparator,
  DropdownMenuTrigger,
} from "./ui/dropdown-menu";

const Navbar = () => {
  const { auth } = useAuth();
  const logout = useLogout();

  const Links = auth ? AuthNavbar.links : NoAuthNavbar.links;
  const SheetLinks = auth ? AuthNavbar.sheetLinks : NoAuthNavbar.sheetLinks;

  return (
    <nav className="flex justify-center items-center w-full border-b border-accent py-4 min-w-[350px] min-h-[60px]">
      <div className="flex justify-between items-center flex-grow px-6 md:max-w-[75%]">
        <div className="flex items-center gap-3">
          <Package />
          <h1 className="text-2xl font-bold">Course Archive</h1>
        </div>
        <div className="hidden md:flex items-center">
          <ul className="flex items-center gap-1 lg:gap-5 h-10">
            <Links logout={logout} />
            <Separator orientation="vertical" />
            <li>
              <ThemeToggle />
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
              <SheetLinks logout={logout} />
              <ThemeToggle />
            </div>
          </SheetContent>
        </Sheet>
      </div>
    </nav>
  );
};

const NoAuthNavbar = {
  sheetLinks: memo(() => (
    <>
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
    </>
  )),
  links: memo(() => (
    <>
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
    </>
  )),
};

const AuthNavbar = {
  sheetLinks: memo(({ logout }: { logout: () => void }) => (
    <>
      <NavLink
        to="/"
        className={({ isActive }) => (isActive ? "text-primary font-semibold" : undefined)}
      >
        <SheetClose>Home</SheetClose>
      </NavLink>
      <NavLink
        to="/register"
        className={({ isActive }) => (isActive ? "text-primary font-semibold" : undefined)}
      >
        <SheetClose>Account</SheetClose>
      </NavLink>
      <span onClick={logout}>
        <SheetClose>Logout</SheetClose>
      </span>
    </>
  )),
  links: memo(({ logout }: { logout: () => void }) => (
    <>
      <li>
        <NavLink to="/" className={({ isActive }) => (isActive ? "text-primary" : undefined)}>
          <Button variant="ghost" className="font-semibold">
            Home
          </Button>
        </NavLink>
      </li>
      <li>
        <NavLink
          to="/courses"
          className={({ isActive }) => (isActive ? "text-primary" : undefined)}
        >
          <Button variant="ghost" className="font-semibold">
            Courses
          </Button>
        </NavLink>
      </li>
      <li>
        <DropdownMenu>
          <DropdownMenuTrigger asChild>
            <Button variant="ghost" className="flex items-center font-semibold">
              <span>Account</span>
              <ChevronDown className="h-3 w-3 ml-1 mt-1" />
            </Button>
          </DropdownMenuTrigger>
          <DropdownMenuContent className="w-30">
            <DropdownMenuLabel>My Account</DropdownMenuLabel>
            <DropdownMenuSeparator />
            <DropdownMenuItem onClick={logout} className="cursor-pointer">
              <LogOut className="mr-2 h-4 w-4" />
              <Button variant="ghost" className="p-0 m-0 h-auto">
                Log out
              </Button>
            </DropdownMenuItem>
          </DropdownMenuContent>
        </DropdownMenu>
      </li>
    </>
  )),
};

export default Navbar;
