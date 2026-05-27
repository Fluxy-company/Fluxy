import Navbar from "../components/layout/NavBar";
import Button from "../components/ui/Button";
import HeroSection from "../components/sections/HeroSection";
import MetricaSection from "../components/sections/MetricaSection";
import OQueFazemosSection from "../components/sections/OQueFazemosSection"; 
import ParaQuemSection from "../components/sections/ParaQuemSection";
import ApoiadoresSection from "../components/sections/ApoiadoresSection";
import EventosSection from "../components/sections/EventosSection";
import Footer from "../components/layout/Footer";

export default function Institucional(){
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
      
      <HeroSection/>
      <MetricaSection />
      <OQueFazemosSection />
      <ParaQuemSection /> 
      <EventosSection /  >
      <ApoiadoresSection />
 
      <Footer />

    </div>
  ); 
}