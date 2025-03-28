package com.example.weatherapp.model

data class GeocodingResponse(
    val results: List<Result>
)



data class Result(
    val address_components: List<AddressComponent>,
    val formatted_address: String?,
    val geometry: Geometry?,
    val place_id: String?,
    val types: List<String>
)

data class AddressComponent(
    val long_name: String?,
    val short_name: String?,
    val types: List<String>
)

data class Geometry(
    val location: Location?,
    val location_type: String?,
    val viewport: Viewport?
)

data class Location(
    val lat: Double?,
    val lng: Double?
)

data class Viewport(
    val northeast: Coordinates?,
    val southwest: Coordinates?
)

data class Coordinates(
    val lat: Double?,
    val lng: Double?
)

