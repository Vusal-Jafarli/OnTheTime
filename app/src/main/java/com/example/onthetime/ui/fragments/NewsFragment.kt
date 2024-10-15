package com.example.onthetime.ui.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.onthetime.R
import com.example.onthetime.adapter.NewsAdapter
import com.example.onthetime.databinding.FragmentNewsBinding
import com.example.onthetime.viewmodel.CalendarViewModel
import com.example.onthetime.viewmodel.NewsViewModel
import com.google.firebase.auth.FirebaseAuth
import com.squareup.picasso.Picasso

class NewsFragment : Fragment() {
    lateinit var binding: FragmentNewsBinding
    private val newsViewModel: NewsViewModel by activityViewModels()
    lateinit var adapter: NewsAdapter


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        binding = FragmentNewsBinding.inflate(inflater, container, false)
//        newsViewModel = ViewModelProvider(this).get(NewsViewModel::class.java)
        return binding.root
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var imageView = view.findViewById<ImageView>(R.id.news_icon_image)
        var linearLayout = view.findViewById<LinearLayout>(R.id.linear_layout_news)
        var recyclerView = view.findViewById<RecyclerView>(R.id.news_recyclerview)
        var swipeRefreshLayout = binding.swipeRefreshLayout

        swipeRefreshLayout.setOnRefreshListener {
            newsViewModel.fetchNews(FirebaseAuth.getInstance().currentUser?.uid.toString())
            swipeRefreshLayout.isRefreshing = false
        }

        binding.addButtonNewsfeed.setOnClickListener {
            val navController = findNavController()
            navController.navigate(R.id.addNewsFragment)
        }

        recyclerView = binding.newsRecyclerview
        adapter = NewsAdapter(emptyList())
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(context)


        val userID = FirebaseAuth.getInstance().currentUser!!.uid


        newsViewModel.news.observe(viewLifecycleOwner) { item ->

                newsViewModel.repository.getUserStatus(userID) { status ->
                    if (status == "employee") {
                        newsViewModel.repository.getUserAnyField(
                            userID,
                            "employees",
                            "employerId"
                        ) { employerID ->
                            newsViewModel.repository.getNewsWithSnapshotbyID(employerID!!) { list ->
                                newsViewModel.setNews(list)
                                if (list.size > 0) {
                                    recyclerView.visibility = View.VISIBLE
                                } else {
                                    imageView.visibility = View.VISIBLE
                                    linearLayout.visibility = View.VISIBLE
                                    recyclerView.visibility = View.GONE

                                }
                                    adapter.news = list

                            }
                        }
                    } else if (status == "employer") {
                        newsViewModel.repository.getUserAnyField(
                            userID,
                            "employers",
                            "id"
                        ) { employerID ->
                            newsViewModel.repository.getNewsWithSnapshotbyID(employerID!!) { list ->
                                newsViewModel.setNews(list)
                                if (list.size > 0) {
                                    recyclerView.visibility = View.VISIBLE
                                } else {
                                    imageView.visibility = View.VISIBLE
                                    linearLayout.visibility = View.VISIBLE
                                    recyclerView.visibility = View.GONE

                                }
                                    adapter.news = list

                            }
                        }
                    }


                }
                //                binding.proggresBarNews.visibility = View.GONE
            adapter.notifyDataSetChanged()

        }


    }

}
