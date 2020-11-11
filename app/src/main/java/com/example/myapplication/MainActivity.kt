package com.example.myapplication

import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SnapHelper
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private var adapter: MyAdapter? = null
    private var listData: MutableList<Item?>? = null
    private var offset = 0
    private var limit = 3
    private var isLoading = false
    private var isScrollToNewItem = false
    private var positionCurrent = 0
    private var totalItem = 10

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        listData = ArrayList()
        loadData()
        adapter = MyAdapter(listData)
        rcvItem.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        rcvItem.setHasFixedSize(true)
        rcvItem.adapter = adapter
        val snapHelperTo: SnapHelper = LinearSnapHelper()
        snapHelperTo.attachToRecyclerView(rcvItem)
        initScrollListener()
    }

    private fun initScrollListener() {
        rcvItem.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                /*When rcv stop in pos = size - 2 and size - 1 will smoothScrollToPosition to new item*/
                isScrollToNewItem = newState == 0 && positionCurrent >= listData?.size!! - 2
            }

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val linearLayoutManager = recyclerView.layoutManager as LinearLayoutManager?
                positionCurrent = linearLayoutManager?.findLastCompletelyVisibleItemPosition()!!
                if (!isLoading) {
                    /*Position visible = size - 1 -> Load more*/
                    if (positionCurrent == listData?.size!! - 1) {
                        loadMore()
                        isLoading = true
                    }
                }
            }
        })
    }

    private fun loadData() {
        for (i in 0 until limit) {
            listData?.add(Item(i))
        }
    }

    private fun loadMore() {
        /*Add view load more*/
        listData?.add(null)
        adapter?.notifyItemInserted(listData?.size!! - 1)
        Handler().postDelayed(Runnable {
            /*Remove view load more, after add new item to list*/
            listData?.removeAt(listData?.size!! - 1)
            offset = listData?.size!!
            adapter?.notifyItemRemoved(offset)
            var currentSize = offset
            val nextLimit = currentSize + 3
            while (currentSize - 1 < nextLimit) {
                listData?.add(Item(currentSize))
                currentSize++
            }
            adapter?.notifyDataSetChanged()
            if (isScrollToNewItem) {
                rcvItem.smoothScrollToPosition(offset)
            }
            /*Block load more when current position = number limited item*/
            isLoading = listData?.size!! == totalItem + 1
        }, 2000)
    }
}