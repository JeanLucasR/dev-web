package observer;

import org.junit.Test;
import static org.junit.Assert.*;

import dto.ChamadaDto;
import service.PainelService;

public class ChamadaSubjectTest {

    @Test
    public void testarNotificacao() {

        ChamadaSubject subject = ChamadaSubject.getInstancia();
        PainelService painel = PainelService.getInstancia();

        subject.adicionarObserver(painel);

        try {
            long versaoAnterior = painel.consultar().getVersao();

            subject.notificar(new ChamadaDto("E0001", 1));

            PainelService.EstadoPainel estado = painel.consultar();

            assertEquals("E0001", estado.getAtual().getCodigo());
            assertEquals(1, estado.getAtual().getGuiche());
            assertEquals(versaoAnterior + 1, estado.getVersao());

        } finally {
            subject.removerObserver(painel);
        }
    }

    @Test
    public void testarHistorico() {

        ChamadaSubject subject = ChamadaSubject.getInstancia();
        PainelService painel = PainelService.getInstancia();

        subject.adicionarObserver(painel);

        try {
            subject.notificar(new ChamadaDto("C0001", 1));
            subject.notificar(new ChamadaDto("E0002", 2));

            PainelService.EstadoPainel estado = painel.consultar();

            assertEquals("E0002", estado.getAtual().getCodigo());
            assertEquals(2, estado.getAtual().getGuiche());

            assertEquals(
                "C0001",
                estado.getUltimas().get(0).getCodigo()
            );

        } finally {
            subject.removerObserver(painel);
        }
    }

    @Test
    public void testarLimiteHistorico() {

        ChamadaSubject subject = ChamadaSubject.getInstancia();
        PainelService painel = PainelService.getInstancia();

        subject.adicionarObserver(painel);

        try {
            for (int i = 1; i <= 10; i++) {
                subject.notificar(new ChamadaDto("E" + i, 1));
            }

            PainelService.EstadoPainel estado = painel.consultar();

            assertEquals(5, estado.getUltimas().size());
            assertEquals("E10", estado.getAtual().getCodigo());
            assertEquals("E9", estado.getUltimas().get(0).getCodigo());
            assertEquals("E5", estado.getUltimas().get(4).getCodigo());

        } finally {
            subject.removerObserver(painel);
        }
    }
}