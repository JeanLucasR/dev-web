package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import database.ConnectionFactory;
import model.Senha;

public class SenhasDao {

    public void salvarSenha(Senha senha) throws SQLException {
        String sql = "INSERT INTO senhas (codigo, prioridade, tipo, emissao) VALUES (?, ?, ?, ?)";

        try (
            Connection conn = ConnectionFactory.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)
        ){
            ps.setString(1, senha.getCodigo());
            ps.setBoolean(2, senha.isPrioridade());
            ps.setString(3, senha.getTipo());

            LocalDateTime emissao = LocalDateTime.now();

            ps.setTimestamp(4, Timestamp.valueOf(emissao));

            ps.executeUpdate();
        }
    }


    public boolean atualizarStatus(Senha senha, String status, LocalDateTime aberturaGuiche) throws SQLException {
        String sql = " UPDATE senhas SET stat = ? WHERE codigo = ? AND emissao >= ? ";

        try(
            Connection conn = ConnectionFactory.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)
        ){
            ps.setString(1, status);
            ps.setString(2, senha.getCodigo());
            ps.setTimestamp(3, Timestamp.valueOf(aberturaGuiche));

            int linhasAlteradas = ps.executeUpdate();

            return linhasAlteradas > 0;

        }
    }

    public List<Senha> listar(LocalDateTime aberturaGuiche) throws SQLException {
        String sql = "SELECT codigo, prioridade, tipo, stat, emissao FROM senhas WHERE emissao >= ?";
        List<Senha> senhas = new ArrayList<>();

        try(
            Connection conn = ConnectionFactory.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)
        ){
            ps.setTimestamp(1, Timestamp.valueOf(aberturaGuiche));
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                Senha senha = new Senha(
                    rs.getString("codigo"),
                    rs.getBoolean("prioridade"),
                    rs.getString("tipo"),
                    rs.getString("stat")
                );

                senhas.add(senha);
            }

            return senhas;
        }
    }

    public List<Senha> listarAguardandoPorTipo(String tipo)
        throws SQLException {

    String sql = "SELECT codigo, prioridade, tipo, stat FROM senhas WHERE tipo = ? AND stat = 'AGUARDANDO' ORDER BY emissao ASC, ID ASC";

    List<Senha> senhas = new ArrayList<>();

    try (
        Connection conn = ConnectionFactory.getConnection();
        PreparedStatement ps = conn.prepareStatement(sql)
    ) {
        ps.setString(1, tipo);

        try (ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Senha senha = new Senha(
                    rs.getString("codigo"),
                    rs.getBoolean("prioridade"),
                    rs.getString("tipo"),
                    rs.getString("stat")
                );

                senhas.add(senha);
            }
        }
    }

    return senhas;
    }

public int buscarContadorPrioridade(String tipo)
        throws SQLException {

    String sql = "SELECT prioritarias_consecutivas FROM controle_fila WHERE tipo = ?";

    try (
        Connection conn = ConnectionFactory.getConnection();
        PreparedStatement ps = conn.prepareStatement(sql)
    ) {
        ps.setString(1, tipo);

        try (ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return rs.getInt("prioritarias_consecutivas");
            }
        }
    }

    throw new SQLException("Tipo de atendimento não cadastrado.");
    }

    public void atualizarContadorPrioridade(
        String tipo,
        int contador
) throws SQLException {

    String sql = "UPDATE controle_fila SET prioritarias_consecutivas = ? WHERE tipo = ?";

    try (
        Connection conn = ConnectionFactory.getConnection();
        PreparedStatement ps = conn.prepareStatement(sql)
    ) {
        ps.setInt(1, contador);
        ps.setString(2, tipo);

        if (ps.executeUpdate() == 0) {
            throw new SQLException("Tipo de atendimento não encontrado.");
        }
    }
}
}
