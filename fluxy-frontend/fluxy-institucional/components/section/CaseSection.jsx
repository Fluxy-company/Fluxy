import Layout from "../layout/Layout";

export default function CaseSection({
  titulo,
  subtitulo,
  descricao,
  imagem,
  links = [],
  bgClass,
  textClass,
  children
}) {
  return (
    <section className={`bg-${bgClass} max-h-133 flex items-center overflow-hidden text-${textClass}`}>
      <Layout>
        <div className="flex items-center justify-between gap-12">

          <div className="flex flex-col gap-6 max-w-xl">

            <h2 className="text-4xl md:text-5xl font-bold">
              {titulo}
            </h2>

            {subtitulo && (
              <div className="flex items-center gap-2 text-3xl">
                <span className="w-2 h-2 bg-[#7B2CBF] rounded-full"></span>
                <span>{subtitulo}</span>
              </div>
            )}

            <p className="text-xl">
              {descricao}
            </p>

            <div className="flex gap-10 mt-6">
              {links.map((link, index) => (
                <a
                  key={index}
                  href={link.href}
                  className="flex items-center underline underline-offset-4 text-xl"
                >
                  <img
                    src={link.icon}
                    alt={link.label}
                    className="w-6 h-6 "
                  />
                  {link.label}
                </a>
              ))}
            </div>

            {children}

          </div>

          <div className="hidden md:flex items-center justify-end w-full">
            <img
              src={imagem}
              alt="Projeto"
              className="h-96 object-contain"
            />
          </div>

        </div>
      </Layout>
    </section>
  );
}