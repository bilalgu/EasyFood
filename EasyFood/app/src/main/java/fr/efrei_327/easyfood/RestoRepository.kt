package fr.efrei_327.easyfood

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import fr.efrei_327.easyfood.RestoRepository.Singleton.databaseRef
import fr.efrei_327.easyfood.RestoRepository.Singleton.restoList
import javax.security.auth.callback.Callback

class RestoRepository {

    // Pour que ça ne se réinitialise pas
    object Singleton {

        // se connecter à la référence "restaurants" dans la BDD
        val databaseRef = FirebaseDatabase.getInstance().getReference("restaurants")

        // créer une liste qui contient les restaurants
        val restoList = arrayListOf<RestoModel>()
    }

    // Inserer un resto dans la BDD
    fun insertResto(resto: RestoModel) = databaseRef.child(resto.id).setValue(resto)

    fun updateData(callback: () -> Unit){

        databaseRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                //retirer les anciennes
                restoList.clear()

                // recolter la liste
                for(ds in snapshot.children){
                    //construire un objet resto
                    val resto = ds.getValue(RestoModel::class.java)

                    //verifé que le resto n'est pas null
                    if (resto != null){
                        //ajouter le resto à la liste
                        restoList.add(resto)
                    }
                }
                //actionner le callback
                callback()
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })
    }


}