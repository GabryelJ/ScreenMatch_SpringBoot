package dev.gabryel.screenmatch.service;

import com.google.cloud.vertexai.VertexAI;
import com.google.cloud.vertexai.api.GenerateContentResponse;
import com.google.cloud.vertexai.generativeai.GenerativeModel;
import com.google.cloud.vertexai.generativeai.ResponseHandler;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class GeminiService {
    private final String projectId;
    private final String location;
    private final String modelName;

    public GeminiService(
        @Value("${gemini.gcp.project-id}") String projectId,
        @Value("${gemini.gcp.location}") String location,
        @Value("${gemini.model.name}") String modelName) {
        this.projectId = projectId;
        this.location = location;
        this.modelName = modelName;
    }

    public String getTranslation(String text) {
        try (VertexAI vertexAI = new VertexAI(projectId, location)) {

            String promptText = "Traduza o seguinte texto para o português, sem adicionar nenhuma explicação ou texto extra, apenas a tradução pura: " + text;

            GenerativeModel model = new GenerativeModel(modelName, vertexAI);
            GenerateContentResponse response = model.generateContent(promptText);

            return ResponseHandler.getText(response);

        } catch (IOException e) {

            System.err.println("Erro ao comunicar com a API Vertex AI: " + e.getMessage());
            throw new RuntimeException("Erro ao tentar realizar a tradução.", e);
        }
    }
}