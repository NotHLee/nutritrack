package com.Lee_34393862.nutritrack.data

import android.util.Log
import com.Lee_34393862.nutritrack.data.entities.Patient
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

object AuthManager {

    private var _updateUserJob: Job? = null
    private var _currentUser = MutableStateFlow<User?>(null)
    val currentUser: StateFlow<User?> = _currentUser.asStateFlow()

    // function to cache current user session for global access to user
    fun saveCurrentUserSession(patient: Patient) {
        _currentUser.value = patientToUser(patient)
    }

    // function to save current job that is updating the user via patient repository
    fun saveUpdateUserJob(job: Job) {
        _updateUserJob = job
    }

    fun logout() {
        _updateUserJob?.cancel()
        _updateUserJob = null
        _currentUser.value = null
    }
}

fun patientToUser(patient: Patient): User {
    return User(
        userId = patient.userId,
        name = patient.name,
        password = patient.password,
        phoneNumber = patient.phoneNumber,
        sex = patient.sex,
        heifaTotalScore = if (patient.sex.equals("Male", ignoreCase = true)) patient.heifaTotalScoreMale else patient.heifaTotalScoreFemale,
        discretionaryHeifaScore = if (patient.sex.equals("Male", ignoreCase = true)) patient.discretionaryHeifaScoreMale else patient.discretionaryHeifaScoreFemale,
        discretionaryServeSize = patient.discretionaryServeSize,
        vegetablesHeifaScore = if (patient.sex.equals("Male", ignoreCase = true)) patient.vegetablesHeifaScoreMale else patient.vegetablesHeifaScoreFemale,
        vegetablesWithLegumesAllocatedServeSize = patient.vegetablesWithLegumesAllocatedServeSize,
        legumesAllocatedVegetables = patient.legumesAllocatedVegetables,
        vegetablesVariationsScore = patient.vegetablesVariationsScore,
        vegetablesCruciferous = patient.vegetablesCruciferous,
        vegetablesTuberAndBulb = patient.vegetablesTuberAndBulb,
        vegetablesOther = patient.vegetablesOther,
        legumes = patient.legumes,
        vegetablesGreen = patient.vegetablesGreen,
        vegetablesRedAndOrange = patient.vegetablesRedAndOrange,
        fruitHeifaScore = if (patient.sex.equals("Male", ignoreCase = true)) patient.fruitHeifaScoreMale else patient.fruitHeifaScoreFemale,
        fruitServeSize = patient.fruitServeSize,
        fruitVariationsScore = patient.fruitVariationsScore,
        fruitPome = patient.fruitPome,
        fruitTropicalAndSubtropical = patient.fruitTropicalAndSubtropical,
        fruitBerry = patient.fruitBerry,
        fruitStone = patient.fruitStone,
        fruitCitrus = patient.fruitCitrus,
        fruitOther = patient.fruitOther,
        grainsAndCerealsHeifaScore = if (patient.sex.equals("Male", ignoreCase = true)) patient.grainsAndCerealsHeifaScoreMale else patient.grainsAndCerealsHeifaScoreFemale,
        grainsAndCerealsServeSize = patient.grainsAndCerealsServeSize,
        grainsAndCerealsNonWholeGrains = patient.grainsAndCerealsNonWholeGrains,
        wholeGrainsHeifaScore = if (patient.sex.equals("Male", ignoreCase = true)) patient.wholeGrainsHeifaScoreMale else patient.wholeGrainsHeifaScoreFemale,
        wholeGrainsServeSize = patient.wholeGrainsServeSize,
        meatAndAlternativesHeifaScore = if (patient.sex.equals("Male", ignoreCase = true)) patient.meatAndAlternativesHeifaScoreMale else patient.meatAndAlternativesHeifaScoreFemale,
        meatAndAlternativesWithLegumesAllocatedServeSize = patient.meatAndAlternativesWithLegumesAllocatedServeSize,
        legumesAllocatedMeatAndAlternatives = patient.legumesAllocatedMeatAndAlternatives,
        dairyAndAlternativesHeifaScore = if (patient.sex.equals("Male", ignoreCase = true)) patient.dairyAndAlternativesHeifaScoreMale else patient.dairyAndAlternativesHeifaScoreFemale,
        dairyAndAlternativesServeSize = patient.dairyAndAlternativesServeSize,
        sodiumHeifaScore = if (patient.sex.equals("Male", ignoreCase = true)) patient.sodiumHeifaScoreMale else patient.sodiumHeifaScoreFemale,
        sodiumMgMilligrams = patient.sodiumMgMilligrams,
        alcoholHeifaScore = if (patient.sex.equals("Male", ignoreCase = true)) patient.alcoholHeifaScoreMale else patient.alcoholHeifaScoreFemale,
        alcoholStandardDrinks = patient.alcoholStandardDrinks,
        waterHeifaScore = if (patient.sex.equals("Male", ignoreCase = true)) patient.waterHeifaScoreMale else patient.waterHeifaScoreFemale,
        water = patient.water,
        waterTotalMl = patient.waterTotalMl,
        beverageTotalMl = patient.beverageTotalMl,
        sugarHeifaScore = if (patient.sex.equals("Male", ignoreCase = true)) patient.sugarHeifaScoreMale else patient.sugarHeifaScoreFemale,
        sugar = patient.sugar,
        saturatedFatHeifaScore = if (patient.sex.equals("Male", ignoreCase = true)) patient.saturatedFatHeifaScoreMale else patient.saturatedFatHeifaScoreFemale,
        saturatedFat = patient.saturatedFat,
        unsaturatedFatHeifaScore = if (patient.sex.equals("Male", ignoreCase = true)) patient.unsaturatedFatHeifaScoreMale else patient.unsaturatedFatHeifaScoreFemale,
        unsaturatedFatServeSize = patient.unsaturatedFatServeSize
    )
}


