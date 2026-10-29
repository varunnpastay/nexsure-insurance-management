import axios from "axios";

// Spring Boot backend
const api = axios.create({
  baseURL: "http://localhost:8080",
});

// every backend reply is ApiResponse { message, data, httpStatus } -> return just "data"
export async function getData(url) {
  const res = await api.get(url);
  return res.data.data;
}

// error message sent by GlobalExceptionHandler (ApiResponse.message)
export function errorMsg(err) {
  return err.response?.data?.message || "Something went wrong. Is the backend running?";
}

// customer portal session
export function getUser() {
  const data = localStorage.getItem("customer");
  return data ? JSON.parse(data) : null;
}

export function saveUser(user) {
  localStorage.setItem("customer", JSON.stringify(user));
}

export function logout() {
  localStorage.removeItem("customer");
}

// admin portal session (stored separately, so both portals can be open at once)
export function getAdmin() {
  const data = localStorage.getItem("admin");
  return data ? JSON.parse(data) : null;
}

export function saveAdmin(admin) {
  localStorage.setItem("admin", JSON.stringify(admin));
}

export function adminLogout() {
  localStorage.removeItem("admin");
}

export function money(value) {
  return "₹" + Number(value || 0).toLocaleString("en-IN");
}

export default api;
