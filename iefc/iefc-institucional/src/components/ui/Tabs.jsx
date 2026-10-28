export default function Tabs({ abas, ativa, onChange, className = '' }) {
  return (
    <div
      role="tablist"
      aria-label="Seções da página"
      className={`flex gap-1 border-b border-border overflow-x-auto ${className}`}
    >
      {abas.map((aba) => (
        <button
          key={aba.valor}
          type="button"
          role="tab"
          aria-selected={ativa === aba.valor}
          onClick={() => onChange(aba.valor)}
          className={`shrink-0 px-4 py-3 font-label text-[12px] tracking-[0.08em] uppercase cursor-pointer border-b-2 -mb-px transition-colors ${
            ativa === aba.valor
              ? 'border-teal-accessible text-teal-accessible'
              : 'border-transparent text-ink-caption hover:text-ink-soft'
          }`}
        >
          {aba.titulo}
        </button>
      ))}
    </div>
  )
}
