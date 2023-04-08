package com.patrickmota.brewery.view.adapters.searchFragment

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.patrickmota.brewery.data.remote.model.BreweryResponse
import com.patrickmota.brewery.databinding.SearchResultItemBinding

class SearchFragmentAdapter(
    private val items: List<BreweryResponse>,
    private val onItemClicked: (BreweryResponse) -> Unit
) : RecyclerView.Adapter<SearchFragmentAdapter.ViewHolder>() {

    class ViewHolder(val binding: SearchResultItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            SearchResultItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder) {
            with(items[position]) {

                binding.searchResultItemBreweryNameTextView.text = name
                binding.searchResultItemLogoTextTextView.text = name?.substring(0, 1)
                binding.searchResultItemBreweryCategoryTextView.text = breweryType

                binding.root.setOnClickListener {
                    onItemClicked(items[position])
                }
            }
        }
    }

    override fun getItemCount(): Int = items.size

}