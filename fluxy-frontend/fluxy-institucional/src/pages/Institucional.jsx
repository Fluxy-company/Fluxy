import { useLocation } from "react-router-dom";
import { useEffect } from "react";

import NavBar from "../components/layout/NavBar";
import Button from "../components/ui/Button";
import HeroSection from "../components/section/HeroSection";
import ValoresSection from "../components/section/ValoresSection";
import QuemSomosSection from "../components/section/QuemSomosSection";
import QuemTransformamosSection from "../components/section/QuemTransformamosSection";
import Footer from "../components/layout/Footer";

export default function Institucional(){
  const location = useLocation();

  useEffect(() => {
    if (location.hash) {
      const element = document.querySelector(location.hash);

      if (element) {
        element.scrollIntoView({
          behavior: "smooth",
        });
      }
    }
  }, [location]);

  return (
    <div>
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
    </div>
  );
}