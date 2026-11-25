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

@WebServlet("/chamarSenha")
public class AtendenteServlet extends HttpServlet {
    private final Gson gson = new Gson();


    @Override 
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json;charset=UTF-8");

        HttpSession sessao = req.getSession(false);

        if(sessao == null || sessao.getAttribute("id") == null){
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            gson.toJson(Map.of("erro", "Guiche não iniciado"),  resp.getWriter());
            return;
        }

        try{
            int id = (Integer) sessao.getAttribute("id");
            AtendenteService service = new AtendenteService(id);

            gson.toJson(service.listar(), resp.getWriter());
        } catch (Exception e){
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            gson.toJson(Map.of("erro", "Erro ao listar senhas"), resp.getWriter());
        }
    }


    //TODO: implementar as operações de post de chamar uma senha, chamar novamente uma senha chamada, iniciar atendimento, finalizar atendimento e marcar senha como ausente

    

    
}


