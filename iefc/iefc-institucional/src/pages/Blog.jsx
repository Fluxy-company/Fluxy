import { useSearchParams } from 'react-router-dom'
import Layout from '../components/Layout'
import PageHeader from '../components/PageHeader'
import Tabs from '../components/ui/Tabs'
import EmptyState from '../components/ui/EmptyState'
import { posts } from '../data/siteData'

const ABAS = [
  { valor: 'todos', titulo: 'Todos' },
  { valor: 'categorias', titulo: 'Categorias' },
]

function CardPost({ post }) {
  return (
    <div className="border border-border rounded-lg2 p-6 lift">
      <i className={`ph-light ${post.icon} text-[22px]`} style={{ color: post.tint }} />
      <span className="block font-label text-[10px] tracking-[0.1em] uppercase text-teal-accessible mt-3 mb-1.5">
        {post.tag}
      </span>
      <h3 className="font-display font-medium text-navy text-[16px] mb-1.5">{post.title}</h3>
      <p className="text-[13px] text-ink-soft leading-relaxed m-0">{post.excerpt}</p>
    </div>
  )
}

export default function Blog() {
  const [searchParams, setSearchParams] = useSearchParams()
  const aba = ABAS.some((a) => a.valor === searchParams.get('aba')) ? searchParams.get('aba') : 'todos'

  function mudarAba(valor) {
    setSearchParams(valor === 'todos' ? {} : { aba: valor })
  }

  const categorias = [...new Set(posts.map((p) => p.tag))]

  return (
    <Layout>
      <PageHeader
        eyebrow="Blog / Notícias"
        title="Bastidores da Ciência no IEFC"
        lead="Histórias, pesquisas e novidades dos programas e projetos do Instituto."
      />

      <section className="px-6 md:px-10 py-14">
        <div className="max-w-content mx-auto">
          <Tabs abas={ABAS} ativa={aba} onChange={mudarAba} className="mb-10 overscroll-x-none" />

          {posts.length === 0 ? (
            <EmptyState titulo="Nenhum post publicado ainda." />
          ) : aba === 'todos' ? (
            <div className="grid grid-cols-1 md:grid-cols-3 gap-5">
              {posts.map((post) => (
                <CardPost key={post.title} post={post} />
              ))}
            </div>
          ) : (
            <div className="space-y-10">
              {categorias.map((categoria) => (
                <div key={categoria}>
                  <h3 className="font-label text-[11px] tracking-[0.14em] uppercase text-ink-caption mb-4">
                    {categoria}
                  </h3>
                  <div className="grid grid-cols-1 md:grid-cols-3 gap-5">
                    {posts
                      .filter((p) => p.tag === categoria)
                      .map((post) => (
                        <CardPost key={post.title} post={post} />
                      ))}
                  </div>
                </div>
              ))}
            </div>
          )}
        </div>
      </section>
    </Layout>
  )
}
