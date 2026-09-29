# 📋 IEFC — Documento de Contexto Completo do Projeto

> **Projeto:** IEFC — Instituto de Educação e Formação Corporativa  
> **Equipe:** Fluxy (SPTech School — Projeto Integrador)  
> **Gerado em:** 2026-09-29  
> **Stack principal:** Spring Boot 4 (Java 21) + React 18 + PostgreSQL + Redis + Docker

---

## 1. Visão Geral

O IEFC é um sistema web institucional composto por três grandes componentes:

| Componente | Tecnologia | Porta padrão |
|---|---|---|
| `iefc-institucional` | React 18 + Vite + TailwindCSS | 3000 |
| `iefc-backend` | Spring Boot 4 (Java 21) | 8080 |
| `email-service` | Spring Boot 4 (Java 21) — **Clean Arch** | 8081 |

Toda a infraestrutura é orquestrada via **Docker Compose**, com dois bancos PostgreSQL independentes (`iefc-db` e `email-db`) e um **Redis** (rate-limiting via Bucket4j/Lettuce).

---

## 2. Estrutura de Repositório

```
iefc/
├── .env                         # Variáveis de ambiente (root)
├── .gitignore
├── docker-compose.yml           # Orquestrador principal (5 serviços)
├── backend/
│   ├── .env                     # Variáveis de ambiente (backend)
│   ├── .env.example
│   ├── docker-compose.yml       # Compose auxiliar (só backend)
│   ├── iefc-backend/            # API principal (Spring Boot)
│   └── email-service/           # Microsserviço de e-mail (Clean Arch)
└── iefc-institucional/          # Frontend institucional (React/Vite)
```

---

## 3. Infraestrutura Docker

### Serviços definidos no `docker-compose.yml` raiz

```
iefc-db          → PostgreSQL 16-alpine (banco principal, healthcheck)
email-db         → PostgreSQL 16-alpine (banco do email-service, healthcheck)
redis            → Redis 7.4-alpine, 256 MB, LRU, AOF (rate-limiting)
iefc-api         → iefc-backend (porta ${IEFC_APP_PORT:-8080})
email-service    → email-service (porta ${EMAIL_APP_PORT:-8081})
iefc-web         → iefc-institucional (porta ${IEFC_WEB_PORT:-3000})
```

**Dependências de inicialização:**
- `iefc-api` espera `iefc-db` (healthy) + `redis` (healthy) + `email-service` (started)
- `email-service` espera `email-db` (healthy)
- `iefc-web` espera `iefc-api`

---

## 4. Frontend — `iefc-institucional`

### Tech Stack
- **Framework:** React 18.3 + Vite 5.3
- **Roteamento:** React Router DOM 6.26
- **Estilo:** TailwindCSS 3.4 + PostCSS

### Estrutura de arquivos

```
src/
├── App.jsx                # Roteador principal (Routes)
├── main.jsx               # Entry point (BrowserRouter + StrictMode)
├── index.css              # CSS global
├── pages/
│   ├── Home.jsx           # Página principal institucional
│   ├── Cursos.jsx         # Listagem de cursos disponíveis
│   ├── Apoie.jsx          # Página de apoio/doação
│   ├── Contato.jsx        # Formulário de contato
│   └── Transparencia.jsx  # Relatórios e transparência pública
├── components/
│   ├── Header.jsx         # Navegação principal
│   ├── Footer.jsx         # Rodapé
│   ├── Layout.jsx         # Wrapper de layout (Header + children + Footer)
│   ├── Button.jsx         # Componente de botão reutilizável
│   ├── Eyebrow.jsx        # Label de seção (eyebrow text)
│   ├── PageHeader.jsx     # Header de página interna
│   └── AnimatedStat.jsx   # Estatística com animação numérica
├── data/                  # Dados estáticos (JSON/JS)
└── styles/                # Estilos adicionais
```

### Rotas disponíveis

| Rota | Componente | Descrição |
|---|---|---|
| `/` | `Home` | Página institucional principal |
| `/cursos` | `Cursos` | Catálogo de cursos |
| `/apoie` | `Apoie` | Página de apoio |
| `/contato` | `Contato` | Formulário de contato |
| `/transparencia` | `Transparencia` | Relatórios públicos |

---

## 5. Backend Principal — `iefc-backend`

### Tech Stack

| Item | Versão / Detalhe |
|---|---|
| Spring Boot | 4.0.6 |
| Java | 21 |
| Banco | PostgreSQL 16 |
| ORM | Spring Data JPA / Hibernate |
| Segurança | Spring Security + OAuth2 Resource Server (JWT RSA) |
| Documentação | SpringDoc OpenAPI 3.0.2 (Swagger UI) |
| PDF | Apache PDFBox 3.0.3 |
| Rate Limit | Bucket4j 8.16.0 + Redis (Lettuce) |
| Email | Spring Mail (Mailtrap sandbox) + `email-service` via REST |
| Build | Maven + Lombok |
| Testes | H2 (in-memory), Spring Security Test, WebMVC Test |

