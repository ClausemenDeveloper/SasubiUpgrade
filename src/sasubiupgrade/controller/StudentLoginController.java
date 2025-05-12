package sasubiupgrade.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.control.TextField;
import javafx.scene.control.PasswordField;
import javafx.scene.control.Alert;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class StudentLoginController {

    @FXML
    private TextField nomeField;

    @FXML
    private PasswordField palavraPasseField;

    private static final String CAMINHO_ESTUDANTES = "estudantes.csv";

    @FXML
    public void fazerLogin() {
        String nome = nomeField.getText().trim();
        String palavraPasse = palavraPasseField.getText().trim();

        try {
            // Verificar se o arquivo existe, caso contrário, criá-lo com dados padrão
            if (!Files.exists(Paths.get(CAMINHO_ESTUDANTES))) {
                criarArquivoEstudantes();
            }

            List<String> linhas = Files.readAllLines(Paths.get(CAMINHO_ESTUDANTES));
            boolean loginValido = false;
            String nomeLogado = null;
            double saldoDevido = 0.0;

            // Pular a primeira linha (cabeçalho) e processar as demais
            for (int i = 1; i < linhas.size(); i++) {
                String linha = linhas.get(i).trim();
                if (linha.isEmpty()) {
                    System.out.println("Linha vazia ignorada na posição: " + i);
                    continue;
                }

                String[] partes = linha.split(",");
                if (partes.length == 4) {
                    String nomeArquivo = partes[1].trim();
                    String senhaArquivo = partes[2].trim();
                    String saldoStr = partes[3].trim();

                    // Depuração detalhada
                    System.out.println("Verificando linha " + i + ": nomeArquivo='" + nomeArquivo + "', senhaArquivo='" + senhaArquivo + "', saldoStr='" + saldoStr + "'");
                    System.out.println("Comparando com: nome='" + nome + "', palavraPasse='" + palavraPasse + "'");

                    try {
                        double saldo = Double.parseDouble(saldoStr);
                        if (nomeArquivo.equalsIgnoreCase(nome) && senhaArquivo.equals(palavraPasse)) {
                            loginValido = true;
                            nomeLogado = nomeArquivo;
                            saldoDevido = saldo;
                            System.out.println("Login válido para: " + nomeLogado + ", saldo: " + saldoDevido);
                            break;
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("Erro ao converter saldo na linha " + i + ": " + saldoStr);
                    }
                } else {
                    System.out.println("Linha malformada na posição " + i + ": " + linha);
                }
            }

            if (loginValido) {
                carregarMainView(nomeLogado, saldoDevido);
            } else {
                System.out.println("Login falhou para nome: " + nome + ", palavra-passe: " + palavraPasse);
                Alert alert = new Alert(Alert.AlertType.ERROR, "Nome ou palavra-passe inválidos!");
                alert.showAndWait();
            }
        } catch (IOException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR, "Erro ao ler ou criar o arquivo de estudantes: " + e.getMessage());
            alert.showAndWait();
        }
    }

    @FXML
    public void irParaRegistro() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/sasubiupgrade/view/StudentRegisterView.fxml"));
            Parent pagina = loader.load();
            Stage stage = (Stage) nomeField.getScene().getWindow();
            Scene scene = new Scene(pagina);
            stage.setTitle("Sistema de Pagamentos - Registro");
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR, "Erro ao carregar a tela de registro: " + e.getMessage());
            alert.showAndWait();
        }
    }

    private void carregarMainView(String nome, double saldo) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/sasubiupgrade/view/MainView.fxml"));
        Parent pagina = loader.load();

        MainController controller = loader.getController();
        Stage stage = (Stage) nomeField.getScene().getWindow();
        controller.setStage(stage); // Passa o Stage para o MainController
        controller.setEstudanteLogado(nome, saldo);

        Scene scene = new Scene(pagina);
        stage.setTitle("Sistema de Pagamentos - Menu Principal");
        stage.setScene(scene);
        stage.show();
    }

    private void criarArquivoEstudantes() throws IOException {
        String[] dadosEstudantes = {
            "id,nome,palavraPasse,saldoDevido",
            "1,Clausemen Custodio Nanro,senha123,450.00",
            "2,Ana Silva,ana2025,300.00",
            "3,Joao Maravilhoso,joao2025,600.00",
            "4,Maria Oliveira,maria2025,150.00",
            "5,Pedro Santos,pedro2025,0.00",
            "6,Lucas Almeida,lucas2025,750.00",
            "7,Beatriz Costa,beatriz2025,200.00",
            "8,Carlos Mendes,carlos2025,500.00",
            "9,Sofia Rodrigues,sofia2025,100.00",
            "10,Fernando Lima,fernando2025,300.00",
            "1000,Tatiana Araujo,tatiana2025,250.00"
        };
        Files.write(Paths.get(CAMINHO_ESTUDANTES), String.join("\n", dadosEstudantes).getBytes());
        System.out.println("Arquivo estudantes.csv criado com sucesso em: " + Paths.get(CAMINHO_ESTUDANTES).toAbsolutePath());
    }
}