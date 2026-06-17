import Layout from "../layout/Layout";

export default function ValoresSection(){
  const itens = [
    {
      titulo: "Missão",
      linkIcone: "../../src/assets/puzzle-missao.svg",
      descricao: "Empoderar usuários a criar e evoluir seus próprios sistemas, com autonomia, flexibilidade e controle total sobre seus processos.",
    },
    {
      titulo: "Visão",
      linkIcone: "../../src/assets/target-visao.svg",
      descricao: "Ser a principal plataforma de criação e personalização de sistemas, onde cada usuário tem liberdade para construir soluções únicas.",
    },
    {
      titulo: "Valores",
      linkIcone: "../../src/assets/lightning-valores.svg",
      descricao: "Conectividade, Agilidade e Foco no Usuário.",
    },
  ];
  return (
    <section className="bg-[#1C1F26] py-20 px-6" id="valores">
      <Layout>
        <div className="max-w-6xl mx-auto grid grid-cols-1 md:grid-cols-3 gap-12 text-center">
          {itens.map((item, index) => (
            <div key={index} className="flex flex-col items-center group">

              <div className="relative mb-8">
                <span className="absolute -top-1 -left-2 w-6 h-6 bg-[#8257e5] rounded-sm -z-10">Opa</span>
                <h2 className="text-4xl font-semibold text-[#D6D6D6]">
                  {item.titulo}
                </h2>
              </div>

              <div className="mb-6 h-16 w-16 flex items-center justify-center">
                <img 
                  src={item.linkIcone} 
                  alt={`Ícone representativo de ${item.titulo}`}
                  className="w-full h-full" 
                />
              </div>
              <p className="text-[#D6D6D6] text-lg leading-relaxed max-w-xs">
                {item.descricao}
              </p>
            </div>
          ))}
        </div>
      </Layout>
    </section>
  );
}