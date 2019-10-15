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


    fun clear() {
        items.clear()
        notifyDataSetChanged()
    }

    fun setItems(newItems: List<T>) {
        items.clear()
        items += newItems
        notifyDataSetChanged()
    }

    operator fun get(position: Int): T {
        return items[position]
    }

    operator fun set(position: Int, newItem: T) {
        items[position] = newItem
        notifyItemChanged(position)
    }

    fun addItem(newItem: T): Int {
        items += newItem
        notifyItemInserted(items.lastIndex)
        return items.lastIndex
    }

    fun addItem(position: Int, newItem: T) {
        items.add(position, newItem)
        notifyItemInserted(position)
    }

    fun changeItem(position: Int, newItem: T) {
        items[position] = newItem
        notifyItemChanged(position)
    }

    operator fun plusAssign(newItems: List<T>) {
        newItems.forEach { plusAssign(it) }
    }

    operator fun plusAssign(newItem: T) {
        items += newItem
        notifyItemInserted(items.lastIndex)
        if (items.lastIndex != 0) {
            notifyItemChanged(items.lastIndex - 1)
        }
    }

    operator fun minusAssign(itemToRemove: T) {
        val indexOf = items.indexOf(itemToRemove)
        removeAt(indexOf)
    }

    infix fun removeAt(position: Int) {
        items.removeAt(position)
        notifyItemRemoved(position)
        if (items.lastIndex != 0) {
            notifyItemChanged(position)
        }
    }


    operator fun plusAssign(hd: HolderDefinition<T>) {
        addHolder(hd.viewType, hd.predicate, hd.generator)
    }

    fun addHolder(vt: Int, hp: HolderPredicate<T>, hg: HolderGenerator<T>): BaseRecyclerAdapter<T> {
        viewTypes[hp] = vt
        holderGenerators[vt] = hg
        return this
    }

    fun addHolder(hd: HolderDefinition<T>): BaseRecyclerAdapter<T> {
        viewTypes[hd.predicate] = hd.viewType
        holderGenerators[hd.viewType] = hd.generator
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
        this.viewType = 0
        this.predicate = { true }
        this.generator = generator
    }
}


fun ViewGroup.inflate(@LayoutRes l: Int): View = LayoutInflater.from(context).inflate(l, this, false)
