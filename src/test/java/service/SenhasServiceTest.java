package service;

import static org.junit.Assert.assertEquals;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import database.ConnectionFactory;
import dto.EmissaoSenhaDto;

public class SenhasServiceTest {

    @Before
    public void limparDadosAntes() throws SQLException {
        limparTabelaFila();
    }

    @After
    public void limparDadosDepois() throws SQLException {
        limparTabelaFila();
    }

    @Test
    public void deveCalcularPessoasNaFrenteParaSenhasNormaisEPrioritaria() throws SQLException {
        SenhasService service = new SenhasService();

        EmissaoSenhaDto primeiraNormal = service.emitirSenha("E", false);
        assertEquals(0, primeiraNormal.getPessoasNaFrente());

        EmissaoSenhaDto segundaNormal = service.emitirSenha("E", false);
        assertEquals(1, segundaNormal.getPessoasNaFrente());

        EmissaoSenhaDto terceiraNormal = service.emitirSenha("E", false);
        assertEquals(2, terceiraNormal.getPessoasNaFrente());

        EmissaoSenhaDto prioritaria = service.emitirSenha("E", true);
        assertEquals(0, prioritaria.getPessoasNaFrente());
    }

    private void limparTabelaFila() throws SQLException {
        try (Connection conn = ConnectionFactory.getConnection();
             Statement statement = conn.createStatement()) {
            statement.executeUpdate("DELETE FROM senhas");
            statement.executeUpdate(
                "UPDATE controle_fila SET prioritarias_consecutivas = 0 WHERE tipo = 'E'"
            );
        }
    }
}