### Estrutura de Pacotes (arquitetura atual — Layered)

```
school.sptech.iefcbackend/
├── IefcBackendApplication.java     # @SpringBootApplication + OpenAPI config
├── config/
│   ├── AdminUserConfig.java        # CommandLineRunner: cria admin + usuário padrão na inicialização
│   ├── RateLimitConfig.java        # RedisClient + LettuceBasedProxyManager (Bucket4j)
│   ├── RateLimitInterceptor.java   # HandlerInterceptor: aplica rate limit por IP
│   ├── RestTemplateConfig.java     # Bean RestTemplate
│   ├── SpringSecurityConfig.java   # SecurityFilterChain, JWT RSA, BCrypt, CORS
│   └── WebConfig.java              # Registro do RateLimitInterceptor no MVC
├── controllers/
│   ├── CursoController.java        # CRUD cursos + progressoAula
│   ├── EmpresaController.java      # CRUD empresas
│   ├── EventoController.java       # CRUD eventos
│   ├── InscricaoController.java    # Inscrições em cursos
│   ├── ProjetoController.java      # CRUD projetos
│   ├── RelatorioController.java    # Geração de relatório PDF
│   ├── TemaController.java         # CRUD temas de cursos
│   ├── TokenController.java        # POST /api/v1/login (geração JWT)
│   ├── UsuarioController.java      # CRUD usuários + aprovação/reprovação
│   └── VideoController.java        # CRUD vídeos de curso
├── dto/
│   ├── LoginRequestDTO.java        # record { email, senha }
│   ├── LoginResponseDTO.java       # record { accessToken, expiresIn }
│   ├── UsuarioAdminRequestDTO.java # Criação de usuário pelo admin (qualquer role)
│   ├── UsuarioColaboradorRequestDTO.java # Autocadastro colaborador (nasce PENDENTE)
│   ├── UsuarioRequestDTO.java      # Autocadastro aluno
│   ├── UsuarioResponseDTO.java     # DTO de resposta de usuário
│   ├── mapper/
│   │   └── UsuarioDTOMapper.java   # Conversão Usuario ↔ DTOs
│   └── relatorio/
│       ├── DepoimentoDTO.java      # Depoimento para o relatório PDF
│       ├── EventoRelatorioDTO.java # Dados de evento no relatório
│       ├── MembroEquipeDTO.java    # Membro da equipe no relatório
│       └── RelatorioRequestDTO.java # Request body para geração de relatório
├── events/
│   ├── ColaboradorAnalisadoEvent.java  # Disparado ao aprovar/reprovar colaborador
│   ├── ColaboradorCadastradoEvent.java # Disparado ao criar colaborador (PENDENTE)
│   └── RelatorioGeradoEvent.java       # Disparado ao gerar relatório
├── exception/
│   ├── CadastroPendenteException.java  # 403: login tentado com status PENDENTE
│   ├── CadastroReprovadoException.java # 403: login tentado com status REPROVADO
│   ├── EmailJaCadastradoException.java # 409: e-mail duplicado
│   ├── ExceptionResponse.java          # Envelope JSON de erro
│   ├── RecursoNaoEncontradoException.java # 404: recurso não encontrado
│   └── handler/                        # GlobalExceptionHandler (implícito)
├── models/
│   ├── Curso.java          # Entidade: tb_curso (→ Tema, → Empresa, @Formula totalAulas)
│   ├── Empresa.java        # Entidade: tb_empresa (→ Usuario)
│   ├── Eventos.java        # Entidade: tb_eventos (→ Empresa, M2M → Usuario)
│   ├── Inscricao.java      # Entidade: tb_inscricao (→ Usuario, → Curso)
│   ├── ProgressoAula.java  # Entidade: progresso do aluno por aula/vídeo
│   ├── Projeto.java        # Entidade: tb_projeto (→ Empresa, enum StatusProjeto)
│   ├── Role.java           # Enum: ADMIN, COLABORADOR, ALUNO (getAuthority)
│   ├── StatusCadastro.java # Enum: PENDENTE, APROVADO, REPROVADO
│   ├── Tema.java           # Entidade: tb_tema
│   ├── Usuario.java        # Entidade: tb_usuario (roles, status, loginCorreto, aprovar, reprovar)
│   └── Video.java          # Entidade: tb_video (→ Curso)
├── observer/
│   ├── ColaboradorNotificacaoListener.java # @EventListener: rota eventos → notificações e-mail
│   └── RelatorioEmailListener.java         # @EventListener: envia relatório por e-mail
├── repository/
│   ├── CursoRepository.java        # JpaRepository<Curso, Long>
│   ├── EmpresaRepository.java      # JpaRepository<Empresa, Long>
│   ├── EventoRepository.java       # JpaRepository<Eventos, Long>
│   ├── InscricaoRepository.java    # findByUsuarioId, existsByUsuarioIdAndCursoId
│   ├── ProgressoAulaRepository.java
│   ├── ProjetoRepository.java      # findByNome, findAllByStatus, findAllByDataInicio
│   ├── TemaRepository.java
│   ├── UsuarioRepository.java      # findByEmail, existsByEmail, findAllAdmins, findByStatus
│   └── VideoRepository.java
└── services/
    ├── CursoService.java
    ├── EmailService.java                       # Spring Mail direto (relatório)
    ├── EmailServiceNotificacaoAdapter.java     # Adapter REST → email-service
    ├── EmpresaService.java
    ├── EventoService.java
    ├── InscricaoService.java
    ├── NotificacaoColaboradorService.java      # Interface (porta de notificação)
    ├── ProgressoAulaService.java
    ├── ProjetoService.java
    ├── RateLimitService.java                   # Lógica de rate limit via Bucket4j
    ├── RelatorioService.java                   # Geração de PDF (Apache PDFBox, 594 linhas)
    ├── TemaService.java
    ├── UsuarioService.java                     # Cadastro, aprovação, reprovação, JWT events
    └── VideoService.java
```

