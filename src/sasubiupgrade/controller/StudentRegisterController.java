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
import java.nio.file.StandardOpenOption;
import java.util.List;

public class StudentRegisterController {

    @FXML
    private TextField nomeField;

    @FXML
    private PasswordField palavraPasseField;

    @FXML
    private TextField saldoField;

    private static final String CAMINHO_ESTUDANTES = "estudantes.csv";

    @FXML
    public void registrarEstudante() {
        String nome = nomeField.getText().trim();
        String palavraPasse = palavraPasseField.getText().trim();
        String saldoStr = saldoField.getText().trim();

        // Validação
        if (nome.isEmpty() || palavraPasse.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Nome e palavra-passe são obrigatórios!");
            alert.showAndWait();
            return;
        }

        double saldo;
        try {
            saldo = saldoStr.isEmpty() ? 0.0 : Double.parseDouble(saldoStr);
            if (saldo < 0) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "O saldo inicial não pode ser negativo!");
                alert.showAndWait();
                return;
            }
        } catch (NumberFormatException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Saldo inválido! Use um número (ex: 0.00).");
            alert.showAndWait();
            return;
        }

        try {
            // Verificar se o nome já existe
            if (nomeExiste(nome)) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Este nome já está registrado!");
                alert.showAndWait();
                return;
            }

            // Obter o próximo ID
            int proximoId = obterProximoId();

            // Gravar o novo estudante no arquivo
            String novaLinha = String.format("%d,%s,%s,%.2f", proximoId, nome, palavraPasse, saldo);
            Files.write(Paths.get(CAMINHO_ESTUDANTES), (novaLinha + "\n").getBytes(), StandardOpenOption.APPEND);

            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Estudante registrado com sucesso!");
            alert.showAndWait();

            // Limpar os campos
            nomeField.clear();
            palavraPasseField.clear();
            saldoField.clear();

        } catch (IOException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR, "Erro ao registrar estudante: " + e.getMessage());
            alert.showAndWait();
        }
    }

    @FXML
    public void voltarParaLogin() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/sasubiupgrade/view/StudentLoginView.fxml"));
            Parent pagina = loader.load();
            Stage stage = (Stage) nomeField.getScene().getWindow();
            Scene scene = new Scene(pagina);
            stage.setTitle("Sistema de Pagamentos - Login");
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR, "Erro ao voltar para o login: " + e.getMessage());
            alert.showAndWait();
        }
    }

    private boolean nomeExiste(String nome) throws IOException {
        if (!Files.exists(Paths.get(CAMINHO_ESTUDANTES))) {
            return false;
        }
        List<String> linhas = Files.readAllLines(Paths.get(CAMINHO_ESTUDANTES));
        for (int i = 1; i < linhas.size(); i++) {
            String[] partes = linhas.get(i).split(",");
            if (partes.length >= 2 && partes[1].trim().equalsIgnoreCase(nome)) {
                return true;
            }
        }
        return false;
    }

    private int obterProximoId() throws IOException {
        if (!Files.exists(Paths.get(CAMINHO_ESTUDANTES))) {
            return 1;
        }
        List<String> linhas = Files.readAllLines(Paths.get(CAMINHO_ESTUDANTES));
        int maxId = 0;
        for (int i = 1; i < linhas.size(); i++) {
            String[] partes = linhas.get(i).split(",");
            if (partes.length >= 1) {
                try {
                    int id = Integer.parseInt(partes[0].trim());
                    maxId = Math.max(maxId, id);
                } catch (NumberFormatException e) {
                    System.out.println("Erro ao converter ID na linha " + i + ": " + partes[0]);
                }
            }
        }
        return maxId + 1;
    }
}