package com.example.weatherapp.data.local

import java.text.SimpleDateFormat
import java.util.Locale

class LocalFunctions {
    fun formatDate(currentTime: String) : String{
        if(currentTime.isNotBlank()){
            val inputDate = currentTime
            val inputFormat = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.ENGLISH)
            val outputFormat = SimpleDateFormat("dd, MMM", Locale.ENGLISH)

            val date = inputFormat.parse(inputDate) // Parse the input string
            val formattedDate = outputFormat.format(date!!) // Format to "MMM, dd"

            println(formattedDate)
            return  formattedDate
        }
        else{
            return ""
        }
    }

    fun formatTime(currentTime: String): String{

        // Define the input and output formats
        val inputFormat = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
        val outputFormat = SimpleDateFormat("HH:mm", Locale.getDefault())

        // Parse the input string and format it to the desired format
        val date = inputFormat.parse(currentTime)
        val formattedTime = outputFormat.format(date)

        println(formattedTime) // Output: 00:00
        return formattedTime
    }
}