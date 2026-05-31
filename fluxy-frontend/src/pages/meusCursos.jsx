import { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import DashboardLayout from "../components/layout/DashboardLayout";
import StatCard from "../components/ui/StatCard";
import TabGroup from "../components/ui/TabGroup";
import CourseCard from "../components/ui/CourseCard";
import { fetchInscricoes, fetchProgresso, fetchVideosPorCurso, getUserId } from "../services/api";

const FEATURED_VIDEO = {
  title: "Liderança de Dados e Estratégia de Negócios",
  videoUrl: "https://www.youtube.com/watch?v=X3paOmcrTjQ",
  enrolled: "4.5k alunos já inscritos",
  description: "Aprenda como alinhar estratégias de dados com objetivos de negócios. Este curso aborda governança de dados, tomada de decisão orientada a dados, e como construir uma cultura data-driven na sua organização.",
};

const PAGE_TABS = [
  { label: "Cursos", value: "cursos" },
  { label: "Conteúdos", value: "conteudos" },
  { label: "Lista de desejos", value: "lista-desejos" },
];

const FILTER_TABS = [
  { label: "Todos", value: "todos" },
  { label: "Em andamento", value: "em-andamento" },
  { label: "Finalizados", value: "finalizados" },
];

function getYouTubeThumbnail(url) {
  try {
    const regExp = /(?:youtu\.be\/|youtube\.com\/(?:watch\?v=|embed\/|shorts\/))([a-zA-Z0-9_-]{11})/;
    const match = url.match(regExp);
    if (match && match[1]) {
      return `https://img.youtube.com/vi/${match[1]}/hqdefault.jpg`;
    }
  } catch {
    // fall through
  }
  return null;
}

export default function MeusCursos() {
  const [pageTab, setPageTab] = useState("cursos");
  const [filterTab, setFilterTab] = useState("todos");
  const [coursesData, setCoursesData] = useState([]);
  const [loading, setLoading] = useState(true);
  const navigate = useNavigate();

  useEffect(() => {
    const userId = getUserId();
    if (!userId) {
      setLoading(false);
      return;
    }
    setLoading(true);
    fetchInscricoes(userId)
      .then(async (inscricoes) => {
        const cursosFormatados = await Promise.all(
          inscricoes.map(async (insc) => {
            const curso = insc.curso;
            let progress = 0;
            let status = "em-andamento";
            try {
              const [progressoData, videosData] = await Promise.all([
                fetchProgresso(userId, curso.id),
                fetchVideosPorCurso(curso.id),
              ]);
              const totalAulas = videosData.length;
              const concluidas = progressoData.filter((p) => p.concluida).length;
              progress = totalAulas > 0 ? Math.round((concluidas / totalAulas) * 100) : 0;
              if (progress === 100) status = "concluido";
            } catch {
              // keep defaults
            }
            return {
              id: curso.id,
              title: curso.titulo,
              status,
              progress,
              meta: curso.tema?.nome || "",
              thumbnailVideoId: curso.videoId || "",
            };
          })
        );
        setCoursesData(cursosFormatados);
      })
      .catch(() => {
        setCoursesData([]);
      })
      .finally(() => setLoading(false));
  }, []);

  const filteredCourses = coursesData.filter((course) => {
    if (filterTab === "todos") return true;
    if (filterTab === "em-andamento")
      return course.status === "em-andamento" || course.status === "vitalicio";
    if (filterTab === "finalizados") return course.status === "concluido";
    return true;
  });

  const stats = {
    emAndamento: coursesData.filter(
      (c) => c.status === "em-andamento" || c.status === "vitalicio"
    ).length,
    concluidos: coursesData.filter((c) => c.status === "concluido").length,
    horasEstudo: coursesData.length * 10,
  };

  const featuredThumbnail = getYouTubeThumbnail(FEATURED_VIDEO.videoUrl);

  return (
    <DashboardLayout>
      {/* Page Header */}
      <div className="mb-8">
        <h1 className="text-3xl font-bold text-[#1e2d3d]">Meus Cursos</h1>
        <p className="text-gray-500 mt-1">
          Acompanhe seu progresso e continue sua jornada de aprendizado.
        </p>
      </div>

      {/* Page Tabs */}
      <TabGroup
        tabs={PAGE_TABS}
        activeTab={pageTab}
        onTabChange={setPageTab}
        className="border-b border-gray-200 mb-8"
      />

      {/* Stats Row */}
      <div className="grid grid-cols-1 sm:grid-cols-3 gap-4 mb-8">
        <StatCard
          label="Em Andamento"
          value={String(stats.emAndamento).padStart(2, "0")}
          detail="Cursos"
        />
        <StatCard label="Concluídos" value={stats.concluidos} detail="Certificados" />
        <StatCard label="Horas de Estudo" value={stats.horasEstudo} detail="Este mês" />
      </div>

      {/* Filter Row */}
      <div className="flex items-center justify-between mb-6">
        <div className="flex gap-3">
          {FILTER_TABS.map((tab) => (
            <button
              key={tab.value}
              onClick={() => setFilterTab(tab.value)}
              className={`px-4 py-2 !text-sm font-medium transition-colors cursor-pointer ${
                filterTab === tab.value
                  ? "!bg-[#1e2d3d] text-white"
                  : "!bg-transparent !text-gray-500 hover:!text-gray-700"
              }`}
            >
              {tab.label}
            </button>
          ))}
        </div>

        <button className="flex items-center gap-2 !text-sm !text-gray-500 hover:!text-gray-700 !bg-transparent !p-0 transition-colors cursor-pointer">
          <svg className="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
            <path
              strokeLinecap="round"
              strokeLinejoin="round"
              strokeWidth={2}
              d="M3 4a1 1 0 011-1h16a1 1 0 011 1v2.586a1 1 0 01-.293.707l-6.414 6.414a1 1 0 00-.293.707V17l-4 4v-6.586a1 1 0 00-.293-.707L3.293 7.293A1 1 0 013 6.586V4z"
            />
          </svg>
          Filtrar por Data
        </button>
      </div>

      {/* Course Grid */}
      <div className="grid grid-cols-1 md:grid-cols-2 gap-6 mb-16">
        {filteredCourses.map((course) => {
          const thumb = course.thumbnailVideoId
            ? `https://img.youtube.com/vi/${course.thumbnailVideoId}/hqdefault.jpg`
            : null;
          return (
            <CourseCard
              key={course.id}
              title={course.title}
              thumbnailUrl={thumb}
              status={course.status}
              progress={course.progress}
              meta={course.meta}
              onClick={() => navigate(`/curso/${course.id}`)}
            />
          );
        })}
      </div>

      {/* Recommended Section */}
      <div className="mb-12">
        <h2 className="text-2xl font-bold text-[#1e2d3d] mb-1">Recomendado para você</h2>
        <p className="text-gray-500 text-sm mb-6">
          Cursos baseados no seu perfil de aprendizado.
        </p>

        <div className="grid grid-cols-1 lg:grid-cols-5 gap-6">
          {/* Featured Video Card */}
          <div className="lg:col-span-3 relative overflow-hidden group">
            {featuredThumbnail && (
              <img
                src={featuredThumbnail}
                alt={FEATURED_VIDEO.title}
                className="w-full h-full object-cover min-h-[320px]"
              />
            )}
            <div className="absolute inset-0 bg-gradient-to-t from-black/90 via-black/40 to-transparent" />
            <div className="absolute bottom-0 left-0 p-6">
              <span className="text-xs uppercase tracking-widest text-blue-300 font-semibold">
                Masterclass Exclusiva
              </span>
              <h3 className="text-2xl font-bold text-white mt-2 leading-tight">
                {FEATURED_VIDEO.title}
              </h3>
              <div className="flex items-center gap-4 mt-4">
                <a
                  href={FEATURED_VIDEO.videoUrl}
                  target="_blank"
                  rel="noopener noreferrer"
                  className="!bg-white text-gray-900 !text-sm font-semibold px-5 py-2 hover:!bg-gray-200 transition-colors"
                >
                  Ver detalhes
                </a>
                <span className="text-gray-300 text-sm">{FEATURED_VIDEO.enrolled}</span>
              </div>
            </div>
          </div>

          {/* Side Info Cards */}
          <div className="lg:col-span-2 flex flex-col gap-6">
            <div className="bg-white border border-gray-200 p-6 flex flex-col gap-2">
              <span className="text-xs uppercase tracking-widest text-blue-600 font-semibold">
                Próximo Objetivo
              </span>
              <h3 className="text-xl font-bold text-[#1e2d3d]">
                Certificação Data Architect Professional
              </h3>
              <a href="#" className="text-blue-600 text-sm font-medium hover:underline mt-1">
                Ver requisitos →
              </a>
            </div>

            <div className="bg-white border border-gray-200 p-6 flex flex-col gap-2 flex-1">
              <h3 className="text-xl font-bold text-[#1e2d3d]">Sobre o curso</h3>
              <p className="text-gray-500 text-sm">
                {FEATURED_VIDEO.description}
              </p>
            </div>
          </div>
        </div>
      </div>
    </DashboardLayout>
  );
}
