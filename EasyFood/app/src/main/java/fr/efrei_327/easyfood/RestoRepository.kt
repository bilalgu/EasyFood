package fr.efrei_327.easyfood

import android.net.Uri
import com.google.android.gms.tasks.Continuation
import com.google.android.gms.tasks.Task
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.UploadTask
import fr.efrei_327.easyfood.RestoRepository.Singleton.databaseRef
import fr.efrei_327.easyfood.RestoRepository.Singleton.downloadUri
import fr.efrei_327.easyfood.RestoRepository.Singleton.restoList
import fr.efrei_327.easyfood.RestoRepository.Singleton.restoListdistance
import fr.efrei_327.easyfood.RestoRepository.Singleton.storageRef
import java.util.*
import javax.security.auth.callback.Callback

class RestoRepository {

    // Pour que ça ne se réinitialise pas
    object Singleton {
        // se connecter à l'espace de stockage
        val storageRef = FirebaseStorage.getInstance().getReferenceFromUrl("gs://easyfood-de6e1.appspot.com")

        // se connecter à la référence "restaurants" dans la BDD
        val databaseRef = FirebaseDatabase.getInstance().getReference("restaurants")

        // créer une liste qui contient les restaurants
        val restoList = arrayListOf<RestoModel>()

        // list ordonner de maniere croissante avec les distance
        var restoListdistance = arrayListOf<RestoModel>()

        // contenir le lien de l'image courante
        var downloadUri: Uri? = null
    }

    // Inserer un resto dans la BDD
    fun insertResto(resto: RestoModel) = databaseRef.child(resto.id).setValue(resto)

    fun updateData(callback: () -> Unit){

        databaseRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                //retirer les anciennes
                restoList.clear()
                restoListdistance.clear()

                // recolter la liste
                for(ds in snapshot.children){
                    //construire un objet resto
                    val resto = ds.getValue(RestoModel::class.java)

                    //verifé que le resto n'est pas null
                    if (resto != null){
                        //ajouter le resto à la liste
                        restoList.add(resto)
                        restoListdistance.add(resto)
                    }
                }

                //ordonner la liste
                restoListdistance.sortBy { it.distance }

                //actionner le callback
                callback()
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })
    }

    //envoyer des fichiers sur le storage
    fun uploadImage(file: Uri, callback: () -> Unit){
        //verifier que le fichier n'est pas null
        if (file != null){
            val fileName = UUID.randomUUID().toString() + ".jpg"
            val ref = storageRef.child(fileName)
            val uploadTask = ref.putFile(file)

            //demarrer la tache d'envoi
            uploadTask.continueWithTask(Continuation<UploadTask.TaskSnapshot, Task<Uri>>{ task ->

                //si il y a eu un probleme lors de l'envoi du fichier
                if (!task.isSuccessful){
                    task.exception?.let { throw it }
                }

                return@Continuation ref.downloadUrl

            }).addOnCompleteListener{ task ->
                //verifier si tout a bien fonctionné
                if (task.isSuccessful){
                    //recuperer l'image
                    downloadUri = task.result
                    callback()
                }
            }
        }
    }

    //mettre à jour le resto
    fun updateResto(resto: RestoModel){
        databaseRef.child(resto.id).setValue(resto)
    }


}