package fr.efrei_327.easyfood.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import fr.efrei_327.easyfood.MainActivity
import fr.efrei_327.easyfood.R
import fr.efrei_327.easyfood.RestoRepository.Singleton.restoList
import fr.efrei_327.easyfood.RestoRepository.Singleton.restoListdistance
import fr.efrei_327.easyfood.adapter.RestoAdapterHome

class HomeFragment(
    private val context: MainActivity
) : Fragment() {

    @SuppressLint("CutPasteId")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view =  inflater?.inflate(R.layout.fragment_home, container, false)

        //recuperer le 1er recyclerview "les plus proches"
        val horizontalRecyclerView1 = view.findViewById<RecyclerView>(R.id.horizontal_recycler_view1)
        horizontalRecyclerView1.adapter = RestoAdapterHome(context, restoListdistance,R.layout.item_horizontal_home)

        //recuperer le 2er recyclerview
        val horizontalRecyclerView2 = view.findViewById<RecyclerView>(R.id.horizontal_recycler_view2)
        horizontalRecyclerView2.adapter = RestoAdapterHome(context,restoList,R.layout.item_horizontal_home)

        return view
    }
}