package dao;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import database.ConnectionFactory;
import model.Senha;

public class SenhasDaoTest {

    @Before
    public void limparDadosAntesDoTeste() throws SQLException {
        limparTabelaSenhas();
    }

    @After
    public void limparDadosDepoisDoTeste() throws SQLException {
        limparTabelaSenhas();
    }

    @Test
    public void deveSalvarDuasSenhasAlterarUmaParaChamadoEListaLas() throws SQLException {
        SenhasDao dao = new SenhasDao();
        LocalDateTime aberturaGuiche = LocalDateTime.now().minusMinutes(1);

        Senha primeiraSenha = new Senha(false, "T");
        Senha segundaSenha = new Senha(true, "T");

        dao.salvarSenha(primeiraSenha);
        dao.salvarSenha(segundaSenha);

        assertTrue(
            "A primeira senha deve ser atualizada",
            dao.atualizarStatus(primeiraSenha, "CHAMADO", aberturaGuiche)
        );

        List<Senha> senhas = dao.listar(aberturaGuiche);

        Senha primeiraSenhaListada = encontrarSenha(senhas, primeiraSenha.getCodigo());
        Senha segundaSenhaListada = encontrarSenha(senhas, segundaSenha.getCodigo());

        assertNotNull(primeiraSenhaListada);
        assertNotNull(segundaSenhaListada);
        assertTrue("A primeira senha deve estar CHAMADO", "CHAMADO".equals(primeiraSenhaListada.getStatus()));
        assertFalse("A segunda senha nao deve estar CHAMADO", "CHAMADO".equals(segundaSenhaListada.getStatus()));
    }

    private Senha encontrarSenha(List<Senha> senhas, String codigo) {
        for (Senha senha : senhas) {
            if (codigo.equals(senha.getCodigo())) {
                return senha;
            }
        }
        return null;
    }

    private void limparTabelaSenhas() throws SQLException {
        try (Connection conn = ConnectionFactory.getConnection();
             Statement statement = conn.createStatement()) {
            statement.executeUpdate("DELETE FROM senhas");
        }
    }
}
