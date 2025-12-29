package com.example.studentmanager

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.studentmanager.databinding.FragmentAddStudentBinding

class AddStudentFragment : Fragment() {

    private val viewModel: StudentViewModel by activityViewModels()
    private lateinit var binding: FragmentAddStudentBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_add_student, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonSave.setOnClickListener {
            val mssv = binding.editTextMSSV.text.toString().trim()
            val hoTen = binding.editTextName.text.toString().trim()
            val soDienThoai = binding.editTextPhone.text.toString().trim()
            val diaChi = binding.editTextAddress.text.toString().trim()

            // Kiểm tra dữ liệu đầu vào
            if (mssv.isEmpty() || hoTen.isEmpty() || soDienThoai.isEmpty() || diaChi.isEmpty()) {
                Toast.makeText(requireContext(), "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Kiểm tra MSSV đã tồn tại chưa
            if (viewModel.isStudentExists(mssv)) {
                Toast.makeText(requireContext(), "MSSV đã tồn tại", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val student = Student(mssv, hoTen, soDienThoai, diaChi)
            if (viewModel.addStudent(student)) {
                Toast.makeText(requireContext(), "Thêm sinh viên thành công", Toast.LENGTH_SHORT).show()
                findNavController().navigateUp()
            } else {
                Toast.makeText(requireContext(), "Thêm sinh viên thất bại", Toast.LENGTH_SHORT).show()
            }
        }
    }
}