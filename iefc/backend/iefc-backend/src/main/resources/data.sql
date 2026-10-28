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

INSERT INTO tb_tema (nome) SELECT 'Física' WHERE NOT EXISTS (SELECT 1 FROM tb_tema WHERE nome = 'Física');
INSERT INTO tb_tema (nome) SELECT 'Astronomia' WHERE NOT EXISTS (SELECT 1 FROM tb_tema WHERE nome = 'Astronomia');
INSERT INTO tb_tema (nome) SELECT 'Química' WHERE NOT EXISTS (SELECT 1 FROM tb_tema WHERE nome = 'Química');
INSERT INTO tb_tema (nome) SELECT 'Biologia' WHERE NOT EXISTS (SELECT 1 FROM tb_tema WHERE nome = 'Biologia');
INSERT INTO tb_tema (nome) SELECT 'Matemática' WHERE NOT EXISTS (SELECT 1 FROM tb_tema WHERE nome = 'Matemática');

INSERT INTO tb_curso (titulo, descricao, instrutor, video_id, id_tema)
SELECT 'Física Básica: Mecânica Clássica', 'Cinemática, leis de Newton, trabalho, energia e quantidade de movimento com exemplos do dia a dia.', 'Dra. Helena Duarte', 'W9fnE9NdFzo',
       (SELECT tema_id FROM tb_tema WHERE nome = 'Física')
WHERE NOT EXISTS (SELECT 1 FROM tb_curso WHERE titulo = 'Física Básica: Mecânica Clássica');

INSERT INTO tb_curso (titulo, descricao, instrutor, video_id, id_tema)
SELECT 'Eletricidade e Magnetismo', 'Carga elétrica, campo elétrico, circuitos, ímãs e indução eletromagnética de forma intuitiva.', 'Prof. Marcelo Tavares', 'ipbBPqUEZjc',
       (SELECT tema_id FROM tb_tema WHERE nome = 'Física')
WHERE NOT EXISTS (SELECT 1 FROM tb_curso WHERE titulo = 'Eletricidade e Magnetismo');

INSERT INTO tb_curso (titulo, descricao, instrutor, video_id, id_tema)
SELECT 'Termodinâmica e Ondas', 'Calor, temperatura, leis da termodinâmica, ondas sonoras e luminosas aplicadas ao cotidiano.', 'Dra. Helena Duarte', 'U_2AJc1mcas',
       (SELECT tema_id FROM tb_tema WHERE nome = 'Física')
WHERE NOT EXISTS (SELECT 1 FROM tb_curso WHERE titulo = 'Termodinâmica e Ondas');

INSERT INTO tb_curso (titulo, descricao, instrutor, video_id, id_tema)
SELECT 'Introdução à Física Quântica', 'Dualidade onda-partícula, princípio da incerteza e as ideias que mudaram a física no século XX.', 'Prof. Rodrigo Nascimento', 'sxorxWEgmB8',
       (SELECT tema_id FROM tb_tema WHERE nome = 'Física')
WHERE NOT EXISTS (SELECT 1 FROM tb_curso WHERE titulo = 'Introdução à Física Quântica');

INSERT INTO tb_curso (titulo, descricao, instrutor, video_id, id_tema)
SELECT 'Astronomia para Iniciantes', 'Sistema Solar, estrelas, galáxias e como observar o céu a olho nu e com telescópios simples.', 'Dra. Camila Rezende', '3bE0NZVRwNo',
       (SELECT tema_id FROM tb_tema WHERE nome = 'Astronomia')
WHERE NOT EXISTS (SELECT 1 FROM tb_curso WHERE titulo = 'Astronomia para Iniciantes');

INSERT INTO tb_curso (titulo, descricao, instrutor, video_id, id_tema)
SELECT 'Cosmologia: Big Bang e Buracos Negros', 'Origem e evolução do Universo, relatividade geral, buracos negros e ondas gravitacionais.', 'Dra. Camila Rezende', 'Bl8Q7Lt56y0',
       (SELECT tema_id FROM tb_tema WHERE nome = 'Astronomia')
WHERE NOT EXISTS (SELECT 1 FROM tb_curso WHERE titulo = 'Cosmologia: Big Bang e Buracos Negros');

