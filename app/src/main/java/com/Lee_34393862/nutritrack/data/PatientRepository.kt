package com.Lee_34393862.nutritrack.data

import android.content.Context
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.core.content.edit
import java.io.BufferedReader
import java.io.InputStreamReader

class PatientRepository(val context: Context) {

    private var patientData: Map<String, String> by mutableStateOf(mapOf())

    fun authenticate(userId: String, phoneNumber: String): Result<String> {

        val userIdIndex = 1
        val phoneNumIndex = 0
        val assets = context.assets

        // match the user id and phone number, then cache patient data if successful
        try {
            val inputStream = assets.open("patients.csv")
            val reader = BufferedReader(InputStreamReader(inputStream))
            val header = reader.readLine().split(",")

            reader.useLines { lines ->
                lines.forEach { line ->
                    val values = line.split(",")
                    if (values[userIdIndex] == userId && values[phoneNumIndex] == phoneNumber) {
                        patientData = header.zip(values).toMap()
                        Log.d("data", patientData.toString())
                        return Result.success("Successful login")
                    }
                }
            }
            reader.close()
        } catch (e: Exception) {
            return Result.failure(e)
        }

        return Result.failure(Exception("Invalid credentials"))

    }

    fun queryPatientData(queryParam: String) : Result<String> {

        if (patientData.isEmpty()) {
            return Result.failure(Exception("Not logged in"))
        }

        try {
            val result = patientData[queryParam]
                ?: return Result.failure(NoSuchElementException("Invalid query"))

            return Result.success(result)

        } catch (e: Exception) {
            return Result.failure(e)
        }
    }

    fun getAllUserId() : Result<List<String>> {

        val userIdSet = sortedSetOf<Int>()
        val userIdIndex = 1
        val assets = context.assets

        try {
            val inputStream = assets.open("patients.csv")
            val reader = BufferedReader(InputStreamReader(inputStream))

            reader.useLines { lines ->
                lines.drop(1).forEach { line ->
                    val values = line.split(",")
                    userIdSet.add(values[userIdIndex].toInt())
                }
            }

            return Result.success(userIdSet.toList().map { it.toString() })
        } catch (e: Exception) {
            return Result.failure(e)
        }
    }

    fun savePreference(key: String, value: String) {

        // get the relevant id
        val id = queryPatientData("User_ID").getOrThrow()

        context.getSharedPreferences(
            id, Context.MODE_PRIVATE
        ).edit() {
            putString(key, value)
        }
    }

    fun getPreference(key: String): String {

        // get the relevant id
        val id = queryPatientData("User_ID").getOrThrow()

        val sharedPref = context.getSharedPreferences(
            id, Context.MODE_PRIVATE
        )

        return sharedPref.getString(key, "").toString()
    }

    fun getTotalFoodScore(): String {

        // extract food score based on gender
        val gender = queryPatientData("Sex").getOrThrow()

        return when (gender) {
            "Male" -> queryPatientData("HEIFAtotalscoreMale").getOrThrow()
            "Female" -> queryPatientData("HEIFAtotalscoreFemale").getOrThrow()
            else -> "error"
        }
    }
}