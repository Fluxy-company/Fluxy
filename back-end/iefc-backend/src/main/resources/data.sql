-- Seed temas
INSERT INTO tb_tema (nome) SELECT 'Data Science' WHERE NOT EXISTS (SELECT 1 FROM tb_tema WHERE nome = 'Data Science');
INSERT INTO tb_tema (nome) SELECT 'Cloud Computing' WHERE NOT EXISTS (SELECT 1 FROM tb_tema WHERE nome = 'Cloud Computing');
INSERT INTO tb_tema (nome) SELECT 'Programação' WHERE NOT EXISTS (SELECT 1 FROM tb_tema WHERE nome = 'Programação');
INSERT INTO tb_tema (nome) SELECT 'Business Intelligence' WHERE NOT EXISTS (SELECT 1 FROM tb_tema WHERE nome = 'Business Intelligence');
INSERT INTO tb_tema (nome) SELECT 'Gestão e Negócios' WHERE NOT EXISTS (SELECT 1 FROM tb_tema WHERE nome = 'Gestão e Negócios');

-- Seed courses
INSERT INTO tb_curso (titulo, descricao, instrutor, video_id, id_tema)
SELECT 'Introdução à Ciência de Dados', 'Fundamentos de ciência de dados: coleta, limpeza, análise e visualização de dados para tomada de decisões.', 'Dr. Daniel Silva', 'ua-CiDNNj30',
       (SELECT tema_id FROM tb_tema WHERE nome = 'Data Science')
WHERE NOT EXISTS (SELECT 1 FROM tb_curso WHERE titulo = 'Introdução à Ciência de Dados');

INSERT INTO tb_curso (titulo, descricao, instrutor, video_id, id_tema)
SELECT 'Machine Learning: Do Zero ao Deploy', 'Aprenda algoritmos de ML, treinamento de modelos e deploy em produção com Python e scikit-learn.', 'Dr. Daniel Silva', '7eh4d6sabA0',
       (SELECT tema_id FROM tb_tema WHERE nome = 'Data Science')
WHERE NOT EXISTS (SELECT 1 FROM tb_curso WHERE titulo = 'Machine Learning: Do Zero ao Deploy');

INSERT INTO tb_curso (titulo, descricao, instrutor, video_id, id_tema)
SELECT 'AWS para Iniciantes', 'Domine os serviços fundamentais da AWS: EC2, S3, RDS, Lambda e IAM.', 'Maria Fernandes', 'ulprqHHWlng',
       (SELECT tema_id FROM tb_tema WHERE nome = 'Cloud Computing')
WHERE NOT EXISTS (SELECT 1 FROM tb_curso WHERE titulo = 'AWS para Iniciantes');

INSERT INTO tb_curso (titulo, descricao, instrutor, video_id, id_tema)
SELECT 'Docker e Kubernetes na Prática', 'Containerização e orquestração de aplicações com Docker e Kubernetes em ambientes de produção.', 'Rafael Gomes', 'Kzcz-EVKBEQ',
       (SELECT tema_id FROM tb_tema WHERE nome = 'Cloud Computing')
WHERE NOT EXISTS (SELECT 1 FROM tb_curso WHERE titulo = 'Docker e Kubernetes na Prática');

INSERT INTO tb_curso (titulo, descricao, instrutor, video_id, id_tema)
SELECT 'Java Spring Boot: API REST Completa', 'Construa APIs REST robustas com Spring Boot, JPA, Security e documentação com Swagger.', 'Ana Beatriz Costa', 'wlYvA2b1BWI',
       (SELECT tema_id FROM tb_tema WHERE nome = 'Programação')
WHERE NOT EXISTS (SELECT 1 FROM tb_curso WHERE titulo = 'Java Spring Boot: API REST Completa');

INSERT INTO tb_curso (titulo, descricao, instrutor, video_id, id_tema)
SELECT 'React.js: Frontend Moderno', 'Desenvolva aplicações web modernas com React, hooks, context API e integração com APIs REST.', 'Pedro Almeida', 'SqcY0GlETPk',
       (SELECT tema_id FROM tb_tema WHERE nome = 'Programação')