### Endpoints da API

#### Autenticação — `POST /api/v1/login` (público)
- Recebe `{ email, senha }`, valida senha BCrypt, verifica status (PENDENTE/REPROVADO), retorna JWT RSA com scopes de roles.

#### Usuários — `/api/v1/usuarios`
| Método | Rota | Auth | Descrição |
|---|---|---|---|
| GET | `/` | ALUNO/COLABORADOR/ADMIN | Lista todos |
| GET | `/{id}` | ALUNO/COLABORADOR/ADMIN | Busca por ID |
| GET | `/email?email=...` | ALUNO/COLABORADOR/ADMIN | Busca por e-mail |
| POST | `/` | Público | Autocadastro (ALUNO) |
| POST | `/admin` | ADMIN | Cria usuário com qualquer role |
| POST | `/colaboradores` | Público | Autocadastro colaborador (PENDENTE) |
| GET | `/pendentes` | ADMIN | Lista pendentes de aprovação |
| PUT | `/{id}/aprovar` | ADMIN | Aprova colaborador |
| PUT | `/{id}/reprovar` | ADMIN | Reprova colaborador |
| GET | `/admin-users` | ADMIN | Lista todos os admins |
| PUT | `/{id}` | ADMIN | Atualiza usuário |
| DELETE | `/{id}` | ADMIN | Deleta usuário |

#### Empresas — `/api/v1/empresas`
CRUD completo (GET, POST, PUT, DELETE), rotas GET públicas.

#### Projetos — `/api/v1/projetos`
| Método | Rota | Descrição |
|---|---|---|
| GET | `/` | Lista todos |
| GET | `/nome/{nome}` | Busca por nome |
| GET | `/dataInicio/{data}` | Busca por data de início |
| GET | `/status/{status}` | Busca por status (EM_ANDAMENTO/CONCLUIDO/CANCELADO/PENDENTE) |
| POST | `/` | Cria projeto |
| PUT | `/{id}` | Atualiza projeto |
| DELETE | `/{id}` | Deleta projeto |

#### Cursos — `/api/v1/cursos`
CRUD completo + progresso de aula.

#### Vídeos — `/api/v1/videos`
CRUD de vídeos vinculados a cursos.

#### Eventos — `/api/v1/eventos`
CRUD de eventos + listagem de participantes.

#### Inscrições — `/api/v1/inscricoes`
- Inscrever usuário em curso, listar por usuário, verificar inscrição, cancelar.

#### Temas — `/api/v1/temas`
CRUD de temas (categorias de cursos).

#### Relatório — `/api/v1/relatorios`
- `POST /gerar` — Gera PDF preenchido via Apache PDFBox e envia por e-mail.

### Segurança

- **JWT RSA assimétrico**: chave privada (`app.key`) + pública (`app.pub`) em `resources/`.
- **Roles**: `ROLE_ADMIN`, `ROLE_COLABORADOR`, `ROLE_ALUNO` (prefixados com `SCOPE_` no Spring Security).
- **Rate Limit**: Bucket4j + Redis (Lettuce), controlado por IP via `RateLimitInterceptor`.
- **CORS**: `allowedOriginPatterns(*)`, sem credenciais, métodos GET/POST/PUT/DELETE.
- **Usuário admin**: criado na inicialização via `AdminUserConfig` se não existir.

### Fluxo de Notificação (Padrão Observer)