INSERT INTO tb_curso (titulo, descricao, instrutor, video_id, id_tema)
SELECT 'Química Geral', 'Átomos, ligações químicas, tabela periódica, reações e estequiometria com experimentos simples.', 'Prof. Fernando Lopes', 'XCZyx2IHdxA',
       (SELECT tema_id FROM tb_tema WHERE nome = 'Química')
WHERE NOT EXISTS (SELECT 1 FROM tb_curso WHERE titulo = 'Química Geral');

INSERT INTO tb_curso (titulo, descricao, instrutor, video_id, id_tema)
SELECT 'Biologia Celular e Genética', 'Estrutura da célula, DNA, hereditariedade e noções de biotecnologia e edição genética.', 'Dra. Patrícia Moura', '4FsLnuELY_E',
       (SELECT tema_id FROM tb_tema WHERE nome = 'Biologia')
WHERE NOT EXISTS (SELECT 1 FROM tb_curso WHERE titulo = 'Biologia Celular e Genética');

INSERT INTO tb_curso (titulo, descricao, instrutor, video_id, id_tema)
SELECT 'Matemática para a Ciência', 'Funções, vetores, derivadas e integrais explicadas com aplicações em física e engenharia.', 'Prof. Rodrigo Nascimento', 'GWwJ4wsFqCQ',
       (SELECT tema_id FROM tb_tema WHERE nome = 'Matemática')
WHERE NOT EXISTS (SELECT 1 FROM tb_curso WHERE titulo = 'Matemática para a Ciência');

INSERT INTO tb_curso (titulo, descricao, instrutor, video_id, id_tema)
SELECT 'Método Científico e Divulgação da Ciência', 'Como formular hipóteses, planejar experimentos, ler artigos e comunicar ciência ao público.', 'Dra. Patrícia Moura', 'dWMREFKZ-2k',
       (SELECT tema_id FROM tb_tema WHERE nome = 'Biologia')
WHERE NOT EXISTS (SELECT 1 FROM tb_curso WHERE titulo = 'Método Científico e Divulgação da Ciência');

INSERT INTO video (titulo, url, video_id, duracao, modulo, ordem, id_curso)
SELECT 'As 3 LEIS DE NEWTON: Resumo em 5 Minutos!', 'https://www.youtube.com/watch?v=W9fnE9NdFzo', 'W9fnE9NdFzo', '5:03', 'Aulas', 1,
       (SELECT curso_id FROM tb_curso WHERE titulo = 'Física Básica: Mecânica Clássica')
WHERE NOT EXISTS (SELECT 1 FROM video WHERE video_id = 'W9fnE9NdFzo' AND id_curso = (SELECT curso_id FROM tb_curso WHERE titulo = 'Física Básica: Mecânica Clássica'));

INSERT INTO video (titulo, url, video_id, duracao, modulo, ordem, id_curso)
SELECT 'Aula 1.1 - O que é a Mecânica Clássica', 'https://www.youtube.com/watch?v=Wm2K8z5wpto', 'Wm2K8z5wpto', '18:53', 'Aulas', 2,
       (SELECT curso_id FROM tb_curso WHERE titulo = 'Física Básica: Mecânica Clássica')
WHERE NOT EXISTS (SELECT 1 FROM video WHERE video_id = 'Wm2K8z5wpto' AND id_curso = (SELECT curso_id FROM tb_curso WHERE titulo = 'Física Básica: Mecânica Clássica'));

INSERT INTO video (titulo, url, video_id, duracao, modulo, ordem, id_curso)
SELECT '01: Mec. Clássica - Cinemática - Vetor Posição e Deslocamento - Movimento Tridimensional', 'https://www.youtube.com/watch?v=TM_d0ehbRFc', 'TM_d0ehbRFc', '9:13', 'Aulas', 3,
       (SELECT curso_id FROM tb_curso WHERE titulo = 'Física Básica: Mecânica Clássica')
WHERE NOT EXISTS (SELECT 1 FROM video WHERE video_id = 'TM_d0ehbRFc' AND id_curso = (SELECT curso_id FROM tb_curso WHERE titulo = 'Física Básica: Mecânica Clássica'));

INSERT INTO video (titulo, url, video_id, duracao, modulo, ordem, id_curso)
SELECT '02: Mec. Clássica - Exemplo - Vetor Posição e Deslocamento - Mov. Tridimensional e Bidimensional', 'https://www.youtube.com/watch?v=mK-GOWCfgNQ', 'mK-GOWCfgNQ', '12:39', 'Aulas', 4,
       (SELECT curso_id FROM tb_curso WHERE titulo = 'Física Básica: Mecânica Clássica')
