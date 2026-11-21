package controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.lang.reflect.Proxy;
import java.sql.SQLException;

import org.junit.Test;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import dto.EmissaoSenhaDto;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.Senha;
import service.UserService;

public class UserServletTest {

    @Test
    public void deveRetornarCodigoEFilaQuandoRequisicaoForValida() throws IOException {
        StubUserService service = new StubUserService();
        CapturedResponse response = post(
                "{\"tipo\":\"E\",\"prioridade\":true}",
                service);

        JsonObject json = JsonParser.parseString(response.body.toString()).getAsJsonObject();
        assertEquals(HttpServletResponse.SC_CREATED, response.status);
        assertEquals("E", service.tipo);
        assertTrue(service.prioridade);
        assertEquals("Senha criada com sucesso.", json.get("mensagem").getAsString());
        assertEquals(service.emissao.getSenha().getCodigo(), json.get("codigo").getAsString());
        assertEquals(3, json.get("pessoasNaFrente").getAsInt());
    }

    @Test
    public void deveRetornarBadRequestParaJsonMalformado() throws IOException {
        CapturedResponse response = post("{", new StubUserService());

        assertEquals(HttpServletResponse.SC_BAD_REQUEST, response.status);
        assertTrue(response.body.toString().contains("JSON malformado"));
    }

    @Test
    public void deveRetornarBadRequestParaCorpoNulo() throws IOException {
        CapturedResponse response = post("null", new StubUserService());

        assertEquals(HttpServletResponse.SC_BAD_REQUEST, response.status);
    }

    @Test
    public void deveRetornarBadRequestQuandoPrioridadeEstiverAusenteOuNaoForBoolean() throws IOException {
        CapturedResponse ausente = post("{\"tipo\":\"E\"}", new StubUserService());
        CapturedResponse invalida = post(
                "{\"tipo\":\"E\",\"prioridade\":\"talvez\"}",
                new StubUserService());

        assertEquals(HttpServletResponse.SC_BAD_REQUEST, ausente.status);
        assertEquals(HttpServletResponse.SC_BAD_REQUEST, invalida.status);
    }

    @Test
    public void deveRetornarInternalServerErrorQuandoServicoFalharNoBanco() throws IOException {
        StubUserService service = new StubUserService();
        service.failure = new SQLException("database unavailable");

        CapturedResponse response = post(
                "{\"tipo\":\"E\",\"prioridade\":false}",
                service);

        assertEquals(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, response.status);
        assertTrue(response.body.toString().contains("Não foi possível emitir a senha."));
    }

    private CapturedResponse post(String body, StubUserService service) throws IOException {
        CapturedResponse captured = new CapturedResponse();
        UserServlet servlet = new UserServlet(service) {
            @Override
            public void log(String message, Throwable throwable) {
            }
        };
        servlet.doPost(request(body), response(captured));
        return captured;
    }

    private HttpServletRequest request(String body) {
        return (HttpServletRequest) Proxy.newProxyInstance(
                HttpServletRequest.class.getClassLoader(),
                new Class<?>[] { HttpServletRequest.class },
                (proxy, method, args) -> {
                    if ("getReader".equals(method.getName())) {
                        return new BufferedReader(new StringReader(body));
                    }
                    return null;
                });
    }

    private HttpServletResponse response(CapturedResponse captured) {
        return (HttpServletResponse) Proxy.newProxyInstance(
                HttpServletResponse.class.getClassLoader(),
                new Class<?>[] { HttpServletResponse.class },
                (proxy, method, args) -> {
                    if ("getWriter".equals(method.getName())) {
                        return new PrintWriter(captured.body);
                    }
                    if ("setStatus".equals(method.getName())) {
                        captured.status = (int) args[0];
                    }
                    return null;
                });
    }

    private static class CapturedResponse {
        private int status;
        private final StringWriter body = new StringWriter();
    }

    private static class StubUserService extends UserService {
        private String tipo;
        private boolean prioridade;
        private SQLException failure;
        private EmissaoSenhaDto emissao;

        @Override
        public EmissaoSenhaDto solicitarSenha(String tipo, boolean prioridade) throws SQLException {
            this.tipo = tipo;
            this.prioridade = prioridade;
            if (failure != null) {
                throw failure;
            }
            emissao = new EmissaoSenhaDto(new Senha(prioridade, tipo), 3);
            return emissao;
        }
    }
}
