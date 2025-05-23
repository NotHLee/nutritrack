package com.Lee_34393862.nutritrack.data.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.Lee_34393862.nutritrack.data.entities.Patient
import com.Lee_34393862.nutritrack.data.network.GenAIService
import com.Lee_34393862.nutritrack.data.repositories.PatientRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ClinicianViewModel(context: Context) : ViewModel() {

    private val patientRepository = PatientRepository(context = context)
    private val genAIService = GenAIService()

    private val _maleHeifaScoreAverage = MutableStateFlow<Double>(0.0)
    private val _femaleHeifaScoreAverage = MutableStateFlow<Double>(0.0)
    private val _patients = MutableStateFlow<List<Patient>>(emptyList())
    private val _analysisText = MutableStateFlow<List<String>>(emptyList())
    val maleHeifaScoreAverage: StateFlow<Double> get() = _maleHeifaScoreAverage.asStateFlow()
    val femaleHeifaScoreAverage: StateFlow<Double> get() = _femaleHeifaScoreAverage.asStateFlow()
    val analysisText: StateFlow<List<String>> get() = _analysisText.asStateFlow()

    init {
        viewModelScope.launch {
            patientRepository.getMaleHeifaScoreAverage().collect {
                _maleHeifaScoreAverage.value = it
            }
        }
        viewModelScope.launch {
            patientRepository.getFemaleHeifaScoreAverage().collect {
                _femaleHeifaScoreAverage.value = it
            }
        }
        viewModelScope.launch {
            patientRepository.getAllPatients().collect {
                _patients.value = it
            }
        }
    }

    fun analyzePatientData() {
        viewModelScope.launch {
            // format the prompt
            val prompt = StringBuilder()
            prompt.append("You are a nutritionist. You have the following patients:\n")
            _patients.value.forEach {
                prompt.append("$it\n")
            }
            prompt.append("""
                Analyze the patient nutrition data above and provide 3 interesting patterns you observe. The dataset contains HEIFA scores across various food categories (vegetables, fruits, grains, etc.), broken down by sex, serving sizes, food variations, and nutrient intake.
                Answer in the following format:
                <pattern>
                **Variable Water Intake**: Consumption of water varies greatly among the users in this dataset, with scores ranging from 0 to 100. There isn't a clear, immediate correlation in this small sample between water intake score and the overall HEIFA score, though some high scorers did have high water intake.
                </pattern>
                <pattern>
                **Low Wholegrain Consumption**: The intake of wholegrains appears generally low across all users. Only a few users in the provided sample data had a recorded intake and score for wholegrains, while the rest had zero.
                </pattern>
                <pattern>
                **Potential Gender Difference in HEIFA Scoring**: The data includes columns for both HEIFAtotalscoreMale and HEIFAtotalscoreFemale for each user. In several cases, the potential score calculated for females is slightly higher than that calculated for males, suggesting the HEIFA criteria might result in slightly different potential maximums or scoring based on gender recommendations, though the actual resulting totals were similar.
                </pattern>
            """.trimIndent())

            // call genAIService to generate the response
            val response = StringBuilder()
            genAIService.generateContentStream(prompt.toString()).collect { chunk ->
                chunk.text?.let { textChunk ->
                    response.append(textChunk)
                    val currentRawResponse = response.toString()

                    val extractedPatterns = mutableListOf<String>()
                    val patternStartTag = "<pattern>"
                    val patternEndTag = "</pattern>"

                    val parts = currentRawResponse.split(patternStartTag)

                    // first part (parts[0]) is any text before the first <pattern> tag.
                    // iterate from the second part onwards.
                    for (i in 1 until parts.size) {
                        val potentialPatternSegment = parts[i]
                        val endTagIndex = potentialPatternSegment.indexOf(patternEndTag)

                        if (endTagIndex != -1) {
                            // this segment contains a complete pattern (or the end of one)
                            val patternContent = potentialPatternSegment.substring(0, endTagIndex).trim()
                            if (patternContent.isNotEmpty()) {
                                extractedPatterns.add(patternContent)
                            }
                        } else {
                            // this segment is part of an ongoing pattern (no end tag yet)
                            val patternContent = potentialPatternSegment.trim()
                            if (patternContent.isNotEmpty()) {
                                extractedPatterns.add(patternContent)
                            }
                        }
                    }
                    _analysisText.value = extractedPatterns
                }
            }
        }
    }

    class ClinicianViewModelFactory(context: Context) : ViewModelProvider.Factory {
        private val context = context.applicationContext
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return ClinicianViewModel(context) as T
        }
    }


}