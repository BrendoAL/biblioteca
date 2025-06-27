package Dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import Model.Livros;
import View.ConexaoBD;

public class LivrosDao {

    public static List<Livros> getAll() {
        List<Livros> livros = new ArrayList<>();
        String sql = "SELECT * FROM livros";

        try (Connection con = ConexaoBD.getConexao();
             PreparedStatement stm = con.prepareStatement(sql);
             ResultSet rs = stm.executeQuery()) {

            while (rs.next()) {
                Livros l = new Livros();
                l.setCd_livro(rs.getInt("cd_livro"));
                l.setTitulo_livro(rs.getString("titulo_livro"));
                l.setAutor_livro(rs.getString("autor_livro"));
                l.setIsbn(rs.getString("isbn"));
                l.setAno_publicado(rs.getString("ano_publicado"));
                l.setEmprestado(rs.getString("emprestado"));
                livros.add(l);
            }

        } catch (SQLException e) {
            System.out.println("Erro ao listar livros: " + e.getMessage());
        }

        return livros;
    }

    public static Livros inserirLivros(Livros livro) {
        String sql = "INSERT INTO livros (titulo_livro, ano_publicado, autor_livro, isbn, emprestado) VALUES (?, ?, ?, ?, ?)";

        try (Connection con = ConexaoBD.getConexao();
             PreparedStatement stm = con.prepareStatement(sql)) {

            stm.setString(1, livro.getTitulo_livro());
            stm.setString(2, livro.getAno_publicado());
            stm.setString(3, livro.getAutor_livro());
            stm.setString(4, livro.getIsbn());
            stm.setString(5, livro.getEmprestado());

            stm.execute();

        } catch (SQLException e) {
            System.out.println("Erro ao inserir livro: " + e.getMessage());
        }

        return livro;
    }

    public static Livros alterarLivros(Livros livro) {
        String sql = "UPDATE livros SET titulo_livro = ?, ano_publicado = ?, autor_livro = ?, isbn = ?, emprestado = ? WHERE cd_livro = ?";

        try (Connection con = ConexaoBD.getConexao();
             PreparedStatement stm = con.prepareStatement(sql)) {

            stm.setString(1, livro.getTitulo_livro());
            stm.setString(2, livro.getAno_publicado());
            stm.setString(3, livro.getAutor_livro());
            stm.setString(4, livro.getIsbn());
            stm.setString(5, livro.getEmprestado());
            stm.setInt(6, livro.getCd_livro());

            stm.execute();

        } catch (SQLException e) {
            System.out.println("Erro ao alterar livro: " + e.getMessage());
        }

        return livro;
    }

    public static void excluirLivro(int id) {
        String sql = "DELETE FROM livros WHERE cd_livro = ?";

        try (Connection con = ConexaoBD.getConexao();
             PreparedStatement stm = con.prepareStatement(sql)) {

            stm.setInt(1, id);
            stm.execute();

        } catch (SQLException e) {
            System.out.println("Erro ao excluir livro: " + e.getMessage());
        }
    }

    public static Livros getByIdLivros(int id) {
        Livros livro = null;
        String sql = "SELECT * FROM livros WHERE cd_livro = ?";

        try (Connection con = ConexaoBD.getConexao();
             PreparedStatement stm = con.prepareStatement(sql)) {

            stm.setInt(1, id);
            ResultSet rs = stm.executeQuery();

            if (rs.next()) {
                livro = new Livros();
                livro.setCd_livro(rs.getInt("cd_livro"));
                livro.setTitulo_livro(rs.getString("titulo_livro"));
                livro.setAutor_livro(rs.getString("autor_livro"));
                livro.setAno_publicado(rs.getString("ano_publicado"));
                livro.setIsbn(rs.getString("isbn"));
                livro.setEmprestado(rs.getString("emprestado"));
            }

        } catch (SQLException e) {
            System.out.println("Erro ao buscar livro por ID: " + e.getMessage());
        }

        return livro;
    }

    public static void atualizarStatusEmprestimo(int cdLivro, String status) {
        String sql = "UPDATE livros SET emprestado = ? WHERE cd_livro = ?";

        try (Connection con = ConexaoBD.getConexao();
             PreparedStatement stm = con.prepareStatement(sql)) {

            stm.setString(1, status); // "S" ou "N"
            stm.setInt(2, cdLivro);
            stm.execute();

        } catch (SQLException e) {
            System.out.println("Erro ao atualizar status de empréstimo: " + e.getMessage());
        }
    }

    public static boolean livroExiste(int cdLivro) {
    String sql = "SELECT 1 FROM livros WHERE cd_livro = ?";

    try (Connection con = ConexaoBD.getConexao();
         PreparedStatement stm = con.prepareStatement(sql)) {

        stm.setInt(1, cdLivro);
        ResultSet rs = stm.executeQuery();
        return rs.next();

    } catch (SQLException e) {
        System.err.println("Erro ao verificar existência do livro: " + e.getMessage());
        return false;
    }
}



}
