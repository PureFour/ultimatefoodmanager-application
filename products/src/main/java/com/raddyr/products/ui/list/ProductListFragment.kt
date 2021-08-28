package com.raddyr.products.ui.list

import android.opengl.Visibility
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.raddyr.core.base.BaseFragment
import com.raddyr.core.data.network.interceptors.NetworkConnectionInterceptor
import com.raddyr.core.util.extensions.displayQuestionDialog
import com.raddyr.core.util.formaters.DateFormatterUtils
import com.raddyr.core.util.interfaces.Home
import com.raddyr.core.util.responseHandler.Callback
import com.raddyr.core.util.responseHandler.ResponseHandler
import com.raddyr.core.util.tokenUtil.TokenUtil
import com.raddyr.products.R
import com.raddyr.products.data.db.dao.ProductDao
import com.raddyr.products.data.model.FiltersRequest
import com.raddyr.products.data.model.Product
import com.raddyr.products.data.model.ProductMapper
import com.raddyr.products.ui.customViews.FilterBottomSheetFragment
import com.raddyr.products.ui.customViews.ProductQuantityChanger
import com.raddyr.products.ui.details.ProductDetailsActivity
import com.raddyr.products.util.FiltersCache
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.product_list_fragment.*
import javax.inject.Inject

@AndroidEntryPoint
class ProductListFragment(override val contentViewLayout: Int = R.layout.product_list_fragment) :
    BaseFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var responseHandler: ResponseHandler

    @Inject
    lateinit var tokenUtil: TokenUtil

    @Inject
    lateinit var productdao: ProductDao

    @Inject
    lateinit var dateFormatterUtils: DateFormatterUtils

    @Inject
    lateinit var filtersCache: FiltersCache

    @Inject
    lateinit var networkInterceptor: NetworkConnectionInterceptor


    private val viewModel by viewModels<ProductListViewModel> { viewModelFactory }
    private val startForResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {}

    override fun onResume() {
        emptyList.visibility = GONE
        viewModel.allRequest.value = FiltersRequest()
        super.onResume()
    }

    override fun afterView() {
        setupObservers()
        setSwipeToRefresh()
        observeLocalDb()
        setFilterIcon()
    }

    private fun setFilterIcon() {
        with((activity as Home).getToolbar().menu.findItem(R.id.edit_product)) {
            icon = ContextCompat.getDrawable(requireContext(), R.drawable.ic_filter)
            isVisible = true
            setOnMenuItemClickListener {
                if (networkInterceptor.isInternetEnabled()) {
                    val fragment = FilterBottomSheetFragment.newInstance(dateFormatterUtils, filtersCache, {viewModel.allRequest.value = it}, {viewModel.allRequest.value = FiltersRequest()})
                    fragment.show((activity as AppCompatActivity).supportFragmentManager, "filterDialog")
                } else {
                    Toast.makeText(requireContext(), R.string.turn_on_internet, Toast.LENGTH_LONG).show()
                }
                true
            }
        }

    }

    private fun observeLocalDb() {
        productdao.getAllLiveData().observe(this, {
            setAdapter(it.map { product -> ProductMapper.getProduct(product) }.toList())
        })
    }

    private fun setSwipeToRefresh() {
        swipeToRefresh.setColorSchemeColors(requireContext().getColor(R.color.colorPrimary))
        swipeToRefresh.setProgressBackgroundColorSchemeColor(requireContext().getColor(R.color.black))
        swipeToRefresh.setOnRefreshListener {
            viewModel.allRequest.value = FiltersRequest()
            swipeToRefresh.isRefreshing = false
        }
    }

    private fun setupObservers() {
        responseHandler.observe(
            viewLifecycleOwner,
            viewModel.allResponse,
            object : Callback<List<Product>> {
                override fun onLoaded(data: List<Product>) {
                    setAdapter(data)
                }
            })

        responseHandler.observe(
            this,
            viewModel.deleteResponse,
            object : Callback<Unit> {
                override fun onLoaded(data: Unit) {
                    viewModel.allRequest.value = FiltersRequest()
                }
            })

        responseHandler.observe(this, viewModel.editResponse, object : Callback<Product> {
            override fun onLoaded(data: Product) {
                viewModel.allRequest.value = FiltersRequest()
            }
        })
    }

    private fun setAdapter(data: List<Product>) {
        if (data.filter { it.metadata?.isSynchronized == true}.isEmpty()) {
            emptyList.visibility = VISIBLE
            recycler.visibility = GONE
        } else {
            recycler.visibility = VISIBLE
            emptyList.visibility = GONE
            val list = data.filter { it.metadata?.toBeDeleted == false }
                .groupBy { product -> product.productCard?.barcode }.toList()
            recycler.adapter = ProductListRecycler(
                list,
                tokenUtil,
                {
                    startForResult.launch(
                        ProductDetailsActivity.prepareIntentWithUIID(
                            requireContext(),
                            it.uuid.toString()
                        )
                    )
                }, { showEditQuantityDialog(it) }, { showDeleteDialog(it) })
            recycler.layoutManager = GridLayoutManager(requireContext(), 1)
        }
    }

    private fun showEditQuantityDialog(product: Product) {
        ProductQuantityChanger(requireContext(), product) {
            viewModel.editRequest.value = product.setQuantity(it)
        }.show()
    }

    private fun showDeleteDialog(product: Product) {
        activity?.displayQuestionDialog(
            getString(R.string.delete_question_title),
            positiveListener = { _, _ ->
                viewModel.deleteRequest.value = product
            })
    }
}