```
UsuarioService.cadastrarColaborador()
    └── eventPublisher.publishEvent(ColaboradorCadastradoEvent)
            └── ColaboradorNotificacaoListener.onColaboradorCadastrado()
                    └── EmailServiceNotificacaoAdapter.notificarNovoCadastroPendente()
                            └── REST POST → email-service /api/v1/emails

UsuarioService.aprovarCadastro() / reprovarCadastro()
    └── eventPublisher.publishEvent(ColaboradorAnalisadoEvent)
            └── ColaboradorNotificacaoListener.onColaboradorAnalisado()
                    └── EmailServiceNotificacaoAdapter.notificarCadastroAprovado/Reprovado()
                            └── REST POST → email-service /api/v1/emails
```

---

## 6. Microsserviço de E-mail — `email-service`

### Descrição
Microsserviço independente, **já implementado em Clean Architecture**, responsável por receber solicitações de envio de e-mail, enfileirá-las no banco e processá-las de forma assíncrona.

### Tech Stack
- Spring Boot 4.0.5 + Java 21
- Spring Mail (Mailtrap)
- Spring Data JPA + PostgreSQL
- Spring Security (API Key via header `X-API-KEY`)
- Springdoc OpenAPI 3.0.2
- Bucket4j para scheduling

### Camadas Clean Architecture implementadas

```
school.sptech.emailservice/
├── domain/
│   ├── entity/
│   │   ├── Email.java              # Entidade de domínio (sem anotações JPA)
│   │   ├── EmailStatus.java        # Enum: PENDENTE, ENVIADO, FALHOU
│   │   └── Pagina.java             # Value Object: paginação
│   ├── event/
│   │   └── EmailCriadoEvent.java   # Evento de domínio
│   └── port/
│       ├── EmailRepositoryPort.java # Interface de persistência
│       ├── EmailSenderPort.java     # Interface de envio
│       └── NotificadorFilaPort.java # Interface de notificação de fila
├── usecase/
│   ├── ConsultarEmailUseCase.java
│   ├── ProcessarFilaEmailsUseCase.java
│   ├── ReenviarEmailUseCase.java
│   ├── SolicitarEnvioEmailCommand.java  # Command object (input)
│   └── SolicitarEnvioEmailUseCase.java
├── infrastructure/
│   ├── config/
│   │   ├── OpenApiConfig.java
│   │   ├── SecurityConfig.java         # API Key auth
│   │   └── UseCaseConfig.java          # @Bean dos use cases
│   ├── persistence/
│   │   ├── EmailJpaEntity.java         # Entidade JPA separada do domínio
│   │   ├── EmailJpaRepository.java     # Spring Data JPA
│   │   ├── EmailPersistenceMapper.java # Email ↔ EmailJpaEntity
│   │   └── EmailRepositoryAdapter.java # Implementa EmailRepositoryPort
│   ├── mail/
│   │   └── JavaMailEmailSenderAdapter.java # Implementa EmailSenderPort
│   ├── event/
│   │   ├── EmailCriadoAsyncListener.java
│   │   └── SpringEventNotificadorFilaAdapter.java # Implementa NotificadorFilaPort
│   └── scheduler/
│       └── EmailQueueScheduler.java    # @Scheduled: processa fila periodicamente
└── web/
    ├── controller/
    │   └── EmailController.java
    ├── dto/
    │   ├── EmailResponseDTO.java
    │   ├── PaginaResponseDTO.java
    │   └── SolicitarEnvioEmailRequestDTO.java
    ├── filter/
    │   └── ApiKeyAuthFilter.java
    ├── handler/
    │   ├── ExceptionResponse.java
    │   └── GlobalExceptionHandler.java
    └── mapper/
        └── EmailWebMapper.java         # DTO ↔ Command / Entity
```

### Serviços de aplicação (services/ — wrappers de use cases)
- `ConsultarEmailService`, `ProcessarFilaEmailsService`, `ReenviarEmailService`, `SolicitarEnvioEmailService`

### Exceções de domínio
- `EmailNaoEncontradoException`, `EnvioEmailException`

---

## 7. Modelos de Dados — iefc-backend

### Diagrama de Entidades

