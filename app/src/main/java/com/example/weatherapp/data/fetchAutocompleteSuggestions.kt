import android.util.Log
import com.google.android.libraries.places.api.model.AutocompletePrediction
import com.google.android.libraries.places.api.net.PlacesClient
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest
import com.google.android.libraries.places.api.model.TypeFilter

fun fetchAutocompleteSuggestions(query: String, placesClient: PlacesClient, callback: (List<AutocompletePrediction>) -> Unit) {
    if (query.isBlank()) {
        callback(emptyList())
        return
    }

    val request = FindAutocompletePredictionsRequest.builder()
        .setQuery(query)
        .setTypeFilter(TypeFilter.CITIES) // Filter for cities only
        .build()

    placesClient.findAutocompletePredictions(request)
        .addOnSuccessListener { response ->
            callback(response.autocompletePredictions)
            Log.d("Prediction", response.autocompletePredictions.toString())
        }
        .addOnFailureListener { exception ->
            exception.printStackTrace()
            callback(emptyList())
        }
}
