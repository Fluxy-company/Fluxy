import { useState, useEffect, useMemo } from "react";
import { useNavigate } from "react-router-dom";
import DashboardLayout from "../components/layout/DashboardLayout";
import CatalogCard from "../components/ui/CatalogCard";
import { fetchCursos, cursoToCatalogFormat, fetchInscricoes, inscreverUsuario, getUserId } from "../services/api";

export default function Cursos() {
  const [activeCategory, setActiveCategory] = useState("Todos");
  const [search, setSearch] = useState("");
  const [courses, setCourses] = useState([]);
  const [enrolledIds, setEnrolledIds] = useState(new Set());
  const [loading, setLoading] = useState(true);
  const navigate = useNavigate();

  const handleEnroll = async (courseId) => {
    const userId = getUserId();
    if (!userId) {
      navigate("/login");
      return;
    }
    try {
      await inscreverUsuario(userId, courseId);
      setEnrolledIds((prev) => new Set([...prev, courseId]));
    } catch (err) {
      console.error("Erro ao inscrever:", err);
    }
  };

  useEffect(() => {
    setLoading(true);
    const userId = getUserId();

    const cursosPromise = fetchCursos().then((data) => data.map(cursoToCatalogFormat)).catch(() => []);
    const inscricoesPromise = userId
      ? fetchInscricoes(userId).then((list) => new Set(list.map((i) => i.curso.id))).catch(() => new Set())
      : Promise.resolve(new Set());

    Promise.all([cursosPromise, inscricoesPromise])
      .then(([cursosData, enrolledSet]) => {
        setCourses(cursosData);
        setEnrolledIds(enrolledSet);
      })
      .finally(() => setLoading(false));
  }, []);

  const categories = useMemo(() => {
    const cats = [...new Set(courses.map((c) => c.category))];
    return ["Todos", ...cats];
  }, [courses]);

  const filtered = courses.filter((course) => {
    const matchCategory = activeCategory === "Todos" || course.category === activeCategory;
    const matchSearch =
      !search ||
      course.title.toLowerCase().includes(search.toLowerCase()) ||
      course.instructor.toLowerCase().includes(search.toLowerCase()) ||
      course.category.toLowerCase().includes(search.toLowerCase());
    return matchCategory && matchSearch;
  });

  const featured = courses.slice(0, 3);

  // Group by category for the "Todos" view
  const grouped = {};
  for (const course of filtered) {
    if (!grouped[course.category]) grouped[course.category] = [];
    grouped[course.category].push(course);
  }

  const topFeatured = featured[0];
  const topFeaturedThumb = topFeatured && topFeatured.videoId
    ? `https://img.youtube.com/vi/${topFeatured.videoId}/hqdefault.jpg`
    : null;

  if (loading) {
    return (
      <DashboardLayout>
        <div className="flex items-center justify-center min-h-[400px]">
          <p className="text-gray-500 text-lg">Carregando cursos...</p>
        </div>
      </DashboardLayout>
    );
  }

  return (
    <DashboardLayout>
      {/* Page Header */}
      <div className="mb-6">
        <h1 className="text-3xl font-bold text-[#1e2d3d]">Cursos</h1>
        <p className="text-gray-500 mt-1">
          Explore nosso catálogo e encontre o curso ideal para o seu crescimento.
        </p>
      </div>

      {/* Search bar */}
      <div className="relative mb-6">
        <input
          type="text"
          placeholder="Buscar cursos, instrutores ou temas..."
          value={search}
          onChange={(e) => setSearch(e.target.value)}
          className="w-full border border-gray-200 px-4 py-3 pl-10 !text-sm text-gray-700 placeholder-gray-400 focus:outline-none focus:ring-1 focus:ring-blue-500 !bg-white"
        />
        <svg
          className="absolute left-3 top-1/2 -translate-y-1/2 w-4 h-4 text-gray-400"
          fill="none"
          stroke="currentColor"
          viewBox="0 0 24 24"
        >
          <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M21 21l-6-6m2-5a7 7 0 11-14 0 7 7 0 0114 0z" />
        </svg>
      </div>

      {/* Category Filter */}
      <div className="flex gap-3 flex-wrap mb-8">
        {categories.map((cat) => (
          <button
            key={cat}
            onClick={() => setActiveCategory(cat)}
            className={`px-4 py-2 !text-sm font-medium transition-colors cursor-pointer ${
              activeCategory === cat
                ? "!bg-[#1e2d3d] text-white"
                : "!bg-transparent !text-gray-500 hover:!text-gray-700 border border-gray-200"
            }`}
          >
            {cat}
          </button>
        ))}
      </div>

      {/* Course Grid — grouped by category or flat */}
      {activeCategory === "Todos" ? (
        Object.entries(grouped).map(([category, courses]) => (
          <div key={category} className="mb-10">
            <h2 className="text-xl font-bold text-[#1e2d3d] mb-4">{category}</h2>
            <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 gap-6">
              {courses.map((course) => (
                <CatalogCard
                  key={course.id}
                  title={course.title}
                  thumbnailUrl={course.videoId ? `https://img.youtube.com/vi/${course.videoId}/hqdefault.jpg` : null}
                  instructor={course.instructor}
                  totalLessons={course.totalLessons}
                  isEnrolled={enrolledIds.has(course.id)}
                  onEnroll={() => handleEnroll(course.id)}
                  onClick={() => navigate(`/curso/${course.id}`)}
                />
              ))}
            </div>
          </div>
        ))
      ) : (
        <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 gap-6 mb-10">
          {filtered.map((course) => (
            <CatalogCard
              key={course.id}
              title={course.title}
              thumbnailUrl={course.videoId ? `https://img.youtube.com/vi/${course.videoId}/hqdefault.jpg` : null}
              instructor={course.instructor}
              totalLessons={course.totalLessons}
              isEnrolled={enrolledIds.has(course.id)}
              onEnroll={() => handleEnroll(course.id)}
              onClick={() => navigate(`/curso/${course.id}`)}
            />
          ))}
          {filtered.length === 0 && (
            <p className="text-gray-400 col-span-full text-center py-12">
              Nenhum curso encontrado para esta busca.
            </p>
          )}
        </div>
      )}

      {/* Recommended / Featured Section */}
      <div className="mb-12">
        <h2 className="text-2xl font-bold text-[#1e2d3d] mb-1">Em Destaque</h2>
        <p className="text-gray-500 text-sm mb-6">
          Os cursos mais populares da plataforma.
        </p>

        <div className="grid grid-cols-1 lg:grid-cols-5 gap-6">
          {/* Featured big card */}
          {topFeatured && (
            <div
              className="lg:col-span-3 relative overflow-hidden group cursor-pointer"
              onClick={() => navigate(`/curso/${topFeatured.id}`)}
            >
              {topFeaturedThumb && (
                <img
                  src={topFeaturedThumb}
                  alt={topFeatured.title}
                  className="w-full h-full object-cover min-h-[320px] group-hover:scale-105 transition-transform duration-300"
                />
              )}
              <div className="absolute inset-0 bg-gradient-to-t from-black/90 via-black/40 to-transparent" />
              <div className="absolute bottom-0 left-0 p-6">
                <span className="text-xs uppercase tracking-widest text-blue-300 font-semibold">
                  Mais Popular
                </span>
                <h3 className="text-2xl font-bold text-white mt-2 leading-tight">
                  {topFeatured.title}
                </h3>
                <p className="text-gray-300 text-sm mt-2 max-w-md">{topFeatured.description}</p>
                <div className="flex items-center gap-4 mt-4">
                  <span className="!bg-white text-gray-900 !text-sm font-semibold px-5 py-2 inline-block">
                    Ver curso
                  </span>
                </div>
              </div>
            </div>
          )}

          {/* Side featured cards */}
          <div className="lg:col-span-2 flex flex-col gap-6">
            {featured.slice(1, 3).map((course) => (
              <div
                key={course.id}
                className="bg-white border border-gray-200 p-5 flex gap-4 cursor-pointer hover:shadow-sm transition-shadow"
                onClick={() => navigate(`/curso/${course.id}`)}
              >
                <img
                  src={course.videoId ? `https://img.youtube.com/vi/${course.videoId}/hqdefault.jpg` : ''}
                  alt={course.title}
                  className="w-24 h-16 object-cover shrink-0"
                />
                <div className="flex flex-col gap-1 min-w-0">
                  <h3 className="text-sm font-semibold text-[#1e2d3d] leading-snug">{course.title}</h3>
                  <p className="text-xs text-gray-500">{course.instructor}</p>
                  <p className="text-xs text-gray-400">
                    {course.totalLessons} aulas
                  </p>
                </div>
              </div>
            ))}
          </div>
        </div>
      </div>
    </DashboardLayout>
  );
}
