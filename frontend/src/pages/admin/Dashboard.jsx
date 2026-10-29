import { useEffect, useState } from "react";
import { Link } from "react-router-dom";
import { getData, getAdmin } from "../../api";

const cards = [
  { key: "categories", label: "Categories", to: "/admin/categories" },
  { key: "policies", label: "Policies", to: "/admin/policies" },
  { key: "purchases", label: "Purchases", to: "/admin/purchases" },
  { key: "claims", label: "Claims", to: "/admin/claims" },
  { key: "pendingClaims", label: "Pending claims", to: "/admin/claims" },
  { key: "users", label: "Users", to: "/admin/users" },
];

export default function Dashboard() {
  const user = getAdmin();
  const [stats, setStats] = useState({});

  useEffect(() => {
    getData("/admin/stats").then(setStats);
  }, []);

  return (
    <div className="mx-auto max-w-6xl px-4 py-8">
      <h1 className="text-2xl font-semibold text-slate-800">Admin dashboard</h1>
      <p className="mb-8 text-slate-500">Welcome, {user.fullName}.</p>

      <div className="grid gap-4 sm:grid-cols-2 lg:grid-cols-3">
        {cards.map((c) => (
          <Link key={c.key} to={c.to} className="card hover:border-indigo-300 hover:shadow-md transition">
            <p className="text-sm text-slate-500">{c.label}</p>
            <p className="mt-1 text-3xl font-bold text-indigo-600">{stats[c.key] ?? "-"}</p>
          </Link>
        ))}
      </div>
    </div>
  );
}
