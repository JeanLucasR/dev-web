package dto;

public class ChamadaDto {

    private final String codigo;
    private final int guiche;

    public ChamadaDto(String codigo, int guiche) {
        this.codigo = codigo;
        this.guiche = guiche;
    }

    public String getCodigo() {
        return codigo;
    }

    public int getGuiche() {
        return guiche;
    }
}