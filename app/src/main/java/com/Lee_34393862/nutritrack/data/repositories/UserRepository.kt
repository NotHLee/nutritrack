package com.Lee_34393862.nutritrack.data.repositories

import android.content.Context
import android.util.Log
import com.Lee_34393862.nutritrack.data.AppDatabase
import com.Lee_34393862.nutritrack.data.dao.FoodIntakeDao
import com.Lee_34393862.nutritrack.data.dao.PatientDao
import com.Lee_34393862.nutritrack.data.entities.Patient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch

class UserRepository {

    var patientDao: PatientDao
    var foodIntakeDao: FoodIntakeDao

    private val repositoryScope = CoroutineScope(Dispatchers.IO)
    private var currentUserJob: Job? = null

    // cache current user for quick access globally
    private var _currentUser = MutableStateFlow<User?>(null)
    val currentUser = _currentUser.asStateFlow()

    // flag to determine login status
    private var _isLogin = MutableStateFlow<Boolean>(false)
    var isLogin = _isLogin.asStateFlow()

    constructor(context: Context) {
        patientDao = AppDatabase.getDatabase(context = context).patientDao()
        foodIntakeDao = AppDatabase.getDatabase(context = context).foodIntakeDao()
    }

    suspend fun authenticate(userId: String, password: String): Result<String> {

        // query for patient entity with userId and match password
        val patient: Patient? = patientDao.getPatientByUserId(userId = userId).firstOrNull()

        // null means user does not exist
        if (patient == null) {
            return Result.failure(Exception("User id does not exist"))
        }

        // null password means this account has not been claimed, registration is required first
        if (patient.password.isEmpty()) {
            return Result.failure(Exception("User id has not been registered"))
        }

        if (patient.password != password) {
            return Result.failure(Exception("Password does not match"))
        }

        // start observing the user from database for any changes after login
        // collectLatest is used for fast update (e.g. immediate transition to landing page after logout)
        currentUserJob = repositoryScope.launch {
            patientDao.getPatientByUserId(userId).collectLatest { patient ->
                _currentUser.value = patient?.let { patientToUser(it) }
            }
        }

        // set initial value after successful auth
        _currentUser.value = patientToUser(patient)
        _isLogin.value = true
        Log.d("user repo", isLogin.value.toString())
        return Result.success("Login successful")
    }

    suspend fun register(userId: String, phoneNumber: String, password: String, confirmPassword: String): Result<String> {

        // query for patient entity with userId
        val patient: Patient? = patientDao.getPatientByUserId(userId = userId).firstOrNull()

        // null means user does not exist
        if (patient == null) {
            return Result.failure(Exception("User id does not exist"))
        }

        // non null password means this account has been claimed
        if (patient.password.isNotEmpty()) {
            return Result.failure(Exception("User id has been claimed"))
        }

        // match phone number
        if (patient.phoneNumber != phoneNumber) {
            return Result.failure(Exception("Incorrect phone number"))
        }

        // ensure password is not empty
        if (password.isEmpty()) {
            return Result.failure(Exception("Password cannot be empty"))
        }

        if (password != confirmPassword) {
            return Result.failure(Exception("Passwords do not match"))
        }

        patientDao.update(patient.copy(password = password))
        return Result.success("User id successfully claimed")

    }

    fun logout() {
        currentUserJob?.cancel()        // make sure to stop observing after current user logs out
        _currentUser.value = null
        _isLogin.value = false
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