```
tb_usuario ──────────────────────────────────────────────────┐
│ usuario_id (PK)                                             │
│ nome, email (unique), senha                                 │
│ status: PENDENTE | APROVADO | REPROVADO                     │
│ createdAt (epoch)                                           │
│ roles → tb_usuario_roles (SET<Role>: ADMIN/COLABORADOR/ALUNO)│
└──────────────────────────────────────────────────────────────┘
        │                          │
        │ 1:N                      │ M:N (via usuario_evento)
        ▼                          ▼
   tb_empresa              tb_eventos
   │ empresa_id (PK)       │ eventos_id (PK)
   │ nome, cnpj, telefone  │ titulo, data, descricao
   │ → usuario             │ status: ATIVO|FECHADO|CANCELADO|EM_ANALISE
   └─────────────────────  │ → empresa
        │ 1:N              └──────────────────────────────────────
        ├──────── tb_projeto
        │         │ projeto_id (PK)
        │         │ nome (unique), descricao
        │         │ dataInicio, dataFim
        │         │ status: EM_ANDAMENTO|CONCLUIDO|CANCELADO|PENDENTE
        │         └─────────────────────────────────────────────────
        │
        └──────── tb_curso
                  │ curso_id (PK)
                  │ titulo, descricao, instrutor
                  │ videoId (capa), totalAulas (@Formula)
                  │ → tema (tb_tema)
                  │ → empresa (tb_empresa)
                  │    │ 1:N
                  │    └──── tb_video (Video)
                  │           │ idVideo (PK)
                  │           │ titulo, url, videoId, duracao, modulo, ordem
                  │           └─────────────────────────────────────────────
                  │
                  └── tb_inscricao
                      │ inscricao_id (PK)
                      │ dataInscricao
                      │ → usuario
                      │ → curso
                      └──────────────────────────────────────────────────────

tb_tema
│ (id, nome)
└──────────────────────────────────────────────────────────────────────────
```

---

## 8. Variáveis de Ambiente

| Variável | Serviço | Descrição |
|---|---|---|
| `IEFC_DATABASE_HOST` | iefc-backend | Host do banco (default: `iefc-db`) |
| `IEFC_DATABASE_PORT` | iefc-backend | Porta PostgreSQL (default: `5432`) |
| `IEFC_DATABASE_NAME` | iefc-backend | Nome do banco (default: `iefc_db`) |
| `IEFC_DATABASE_USERNAME` | iefc-backend | Usuário do banco |
| `IEFC_DATABASE_PASSWORD` | iefc-backend | Senha do banco |
| `APP_ADMIN_EMAIL` | iefc-backend | E-mail do admin inicial |
| `APP_ADMIN_SENHA` | iefc-backend | Senha do admin inicial (obrigatório) |
| `APP_USUARIO_PADRAO_EMAIL` | iefc-backend | E-mail do usuário padrão (opcional) |
| `APP_USUARIO_PADRAO_SENHA` | iefc-backend | Senha do usuário padrão (opcional) |
| `MAILTRAP_USERNAME` | iefc-backend | Credencial Mailtrap SMTP |
| `MAILTRAP_PASSWORD` | iefc-backend | Credencial Mailtrap SMTP |
| `RELATORIO_EMAIL_DESTINATARIO` | iefc-backend | Destinatário do relatório PDF |
| `EMAIL_SERVICE_BASE_URL` | iefc-backend | URL do email-service (default: `http://email-service:8080`) |
| `EMAIL_SERVICE_API_KEY` | iefc-backend | API Key para autenticar no email-service |
| `EMAIL_SERVICE_REMETENTE` | iefc-backend | Remetente padrão dos e-mails |
| `ADMIN_NOTIFICACAO_EMAIL` | iefc-backend | Admin que recebe notificações de colaboradores |
| `FRONTEND_BASE_URL` | iefc-backend | URL do frontend (default: `http://localhost:3000`) |
| `EMAIL_DATABASE_*` | email-service | Configurações do banco do email-service |
| `IEFC_APP_PORT` | compose | Porta exposta iefc-api (default: 8080) |
| `EMAIL_APP_PORT` | compose | Porta exposta email-service (default: 8081) |
| `IEFC_WEB_PORT` | compose | Porta exposta frontend (default: 3000) |

---

## 9. Padrões de Design Utilizados

| Padrão | Onde |
|---|---|
| **Repository** | Todos os `*Repository` (Spring Data JPA) |
| **Service Layer** | Todos os `*Service` |
| **DTO / Mapper** | `dto/` + `UsuarioDTOMapper` |
| **Observer** (Spring Events) | `events/` + `observer/` |
| **Adapter** | `EmailServiceNotificacaoAdapter` (REST → email-service) |
| **Interface Port** | `NotificacaoColaboradorService` |
| **Command Runner** | `AdminUserConfig` (CommandLineRunner) |
| **Rate Limiting** | `RateLimitInterceptor` + `RateLimitService` + Bucket4j/Redis |
| **JWT RSA** | `SpringSecurityConfig` + `TokenController` |

---

---

# 🏗️ PROPOSTA: Migração do `iefc-backend` para Clean Architecture

> O `email-service` já serve como **referência** de Clean Architecture no projeto. O objetivo é alinhar o `iefc-backend` ao mesmo padrão.

## 10. Por que migrar?

### Problemas da arquitetura atual (Layered)
1. **`TokenController` acessa `UsuarioRepository` diretamente** — viola separação de camadas.
2. **`ProjetoService` retorna entidades JPA direto** — acoplamento domínio ↔ transporte.
3. **`RelatorioService` tem 594 linhas** — mistura geração de PDF (infra) com lógica de negócio.
4. **`EmailServiceNotificacaoAdapter` vive em `services/`** — é um adaptador de infraestrutura.
5. **DTOs misturados com mappers** sem separação clara de camadas.
6. **Sem interfaces de porta** (exceto `NotificacaoColaboradorService`).

