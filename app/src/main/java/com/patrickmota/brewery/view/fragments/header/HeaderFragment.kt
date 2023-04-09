package com.patrickmota.brewery.view.fragments.header

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.patrickmota.brewery.R
import com.patrickmota.brewery.databinding.FragmentHeaderBinding
import com.patrickmota.brewery.view.activities.FavoriteActivity

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
        binding.activityDetailHeaderMaterialToolbar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.favorite_home -> {
                    startActivity(
                        Intent(context, FavoriteActivity::class.java)
                    )
                    true
                }
                else -> false
            }
        }
    }

}