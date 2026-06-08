import { href } from "react-router-dom";
import Layout from "../layout/Layout";

export default function ApoiadoresSection(){
  const logos = [
    { id: 1, nome: "A&M", src: "/empresas/alvarez-e-marsal.jpg" },
    { id: 2, nome: "Bisutti", src: "/empresas/bisutti.jpg" },
    { id: 3, nome: "Casa do Damasco", src: "/empresas/casa-do-damasco.jpg" },
    { id: 4, nome: "Casa Vasconcellos", src: "/empresas/casa-vasconcellos.jpg" },
    { id: 5, nome: "Celta Containers", src: "/empresas/celta-containers.jpg" },
    { id: 6, nome: "Finocchio & Ustra", src: "/empresas/finocchio-e-ustra.jpg" },
    { id: 7, nome: "Grupo Terra", src: "/empresas/grupo-terra.jpg" },
    { id: 8, nome: "Produtora / Canon", src: "/empresas/produtora-canon.jpg" },
  ];

  return (
    <section className="bg-[#F2B242] text-[#032738]" id="apoio">
      
      <div className="py-12">
        <Layout>
          <h2 className="text-2xl md:text-3xl font-bold mb-8">
            Conheça nossos apoiadores
          </h2>
          
          <div className="grid grid-cols-2 md:grid-cols-4 gap-4 md:gap-6 items-center">
            {logos.map((logo) => (
              <div 
                key={logo.id} 
                className="bg-white rounded-lg p-4 flex items-center justify-center shadow-sm border border-black/5 overflow-hidden"
              >
                <img 
                  src={logo.src} 
                  alt={logo.nome} 
                  className="object-contain filter contrast-125" 
                />
              </div>
            ))}
          </div>
        </Layout>
      </div>

      <div className="border-t border-[#032738]/20 w-full"></div>

      <div className="bg-[#F4C87C] py-12">
        <Layout>
          <h2 className="text-2xl md:text-3xl font-bold mb-8">
            Apoie você também!
          </h2>

          <div className="grid grid-cols-1 md:grid-cols-2 gap-12 md:gap-16 items-start">
            
            <div className="flex flex-col gap-3">
              <h3 className="text-lg md:text-xl font-bold">
                Via transferência bancária
              </h3>
              <div className="text-sm md:text-base font-normal leading-relaxed flex flex-col gap-1">
                <p className="font-normal mt-1">Dados da conta do IEFC:</p>
                <p><span className="font-semibold">Razão social:</span> IEFC - Instituto Educacional Futuro da Ciência</p>
                <p><span className="font-semibold">Banco:</span> Itaú (341)</p>
                <p><span className="font-semibold">Agência:</span> 0066</p>
                <p><span className="font-semibold">Conta Corrente:</span> 38802-6</p>
                <p><span className="font-semibold">CNPJ:</span> 29.260.548/0001-52</p>
              </div>
            </div>

            <div className="flex flex-col gap-3">
              <h3 className="text-lg md:text-xl font-bold">
                Via Cartão de Crédito
              </h3>
              <p className="text-md md:text-base font-normal leading-relaxed">
                Faça doações mensais e <br /> ajude a implementar nossos projetos.
              </p>
              <p className="text-sm md:text-base mt-2">
                Acesse o PayPal {` `}
                <a href="https://www.paypal.com/donate?hosted_button_id=GBNZ6EHDRBB2C" target="_blank"
                 className="underline font-semibold hover:text-blue-900 transition-colors">
                    aqui
                </a>.
              </p>
              
              <div className="bg-white p-2 rounded-md w-32 h-32 mt-2 shadow-sm flex items-center justify-center">
                <img 
                  src="/doacao-qrcode.jpg"
                  alt="QR Code para Doação" 
                  className="w-full h-full object-contain"
                />
              </div>
            </div>

          </div>

          <div className="w-full flex justify-center mt-16 pb-2" >
            <a 
              href="https://www.iefc.org.br/_files/ugd/0c24b7_086e5b00fd1c4dd0a2b5e5c9eaf5d905.pdf"
              target="_blank" 
              className="text-base md:text-2xl font-semibold border-b-2 border-[#032738] pb-0.5 hover:text-blue-900 hover:border-blue-900 transition-colors tracking-wide"
            >
              Confira também nossos benefícios fiscais
            </a>
          </div>

        </Layout>
      </div>

    </section>
  );
}