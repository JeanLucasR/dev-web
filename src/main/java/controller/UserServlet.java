package controller;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;

import dto.EmissaoSenhaDto;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import service.UserService;

@WebServlet("/usuarioSenha")
public class UserServlet extends HttpServlet {
    private final UserService service = new UserService();
    private final Gson gson = new Gson();


    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        req.setCharacterEncoding("UTF-8");
        resp.setContentType("application/json;charset=UTF-8");

        try {
            JsonElement body = JsonParser.parseReader(req.getReader());

            JsonObject entrada = body.getAsJsonObject();
            JsonElement tipoElement = entrada.get("tipo");
            JsonElement prioridadeElement = entrada.get("prioridade");
            
            

            EmissaoSenhaDto emissao = service.solicitarSenha(
                    tipoElement.getAsString(),
                    prioridadeElement.getAsBoolean());
            resp.setStatus(HttpServletResponse.SC_CREATED);
            gson.toJson(Map.of(
                    "mensagem", "Senha criada com sucesso.",
                    "codigo", emissao.getSenha().getCodigo(),
                    "pessoasNaFrente", emissao.getPessoasNaFrente()),
                    resp.getWriter()
                );

        } catch (JsonParseException ex) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().write(gson.toJson(Map.of("erro", "JSON malformado.")));
        } catch (IllegalArgumentException ex) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().write(gson.toJson(Map.of("erro", ex.getMessage())));
        } catch (SQLException ex) {
            log("Erro ao emitir senha.", ex);
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().write(gson.toJson(Map.of("erro", "Não foi possível emitir a senha.")));
        }
    }
}
