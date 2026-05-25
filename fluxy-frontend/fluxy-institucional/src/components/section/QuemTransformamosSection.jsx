import CaseSection from "./CaseSection";
import iefcLogo from "../../assets/iefc-logo.png"
import Layout from "../layout/Layout";
import siteIcone from "../../assets/site.svg";
import instagramIcone from "../../assets/instagram.svg";


export default function QuemTransformamosSection() {

  return (
    <section className="py-8 bg-white  text-[#032738]" id="quem-transformamos">
        <Layout>
          <CaseSection 
            titulo="Quem transformamos"
            subtitulo="IEFC- Instituto Educacional Futuro da Ciência"
            descricao="Desenvolvimento de um site institucional personalizável, com uma área de cursos integrada, permitindo que a própria empresa gerencie, atualize e evolua seus conteúdos de forma autônoma."
            imagem={iefcLogo}
            links={[
              {
                label: "Site",
                href: "https://www.iefc.org.br/",
                icon: siteIcone
              },
              {
                label: "Instagram",
                href: "https://www.instagram.com/iefc_instituto/",
                icon: instagramIcone
              }
            ]}
          / >
        </Layout>
    </section>
  );
}