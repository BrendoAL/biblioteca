package Model;

import java.util.List;
import java.util.Scanner;
import Dao.FuncionariosDao;
import View.App;

public class Funcionarios {

    private int cd_funcionario;
    private String nm_funcionario;
    private String senha;

    public Funcionarios() {
    }

    public Funcionarios(int cd_funcionario, String nm_funcionario, String senha) {
        this.cd_funcionario = cd_funcionario;
        this.nm_funcionario = nm_funcionario;
        this.senha = senha;
    }

    public int getCd_funcionario() {
        return cd_funcionario;
    }

    public void setCd_funcionario(int cd_funcionario) {
        this.cd_funcionario = cd_funcionario;
    }

    public String getNm_funcionario() {
        return nm_funcionario;
    }

    public void setNm_funcionario(String nm_funcionario) {
        this.nm_funcionario = nm_funcionario;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    @Override
    public String toString() {
        return "Código Funcionário: " + cd_funcionario +
                " | Nome: " + nm_funcionario;
    }

   public static void inserirFuncionario(Scanner sc) {
    System.out.print("Nome do funcionário: ");
    String nome = sc.nextLine();

    System.out.print("Senha do funcionário: ");
    String senha = sc.nextLine();

    Funcionarios funcionario = new Funcionarios();
    funcionario.setNm_funcionario(nome);
    funcionario.setSenha(senha);

    funcionario = FuncionariosDao.inserirFuncionario(funcionario); // recebe com o código

    if (funcionario.getCd_funcionario() > 0) {
        System.out.println("Funcionário " + funcionario.getNm_funcionario()
                + " cadastrado com sucesso. Código: " + funcionario.getCd_funcionario());
                System.out.println();
    } else {
        System.out.println("Falha ao cadastrar funcionário.");
        System.out.println();
    }
}


    public static void listarFuncionarios() {
        List<Funcionarios> lista = FuncionariosDao.getAll();

        if (lista.isEmpty()) {
            System.out.println("Nenhum funcionário encontrado.");
        } else {
            for (Funcionarios f : lista) {
                System.out.println(f.toString());
            }
        }
    }

    public static void alterarFuncionarios(Scanner sc) {
        System.out.print("Digite o código do funcionário: ");
        Funcionarios f = buscarPorFuncionario(sc);
        if (f == null)
            return;

        System.out.println("Atual:");
        System.out.println(f.toString());
        System.out.println("-------------------");

        System.out.print("Digite o novo nome: ");
        f.setNm_funcionario(sc.nextLine());

        System.out.print("Digite a nova senha: ");
        f.setSenha(sc.nextLine());

        FuncionariosDao.alterarFuncionarios(f);
        System.out.println("*** Funcionário alterado com sucesso ***");
    }

    public static void alterarSenhaFuncionario(Scanner sc) {
        Funcionarios f = buscarPorFuncionario(sc);
        if (f == null)
            return;

        System.out.print("Digite a nova senha: ");
        f.setSenha(sc.nextLine());

        FuncionariosDao.alterarFuncionarios(f);
        System.out.println("*** Senha alterada com sucesso ***");
    }

    public static void excluirFuncionario(Scanner sc) {
        System.out.print("Insira o código do funcionário: ");
        int id = lerIntComValidacao(sc, "Digite o código do funcionário: ");

        Funcionarios f = FuncionariosDao.getByIdFuncionario(id);
        if (f == null || f.getNm_funcionario() == null) {
            System.out.println("Funcionário não encontrado.");
            return;
        }

        FuncionariosDao.excluirFuncionario(id);
        System.out.println("*** Funcionário excluído com sucesso ***");
    }

    public static void buscarFuncionario(Scanner sc) {
        Funcionarios f = buscarPorFuncionario(sc);
        if (f != null) {
            System.out.println(f.toString());
        }
    }

    private static Funcionarios buscarPorFuncionario(Scanner sc) {
        int id = lerIntComValidacao(sc, "Digite o código do funcionário: ");

        Funcionarios f = FuncionariosDao.getByIdFuncionario(id);
        if (f == null || f.getNm_funcionario() == null) {
            System.out.println("Nenhum funcionário encontrado.");
            return null;
        }
        return f;
    }

    private static Funcionarios realizarLogin(Scanner sc) {
        System.out.println("=== LOGIN FUNCIONÁRIO ===");
        int cdFuncionario = lerIntComValidacao(sc, "Digite o código do funcionário: ");

        System.out.print("Digite a senha: ");
        String senha = sc.nextLine();

        Funcionarios f = FuncionariosDao.getByIdFuncionario(cdFuncionario);

        if (f != null && senha.equals(f.getSenha())) {
            System.out.println("Login realizado com sucesso!\n");
            return f;
        } else {
            System.out.println("Código ou senha inválidos.\n");
            return null;
        }
    }

    public static void funcionarioLogado(Scanner sc) {
        Funcionarios funcionarioLogado = realizarLogin(sc);
        if (funcionarioLogado != null) {
            System.out.println("Acessando sistema como: " + funcionarioLogado.getNm_funcionario());
            App.menuPrincipal(sc, funcionarioLogado);

        } else {
            System.out.println("Login ou senha inválidos.\n");
        }
    }

    // Método auxiliar para ler inteiro com validação e evitar
    // InputMismatchException
    public static int lerIntComValidacao(Scanner sc, String mensagem) {
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

    public static void menuFuncionario(Scanner sc) {
        int opcao;
        do {
            System.out.println("\n*** MENU FUNCIONÁRIO ***");
            System.out.println("1: Cadastrar Funcionário");
            System.out.println("2: Listar Funcionários");
            System.out.println("3: Editar Cadastro");
            System.out.println("4: Editar Senha");
            System.out.println("5: Excluir Cadastro Funcionário");
            System.out.println("6: Buscar Por Código ");
            System.out.println("0: Voltar ao Menu Principal");
            opcao = lerIntComValidacao(sc, "ESCOLHA UMA OPÇÃO:  \n");
            System.out.println();

            switch (opcao) {
                case 1 -> inserirFuncionario(sc);
                case 2 -> listarFuncionarios();
                case 3 -> alterarFuncionarios(sc);
                case 4 -> alterarSenhaFuncionario(sc);
                case 5 -> excluirFuncionario(sc);
                case 6 -> buscarFuncionario(sc);
                case 0 -> System.out.println("Voltando...");
                default -> System.out.println("Opção inválida!");
            }

        } while (opcao != 0);
    }

}
