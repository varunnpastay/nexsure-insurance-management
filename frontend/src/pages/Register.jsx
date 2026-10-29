import { useState } from "react";
import { Link, useNavigate } from "react-router-dom";
import api, { errorMsg } from "../api";

export default function Register() {
  const navigate = useNavigate();
  const [form, setForm] = useState({ fullName: "", email: "", password: "" });
  const [error, setError] = useState("");

  function handleChange(e) {
    setForm({ ...form, [e.target.name]: e.target.value });
  }

  async function handleSubmit(e) {
    e.preventDefault();
    setError("");
    try {
      const res = await api.post("/user/register", form);
      navigate("/login", { state: { success: res.data.message } });
    } catch (err) {
      setError(errorMsg(err));
    }
  }

  return (
    <div className="mx-auto max-w-md px-4 py-16">
      <form onSubmit={handleSubmit} className="card space-y-4">
        <h1 className="text-2xl font-semibold text-slate-800">Create account</h1>
        {error && <p className="rounded-lg bg-red-50 px-3 py-2 text-sm text-red-600">{error}</p>}

        <div>
          <label className="label">Full name</label>
          <input className="input" name="fullName" value={form.fullName} onChange={handleChange} required />
        </div>
        <div>
          <label className="label">Email</label>
          <input className="input" type="email" name="email" value={form.email} onChange={handleChange} required />
        </div>
        <div>
          <label className="label">Password</label>
          <input className="input" type="password" name="password" value={form.password} onChange={handleChange} required minLength={4} />
        </div>

        <button className="btn w-full">Register</button>
        <p className="text-center text-sm text-slate-500">
          Already have an account? <Link to="/login" className="text-indigo-600 hover:underline">Login</Link>
        </p>
      </form>
    </div>
  );
}
