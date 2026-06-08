const COURSES_DATA = [
  {
    id: 1,
    title: "Análise Preditiva e Modelagem de Dados II",
    status: "em-andamento",
    progress: 85,
    meta: "Próxima aula: Redes Neurais",
    instructor: {
      name: "Dr. Daniel Silva",
      role: "PhD em Estatística e Matemática • Professor Sênior",
    },
    modules: [
      {
        title: "Introdução",
        lessons: [
          { id: "1-1-1", title: "Bases gerais", videoId: "aircAruvnKk", duration: "12:34", completed: true },
          { id: "1-1-2", title: "Análise Exploratória", videoId: "OVjWsGL5bDo", duration: "04:12", completed: true },
        ],
      },
      {
        title: "Módulo 1: Regressão Linear",
        lessons: [
          { id: "1-2-1", title: "Conceitos de Regressão", videoId: "nk2CQITm_eo", duration: "15:20", completed: true },
          { id: "1-2-2", title: "Regressão Múltipla", videoId: "zITIFTsivN8", duration: "18:45", completed: true },
        ],
      },
      {
        title: "Módulo 2: Redes Neurais",
        lessons: [
          { id: "1-3-1", title: "Introdução a Redes Neurais", videoId: "bfmFfD2RIcg", duration: "20:10", completed: false },
          { id: "1-3-2", title: "Backpropagation", videoId: "Ilg3gGewQ5U", duration: "22:30", completed: false },
        ],
      },
      {
        title: "Avaliação Final",
        lessons: [
          { id: "1-4-1", title: "Projeto Final", videoId: "GwIo3gDZCVQ", duration: "05:00", completed: false },
        ],
      },
    ],
  },
  {
    id: 2,
    title: "Arquitetura de Dados em Nuvem: AWS e Azure",
    status: "nao-iniciado",
    progress: 0,
    meta: "Inscrito em: 12 Out 2023",
    instructor: {
      name: "Maria Fernandes",
      role: "Cloud Architect • AWS Certified Solutions Architect",
    },
    modules: [
      {
        title: "Introdução",
        lessons: [
          { id: "2-1-1", title: "Visão geral de Cloud", videoId: "JIbIYCM48to", duration: "10:15", completed: false },
          { id: "2-1-2", title: "AWS vs Azure", videoId: "a9__D53WsUs", duration: "14:30", completed: false },
        ],
      },
      {
        title: "Módulo 1: AWS Fundamentals",
        lessons: [
          { id: "2-2-1", title: "S3 e Storage", videoId: "77lMCiiMilo", duration: "16:00", completed: false },
          { id: "2-2-2", title: "EC2 e Compute", videoId: "TsRBftzZP6o", duration: "19:45", completed: false },
        ],
      },
      {
        title: "Módulo 2: Azure Fundamentals",
        lessons: [
          { id: "2-3-1", title: "Azure Blob Storage", videoId: "NKEFWyqJ5XA", duration: "13:20", completed: false },
          { id: "2-3-2", title: "Azure Functions", videoId: "Vxf-rOEO1q4", duration: "17:10", completed: false },
        ],
      },
    ],
  },
  {
    id: 3,
    title: "Gestão de Projetos Ágeis para Analistas",
    status: "vitalicio",
    progress: 42,
    meta: "Visto por último: Ontem",
    instructor: {
      name: "Carlos Mendes",
      role: "Scrum Master • PMP Certified",
    },
    modules: [
      {
        title: "Introdução ao Ágil",
        lessons: [
          { id: "3-1-1", title: "O que é Agile?", videoId: "502ILHjX9EE", duration: "08:40", completed: true },
          { id: "3-1-2", title: "Scrum Framework", videoId: "9TycLR0TxFA", duration: "12:15", completed: true },
        ],
      },
      {
        title: "Módulo 1: Scrum na Prática",
        lessons: [
          { id: "3-2-1", title: "Sprint Planning", videoId: "2Vt7Ik8Ublw", duration: "14:50", completed: false },
          { id: "3-2-2", title: "Daily Standup", videoId: "xcC0LmkzG9g", duration: "09:30", completed: false },
        ],
      },
      {
        title: "Módulo 2: Kanban",
        lessons: [
          { id: "3-3-1", title: "Kanban Board", videoId: "iVaFVa7HYj4", duration: "11:20", completed: false },
          { id: "3-3-2", title: "WIP Limits", videoId: "CD0y-aU1sXo", duration: "10:00", completed: false },
        ],
      },
    ],
  },
  {
    id: 4,
    title: "Python para Engenharia de Dados: Avançado",
    status: "em-andamento",
    progress: 15,
    meta: "Módulo 2: Pipelines ETL",
    instructor: {
      name: "Ana Beatriz Costa",
      role: "Data Engineer • Python Expert",
    },
    modules: [
      {
        title: "Revisão Python",
        lessons: [
          { id: "4-1-1", title: "Python Avançado", videoId: "rfscVS0vtbw", duration: "25:00", completed: true },
          { id: "4-1-2", title: "Decorators e Generators", videoId: "swU3c34d2NQ", duration: "18:30", completed: false },
        ],
      },
      {
        title: "Módulo 1: Pandas Avançado",
        lessons: [
          { id: "4-2-1", title: "DataFrames Otimizados", videoId: "vmEHCJofslg", duration: "20:15", completed: false },
          { id: "4-2-2", title: "Merge e Join", videoId: "iYie42M1ZyU", duration: "16:40", completed: false },
        ],
      },
      {
        title: "Módulo 2: Pipelines ETL",
        lessons: [
          { id: "4-3-1", title: "Arquitetura ETL", videoId: "oF_2J3TnQBo", duration: "22:10", completed: false },
          { id: "4-3-2", title: "Apache Airflow", videoId: "AHMm1wfGuHE", duration: "28:00", completed: false },
        ],
      },
    ],
  },
];

export function getCourseById(id) {
  return COURSES_DATA.find((c) => c.id === Number(id));
}

export function getFirstUncompletedLesson(course) {
  for (const mod of course.modules) {
    for (const lesson of mod.lessons) {
      if (!lesson.completed) return lesson;
    }
  }
  return course.modules[0]?.lessons[0] || null;
}

export function getCourseThumbnail(course) {
  const firstLesson = course.modules[0]?.lessons[0];
  if (firstLesson) {
    return `https://img.youtube.com/vi/${firstLesson.videoId}/hqdefault.jpg`;
  }
  return null;
}

export default COURSES_DATA;
