package org.cream.corutines_practice_red.main.main

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import org.cream.corutines_practice_red.R
import org.cream.corutines_practice_red.databinding.FragmentFavouritesBinding
import org.cream.corutines_practice_red.databinding.FragmentMainBinding

class FavouritesFragment : Fragment() {

    private lateinit var imageSearchViewModel: ImageSearchViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        imageSearchViewModel = ViewModelProvider(this)[ImageSearchViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentMainBinding.inflate(inflater, container, false)
        val root = binding.root

        return root
    }

}