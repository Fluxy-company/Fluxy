import { NavLink } from 'react-router-dom'

const variants = {
  amber:
    'bg-amber text-navy hover:bg-amber-light',
  navy:
    'bg-navy text-white hover:bg-navy-hover',
  outline:
    'bg-transparent text-teal-accessible border-[1.5px] border-teal-accessible hover:bg-teal-accessible hover:text-white',
}

export default function Button({ to, href, children, variant = 'amber', icon = 'ph-arrow-right', className = '' }) {
  const cls = `inline-flex items-center gap-2 font-label font-bold text-[13px] tracking-[0.08em] uppercase px-6 py-3.5 rounded-md2 cursor-pointer transition-colors ${variants[variant]} ${className}`

  const content = (
    <>
      {children}
      {icon && <i className={`ph ${icon}`} />}
    </>
  )

  if (to) {
    return (
      <NavLink to={to} className={cls}>
        {content}
      </NavLink>
    )
  }
  if (href) {
    return (
      <a href={href} target="_blank" rel="noreferrer" className={cls}>
        {content}
      </a>
    )
  }
  return <button className={cls}>{content}</button>
}
