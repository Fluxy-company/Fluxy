import Navbar from "../components/layout/NavBar";
import Footer from "../components/layout/Footer";
import Button from "../components/ui/Button";


export default function Login(){
  return (
    <div>
      <Navbar>

        <Button href="/cadastro" className=" text-[#032738] border-2 border-[#0A4A68]">
            Cadastre-se
        </Button>

        <Button href="/login" className="bg-[#0A4A68] text-[#E4E4F2] border-[#0A4A68] border-2">
          Login
        </Button>

      </Navbar>
      <div className="bg-[url(/background-iefc.jpg)] bg-no-repeat min-h-screen bg-cover">
        <div className="grid grid-cols-1 md:grid-cols-2 h-screen ">
          <div className="bg-[#F9F9F9] mt-22 mb-2 rounded-md w-full max-w-lg p-10 flex flex-col gap-4 col-2">
            <div className="text-center">
              <h2 className="text-2xl font-bold text-[#032738] py-4">Bem-vindo de volta!</h2>
              <h3 className="text-2xl font-normal text-[#8F8F9F]">Entre com sua conta</h3>
            </div>

            <div className="flex flex-col gap-1">
              <label htmlFor="input_email">Email</label>
              <input
                id="input_email"
                type="text"
                placeholder="Digite seu email"
                className="w-full p-3 rounded border border-[#032738] bg-transparent outline-none focus:border-[#7F77DD]"
              />
            </div>

            <div className="flex flex-col gap-1">
              <label htmlFor="input_senha">Senha</label>
              <input
                id="input_senha"
                type="password"
                placeholder="Digite sua senha"
                className="w-full p-3 rounded border border-[#032738] bg-transparent outline-none focus:border-[#7F77DD]"
              />
            </div>

            <Button
              className="bg-[#0A4A68] text-white text-lg w-full hover:opacity-90 transition"
            >
              Entrar
            </Button>

            <div className="w-full flex group text-center">
              <span>Ainda não tem cadastro?</span>
              <a 
                href="/login" 
                className="text-[#1D9291] border-b-2 border-[#032738]"
              >
                 {` `} Crie uma conta 
                <svg 
                  xmlns="http://www.w3.org/2000/svg" 
                  fill="none" 
                  viewBox="0 0 24 24" 
                  strokeWidth={2} 
                  stroke="currentColor" 
                  className="w-5 h-5 transform group-hover:translate-x-1 transition-transform inline"
                >
                  <path strokeLinecap="round" strokeLinejoin="round" d="M13.5 4.5l6 6m0 0l-6 6M19.5 10.5h-14" />
                </svg>
              </a>
            </div>
          </div>
        </div>
      </div>
      <Footer />
    </div>
  );
}