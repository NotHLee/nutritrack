package com.Lee_34393862.nutritrack.data.datasource

import android.content.Context
import android.util.Log
import com.Lee_34393862.nutritrack.data.dao.PatientDao
import com.Lee_34393862.nutritrack.data.databases.AppDatabase
import com.Lee_34393862.nutritrack.data.entities.Patient
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.runBlocking
import java.io.BufferedReader
import java.io.InputStreamReader
import kotlin.Int
import kotlin.sequences.forEach

/**
 * Class to load csv data to AppDatabase upon first entry
 */
class CsvDataSource(val context: Context) {

    private val CSV_FILENAME = "patients.csv"
    val database: AppDatabase = AppDatabase.getDatabase(context)
    val patientDao: PatientDao = database.patientDao()

    /**
     * Check if database ia already seeded
     */

    private fun isDatabaseSeeded(): Boolean {
        return runBlocking {
            patientDao.getAllPatients().first().isNotEmpty()
        }
    }

    /**
     * Parse patient data CSV to a list of patients
     */
    private fun parseCSV(): List<Map<String, String>> {
        var patients = mutableListOf<Map<String, String>>()
        val assets = context.assets
        val inputStream = assets.open(CSV_FILENAME)
        val reader = BufferedReader(InputStreamReader(inputStream))
        val header = reader.readLine().split(",")

        reader.useLines { lines ->
            var patientData: Map<String, String> = mapOf()
            lines.forEach { line ->
                val values = line.split(",")
                patientData = header.zip(values).toMap()
                patients.add(patientData)
            }
        }
        reader.close()
        return patients
    }