WHERE NOT EXISTS (SELECT 1 FROM video WHERE video_id = 'mK-GOWCfgNQ' AND id_curso = (SELECT curso_id FROM tb_curso WHERE titulo = 'Física Básica: Mecânica Clássica'));

INSERT INTO video (titulo, url, video_id, duracao, modulo, ordem, id_curso)
SELECT 'Apresentação do Curso - Aula 0 - Eletricidade e Magnetismo para Graduandos', 'https://www.youtube.com/watch?v=ipbBPqUEZjc', 'ipbBPqUEZjc', '3:09', 'Aulas', 1,
       (SELECT curso_id FROM tb_curso WHERE titulo = 'Eletricidade e Magnetismo')
WHERE NOT EXISTS (SELECT 1 FROM video WHERE video_id = 'ipbBPqUEZjc' AND id_curso = (SELECT curso_id FROM tb_curso WHERE titulo = 'Eletricidade e Magnetismo'));

INSERT INTO video (titulo, url, video_id, duracao, modulo, ordem, id_curso)
SELECT 'Conceitos iniciais - Aula 1 - Eletricidade e Magnetismo para graduandos', 'https://www.youtube.com/watch?v=I2r-u2ek1YM', 'I2r-u2ek1YM', '37:50', 'Aulas', 2,
       (SELECT curso_id FROM tb_curso WHERE titulo = 'Eletricidade e Magnetismo')
WHERE NOT EXISTS (SELECT 1 FROM video WHERE video_id = 'I2r-u2ek1YM' AND id_curso = (SELECT curso_id FROM tb_curso WHERE titulo = 'Eletricidade e Magnetismo'));

INSERT INTO video (titulo, url, video_id, duracao, modulo, ordem, id_curso)
SELECT 'Eletromagnetismo - Informações gerais e conceitos prévios - Aula 1.1', 'https://www.youtube.com/watch?v=NB591L2HYr0', 'NB591L2HYr0', '16:47', 'Aulas', 3,
       (SELECT curso_id FROM tb_curso WHERE titulo = 'Eletricidade e Magnetismo')
WHERE NOT EXISTS (SELECT 1 FROM video WHERE video_id = 'NB591L2HYr0' AND id_curso = (SELECT curso_id FROM tb_curso WHERE titulo = 'Eletricidade e Magnetismo'));

INSERT INTO video (titulo, url, video_id, duracao, modulo, ordem, id_curso)
SELECT 'A Primeira Lei da Termodinâmica Explicada', 'https://www.youtube.com/watch?v=U_2AJc1mcas', 'U_2AJc1mcas', '13:23', 'Aulas', 1,
       (SELECT curso_id FROM tb_curso WHERE titulo = 'Termodinâmica e Ondas')
WHERE NOT EXISTS (SELECT 1 FROM video WHERE video_id = 'U_2AJc1mcas' AND id_curso = (SELECT curso_id FROM tb_curso WHERE titulo = 'Termodinâmica e Ondas'));

INSERT INTO video (titulo, url, video_id, duracao, modulo, ordem, id_curso)
SELECT 'Ondas: O Que São e Como Funcionam 🌊', 'https://www.youtube.com/watch?v=c-SFJSRnsKk', 'c-SFJSRnsKk', '7:17', 'Aulas', 2,
       (SELECT curso_id FROM tb_curso WHERE titulo = 'Termodinâmica e Ondas')
WHERE NOT EXISTS (SELECT 1 FROM video WHERE video_id = 'c-SFJSRnsKk' AND id_curso = (SELECT curso_id FROM tb_curso WHERE titulo = 'Termodinâmica e Ondas'));

INSERT INTO video (titulo, url, video_id, duracao, modulo, ordem, id_curso)
SELECT 'Física Quântica Para Iniciantes (A MELHOR Explicação!)', 'https://www.youtube.com/watch?v=sxorxWEgmB8', 'sxorxWEgmB8', '6:36', 'Aulas', 1,
       (SELECT curso_id FROM tb_curso WHERE titulo = 'Introdução à Física Quântica')
WHERE NOT EXISTS (SELECT 1 FROM video WHERE video_id = 'sxorxWEgmB8' AND id_curso = (SELECT curso_id FROM tb_curso WHERE titulo = 'Introdução à Física Quântica'));

