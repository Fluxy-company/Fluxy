import Button from "../ui/Button";
import Layout from "../layout/Layout";

export default function HeroSection(){
  return (
    <section className="pt-24 md:pt-30 bg-[#474D5E] flex flex-col justify-center gap-6 md:gap-10 min-h-screen md:h-140" id="inicio">
      <Layout>

        <h1 className="text-4xl sm:text-5xl md:text-7xl font-medium text-[#E4E4F2] leading-tight">
          Transformando <br className="hidden sm:block" />
          curiosidade em ciência
        </h1>
        
        <p className="text-lg sm:text-xl md:text-3xl text-[#E4E4F2] mt-4 md:mt-8 font-light leading-relaxed">
          Ensino para todos: acreditamos no <span className="font-normal">futuro</span> e na <span className="font-normal">mudança</span> <br className="hidden sm:block" /> através do desenvolvimento científico
        </p>

        <div className="w-full flex flex-col sm:flex-row justify-start md:justify-end gap-4 mt-4">
          <Button className={"bg-[#E4E4F2] text-[#032738] mt-4 sm:mt-8 border-[#032738] border-4 w-full sm:w-auto text-center"} href={"#participar"}>
            Participar
          </Button>

          <Button className={"text-[#E4E4F2] bg-[#032738] border-[#032738] border-2 mt-2 sm:mt-8 sm:ml-6 w-full sm:w-auto text-center"} href={"#doar"} >
            Doar
          </Button>
        </div>

      </Layout>
    </section>
  );
}