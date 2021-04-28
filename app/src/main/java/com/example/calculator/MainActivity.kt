package com.example.calculator

import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.calculator.databinding.ActivityMainBinding
import java.lang.NumberFormatException

class MainActivity : AppCompatActivity(), View.OnClickListener {

    private val operandListId = listOf(R.id.buttonPlus, R.id.buttonSubtract, R.id.buttonMulti, R.id.buttonDivision)
    private val numberListId = listOf(R.id.button0, R.id.button1, R.id.button2, R.id.button3, R.id.button4,
        R.id.button5, R.id.button6, R.id.button7, R.id.button8, R.id.button9)
    private val operandListDefault by lazy {
        listOf(getString(R.string.text_plus), getString(R.string.text_multiple),
            getString(R.string.text_subtract), getString(R.string.text_division))
    }
    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    private var operandStr = StringBuilder("")
    private var operand = ""
    private var number1 = 0.0
    private var number2 = 0.0
    private var isNewOperand = false
    private var result = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        initView()
    }

    private fun initView() {
        listOf(binding.button0, binding.button1, binding.button2, binding.button3, binding.button4,
            binding.button5, binding.button6, binding.button7, binding.button8, binding.button9,
            binding.buttonClear, binding.buttonDot, binding.buttonEqual, binding.buttonModulus,
            binding.buttonNegative, binding.buttonDel, binding.buttonMulti, binding.buttonPlus,
            binding.buttonSubtract, binding.buttonDivision)
            .forEach { it.setOnClickListener(this) }
    }

    override fun onClick(p0: View?) {
        val btClick = if (p0 is Button) p0 else return

        when (btClick.id) {
            in numberListId -> {
                if (isNewOperand &&
                    operandStr.toString() !in listOf(getString(R.string.text_plus), getString(R.string.text_subtract))) {
                    operandStr.clear()
                }
                isNewOperand = false
                showText(btClick)
            }

            in operandListId -> {
                try {
                    if (TextUtils.isEmpty(operand)) inputOperandOfCalculation(btClick) else disableClickOperand(btClick)

                } catch (e: Exception) {
                    if (btClick.text.toString() in listOf(getString(R.string.text_plus), getString(R.string.text_subtract))) {
                        disableClickOperand(btClick)
                    } else {
                        throw NumberFormatException()
                    }
                }
            }

            R.id.buttonDot -> {
                if (!isNewOperand) {
                    operandStr.append(binding.buttonDot.text)
                    binding.textResult.text = operandStr.toString()
                }
            }

            R.id.buttonClear -> {
                operandStr.clear()
                operandStr.append(if (result % 1 == 0.toDouble()) result.toInt().toString() else String.format(getString(R.string.text_result_format), result))
                binding.textResult.text = operandStr.toString()
            }

            R.id.buttonModulus -> {
                val mudulusNumber: Double
                try {
                    if (!TextUtils.isEmpty(binding.textResult.text)) {
                        mudulusNumber = binding.textResult.text.toString().toDouble() / 100
                        binding.textResult.text = String.format(getString(R.string.text_result_format), mudulusNumber)
                        operandStr.clear()
                        operandStr.append(binding.textResult.text)
                        return
                    }
                } catch (e: Exception) {
                    throw NumberFormatException()
                }
            }

            R.id.buttonNegative -> {
                if (!TextUtils.isEmpty(binding.textResult.text)) {
                    binding.textResult.text = (binding.textResult.text.toString().toDouble() * -1).toString()
                    operandStr.clear()
                    operandStr.append(binding.textResult.text)
                }
            }

            R.id.buttonEqual -> resolveOperand()

            R.id.buttonDel -> {
                isNewOperand = false
                operandStr.clear()
                binding.textResult.text = ""
                number1 = 0.0
                number2 = 0.0
            }
        }
    }

    private fun showText(btClick: Button) {
        operandStr.append(btClick.text)
        binding.textResult.text = operandStr.toString()
    }

    private fun inputOperandOfCalculation(btClick: Button) {
        number1 = operandStr.toString().toDouble()
        operandStr.clear()
        operand = btClick.text.toString()
    }

    private fun disableClickOperand(btClick: Button) {
        if (!TextUtils.isEmpty(operandStr) &&
            operandStr.toString() in operandListDefault) {
            return
        }
        showText(btClick)
    }

    private fun resolveOperand() {
        number2 = operandStr.toString().toDouble()
        result = if (TextUtils.isEmpty(operand)) number2 else {

            when (operand) {
                getString(R.string.text_plus) -> number1 + number2
                getString(R.string.text_subtract) -> number1 - number2
                getString(R.string.text_multiple) -> number1 * number2
                getString(R.string.text_division) -> number1 / number2
                else -> return
            }
        }

        binding.textResult.text = if (result % 1 == 0.toDouble()) result.toInt().toString() else String.format(getString(R.string.text_result_format), result)
        operandStr.clear()
        operandStr.append(result.toString())
        isNewOperand = true
        operand = ""
    }
}
