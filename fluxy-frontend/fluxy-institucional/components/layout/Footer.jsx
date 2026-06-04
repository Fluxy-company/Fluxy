import Layout from "./Layout";
import Navbar from "./NavBar";

export default function Footer() {
  return (
    <footer className="bg-[#1C1F26] py-12" id="saiba-mais">
      <Layout>

        <div className="flex flex-col gap-10">

          <div className="flex justify-center items-center text-center">
            
            <div className="text-[#D6D6D6] justify-center">
              <p className="text-xl">
                Acompanhe nossas atualizações diretamente da fonte!
              </p>

              <a
                href="https://github.com/Fluxy-company/Fluxy"
                className="flex items-center justify-center gap-2 mt-2 text-xl font-semibold"
              >
                <img
                  src="../../src/assets/github.svg"
                  alt="GitHub"
                  className="h-6 w-18"
                />
                /Fluxy
              </a>
            </div>
          </div>

          <Navbar isFooter />

          <div className="border-t border-white/20 pt-6 flex justify-center items-center gap-2 text-[#D6D6D6]">
            <span className="text-xl">©</span>
            <span>2025 Fluxy</span>
          </div>

        </div>

      </Layout>
    </footer>
  );
}