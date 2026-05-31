export default function Badge({ children, variant = "default" }) {
  const variants = {
    "em-andamento": "!bg-[#1e2d3d] text-white",
    "nao-iniciado": "!bg-gray-400 text-white",
    "concluido": "!bg-green-600 text-white",
    "vitalicio": "!bg-amber-600 text-white",
    default: "!bg-gray-400 text-white",
  };

  return (
    <span
      className={`px-3 py-1 text-xs font-bold uppercase tracking-wider ${variants[variant] || variants.default}`}
    >
      {children}
    </span>
  );
}
