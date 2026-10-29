import { useState } from "react";
import { Link, useNavigate } from "react-router-dom";
import api, { saveAdmin, errorMsg } from "../../api";
import Logo from "../../components/Logo";

// separate login page for the admin portal
export default function AdminLogin() {
  const navigate = useNavigate();
  const [form, setForm] = useState({ email: "", password: "" });
  const [error, setError] = useState("");

  function handleChange(e) {
    setForm({ ...form, [e.target.name]: e.target.value });
  }

  async function handleSubmit(e) {
    e.preventDefault();
    setError("");
    try {
      const res = await api.post("/admin/login", form);
      saveAdmin(res.data.data);
      navigate("/admin");
    } catch (err) {
      setError(errorMsg(err));
    }
  }

  return (
    <div className="flex min-h-screen items-center justify-center bg-slate-900 px-4">
      <form onSubmit={handleSubmit} className="w-full max-w-sm space-y-4 rounded-2xl bg-white p-8 shadow-xl">
        <div className="text-center">
          <Logo className="mx-auto h-10 w-10 text-indigo-600" />
          <h1 className="mt-3 text-2xl font-semibold text-slate-800">Admin Portal</h1>
          <p className="text-sm text-slate-500">NexSure staff login</p>
        </div>

        {error && <p className="rounded-lg bg-red-50 px-3 py-2 text-sm text-red-600">{error}</p>}

        <div>
          <label className="label">Admin email</label>
          <input className="input" type="email" name="email" value={form.email} onChange={handleChange} required />
        </div>
        <div>
          <label className="label">Password</label>
          <input className="input" type="password" name="password" value={form.password} onChange={handleChange} required />
        </div>

        <button className="btn w-full">Login as Admin</button>
        <p className="text-center text-sm text-slate-500">
          Not an admin? <Link to="/login" className="text-indigo-600 hover:underline">Customer login</Link>
        </p>
      </form>
    </div>
  );
}
