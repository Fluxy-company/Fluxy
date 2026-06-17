import Layout from "../layout/Layout";

import imgFabiana from "../../assets/fabiana-botelho.png" 
import imgEstudante from "../../assets/estudante.png" 

export default function ParaQuemSection(){
  const cards = [
    {
      id: 1,
      titulo: "Professores",
      descricao: "",
      caminho: imgFabiana
    },
    {
      id: 2,
      titulo: "Estudantes e entusiastas",
      descricao: "",
      caminho: imgEstudante
    }
  ]
  
  return(
    <section className="bg-[#EAEAF4] py-16" id="o-que-fazemos">
    <Layout>
      <h2 className="text-3xl md:text-4xl font-bold text-[#032738] mb-10">
        Para quem é
      </h2>

      <div className="grid grid-cols-1 md:grid-cols-2 gap-8">
        {cards.map((card) => (
          <div 
            key={card.id} 
            className="bg-[#F9F9F9] rounded-t-xl overflow-hidden flex flex-col h-full shadow-sm"
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
          Conheça mais da nossa história
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