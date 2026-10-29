import { useEffect, useState } from "react";
import api, { getData, money, errorMsg } from "../../api";
import StatusBadge from "../../components/StatusBadge";

const emptyForm = {
  categoryId: "",
  policyName: "",
  premiumAmount: "",
  coverageAmount: "",
  durationMonths: "",
  description: "",
  status: "ACTIVE",
};

export default function AdminPolicies() {
  const [policies, setPolicies] = useState([]);
  const [categories, setCategories] = useState([]);
  const [form, setForm] = useState(emptyForm);
  const [editId, setEditId] = useState(null); // null = adding, number = editing

  function load() {
    getData("/policy/get").then(setPolicies);
  }

  useEffect(() => {
    load();
    getData("/category/get").then(setCategories);
  }, []);

  function handleChange(e) {
    setForm({ ...form, [e.target.name]: e.target.value });
  }

  function startEdit(p) {
    setEditId(p.policyId);
    setForm({
      categoryId: p.category?.categoryId || "",
      policyName: p.policyName,
      premiumAmount: p.premiumAmount,
      coverageAmount: p.coverageAmount,
      durationMonths: p.durationMonths,
      description: p.description || "",
      status: p.status,
    });
    window.scrollTo({ top: 0, behavior: "smooth" });
  }

  function cancelEdit() {
    setEditId(null);
    setForm(emptyForm);
  }

  async function handleSubmit(e) {
    e.preventDefault();
    // same fields as AddPolicy / UpdatePolicy request classes
    const data = {
      ...form,
      categoryId: Number(form.categoryId),
      premiumAmount: Number(form.premiumAmount),
      coverageAmount: Number(form.coverageAmount),
      durationMonths: Number(form.durationMonths),
    };
    try {
      if (editId) {
        await api.put(`/policy/update/${editId}`, data);
      } else {
        await api.post("/policy/add", data);
      }
      cancelEdit();
      load();
    } catch (err) {
      alert(errorMsg(err));
    }
  }

  async function remove(id) {
    if (!confirm("Delete this policy?")) return;
    try {
      await api.delete(`/policy/delete/${id}`);
      load();
    } catch (err) {
      alert(errorMsg(err));
    }
  }

  return (
    <div className="mx-auto max-w-6xl px-4 py-8">
      <h1 className="page-title">Manage policies</h1>

      <form onSubmit={handleSubmit} className="card mb-8 grid gap-4 md:grid-cols-3">
        <h2 className="text-lg font-semibold text-slate-800 md:col-span-3">
          {editId ? "Edit policy" : "Add policy"}
        </h2>
        <div>
          <label className="label">Category</label>
          <select className="input" name="categoryId" value={form.categoryId} onChange={handleChange} required>
            <option value="">Select category</option>
            {categories.map((c) => (
              <option key={c.categoryId} value={c.categoryId}>{c.categoryName}</option>
            ))}
          </select>
        </div>
        <div>
          <label className="label">Policy name</label>
          <input className="input" name="policyName" value={form.policyName} onChange={handleChange} required />
        </div>
        <div>
          <label className="label">Premium amount</label>
          <input className="input" type="number" step="0.01" min="0" name="premiumAmount" value={form.premiumAmount} onChange={handleChange} required />
        </div>
        <div>
          <label className="label">Coverage amount</label>
          <input className="input" type="number" step="0.01" min="0" name="coverageAmount" value={form.coverageAmount} onChange={handleChange} required />
        </div>
        <div>
          <label className="label">Duration (months)</label>
          <input className="input" type="number" min="1" name="durationMonths" value={form.durationMonths} onChange={handleChange} required />
        </div>
        {editId && (
          <div>
            <label className="label">Status</label>
            <select className="input" name="status" value={form.status} onChange={handleChange}>
              <option value="ACTIVE">ACTIVE</option>
              <option value="INACTIVE">INACTIVE</option>
            </select>
          </div>
        )}
        <div className="md:col-span-3">
          <label className="label">Description</label>
          <textarea className="input" rows="2" name="description" value={form.description} onChange={handleChange} />
        </div>
        <div className="flex gap-3 md:col-span-3">
          <button className="btn">{editId ? "Update policy" : "Add policy"}</button>
          {editId && <button type="button" onClick={cancelEdit} className="btn-outline">Cancel</button>}
        </div>
      </form>

      <div className="table-wrap">
        <table className="w-full">
          <thead>
            <tr>
              <th className="th">Name</th>
              <th className="th">Category</th>
              <th className="th">Premium</th>
              <th className="th">Coverage</th>
              <th className="th">Months</th>
              <th className="th">Status</th>
              <th className="th"></th>
            </tr>
          </thead>
          <tbody>
            {policies.map((p) => (
              <tr key={p.policyId}>
                <td className="td font-medium">{p.policyName}</td>
                <td className="td">{p.category?.categoryName}</td>
                <td className="td">{money(p.premiumAmount)}</td>
                <td className="td">{money(p.coverageAmount)}</td>
                <td className="td">{p.durationMonths}</td>
                <td className="td"><StatusBadge status={p.status} /></td>
                <td className="td whitespace-nowrap text-right space-x-2">
                  <button onClick={() => startEdit(p)} className="btn-outline !py-1.5 !px-3">Edit</button>
                  <button onClick={() => remove(p.policyId)} className="btn-danger">Delete</button>
                </td>
              </tr>
            ))}
            {policies.length === 0 && (
              <tr><td className="td text-center text-slate-400" colSpan="7">No policies yet.</td></tr>
            )}
          </tbody>
        </table>
      </div>
    </div>
  );
}
