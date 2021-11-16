package fr.efrei_327.easyfood

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import fr.efrei_327.easyfood.fragments.HomeFragment

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //injecter le fragment dans le fragment_container (dans activity_main)
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragment_container, HomeFragment())
        transaction.addToBackStack(null)
        transaction.commit()
    }
}