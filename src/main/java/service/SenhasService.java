package service;

import java.sql.SQLException;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;
import java.util.Set;

import dao.SenhasDao;
import dto.EmissaoSenhaDto;
import model.Senha;

public class SenhasService {
    private final SenhasDao dao = new SenhasDao();


    // E = Emissão de documento
    // C = Renovação CNH
    // LIC = Licenciamento anual de veículos
    private static final Set<String> TIPOS_VALIDOS = Set.of("E", "C", "LIC");



    private int calcularPosicao(Senha novaSenha, List<Senha> fila, int consecutivas){
        Deque<Senha> prioritarias = new ArrayDeque<>();
        Deque<Senha> normais = new ArrayDeque<>();

        for(Senha senha : fila)
            if(senha.isPrioridade())
                prioritarias.addLast(senha);
            else
                normais.addLast(senha);

        
        int pessoasNaFrente = 0;


        Senha proxima;
        while(!prioritarias.isEmpty() || !normais.isEmpty()){
            if(consecutivas < 2 && !prioritarias.isEmpty()){
                proxima = prioritarias.removeFirst();
                consecutivas++;
            }
            else if(!normais.isEmpty()){
                proxima = normais.removeFirst();
                consecutivas = 0;
            }
            else
            {
                proxima = prioritarias.removeFirst();
                consecutivas = 2;
            }

            if (proxima.getCodigo().equals(novaSenha.getCodigo())) 
                return pessoasNaFrente;

            pessoasNaFrente++;
        }

        throw new IllegalStateException("Senha emitida não encontrada na fila.");
        
    }


    public synchronized EmissaoSenhaDto emitirSenha( String tipo, boolean prioridade) throws SQLException {
        if(tipo == null || !TIPOS_VALIDOS.contains(tipo)){
            throw new IllegalArgumentException("Tipo de atendimento inválido.");
        }


         Senha senha = new Senha(prioridade, tipo);

    
    System.out.println(senha.getCodigo());
    System.out.println(senha.getTipo());
    System.out.println(senha.isPrioridade());

    dao.salvarSenha(senha);

    
    List<Senha> fila = dao.listarAguardandoPorTipo(tipo);

   
    int consecutivas = dao.buscarContadorPrioridade(tipo);

 
    int pessoasNaFrente = calcularPosicao(
        senha, fila, consecutivas
    );

        return new EmissaoSenhaDto(senha, pessoasNaFrente);
    }

}