INSERT INTO video (titulo, url, video_id, duracao, modulo, ordem, id_curso)
SELECT 'Conheça o SISTEMA SOLAR e seus Planetas', 'https://www.youtube.com/watch?v=3bE0NZVRwNo', '3bE0NZVRwNo', '12:40', 'Aulas', 1,
       (SELECT curso_id FROM tb_curso WHERE titulo = 'Astronomia para Iniciantes')
WHERE NOT EXISTS (SELECT 1 FROM video WHERE video_id = '3bE0NZVRwNo' AND id_curso = (SELECT curso_id FROM tb_curso WHERE titulo = 'Astronomia para Iniciantes'));

INSERT INTO video (titulo, url, video_id, duracao, modulo, ordem, id_curso)
SELECT 'A origem do universo | Teoria do BIG BANG', 'https://www.youtube.com/watch?v=Bl8Q7Lt56y0', 'Bl8Q7Lt56y0', '19:03', 'Aulas', 1,
       (SELECT curso_id FROM tb_curso WHERE titulo = 'Cosmologia: Big Bang e Buracos Negros')
WHERE NOT EXISTS (SELECT 1 FROM video WHERE video_id = 'Bl8Q7Lt56y0' AND id_curso = (SELECT curso_id FROM tb_curso WHERE titulo = 'Cosmologia: Big Bang e Buracos Negros'));

INSERT INTO video (titulo, url, video_id, duracao, modulo, ordem, id_curso)
SELECT 'Buracos Negros Explicados', 'https://www.youtube.com/watch?v=GbJJRsS6OR4', 'GbJJRsS6OR4', '10:03', 'Aulas', 2,
       (SELECT curso_id FROM tb_curso WHERE titulo = 'Cosmologia: Big Bang e Buracos Negros')
WHERE NOT EXISTS (SELECT 1 FROM video WHERE video_id = 'GbJJRsS6OR4' AND id_curso = (SELECT curso_id FROM tb_curso WHERE titulo = 'Cosmologia: Big Bang e Buracos Negros'));

INSERT INTO video (titulo, url, video_id, duracao, modulo, ordem, id_curso)
SELECT 'Universo O Enigma dos Buracos Negros  Documentário dublado em português', 'https://www.youtube.com/watch?v=Ud0p0h6-j1k', 'Ud0p0h6-j1k', '1:06:05', 'Aulas', 3,
       (SELECT curso_id FROM tb_curso WHERE titulo = 'Cosmologia: Big Bang e Buracos Negros')
WHERE NOT EXISTS (SELECT 1 FROM video WHERE video_id = 'Ud0p0h6-j1k' AND id_curso = (SELECT curso_id FROM tb_curso WHERE titulo = 'Cosmologia: Big Bang e Buracos Negros'));

INSERT INTO video (titulo, url, video_id, duracao, modulo, ordem, id_curso)
SELECT 'INTRODUÇÃO à QUÍMICA: Massa, Volume, Densidade, Estados Físicos, Transformações| Aula 01 (Química I)', 'https://www.youtube.com/watch?v=XCZyx2IHdxA', 'XCZyx2IHdxA', '1:27:53', 'Aulas', 1,
       (SELECT curso_id FROM tb_curso WHERE titulo = 'Química Geral')
WHERE NOT EXISTS (SELECT 1 FROM video WHERE video_id = 'XCZyx2IHdxA' AND id_curso = (SELECT curso_id FROM tb_curso WHERE titulo = 'Química Geral'));

INSERT INTO video (titulo, url, video_id, duracao, modulo, ordem, id_curso)
SELECT 'SUBSTÂNCIAS Simples e Compostas, GRÁFICOS de Aquecimento, Tipos de MISTURAS | Aula 02 (Química I)', 'https://www.youtube.com/watch?v=yqZ6mVvZDmo', 'yqZ6mVvZDmo', '1:05:00', 'Aulas', 2,
       (SELECT curso_id FROM tb_curso WHERE titulo = 'Química Geral')
WHERE NOT EXISTS (SELECT 1 FROM video WHERE video_id = 'yqZ6mVvZDmo' AND id_curso = (SELECT curso_id FROM tb_curso WHERE titulo = 'Química Geral'));

