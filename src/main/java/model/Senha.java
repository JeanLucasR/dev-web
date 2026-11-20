package model;

import java.util.concurrent.atomic.AtomicInteger;

public class Senha{
    private static final AtomicInteger numero = new AtomicInteger(0);


    private final String codigo;
    private final boolean prioridade;
    private final String tipo;
    private String status;

    public Senha(boolean prioridade, String tipo){
        this.prioridade = prioridade;
        this.tipo = tipo;
        this.status = "AGUARDANDO";
        
        int valor = numero.incrementAndGet();

        String P = prioridade ? "P" : "";
        String numero_string = String.format("%04d", valor);


        this.codigo = tipo + P + numero_string;
    }

    public Senha(String codigo, boolean prioridade, String tipo, String status){
        this.codigo = codigo;
        this.prioridade = prioridade;
        this.tipo = tipo;
        this.status = status;
    }

    public String getCodigo() {
        return codigo;
    }

    public boolean isPrioridade() {
        return prioridade;
    }

    public String getTipo() {
        return tipo;
    }

    public String getStatus(){
        return status;
    }

    public void setStatus(String status){
        this.status = status;
    }
}