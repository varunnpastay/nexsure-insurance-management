import { useEffect, useState } from "react";
import api, { getData, errorMsg } from "../../api";

export default function Users() {
  const [users, setUsers] = useState([]);

  function load() {
    getData("/user/get").then(setUsers);
  }

  useEffect(load, []);

  async function remove(id) {
    if (!confirm("Delete this user?")) return;
    try {
      await api.delete(`/user/delete/${id}`);
      load();
    } catch (err) {
      alert(errorMsg(err));
    }
  }

  return (
    <div className="mx-auto max-w-6xl px-4 py-8">
      <h1 className="page-title">Manage customers</h1>
      <div className="table-wrap">
        <table className="w-full">
          <thead>
            <tr>
              <th className="th">ID</th>
              <th className="th">Name</th>
              <th className="th">Email</th>
              <th className="th">Role</th>
              <th className="th">Joined</th>
              <th className="th"></th>
            </tr>
          </thead>
          <tbody>
            {users.map((u) => (
              <tr key={u.userId}>
                <td className="td">{u.userId}</td>
                <td className="td font-medium">{u.fullName}</td>
                <td className="td">{u.email}</td>
                <td className="td">{u.role}</td>
                <td className="td">{u.createdAt?.slice(0, 10)}</td>
                <td className="td text-right">
                  {u.role !== "ADMIN" && (
                    <button onClick={() => remove(u.userId)} className="btn-danger">Delete</button>
                  )}
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>
    </div>
  );
}
