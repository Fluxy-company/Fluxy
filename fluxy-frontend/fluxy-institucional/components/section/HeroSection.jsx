import Button from "../ui/Button";
import Layout from "../layout/Layout";
import logo from "../../src/assets/fluxy-claro.svg"

export default function HeroSection(){
  return (
    <section className="pt-8 bg-linear-to-b from-[#0E1116] to-[#22344E] flex flex-col justify-center px-6 gap-10 h-140 " id="inicio">
      <Layout>
        
        <div className="flex justify-start items-center mb-8">
            <img
              src={logo}
              alt="Ilustração"
              className="w-full max-w-52 h-auto"
            />
        </div>

        <h1 className="text-2xl font-bold text-[#D6D6D6]">O fluxo certo para o seu crescimento</h1>
        <p className="text-xl text-[#D6D6D6] mt-4 max-w-xl mb-8 "> Auxiliando empresas por meio de automações e otimizações de processos, auxiliando na transformação digital.</p>

        <Button className={"bg-[#3281F8] text-[#FCFFFD] mt-8"} href={"#quem-transformamos"}>
          Quem transformamos
        </Button>

        <Button className={"text-[#FCFFFD] border-[#3281F8] border-2 mt-8  ml-6"} href={"#saiba-mais"} >
          Fale Conosco
        </Button>

      </Layout>
    </section>
  );
}