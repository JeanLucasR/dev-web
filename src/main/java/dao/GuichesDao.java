package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;

import database.ConnectionFactory;
import dto.GuicheDto;
import model.Guiche;


public class GuichesDao {
    public void abrirGuiche(Guiche guiche) throws SQLException {
        String sql = "UPDATE guiches SET atende_cnh = ?, atende_emissao = ?, atende_lic = ?, abertura = ? WHERE id = ?";

        try(
            Connection conn = ConnectionFactory.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
        ){
            ps.setBoolean(1, guiche.isCNH());
            ps.setBoolean(2, guiche.isEmissao());
            ps.setBoolean(3, guiche.isLic());
            ps.setTimestamp(4, Timestamp.valueOf(guiche.getAbertura()));
            ps.setInt(5, guiche.getId());

            ps.executeUpdate();
        }catch(SQLException e){
            throw new RuntimeException("Erro ao abrir o guiche no banco de dados", e);
        }
    }

    public GuicheDto recuperarGuiche(int id) throws SQLException {
        String sql = "SELECT id, atende_cnh, atende_emissao, atende_lic, abertura FROM guiches WHERE id = ?";

        try (
            Connection conn = ConnectionFactory.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)
        ) {
            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Timestamp aberturaTimestamp = rs.getTimestamp("abertura");

                    LocalDateTime abertura = aberturaTimestamp != null ? aberturaTimestamp.toLocalDateTime() : null;

                    return new GuicheDto(
                        rs.getInt("id"),
                        rs.getBoolean("atende_cnh"),
                        rs.getBoolean("atende_emissao"),
                        rs.getBoolean("atende_lic"),
                        abertura
                    );
                }

                return null; // Guichê não encontrado
            }
        }
    }
}
