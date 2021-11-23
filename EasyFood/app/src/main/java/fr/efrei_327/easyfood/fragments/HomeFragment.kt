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
import fr.efrei_327.easyfood.RestoModel
import fr.efrei_327.easyfood.adapter.RestoAdapter

class HomeFragment(
    private val context: MainActivity
) : Fragment() {

    @SuppressLint("CutPasteId")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view =  inflater?.inflate(R.layout.fragment_home, container, false)

        // créer une liste qui stock les resto
        val restoList = arrayListOf<RestoModel>()

        //enregistrer n resto dans la liste
        restoList.add(RestoModel(
            "Otacos",
            "https://cc-rivedroite.com/wp-content/uploads/2020/01/otacos.jpg"))

        restoList.add(RestoModel(
            "PizzaHut",
            "https://logowik.com/content/uploads/images/130_pizzahut.jpg"))

        restoList.add(RestoModel(
            "Quick",
            "https://upload.wikimedia.org/wikipedia/fr/thumb/c/c9/Quick_1991_logo.svg/1024px-Quick_1991_logo.svg.png"))

        restoList.add(RestoModel(
            "McDonald's",
            "https://images.bfmtv.com/NUJHUYUkXAYVPZAR888_w9rjrNc=/0x0:1196x1192/1196x0/images/-458880.jpg"))

        restoList.add(RestoModel(
            "Subway",
            "https://i.ytimg.com/vi/CBIHwTK-l34/maxresdefault.jpg"))

        //recuperer le 1er recyclerview
        val horizontalRecyclerView1 = view.findViewById<RecyclerView>(R.id.horizontal_recycler_view1)
        horizontalRecyclerView1.adapter = RestoAdapter(context,restoList,R.layout.item_horizontal_proche)

        //recuperer le 2er recyclerview
        val horizontalRecyclerView2 = view.findViewById<RecyclerView>(R.id.horizontal_recycler_view2)
        horizontalRecyclerView2.adapter = RestoAdapter(context,restoList,R.layout.item_horizontal_note)

        return view
    }
}