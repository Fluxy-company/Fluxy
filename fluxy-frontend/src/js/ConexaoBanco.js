let porta_spring = import.meta.env.SPRING_PORT;
let servico_spring = import.meta.env.SERVICO_SPRING;

const token = localStorage.getItem(`token`)

rota.post(`/caminho_postar`, async function (req, res) {
    var caminho = req.query.caminho;
    

    try {
        const resp = await fetch(`http://${servico_spring}:${porta_spring}/${caminho}`, {
            method: "POST",
            headers: {
                "Accept": "application/json",
                "Content-Type": "application/json",
                "Accept-Language":"pt-BR",
                "Authorization": `Bearer ${token}`

            },
            body: JSON.stringify(req.body)
        });

        const resposta = await resp;
    
        if (!resp.ok) {
            const respostaFormatada =await resp.text()
            console.error("Erro vindo do Spring:", respostaFormatada);
            return res.status(resp.status).json(respostaFormatada);
        }

        return res.status(resp.status).json(resposta);

    } catch (error) {
        console.error("Erro geral:", error);
        res.status(500).json({ erro: "Erro interno no Node" });
    }
});

rota.get(`/caminho_listar`, async function (req, res) {
    var caminho = req.query.caminho;

    try {
        const resp = await fetch(`http://${servico_spring}:${porta_spring}/${caminho}/`, {
            method: "GET",
            headers: {
                "Accept": "application/json",
                "Content-Type": "application/json",
                "Accept-Language":"pt-BR",
                "Authorization": `Bearer ${token}`
            }        
        });

        const resposta = await resp;

         if(!(resp.ok)){

            return [];
        }
        const dados = await resposta.json()
        return res.status(resp.status).json(dados);

    } catch (error) {
        console.error("Erro geral:", error);
        res.status(500).json({ erro: "Erro interno no Node" });
    }
});

rota.get(`/caminho_achar_id`, async function (req, res) {
    var caminho = req.query.caminho;
    var id = req.query.id;
    try {
        const resp = await fetch(`http://${servico_spring}:${porta_spring}/${caminho}/${id}`, {
            method: "GET",
            headers: {
                "Accept": "application/json",
                "Content-Type": "application/json",
                "Accept-Language":"pt-BR",
                "Authorization": `Bearer ${token}`
            }        
        });
        const resposta = await resp;
        const dados = await resposta.json()
        return res.status(resp.status).json(dados);

    } catch (error) {
        console.error("Erro geral:", error);
        res.status(500).json({ erro: "Erro interno no Node" });
    }
});

rota.delete(`/caminho_remover`, async function (req, res) {
    var caminho = req.query.caminho;
    var id = req.query.id;

    try {
        const resp = await fetch(`http://${servico_spring}:${porta_spring}/${caminho}/${id}`, {
            method: "DELETE",
            headers: {
                "Accept": "application/json",
                "Content-Type": "application/json",
                "Accept-Language":"pt-BR",
                "Authorization": `Bearer ${token}`
            }        
        });

        const resposta = await resp;
        return res.status(resp.status).json(resposta);

    } catch (error) {
        console.error("Erro geral:", error);
        res.status(500).json({ erro: "Erro interno no Node" });
    }
});

rota.put(`/caminho_atualizar`, async function (req, res) {
    var caminho = req.query.caminho;
    var id = req.query.id;

    try {
        const resp = await fetch(`http://${servico_spring}:${porta_spring}/${caminho}/${id}`, {
            method: "PUT",
            headers: {
                "Accept": "application/json",
                "Content-Type": "application/json",
                "Accept-Language":"pt-BR",
                "Authorization": `Bearer ${token}`
            },
            body: JSON.stringify(req.body)
        });

        const resposta = await resp;
    
        if (!resp.ok) {
            const respostaFormatada =await resp.text()
            console.error("Erro vindo do Spring:", respostaFormatada);
            return res.status(resp.status).json(respostaFormatada);
        }

        return res.status(resp.status).json(resposta);
    } catch (error) {
        console.error("Erro geral:", error);
        res.status(500).json({ erro: "Erro interno no Node" });
    }

    
    
});
rota.post(`/caminho_autenticar`, async function (req, res) {
    var caminho = req.query.caminho;


    try {
        const resp = await fetch(`http://${servico_spring}:${porta_spring}/${caminho}`, {
            method: "POST",
            headers: {
                "Accept": "application/json",
                "Content-Type": "application/json",
                "Accept-Language":"pt-BR"

            },
            body: JSON.stringify(req.body)
        });

        const resposta = await resp;
    
        if (!resp.ok) {
            const respostaFormatada =await resp.text()
            console.error("Erro vindo do Spring:", respostaFormatada);
            return res.status(resp.status).json(respostaFormatada);
        }
        return res.status(resp.status).json(resposta);

    } catch (error) {
        console.error("Erro geral:", error);
        res.status(500).json({ erro: "Erro interno no Node" });
    }
});




module.exports = rota;