/**
 * Represents the current user of this session
 */
data class User (
    val userId: String,
    val name: String = "",
    val password: String = "",
    val phoneNumber: String = "",
    val sex: String = "",
    val heifaTotalScore: Double = 0.0,
    val discretionaryHeifaScore: Double = 0.0,
    val discretionaryServeSize: Double = 0.0,
    val vegetablesHeifaScore: Double = 0.0,
    val vegetablesWithLegumesAllocatedServeSize: Double = 0.0,
    val legumesAllocatedVegetables: Double = 0.0,
    val vegetablesVariationsScore: Double = 0.0,
    val vegetablesCruciferous: Double = 0.0,
    val vegetablesTuberAndBulb: Double = 0.0,
    val vegetablesOther: Double = 0.0,
    val legumes: Double = 0.0,
    val vegetablesGreen: Double = 0.0,
    val vegetablesRedAndOrange: Double = 0.0,
    val fruitHeifaScore: Double = 0.0,
    val fruitServeSize: Double = 0.0,
    val fruitVariationsScore: Double = 0.0,
    val fruitPome: Double = 0.0,
    val fruitTropicalAndSubtropical: Double = 0.0,
    val fruitBerry: Double = 0.0,
    val fruitStone: Double = 0.0,
    val fruitCitrus: Double = 0.0,
    val fruitOther: Double = 0.0,
    val grainsAndCerealsHeifaScore: Double = 0.0,
    val grainsAndCerealsServeSize: Double = 0.0,
    val grainsAndCerealsNonWholeGrains: Double = 0.0,
    val wholeGrainsHeifaScore: Double = 0.0,
    val wholeGrainsServeSize: Double = 0.0,
    val meatAndAlternativesHeifaScore: Double = 0.0,
    val meatAndAlternativesWithLegumesAllocatedServeSize: Double = 0.0,
    val legumesAllocatedMeatAndAlternatives: Double = 0.0,
    val dairyAndAlternativesHeifaScore: Double = 0.0,
    val dairyAndAlternativesServeSize: Double = 0.0,
    val sodiumHeifaScore: Double = 0.0,
    val sodiumMgMilligrams: Double = 0.0,
    val alcoholHeifaScore: Double = 0.0,
    val alcoholStandardDrinks: Double = 0.0,
    val waterHeifaScore: Double = 0.0,
    val water: Double = 0.0,
    val waterTotalMl: Double = 0.0,
    val beverageTotalMl: Double = 0.0,
    val sugarHeifaScore: Double = 0.0,
    val sugar: Double = 0.0,
    val saturatedFatHeifaScore: Double = 0.0,
    val saturatedFat: Double = 0.0,
    val unsaturatedFatHeifaScore: Double = 0.0,
    val unsaturatedFatServeSize: Double = 0.0
)