INSERT INTO video (titulo, url, video_id, duracao, modulo, ordem, id_curso)
SELECT 'INTRODUÇÃO À QUÍMICA - CONCEITOS FUNDAMENTAIS: Matéria Energia Substâncias Puras e Misturas -Vídeo 1', 'https://www.youtube.com/watch?v=bbT9EDQJnIA', 'bbT9EDQJnIA', '7:29', 'Aulas', 3,
       (SELECT curso_id FROM tb_curso WHERE titulo = 'Química Geral')
WHERE NOT EXISTS (SELECT 1 FROM video WHERE video_id = 'bbT9EDQJnIA' AND id_curso = (SELECT curso_id FROM tb_curso WHERE titulo = 'Química Geral'));

INSERT INTO video (titulo, url, video_id, duracao, modulo, ordem, id_curso)
SELECT 'Introdução à Biologia Celular (Citologia) - Brasil Escola', 'https://www.youtube.com/watch?v=4FsLnuELY_E', '4FsLnuELY_E', '14:15', 'Aulas', 1,
       (SELECT curso_id FROM tb_curso WHERE titulo = 'Biologia Celular e Genética')
WHERE NOT EXISTS (SELECT 1 FROM video WHERE video_id = '4FsLnuELY_E' AND id_curso = (SELECT curso_id FROM tb_curso WHERE titulo = 'Biologia Celular e Genética'));

INSERT INTO video (titulo, url, video_id, duracao, modulo, ordem, id_curso)
SELECT 'DNA E RNA - ÁCIDOS NUCLEICOS - BIOQUÍMICA | Biologia com Samuel Cunha', 'https://www.youtube.com/watch?v=LFqaBPFXAh0', 'LFqaBPFXAh0', '38:19', 'Aulas', 2,
       (SELECT curso_id FROM tb_curso WHERE titulo = 'Biologia Celular e Genética')
WHERE NOT EXISTS (SELECT 1 FROM video WHERE video_id = 'LFqaBPFXAh0' AND id_curso = (SELECT curso_id FROM tb_curso WHERE titulo = 'Biologia Celular e Genética'));

INSERT INTO video (titulo, url, video_id, duracao, modulo, ordem, id_curso)
SELECT 'Aula 1: Coeficiente angular e retas secante e tangente a uma curva', 'https://www.youtube.com/watch?v=GWwJ4wsFqCQ', 'GWwJ4wsFqCQ', '5:18', 'Aulas', 1,
       (SELECT curso_id FROM tb_curso WHERE titulo = 'Matemática para a Ciência')
WHERE NOT EXISTS (SELECT 1 FROM video WHERE video_id = 'GWwJ4wsFqCQ' AND id_curso = (SELECT curso_id FROM tb_curso WHERE titulo = 'Matemática para a Ciência'));

INSERT INTO video (titulo, url, video_id, duracao, modulo, ordem, id_curso)
SELECT 'Aula 2: Funções e taxas de variação', 'https://www.youtube.com/watch?v=SlOuJU5ROW0', 'SlOuJU5ROW0', '5:04', 'Aulas', 2,
       (SELECT curso_id FROM tb_curso WHERE titulo = 'Matemática para a Ciência')
WHERE NOT EXISTS (SELECT 1 FROM video WHERE video_id = 'SlOuJU5ROW0' AND id_curso = (SELECT curso_id FROM tb_curso WHERE titulo = 'Matemática para a Ciência'));

INSERT INTO video (titulo, url, video_id, duracao, modulo, ordem, id_curso)
SELECT 'CÁLCULO I #0 - Apresentação do Curso de Cálculo Diferencial e Integral | Matemática Rio', 'https://www.youtube.com/watch?v=iMZzpO54yY0', 'iMZzpO54yY0', '9:08', 'Aulas', 3,
       (SELECT curso_id FROM tb_curso WHERE titulo = 'Matemática para a Ciência')
WHERE NOT EXISTS (SELECT 1 FROM video WHERE video_id = 'iMZzpO54yY0' AND id_curso = (SELECT curso_id FROM tb_curso WHERE titulo = 'Matemática para a Ciência'));

INSERT INTO video (titulo, url, video_id, duracao, modulo, ordem, id_curso)
SELECT 'RESUMO Sobre MÉTODO CIENTÍFICO | Biologia com Samuel Cunha', 'https://www.youtube.com/watch?v=dWMREFKZ-2k', 'dWMREFKZ-2k', '7:23', 'Aulas', 1,
       (SELECT curso_id FROM tb_curso WHERE titulo = 'Método Científico e Divulgação da Ciência')
