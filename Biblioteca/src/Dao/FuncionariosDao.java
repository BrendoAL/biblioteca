package Dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import Model.Funcionarios;
import View.ConexaoBD;

public class FuncionariosDao {

    public static List<Funcionarios> getAll() {
        List<Funcionarios> funcionarios = new ArrayList<>();
        String sql = "SELECT * FROM funcionarios";

        try (Connection con = ConexaoBD.getConexao();
             PreparedStatement stm = con.prepareStatement(sql);
             ResultSet rs = stm.executeQuery()) {

            while (rs.next()) {
                Funcionarios f = new Funcionarios();
                f.setCd_funcionario(rs.getInt("cd_funcionario"));
                f.setNm_funcionario(rs.getString("nm_funcionario"));
                f.setSenha(rs.getString("senha"));
                funcionarios.add(f);
            }

        } catch (SQLException e) {
            System.out.println("Erro ao listar funcionários: " + e.getMessage());
        }

        return funcionarios;
    }

    public static Funcionarios inserirFuncionario(Funcionarios funcionario) {
        String sql = "INSERT INTO funcionarios (nm_funcionario, senha) VALUES (?, ?)";

        try (Connection con = ConexaoBD.getConexao();
             PreparedStatement stm = con.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {

            stm.setString(1, funcionario.getNm_funcionario());
            stm.setString(2, funcionario.getSenha());
            stm.executeUpdate();

            try (ResultSet rs = stm.getGeneratedKeys()) {
                if (rs.next()) {
                    funcionario.setCd_funcionario(rs.getInt(1)); // Define o ID gerado
                }
            }

        } catch (SQLException e) {
            System.out.println("Erro ao inserir funcionário: " + e.getMessage());
        }

        return funcionario;
    }

    public static Funcionarios alterarFuncionarios(Funcionarios funcionario) {
        String sql = "UPDATE funcionarios SET nm_funcionario = ?, senha = ? WHERE cd_funcionario = ?";

        try (Connection con = ConexaoBD.getConexao();
             PreparedStatement stm = con.prepareStatement(sql)) {

            stm.setString(1, funcionario.getNm_funcionario());
            stm.setString(2, funcionario.getSenha());
            stm.setInt(3, funcionario.getCd_funcionario());
            stm.execute();

        } catch (SQLException e) {
            System.out.println("Erro ao alterar funcionário: " + e.getMessage());
        }

        return funcionario;
    }

    public static void excluirFuncionario(int id) {
        String sql = "DELETE FROM funcionarios WHERE cd_funcionario = ?";

        try (Connection con = ConexaoBD.getConexao();
             PreparedStatement stm = con.prepareStatement(sql)) {

            stm.setInt(1, id);
            stm.execute();

        } catch (SQLException e) {
            System.out.println("Erro ao excluir funcionário: " + e.getMessage());
        }
    }

    public static Funcionarios getByIdFuncionario(int id) {
        Funcionarios f = null;
        String sql = "SELECT * FROM funcionarios WHERE cd_funcionario = ?";

        try (Connection con = ConexaoBD.getConexao();
             PreparedStatement stm = con.prepareStatement(sql)) {

            stm.setInt(1, id);
            ResultSet rs = stm.executeQuery();

            if (rs.next()) {
                f = new Funcionarios();
                f.setCd_funcionario(rs.getInt("cd_funcionario"));
                f.setNm_funcionario(rs.getString("nm_funcionario"));
                f.setSenha(rs.getString("senha"));
            }

        } catch (SQLException e) {
            System.out.println("Erro ao buscar funcionário por ID: " + e.getMessage());
        }

        return f;
    }

    public static Funcionarios autenticarFuncionario(String nome, String senha) {
        Funcionarios f = null;
        String sql = "SELECT * FROM funcionarios WHERE nm_funcionario = ? AND senha = ?";

        try (Connection con = ConexaoBD.getConexao();
             PreparedStatement stm = con.prepareStatement(sql)) {

            stm.setString(1, nome);
            stm.setString(2, senha);

            ResultSet rs = stm.executeQuery();
            if (rs.next()) {
                f = new Funcionarios();
                f.setCd_funcionario(rs.getInt("cd_funcionario"));
                f.setNm_funcionario(rs.getString("nm_funcionario"));
                f.setSenha(rs.getString("senha"));
            }

        } catch (SQLException e) {
            System.out.println("Erro na autenticação de funcionário: " + e.getMessage());
        }

        return f;
    }
}
