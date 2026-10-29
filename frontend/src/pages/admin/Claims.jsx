import { useEffect, useState } from "react";
import api, { getData, money } from "../../api";
import StatusBadge from "../../components/StatusBadge";

export default function Claims() {
  const [claims, setClaims] = useState([]);

  function load() {
    getData("/claim/get").then(setClaims);
  }

  useEffect(load, []);

  async function changeStatus(id, status) {
    await api.put(`/claim/status/${id}?status=${status}`);
    load();
  }

  async function remove(id) {
    if (!confirm("Delete this claim?")) return;
    await api.delete(`/claim/delete/${id}`);
    load();
  }

  return (
    <div className="mx-auto max-w-6xl px-4 py-8">
      <h1 className="page-title">Manage claims</h1>
      <div className="table-wrap">
        <table className="w-full">
          <thead>
            <tr>
              <th className="th">Customer</th>
              <th className="th">Policy</th>
              <th className="th">Amount</th>
              <th className="th">Reason</th>
              <th className="th">Date</th>
              <th className="th">Status</th>
              <th className="th">Update</th>
              <th className="th"></th>
            </tr>
          </thead>
          <tbody>
            {claims.map((c) => (
              <tr key={c.claimId}>
                <td className="td">{c.user?.fullName}</td>
                <td className="td">
                  {c.purchase?.policy?.policyName}
                  <div className="font-mono text-xs text-slate-400">{c.purchase?.policyNumber}</div>
                </td>
                <td className="td">{money(c.claimAmount)}</td>
                <td className="td">{c.claimReason}</td>
                <td className="td">{c.claimDate}</td>
                <td className="td"><StatusBadge status={c.status} /></td>
                <td className="td">
                  <select className="input !w-auto !py-1" value={c.status} onChange={(e) => changeStatus(c.claimId, e.target.value)}>
                    <option value="PENDING">PENDING</option>
                    <option value="APPROVED">APPROVED</option>
                    <option value="REJECTED">REJECTED</option>
                  </select>
                </td>
                <td className="td text-right">
                  <button onClick={() => remove(c.claimId)} className="btn-danger">Delete</button>
                </td>
              </tr>
            ))}
            {claims.length === 0 && (
              <tr><td className="td text-center text-slate-400" colSpan="8">No claims yet.</td></tr>
            )}
          </tbody>
        </table>
      </div>
    </div>
  );
}
