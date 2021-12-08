package fr.efrei_327.easyfood.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import fr.efrei_327.easyfood.MainActivity
import fr.efrei_327.easyfood.R

class AddRestoFragment(
    private val context: MainActivity
): Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater?.inflate(R.layout.fragment_add_resto,container,false)

        return view
    }
}