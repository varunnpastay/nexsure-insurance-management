import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import api, { getData, getUser, money, errorMsg } from "../../api";

export default function Policies() {
  const user = getUser();
  const navigate = useNavigate();
  const [policies, setPolicies] = useState([]);
  const [categories, setCategories] = useState([]);
  const [categoryId, setCategoryId] = useState("");

  useEffect(() => {
    getData("/category/get").then(setCategories);
  }, []);

  useEffect(() => {
    const url = categoryId ? `/policy/active?categoryId=${categoryId}` : "/policy/active";
    getData(url).then(setPolicies);
  }, [categoryId]);

  async function buy(policy) {
    if (!confirm(`Buy "${policy.policyName}" for ${money(policy.premiumAmount)}?`)) return;
    try {
      await api.post("/purchase/buy", { userId: user.userId, policyId: policy.policyId });
      navigate("/my-purchases");
    } catch (err) {
      alert(errorMsg(err));
    }
  }

  return (
    <div className="mx-auto max-w-6xl px-4 py-8">
      <div className="mb-6 flex flex-wrap items-center justify-between gap-4">
        <h1 className="text-2xl font-semibold text-slate-800">Browse policies</h1>
        <select className="input max-w-xs" value={categoryId} onChange={(e) => setCategoryId(e.target.value)}>
          <option value="">All categories</option>
          {categories.map((c) => (
            <option key={c.categoryId} value={c.categoryId}>{c.categoryName}</option>
          ))}
        </select>
      </div>

      {policies.length === 0 && <p className="text-slate-500">No policies available.</p>}

      <div className="grid gap-6 sm:grid-cols-2 lg:grid-cols-3">
        {policies.map((p) => (
          <div key={p.policyId} className="card flex flex-col">
            <span className="mb-2 w-fit rounded-full bg-indigo-50 px-2.5 py-1 text-xs font-medium text-indigo-700">
              {p.category?.categoryName}
            </span>
            <h3 className="text-lg font-semibold text-slate-800">{p.policyName}</h3>
            <p className="mt-1 flex-1 text-sm text-slate-500">{p.description}</p>

            <div className="mt-4 grid grid-cols-3 gap-2 text-center text-sm">
              <div className="rounded-lg bg-slate-50 p-2">
                <p className="text-xs text-slate-400">Premium</p>
                <p className="font-semibold text-slate-700">{money(p.premiumAmount)}</p>
              </div>
              <div className="rounded-lg bg-slate-50 p-2">
                <p className="text-xs text-slate-400">Coverage</p>
                <p className="font-semibold text-slate-700">{money(p.coverageAmount)}</p>
              </div>
              <div className="rounded-lg bg-slate-50 p-2">
                <p className="text-xs text-slate-400">Duration</p>
                <p className="font-semibold text-slate-700">{p.durationMonths} mo</p>
              </div>
            </div>

            <button onClick={() => buy(p)} className="btn mt-4">Buy now</button>
          </div>
        ))}
      </div>
    </div>
  );
}
