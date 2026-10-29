import { NavLink, Outlet, useNavigate } from "react-router-dom";
import { getAdmin, adminLogout } from "../api";
import Logo from "./Logo";

const links = [
  { to: "/admin", label: "Dashboard" },
  { to: "/admin/categories", label: "Categories" },
  { to: "/admin/policies", label: "Policies" },
  { to: "/admin/purchases", label: "Purchases" },
  { to: "/admin/claims", label: "Claims" },
  { to: "/admin/users", label: "Customers" },
];

// admin portal layout - dark sidebar + content area
export default function AdminLayout() {
  const navigate = useNavigate();
  const admin = getAdmin();

  function handleLogout() {
    adminLogout();
    navigate("/admin/login");
  }

  const linkClass = ({ isActive }) =>
    `block whitespace-nowrap rounded-lg px-3 py-2 text-sm font-medium ${
      isActive ? "bg-indigo-600 text-white" : "text-slate-300 hover:bg-slate-800 hover:text-white"
    }`;

  return (
    <div className="min-h-screen bg-slate-100 md:flex">
      {/* sidebar (top bar on mobile) */}
      <aside className="bg-slate-900 md:sticky md:top-0 md:flex md:h-screen md:w-60 md:flex-col">
        <div className="flex items-center justify-between px-5 py-4 md:py-6">
          <div className="flex items-center gap-2 text-lg font-bold text-white">
            <Logo className="h-6 w-6 text-indigo-400" /> NexSure
            <span className="rounded bg-indigo-600 px-1.5 py-0.5 text-[10px] font-semibold uppercase tracking-wide">Admin</span>
          </div>
          <button onClick={handleLogout} className="text-sm text-slate-300 hover:text-white md:hidden">Logout</button>
        </div>

        <nav className="flex gap-1 overflow-x-auto px-3 pb-3 md:flex-1 md:flex-col md:overflow-visible">
          {links.map((l) => (
            <NavLink key={l.to} to={l.to} end className={linkClass}>{l.label}</NavLink>
          ))}
        </nav>

        <div className="hidden border-t border-slate-800 p-4 md:block">
          <p className="text-sm font-medium text-white">{admin?.fullName}</p>
          <p className="mb-3 truncate text-xs text-slate-400">{admin?.email}</p>
          <button onClick={handleLogout} className="w-full rounded-lg border border-slate-700 py-2 text-sm text-slate-300 hover:bg-slate-800 cursor-pointer">
            Logout
          </button>
        </div>
      </aside>

      {/* page content */}
      <main className="flex-1">
        <Outlet />
      </main>
    </div>
  );
}
