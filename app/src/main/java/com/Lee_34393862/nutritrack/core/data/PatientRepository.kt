package com.Lee_34393862.nutritrack.core.data

import android.content.Context
import java.io.BufferedReader
import java.io.InputStreamReader
import java.util.SortedSet

class PatientRepository(val context: Context) {

    fun queryPatientData(userId: String, queryParam: String) : Result<String?> {

        val userIdIndex: Int = 1
        val assets = context.assets

        try {
            val inputStream = assets.open("patients.csv")
            val reader = BufferedReader(InputStreamReader(inputStream))
            val header = reader.readLine()

            reader.useLines { lines ->
                lines.forEach { line ->
                    val values = line.split(",")
                    if (values[userIdIndex] == userId) {
                        return Result.success(values[header.indexOf(queryParam)])
                    }
                }
            }

            reader.close()

            return Result.failure(
                NoSuchElementException("Invalid user id")
            )
        } catch (e: Exception) {
            return Result.failure(e)
        }
    }

    fun getAllUserId() : Result<SortedSet<Int>> {

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

            return Result.success(userIdSet)
        } catch (e: Exception) {
            return Result.failure(e)
        }
    }
}