export default function CatalogCard({ title, thumbnailUrl, instructor, totalLessons, onClick, isEnrolled, onEnroll }) {
  return (
    <div
      onClick={onClick}
      className="bg-white overflow-hidden shadow-sm hover:shadow-md transition-shadow cursor-pointer group"
    >
      <div className="relative w-full aspect-video bg-gray-100 overflow-hidden">
        {thumbnailUrl ? (
          <img
            src={thumbnailUrl}
            alt={title}
            className="w-full h-full object-cover group-hover:scale-105 transition-transform duration-300"
          />
        ) : (
          <div className="w-full h-full flex items-center justify-center text-gray-400">
            Sem thumbnail
          </div>
        )}

      </div>

      <div className="p-4 flex flex-col gap-2">
        <h3 className="text-sm font-semibold text-[#1e2d3d] leading-snug line-clamp-2">{title}</h3>

        <p className="text-xs text-gray-500">{instructor}</p>

        <div className="flex items-center gap-3 text-xs text-gray-400 mt-1">
          <span className="flex items-center gap-1">
            <svg className="w-3.5 h-3.5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M12 6.253v13m0-13C10.832 5.477 9.246 5 7.5 5S4.168 5.477 3 6.253v13C4.168 18.477 5.754 18 7.5 18s3.332.477 4.5 1.253m0-13C13.168 5.477 14.754 5 16.5 5c1.747 0 3.332.477 4.5 1.253v13C19.832 18.477 18.247 18 16.5 18c-1.746 0-3.332.477-4.5 1.253" />
            </svg>
            {totalLessons} aulas
          </span>
        </div>

        {!isEnrolled && onEnroll && (
          <button
            onClick={(e) => {
              e.stopPropagation();
              onEnroll();
            }}
            className="w-full mt-2 py-2 !text-xs font-semibold !bg-[#1e2d3d] text-white hover:!bg-[#2a3f52] transition-colors cursor-pointer"
          >
            Inscrever-se
          </button>
        )}
        {isEnrolled && (
          <div className="w-full mt-2 py-2 text-center !text-xs font-semibold text-[#1e2d3d] bg-gray-100">
            <span className="flex items-center justify-center gap-1">
              <svg className="w-3.5 h-3.5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2.5} d="M5 13l4 4L19 7" />
              </svg>
              Inscrito
            </span>
          </div>
        )}
      </div>
    </div>
  );
}
