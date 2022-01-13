package fr.efrei_327.easyfood

class RestoModel(
    val id: String ="id",
    val nom: String="Nom du restaurant",
    val imageUrl: String="https://cc-rivedroite.com/wp-content/uploads/2020/01/otacos.jpg",
    val adresse: String="Adresse",
    val lat: Double = 0.0,
    val long: Double = 0.0,
    var distance: Double? = null,
    val halal: Boolean = false,
    val vegan: Boolean = false
)