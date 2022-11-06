package com.mansao.myapplication.ui

import android.app.SearchManager
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.mansao.myapplication.R
import com.mansao.myapplication.data.Result
import com.mansao.myapplication.databinding.FragmentNewsBinding

class NewsFragment : Fragment() {

    private var tabName: String? = null

    private var _binding: FragmentNewsBinding? = null
    private val binding get() = _binding
    private lateinit var newsAdapter: NewsAdapter
    private lateinit var factory: ViewModelFactory

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentNewsBinding.inflate(layoutInflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        tabName = arguments?.getString(ARG_TAB)

        factory = ViewModelFactory.getInstance(requireActivity())
        val viewModel: NewsViewModel by viewModels {
            factory
        }

        newsAdapter = NewsAdapter { news ->
            if (news.isBookmarked) {
                viewModel.deleteNews(news)
            } else {
                viewModel.saveNews(news)
            }
        }

        if (tabName == TAB_NEWS) {
            viewModel.getHeadlineNews().observe(viewLifecycleOwner) { result ->
                if (result != null) {
                    when (result) {
                        is Result.Loading -> {
                            binding?.progressBar?.visibility = View.VISIBLE
                        }
                        is Result.Success -> {
                            binding?.progressBar?.visibility = View.GONE
                            val newsData = result.data
                            newsAdapter.submitList(newsData)
                        }
                        is Result.Error -> {
                            binding?.progressBar?.visibility = View.GONE
                            Toast.makeText(
                                context,
                                result.error,
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }
            }
            searchNews()
        } else if (tabName == TAB_BOOKMARK) {
            binding?.searchView?.visibility = View.GONE
            viewModel.getBookmarkedNews().observe(viewLifecycleOwner) { bookmarkedNews ->
                binding?.progressBar?.visibility = View.GONE
                newsAdapter.submitList(bookmarkedNews)
            }
        }

        binding?.rvNews?.apply {
            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(true)
            adapter = newsAdapter
        }
    }

    private fun searchNews() {
        val searchManager = context?.getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val viewModel: NewsViewModel by viewModels {
            factory
        }
        binding?.apply {
            searchView.setSearchableInfo(searchManager.getSearchableInfo(requireActivity().componentName))
            searchView.queryHint = resources.getString(R.string.search_hint)
            searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    query?.let { viewModel.searchNews(it) }?.observe(viewLifecycleOwner){result ->
                        if (result != null){
                            when(result){
                                is Result.Loading -> {
                                    binding?.progressBar?.visibility = View.VISIBLE
                                }
                                is Result.Success -> {
                                    binding?.progressBar?.visibility = View.GONE
                                    val newsData = result.data
                                    newsAdapter.submitList(newsData)
                                }
                                is Result.Error -> {
                                    binding?.progressBar?.visibility = View.GONE
                                    Toast.makeText(
                                        context,
                                        "Terjadi kesalahan" + result.error,
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    Log.d(TAG, result.error)
                                }
                            }
                        }
                    }
                    return false
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    return false
                }

            })
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    companion object {
        const val ARG_TAB = "tab_name"
        const val TAB_NEWS = "news"
        const val TAB_BOOKMARK = "bookmark"
        private var TAG = NewsFragment::class.java.simpleName
    }
}