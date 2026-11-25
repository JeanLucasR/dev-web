package service;

import java.sql.SQLException;
import java.util.List;

import dao.GuichesDao;
import dao.SenhasDao;
import dto.GuicheDto;
import model.Guiche;
import model.Senha;

public class AtendenteService { 

    private final SenhasDao senhasDao = new SenhasDao();
    private final GuichesDao guichesDao = new GuichesDao();

    private final Guiche guiche;

    public AtendenteService(int id, boolean C, boolean E, boolean LIC){
        guiche = new Guiche(id, C, E, LIC);
    }

    public AtendenteService(int id) throws SQLException{
        GuicheDto dto = guichesDao.recuperarGuiche(id);
        guiche = new Guiche(id, dto.atende_cnh(), dto.atende_emissao(), dto.atende_lic());
    }

    public boolean abrirGuiche(){
        try{
            guichesDao.abrirGuiche(guiche);
            return true;
        }catch (SQLException e){
            return false;
        }
    }

    public Senha chamarSenha() throws SQLException {
        for (int i = 0; i < guiche.getQtdTipos(); i++) {
            String tipo = guiche.atenderTipo();
            int contadorPrioridade = senhasDao.buscarContadorPrioridade(tipo);
            List<Senha> fila = senhasDao.listarAguardandoPorTipo(tipo, guiche.getAbertura());

            if (fila.isEmpty())
                continue;
            

            boolean prioridadeEsperada = contadorPrioridade < 2;
            for (int prioridadePasso = 0; prioridadePasso < 2; prioridadePasso++) {
                boolean prioridadeAtual = prioridadePasso == 0 ? prioridadeEsperada : !prioridadeEsperada;
                for (Senha senha : fila) {
                    if (senha.isPrioridade() != prioridadeAtual) 
                        continue;
                    
                    if (senhasDao.chamarSenha(senha, guiche.getAbertura())) {
                        senha.setStatus("CHAMADA");
                        int proximoContador = senha.isPrioridade() ? Math.min(contadorPrioridade + 1, 2) : 0;
                        senhasDao.atualizarContadorPrioridade(tipo, proximoContador);
                        return senha;
                    }
                }
            }
        }

        return null;
    }

    public List<Senha> listar() throws SQLException {
        return senhasDao.listar(guiche.getAbertura());
    }

    public void iniciarAtendimento(Senha senha) throws SQLException{
        senhasDao.iniciarAtendimento(senha, guiche.getAbertura());
    }

    public void finalizarAtendimento(Senha senha) throws SQLException {
        senhasDao.finalizarAtendimento(senha, guiche.getAbertura());
    }

    public void marcarAusente(Senha senha) throws SQLException {
        senhasDao.marcarAusente(senha, guiche.getAbertura());
    }

}