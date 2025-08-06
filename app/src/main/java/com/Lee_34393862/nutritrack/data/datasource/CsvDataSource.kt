package com.Lee_34393862.nutritrack.data.datasource

import android.content.Context
import android.util.Log
import com.Lee_34393862.nutritrack.data.AppDatabase
import com.Lee_34393862.nutritrack.data.entities.FoodIntake
import com.Lee_34393862.nutritrack.data.entities.Patient
import kotlinx.coroutines.flow.firstOrNull
import java.io.BufferedReader
import java.io.InputStreamReader

/**
 * Class to load csv data to AppDatabase upon first entry
 */
object CsvDataSource {

    /**
     * Check if database ia already seeded
     */
    suspend fun isDatabaseSeeded(database: AppDatabase): Boolean {
        return !database.patientDao().getAllPatients().firstOrNull().isNullOrEmpty()
    }

    /**
     * Parse patient data CSV to a list of patients
     */
    suspend fun parseCSV(context: Context) {

        val database: AppDatabase = AppDatabase.getDatabase(context)

        // early exit if db is already seeded
        if (isDatabaseSeeded(database = database)) return

        Log.d("populate", "populating database")

        // read the csv line by line and map it as a patient entity to be inserted in the db
        val assets = context.assets
        val inputStream = assets.open("patients.csv")
        val reader = BufferedReader(InputStreamReader(inputStream))
        val header = reader.readLine().split(",")

        reader.useLines { lines ->
            var patientData: Map<String, String> = mapOf()
            lines.forEach { line ->
                val values = line.split(",")
                patientData = header.zip(values).toMap()
                var patient = mapToPatient(patientData)
                database.patientDao().insert(patient = patient)
                database.foodIntakeDao().insert(foodIntake = FoodIntake(userId = patient.userId))
            }
        }
        reader.close()
    }

