package com.sagrishin.ptsadm.common.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder

typealias HolderPredicate<T> = (item: T) -> Boolean
typealias HolderGenerator<T> = (parent: ViewGroup) -> BaseHolder<T>

typealias ClickListener<T> = (item: T) -> Unit

open class BaseRecyclerAdapter<T>(
    val items: MutableList<T> = mutableListOf(),
    private val clickListener: ClickListener<T>? = null
) : RecyclerView.Adapter<BaseHolder<T>>() {

    private val viewTypes: MutableMap<HolderPredicate<T>, Int> = mutableMapOf()
    private val holderGenerators: MutableMap<Int, HolderGenerator<T>> = mutableMapOf()

    override fun getItemViewType(position: Int): Int {
        return viewTypes.filterKeys { it(items[position]) }.values.first()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseHolder<T> {
        return holderGenerators[viewType]!!(parent)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: BaseHolder<T>, position: Int) {
        clickListener?.let {
            holder.bind(items[position], clickListener)
        } ?: let {
            holder.bind(items[position])
        }
    }


    fun setItems(newItems: List<T>) {
        items.clear()
        items += newItems
        notifyDataSetChanged()
    }

    operator fun plusAssign(newItems: List<T>) {
        newItems.forEach { plusAssign(it) }
    }

    operator fun plusAssign(newItem: T) {
        items += newItem
        notifyItemInserted(items.lastIndex)
    }

    operator fun minusAssign(itemToRemove: T) {
        items.forEachIndexed { i, item ->
            if (item == itemToRemove) {
                removeAt(i)
                return@forEachIndexed
            }
        }
    }

    infix fun removeAt(position: Int) {
        items.removeAt(position)
        notifyItemRemoved(position)
    }


    operator fun plusAssign(hd: HolderDefinition<T>) {
        addHolder(hd.viewType, hd.predicate, hd.generator)
    }

    fun addHolder(viewType: Int, predicate: HolderPredicate<T>, generator: HolderGenerator<T>): BaseRecyclerAdapter<T> {
        viewTypes[predicate] = viewType
        holderGenerators[viewType] = generator
        return this
    }

}


abstract class BaseHolder<T>(v: View) : ViewHolder(v) {

    open fun bind(item: T) {

    }

    open fun bind(item: T, cl: ClickListener<T>) {
        bind(item)
    }

}


class HolderDefinition<T> {

    var viewType: Int = -1
    lateinit var predicate: HolderPredicate<T>
    lateinit var generator: HolderGenerator<T>

}


fun <T> holder(block: HolderDefinition<T>.() -> Unit): HolderDefinition<T> {
    return HolderDefinition<T>().apply(block)
}


fun <T> holder1(generator: HolderGenerator<T>): HolderDefinition<T> {
    return holder {
        this.viewType = -1
        this.predicate = { true }
        this.generator = generator
    }
}


fun ViewGroup.inflate(@LayoutRes l: Int): View = LayoutInflater.from(context).inflate(l, this, false)
