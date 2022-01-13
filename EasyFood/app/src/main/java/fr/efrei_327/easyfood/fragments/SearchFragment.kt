package fr.efrei_327.easyfood.fragments

import android.content.ContentValues
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.core.view.isGone
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import fr.efrei_327.easyfood.MainActivity
import fr.efrei_327.easyfood.R
import fr.efrei_327.easyfood.RestoModel
import fr.efrei_327.easyfood.RestoRepository.Singleton.restoList
import fr.efrei_327.easyfood.adapter.RestoAdapterHome
import fr.efrei_327.easyfood.adapter.RestoAdapterSearch
import java.util.*
import kotlin.collections.ArrayList


class SearchFragment(
    private val context: MainActivity
): Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater?.inflate(R.layout.fragment_search,container,false)

        //recuperer la recycler view
        val searchRecyclerView = view.findViewById<RecyclerView>(R.id.search_recyclerview)
        searchRecyclerView.adapter = RestoAdapterSearch(context,restoList,R.layout.item_vertical_search)
        searchRecyclerView.layoutManager = LinearLayoutManager(context)



        //gerer les filtres

        //instanciation des valeurs
        val distance_button = view.findViewById<Button>(R.id.distance_button)
        val notes_button = view.findViewById<Button>(R.id.notes_button)
        val vegan_button = view.findViewById<Button>(R.id.vegan_button)
        val halal_button = view.findViewById<Button>(R.id.halal_button)

        var filtersList: ArrayList<String> = arrayListOf()
        var rLTextChange = ArrayList<RestoModel>(restoList)
        var rLFilter = ArrayList<RestoModel>(restoList)


        distance_button.setOnClickListener {

            if (filtersList.contains("distance")){
                lookUnSelected(distance_button)
                filtersList.remove("distance")

            }
            else{
                lookSelected(distance_button)
                filtersList.add("distance")

            }

            filterSearchView(filtersList,rLFilter)

            searchRecyclerView.adapter = RestoAdapterSearch(context,rLFilter,R.layout.item_vertical_search)
            searchRecyclerView.layoutManager = LinearLayoutManager(context)

        }

        notes_button.setOnClickListener {

            if (filtersList.contains("notes")){
                lookUnSelected(notes_button)
                filtersList.remove("notes")
            }
            else{
                lookSelected(notes_button)
                filtersList.add("notes")
            }

        }

        vegan_button.setOnClickListener {

            if (filtersList.contains("vegan")){
                lookUnSelected(vegan_button)
                filtersList.remove("vegan")

                rLFilter.clear()
                rLFilter.addAll(rLTextChange)
            }
            else{
                lookSelected(vegan_button)
                filtersList.add("vegan")
            }

            filterSearchView(filtersList,rLFilter)

            searchRecyclerView.adapter = RestoAdapterSearch(context,rLFilter,R.layout.item_vertical_search)
            searchRecyclerView.layoutManager = LinearLayoutManager(context)

        }

        halal_button.setOnClickListener {

            if (filtersList.contains("halal")){
                lookUnSelected(halal_button)
                filtersList.remove("halal")

                rLFilter.clear()
                rLFilter.addAll(rLTextChange)
            }
            else{
                lookSelected(halal_button)
                filtersList.add("halal")
            }

            filterSearchView(filtersList,rLFilter)

            searchRecyclerView.adapter = RestoAdapterSearch(context,rLFilter,R.layout.item_vertical_search)
            searchRecyclerView.layoutManager = LinearLayoutManager(context)

        }



        //gerer la searchview
        val searchView = view.findViewById<SearchView>(R.id.searchview)

        searchView.setOnQueryTextListener(object: SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {

                if (newText != null) {

                    rLTextChange.clear()
                    rLTextChange.addAll(restoList.filter { it.nom.lowercase(
                    ).contains(newText.lowercase().subSequence(0,newText.length))
                    })

                    rLFilter.clear()
                    rLFilter.addAll(rLTextChange)

                    filterSearchView(filtersList,rLFilter)

                    searchRecyclerView.adapter = RestoAdapterSearch(context,rLFilter,R.layout.item_vertical_search)
                }
                searchRecyclerView.layoutManager = LinearLayoutManager(context)
                return true
            }

        })


        return view
    }

    private fun filterSearchView(filterList: ArrayList<String>,currentList: ArrayList<RestoModel>) {




        if (filterList.contains("distance")){
            // Tri ordre croissance par distance
            currentList.sortBy { it.distance }
        }
        else {
            val newList = ArrayList<RestoModel>(restoList)
            val newListCompare = ArrayList<RestoModel>(restoList)
            // Remet la liste de base en remouvant les items qui n'apparaissent plus
            newListCompare.forEach {
                if (!currentList.contains(it)){
                    newList.remove(it)
                }
            }
            currentList.clear()
            currentList.addAll(newList)
        }

        if (filterList.contains("notes")){
            // Tri du mieux notés
        }
        if (filterList.contains("vegan")){
            // Garde que les vegan = true
            val newList = ArrayList<RestoModel>(restoList)
            newList.clear()
            newList.addAll(currentList.filter { it.vegan  })

            currentList.clear()
            currentList.addAll(newList)

        }
        if (filterList.contains("halal")) {
            // Garde que les halal = True
            val newList = ArrayList<RestoModel>(restoList)
            newList.clear()
            newList.addAll(currentList.filter { it.halal  })

            currentList.clear()
            currentList.addAll(newList)
        }

    }


    private fun UnSelectedAllFiltersButtons(distance_button: Button,notes_button: Button,vegan_button: Button,halal_button: Button) {
        lookUnSelected(distance_button)
        lookUnSelected(notes_button)
        lookUnSelected(vegan_button)
        lookUnSelected(halal_button)
    }

    private fun lookSelected(current_button: Button) {
        val grey = ContextCompat.getColor(context,R.color.light_gray)
        val white = ContextCompat.getColor(context,R.color.medium_white)

        current_button.setTextColor(white)
        current_button.setBackgroundColor(grey)
    }

    private fun lookUnSelected(current_button: Button) {
        val dark_white = ContextCompat.getColor(context,R.color.dark_white)
        val black = ContextCompat.getColor(context,R.color.black)

        current_button.setTextColor(black)
        current_button.setBackgroundColor(dark_white)
    }
}