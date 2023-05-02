package com.example.pixeltask.ui

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.example.pixeltask.R
import com.example.pixeltask.database.AppDatabase
import com.example.pixeltask.databinding.ActivityMainBinding
import com.example.pixeltask.repository.ProductRepository
import com.example.pixeltask.viewmodel.ProductViewModel
import com.example.pixeltask.viewmodel.ProductViewModelFactory
import com.google.android.material.bottomsheet.BottomSheetDialog


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: ProductViewModel
    private lateinit var adapter: ProductAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        val repository = ProductRepository(AppDatabase.getDatabase(this).productDao())
        val factory = ProductViewModelFactory(repository)
        viewModel = ViewModelProvider(this, factory)[ProductViewModel::class.java]

        viewModel.setNetworkState(isNetworkAvailable(this))

        adapter = ProductAdapter(viewModel.products.value ?: emptyList())


        binding.productList.layoutManager = GridLayoutManager(this, 2)
        binding.productList.adapter = adapter

        viewModel.products.observe(this) { products ->
            adapter.setProducts(products)
        }
        binding.ivDownload.setOnClickListener {
            viewModel.getProducts()
        }

        binding.menuIcon.setOnClickListener {
            val bottomSheetDialog = BottomSheetDialog(this)
            //Set Background Transparent
            bottomSheetDialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
            bottomSheetDialog.setContentView(R.layout.bottom_sheet_layout)
            bottomSheetDialog.show()
        }
    }

    private fun isNetworkAvailable(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val network = connectivityManager.activeNetwork ?: return false
            val activeNetwork = connectivityManager.getNetworkCapabilities(network) ?: return false
            return activeNetwork.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
        } else {
            val activeNetworkInfo = connectivityManager.activeNetworkInfo
            return activeNetworkInfo != null && activeNetworkInfo.isConnected
        }
    }

}
