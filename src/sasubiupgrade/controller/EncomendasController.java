package sasubiupgrade.controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import sasubiupgrade.model.Encomenda;

public class EncomendasController {

    @FXML
    private TextField inputNome;

    @FXML
    private ListView<String> listaEncomendas;

    private ObservableList<String> encomendas;
    private int encomendaCounter;
    private static final String CAMINHO_ARQUIVO_LOCAL = "encomendas.csv";
    private static final String CAMINHO_ARQUIVO_REPORTADO = "encomendas_reportadas.csv";

    @FXML
    public void initialize() {
        encomendaCounter = 1;
        encomendas = FXCollections.observableArrayList();
        loadEncomendas();
        listaEncomendas.setItems(encomendas);
    }

    @FXML
    public void registrarEncomenda() {
        String nome = inputNome.getText().trim();

        // Validate input
        if (nome.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Por favor, insira o nome do estudante!");
            alert.showAndWait();
            return;
        }

        // Create new parcel entry
        String id = String.format("ENC%03d", encomendaCounter++);
        String dataRegistro = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
        Encomenda encomenda = new Encomenda(nome, id, dataRegistro, "Local");
        encomendas.add(encomenda.toString());
        saveToFileLocal(encomenda.toString());

        // Show success message
        Alert alert = new Alert(Alert.AlertType.INFORMATION, "Encomenda registrada com sucesso! ID: " + id);
        alert.showAndWait();

        // Clear input field
        inputNome.clear();
    }

    @FXML
    public void verEncomendas() {
        if (encomendas.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Nenhuma encomenda registrada.");
            alert.showAndWait();
        } else {
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Encomendas exibidas na lista abaixo.");
            alert.showAndWait();
        }
        listaEncomendas.refresh();
    }

    private void loadEncomendas() {
        try {
            // Carregar encomendas locais
            if (Files.exists(Paths.get(CAMINHO_ARQUIVO_LOCAL))) {
                encomendas.addAll(Files.readAllLines(Paths.get(CAMINHO_ARQUIVO_LOCAL), StandardCharsets.UTF_8));
            }
            // Carregar encomendas reportadas
            if (Files.exists(Paths.get(CAMINHO_ARQUIVO_REPORTADO))) {
                encomendas.addAll(Files.readAllLines(Paths.get(CAMINHO_ARQUIVO_REPORTADO), StandardCharsets.UTF_8));
            }
        } catch (IOException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR, "Erro ao carregar as encomendas: " + e.getMessage());
            alert.showAndWait();
        }
    }

    private void saveToFileLocal(String encomenda) {
        try {
            Files.createDirectories(Paths.get(CAMINHO_ARQUIVO_LOCAL).getParent());
            Files.write(Paths.get(CAMINHO_ARQUIVO_LOCAL), (encomenda + "\n").getBytes(StandardCharsets.UTF_8),
                StandardOpenOption.CREATE, StandardOpenOption.APPEND);
        } catch (IOException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR, "Erro ao salvar as encomendas locais: " + e.getMessage());
            alert.showAndWait();
        }
    }

    // Método para simular ou carregar encomendas reportadas (pode ser expandido)
    private void loadReportedEncomendas() {
        try {
            if (Files.exists(Paths.get(CAMINHO_ARQUIVO_REPORTADO))) {
                encomendas.addAll(Files.readAllLines(Paths.get(CAMINHO_ARQUIVO_REPORTADO), StandardCharsets.UTF_8));
            }
        } catch (IOException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR, "Erro ao carregar encomendas reportadas: " + e.getMessage());
            alert.showAndWait();
        }
    }
}