package dto;

import java.time.LocalDateTime;

public record GuicheDto(int id, boolean atende_cnh, boolean atende_emissao, boolean atende_lic, LocalDateTime abertura) {}
