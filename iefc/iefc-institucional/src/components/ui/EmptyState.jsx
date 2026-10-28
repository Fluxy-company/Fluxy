export default function EmptyState({ icone = 'ph-light ph-tray', titulo, descricao, acao }) {
  return (
    <div className="flex flex-col items-center text-center py-10 px-4">
      <i className={`${icone} text-blue-gray text-[32px]`} />
      <p className="font-display font-medium text-navy text-[16px] mt-3 mb-1">{titulo}</p>
      {descricao && <p className="text-[13px] text-ink-soft max-w-[360px] mb-4">{descricao}</p>}
      {acao}
    </div>
  )
}
