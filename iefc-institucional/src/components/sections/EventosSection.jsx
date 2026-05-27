import Layout from "../layout/Layout";

export default function EventosSection(){
  const eventos = [
    {
      id: 1,
      titulo: "Física e o buraco da fechadura",
      palestrante: "Fabiana Botelho Kneubil",
      local: "Avenida do Estado, 1999 - Prédio lorem ipsum",
      data: "13/04",
      horario: "14h30",
    },
    {
      id: 2,
      titulo: "Física e o buraco da fechadura",
      palestrante: "Fabiana Botelho Kneubil",
      local: "Avenida do Estado, 1999 - Prédio lorem ipsum",
      data: "13/04",
      horario: "14h30",
    },
  ];

  return (
    <section className="bg-white py-16" id="eventos">
      <Layout>
        {/* Título Principal */}
        <h2 className="text-3xl md:text-4xl font-bold text-[#032738] mb-6">
          Acompanhe nossos próximos eventos
        </h2>

        {/* Linha divisória superior do título */}
        <div className="border-t border-[#032738] w-full mb-6"></div>

        {/* Lista de Eventos */}
        <div className="flex flex-col">
          {eventos.map((evento) => (
            <div 
              key={evento.id} 
              className="border-b border-[#032738] py-6 flex flex-col md:flex-row md:justify-between md:items-start gap-4"
            >
              {/* Lado Esquerdo: Indicador, Título, Palestrante e Local */}
              <div className="flex gap-4 items-start flex-grow">
                {/* Bolinha Verde Lateral */}
                <span className="w-4 h-4 bg-[#3B8E8E] rounded-full mt-1.5 flex-shrink-0"></span>
                
                <div>
                  <h3 className="text-lg md:text-xl text-[#032738]">
                    <span className="font-bold">{evento.titulo}</span>
                    <span className="font-light"> - {evento.palestrante}</span>
                  </h3>
                  <p className="text-gray-600 font-light mt-2 text-base md:text-lg">
                    {evento.local}
                  </p>
                </div>
              </div>

              {/* Lado Direito: Data e Horário */}
              <div className="text-right text-gray-700 text-lg md:text-xl font-light whitespace-nowrap pl-8 md:pl-0">
                {evento.data} -{evento.horario}
              </div>
            </div>
          ))}
        </div>

        {/* Link "Veja nosso calendário de eventos" alinhado à direita */}
        <div className="w-full flex justify-end mt-8">
          <a 
            href="#calendario" 
            className="group flex items-center gap-2 text-base md:text-lg font-medium text-[#3B8E8E] border-b-2 border-[#3B8E8E] pb-1 hover:text-teal-800 hover:border-teal-800 transition-colors"
          >
            Veja nosso calendário de eventos
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