---

## 11. Nova Estrutura de Pacotes (Clean Architecture)

```
school.sptech.iefcbackend/
│
├── IefcBackendApplication.java
│
├── domain/                          ← NÚCLEO: zero dependências externas
│   ├── entity/
│   │   ├── Usuario.java             # (mantém lógica: loginCorreto, aprovar, reprovar)
│   │   ├── Empresa.java
│   │   ├── Projeto.java
│   │   ├── Curso.java
│   │   ├── Video.java
│   │   ├── Eventos.java
│   │   ├── Inscricao.java
│   │   ├── ProgressoAula.java
│   │   └── Tema.java
│   ├── enums/
│   │   ├── Role.java
│   │   ├── StatusCadastro.java
│   │   └── StatusProjeto.java       # (extraído de Projeto.StatusProjeto)
│   ├── event/
│   │   ├── ColaboradorAnalisadoEvent.java
│   │   ├── ColaboradorCadastradoEvent.java
│   │   └── RelatorioGeradoEvent.java
│   ├── exception/
│   │   ├── CadastroPendenteException.java
│   │   ├── CadastroReprovadoException.java
│   │   ├── EmailJaCadastradoException.java
│   │   └── RecursoNaoEncontradoException.java
│   └── port/                        ← Interfaces de saída (hexágono)
│       ├── UsuarioRepositoryPort.java
│       ├── EmpresaRepositoryPort.java
│       ├── ProjetoRepositoryPort.java
│       ├── CursoRepositoryPort.java
│       ├── VideoRepositoryPort.java
│       ├── EventoRepositoryPort.java
│       ├── InscricaoRepositoryPort.java
│       ├── ProgressoAulaRepositoryPort.java
│       ├── TemaRepositoryPort.java
│       ├── NotificacaoColaboradorPort.java  # (renomeado de NotificacaoColaboradorService)
│       ├── RelatorioPdfPort.java            # nova: abstrai geração de PDF
│       └── RelatorioEmailPort.java          # nova: abstrai envio de relatório
│
├── usecase/                         ← CASOS DE USO (orquestração pura)
│   ├── usuario/
│   │   ├── BuscarTodosUsuariosUseCase.java
│   │   ├── BuscarUsuarioPorIdUseCase.java
│   │   ├── BuscarUsuarioPorEmailUseCase.java
│   │   ├── CadastrarAlunoUseCase.java
│   │   ├── CadastrarColaboradorUseCase.java
│   │   ├── CriarUsuarioAdminUseCase.java
│   │   ├── AtualizarUsuarioUseCase.java
│   │   ├── DeletarUsuarioUseCase.java
│   │   ├── AprovarCadastroUseCase.java
│   │   ├── ReprovarCadastroUseCase.java
│   │   └── ListarPendentesUseCase.java
│   ├── auth/
│   │   └── AutenticarUsuarioUseCase.java    # lógica do TokenController
│   ├── empresa/
│   │   └── (EmpresaUseCases...)
│   ├── projeto/
│   │   └── (ProjetoUseCases...)
│   ├── curso/
│   │   └── (CursoUseCases...)
│   ├── video/
│   │   └── (VideoUseCases...)
│   ├── evento/
│   │   └── (EventoUseCases...)
│   ├── inscricao/
│   │   └── (InscricaoUseCases...)
│   ├── tema/
│   │   └── (TemaUseCases...)
│   └── relatorio/
│       └── GerarRelatorioUseCase.java
│
├── infrastructure/                  ← ADAPTADORES DE SAÍDA
│   ├── config/
│   │   ├── AdminUserConfig.java         # CommandLineRunner (chama use cases)
│   │   ├── RateLimitConfig.java
│   │   ├── RestTemplateConfig.java
│   │   ├── SpringSecurityConfig.java
│   │   ├── WebConfig.java
│   │   └── UseCaseConfig.java           # @Bean de todos os use cases
│   ├── persistence/
│   │   ├── jpa/
│   │   │   ├── UsuarioJpaRepository.java    # (atual UsuarioRepository)
│   │   │   ├── EmpresaJpaRepository.java
│   │   │   ├── ProjetoJpaRepository.java
│   │   │   ├── CursoJpaRepository.java
│   │   │   ├── VideoJpaRepository.java
│   │   │   ├── EventoJpaRepository.java
│   │   │   ├── InscricaoJpaRepository.java
│   │   │   ├── ProgressoAulaJpaRepository.java
│   │   │   └── TemaJpaRepository.java
│   │   └── adapter/
│   │       ├── UsuarioRepositoryAdapter.java    # Implementa UsuarioRepositoryPort
│   │       ├── EmpresaRepositoryAdapter.java
│   │       ├── ProjetoRepositoryAdapter.java
│   │       ├── CursoRepositoryAdapter.java
│   │       ├── VideoRepositoryAdapter.java
│   │       ├── EventoRepositoryAdapter.java
│   │       ├── InscricaoRepositoryAdapter.java
│   │       ├── ProgressoAulaRepositoryAdapter.java
│   │       └── TemaRepositoryAdapter.java
│   ├── notification/
│   │   └── EmailServiceNotificacaoAdapter.java  # Implementa NotificacaoColaboradorPort
│   ├── pdf/
│   │   └── PdfBoxRelatorioAdapter.java          # Implementa RelatorioPdfPort (594 linhas → aqui)
│   ├── mail/
│   │   └── SpringMailRelatorioAdapter.java      # Implementa RelatorioEmailPort
│   ├── event/
│   │   ├── ColaboradorNotificacaoListener.java  # @EventListener (mantido)
│   │   └── RelatorioEmailListener.java          # @EventListener (mantido)
│   ├── ratelimit/
│   │   ├── RateLimitInterceptor.java
│   │   └── RateLimitService.java
│   └── security/
│       └── (configurações JWT / BCrypt — já em config/)
│
└── web/                             ← ADAPTADORES DE ENTRADA
    ├── controller/
    │   ├── CursoController.java
    │   ├── EmpresaController.java
    │   ├── EventoController.java
    │   ├── InscricaoController.java
    │   ├── ProjetoController.java
    │   ├── RelatorioController.java
    │   ├── TemaController.java
    │   ├── TokenController.java
    │   ├── UsuarioController.java
    │   └── VideoController.java
    ├── dto/
    │   ├── LoginRequestDTO.java
    │   ├── LoginResponseDTO.java
    │   ├── UsuarioAdminRequestDTO.java
    │   ├── UsuarioColaboradorRequestDTO.java
    │   ├── UsuarioRequestDTO.java
    │   ├── UsuarioResponseDTO.java
    │   └── relatorio/
    │       ├── DepoimentoDTO.java
    │       ├── EventoRelatorioDTO.java
    │       ├── MembroEquipeDTO.java
    │       └── RelatorioRequestDTO.java
    ├── mapper/
    │   └── UsuarioDTOMapper.java
    └── handler/
        ├── ExceptionResponse.java
        └── GlobalExceptionHandler.java  # (mover de exception/handler/)
```