WHERE NOT EXISTS (SELECT 1 FROM tb_curso WHERE titulo = 'React.js: Frontend Moderno');

INSERT INTO tb_curso (titulo, descricao, instrutor, video_id, id_tema)
SELECT 'Power BI: Dashboards Profissionais', 'Crie dashboards interativos e relatórios profissionais com Power BI, DAX e Power Query.', 'Juliana Martins', '3u7MQz1EyPY',
       (SELECT tema_id FROM tb_tema WHERE nome = 'Business Intelligence')
WHERE NOT EXISTS (SELECT 1 FROM tb_curso WHERE titulo = 'Power BI: Dashboards Profissionais');

INSERT INTO tb_curso (titulo, descricao, instrutor, video_id, id_tema)
SELECT 'Product Management: Do Discovery ao Delivery', 'Frameworks de discovery, priorização, roadmaps e métricas de produto para PMs.', 'Carlos Mendes', '502ILHjX9EE',
       (SELECT tema_id FROM tb_tema WHERE nome = 'Gestão e Negócios')
WHERE NOT EXISTS (SELECT 1 FROM tb_curso WHERE titulo = 'Product Management: Do Discovery ao Delivery');

-- Seed videos for "Java Spring Boot: API REST Completa" (curso_id depends on insert order, using subquery)
INSERT INTO video (titulo, url, video_id, duracao, modulo, ordem, id_curso)
SELECT 'Introdução ao Spring Boot', 'https://www.youtube.com/watch?v=wlYvA2b1BWI', 'wlYvA2b1BWI', '15:30', 'Introdução', 1,
       (SELECT curso_id FROM tb_curso WHERE titulo = 'Java Spring Boot: API REST Completa')
WHERE NOT EXISTS (SELECT 1 FROM video WHERE titulo = 'Introdução ao Spring Boot');

INSERT INTO video (titulo, url, video_id, duracao, modulo, ordem, id_curso)
SELECT 'Configurando o Projeto', 'https://www.youtube.com/watch?v=nk2CQITm_eo', 'nk2CQITm_eo', '12:45', 'Introdução', 2,
       (SELECT curso_id FROM tb_curso WHERE titulo = 'Java Spring Boot: API REST Completa')
WHERE NOT EXISTS (SELECT 1 FROM video WHERE titulo = 'Configurando o Projeto');

INSERT INTO video (titulo, url, video_id, duracao, modulo, ordem, id_curso)
SELECT 'Criando Entidades JPA', 'https://www.youtube.com/watch?v=OVjWsGL5bDo', 'OVjWsGL5bDo', '18:20', 'Módulo 1: JPA e Banco de Dados', 3,
       (SELECT curso_id FROM tb_curso WHERE titulo = 'Java Spring Boot: API REST Completa')
WHERE NOT EXISTS (SELECT 1 FROM video WHERE titulo = 'Criando Entidades JPA');

INSERT INTO video (titulo, url, video_id, duracao, modulo, ordem, id_curso)
SELECT 'Repositories e Services', 'https://www.youtube.com/watch?v=aircAruvnKk', 'aircAruvnKk', '20:10', 'Módulo 1: JPA e Banco de Dados', 4,
       (SELECT curso_id FROM tb_curso WHERE titulo = 'Java Spring Boot: API REST Completa')
WHERE NOT EXISTS (SELECT 1 FROM video WHERE titulo = 'Repositories e Services');

INSERT INTO video (titulo, url, video_id, duracao, modulo, ordem, id_curso)
SELECT 'Controllers REST', 'https://www.youtube.com/watch?v=zITIFTsivN8', 'zITIFTsivN8', '22:00', 'Módulo 2: API REST', 5,
       (SELECT curso_id FROM tb_curso WHERE titulo = 'Java Spring Boot: API REST Completa')
WHERE NOT EXISTS (SELECT 1 FROM video WHERE titulo = 'Controllers REST');

