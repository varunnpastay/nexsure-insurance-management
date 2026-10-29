import { Link } from "react-router-dom";
import { getUser } from "../api";
import Logo from "../components/Logo";

const features = [
  { title: "Easy policies", text: "Browse health, life, vehicle and more policies by category." },
  { title: "Instant purchase", text: "Buy a policy in one click and get your policy number right away." },
  { title: "Simple claims", text: "File a claim on any purchased policy and track its status." },
];

export default function Home() {
  const user = getUser();
  const startLink = user ? "/policies" : "/register";

  return (
    <div>
      <section className="bg-gradient-to-br from-indigo-600 to-indigo-800 text-white">
        <div className="mx-auto max-w-6xl px-4 py-20 text-center">
          <Logo className="mx-auto mb-6 h-14 w-14 text-indigo-200" />
          <h1 className="text-4xl font-bold md:text-5xl">Protect what matters most</h1>
          <p className="mx-auto mt-4 max-w-xl text-indigo-100">
            NexSure helps you find, buy and manage insurance policies and claims, all in one place.
          </p>
          <div className="mt-8 flex justify-center gap-3">
            <Link to={startLink} className="rounded-lg bg-white px-6 py-3 font-semibold text-indigo-700 hover:bg-indigo-50">
              Get started
            </Link>
            {!user && (
              <Link to="/login" className="rounded-lg border border-white/60 px-6 py-3 font-semibold hover:bg-white/10">
                Login
              </Link>
            )}
          </div>
        </div>
      </section>

      <section className="mx-auto grid max-w-6xl gap-6 px-4 py-16 md:grid-cols-3">
        {features.map((f) => (
          <div key={f.title} className="card">
            <h3 className="text-lg font-semibold text-slate-800">{f.title}</h3>
            <p className="mt-2 text-sm text-slate-500">{f.text}</p>
          </div>
        ))}
      </section>

      <footer className="border-t border-slate-200 py-6 text-center text-sm text-slate-400">
        © NexSure Insurance · <Link to="/admin/login" className="hover:text-indigo-600">Admin Portal</Link>
      </footer>
    </div>
  );
}