    /**
     * Helper method to map csv entries to patient entity
     */
    fun mapToPatient(entry: Map<String, String>): Patient {
        return Patient(
            name = "", // Assuming 'name' is not in the CSV based on the original code
            phoneNumber = entry["PhoneNumber"] ?: "",
            userId = entry["User_ID"]?: "",
            sex = entry["Sex"] ?: "",
            heifaTotalScoreMale = entry["HEIFAtotalscoreMale"]?.toDouble() ?: 0.0,
            heifaTotalScoreFemale = entry["HEIFAtotalscoreFemale"]?.toDouble() ?: 0.0,
            discretionaryHeifaScoreMale = entry["DiscretionaryHEIFAscoreMale"]?.toDouble()
                ?: 0.0,
            discretionaryHeifaScoreFemale = entry["DiscretionaryHEIFAscoreFemale"]?.toDouble()
                ?: 0.0,
            discretionaryServeSize = entry["Discretionaryservesize"]?.toDouble() ?: 0.0,
            vegetablesHeifaScoreMale = entry["VegetablesHEIFAscoreMale"]?.toDouble() ?: 0.0,
            vegetablesHeifaScoreFemale = entry["VegetablesHEIFAscoreFemale"]?.toDouble()
                ?: 0.0,
            vegetablesWithLegumesAllocatedServeSize = entry["Vegetableswithlegumesallocatedservesize"]?.toDouble()
                ?: 0.0,
            legumesAllocatedVegetables = entry["LegumesallocatedVegetables"]?.toDouble()
                ?: 0.0,
            vegetablesVariationsScore = entry["Vegetablesvariationsscore"]?.toDouble()
                ?: 0.0,
            vegetablesCruciferous = entry["VegetablesCruciferous"]?.toDouble() ?: 0.0,
            vegetablesTuberAndBulb = entry["VegetablesTuberandbulb"]?.toDouble() ?: 0.0,
            vegetablesOther = entry["VegetablesOther"]?.toDouble() ?: 0.0,
            legumes = entry["Legumes"]?.toDouble() ?: 0.0,
            vegetablesGreen = entry["VegetablesGreen"]?.toDouble() ?: 0.0,
            vegetablesRedAndOrange = entry["VegetablesRedandorange"]?.toDouble() ?: 0.0,
            fruitHeifaScoreMale = entry["FruitHEIFAscoreMale"]?.toDouble() ?: 0.0,
            fruitHeifaScoreFemale = entry["FruitHEIFAscoreFemale"]?.toDouble() ?: 0.0,
            fruitServeSize = entry["Fruitservesize"]?.toDouble() ?: 0.0,
            fruitVariationsScore = entry["Fruitvariationsscore"]?.toDouble() ?: 0.0,
            fruitPome = entry["FruitPome"]?.toDouble() ?: 0.0,
            fruitTropicalAndSubtropical = entry["FruitTropicalandsubtropical"]?.toDouble()
                ?: 0.0,
            fruitBerry = entry["FruitBerry"]?.toDouble() ?: 0.0,
            fruitStone = entry["FruitStone"]?.toDouble() ?: 0.0,
            fruitCitrus = entry["FruitCitrus"]?.toDouble() ?: 0.0,
            fruitOther = entry["FruitOther"]?.toDouble() ?: 0.0,
            grainsAndCerealsHeifaScoreMale = entry["GrainsandcerealsHEIFAscoreMale"]?.toDouble()
                ?: 0.0,
            grainsAndCerealsHeifaScoreFemale = entry["GrainsandcerealsHEIFAscoreFemale"]?.toDouble()
                ?: 0.0,
            grainsAndCerealsServeSize = entry["Grainsandcerealsservesize"]?.toDouble()
                ?: 0.0,
            grainsAndCerealsNonWholeGrains = entry["GrainsandcerealsNonwholegrains"]?.toDouble()
                ?: 0.0,
            wholeGrainsHeifaScoreMale = entry["WholegrainsHEIFAscoreMale"]?.toDouble()
                ?: 0.0,
            wholeGrainsHeifaScoreFemale = entry["WholegrainsHEIFAscoreFemale"]?.toDouble()
                ?: 0.0,
            wholeGrainsServeSize = entry["Wholegrainsservesize"]?.toDouble() ?: 0.0,
            meatAndAlternativesHeifaScoreMale = entry["MeatandalternativesHEIFAscoreMale"]?.toDouble()
                ?: 0.0,
            meatAndAlternativesHeifaScoreFemale = entry["MeatandalternativesHEIFAscoreFemale"]?.toDouble()
                ?: 0.0,
            meatAndAlternativesWithLegumesAllocatedServeSize = entry["Meatandalternativeswithlegumesallocatedservesize"]?.toDouble()
                ?: 0.0,
            legumesAllocatedMeatAndAlternatives = entry["LegumesallocatedMeatandalternatives"]?.toDouble()
                ?: 0.0,
            dairyAndAlternativesHeifaScoreMale = entry["DairyandalternativesHEIFAscoreMale"]?.toDouble()
                ?: 0.0,
            dairyAndAlternativesHeifaScoreFemale = entry["DairyandalternativesHEIFAscoreFemale"]?.toDouble()
                ?: 0.0,
            dairyAndAlternativesServeSize = entry["Dairyandalternativesservesize"]?.toDouble()
                ?: 0.0,
            sodiumHeifaScoreMale = entry["SodiumHEIFAscoreMale"]?.toDouble() ?: 0.0,
            sodiumHeifaScoreFemale = entry["SodiumHEIFAscoreFemale"]?.toDouble() ?: 0.0,
            sodiumMgMilligrams = entry["Sodiummgmilligrams"]?.toDouble() ?: 0.0,
            alcoholHeifaScoreMale = entry["AlcoholHEIFAscoreMale"]?.toDouble() ?: 0.0,
            alcoholHeifaScoreFemale = entry["AlcoholHEIFAscoreFemale"]?.toDouble() ?: 0.0,
            alcoholStandardDrinks = entry["Alcoholstandarddrinks"]?.toDouble() ?: 0.0,
            waterHeifaScoreMale = entry["WaterHEIFAscoreMale"]?.toDouble() ?: 0.0,
            waterHeifaScoreFemale = entry["WaterHEIFAscoreFemale"]?.toDouble() ?: 0.0,
            water = entry["Water"]?.toDouble() ?: 0.0,
            waterTotalMl = entry["WaterTotalmL"]?.toDouble() ?: 0.0,
            beverageTotalMl = entry["BeverageTotalmL"]?.toDouble() ?: 0.0,
            sugarHeifaScoreMale = entry["SugarHEIFAscoreMale"]?.toDouble() ?: 0.0,
            sugarHeifaScoreFemale = entry["SugarHEIFAscoreFemale"]?.toDouble() ?: 0.0,
            sugar = entry["Sugar"]?.toDouble() ?: 0.0,
            saturatedFatHeifaScoreMale = entry["SaturatedFatHEIFAscoreMale"]?.toDouble()
                ?: 0.0,
            saturatedFatHeifaScoreFemale = entry["SaturatedFatHEIFAscoreFemale"]?.toDouble()
                ?: 0.0,
            saturatedFat = entry["SaturatedFat"]?.toDouble() ?: 0.0,
            unsaturatedFatHeifaScoreMale = entry["UnsaturatedFatHEIFAscoreMale"]?.toDouble()
                ?: 0.0,
            unsaturatedFatHeifaScoreFemale = entry["UnsaturatedFatHEIFAscoreFemale"]?.toDouble()
                ?: 0.0,
            unsaturatedFatServeSize = entry["UnsaturatedFatservesize"]?.toDouble() ?: 0.0
        )
    }
}