---

## 12. Mapeamento: Arquivo Atual → Destino Clean Arch

| Arquivo atual | Novo destino | Observação |
|---|---|---|
| `models/Usuario.java` | `domain/entity/Usuario.java` | **Remover** import de `dto/LoginRequestDTO` — mover lógica `loginCorreto` para use case |
| `models/Empresa.java` | `domain/entity/Empresa.java` | — |
| `models/Projeto.java` | `domain/entity/Projeto.java` | Extrair `StatusProjeto` para `domain/enums/` |
| `models/Curso.java` | `domain/entity/Curso.java` | Remover `@JsonBackReference` — usar DTO |
| `models/Video.java` | `domain/entity/Video.java` | Remover `@JsonBackReference` |
| `models/Eventos.java` | `domain/entity/Eventos.java` | — |
| `models/Inscricao.java` | `domain/entity/Inscricao.java` | — |
| `models/ProgressoAula.java` | `domain/entity/ProgressoAula.java` | — |
| `models/Tema.java` | `domain/entity/Tema.java` | — |
| `models/Role.java` | `domain/enums/Role.java` | — |
| `models/StatusCadastro.java` | `domain/enums/StatusCadastro.java` | — |
| `events/*.java` | `domain/event/*.java` | — |
| `exception/CadastroPendenteException.java` | `domain/exception/` | — |
| `exception/CadastroReprovadoException.java` | `domain/exception/` | — |
| `exception/EmailJaCadastradoException.java` | `domain/exception/` | — |
| `exception/RecursoNaoEncontradoException.java` | `domain/exception/` | — |
| `exception/ExceptionResponse.java` | `web/handler/ExceptionResponse.java` | Pertence à camada web |
| `exception/handler/` | `web/handler/` | — |
| `services/NotificacaoColaboradorService.java` | `domain/port/NotificacaoColaboradorPort.java` | Renomear para Port |
| `repository/*.java` | `infrastructure/persistence/jpa/*.java` | Renomear com sufixo `JpaRepository` |
| `services/EmailServiceNotificacaoAdapter.java` | `infrastructure/notification/` | Implementa `NotificacaoColaboradorPort` |
| `services/RelatorioService.java` | `infrastructure/pdf/PdfBoxRelatorioAdapter.java` | Lógica PDF pura vai para infra |
| `services/EmailService.java` | `infrastructure/mail/SpringMailRelatorioAdapter.java` | Implementa `RelatorioEmailPort` |
| `services/RateLimitService.java` | `infrastructure/ratelimit/RateLimitService.java` | — |
| `services/UsuarioService.java` | `usecase/usuario/` (vários use cases) | Dividir por responsabilidade |
| `services/ProjetoService.java` | `usecase/projeto/` | — |
| `services/CursoService.java` | `usecase/curso/` | — |
| `services/EmpresaService.java` | `usecase/empresa/` | — |
| `services/EventoService.java` | `usecase/evento/` | — |
| `services/InscricaoService.java` | `usecase/inscricao/` | — |
| `services/TemaService.java` | `usecase/tema/` | — |
| `services/VideoService.java` | `usecase/video/` | — |
| `services/ProgressoAulaService.java` | `usecase/progressoaula/` | — |
| `controllers/*.java` | `web/controller/*.java` | Injetam use cases (não services) |
| `dto/*.java` | `web/dto/*.java` | — |
| `dto/mapper/*.java` | `web/mapper/*.java` | — |
| `config/SpringSecurityConfig.java` | `infrastructure/config/SpringSecurityConfig.java` | — |
| `config/AdminUserConfig.java` | `infrastructure/config/AdminUserConfig.java` | Chama use cases |
| `config/RateLimitConfig.java` | `infrastructure/config/RateLimitConfig.java` | — |
| `config/RateLimitInterceptor.java` | `infrastructure/ratelimit/RateLimitInterceptor.java` | — |
| `config/RestTemplateConfig.java` | `infrastructure/config/RestTemplateConfig.java` | — |
| `config/WebConfig.java` | `infrastructure/config/WebConfig.java` | — |
| `observer/*.java` | `infrastructure/event/*.java` | São listeners de eventos de domínio |

