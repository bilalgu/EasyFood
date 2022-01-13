package fr.efrei_327.easyfood

import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.content.pm.PackageManager
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.app.ActivityCompat
import fr.efrei_327.easyfood.fragments.HomeFragment
import android.widget.TextView

import android.graphics.PorterDuff
import android.location.Location
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.google.android.gms.location.*
import com.google.android.material.bottomnavigation.BottomNavigationView
import fr.efrei_327.easyfood.RestoRepository.Singleton.restoList
import fr.efrei_327.easyfood.fragments.AddRestoFragment
import fr.efrei_327.easyfood.fragments.SearchFragment
import java.lang.Math.*
import java.util.ArrayList
import java.util.jar.Manifest


class MainActivity : AppCompatActivity() {
    lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //charger le repository
        val repo = RestoRepository()
        var charge = true

        //avoir la localisation courante qui s'actualise
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        fetchLocation()

        //mettre à jour la liste de resto
        repo.updateData{

            //charger la page d'accueil (une seule fois)
            if (charge) {
                loadFragment(HomeFragment(this))
                charge = false
            }

            //importer la barre de navigation
            val navigationView = findViewById<BottomNavigationView>(R.id.navigation_view)
            navigationView.setOnNavigationItemSelectedListener {
                when(it.itemId)
                {
                    R.id.home_page -> {
                        //charger la page d'accueil
                        loadFragment(HomeFragment(this))
                        return@setOnNavigationItemSelectedListener true
                    }

                    R.id.search_page -> {
                        loadFragment(SearchFragment(this))
                        return@setOnNavigationItemSelectedListener true
                    }

                    R.id.add_resto_page -> {
                        loadFragment(AddRestoFragment(this))
                        return@setOnNavigationItemSelectedListener true
                    }
                    else -> false
                }
            }
        }
    }

    private fun loadFragment(fragment: Fragment) {

        //injecter le fragment dans le fragment_container (dans Main Activity)
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragment_container, fragment)
        transaction.addToBackStack(null)
        transaction.commit()

    }

    private fun distanceInKm(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Double {
        val MEAN_EARTH_RADIUS = 6371.008
        val D2R = PI / 180.0

        val lonDiff = (lon2 - lon1) * D2R
        val latDiff = (lat2 - lat1) * D2R
        val latSin = sin(latDiff / 2.0)
        val lonSin = sin(lonDiff / 2.0)
        val a = latSin * latSin + (cos(lat1 * D2R) * cos(lat2 * D2R) * lonSin * lonSin)
        val c = 2.0 * atan2(sqrt(a), sqrt(1.0 - a))
        return MEAN_EARTH_RADIUS * c
    }

    private fun fetchLocation() {

        //charger le repository
        val repo = RestoRepository()

        val request = LocationRequest()
        request.interval = 10000
        request.fastestInterval = 5000
        request.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        val permission = ContextCompat.checkSelfPermission(this, ACCESS_FINE_LOCATION
        )
        if (permission == PackageManager.PERMISSION_GRANTED) {

            fusedLocationProviderClient.requestLocationUpdates(request, object : LocationCallback() {
                override fun onLocationResult(locationResult: LocationResult) {
                    val location: Location? = locationResult.lastLocation
                    if (location != null) {

                        //Affichage de la localisation
                        /*
                        val toast: Toast = Toast.makeText(applicationContext, "${location.latitude} ${location.longitude}", Toast.LENGTH_SHORT)
                        val view: View? = toast.view

                        if (view != null) {
                            view.getBackground().setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_IN)
                        }

                        val text: TextView? = view?.findViewById(android.R.id.message)
                        if (text != null) {
                            text.setTextColor(Color.BLACK)
                        }

                        toast.show()
                        */

                        //Mettre à jour les distances
                        restoList.forEach{
                            val dis = distanceInKm(it.lat,it.long,location.latitude,location.longitude)
                            it.distance = dis
                            repo.updateResto(it)
                        }
                    }
                }
            }, null)
        }
    }
}