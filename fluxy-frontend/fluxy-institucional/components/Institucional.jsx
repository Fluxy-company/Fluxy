import NavBar from "./layout/NavBar";
import Button from "./ui/Button";
import HeroSection from "./section/HeroSection";
import ValoresSection from "./section/ValoresSection";
import QuemSomosSection from "./section/QuemSomosSection";
import QuemTransformamosSection from "./section/QuemTransformamosSection";
import Footer from "./layout/Footer";

export default function Institucional(){
  return (
    <>
        <NavBar>
          
          <Button href="/login" className="text-[#FCFFFD] border-[#3281F8] border-2">
            Login
          </Button>

          <Button href="/cadastro" className="bg-[#3281F8] text-[#FCFFFD]">
            Cadastro
          </Button>
        </NavBar>        
        <HeroSection />
        <ValoresSection />
        <QuemSomosSection />
        <QuemTransformamosSection />
        <Footer />
    </>
  );
}