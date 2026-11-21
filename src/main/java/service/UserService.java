package service;

import java.sql.SQLException;

import dto.EmissaoSenhaDto;

public class UserService {

    private final SenhasService senhasService = new SenhasService();


    public EmissaoSenhaDto solicitarSenha(String tipo, boolean prioridade) throws SQLException {
        return senhasService.emitirSenha(tipo, prioridade);
    }
}