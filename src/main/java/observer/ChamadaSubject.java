package observer;

import java.util.concurrent.CopyOnWriteArrayList;

import dto.ChamadaDto;

public class ChamadaSubject {

    private static final ChamadaSubject instancia =
        new ChamadaSubject();

    private final CopyOnWriteArrayList<ChamadaObserver> observadores =
        new CopyOnWriteArrayList<>();

    private ChamadaSubject() {}

    public static ChamadaSubject getInstancia() {
        return instancia;
    }

    public void adicionarObserver(ChamadaObserver observer) {
        observadores.addIfAbsent(observer);
    }

    public void removerObserver(ChamadaObserver observer) {
        observadores.remove(observer);
    }

    public synchronized void notificar(ChamadaDto chamada) {
        for (ChamadaObserver observer : observadores) {
            observer.atualizar(chamada);
        }
    }
}