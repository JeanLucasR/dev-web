package model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ConcurrentHashMap;

import org.junit.Test;

public class SenhaConcurrencyTest {

    @Test
    public void deveGerarCodigosUnicosParaCriacoesSimultaneas() throws Exception {
        int quantidadeDeThreads = 8;
        int senhasPorThread = 500;
        CountDownLatch prontas = new CountDownLatch(quantidadeDeThreads);
        CountDownLatch iniciar = new CountDownLatch(1);
        Set<String> codigos = ConcurrentHashMap.newKeySet();
        ExecutorService executor = Executors.newFixedThreadPool(quantidadeDeThreads);
        List<Future<?>> tarefas = new ArrayList<>();

        try {
            for (int i = 0; i < quantidadeDeThreads; i++) {
                tarefas.add(executor.submit(() -> {
                    prontas.countDown();
                    iniciar.await();
                    for (int j = 0; j < senhasPorThread; j++) {
                        assertTrue(codigos.add(new Senha(false, "E").getCodigo()));
                    }
                    return null;
                }));
            }

            prontas.await();
            iniciar.countDown();
            for (Future<?> tarefa : tarefas) {
                tarefa.get();
            }

            assertEquals(quantidadeDeThreads * senhasPorThread, codigos.size());
        } finally {
            executor.shutdownNow();
        }
    }
}
