package fr.efrei_327.easyfood.fragments

import android.app.Activity
import android.content.ContentValues.TAG
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.Autocomplete
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode
import fr.efrei_327.easyfood.MainActivity
import fr.efrei_327.easyfood.R
import fr.efrei_327.easyfood.RestoModel
import fr.efrei_327.easyfood.RestoRepository
import fr.efrei_327.easyfood.RestoRepository.Singleton.downloadUri
import java.net.URI
import java.util.*
import java.util.Arrays.asList

class AddRestoFragment(
    private val context: MainActivity
): Fragment() {

    private var uploadedImage: ImageView? = null
    private var file: Uri? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val view = inflater?.inflate(R.layout.fragment_add_resto,container,false)

        //Recupere les différents items du layout
        val resto_adresse = view.findViewById<EditText>(R.id.resto_adresse)
        val confirm_button = view.findViewById<Button>(R.id.confirm_button)
        val resto_nom = view.findViewById<EditText>(R.id.resto_nom)
        val upload_button = view.findViewById<Button>(R.id.upload_button)
        uploadedImage = view.findViewById<ImageView>(R.id.preview_image)


        // Initalisation de places
        Places.initialize(context,"AIzaSyB0UDt7fzkv8KerbNlylOonWcnPEej_i0s")

        var restolat : Double = 0.0
        var restolong : Double = 0.0
        var restoadresse : String = "adresse"

        // Activité pour récupérer latitude, longitude, ...
        val adresseActivity = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {

                val data: Intent? = result.data
                val place = Autocomplete.getPlaceFromIntent(data)

                // met l'adresse sur l'edit text
                resto_adresse.setText(place.address)

                // recupere longitude et latitude
                var resto_latlng = place.latLng.toString()

                resto_latlng = resto_latlng.replace("lat/lng:","")
                resto_latlng = resto_latlng.replace("(","")
                resto_latlng = resto_latlng.replace(")","")
                val split = resto_latlng.split(",")


                restoadresse = place.address
                restolat = split[0].toDouble()
                restolong = split[1].toDouble()

                //Afficher dans la console pour le debugage
                Log.i(TAG,"Lat :  " + restolat)
                Log.i(TAG,"Long :  " + restolong)
                Log.i(TAG,"Adresse :  " + restoadresse)

            }
        }

        // Activité pour récupérer l'image
        val imageActivity = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->

            val data: Intent? = result.data

            if (result.resultCode == Activity.RESULT_OK) {

                //verifie si les données sont nulle
                if (data == null || data.data == null) return@registerForActivityResult

                //recuperer l'image
                file = data.data

                //mettre à jour l'aperçu de l'image
                uploadedImage?.setImageURI(file)


            }
        }

        //Quand on clique sur le boutton upload image
        upload_button.setOnClickListener {
            val intent = Intent()
            intent.type = "image/"
            intent.action = Intent.ACTION_GET_CONTENT
            imageActivity.launch(Intent.createChooser(intent, "Select Picture"))
        }


        // Quand on clique sur l'EditText
        resto_adresse.setOnClickListener(View.OnClickListener {
            val fields = asList(Place.Field.ADDRESS,Place.Field.LAT_LNG)
            val intent = Autocomplete.IntentBuilder(AutocompleteActivityMode.OVERLAY,fields).build(context)

            adresseActivity.launch(intent)

        })

        //Quand on clique sur le Boutton confirmer
        confirm_button.setOnClickListener{ sendData(view,restolat,restolong,restoadresse) }


        return view
    }


    private fun sendData(view: View,restolat: Double,restolong: Double ,restoadresse: String) {

        //heberger sur le bucket(storage)
        val repo = RestoRepository()
        repo.uploadImage(file!!){
            val restoname = view.findViewById<EditText>(R.id.resto_nom).text.toString()
            val downloadImageUrl = downloadUri

            //creer un objet du type RestoModel
            val resto = RestoModel(
                UUID.randomUUID().toString(),
                restoname,
                downloadImageUrl.toString(),
                restoadresse,
                restolat,
                restolong
            )

            //envoyer dans la BDD
            repo.insertResto(resto)
        }

    }

}