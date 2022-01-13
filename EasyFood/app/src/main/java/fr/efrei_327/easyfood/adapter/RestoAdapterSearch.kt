package fr.efrei_327.easyfood.adapter

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import fr.efrei_327.easyfood.*

class RestoAdapterSearch(
    val context: MainActivity,
    private val restoList: List<RestoModel>,
    private val layoutID: Int)
    : RecyclerView.Adapter<RestoAdapterSearch.ViewHolder>(){

    // permet de faire une boite de composant
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view){
        val restoImage = view.findViewById<ImageView>(R.id.image_item)
        val restoName = view.findViewById<TextView>(R.id.image_name)
        val restoDistance = view.findViewById<TextView>(R.id.search_resto_distance)
    }


    //permet d'injecter item_horizontal_proche
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(layoutID, parent, false)

        return ViewHolder(view)
    }

    //permet de mettre a jour chaque modele avec le resto
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        // recuperer les informations du resto
        val currentResto = restoList[position]

        //utiliser glide pour les images
        Glide.with(context).load(Uri.parse(currentResto.imageUrl)).into(holder.restoImage)

        //mettre à jour le nom du restaurant
        holder.restoName.text = currentResto.nom
        holder.restoDistance.text = currentResto.distance.toString().substring(0,4)

        //interaction lors du clic sur un resto
        holder.itemView.setOnClickListener{
            val dialog = RestoPopupSearch(this,currentResto)

            // aficher la popup
            val ft: FragmentTransaction = context.supportFragmentManager.beginTransaction()
            dialog.show(ft, RestoPopupSearch.TAG)

        }

    }

    override fun getItemCount(): Int = restoList.size

}