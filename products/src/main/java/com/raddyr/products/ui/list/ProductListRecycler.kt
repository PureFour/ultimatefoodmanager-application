package com.raddyr.products.ui.list

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.raddyr.core.util.tokenUtil.TokenUtil
import com.raddyr.products.R
import com.raddyr.products.data.model.Product
import kotlinx.android.synthetic.main.products_view_container.view.*

class ProductListRecycler(
    val list: List<Pair<String?, List<Product>>>,
    val tokenUtil: TokenUtil,
    val detailsListener: (product: Product) -> Unit,
    val editListener: (product: Product) -> Unit,
    val deleteListener: (product: Product) -> Unit
) :
    RecyclerView.Adapter<ProductListRecycler.ProductViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ProductViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.products_view_container, parent, false)
    )

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        holder.bind(list[position].second)
    }

    inner class ProductViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bind(products: List<Product>) {
            with(itemView) {
                viewPager.adapter = ProductViewPagerAdapter(
                    products,
                    detailsListener,
                    editListener,
                    deleteListener,
                    viewPager,
                    tokenUtil
                )
            }
        }
    }
}