WHERE NOT EXISTS (SELECT 1 FROM video WHERE video_id = 'dWMREFKZ-2k' AND id_curso = (SELECT curso_id FROM tb_curso WHERE titulo = 'Método Científico e Divulgação da Ciência'));

INSERT INTO video (titulo, url, video_id, duracao, modulo, ordem, id_curso)
SELECT 'COMO LER UM ARTIGO CIENTÍFICO DE MANEIRA EFICIENTE', 'https://www.youtube.com/watch?v=O6IrJu6jcxE', 'O6IrJu6jcxE', '12:34', 'Aulas', 2,
       (SELECT curso_id FROM tb_curso WHERE titulo = 'Método Científico e Divulgação da Ciência')
WHERE NOT EXISTS (SELECT 1 FROM video WHERE video_id = 'O6IrJu6jcxE' AND id_curso = (SELECT curso_id FROM tb_curso WHERE titulo = 'Método Científico e Divulgação da Ciência'));

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

INSERT INTO video (titulo, url, video_id, duracao, modulo, ordem, id_curso)
SELECT 'Fundamentos de Ciência de Dados', 'https://www.youtube.com/watch?v=ua-CiDNNj30', 'ua-CiDNNj30', '18:20', 'Módulo 1: Fundamentos', 1,
       (SELECT curso_id FROM tb_curso WHERE titulo = 'Introdução à Ciência de Dados')
WHERE NOT EXISTS (SELECT 1 FROM video WHERE video_id = 'ua-CiDNNj30' AND id_curso = (SELECT curso_id FROM tb_curso WHERE titulo = 'Introdução à Ciência de Dados'));

INSERT INTO video (titulo, url, video_id, duracao, modulo, ordem, id_curso)
SELECT 'Introdução ao Machine Learning com Python', 'https://www.youtube.com/watch?v=7eh4d6sabA0', '7eh4d6sabA0', '22:15', 'Módulo 1: Algoritmos de ML', 1,
       (SELECT curso_id FROM tb_curso WHERE titulo = 'Machine Learning: Do Zero ao Deploy')
WHERE NOT EXISTS (SELECT 1 FROM video WHERE video_id = '7eh4d6sabA0' AND id_curso = (SELECT curso_id FROM tb_curso WHERE titulo = 'Machine Learning: Do Zero ao Deploy'));

INSERT INTO video (titulo, url, video_id, duracao, modulo, ordem, id_curso)
SELECT 'Containers com Docker e Kubernetes na Prática', 'https://www.youtube.com/watch?v=Kzcz-EVKBEQ', 'Kzcz-EVKBEQ', '19:40', 'Módulo 1: Containers', 1,
       (SELECT curso_id FROM tb_curso WHERE titulo = 'Docker e Kubernetes na Prática')
WHERE NOT EXISTS (SELECT 1 FROM video WHERE video_id = 'Kzcz-EVKBEQ' AND id_curso = (SELECT curso_id FROM tb_curso WHERE titulo = 'Docker e Kubernetes na Prática'));

INSERT INTO video (titulo, url, video_id, duracao, modulo, ordem, id_curso)
SELECT 'Construindo Dashboards no Power BI', 'https://www.youtube.com/watch?v=3u7MQz1EyPY', '3u7MQz1EyPY', '24:10', 'Módulo 1: Visualização de Dados', 1,
       (SELECT curso_id FROM tb_curso WHERE titulo = 'Power BI: Dashboards Profissionais')
WHERE NOT EXISTS (SELECT 1 FROM video WHERE video_id = '3u7MQz1EyPY' AND id_curso = (SELECT curso_id FROM tb_curso WHERE titulo = 'Power BI: Dashboards Profissionais'));

INSERT INTO video (titulo, url, video_id, duracao, modulo, ordem, id_curso)
SELECT 'Do Discovery ao Delivery em Produtos Digitais', 'https://www.youtube.com/watch?v=502ILHjX9EE', '502ILHjX9EE', '17:50', 'Módulo 1: Gestão de Produto', 1,
       (SELECT curso_id FROM tb_curso WHERE titulo = 'Product Management: Do Discovery ao Delivery')
WHERE NOT EXISTS (SELECT 1 FROM video WHERE video_id = '502ILHjX9EE' AND id_curso = (SELECT curso_id FROM tb_curso WHERE titulo = 'Product Management: Do Discovery ao Delivery'));
