package Dao;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import Model.Emprestimos;
import View.ConexaoBD;

public class EmprestimosDao {

    public static void registrarEmprestimo(Emprestimos e) {
        String sql = "INSERT INTO emprestimos(cd_livro, cd_usuario, cd_funcionario, dt_emprestimo, dt_devolucao_prevista) VALUES (?, ?, ?, ?, ?)";

        try (Connection con = ConexaoBD.getConexao();
                PreparedStatement stm = con.prepareStatement(sql)) {

            stm.setInt(1, e.getCd_livro());
            stm.setInt(2, e.getCd_usuario());
            stm.setInt(3, e.getCd_funcionario());
            stm.setDate(4, java.sql.Date.valueOf(e.getDt_emprestimo()));
            stm.setDate(5, java.sql.Date.valueOf(e.getDt_devolucao_prevista()));
            stm.execute();

        } catch (SQLException ex) {
            System.err.println("Erro ao registrar empréstimo: " + ex.getMessage());
        }
    }

    public static void registrarDevolucao(int cdEmprestimo, LocalDate dtDevolucaoReal) {
        String sql = "UPDATE emprestimos SET dt_devolucao_real = ? WHERE cd_emprestimo = ?";

        try (Connection con = ConexaoBD.getConexao();
                PreparedStatement stm = con.prepareStatement(sql)) {

            stm.setDate(1, java.sql.Date.valueOf(dtDevolucaoReal));
            stm.setInt(2, cdEmprestimo);
            stm.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Erro ao registrar devolução: " + e.getMessage());
        }
    }

    public static Emprestimos buscarEmprestimoAtivoPorLivro(int cdLivro) {
        String sql = """
                SELECT e.cd_emprestimo, e.cd_usuario, u.nm_usuario, e.dt_emprestimo, e.dt_devolucao_prevista
                FROM emprestimos e
                JOIN usuarios u ON u.cd_usuario = e.cd_usuario
                WHERE e.cd_livro = ? AND e.dt_devolucao_real IS NULL
                """;

        try (Connection con = ConexaoBD.getConexao();
                PreparedStatement stm = con.prepareStatement(sql)) {

            stm.setInt(1, cdLivro);
            ResultSet rs = stm.executeQuery();

            if (rs.next()) {
                Emprestimos e = new Emprestimos();
                e.setCd_emprestimo(rs.getInt("cd_emprestimo"));
                e.setCd_livro(cdLivro);
                e.setCd_usuario(rs.getInt("cd_usuario"));
                e.setNomeUsuario(rs.getString("nm_usuario"));
                e.setDt_emprestimo(rs.getDate("dt_emprestimo").toLocalDate());
                e.setDt_devolucao_prevista(rs.getDate("dt_devolucao_prevista").toLocalDate());
                return e;
            }

        } catch (SQLException e) {
            System.err.println("Erro ao buscar empréstimo ativo: " + e.getMessage());
        }

        return null;
    }

    public static List<Emprestimos> listarEmprestimosNaoDevolvidos() {
        List<Emprestimos> lista = new ArrayList<>();

        String sql = """
                SELECT e.cd_emprestimo,
                       e.cd_livro,
                       l.titulo_livro AS nome_livro,
                       e.cd_usuario,
                       u.nm_usuario AS nome_usuario,
                       e.cd_funcionario,
                       f.nm_funcionario AS nome_funcionario,
                       e.dt_emprestimo,
                       e.dt_devolucao_prevista
                FROM emprestimos e
                JOIN livros l ON l.cd_livro = e.cd_livro
                JOIN usuarios u ON u.cd_usuario = e.cd_usuario
                JOIN funcionarios f ON f.cd_funcionario = e.cd_funcionario
                WHERE e.dt_devolucao_real IS NULL
                """;

        try (Connection con = ConexaoBD.getConexao();
                PreparedStatement stm = con.prepareStatement(sql);
                ResultSet rs = stm.executeQuery()) {

            while (rs.next()) {
                Emprestimos e = new Emprestimos();
                e.setCd_emprestimo(rs.getInt("cd_emprestimo"));
                e.setCd_livro(rs.getInt("cd_livro"));
                e.setNomeLivro(rs.getString("nome_livro"));
                e.setCd_usuario(rs.getInt("cd_usuario"));
                e.setNomeUsuario(rs.getString("nome_usuario"));
                e.setCd_funcionario(rs.getInt("cd_funcionario"));
                e.setNomeFuncionario(rs.getString("nome_funcionario"));
                e.setDt_emprestimo(rs.getDate("dt_emprestimo").toLocalDate());
                e.setDt_devolucao_prevista(rs.getDate("dt_devolucao_prevista").toLocalDate());

                lista.add(e);
            }

        } catch (SQLException e) {
            System.err.println("Erro ao listar empréstimos não devolvidos: " + e.getMessage());
        }

        return lista;
    }

