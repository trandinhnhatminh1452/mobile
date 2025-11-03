package com.example.myapplication

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.ComponentActivity
import androidx.activity.enableEdgeToEdge


class MainActivity : ComponentActivity() {

    private lateinit var tvResult: TextView
    private lateinit var btnCe: Button
    private lateinit var btnC: Button
    private lateinit var btnBs: Button
    private lateinit var btnDiv: Button
    private lateinit var btn7: Button
    private lateinit var btn8: Button
    private lateinit var btn9: Button
    private lateinit var btnMul: Button
    private lateinit var btn4: Button
    private lateinit var btn5: Button
    private lateinit var btn6: Button
    private lateinit var btnSub: Button
    private lateinit var btn1: Button
    private lateinit var btn2: Button
    private lateinit var btn3: Button
    private lateinit var btnAdd: Button
    private lateinit var btnPlusMinus: Button
    private lateinit var btn0: Button
    private lateinit var btnDot: Button
    private lateinit var btnEq: Button

    // Lưu trữ trạng thái của máy tính
    private var display = "0"
    private var firstOperand: Long? = null
    private var operator: String? = null
    private var waitingForSecondOperand = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.cal)


        initializeViews()
        setupClickListeners()
    }

    private fun initializeViews() {
        tvResult = findViewById(R.id.tvResult)
        btnCe = findViewById(R.id.btn_ce)
        btnC = findViewById(R.id.btn_c)
        btnBs = findViewById(R.id.btn_bs)
        btnDiv = findViewById(R.id.btn_div)
        btn7 = findViewById(R.id.btn_7)
        btn8 = findViewById(R.id.btn_8)
        btn9 = findViewById(R.id.btn_9)
        btnMul = findViewById(R.id.btn_mul)
        btn4 = findViewById(R.id.btn_4)
        btn5 = findViewById(R.id.btn_5)
        btn6 = findViewById(R.id.btn_6)
        btnSub = findViewById(R.id.btn_sub)
        btn1 = findViewById(R.id.btn_1)
        btn2 = findViewById(R.id.btn_2)
        btn3 = findViewById(R.id.btn_3)
        btnAdd = findViewById(R.id.btn_add)
        btnPlusMinus = findViewById(R.id.btn_plusminus)
        btn0 = findViewById(R.id.btn_0)
        btnDot = findViewById(R.id.btn_dot)
        btnEq = findViewById(R.id.btn_eq)

        updateDisplay()
    }

    private fun setupClickListeners() {
        // Nút số
        btn0.setOnClickListener { onNumberClick(0) }
        btn1.setOnClickListener { onNumberClick(1) }
        btn2.setOnClickListener { onNumberClick(2) }
        btn3.setOnClickListener { onNumberClick(3) }
        btn4.setOnClickListener { onNumberClick(4) }
        btn5.setOnClickListener { onNumberClick(5) }
        btn6.setOnClickListener { onNumberClick(6) }
        btn7.setOnClickListener { onNumberClick(7) }
        btn8.setOnClickListener { onNumberClick(8) }
        btn9.setOnClickListener { onNumberClick(9) }

        btnAdd.setOnClickListener { onOperatorClick("+") }
        btnSub.setOnClickListener { onOperatorClick("-") }
        btnMul.setOnClickListener { onOperatorClick("x") }
        btnDiv.setOnClickListener { onOperatorClick("/") }

        btnEq.setOnClickListener { onEqualsClick() }
        btnC.setOnClickListener { onClear() }
        btnCe.setOnClickListener { onClearEntry() }
        btnBs.setOnClickListener { onBackspace() }

        // Các nút chưa implement
        btnPlusMinus.setOnClickListener { /* TODO: Implement plus/minus */ }
        btnDot.setOnClickListener { /* TODO: Implement decimal */ }
    }

    private fun onNumberClick(number: Int) {
        if (waitingForSecondOperand) {
            display = number.toString()
            waitingForSecondOperand = false
        } else {
            if (display == "0") {
                display = number.toString()
            } else {
                display += number.toString()
            }
        }
        updateDisplay()
    }

    private fun onOperatorClick(op: String) {
        val currentValue = display.toLongOrNull() ?: 0
        if (firstOperand == null) {
            firstOperand = currentValue
        } else if (!waitingForSecondOperand) {
            val result = calculate()
            display = result.toString()
            firstOperand = result
        }
        operator = op
        waitingForSecondOperand = true
        updateDisplay()
    }

    private fun onEqualsClick() {
        if (firstOperand != null && operator != null && !waitingForSecondOperand) {
            val result = calculate()
            display = result.toString()
            firstOperand = null
            operator = null
            waitingForSecondOperand = true
            updateDisplay()
        }
    }

    private fun onClear() {
        display = "0"
        firstOperand = null
        operator = null
        waitingForSecondOperand = false
        updateDisplay()
    }

    private fun onClearEntry() {
        display = "0"
        // Nếu đang chờ toán hạng thứ hai, CE sẽ không ảnh hưởng đến phép toán đã lưu
        if (!waitingForSecondOperand) {
            // Nếu đây là toán hạng đầu tiên, ta cũng có thể reset trạng thái
            if(operator == null) {
                firstOperand = null
            }
        }
        updateDisplay()
    }

    private fun onBackspace() {
        if (display.length > 1) {
            display = display.dropLast(1)
        } else {
            display = "0"
        }
        updateDisplay()
    }

    private fun calculate(): Long {
        val secondOperand = display.toLongOrNull() ?: 0
        val first = firstOperand ?: return secondOperand
        return when (operator) {
            "+" -> first + secondOperand
            "-" -> first - secondOperand
            "x" -> first * secondOperand
            "/" -> if (secondOperand != 0L) first / secondOperand else 0 // Tránh chia cho 0
            else -> secondOperand
        }
    }

    private fun updateDisplay() {
        tvResult.text = display
    }
}