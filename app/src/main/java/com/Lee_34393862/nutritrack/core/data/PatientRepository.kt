package com.Lee_34393862.nutritrack.core.data

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import java.io.BufferedReader
import java.io.InputStreamReader

class PatientRepository(val context: Context) {

    private var isAuth: Boolean by mutableStateOf(false)
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
                        isAuth = true
                        patientData = header.zip(values).toMap()
                    }
                }
            }
            reader.close()
        } catch (e: Exception) {
            return Result.failure(e)
        }

        // send response message
        return if (isAuth) {
            Result.success("Auth successful")
        } else {
            Result.failure(Exception("Invalid credentials"))
        }

    }

    fun queryPatientData(queryParam: String) : Result<String> {

        if (!isAuth) {
            return Result.failure(Exception("Not authenticated"))
        }

        try {
            val result = patientData[queryParam]
                ?: return Result.failure(NoSuchElementException("Invalid query"))

            return Result.success(result)

        } catch (e: Exception) {
            return Result.failure(e)
        }
    }

    fun getPatientData(): Result<Map<String,String>> {

        if (!isAuth) {
            return Result.failure(Exception("Not authenticated"))
        }

        return Result.success(patientData)

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
}