package com.raddyr.products.ui.list

import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.google.android.material.tabs.TabLayoutMediator
import com.raddyr.core.util.tokenUtil.TokenUtil
import com.raddyr.products.R
import com.raddyr.products.data.model.Product
import kotlinx.android.synthetic.main.product_list_item.view.*

class ProductViewPagerAdapter(
    val list: List<Product>,
    val detailsListener: (product: Product) -> Unit,
    val editListener: (product: Product) -> Unit,
    val deleteListener: (product: Product) -> Unit,
    val viewPager: ViewPager2,
    val tokenUtil: TokenUtil
) :
    RecyclerView.Adapter<ProductViewPagerAdapter.ProductViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ProductViewHolder(
        LayoutInflater.from(parent.context).inflate(
            R.layout.product_list_item,
            parent,
            false
        ).apply {
            if (list.size == 1) tabLayout.visibility = GONE
        }
    )

    override fun getItemCount() = list.size

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        holder.bind(list[position])
    }

    inner class ProductViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bind(product: Product) {
            with(itemView) {
                TabLayoutMediator(tabLayout, viewPager) { _, _ -> }.attach()
                if (product.productCard?.photoUrl != null) {
                    val glideUrl =
                        GlideUrl(product.productCard?.photoUrl) {
                            mapOf(
                                Pair(
                                    "Authorization",
                                    tokenUtil.getToken()
                                )
                            )
                        }
                    Glide
                        .with(this)
                        .load(glideUrl)
                        .addListener(object : RequestListener<Drawable> {
                            override fun onLoadFailed(
                                e: GlideException?,
                                model: Any?,
                                target: Target<Drawable>?,
                                isFirstResource: Boolean
                            ): Boolean {
                                progressBar.visibility = GONE
                                return false
                            }

                            override fun onResourceReady(
                                resource: Drawable?,
                                model: Any?,
                                target: Target<Drawable>?,
                                dataSource: DataSource?,
                                isFirstResource: Boolean
                            ): Boolean {
                                progressBar.visibility = GONE
                                return false
                            }
                        })
                        .fitCenter()
                        .circleCrop()
                        .error(R.drawable.placeholder)
                        .into(image)

                } else {
                    image.setImageResource(R.drawable.placeholder)
                }
                if (product.metadata?.shared != null && product.metadata?.shared == true) {
                    container.strokeColor = context.getColor(R.color.colorPrimary)
                    container.strokeWidth = 2
                }
                name.text = product.productCard?.name
                brand.text = product.productCard?.brand
                executionDate.text = String.format(
                    context.getString(R.string.execution_date),
                    product.metadata?.expiryDate
                )
                if (brand.text.isNullOrEmpty()) brand.visibility = GONE
                product.quantity?.let { quantity ->
                    product.productCard?.totalQuantity?.let { totalQuantity ->
                        if (quantity != 0f && totalQuantity != 0f) {
                            remainingValue.visibility = View.VISIBLE
                            remainingValue.text = String.format(
                                context.getString(R.string.remaining_value),
                                (quantity / totalQuantity * 100).toInt()
                            )
                        }
                    }
                }
                deleteButton.setOnClickListener { deleteListener.invoke(product) }
                editButton.setOnClickListener { editListener.invoke(product) }

                setOnClickListener {
                    detailsListener.invoke(product)
                }
            }
        }
    }
}
