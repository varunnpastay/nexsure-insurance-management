import { Navigate } from "react-router-dom";
import { getUser, getAdmin } from "../api";

// customer pages - must be logged in on the customer portal
export function CustomerRoute({ children }) {
  return getUser() ? children : <Navigate to="/login" />;
}

// admin pages - must be logged in on the admin portal
export function AdminRoute({ children }) {
  return getAdmin() ? children : <Navigate to="/admin/login" />;
}
