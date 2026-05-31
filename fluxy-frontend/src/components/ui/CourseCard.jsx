import Badge from "./Badge";

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

function getBadgeVariant(status) {
  switch (status) {
    case "em-andamento":
      return "em-andamento";
    case "nao-iniciado":
      return "nao-iniciado";
    case "concluido":
      return "concluido";
    case "vitalicio":
      return "vitalicio";
    default:
      return "default";
  }
}

function getStatusLabel(status) {
  switch (status) {
    case "em-andamento":
      return "Em Andamento";
    case "nao-iniciado":
      return "Não Iniciado";
    case "concluido":
      return "Concluído";
    case "vitalicio":
      return "Vitalício";
    default:
      return status;
  }
}

function getActionLabel(status) {
  switch (status) {
    case "nao-iniciado":
      return "Começar";
    case "em-andamento":
    case "vitalicio":
      return "Continuar";
    case "concluido":
      return "Rever";
    default:
      return "Acessar";
  }
}

export default function CourseCard({ title, thumbnailUrl, videoUrl, status, progress, meta, onClick }) {
  const thumbnail = thumbnailUrl || getYouTubeThumbnail(videoUrl);

  return (
    <div className="bg-white overflow-hidden flex flex-row shadow-sm hover:shadow-md transition-shadow">
      <div className="relative w-44 min-h-[130px] shrink-0 bg-gray-100">
        {thumbnail ? (
          <img
            src={thumbnail}
            alt={title}
            className="w-full h-full object-cover"
          />
        ) : (
          <div className="w-full h-full flex items-center justify-center text-gray-400">
            Sem thumbnail
          </div>
        )}
      </div>

      <div className="p-4 flex flex-col gap-2 flex-1 min-w-0">
        <div className="flex items-center justify-between">
          <Badge variant={getBadgeVariant(status)}>
            {getStatusLabel(status)}
          </Badge>
          {progress !== undefined && (
            <span className="text-sm text-gray-500">{progress}% Completo</span>
          )}
        </div>

        <h3 className="text-sm font-semibold text-[#1e2d3d] leading-snug">{title}</h3>

        {meta && (
          <p className="text-xs text-gray-500 flex items-center gap-1">
            <span>⏱</span> {meta}
          </p>
        )}

        <div className="mt-auto pt-1">
          <button
            onClick={onClick}
            className="!bg-[#1e2d3d] hover:!bg-[#2a3d50] text-white text-xs font-semibold px-4 py-2 transition-colors cursor-pointer"
          >
            {getActionLabel(status)} ▸
          </button>
        </div>
      </div>
    </div>
  );
}
