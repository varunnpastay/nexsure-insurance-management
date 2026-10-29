import { useState } from "react";
import { Link, useNavigate, useLocation } from "react-router-dom";
import api, { saveUser, errorMsg } from "../api";

export default function Login() {
  const navigate = useNavigate();
  const location = useLocation();
  const [form, setForm] = useState({ email: "", password: "" });
  const [error, setError] = useState("");

  function handleChange(e) {
    setForm({ ...form, [e.target.name]: e.target.value });
  }

  async function handleSubmit(e) {
    e.preventDefault();
    setError("");
    try {
      const res = await api.post("/user/login", form);
      const user = res.data.data;
      saveUser(user);
      navigate("/policies");
    } catch (err) {
      setError(errorMsg(err));
    }
  }

  return (
    <div className="mx-auto max-w-md px-4 py-16">
      <form onSubmit={handleSubmit} className="card space-y-4">
        <h1 className="text-2xl font-semibold text-slate-800">Customer Login</h1>

        {location.state?.success && (
          <p className="rounded-lg bg-green-50 px-3 py-2 text-sm text-green-700">{location.state.success}</p>
        )}
        {error && <p className="rounded-lg bg-red-50 px-3 py-2 text-sm text-red-600">{error}</p>}

        <div>
          <label className="label">Email</label>
          <input className="input" type="email" name="email" value={form.email} onChange={handleChange} required />
        </div>
        <div>
          <label className="label">Password</label>
          <input className="input" type="password" name="password" value={form.password} onChange={handleChange} required />
        </div>

        <button className="btn w-full">Login</button>
        <p className="text-center text-sm text-slate-500">
          New here? <Link to="/register" className="text-indigo-600 hover:underline">Create an account</Link>
        </p>
        <p className="border-t border-slate-100 pt-3 text-center text-xs text-slate-400">
          Staff? <Link to="/admin/login" className="hover:text-indigo-600 hover:underline">Go to Admin Portal</Link>
        </p>
      </form>
    </div>
  );
}
