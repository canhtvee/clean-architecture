package com.example.newsapp.ui.headline

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.newsapp.R
import com.example.newsapp.adapters.HeadlineBindingAdapter
import com.example.newsapp.utils.SourcePlanning
import com.example.newsapp.viewmodels.HeadlineViewModel
import com.example.newsapp.viewmodels.WebViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class BusinessFragment : Fragment(R.layout.fragment_business) {

    @Inject
    lateinit var headlineViewModel: HeadlineViewModel

    @Inject
    lateinit var sourcePlanning: SourcePlanning

    @Inject
    lateinit var webViewModel: WebViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val recyclerView = view.findViewById<RecyclerView>(R.id.businessRecyclerView)
        val swipeRefreshLayout = view.findViewById<SwipeRefreshLayout>(R.id.business_layout_swipe_to_refresh)
        swipeRefreshLayout.setOnRefreshListener {
            headlineViewModel.deleteHeadline(sourcePlanning.businessSources)
        }

        headlineViewModel.fetchBusiness()
        headlineViewModel.businessData.observe(viewLifecycleOwner, { resource ->
            HeadlineBindingAdapter(this, webViewModel)
                .bindHeadline(resource, recyclerView, swipeRefreshLayout)
        })

        headlineViewModel.refreshFlag.observe(viewLifecycleOwner, { flag ->
            if (flag) {
                headlineViewModel._deleteFlag.value = false
                headlineViewModel.fetchBusiness()
                headlineViewModel.businessData.observe(viewLifecycleOwner, { resource ->
                    resource.toString() // can not be deleted
                })
            }
        })
    }
}