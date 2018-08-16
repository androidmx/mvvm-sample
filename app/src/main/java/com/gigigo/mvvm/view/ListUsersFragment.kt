package com.gigigo.mvvm.view

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.TextView
import android.widget.Toast
import com.gigigo.mvvm.R
import com.gigigo.mvvm.core.EndlessScrollListener
import com.gigigo.mvvm.databinding.ListUsersFragmentBinding
import com.gigigo.mvvm.view.adapter.ListUsersAdapter
import com.gigigo.mvvm.viewmodel.ListUsersViewModel
import kotlinx.android.synthetic.main.list_users_fragment.*

/**
 * @author JG - August 02, 2018
 * @version 0.0.1
 * @since 0.0.1
 */
class ListUsersFragment: Fragment() {
    companion object {
        fun newInstance() = ListUsersFragment()
        private const val PAGE: Int = 1
        private const val PER_PAGE: Int = 10
    }

    private lateinit var viewModel: ListUsersViewModel
    private lateinit var adapter: ListUsersAdapter
    private lateinit var endlessScrollListener: EndlessScrollListener

    private lateinit var binding: ListUsersFragmentBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initViewModel()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        binding = DataBindingUtil.inflate<ListUsersFragmentBinding>(inflater,
                        R.layout.list_users_fragment,
                        container,
                        false)
        //return inflater.inflate(R.layout.list_users_fragment, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUI()
        populateRecycler(PAGE, PER_PAGE)
    }

    private fun initViewModel() {
        viewModel = ViewModelProviders.of(this).get(ListUsersViewModel::class.java)
    }

    private fun initUI() {
        val layoutManager = LinearLayoutManager(context,
                LinearLayoutManager.VERTICAL,
                false)

        adapter = ListUsersAdapter()
        endlessScrollListener = EndlessScrollListener(layoutManager,
                object : EndlessScrollListener.ScrollListener {
                    override fun onHide() { }

                    override fun onShow() { }

                    override fun onLoadMore(page: Int, totalItemsCount: Int, view: RecyclerView) {
                        if(totalItemsCount >= PER_PAGE) {
                            populateRecycler(page + 1, PER_PAGE)
                        }
                    }
                })

        recycler_view_users.layoutManager = layoutManager
        recycler_view_users.setHasFixedSize(true)
        recycler_view_users.addItemDecoration(DividerItemDecoration(this.context,
                DividerItemDecoration.VERTICAL))
        recycler_view_users.adapter = adapter
        recycler_view_users.addOnScrollListener(endlessScrollListener)

        swipe_refresh_layout.setColorSchemeResources(R.color.colorPrimary,
                R.color.colorPrimaryDark,
                R.color.colorAccent)
        swipe_refresh_layout.setOnRefreshListener {
            adapter.clear()
            endlessScrollListener.resetState()
            populateRecycler(PAGE, PER_PAGE)
        }

        viewModel.error.observe(this, Observer {
            t: Throwable? -> showError(t?.message!!)
            swipe_refresh_layout.isRefreshing = false
        })

        viewModel.isLoading.observe(this, Observer {
            swipe_refresh_layout.isRefreshing = it!!
        })
    }

    private fun populateRecycler(page: Int, perPage: Int) {
        viewModel.getListUsers(page, perPage).observe(this, Observer {
            if (adapter.itemCount == 0) {
                adapter.data = it?.toMutableList()!!
                recycler_view_users.layoutAnimation = AnimationUtils
                        .loadLayoutAnimation(context, R.anim.layout_animation_slide_bottom)
                recycler_view_users.adapter!!.notifyDataSetChanged()
                recycler_view_users.scheduleLayoutAnimation()
            } else {
                adapter.addAll(it!!)
            }
        })
    }

    private fun showError(message: String){
        val snackBar = Snackbar.make(binding.root, message, Snackbar.LENGTH_INDEFINITE)
        val textView = snackBar.view.findViewById<TextView>(android.support.design.R.id.snackbar_text)
        textView.maxLines = 5
        snackBar.setAction(R.string.retry, { populateRecycler(PAGE, PER_PAGE) })
        snackBar.show()
    }
}