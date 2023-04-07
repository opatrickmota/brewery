package com.patrickmota.brewery.view.fragments.header

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.patrickmota.brewery.databinding.FragmentHeaderBinding

class HeaderFragment : Fragment() {

    private lateinit var binding: FragmentHeaderBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHeaderBinding.inflate(inflater, container, false)
        return binding.root
    }

}