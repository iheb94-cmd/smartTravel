package com.isetr.smarttravel.ui.offers

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.isetr.smarttravel.data.model.Offre
import com.isetr.smarttravel.databinding.ItemOffreBinding

class OffreAdapter(
    private val onDetails: (Offre) -> Unit,
    private val onReserver: (Offre) -> Unit
) : ListAdapter<Offre, OffreAdapter.VH>(Diff) {

    inner class VH(val b: ItemOffreBinding) : RecyclerView.ViewHolder(b.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        VH(ItemOffreBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: VH, position: Int) {
        val o = getItem(position)
        with(holder.b) {
            tvNom.text = o.nomOffre
            tvInfo.text = "${o.nomCircuit} - ${o.nomHotel}"
            tvDates.text = "${o.dateDebut} → ${o.dateFin}"
            tvPrix.text = "${o.prixTotal} TND"
            btnDetails.setOnClickListener { onDetails(o) }
            btnReserver.setOnClickListener { onReserver(o) }
        }
    }

    object Diff : DiffUtil.ItemCallback<Offre>() {
        override fun areItemsTheSame(a: Offre, b: Offre) = a.idOffre == b.idOffre
        override fun areContentsTheSame(a: Offre, b: Offre) = a == b
    }
}