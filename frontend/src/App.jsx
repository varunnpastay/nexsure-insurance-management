import { Routes, Route, Outlet } from "react-router-dom";
import Navbar from "./components/Navbar";
import AdminLayout from "./components/AdminLayout";
import { CustomerRoute, AdminRoute } from "./components/ProtectedRoute";

import Home from "./pages/Home";
import Login from "./pages/Login";
import Register from "./pages/Register";
import Policies from "./pages/customer/Policies";
import MyPurchases from "./pages/customer/MyPurchases";
import MyClaims from "./pages/customer/MyClaims";

import AdminLogin from "./pages/admin/AdminLogin";
import Dashboard from "./pages/admin/Dashboard";
import Categories from "./pages/admin/Categories";
import AdminPolicies from "./pages/admin/AdminPolicies";
import Purchases from "./pages/admin/Purchases";
import Claims from "./pages/admin/Claims";
import Users from "./pages/admin/Users";

// customer portal layout - top navbar
function CustomerLayout() {
  return (
    <div className="min-h-screen bg-slate-50">
      <Navbar />
      <Outlet />
    </div>
  );
}

export default function App() {
  return (
    <div className="font-sans">
      <Routes>
        {/* ===== Customer portal ===== */}
        <Route element={<CustomerLayout />}>
          <Route path="/" element={<Home />} />
          <Route path="/login" element={<Login />} />
          <Route path="/register" element={<Register />} />
          <Route path="/policies" element={<CustomerRoute><Policies /></CustomerRoute>} />
          <Route path="/my-purchases" element={<CustomerRoute><MyPurchases /></CustomerRoute>} />
          <Route path="/my-claims" element={<CustomerRoute><MyClaims /></CustomerRoute>} />
        </Route>

        {/* ===== Admin portal ===== */}
        <Route path="/admin/login" element={<AdminLogin />} />
        <Route path="/admin" element={<AdminRoute><AdminLayout /></AdminRoute>}>
          <Route index element={<Dashboard />} />
          <Route path="categories" element={<Categories />} />
          <Route path="policies" element={<AdminPolicies />} />
          <Route path="purchases" element={<Purchases />} />
          <Route path="claims" element={<Claims />} />
          <Route path="users" element={<Users />} />
        </Route>
      </Routes>
    </div>
  );
}
