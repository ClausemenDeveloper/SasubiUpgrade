package sasubiupgrade.controller;

import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import java.io.IOException;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;

public class MainController {

    private Stage primaryStage;
    private String nomeEstudante;
    private double saldoDevido;

    @FXML
    private Label estudanteLabel;

    @FXML
    private Label saldoLabel;

    public void setStage(Stage stage) {
        this.primaryStage = stage;
    }

    public void setEstudanteLogado(String nome, double saldo) {
        this.nomeEstudante = nome;
        this.saldoDevido = saldo;
        updateLabels();
    }

    @FXML
    public void initialize() {
        updateLabels();
    }

    private void updateLabels() {
        if (estudanteLabel != null) {
            estudanteLabel.setText("Estudante: " + (nomeEstudante != null ? nomeEstudante : "N/A"));
        }
        if (saldoLabel != null) {
            saldoLabel.setText(String.format("Saldo Devido: %.2f EUR", saldoDevido));
        }
    }

    @FXML
    public void navigateToResidencia() {
        try {
            carregarPagina("/sasubiupgrade/view/ResidenciaView.fxml", ResidenciaController.class);
        } catch (IOException e) {
            e.printStackTrace();
            showErrorAlert("Erro ao carregar a página: " + e.getMessage());
        }
    }

    @FXML
    public void navigateToCantina() {
        try {
            carregarPagina("/sasubiupgrade/view/CantinaView.fxml", null);
        } catch (IOException e) {
            e.printStackTrace();
            showErrorAlert("Erro ao carregar a página: " + e.getMessage());
        }
    }

    @FXML
    public void navigateToEncomendas() {
        try {
            carregarPagina("/sasubiupgrade/view/EncomendasView.fxml", null);
        } catch (IOException e) {
            e.printStackTrace();
            showErrorAlert("Erro ao carregar a página: " + e.getMessage());
        }
    }

    private <T> void carregarPagina(String fxmlPath, Class<T> controllerType) throws IOException {
        if (primaryStage == null) {
            throw new IllegalStateException("Stage não foi configurado.");
        }

        FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
        if (loader.getLocation() == null) {
            throw new IOException("Recurso FXML não encontrado: " + fxmlPath);
        }
        Parent pagina = loader.load();

        if (controllerType == ResidenciaController.class) {
            ResidenciaController controller = loader.getController();
            controller.setEstudanteLogado(nomeEstudante, saldoDevido);
        }

        Scene scene = new Scene(pagina);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void showErrorAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR, message);
        alert.showAndWait();
    }
}