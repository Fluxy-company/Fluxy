import Layout from "../layout/Layout";

import imgPesquisas from "../../assets/pesquisas.png";
import imgProfessores from "../../assets/professores.png";
import imgProjetos from "../../assets/projetos.png";

export default function OQueFazemosSection(){
  const cards = [
    {
      id: 1,
      titulo: "Pesquisas científicas",
      descricao: "Desenvolvemos investigações e estudos práticos voltados à inovação, incentivando a produção de conhecimento técnico focado na resolução de problemas reais da nossa sociedade.",
      caminho: imgPesquisas
    },
    {
      id: 2,
      titulo: "Formação de professores",
      descricao: "Capacitamos educadores por meio de workshops, lives e cursos especializados, fornecendo ferramentas modernas e metodologias ativas para transformar o ensino de ciências nas salas de aula.",
      caminho: imgProfessores
    },
    {
      id: 3,
      titulo: "Projetos educacionais",
      descricao: "Criamos programas práticos voltados a crianças e jovens, aproximando a comunidade escolar do universo científico através de experiências dinâmicas que despertam a curiosidade.",
      caminho: imgProjetos
    },
  ];

  return (
    <section className="bg-white py-16" id="o-que-fazemos">
      <Layout>
        <h2 className="text-3xl md:text-4xl font-bold text-[#032738] mb-10">
          O que fazemos
        </h2>

        <div className="grid grid-cols-1 md:grid-cols-3 gap-8">
          {cards.map((card) => (
            <div 
              key={card.id} 
              className="bg-[#EAEAF4] rounded-t-xl overflow-hidden flex flex-col h-full shadow-sm"
            >

              <div className="h-48 w-full overflow-hidden">
                <img 
                  src={card.caminho} 
                  alt={card.titulo} 
                  className="w-full h-full object-cover"
                />
              </div>

              <div className="p-6 flex flex-col flex-grow">
                <h3 className="text-2xl font-semibold text-[#032738] mb-3">
                  {card.titulo}
                </h3>
                <p className="text-sm md:text-base text-gray-700 font-light leading-relaxed">
                  {card.descricao}
                </p>
              </div>
            </div>
          ))}
        </div>

        <div className="w-full flex justify-end mt-10">
          <a 
            href="#cursos" 
            className="group flex items-center gap-2 text-lg font-medium text-[#032738] border-b-2 border-[#032738] pb-1 hover:text-blue-900 hover:border-blue-900 transition-colors"
          >
            Conheça nossos cursos

            <svg 
              xmlns="http://www.w3.org/2000/svg" 
              fill="none" 
              viewBox="0 0 24 24" 
              strokeWidth={2} 
              stroke="currentColor" 
              className="w-5 h-5 transform group-hover:translate-x-1 transition-transform"
            >
              <path strokeLinecap="round" strokeLinejoin="round" d="M13.5 4.5l6 6m0 0l-6 6M19.5 10.5h-14" />
            </svg>
          </a>
        </div>
      </Layout>
    </section>
  );
}
