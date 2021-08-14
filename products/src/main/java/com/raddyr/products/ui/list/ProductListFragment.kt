package com.raddyr.products.ui.list

import android.content.DialogInterface
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.raddyr.core.base.BaseFragment
import com.raddyr.core.util.extensions.displayQuestionDialog
import com.raddyr.core.util.responseHandler.Callback
import com.raddyr.core.util.responseHandler.ResponseHandler
import com.raddyr.core.util.tokenUtil.TokenUtil
import com.raddyr.products.R
import com.raddyr.products.data.db.dao.ProductDao
import com.raddyr.products.data.model.Product
import com.raddyr.products.data.model.ProductMapper
import com.raddyr.products.ui.customViews.ProductQuantityChanger
import com.raddyr.products.ui.details.ProductDetailsActivity
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
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


    private val viewModel by viewModels<ProductListViewModel> { viewModelFactory }
    private val startForResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {}

    override fun onResume() {
        emptyList.visibility = GONE
        viewModel.allRequest.value = true
        super.onResume()
    }

    override fun afterView() {
        setupObservers()
        setSwipeToRefresh()
        observeLocalDb()
    }

    private fun observeLocalDb() {
        productdao.getAll()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io()).subscribe({
                setAdapter(
                    it.map { product -> ProductMapper.getProduct(product) }.toList()
                )
            }, {})
    }

    private fun setSwipeToRefresh() {
        swipeToRefresh.setColorSchemeColors(requireContext().getColor(R.color.colorPrimary))
        swipeToRefresh.setProgressBackgroundColorSchemeColor(requireContext().getColor(R.color.black))
        swipeToRefresh.setOnRefreshListener {
            viewModel.allRequest.value = true
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
                    viewModel.allRequest.value = true
                }
            })

        responseHandler.observe(this, viewModel.editResponse, object : Callback<Product> {
            override fun onLoaded(data: Product) {
                viewModel.allRequest.value = true
            }
        })
    }

    private fun setAdapter(data: List<Product>) {
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
        if (list.isEmpty()) emptyList.visibility = VISIBLE else GONE
    }

    private fun showEditQuantityDialog(product: Product) {
        ProductQuantityChanger(requireContext(), product) {
            viewModel.editRequest.value = product.setQuantity(it)
        }.show()
    }

    private fun showDeleteDialog(product: Product) {
        activity?.displayQuestionDialog(
            getString(R.string.delete_question_title),
            positiveListener = DialogInterface.OnClickListener { _, _ ->
                viewModel.deleteRequest.value = product
            })
    }
}