import Button from "../ui/Button";
import Layout from "../layout/Layout"

export default function HeroSection(){
  return (
    <section className="pt-30 bg-[#474D5E] flex flex-col justify-center  gap-10 h-142" id="inicio">
      <Layout>

        <h1 className="text-7xl font-medium text-[#E4E4F2]">Transformando <br />
        curiosidade em ciência</h1>
        <p className="text-3xl text-[#E4E4F2] mt-8 font-light"> Ensino para todos: acreditamos no <span className="font-normal">futuro</span> e na <span className="font-normal">mudança</span> <br /> através do desenvolvimento científico</p>

        <div className="w-full flex justify-start md:justify-end gap-4 mt-4">
          <Button className={"bg-[#E4E4F2] text-[#032738] mt-8 border-[#032738] border-4"} href={"#participar"}>
            Participar
          </Button>

          <Button className={"text-[#E4E4F2] bg-[#032738] border-[#032738] border-2 mt-8  ml-6"} href={"#doar"} >
            Doar
          </Button>
        </div>

      </Layout>
    </section>
  );
}