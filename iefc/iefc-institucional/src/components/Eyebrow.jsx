export default function Eyebrow({ children, className = '' }) {
  return (
    <div
      className={`font-label text-[11px] tracking-[0.22em] uppercase text-teal-accessible mb-2.5 ${className}`}
    >
      {children}
    </div>
  )
}
