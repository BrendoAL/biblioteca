package View;

import java.util.Scanner;

import Model.Funcionarios;
import Model.Livros;
import Model.Usuarios;
import Model.Emprestimos;

public class App {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        if (ConexaoBD.getConexao() != null) {

            int opcao = 0;
            do {
                System.out.println("***MENU INICIAL");
                System.out.println("1: Cadastrar Funcionário");
                System.out.println("2: Login Funcionário");
                System.out.println("3: Listar Funcionário");
                System.out.println("0: Sair");
                System.out.print("Opção: ");

                String entrada = sc.nextLine(); // <- substitui o nextInt()
                try {
                    opcao = Integer.parseInt(entrada); // tenta converter
                } catch (NumberFormatException e) {
                    opcao = -1; // valor inválido
                }

                switch (opcao) {
                    case 1 -> Funcionarios.inserirFuncionario(sc);
                    case 2 -> Funcionarios.funcionarioLogado(sc);
                    case 3 -> Funcionarios.listarFuncionarios();
                    case 0 -> System.out.println("Saindo...");
                    default -> System.out.println("Opção inválida!\n");
                }

            } while (opcao != 0);

        } else {
            System.out.println("Erro de conexão com o banco de dados.");
        }
    }

    public static void menuPrincipal(Scanner sc, Funcionarios funcionario) {
        int opcao = 0;
        do {
            System.out.println("\n=== MENU PRINCIPAL ===");
            System.out.println("1: Funcionários");
            System.out.println("2: Usuários");
            System.out.println("3: Livros");
            System.out.println("4: Empréstimos | Devoluções");
            System.out.println("0: Logout");
            System.out.print("Opção: ");

            String entrada = sc.nextLine(); // sempre ler como String
            try {
                opcao = Integer.parseInt(entrada); // tenta converter para inteiro
            } catch (NumberFormatException e) {
                opcao = -1; // invalida a opção para entrar no default
            }

            switch (opcao) {
                case 1 -> Funcionarios.menuFuncionario(sc);
                case 2 -> Usuarios.menuUsuario(sc);
                case 3 -> Livros.menuLivro(sc);
                case 4 -> Emprestimos.menuEmprestimo(sc, funcionario.getCd_funcionario());
                case 0 -> System.out.println("Logout realizado!\n");
                default -> System.out.println("Opção inválida!\n");
            }
        } while (opcao != 0);
    }

}

