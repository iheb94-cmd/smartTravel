package com.isetr.smarttravel.ui.offers

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.isetr.smarttravel.data.model.Offer
import com.isetr.smarttravel.databinding.ItemOfferBinding

class OffersAdapter(
    private val items: List<Offer>,
    private val onReserve: (Offer) -> Unit
) : RecyclerView.Adapter<OffersAdapter.ViewHolder>() {

    class ViewHolder(val binding: ItemOfferBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ViewHolder(ItemOfferBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val offer = items[position]
        with(holder.binding) {
            tvType.text = offer.type
            tvTitle.text = offer.title
            tvDetails.text = offer.details
            tvPrice.text = offer.price
            btnReserve.setOnClickListener { onReserve(offer) }
        }
    }

    override fun getItemCount() = items.size
}