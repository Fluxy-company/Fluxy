export default function PageHeader({ eyebrow, title, lead }) {
  return (
    <section className="relative px-6 md:px-10 pt-16 pb-14 md:pt-20 md:pb-16 bg-navy overflow-hidden">
      <div className="relative max-w-content mx-auto">
        {eyebrow && (
          <div className="font-label text-[12px] tracking-[0.28em] uppercase text-on-dark-accent mb-5">
            {eyebrow}
          </div>
        )}
        <h1 className="font-display font-bold text-[38px] md:text-[52px] leading-[1.08] text-white mb-4 max-w-[820px] text-balance">
          {title}
        </h1>
        {lead && (
          <p className="text-[17px] md:text-lead leading-relaxed text-on-dark max-w-[640px] m-0">
            {lead}
          </p>
        )}
      </div>
    </section>
  )
}
