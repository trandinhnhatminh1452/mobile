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
import com.example.studentmanager.databinding.FragmentUpdateStudentBinding

class UpdateStudentFragment : Fragment() {

    private val viewModel: StudentViewModel by activityViewModels()
    private lateinit var binding: FragmentUpdateStudentBinding
    private var originalMssv: String = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_update_student, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val args = requireArguments()
        val student = args.getParcelable<Student>("student")
        val position = args.getInt("position", -1)

        if (student == null || position == -1) {
            Toast.makeText(requireContext(), "Dữ liệu sinh viên không hợp lệ", Toast.LENGTH_SHORT).show()
            findNavController().navigateUp()
            return
        }

        originalMssv = student.mssv

        binding.editTextMSSV.setText(student.mssv)
        binding.editTextName.setText(student.hoTen)
        binding.editTextPhone.setText(student.soDienThoai)
        binding.editTextAddress.setText(student.diaChi)

        binding.buttonUpdate.setOnClickListener {
            val mssv = binding.editTextMSSV.text.toString().trim()
            val hoTen = binding.editTextName.text.toString().trim()
            val soDienThoai = binding.editTextPhone.text.toString().trim()
            val diaChi = binding.editTextAddress.text.toString().trim()

            // Kiểm tra dữ liệu đầu vào
            if (mssv.isEmpty() || hoTen.isEmpty() || soDienThoai.isEmpty() || diaChi.isEmpty()) {
                Toast.makeText(requireContext(), "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Kiểm tra nếu MSSV thay đổi và MSSV mới đã tồn tại
            if (mssv != originalMssv && viewModel.isStudentExists(mssv)) {
                Toast.makeText(requireContext(), "MSSV đã tồn tại", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val updatedStudent = Student(mssv, hoTen, soDienThoai, diaChi)
            if (viewModel.updateStudent(position, updatedStudent)) {
                Toast.makeText(requireContext(), "Cập nhật sinh viên thành công", Toast.LENGTH_SHORT).show()
                findNavController().navigateUp()
            } else {
                Toast.makeText(requireContext(), "Cập nhật sinh viên thất bại", Toast.LENGTH_SHORT).show()
            }
        }
    }
}