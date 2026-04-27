let porta_spring = import.meta.env.VITE_SPRING_PORT;
let servico_spring = import.meta.env.VITE_SERVICO_SPRING;


export function verificarEmail(email) {
  if (email.includes("@") && email.includes(".")) {
    return true;
  } else {
    return false;
  }
}

export function senhaValida(senha){
    return senha.length >= 6;
}

function formatarCnpj(cnpj) {

    cnpj = cnpj.replace(/\D/g, "");

    if (cnpj.length !== 14) {
        return cnpj;
    }

    return cnpj.replace(
        /^(\d{2})(\d{3})(\d{3})(\d{4})(\d{2})$/,
        "$1.$2.$3/$4-$5"
    );
}

export function formatarTelefone(telefone) {

    telefone = telefone.replace(/\D/g, "");

    if (telefone.length == 11) {
        return telefone.replace(
            /^(\d{2})(\d{5})(\d{4})$/,
            "($1) $2-$3"
        );
    }

    if (telefone.length == 10) {
        return telefone.replace(
            /^(\d{2})(\d{4})(\d{4})$/,
            "($1) $2-$3"
        );
    }

    return telefone;
}



export const postar_banco = async (caminho_url, corpo) =>{
    try{

        let token = localStorage.getItem('token')

        const resposta = await fetch(`http://${servico_spring}:${porta_spring}/${caminho_url}`, {
            method: "POST",
            headers: {
                "Accept": "application/json",
                "Content-Type": "application/json",
                "Accept-Language":"pt-BR",
                "Authorization": `Bearer ${token}`

            },
            body: JSON.stringify(corpo)
        });

         
    
        if (!resposta.ok) {
            const respostaFormatada =await resposta.text()
            console.error("Erro vindo do Spring:", respostaFormatada);
            return {status: resposta.status, erro: respostaFormatada}
        }

        return resposta;

    }catch(error){
        console.error("Erro geral:", error);
        // res.status(500).json({ erro: "Erro interno no Node" });
    }
}


export const gerarPdf = async (caminho_url, corpo) =>{
    try{

        let token = localStorage.getItem('token')
        
        const resp = await fetch(`http://${servico_spring}:${porta_spring}/${caminho_url}`, {
            method: "POST",
            headers: {
                "Accept": "application/pdf",
                "Content-Type": "application/json",
                "Accept-Language":"pt-BR",
                "Authorization": `Bearer ${token}`
            },
            body: JSON.stringify(corpo)
        });
        if(!(resp.ok)){

            return [];
        }

        return resp;
    }
    catch(error){
        console.error("Erro: ", error);
        return error;
    }
}



export const listar_banco = async (caminho_url) =>{
    try{

        let token = localStorage.getItem('token')
        
        const resp = await fetch(`http://${servico_spring}:${porta_spring}/${caminho_url}`, {
            method: "GET",
            headers: {
                "Accept": "application/json",
                "Content-Type": "application/json",
                "Accept-Language":"pt-BR",
                "Authorization": `Bearer ${token}`
            }        
        });
        if(!(resp.ok)){

            return [];
        }

        return resp;
    }
    catch(error){
        console.error("Erro: ", error);
        return error;
    }
}

export const achar_id_banco = async (caminho_url,id) =>{
    console.log(caminho_url)
    console.log(id)
    try{
        
        let token = localStorage.getItem('token')
        
        const resp = await fetch(`http://${servico_spring}:${porta_spring}/${caminho_url}/${id}`, {
            method: "GET",
            headers: {
                "Accept": "application/json",
                "Content-Type": "application/json",
                "Accept-Language":"pt-BR",
                "Authorization": `Bearer ${token}`
            }        
        });
        return resp;
    }
    catch(error){
        console.error("Erro: ", error);
        return error;
    }
}


export const remover_banco = async (caminho_url,id) =>{
    try{

        let token = localStorage.getItem('token')

        const resp = await fetch(`http://${servico_spring}:${porta_spring}/${caminho_url}/${id}`, {
            method: "DELETE",
            headers: {
                "Accept": "application/json",
                "Content-Type": "application/json",
                "Accept-Language":"pt-BR",
                "Authorization": `Bearer ${token}`
            }        
        });
        if(resp.ok){
            return "removido com sucesso!"
        }
        else{
            return "Id não existe dentro do sistema"
        }
    }
    catch(error){
        console.error("Erro: ", error);
        return error.json();
    }
}
export const atualizar_banco = async (caminho_url, id, corpo) =>{
    try{
        let token = localStorage.getItem('token')


         const resp = await fetch(`http://${servico_spring}:${porta_spring}/${caminho_url}/${id}`, {
            method: "PUT",
            headers: {
                "Accept": "application/json",
                "Content-Type": "application/json",
                "Accept-Language":"pt-BR",
                "Authorization": `Bearer ${token}`
            },
            body: JSON.stringify(corpo)
});
        if(resp.ok){

            return "atualizado com sucesso!"
        }else{
            const respostaFormatada =await resp.text()
            console.error("Erro vindo do Spring:", respostaFormatada);
            return {status: resp.status, erro: respostaFormatada}
        } 
        

    }catch(error){
        console.error("Erro: ", error);
        return error.json();
    }
}


export const autenticar_banco = async (caminho_url, corpo) =>{
    try{
        const resp = await fetch(`http://${servico_spring}:${porta_spring}/${caminho_url}`, {
            method: "POST",
            headers: {
                "Accept": "application/json",
                "Content-Type": "application/json",
                "Accept-Language":"pt-BR",

            },
            body: JSON.stringify(corpo)
        });
    
         if (!resp.ok) {
             const respostaFormatada =await resp.text()
             console.error("Erro vindo do Spring:", respostaFormatada);
             return {status: resp.status, erro: respostaFormatada}
         }

        return resp;

    }catch(error){
        console.error("Erro geral:", error);
    }
}


// Funçao que ainda precisa ver se vai ser usada 
const analisar_ExpiToken = () => {
    const dataAtual = new Date();
    const dataNoLocalStorage = localStorage.getItem('fimToken')
    
    if(dataAtual <= dataNoLocalStorage){
        return true
    }
    return false;
    
}


