package controller;

import java.io.IOException;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import service.PainelService;

@WebServlet("/painel")
public class ChamadaServlet extends HttpServlet {

    private final Gson gson =
        new GsonBuilder().serializeNulls().create();

    private final PainelService painel =
        PainelService.getInstancia();

    @Override
    protected void doGet(HttpServletRequest req,
                         HttpServletResponse resp) throws IOException {

        resp.setContentType("application/json;charset=UTF-8");
        resp.setHeader("Cache-Control", "no-store");

        gson.toJson(
            painel.consultar(),
            resp.getWriter()
        );
    }
}