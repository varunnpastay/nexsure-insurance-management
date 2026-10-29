const colors = {
  ACTIVE: "bg-green-100 text-green-700",
  PAID: "bg-green-100 text-green-700",
  APPROVED: "bg-green-100 text-green-700",
  PENDING: "bg-amber-100 text-amber-700",
  EXPIRED: "bg-amber-100 text-amber-700",
  INACTIVE: "bg-slate-200 text-slate-600",
  CANCELLED: "bg-red-100 text-red-700",
  REJECTED: "bg-red-100 text-red-700",
  FAILED: "bg-red-100 text-red-700",
};

export default function StatusBadge({ status }) {
  return (
    <span className={`rounded-full px-2.5 py-1 text-xs font-semibold ${colors[status] || "bg-slate-100 text-slate-600"}`}>
      {status}
    </span>
  );
}