    public static List<Emprestimos> listarEmprestimosPendentes() {
        List<Emprestimos> lista = new ArrayList<>();

        String sql = """
                SELECT e.cd_emprestimo,
                       e.cd_livro,
                       l.titulo_livro AS nome_livro,
                       e.cd_usuario,
                       u.nm_usuario AS nome_usuario,
                       e.dt_emprestimo,
                       e.dt_devolucao_prevista
                FROM emprestimos e
                JOIN livros l ON l.cd_livro = e.cd_livro
                JOIN usuarios u ON u.cd_usuario = e.cd_usuario
                WHERE e.dt_devolucao_real IS NULL
                  AND e.dt_devolucao_prevista < CURRENT_DATE
                """;

        try (Connection con = ConexaoBD.getConexao();
                PreparedStatement stm = con.prepareStatement(sql);
                ResultSet rs = stm.executeQuery()) {

            while (rs.next()) {
                Emprestimos e = new Emprestimos();
                e.setCd_emprestimo(rs.getInt("cd_emprestimo"));
                e.setCd_livro(rs.getInt("cd_livro"));
                e.setNomeLivro(rs.getString("nome_livro"));
                e.setCd_usuario(rs.getInt("cd_usuario"));
                e.setNomeUsuario(rs.getString("nome_usuario"));
                e.setDt_emprestimo(rs.getDate("dt_emprestimo").toLocalDate());
                e.setDt_devolucao_prevista(rs.getDate("dt_devolucao_prevista").toLocalDate());

                lista.add(e);
            }

        } catch (SQLException e) {
            System.err.println("Erro ao listar empréstimos pendentes: " + e.getMessage());
        }

        return lista;
    }

    public static List<Emprestimos> listarHistoricoPorUsuario(int cdUsuario) {
        List<Emprestimos> lista = new ArrayList<>();
        String sql = """
                SELECT e.cd_emprestimo, e.cd_livro, l.titulo_livro, e.dt_emprestimo, e.dt_devolucao_real
                FROM emprestimos e
                JOIN livros l ON l.cd_livro = e.cd_livro
                WHERE e.cd_usuario = ?
                ORDER BY e.dt_emprestimo DESC
                """;

        try (Connection con = ConexaoBD.getConexao();
                PreparedStatement stm = con.prepareStatement(sql)) {

            stm.setInt(1, cdUsuario);
            ResultSet rs = stm.executeQuery();

            while (rs.next()) {
                Emprestimos e = new Emprestimos();
                e.setCd_emprestimo(rs.getInt("cd_emprestimo"));
                e.setCd_livro(rs.getInt("cd_livro"));
                e.setNomeLivro(rs.getString("titulo_livro"));
                e.setDt_emprestimo(rs.getDate("dt_emprestimo").toLocalDate());

                if (rs.getDate("dt_devolucao_real") != null) {
                    e.setDt_devolucao_real(rs.getDate("dt_devolucao_real").toLocalDate());
                }

                lista.add(e);
            }

        } catch (SQLException e) {
            System.err.println("Erro ao listar histórico por usuário: " + e.getMessage());
        }

        return lista;
    }

    public static boolean usuarioTemEmprestimosAtivos(int cdUsuario) {
        String sql = """
                    SELECT 1
                    FROM emprestimos
                    WHERE cd_usuario = ?
                      AND dt_devolucao_real IS NULL
                    LIMIT 1
                """;

        try (Connection con = ConexaoBD.getConexao();
                PreparedStatement stm = con.prepareStatement(sql)) {

            stm.setInt(1, cdUsuario);
            ResultSet rs = stm.executeQuery();

            return rs.next(); // se existir ao menos 1 linha, retorna true

        } catch (SQLException e) {
            System.err.println("Erro ao verificar empréstimos ativos do usuário: " + e.getMessage());
            return false;
        }
    }

    public static boolean usuarioTemEmprestimosPendentes(int cdUsuario) {
        String sql = """
                    SELECT 1 FROM emprestimos
                    WHERE cd_usuario = ?
                      AND dt_devolucao_real IS NULL
                      AND dt_devolucao_prevista < CURRENT_DATE
                    LIMIT 1
                """;

        try (Connection con = ConexaoBD.getConexao();
                PreparedStatement stm = con.prepareStatement(sql)) {
            stm.setInt(1, cdUsuario);
            ResultSet rs = stm.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            System.err.println("Erro ao verificar pendências: " + e.getMessage());
            return false;
        }
    }

    public static int contarEmprestimosAtivos(int cdUsuario) {
        String sql = """
                    SELECT COUNT(*) AS total
                    FROM emprestimos
                    WHERE cd_usuario = ?
                      AND dt_devolucao_real IS NULL
                """;

        try (Connection con = ConexaoBD.getConexao();
                PreparedStatement stm = con.prepareStatement(sql)) {
            stm.setInt(1, cdUsuario);
            ResultSet rs = stm.executeQuery();
            if (rs.next()) {
                return rs.getInt("total");
            }
        } catch (SQLException e) {
            System.err.println("Erro ao contar empréstimos ativos: " + e.getMessage());
        }
        return 0;
    }

    public static boolean livroEstaEmprestado(int cdLivro) {
        String sql = "SELECT COUNT(*) FROM emprestimos WHERE cd_livro = ? AND dt_devolucao_real IS NULL";
        try (Connection con = ConexaoBD.getConexao();
                PreparedStatement stm = con.prepareStatement(sql)) {
            stm.setInt(1, cdLivro);
            ResultSet rs = stm.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0; // true se houver empréstimo ativo para o livro
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

}
