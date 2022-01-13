package fr.efrei_327.easyfood

import android.net.Uri
import android.os.Bundle
import android.view.*
import fr.efrei_327.easyfood.adapter.RestoAdapterHome
import android.view.ViewGroup

import android.view.LayoutInflater
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.isInvisible
import androidx.fragment.app.DialogFragment
import com.bumptech.glide.Glide

class RestoPopupHome(
    private val adapter: RestoAdapterHome,
    private val currentResto: RestoModel
) : DialogFragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.FullScreenDialogStyle)

    }

    override fun onStart() {
        super.onStart()
        val dialog = dialog
        if (dialog != null) {
            setupComponents()
            val width = ViewGroup.LayoutParams.MATCH_PARENT
            val height = ViewGroup.LayoutParams.MATCH_PARENT
            dialog.window!!.setLayout(width, height)
        }
    }

    private fun setupComponents() {
        //actualiser l'image de lu resto
        val restoImage = dialog?.findViewById<ImageView>(R.id.popup_image)
        if (restoImage != null) {
            Glide.with(adapter.context).load(Uri.parse(currentResto.imageUrl)).into(restoImage)
        }

        dialog?.findViewById<TextView>(R.id.popup_resto_nom)!!.text = currentResto.nom

        dialog?.findViewById<TextView>(R.id.popup_adresses)!!.text = currentResto.adresse

        dialog?.findViewById<TextView>(R.id.popup_distance)!!.text = currentResto.distance.toString().substring(0,4)

        if (currentResto.halal){
            dialog?.findViewById<TextView>(R.id.popup_halal)!!.text = "oui"
        }

        if (currentResto.vegan){
            dialog?.findViewById<TextView>(R.id.popup_vegan)!!.text = "oui"
        }

        //close button
        dialog?.findViewById<ImageView>(R.id.popup_close_button)!!.setOnClickListener {
            //fermer la fenetre
            dismiss()
        }


    }

    override fun onCreateView(
        inflater: LayoutInflater,
        parent: ViewGroup?,
        state: Bundle?
    ): View? {
        super.onCreateView(inflater, parent, state)
        return activity!!.layoutInflater.inflate(R.layout.popup_resto_details, parent, false)
    }

    companion object {
        const val TAG = "FullScreenDialog"
    }
}

/*
class RestoPopup(adapter: RestoAdapter) : Dialog(adapter.context) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.popup_resto_details)

    }
}
 */