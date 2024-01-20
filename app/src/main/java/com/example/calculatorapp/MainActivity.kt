package com.example.calculatorapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import androidx.lifecycle.ReportFragment.Companion.reportFragment
import com.example.calculatorapp.databinding.ActivityMainBinding
import net.objecthunter.exp4j.Expression
import net.objecthunter.exp4j.ExpressionBuilder
import java.lang.ArithmeticException

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var lastNumeric = false
    private var stateError = false
    private var lastDot = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
    // onDigitClick method
    fun onDigitClick(view: View) {

        if (stateError) {
            binding.calculationTv.text = (view as Button).text
            stateError = false
        } else {
            binding.calculationTv.append((view as Button).text)
        }
        lastNumeric = true
        onEqual()
    }

    fun onClearClick(view: View) {

        binding.calculationTv.text = ""
        lastNumeric = false

    }

    // On back Click code here
    fun onBackClick(view: View) {

        binding.calculationTv.text = binding.calculationTv.text.toString().dropLast(1)
        try {
            val lastChar = binding.calculationTv.text.toString().last()
            if (lastChar.isDigit()) {
                onEqual()
            }
        } catch (e: Exception) {
            binding.outputTv.text = ""
            binding.outputTv.visibility = View.GONE
            Log.e("last char error", e.toString())
        }

    }

    fun onOperatorClick(view: View) {

        if (!stateError && lastNumeric) {
            binding.calculationTv.append((view as Button).text)
            lastDot = false
            lastNumeric = false
            onEqual()
        }

    }

    fun onEqualClick(view: View) {

        onEqual()
        binding.calculationTv.text = binding.outputTv.text.toString().drop(1)

    }

    fun onAllClearClick(view: View) {

        binding.calculationTv.text = ""
        binding.outputTv.text = ""
        stateError = false
        lastDot = false
        lastNumeric = false
        binding.outputTv.visibility = View.GONE
    }

    private fun onEqual() {
        if (lastNumeric && !stateError) {
            val txt = binding.calculationTv.text.toString()

            val expression = ExpressionBuilder(txt).build()

            try {
                val result = expression.evaluate()
                binding.outputTv.visibility = View.VISIBLE
                binding.outputTv.text = "= " + result.toString()
            } catch (ex: ArithmeticException) {
                Log.e("evaluate error", ex.toString())
                binding.outputTv.text = "Error"
                stateError = true
                lastNumeric = false
            }
        }
    }

}