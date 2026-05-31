export default function StatCard({ label, value, detail }) {
  return (
    <div className="bg-white border border-gray-200 px-6 py-5 flex flex-col gap-1 min-w-[180px]">
      <span className="text-xs font-bold uppercase tracking-wider text-gray-500">
        {label}
      </span>
      <div className="flex items-baseline gap-2">
        <span className="text-4xl font-bold text-[#1e2d3d]">{value}</span>
        <span className="text-sm text-gray-500">{detail}</span>
      </div>
    </div>
  );
}
