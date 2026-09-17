import { useEffect, useState } from 'react'

const fmt = (n) => Math.round(n).toLocaleString('pt-BR')
const ease = (t) => 1 - Math.pow(1 - t, 3)

export default function AnimatedStat({ icon, target, label }) {
  const [value, setValue] = useState('0')

  useEffect(() => {
    const dur = 1400
    const start = Date.now()
    const timeout = setTimeout(() => {
      const iv = setInterval(() => {
        const p = Math.min((Date.now() - start) / dur, 1)
        setValue(fmt(target * ease(p)))
        if (p >= 1) clearInterval(iv)
      }, 40)
      return () => clearInterval(iv)
    }, 450)
    return () => clearTimeout(timeout)
  }, [target])

  return (
    <div className="bg-white border border-border rounded-lg2 px-6 py-7">
      <i className={`ph ${icon} text-[26px] text-teal-accessible`} />
      <div className="font-label font-bold text-[46px] text-navy leading-none mt-3.5">{value}</div>
      <div className="font-label text-[11px] tracking-[0.14em] uppercase text-ink-soft mt-2.5">{label}</div>
    </div>
  )
}
