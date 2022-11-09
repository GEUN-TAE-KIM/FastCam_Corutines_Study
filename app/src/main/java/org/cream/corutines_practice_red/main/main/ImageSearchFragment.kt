package org.cream.corutines_practice_red.main.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.cream.corutines_practice_red.databinding.FragmentMainBinding

class ImageSearchFragment : Fragment() {

    private lateinit var imageSearchViewModel: ImageSearchViewModel
    private val adapter: ImageSearchAdapter = ImageSearchAdapter {
        imageSearchViewModel.toggle(it)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        imageSearchViewModel = ViewModelProvider(this)[ImageSearchViewModel::class.java]

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val binding = FragmentMainBinding.inflate(inflater, container, false)
        val root = binding.root

        //프래그먼트에서는 lifecycleScope을 viewLifecycleOwner에서 가져와야 함
        //lifecycleScope은 프래그 먼트를
        //viewLifecycleOwner.lifecycleScope 뷰를 가져와서 프래그먼트에서는 이게 적절 함
        viewLifecycleOwner.lifecycleScope.launch {
            imageSearchViewModel.pagingDataFlow
                .collectLatest { items ->
                    adapter.submitData(items)
                }
        }

        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = GridLayoutManager(context, 4)

        binding.search.setOnClickListener {
            val query = binding.editText.text.trim().toString()
            imageSearchViewModel.handleQuery(query)
        }

        return root
    }
}