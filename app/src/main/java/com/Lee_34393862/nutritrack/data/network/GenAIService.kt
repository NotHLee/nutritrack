package com.Lee_34393862.nutritrack.data.network

import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.GenerateContentResponse
import com.google.ai.client.generativeai.type.content
import kotlinx.coroutines.flow.Flow

class GenAIService {

    // this is not the best practice
    // however, apiKey will be hardcoded to make it easier for the marker
    private val model: GenerativeModel = GenerativeModel(
        apiKey = "AIzaSyDlZM5LUWdqXB6DGE01h2YtdKX0W6vd5OU",
        modelName = "gemini-2.0-flash-001"
    )

    // function to generate content from a prompt
    fun generateContentStream(prompt: String): Flow<GenerateContentResponse> {
        return model.generateContentStream(
            content {
                text(prompt)
            }
        )
    }

}