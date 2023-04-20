package com.patrickmota.brewery.favorite

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.patrickmota.brewery.core.data.models.BreweryModel
import com.patrickmota.brewery.core.data.models.BreweryResponse
import com.patrickmota.brewery.core.data.models.toBreweryModel
import com.patrickmota.brewery.favorite.databinding.FavoriteBreweriesItemBinding

class FavoriteBreweriesFragmentAdapter(
    private val items: List<BreweryResponse>,
    private val onItemClicked: (BreweryResponse) -> Unit,
    private val onFavoriteClicked: (BreweryResponse) -> Unit
) : RecyclerView.Adapter<FavoriteBreweriesFragmentAdapter.ViewHolder>() {

    var favoriteBreweries: ArrayList<BreweryModel> = arrayListOf()

    class ViewHolder(val binding: FavoriteBreweriesItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            FavoriteBreweriesItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder) {
            with(items[position]) {

                binding.favoriteBreweriesItemBreweryNameTextView.text = name
                binding.favoriteBreweriesItemLogoTextTextView.text = name?.substring(0, 1)
                binding.favoriteBreweriesItemBreweryCategoryTextView.text = breweryType

                if (favoriteBreweries.contains(items[position].toBreweryModel())) {
                    binding.favoriteBreweriesItemFavoriteImageView.setImageResource(com.patrickmota.brewery.common.R.drawable.ic_favorited)
                }

                binding.favoriteBreweriesItemFavoriteImageView.setOnClickListener {
                    onFavoriteClicked(items[position])
                    onItemClicked(items[position])
                }

                binding.root.setOnClickListener {
                    onItemClicked(items[position])
                }
            }
        }
    }

    override fun getItemCount(): Int = items.size
}