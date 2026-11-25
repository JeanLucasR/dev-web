package model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Guiche {

    private final int id;
    private final boolean atende_cnh;
    private final boolean atende_emissao;
    private final boolean atende_lic;
    private final LocalDateTime abertura;
    
    private final List<String> tiposAtendidos;
    private int proximoTipo;

    public Guiche(int id, boolean C, boolean E, boolean LIC){
        this(id, C, E, LIC, LocalDateTime.now());
    }

    public Guiche(int id, boolean C, boolean E, boolean LIC, LocalDateTime abertura){
        this.id = id;
        this.atende_cnh = C;
        this.atende_emissao = E;
        this.atende_lic = LIC;
        this.abertura = abertura;
        this.tiposAtendidos = new ArrayList<>();

        if (C)
            tiposAtendidos.add("C");
        
        if (E)
            tiposAtendidos.add("E");
        
        if (LIC)
            tiposAtendidos.add("LIC");
        
    }

    public int getId() {
        return id;
    }

    public boolean isCNH(){
        return atende_cnh;
    }

    public boolean isEmissao(){
        return atende_emissao;
    }

    public boolean isLic(){
        return atende_lic;
    }

    public LocalDateTime getAbertura() {
        return abertura;
    }


    public String atenderTipo() {

        String tipo = tiposAtendidos.get(proximoTipo);
        proximoTipo = (proximoTipo + 1) % tiposAtendidos.size();
        return tipo;
    }

    public int getQtdTipos(){
        return tiposAtendidos.size();
    }
}
