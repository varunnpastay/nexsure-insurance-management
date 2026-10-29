import { useEffect, useState } from "react";
import api, { getData, money, errorMsg } from "../../api";
import StatusBadge from "../../components/StatusBadge";

export default function Purchases() {
  const [purchases, setPurchases] = useState([]);

  function load() {
    getData("/purchase/get").then(setPurchases);
  }

  useEffect(load, []);

  async function changeStatus(id, status) {
    await api.put(`/purchase/status/${id}?status=${status}`);
    load();
  }

  async function remove(id) {
    if (!confirm("Delete this purchase?")) return;
    try {
      await api.delete(`/purchase/delete/${id}`);
      load();
    } catch (err) {
      alert(errorMsg(err));
    }
  }

  return (
    <div className="mx-auto max-w-6xl px-4 py-8">
      <h1 className="page-title">Manage purchases</h1>
      <div className="table-wrap">
        <table className="w-full">
          <thead>
            <tr>
              <th className="th">Policy number</th>
              <th className="th">Customer</th>
              <th className="th">Policy</th>
              <th className="th">Amount</th>
              <th className="th">Expires</th>
              <th className="th">Payment</th>
              <th className="th">Status</th>
              <th className="th"></th>
            </tr>
          </thead>
          <tbody>
            {purchases.map((p) => (
              <tr key={p.purchaseId}>
                <td className="td font-mono text-xs">{p.policyNumber}</td>
                <td className="td">{p.user?.fullName}</td>
                <td className="td">{p.policy?.policyName}</td>
                <td className="td">{money(p.purchaseAmount)}</td>
                <td className="td">{p.expiryDate}</td>
                <td className="td"><StatusBadge status={p.paymentStatus} /></td>
                <td className="td">
                  <select className="input !w-auto !py-1" value={p.status} onChange={(e) => changeStatus(p.purchaseId, e.target.value)}>
                    <option value="ACTIVE">ACTIVE</option>
                    <option value="EXPIRED">EXPIRED</option>
                    <option value="CANCELLED">CANCELLED</option>
                  </select>
                </td>
                <td className="td text-right">
                  <button onClick={() => remove(p.purchaseId)} className="btn-danger">Delete</button>
                </td>
              </tr>
            ))}
            {purchases.length === 0 && (
              <tr><td className="td text-center text-slate-400" colSpan="8">No purchases yet.</td></tr>
            )}
          </tbody>
        </table>
      </div>
    </div>
  );
}
