package com.patrickmota.brewery.header

import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.patrickmota.brewery.common.NavigationHelper
import com.patrickmota.brewery.header.databinding.FragmentHeaderBinding
import com.patrickmota.brewery.common.R

class HeaderFragment : Fragment() {
    private lateinit var binding: FragmentHeaderBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHeaderBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupMenuListener()
    }

    private fun setupMenuListener() {
        binding.fragmentDetailHeaderMaterialToolbar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.favorite_home -> {
                    val destination = Uri.parse(NavigationHelper.FAVORITE_DEEP_LINK)
                    findNavController().navigate(destination)
                    true
                }
                else -> false
            }
        }
    }

}