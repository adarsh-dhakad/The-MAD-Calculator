package com.flytbase.calculator.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.flytbase.calculator.databinding.FragmentCalculatorBinding
import com.flytbase.calculator.databinding.ItemLayoutBinding
import com.flytbase.calculator.view.ui.frahments.CalculatorFragment


class InputOutputAdapter(private val activity: CalculatorFragment) :RecyclerView.Adapter<InputOutputAdapter.ViewHolder>(){
    private var list:List<String> = listOf()
    class ViewHolder(view:ItemLayoutBinding):RecyclerView.ViewHolder(view.root){
        val input = view.input
        val output = view.outPut
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemLayoutBinding.inflate(
            LayoutInflater.from(activity.context) , parent ,false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
       val item = list[position]
        holder.output.text = " = ${item.substring(item.indexOf("#")+1)}"
        holder.input.setText(item.substring(0,item.indexOf("#")))
    }

    override fun getItemCount(): Int {
        return list.size
    }

    fun itemList(list: List<String>){
        this.list = list
        notifyDataSetChanged()
    }
}