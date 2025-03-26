package com.Lee_34393862.nutritrack.core.data

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.Lee_34393862.nutritrack.core.model.Patient
import java.io.BufferedReader
import java.io.InputStreamReader
import java.util.SortedSet

class PatientRepository(val context: Context) {

    var currentPatient: Patient by mutableStateOf(Patient("", ""))
    var patientData: Map<String, String> by mutableStateOf(mapOf())

    fun authenticate(userId: String, phoneNumber: String): Result<Patient> {
        queryPatientData(userId, "PhoneNumber")
            .onSuccess { query ->
                if (phoneNumber == query?.get("PhoneNumber")) {
                    // save current patient for this session
                    currentPatient = Patient(userId, phoneNumber)

                    // cache data for current patient


                    return Result.success(currentPatient)
                }
            }
            .onFailure { exception ->
                return Result.failure(exception)
            }
        return Result.failure(
            NoSuchElementException("Invalid credentials")
        )
    }

    private fun queryPatientData(userId: String, queryParam: String?) : Result<Map<String, String>?> {

        val userIdIndex: Int = 1
        val assets = context.assets

        try {
            val inputStream = assets.open("patients.csv")
            val reader = BufferedReader(InputStreamReader(inputStream))
            val header = reader.readLine().split(",")

            reader.useLines { lines ->
                lines.forEach { line ->
                    val values = line.split(",")
                    if (values[userIdIndex] == userId && queryParam != null) {
                        return Result.success(mapOf(Pair(queryParam, values[header.indexOf(queryParam)])))
                    }
                    else if (values[userIdIndex] == userId && queryParam == null) {
                        return Result.success(header.zip(values).toMap())
                    }
                }
            }
            reader.close()
        } catch (e: Exception) {
            return Result.failure(e)
        }

        return Result.failure(
            NoSuchElementException("Invalid user id")
        )
    }

    fun getAllUserId() : Result<List<String>> {

        val userIdSet = sortedSetOf<Int>()
        val userIdIndex: Int = 1
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