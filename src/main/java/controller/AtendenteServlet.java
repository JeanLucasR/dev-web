package controller;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;


import service.AtendenteService;
import dto.SenhasDto;
import model.Senha;

@WebServlet("/atendente/*")
public class AtendenteServlet extends HttpServlet {
    private final Gson gson = new Gson();


    @Override 
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException{
        resp.setContentType("application/json;charset=UTF-8");

        HttpSession sessao = req.getSession(false);

        if(sessao == null || sessao.getAttribute("id") == null){
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            gson.toJson(Map.of("erro", "Guiche não iniciado"),  resp.getWriter());
            return;
        }

        
        int id = (Integer) sessao.getAttribute("id");

        try{
            AtendenteService service = new AtendenteService(id);
            String caminho = req.getPathInfo();

            if("/listar".equals(caminho))
                gson.toJson(service.listar(), resp.getWriter());
            
            else
                resp.setStatus(HttpServletResponse.SC_NOT_FOUND);

        }catch(SQLException e){
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            gson.toJson(Map.of("erro", e.getMessage()), resp.getWriter());
        }
    }

    @Override 
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException{
        HttpSession sessao = req.getSession(false);

        if (sessao == null || sessao.getAttribute("id") == null) {
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        int id = (Integer) sessao.getAttribute("id");

        try{

            AtendenteService service = new AtendenteService(id);

            String caminho = req.getPathInfo();

            if ("/chamar".equals(caminho)) {
                Senha senha = service.chamarSenha();

                resp.setStatus(HttpServletResponse.SC_OK);

                gson.toJson(
                    Map.of("mensagem", "Senha chamada",
                        "senha", senha.getCodigo()
                    ),
                    resp.getWriter());

                //TODO: implementar observer para o painel de senhas chamadas
            }
            else if("/iniciar".equals(caminho)){
                JsonElement body = JsonParser.parseReader(req.getReader());

                JsonObject entrada = body.getAsJsonObject();
                JsonElement codigoElement = entrada.get("codigo");
             

                service.iniciarAtendimento(codigoElement.getAsString());
            }
            else if ("/finalizar".equals(caminho)) {
                JsonElement body = JsonParser.parseReader(req.getReader());

                JsonObject entrada = body.getAsJsonObject();
                JsonElement codigoElement = entrada.get("codigo");

                service.finalizarAtendimento(codigoElement.getAsString());
            }
            else if ("/ausente".equals(caminho)) {
                JsonElement body = JsonParser.parseReader(req.getReader());

                JsonObject entrada = body.getAsJsonObject();
                JsonElement codigoElement = entrada.get("codigo");

                service.marcarAusente(codigoElement.getAsString());
            }
            else {
                resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
            }
        }catch(SQLException e){
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            gson.toJson(Map.of("erro", e.getMessage()), resp.getWriter());
        }

    }


    

    

    
}


