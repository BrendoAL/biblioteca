package Dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import Model.Usuarios;
import View.ConexaoBD;

public class UsuarioDao {

    @SuppressWarnings("finally")
    public static List<Usuarios> getAll() {

        List<Usuarios> usuario = new ArrayList<Usuarios>();
        String sql = "select * from usuarios ";

        try {
            PreparedStatement stm = ConexaoBD.getConexao().prepareStatement(sql);
            ResultSet rs = stm.executeQuery();
            while (rs.next()) {
                Usuarios u = new Usuarios();
                u.setCd_usuario(rs.getInt("cd_usuario"));
                u.setNm_usuario(rs.getString("nm_usuario"));
                u.setNr_telefone(rs.getString("nr_telefone"));
                u.setDs_email(rs.getString("ds_email"));
                usuario.add(u);
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            return usuario;
        }
    }

    public static Usuarios inserirUsuarios(Usuarios usuario) {

        String sql = "insert into usuarios(nm_usuario, nr_telefone, ds_email, senha)values(?, ?, ?, ?)";
        try {
            PreparedStatement stm = ConexaoBD.getConexao().prepareStatement(sql);
            stm.setString(1, usuario.getNm_usuario());
            stm.setString(2, usuario.getNr_telefone());
            stm.setString(3, usuario.getDs_email());
            stm.setString(4, usuario.getSenha());
            stm.execute();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return usuario;
    }

    public static Usuarios alterarUsuarios(Usuarios usuario) {

        String sql = "update usuarios set nm_usuario = ?, nr_telefone = ?, ds_email = ?, senha = ? where cd_usuario = ?";

        try (PreparedStatement stm = ConexaoBD.getConexao().prepareStatement(sql)) {

            stm.setString(1, usuario.getNm_usuario());
            stm.setString(2, usuario.getNr_telefone());
            stm.setString(3, usuario.getDs_email());
            stm.setString(4, usuario.getSenha());
            stm.setInt(5, usuario.getCd_usuario());
            stm.execute();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return usuario;
    }

    public static void excluirUsuarios(int id) {
        String sql = "delete from usuarios where cd_usuario = ?";

        try {
            PreparedStatement stm = ConexaoBD.getConexao().prepareStatement(sql);
            stm.setInt(1, id);
            stm.execute();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @SuppressWarnings("finally")
    public static Usuarios getByIdUsuarios(int id) {

        Usuarios u = new Usuarios();
        String sql = "select * from usuarios where cd_usuario = ?";

        try {
            PreparedStatement stm = ConexaoBD.getConexao().prepareStatement(sql);
            stm.setInt(1, id);
            ResultSet rs = stm.executeQuery();
            if (rs.next()) {
                u.setCd_usuario(rs.getInt("cd_usuario"));
                u.setNm_usuario(rs.getString("nm_usuario"));
                u.setNr_telefone(rs.getString("nr_telefone"));
                u.setDs_email(rs.getString("ds_email"));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            return u;
        }
    }

    public static boolean validarSenhaUsuario(int cdUsuario, String senha) {
        String sql = "SELECT 1 FROM usuarios WHERE cd_usuario = ? AND senha = ?";
        try (Connection con = ConexaoBD.getConexao();
                PreparedStatement stm = con.prepareStatement(sql)) {
            stm.setInt(1, cdUsuario);
            stm.setString(2, senha);
            ResultSet rs = stm.executeQuery();
            return rs.next(); // true se senha correta
        } catch (SQLException e) {
            System.err.println("Erro ao validar senha do usuário: " + e.getMessage());
            return false;
        }
    }

}
