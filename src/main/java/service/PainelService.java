package service;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;

import dto.ChamadaDto;
import observer.ChamadaObserver;

public class PainelService implements ChamadaObserver {

    private static final PainelService instancia =
        new PainelService();

    private static final int LIMITE = 5;

    private ChamadaDto atual;

    private final Deque<ChamadaDto> ultimas =
        new ArrayDeque<>();

    private long versao = 0;

    private PainelService() {}

    public static PainelService getInstancia() {
        return instancia;
    }

    @Override
    public synchronized void atualizar(ChamadaDto chamada) {

        if (atual != null) {
            ultimas.addFirst(atual);

            if (ultimas.size() > LIMITE) {
                ultimas.removeLast();
            }
        }

        atual = chamada;
        versao++;
    }

    public synchronized EstadoPainel consultar() {
        return new EstadoPainel(
            versao,
            atual,
            List.copyOf(ultimas)
        );
    }

    public static class EstadoPainel {

        private final long versao;
        private final ChamadaDto atual;
        private final List<ChamadaDto> ultimas;

        public EstadoPainel(long versao,
                           ChamadaDto atual,
                           List<ChamadaDto> ultimas) {

            this.versao = versao;
            this.atual = atual;
            this.ultimas = List.copyOf(ultimas);
        }

        public long getVersao() {
            return versao;
        }

        public ChamadaDto getAtual() {
            return atual;
        }

        public List<ChamadaDto> getUltimas() {
            return ultimas;
        }
    }
}