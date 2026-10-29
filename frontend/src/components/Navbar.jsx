import { useState } from "react";
import { Link, NavLink, useNavigate, useLocation } from "react-router-dom";
import { getUser, logout } from "../api";
import Logo from "./Logo";

const links = [
  { to: "/policies", label: "Browse policies" },
  { to: "/my-purchases", label: "My purchases" },
  { to: "/my-claims", label: "My claims" },
];

// top navbar - customer portal only
export default function Navbar() {
  useLocation(); // re-render on page change so login/logout updates the bar
  const navigate = useNavigate();
  const [open, setOpen] = useState(false);
  const user = getUser();

  function handleLogout() {
    logout();
    navigate("/login");
  }

  const linkClass = ({ isActive }) =>
    `rounded-md px-3 py-2 text-sm font-medium ${isActive ? "bg-indigo-50 text-indigo-700" : "text-slate-600 hover:text-indigo-600"}`;

  return (
    <nav className="sticky top-0 z-10 border-b border-slate-200 bg-white">
      <div className="mx-auto flex max-w-6xl items-center justify-between px-4 py-3">
        <Link to="/" className="flex items-center gap-2 text-lg font-bold text-indigo-600">
          <Logo /> NexSure
        </Link>

        {user && (
          <div className="hidden items-center gap-1 md:flex">
            {links.map((l) => (
              <NavLink key={l.to} to={l.to} className={linkClass}>{l.label}</NavLink>
            ))}
          </div>
        )}

        <div className="hidden items-center gap-3 md:flex">
          {user ? (
            <>
              <span className="text-sm text-slate-500">Hi, {user.fullName}</span>
              <button onClick={handleLogout} className="btn-outline">Logout</button>
            </>
          ) : (
            <>
              <Link to="/login" className="btn-outline">Login</Link>
              <Link to="/register" className="btn">Register</Link>
            </>
          )}
        </div>

        <button className="md:hidden text-slate-600 text-2xl" onClick={() => setOpen(!open)}>☰</button>
      </div>

      {open && (
        <div className="flex flex-col gap-1 border-t border-slate-100 px-4 py-3 md:hidden" onClick={() => setOpen(false)}>
          {user && links.map((l) => (
            <NavLink key={l.to} to={l.to} className={linkClass}>{l.label}</NavLink>
          ))}
          {user ? (
            <button onClick={handleLogout} className="btn-outline mt-2">Logout</button>
          ) : (
            <>
              <Link to="/login" className="btn-outline mt-2">Login</Link>
              <Link to="/register" className="btn">Register</Link>
            </>
          )}
        </div>
      )}
    </nav>
  );
}
