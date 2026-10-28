import { createContext, useCallback, useContext, useEffect, useRef, useState } from 'react'

const ToastContext = createContext(null)

const ESTILOS = {
  erro: { borda: 'border-danger', icone: 'ph-light ph-warning-circle', cor: 'text-danger' },
  sucesso: { borda: 'border-teal-accessible', icone: 'ph-light ph-check-circle', cor: 'text-teal-accessible' },
  aviso: { borda: 'border-amber-soft', icone: 'ph-light ph-clock-countdown', cor: 'text-navy' },
  info: { borda: 'border-blue-gray', icone: 'ph-light ph-info', cor: 'text-blue-gray' },
}

let proximoId = 1

export function ToastProvider({ children }) {
  const [toasts, setToasts] = useState([])
  const timersRef = useRef({})

  const removerToast = useCallback((id) => {
    setToasts((lista) => lista.filter((t) => t.id !== id))
    clearTimeout(timersRef.current[id])
    delete timersRef.current[id]
  }, [])

  const mostrarToast = useCallback(
    ({ tipo = 'info', mensagem }) => {
      const id = proximoId++
      setToasts((lista) => [...lista, { id, tipo, mensagem }])
      timersRef.current[id] = setTimeout(() => removerToast(id), 5000)
    },
    [removerToast]
  )

  useEffect(() => {
    function aoReceberEvento(evento) {
      mostrarToast(evento.detail)
    }
    window.addEventListener('iefc:toast', aoReceberEvento)
    return () => window.removeEventListener('iefc:toast', aoReceberEvento)
  }, [mostrarToast])

  return (
    <ToastContext.Provider value={{ mostrarToast }}>
      {children}
      <div className="fixed top-4 right-4 z-[1000] flex flex-col gap-2 w-[min(360px,calc(100vw-2rem))]" aria-live="polite">
        {toasts.map((t) => {
          const estilo = ESTILOS[t.tipo] ?? ESTILOS.info
          return (
            <div
              key={t.id}
              className={`flex items-start gap-2 bg-white border ${estilo.borda} rounded-md2 shadow-card px-3.5 py-3`}
              role="status"
            >
              <i className={`${estilo.icone} ${estilo.cor} text-[16px] mt-0.5`} />
              <p className="text-[13px] text-ink m-0 flex-1">{t.mensagem}</p>
              <button
                type="button"
                aria-label="Fechar"
                onClick={() => removerToast(t.id)}
                className="text-ink-caption cursor-pointer"
              >
                <i className="ph-light ph-x text-[14px]" />
              </button>
            </div>
          )
        })}
      </div>
    </ToastContext.Provider>
  )
}

export function useToast() {
  const contexto = useContext(ToastContext)
  if (!contexto) throw new Error('useToast deve ser usado dentro de <ToastProvider>')
  return contexto
}
