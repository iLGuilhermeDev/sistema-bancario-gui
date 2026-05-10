import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Transacao {
    private static int proximoId = 1;
    private int id;
    private String tipo;   // "DEPOSITO", "SAQUE", "TRANSFERENCIA_ENVIO", "TRANSFERENCIA_RECEBIMENTO"
    private BigDecimal valor;
    private String descricao;
    private Date dataHora;

    public Transacao(String tipo, BigDecimal valor, String descricao) {
        this.id = proximoId++;
        this.tipo = tipo;
        this.valor = valor;
        this.descricao = descricao;
        this.dataHora = new Date();
    }

    public String getTipo() {
        return tipo;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public String getDescricao() {
        return descricao;
    }

    public String getDataHoraFormatada() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        return sdf.format(dataHora);
    }

    @Override
    public String toString() {
        return String.format("[%s] %s - R$ %.2f - %s",
                getDataHoraFormatada(), tipo, valor, descricao);
    }
}