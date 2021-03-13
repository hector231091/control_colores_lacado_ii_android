package com.hectormorales.colores_cabina_lacado_ii_android.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hectormorales.colores_cabina_lacado_ii_android.R
import com.hectormorales.colores_cabina_lacado_ii_android.databinding.HistoricItemBinding
import com.hectormorales.colores_cabina_lacado_ii_android.databinding.HistoricalViewBinding

class HistoricalView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : LinearLayout(context, attrs, defStyle) {

    private val adapter: HistoricalAdapter = HistoricalAdapter()

    init {
        layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
        orientation = VERTICAL
        val binding = HistoricalViewBinding.inflate(LayoutInflater.from(context), this)
        with(binding) {
            recyclerView.layoutManager = LinearLayoutManager(context)
            recyclerView.adapter = adapter
            recyclerView.addItemDecoration(SpaceItemDecoration(resources.getDimensionPixelSize(R.dimen.history_entry_vertical_space)))
        }
    }

    fun bind(model: HistoricalUiModel) {
        adapter.setItems(model.list)
    }
}

class HistoricalAdapter : RecyclerView.Adapter<HistoricalAdapter.ViewHolder>() {

    private val list = mutableListOf<HistoricalItem>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            HistoricItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount(): Int {
        return list.size
    }

    fun setItems(list: List<HistoricalItem>) {
        this.list.addAll(list)
        notifyDataSetChanged()
    }

    class ViewHolder(private val binding: HistoricItemBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: HistoricalItem) = with(item) {
            binding.textViewColourCode.text = colourCode
            binding.textViewChangeStartTime.text = changeStartTime
            binding.textViewColourStartTime.text = colourStartTime
            binding.textViewColourEndTime.text = colourEndTime
            binding.textViewHangersAmount.text = hangersAmount.toString()
            binding.textViewObservations.text = observations
        }
    }
}

data class HistoricalUiModel(
    val list: List<HistoricalItem>
)

// Clase "Record" en Python
data class HistoricalItem(
    val id: String,
    val colourCode: String,
    val changeStartTime: String,
    val colourStartTime: String,
    val colourEndTime: String,
    val hangersAmount: Int,
    val observations: String
)