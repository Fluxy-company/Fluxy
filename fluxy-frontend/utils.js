function verificarEmail(email) {
  if (email.includes("@") && email.includes(".")) {
    return true;
  } else {
    return false;
  }
}

function senhaValida(senha){
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

function formatarTelefone(telefone) {

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



