package sasubiupgrade.model;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class PagamentoResidencia {
    private final StringProperty mes = new SimpleStringProperty();
    private final DoubleProperty valor = new SimpleDoubleProperty();
    private final StringProperty referencia = new SimpleStringProperty();
    private final StringProperty status = new SimpleStringProperty();

    public PagamentoResidencia(String mes, double valor, String referencia, String status) {
        this.mes.set(mes);
        this.valor.set(valor);
        this.referencia.set(referencia);
        this.status.set(status);
    }

    // Getters de propriedade
    public StringProperty mesProperty() {
        return mes;
    }

    public DoubleProperty valorProperty() {
        return valor;
    }

    public StringProperty referenciaProperty() {
        return referencia;
    }

    public StringProperty statusProperty() {
        return status;
    }

    public void setStatus(String status) {
        this.status.set(status);
    }

    @Override
    public String toString() {
        return "Pagamento{" +
                "mes='" + mes.get() + '\'' +
                ", valor=" + valor.get() +
                ", referencia='" + referencia.get() + '\'' +
                ", status='" + status.get() + '\'' +
                '}';
    }

    // Carregar pagamentos do arquivo CSV
    public static List<PagamentoResidencia> carregarPagamentos(String caminho) throws IOException {
        List<PagamentoResidencia> lista = new ArrayList<>();
        if (!Files.exists(Paths.get(caminho))) {
            return lista; // Retorna lista vazia se arquivo não existe
        }
        List<String> linhas = Files.readAllLines(Paths.get(caminho));
        for (String linha : linhas) {
            if (!linha.trim().isEmpty()) {
                String[] partes = linha.split(";");
                if (partes.length == 4) {
                    try {
                        double valor = Double.parseDouble(partes[1]);
                        lista.add(new PagamentoResidencia(partes[0], valor, partes[2], partes[3]));
                    } catch (NumberFormatException e) {
                        System.out.println("Erro ao converter valor numérico: " + partes[1]);
                    }
                }
            }
        }
        return lista;
    }

    // Salvar pagamentos no arquivo CSV
    public static void salvarPagamentos(List<PagamentoResidencia> lista, String caminho) throws IOException {
        List<String> linhas = new ArrayList<>();
        for (PagamentoResidencia p : lista) {
            linhas.add(p.mes.get() + ";" +
                       p.valor.get() + ";" +
                       p.referencia.get() + ";" +
                       p.status.get());
        }
        Files.write(Paths.get(caminho), linhas);
    }
}