    /**
     * Helper method to map csv entries to patient entity
     */
    fun mapToPatient(entry: Map<String, String>): Patient {
        return Patient(
            id = 0,
            phoneNumber = entry["phoneNumber"] ?: "",
            userId = entry["userId"]?.toInt() ?: 0,
            sex = entry["sex"] ?: "",
            heifaTotalScoreMale = entry["heifaTotalScoreMale"]?.toDouble() ?: 0.0,
            heifaTotalScoreFemale = entry["heifaTotalScoreFemale"]?.toDouble() ?: 0.0,
            discretionaryHeifaScoreMale = entry["discretionaryHeifaScoreMale"]?.toDouble()
                ?: 0.0,
            discretionaryHeifaScoreFemale = entry["discretionaryHeifaScoreFemale"]?.toDouble()
                ?: 0.0,
            discretionaryServeSize = entry["discretionaryServeSize"]?.toDouble() ?: 0.0,
            vegetablesHeifaScoreMale = entry["vegetablesHeifaScoreMale"]?.toDouble() ?: 0.0,
            vegetablesHeifaScoreFemale = entry["vegetablesHeifaScoreFemale"]?.toDouble()
                ?: 0.0,
            vegetablesWithLegumesAllocatedServeSize = entry["vegetablesWithLegumesAllocatedServeSize"]?.toDouble()
                ?: 0.0,
            legumesAllocatedVegetables = entry["legumesAllocatedVegetables"]?.toDouble()
                ?: 0.0,
            vegetablesVariationsScore = entry["vegetablesVariationsScore"]?.toDouble()
                ?: 0.0,
            vegetablesCruciferous = entry["vegetablesCruciferous"]?.toDouble() ?: 0.0,
            vegetablesTuberAndBulb = entry["vegetablesTuberAndBulb"]?.toDouble() ?: 0.0,
            vegetablesOther = entry["vegetablesOther"]?.toDouble() ?: 0.0,
            legumes = entry["legumes"]?.toDouble() ?: 0.0,
            vegetablesGreen = entry["vegetablesGreen"]?.toDouble() ?: 0.0,
            vegetablesRedAndOrange = entry["vegetablesRedAndOrange"]?.toDouble() ?: 0.0,
            fruitHeifaScoreMale = entry["fruitHeifaScoreMale"]?.toDouble() ?: 0.0,
            fruitHeifaScoreFemale = entry["fruitHeifaScoreFemale"]?.toDouble() ?: 0.0,
            fruitServeSize = entry["fruitServeSize"]?.toDouble() ?: 0.0,
            fruitVariationsScore = entry["fruitVariationsScore"]?.toDouble() ?: 0.0,
            fruitPome = entry["fruitPome"]?.toDouble() ?: 0.0,
            fruitTropicalAndSubtropical = entry["fruitTropicalAndSubtropical"]?.toDouble()
                ?: 0.0,
            fruitBerry = entry["fruitBerry"]?.toDouble() ?: 0.0,
            fruitStone = entry["fruitStone"]?.toDouble() ?: 0.0,
            fruitCitrus = entry["fruitCitrus"]?.toDouble() ?: 0.0,
            fruitOther = entry["fruitOther"]?.toDouble() ?: 0.0,
            grainsAndCerealsHeifaScoreMale = entry["grainsAndCerealsHeifaScoreMale"]?.toDouble()
                ?: 0.0,
            grainsAndCerealsHeifaScoreFemale = entry["grainsAndCerealsHeifaScoreFemale"]?.toDouble()
                ?: 0.0,
            grainsAndCerealsServeSize = entry["grainsAndCerealsServeSize"]?.toDouble()
                ?: 0.0,
            grainsAndCerealsNonWholeGrains = entry["grainsAndCerealsNonWholeGrains"]?.toDouble()
                ?: 0.0,
            wholeGrainsHeifaScoreMale = entry["wholeGrainsHeifaScoreMale"]?.toDouble()
                ?: 0.0,
            wholeGrainsHeifaScoreFemale = entry["wholeGrainsHeifaScoreFemale"]?.toDouble()
                ?: 0.0,
            wholeGrainsServeSize = entry["wholeGrainsServeSize"]?.toDouble() ?: 0.0,
            meatAndAlternativesHeifaScoreMale = entry["meatAndAlternativesHeifaScoreMale"]?.toDouble()
                ?: 0.0,
            meatAndAlternativesHeifaScoreFemale = entry["meatAndAlternativesHeifaScoreFemale"]?.toDouble()
                ?: 0.0,
            meatAndAlternativesWithLegumesAllocatedServeSize = entry["meatAndAlternativesWithLegumesAllocatedServeSize"]?.toDouble()
                ?: 0.0,
            legumesAllocatedMeatAndAlternatives = entry["legumesAllocatedMeatAndAlternatives"]?.toDouble()
                ?: 0.0,
            dairyAndAlternativesHeifaScoreMale = entry["dairyAndAlternativesHeifaScoreMale"]?.toDouble()
                ?: 0.0,
            dairyAndAlternativesHeifaScoreFemale = entry["dairyAndAlternativesHeifaScoreFemale"]?.toDouble()
                ?: 0.0,
            dairyAndAlternativesServeSize = entry["dairyAndAlternativesServeSize"]?.toDouble()
                ?: 0.0,
            sodiumHeifaScoreMale = entry["sodiumHeifaScoreMale"]?.toDouble() ?: 0.0,
            sodiumHeifaScoreFemale = entry["sodiumHeifaScoreFemale"]?.toDouble() ?: 0.0,
            sodiumMgMilligrams = entry["sodiumMgMilligrams"]?.toDouble() ?: 0.0,
            alcoholHeifaScoreMale = entry["alcoholHeifaScoreMale"]?.toDouble() ?: 0.0,
            alcoholHeifaScoreFemale = entry["alcoholHeifaScoreFemale"]?.toDouble() ?: 0.0,
            alcoholStandardDrinks = entry["alcoholStandardDrinks"]?.toDouble() ?: 0.0,
            waterHeifaScoreMale = entry["waterHeifaScoreMale"]?.toDouble() ?: 0.0,
            waterHeifaScoreFemale = entry["waterHeifaScoreFemale"]?.toDouble() ?: 0.0,
            water = entry["water"]?.toDouble() ?: 0.0,
            waterTotalMl = entry["waterTotalMl"]?.toDouble() ?: 0.0,
            beverageTotalMl = entry["beverageTotalMl"]?.toDouble() ?: 0.0,
            sugarHeifaScoreMale = entry["sugarHeifaScoreMale"]?.toDouble() ?: 0.0,
            sugarHeifaScoreFemale = entry["sugarHeifaScoreFemale"]?.toDouble() ?: 0.0,
            sugar = entry["sugar"]?.toDouble() ?: 0.0,
            saturatedFatHeifaScoreMale = entry["saturatedFatHeifaScoreMale"]?.toDouble()
                ?: 0.0,
            saturatedFatHeifaScoreFemale = entry["saturatedFatHeifaScoreFemale"]?.toDouble()
                ?: 0.0,
            saturatedFat = entry["saturatedFat"]?.toDouble() ?: 0.0,
            unsaturatedFatHeifaScoreMale = entry["unsaturatedFatHeifaScoreMale"]?.toDouble()
                ?: 0.0,
            unsaturatedFatHeifaScoreFemale = entry["unsaturatedFatHeifaScoreFemale"]?.toDouble()
                ?: 0.0,
            unsaturatedFatServeSize = entry["unsaturatedFatServeSize"]?.toDouble() ?: 0.0
        )
    }
}