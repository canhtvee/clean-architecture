package com.example.newsapp.ui.home

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.newsapp.R
import com.example.newsapp.adapters.OuterRecyclerViewAdapter
import com.example.newsapp.data.models.Article
import com.example.newsapp.utils.Resource
import com.example.newsapp.utils.SourcePlanning
import com.example.newsapp.viewmodels.ExploreTopicViewModel
import com.example.newsapp.viewmodels.ExploreViewModel
import com.example.newsapp.viewmodels.HeadlineViewModel
import com.example.newsapp.viewmodels.WebViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.annotation.meta.When
import javax.inject.Inject

@AndroidEntryPoint
class ExploreFragment : Fragment() {

    @Inject
    lateinit var exploreViewModel: ExploreViewModel

    @Inject
    lateinit var exploreTopicViewModel: ExploreTopicViewModel

    @Inject
    lateinit var webViewModel: WebViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_explore, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val toolbarTitle = view.findViewById<TextView>(R.id.toolbarTitle)
        toolbarTitle.text = resources.getString(R.string.explore)
        val swipeRefreshLayout = view.findViewById<SwipeRefreshLayout>(R.id.explore_layout_swipe_to_refresh)
        swipeRefreshLayout.setOnRefreshListener {
            exploreViewModel.deleteExplore()
        }

        exploreViewModel.refreshFlag.observe(viewLifecycleOwner, { flag ->
            if (flag) {
                exploreViewModel._deleteFlag.value = false
                exploreViewModel.fetchExplore()
                exploreViewModel.exploreData.observe(viewLifecycleOwner, { resource ->
                    resource.toString() // can not be deleted
                })
            }
        })

        val recyclerView = view.findViewById<RecyclerView>(R.id.exploreRecyclerView)
        exploreViewModel.fetchExplore()
        exploreViewModel.exploreData.observe(viewLifecycleOwner, { resource ->
            when (resource) {
                is Resource.Loading -> {
                    Toast.makeText(view.context, "Loading...", Toast.LENGTH_SHORT).show()
                }

                is Resource.Error -> {
                    Toast.makeText(view.context, "Error", Toast.LENGTH_SHORT).show()
                }

                is Resource.Success -> {
                    swipeRefreshLayout.isRefreshing = false
                    recyclerView.apply {
                        layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
                            .apply { initialPrefetchItemCount = 4 }
                        adapter = OuterRecyclerViewAdapter(
                            resource.data,
                            { onOuterItemClick(it) },
                            { onInnerItemClick(it) }
                        )
                        hasFixedSize()
                    }
                }
            }
        })
    }

    private fun onOuterItemClick(tag: String){
        val toolbarTitle =  when (tag) {
            "apple" -> "Apple News"
            "android" -> "Android News"
            "huawei" -> "Huawei News"
            "tesla" -> "Tesla News"
            "bitcoin" -> "Bitcoin News"
            "facebook" -> "Facebook News"
            else -> "Twitter News"
        }
        exploreTopicViewModel.tag = toolbarTitle
        exploreTopicViewModel.fetTagHeadline(tag)
        val mainNavController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_container)
        mainNavController.navigate(R.id.action_global_to_exploreTopicFragment)
    }
    private fun onInnerItemClick(article: Article){
        webViewModel.setViewData(article)
        val mainNavController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_container)
        mainNavController.navigate(R.id.action_global_to_webViewFragment)
    }
}