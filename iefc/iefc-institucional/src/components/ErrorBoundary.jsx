import { Component } from 'react'

export default class ErrorBoundary extends Component {
  constructor(props) {
    super(props)
    this.state = { comErro: false }
  }

  static getDerivedStateFromError() {
    return { comErro: true }
  }

  componentDidCatch(erro, info) {

    console.error('Erro inesperado capturado pelo ErrorBoundary:', erro, info)
  }

  render() {
    if (this.state.comErro) {
      return (
        <div className="min-h-screen flex items-center justify-center px-6 bg-cream">
          <div className="max-w-[420px] text-center">
            <i className="ph-light ph-warning text-navy text-[40px]" />
            <h1 className="font-display font-bold text-navy text-[26px] mt-4 mb-2">
              Algo deu errado
            </h1>
            <p className="text-[14px] text-ink-soft mb-8">
              Ocorreu um erro inesperado. Tente recarregar a página.
            </p>
            <button
              type="button"
              onClick={() => window.location.reload()}
              className="font-label font-bold text-[12px] tracking-[0.08em] uppercase px-5 py-3 rounded-md2 bg-amber text-navy cursor-pointer"
            >
              Recarregar
            </button>
          </div>
        </div>
      )
    }
    return this.props.children
  }
}
