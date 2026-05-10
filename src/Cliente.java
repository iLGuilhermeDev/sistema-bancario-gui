import java.io.Serializable;

public class Cliente implements Serializable {
    private static final long serialVersionUID = 1L;

    private String nome;
    private String cpf;
    private String senha;

    public Cliente(String nome, String cpf, String senha) {
        this.nome = nome;
        this.cpf = cpf;
        this.senha = senha;
    }

    public String getNome() {
        return nome;
    }

    public String getCpf() {
        return cpf;
    }

    public boolean validarSenha(String tentativa) {
        return this.senha.equals(tentativa);
    }

    @Override
    public String toString() {
        return nome + " (CPF: " + cpf + ")";
    }
}