---

## 13. Regras de Dependência (Clean Architecture)

```
┌──────────────────────────────────────────────┐
│  web/  (controllers, DTOs, handlers)          │
│    ↓ depende de                               │
├──────────────────────────────────────────────┤
│  usecase/  (casos de uso)                     │
│    ↓ depende de                               │
├──────────────────────────────────────────────┤
│  domain/  (entidades, enums, ports, events)   │
│    ✗ NÃO depende de nada                      │
├──────────────────────────────────────────────┤
│  infrastructure/  (JPA, mail, PDF, REST)      │
│    ↓ implementa ports de domain/              │
│    ↓ depende de domain/ e usecase/            │
└──────────────────────────────────────────────┘
```

> **Regra de ouro:** `domain/` nunca importa nada de `infrastructure/`, `web/` ou qualquer framework Spring. Use cases só importam de `domain/`.

---

## 14. Checklist de Migração

- [ ] Criar pacote `domain/` com entidades limpas (sem anotações JPA)
- [ ] Criar pacote `domain/port/` com todas as interfaces de repositório e serviço externo
- [ ] Mover exceções de negócio para `domain/exception/`
- [ ] Mover eventos para `domain/event/`
- [ ] Criar use cases em `usecase/` substituindo os `*Service` atuais
- [ ] Criar `infrastructure/persistence/jpa/` com Spring Data interfaces
- [ ] Criar `infrastructure/persistence/adapter/` implementando as ports
- [ ] Mover `EmailServiceNotificacaoAdapter` para `infrastructure/notification/`
- [ ] Mover lógica PDF de `RelatorioService` para `infrastructure/pdf/PdfBoxRelatorioAdapter`
- [ ] Criar `UseCaseConfig.java` com todos os `@Bean` de use cases
- [ ] Atualizar controllers para injetar use cases (não services)
- [ ] Remover imports de domínio da camada DTO/mapper (usar apenas tipos primitivos)
- [ ] Garantir que entidades JPA ficam apenas em `infrastructure/persistence/`
- [ ] Adicionar testes unitários nos use cases (sem Spring context)

---

## 15. Considerações Técnicas

### Anotações JPA nas entidades de domínio
Como o projeto usa **Spring Data JPA** com mapeamento direto, há duas abordagens:
1. **Pragmática (recomendada para agora):** manter anotações JPA nas entidades de domínio, já que a separação de JpaEntity ↔ DomainEntity tem custo alto. O `email-service` já adotou separação completa.
2. **Pura:** criar `JpaEntity` separadas (como o `email-service`) com mapper entre elas.

### TokenController e AutenticarUsuarioUseCase
O `TokenController` atualmente acessa `UsuarioRepository` diretamente e injeta `JwtEncoder`. Na Clean Arch, o use case `AutenticarUsuarioUseCase` receberá uma porta `UsuarioRepositoryPort` e uma porta `JwtPort`, mantendo a lógica de geração de token dentro da camada de use case.

---

*Documento gerado automaticamente com base na análise completa do código-fonte em 2026-09-29.*
