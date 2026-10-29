import { useEffect, useState } from "react";
import api, { getData, errorMsg } from "../../api";

const emptyForm = { categoryName: "", description: "" };

export default function Categories() {
  const [categories, setCategories] = useState([]);
  const [form, setForm] = useState(emptyForm);

  function load() {
    getData("/category/get").then(setCategories);
  }

  useEffect(load, []);

  function handleChange(e) {
    setForm({ ...form, [e.target.name]: e.target.value });
  }

  async function handleSubmit(e) {
    e.preventDefault();
    try {
      await api.post("/category/add", form);
      setForm(emptyForm);
      load();
    } catch (err) {
      alert(errorMsg(err));
    }
  }

  async function remove(id) {
    if (!confirm("Delete this category?")) return;
    try {
      await api.delete(`/category/delete/${id}`);
      load();
    } catch (err) {
      alert(errorMsg(err));
    }
  }

  return (
    <div className="mx-auto max-w-6xl px-4 py-8">
      <h1 className="page-title">Policy categories</h1>

      <div className="grid gap-6 lg:grid-cols-3">
        <form onSubmit={handleSubmit} className="card space-y-4 h-fit">
          <h2 className="text-lg font-semibold text-slate-800">Add category</h2>
          <div>
            <label className="label">Name</label>
            <input className="input" name="categoryName" value={form.categoryName} onChange={handleChange} required />
          </div>
          <div>
            <label className="label">Description</label>
            <textarea className="input" rows="3" name="description" value={form.description} onChange={handleChange} />
          </div>
          <button className="btn w-full">Add category</button>
        </form>

        <div className="table-wrap lg:col-span-2 h-fit">
          <table className="w-full">
            <thead>
              <tr>
                <th className="th">ID</th>
                <th className="th">Name</th>
                <th className="th">Description</th>
                <th className="th"></th>
              </tr>
            </thead>
            <tbody>
              {categories.map((c) => (
                <tr key={c.categoryId}>
                  <td className="td">{c.categoryId}</td>
                  <td className="td font-medium">{c.categoryName}</td>
                  <td className="td">{c.description}</td>
                  <td className="td text-right">
                    <button onClick={() => remove(c.categoryId)} className="btn-danger">Delete</button>
                  </td>
                </tr>
              ))}
              {categories.length === 0 && (
                <tr><td className="td text-center text-slate-400" colSpan="4">No categories yet.</td></tr>
              )}
            </tbody>
          </table>
        </div>
      </div>
    </div>
  );
}
