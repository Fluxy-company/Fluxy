import Layout from "../layout/Layout";

export default function MetricaSection() {

  const metricas = [
    {
      id: 1,
      number: "45+",
      label: "Cursos, lives e workshops",
      bgColor: "bg-[#F2B242]",
    },
    {
      id: 2,
      number: "110+",
      label: "Cidades impactadas",
      bgColor: "bg-[#7697A0]",
    },
    {
      id: 3,
      number: "3.063",
      label: "Impactados diretamente",
      bgColor: "bg-[#1D9291]",
    },
  ];

  return (

    <section className="bg-[#E4E4F2] py-8 md:py-12">
      <Layout>
        
        <div className="grid grid-cols-1 md:grid-cols-3 gap-20">
          {metricas.map((metrica) => (
            <div
              key={metrica.id}
              className={`${metrica.bgColor} text-white rounded-lg p-6 flex flex-col justify-center items-center text-center shadow-sm h-36 md:h-40`}
            >
              
              <span className="font-[Cabin] text-7xl font-semibold tracking-tight">
                {metrica.number}
              </span>
              
              <p className="text-2xl font-normal mt-2 leading-tight">
                {metrica.label}
              </p>
            </div>
          ))}
        </div>
      </Layout>
    </section>
  );
}