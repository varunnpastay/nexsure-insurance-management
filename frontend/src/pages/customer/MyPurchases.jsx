import { useEffect, useState } from "react";
import { getData, getUser, money } from "../../api";
import StatusBadge from "../../components/StatusBadge";

export default function MyPurchases() {
  const user = getUser();
  const [purchases, setPurchases] = useState([]);

  useEffect(() => {
    getData(`/purchase/user/${user.userId}`).then(setPurchases);
  }, []);

  return (
    <div className="mx-auto max-w-6xl px-4 py-8">
      <h1 className="page-title">My purchases</h1>
      <div className="table-wrap">
        <table className="w-full">
          <thead>
            <tr>
              <th className="th">Policy</th>
              <th className="th">Policy number</th>
              <th className="th">Amount</th>
              <th className="th">Purchased</th>
              <th className="th">Expires</th>
              <th className="th">Payment</th>
              <th className="th">Status</th>
            </tr>
          </thead>
          <tbody>
            {purchases.map((p) => (
              <tr key={p.purchaseId}>
                <td className="td font-medium">{p.policy?.policyName}</td>
                <td className="td font-mono text-xs">{p.policyNumber}</td>
                <td className="td">{money(p.purchaseAmount)}</td>
                <td className="td">{p.purchaseDate}</td>
                <td className="td">{p.expiryDate}</td>
                <td className="td"><StatusBadge status={p.paymentStatus} /></td>
                <td className="td"><StatusBadge status={p.status} /></td>
              </tr>
            ))}
            {purchases.length === 0 && (
              <tr><td className="td text-center text-slate-400" colSpan="7">You have not bought any policy yet.</td></tr>
            )}
          </tbody>
        </table>
      </div>
    </div>
  );
}
