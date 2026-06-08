const API_BASE = import.meta.env.VITE_API_BASE_URL || "/api/v1";

function authHeader() {
    const token = localStorage.getItem("token");
    return token ? { Authorization: `Bearer ${token}` } : {};
}

export async function gerarRelatorioPdf(formData) {
    let resp;
    try {
        resp = await fetch(`${API_BASE}/relatorio/gerar`, {
            method: "POST",
            headers: { Accept: "application/pdf", ...authHeader() },
            body: formData, 
        });
    } catch (networkErr) {
        console.error("[relatorioApi] Erro de rede ao chamar /relatorio/gerar:", networkErr);
        throw new Error(
            "Erro de conexão com o servidor. Verifique se o backend está rodando e tente novamente. ("
            + networkErr.message + ")"
        );
    }
    if (!resp.ok) {
        const errText = await resp.text().catch(() => `HTTP ${resp.status}`);
        console.error("[relatorioApi] Servidor retornou erro:", resp.status, errText);
        throw new Error(`Erro do servidor (${resp.status}): ${errText}`);
    }
    return resp.blob();
}

export function downloadPdfBlob(blob, nome = "Relatorio_IEFC.pdf") {
    const url = URL.createObjectURL(blob);
    const a = document.createElement("a");
    a.href = url;
    a.download = nome;
    a.click();
    URL.revokeObjectURL(url);
}