package dto;

import model.Senha;

public class EmissaoSenhaDto {

    private final Senha senha;
    private final int pessoasNaFrente;

    public EmissaoSenhaDto(Senha senha,int pessoasNaFrente) {
        this.senha = senha;
        this.pessoasNaFrente = pessoasNaFrente;
    }

    public Senha getSenha() {
        return senha;
    }

    public int getPessoasNaFrente() {
        return pessoasNaFrente;
    }
}