INSERT INTO video (titulo, url, video_id, duracao, modulo, ordem, id_curso)
SELECT 'Validação e DTOs', 'https://www.youtube.com/watch?v=bfmFfD2RIcg', 'bfmFfD2RIcg', '16:40', 'Módulo 2: API REST', 6,
       (SELECT curso_id FROM tb_curso WHERE titulo = 'Java Spring Boot: API REST Completa')
WHERE NOT EXISTS (SELECT 1 FROM video WHERE titulo = 'Validação e DTOs');

-- Seed videos for "React.js: Frontend Moderno"
INSERT INTO video (titulo, url, video_id, duracao, modulo, ordem, id_curso)
SELECT 'O que é React?', 'https://www.youtube.com/watch?v=SqcY0GlETPk', 'SqcY0GlETPk', '10:15', 'Introdução', 1,
       (SELECT curso_id FROM tb_curso WHERE titulo = 'React.js: Frontend Moderno')
WHERE NOT EXISTS (SELECT 1 FROM video WHERE titulo = 'O que é React?');

INSERT INTO video (titulo, url, video_id, duracao, modulo, ordem, id_curso)
SELECT 'JSX e Componentes', 'https://www.youtube.com/watch?v=Ilg3gGewQ5U', 'Ilg3gGewQ5U', '14:30', 'Introdução', 2,
       (SELECT curso_id FROM tb_curso WHERE titulo = 'React.js: Frontend Moderno')
WHERE NOT EXISTS (SELECT 1 FROM video WHERE titulo = 'JSX e Componentes');

INSERT INTO video (titulo, url, video_id, duracao, modulo, ordem, id_curso)
SELECT 'useState e useEffect', 'https://www.youtube.com/watch?v=GwIo3gDZCVQ', 'GwIo3gDZCVQ', '18:50', 'Módulo 1: Hooks', 3,
       (SELECT curso_id FROM tb_curso WHERE titulo = 'React.js: Frontend Moderno')
WHERE NOT EXISTS (SELECT 1 FROM video WHERE titulo = 'useState e useEffect');

INSERT INTO video (titulo, url, video_id, duracao, modulo, ordem, id_curso)
SELECT 'Context API', 'https://www.youtube.com/watch?v=77lMCiiMilo', '77lMCiiMilo', '20:10', 'Módulo 1: Hooks', 4,
       (SELECT curso_id FROM tb_curso WHERE titulo = 'React.js: Frontend Moderno')
WHERE NOT EXISTS (SELECT 1 FROM video WHERE titulo = 'Context API');

-- Seed videos for "AWS para Iniciantes"
INSERT INTO video (titulo, url, video_id, duracao, modulo, ordem, id_curso)
SELECT 'Visão geral de Cloud', 'https://www.youtube.com/watch?v=JIbIYCM48to', 'JIbIYCM48to', '10:15', 'Introdução', 1,
       (SELECT curso_id FROM tb_curso WHERE titulo = 'AWS para Iniciantes')
WHERE NOT EXISTS (SELECT 1 FROM video WHERE titulo = 'Visão geral de Cloud');

INSERT INTO video (titulo, url, video_id, duracao, modulo, ordem, id_curso)
SELECT 'S3 e Storage', 'https://www.youtube.com/watch?v=a9__D53WsUs', 'a9__D53WsUs', '16:00', 'Módulo 1: AWS Core', 2,
       (SELECT curso_id FROM tb_curso WHERE titulo = 'AWS para Iniciantes')
WHERE NOT EXISTS (SELECT 1 FROM video WHERE titulo = 'S3 e Storage');

INSERT INTO video (titulo, url, video_id, duracao, modulo, ordem, id_curso)
SELECT 'EC2 e Compute', 'https://www.youtube.com/watch?v=TsRBftzZP6o', 'TsRBftzZP6o', '19:45', 'Módulo 1: AWS Core', 3,
       (SELECT curso_id FROM tb_curso WHERE titulo = 'AWS para Iniciantes')
WHERE NOT EXISTS (SELECT 1 FROM video WHERE titulo = 'EC2 e Compute');
