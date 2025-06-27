package Model;

import java.util.List;
import java.util.Scanner;

import Dao.UsuarioDao;

public class Usuarios {

    private int cd_usuario;
    private String nm_usuario;
    private String nr_telefone;
    private String ds_email;
    private String senha;

    public Usuarios() {
    }

    public Usuarios(int cd_usuario, String nm_usuario, String nr_telefone, String ds_email, String senha) {
        this.cd_usuario = cd_usuario;
        this.nm_usuario = nm_usuario;
        this.nr_telefone = nr_telefone;
        this.ds_email = ds_email;
        this.senha = senha;
    }

    public int getCd_usuario() {
        return cd_usuario;
    }

    public void setCd_usuario(int cd_usuario) {
        this.cd_usuario = cd_usuario;
    }

    public String getNm_usuario() {
        return nm_usuario;
    }

    public void setNm_usuario(String nm_usuario) {
        this.nm_usuario = nm_usuario;
    }

    public String getNr_telefone() {
        return nr_telefone;
    }

    public void setNr_telefone(String nr_telefone) {
        this.nr_telefone = nr_telefone;
    }

    public String getDs_email() {
        return ds_email;
    }

    public void setDs_email(String ds_email) {
        this.ds_email = ds_email;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    @Override
    public String toString() {
        return "Código Usuário: " + cd_usuario +
                " | Nome Usuário: " + nm_usuario +
                " | Telefone: " + nr_telefone +
                " | Email: " + ds_email;
    }

    public static void inserirUsuarios(Scanner sc) {
        Usuarios u = new Usuarios();

        System.out.print("Insira o nome do usuário: ");
        u.setNm_usuario(sc.nextLine());

        System.out.print("Insira o telefone: ");
        u.setNr_telefone(sc.nextLine());

        System.out.print("Insira o email: ");
        u.setDs_email(sc.nextLine());

        System.out.print("Insira a senha: ");
        u.setSenha(sc.nextLine());

        Dao.UsuarioDao.inserirUsuarios(u);
        System.out.println("*** Usuário cadastrado com sucesso ***");
    }

    public static void listarUsuarios(Scanner sc) {
        List<Usuarios> usu = Dao.UsuarioDao.getAll();

        if (usu.isEmpty()) {
            System.out.println("Nenhum usuário cadastrado.");
        } else {
            for (Usuarios u : usu) {
                System.out.println(u.toString());
            }
        }
    }

    public static void alterarUsuario(Scanner sc) {
        Usuarios u = buscarPorUsuario(sc);
        if (u.getCd_usuario() == 0) {
            System.out.println("Usuário não encontrado.");
            return;
        }

        System.out.println("Atual:");
        System.out.println(u.toString());

        if (u.getNm_usuario() == null || u.getNm_usuario().isBlank()) {
            System.out.println("Usuário não tem nome cadastrado.");
        }

        System.out.println("-------------------");

        System.out.print("Digite novo nome: ");
        String novoNome = sc.nextLine();
        if (!novoNome.isBlank()) {
            u.setNm_usuario(novoNome);
        }

        System.out.print("Digite novo telefone: ");
        String novoTelefone = sc.nextLine();
        if (!novoTelefone.isBlank()) {
            u.setNr_telefone(novoTelefone);
        }

        System.out.print("Digite novo email: ");
        String novoEmail = sc.nextLine();
        if (!novoEmail.isBlank()) {
            u.setDs_email(novoEmail);
        }

        System.out.print("Digite nova senha: ");
        String novaSenha = sc.nextLine();
        if (!novaSenha.isBlank()) {
            u.setSenha(novaSenha);
        }

        Dao.UsuarioDao.alterarUsuarios(u);
        System.out.println("*** Usuário alterado com sucesso ***");
    }

    public static void alterarSenhaUsuario(Scanner sc) {
        Usuarios u = buscarPorUsuario(sc);
        if (u == null)
            return;

        System.out.print("Digite nova senha: ");
        u.setSenha(sc.nextLine());

        Dao.UsuarioDao.alterarUsuarios(u);
        System.out.println("*** Senha alterada com sucesso ***");
    }

    public static void excluirUsuario(Scanner sc) {
        System.out.print("Insira o código do usuário: ");
        int id = sc.nextInt();
        sc.nextLine(); // consumir quebra de linha pendente

        Usuarios u = UsuarioDao.getByIdUsuarios(id);

        if (u == null || u.getNm_usuario() == null) {
            System.out.println("Usuário não encontrado.");
            return;
        }

        Dao.UsuarioDao.excluirUsuarios(id);
        System.out.println("*** Usuário excluído com sucesso ***");
    }

    public static void buscarUsuario(Scanner sc) {
        Usuarios u = buscarPorUsuario(sc);
        if (u != null) {
            System.out.println(u.toString());
        }
    }

    public static Usuarios buscarPorUsuario(Scanner sc) {
        System.out.print("Digite o código do usuário: ");
        int id = sc.nextInt();
        sc.nextLine(); // consumir quebra de linha pendente

        Usuarios u = Dao.UsuarioDao.getByIdUsuarios(id);

        if (u == null || u.getCd_usuario() == 0) {
            System.out.println("Nenhum usuário encontrado.");
            return null;
        }

        return u;
    }

    private static int lerIntComValidacao(Scanner sc, String mensagem) {
        int valor;
        while (true) {
            System.out.print(mensagem);
            if (sc.hasNextInt()) {
                valor = sc.nextInt();
                sc.nextLine(); // consumir a quebra de linha pendente
                break;
            } else {
                System.out.println("Entrada inválida! Digite uma opção válida.");
                sc.nextLine(); // descarta a entrada inválida
            }
        }
        return valor;
    }

    public static void menuUsuario(Scanner sc) {
        int opcao;
        do {
            System.out.println("\n*** MENU USUÁRIO ***");
            System.out.println("1: Cadastrar Usuário");
            System.out.println("2: Listar Usuários");
            System.out.println("3: Editar Cadastro");
            System.out.println("4: Editar Senha");
            System.out.println("5: Excluir Cadastro Usuário");
            System.out.println("6: Buscar Por Código");
            System.out.println("0: Voltar para Menu Principal");
            opcao = lerIntComValidacao(sc, "ESCOLHA UMA OPÇÃO:  \n");
            System.out.println();

            switch (opcao) {
                case 1 -> inserirUsuarios(sc);
                case 2 -> listarUsuarios(sc);
                case 3 -> alterarUsuario(sc);
                case 4 -> alterarSenhaUsuario(sc);
                case 5 -> excluirUsuario(sc);
                case 6 -> buscarUsuario(sc);
                case 0 -> System.out.println("Voltando...");
                default -> System.out.println("Opção inválida.");
            }
        } while (opcao != 0);
    }
}
