import { useEffect, useState } from "react";
import api, { getData, getUser, money, errorMsg } from "../../api";
import StatusBadge from "../../components/StatusBadge";

const emptyForm = { purchaseId: "", claimAmount: "", claimReason: "" };

export default function MyClaims() {
  const user = getUser();
  const [claims, setClaims] = useState([]);
  const [purchases, setPurchases] = useState([]);
  const [form, setForm] = useState(emptyForm);
  const [message, setMessage] = useState("");

  function loadClaims() {
    getData(`/claim/user/${user.userId}`).then(setClaims);
  }

  useEffect(() => {
    loadClaims();
    getData(`/purchase/user/${user.userId}`).then(setPurchases);
  }, []);

  function handleChange(e) {
    setForm({ ...form, [e.target.name]: e.target.value });
  }

  async function handleSubmit(e) {
    e.preventDefault();
    setMessage("");
    try {
      const res = await api.post("/claim/add", {
        userId: user.userId,
        purchaseId: Number(form.purchaseId),
        claimAmount: Number(form.claimAmount),
        claimReason: form.claimReason,
      });
      setForm(emptyForm);
      setMessage(res.data.message);
      loadClaims();
    } catch (err) {
      setMessage(errorMsg(err));
    }
  }

  const activePurchases = purchases.filter((p) => p.status === "ACTIVE");

  return (
    <div className="mx-auto max-w-6xl px-4 py-8">
      <h1 className="page-title">My claims</h1>

      <div className="grid gap-6 lg:grid-cols-3">
        <form onSubmit={handleSubmit} className="card space-y-4 h-fit">
          <h2 className="text-lg font-semibold text-slate-800">File a claim</h2>
          {message && <p className="rounded-lg bg-indigo-50 px-3 py-2 text-sm text-indigo-700">{message}</p>}

          <div>
            <label className="label">Purchase</label>
            <select className="input" name="purchaseId" value={form.purchaseId} onChange={handleChange} required>
              <option value="">Select a policy</option>
              {activePurchases.map((p) => (
                <option key={p.purchaseId} value={p.purchaseId}>
                  {p.policy?.policyName} ({p.policyNumber})
                </option>
              ))}
            </select>
          </div>
          <div>
            <label className="label">Claim amount</label>
            <input className="input" type="number" step="0.01" min="1" name="claimAmount" value={form.claimAmount} onChange={handleChange} required />
          </div>
          <div>
            <label className="label">Reason</label>
            <textarea className="input" rows="3" name="claimReason" value={form.claimReason} onChange={handleChange} required />
          </div>
          <button className="btn w-full" disabled={activePurchases.length === 0}>Submit claim</button>
        </form>

        <div className="table-wrap lg:col-span-2 h-fit">
          <table className="w-full">
            <thead>
              <tr>
                <th className="th">Policy</th>
                <th className="th">Amount</th>
                <th className="th">Reason</th>
                <th className="th">Date</th>
                <th className="th">Status</th>
              </tr>
            </thead>
            <tbody>
              {claims.map((c) => (
                <tr key={c.claimId}>
                  <td className="td font-medium">{c.purchase?.policy?.policyName}</td>
                  <td className="td">{money(c.claimAmount)}</td>
                  <td className="td">{c.claimReason}</td>
                  <td className="td">{c.claimDate}</td>
                  <td className="td"><StatusBadge status={c.status} /></td>
                </tr>
              ))}
              {claims.length === 0 && (
                <tr><td className="td text-center text-slate-400" colSpan="5">No claims yet.</td></tr>
              )}
            </tbody>
          </table>
        </div>
      </div>
    </div>
  );
}
