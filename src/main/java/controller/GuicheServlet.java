package controller;

import java.io.IOException;
import java.util.Map;

import com.google.gson.Gson;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import service.AtendenteService;

@WebServlet("/iniciarGuiche")
public class GuicheServlet extends HttpServlet{
    private final Gson gson = new Gson();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        String idString = req.getParameter("id");
        String CString = req.getParameter("CNH");
        String EString = req.getParameter("emissao");
        String LICString = req.getParameter("LIC");

        try{
            int id = Integer.parseInt(idString);
            boolean C = Boolean.parseBoolean(CString);
            boolean E = Boolean.parseBoolean(EString);
            boolean LIC = Boolean.parseBoolean(LICString);

            
            AtendenteService service = new AtendenteService(id, C, E, LIC);

            if (!service.abrirGuiche()) {
                resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                resp.getWriter().write(gson.toJson(Map.of("erro", "Não foi possível abrir o guichê.")));
                return;
            }


            HttpSession sessao = req.getSession(true);
            sessao.setAttribute("id", id);

            resp.sendRedirect("atendente.html?id=" + id);
        } catch (NumberFormatException e){
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().write(gson.toJson(Map.of("erro", "ID deve ser um número inteiro")));
        } 
    }
}
