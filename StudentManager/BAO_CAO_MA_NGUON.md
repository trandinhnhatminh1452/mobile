## 1. Nội dung các file mã nguồn chính

- **File `StudentViewModel.kt`**: Quản lý dữ liệu sinh viên với `LiveData`; lưu trữ danh sách sinh viên trong `MutableLiveData<MutableList<Student>>`; cung cấp các phương thức như `addStudent(student: Student)` để thêm sinh viên mới, `updateStudent(position: Int, student: Student)` để cập nhật thông tin sinh viên, `deleteStudent(position: Int)` để xóa sinh viên khỏi danh sách; mọi thay đổi sẽ tự động cập nhật tới các `Fragment` quan sát `LiveData`.

- **File `Student.kt`**: Định nghĩa lớp dữ liệu `Student` với các thuộc tính `mssv`, `hoTen`, `soDienThoai`, `diaChi`; sử dụng `@Parcelize` để triển khai `Parcelable`, cho phép truyền đối tượng sinh viên giữa các thành phần (Fragment, Navigation) một cách dễ dàng.

- **File `MainActivity.kt`**: Là Activity chính của ứng dụng; đóng vai trò host cho `NavHostFragment`; khởi tạo `NavHostFragment`, lấy `NavController` từ `nav_host_fragment` và cấu hình `AppBarConfiguration`; cài đặt `onSupportNavigateUp()` để hỗ trợ nút back trên `ActionBar` làm việc với Navigation Component.

- **File `StudentListFragment.kt`**: Fragment hiển thị danh sách sinh viên; lấy `StudentViewModel` dùng chung bằng `activityViewModels()`; quan sát `viewModel.students` và cập nhật `ListView` thông qua `StudentAdapter`; xử lý click trên từng item để điều hướng sang `UpdateStudentFragment` kèm theo `position` và `Student` được chọn; xử lý menu tùy chọn (OptionMenu) với nút “Add” để điều hướng sang `AddStudentFragment`.

- **File `AddStudentFragment.kt`**: Fragment thực hiện chức năng thêm sinh viên mới; sử dụng Data Binding (`FragmentAddStudentBinding`) để truy cập các view trong layout `fragment_add_student.xml`; nhận dữ liệu người dùng nhập từ các `EditText`, kiểm tra ràng buộc dữ liệu không được để trống; khi hợp lệ thì tạo đối tượng `Student`, gọi `viewModel.addStudent(student)` để thêm vào danh sách và dùng `findNavController().navigateUp()` để quay lại màn hình danh sách.

- **File `UpdateStudentFragment.kt`**: Fragment cập nhật thông tin sinh viên; sử dụng Data Binding (`FragmentUpdateStudentBinding`) với layout `fragment_update_student.xml`; nhận `Student` và `position` từ `arguments`; hiển thị sẵn thông tin sinh viên lên các `EditText`; khi người dùng chỉnh sửa và bấm nút cập nhật, Fragment kiểm tra dữ liệu, tạo `updatedStudent` và gọi `viewModel.updateStudent(position, updatedStudent)` để cập nhật danh sách, sau đó `navigateUp()` quay lại danh sách.

- **File layout `activity_main.xml`**: Định nghĩa giao diện cho `MainActivity`; chứa một `FragmentContainerView` với id `nav_host_fragment`, được cấu hình làm `NavHostFragment` mặc định (`app:defaultNavHost="true"`) và gắn với đồ thị điều hướng `@navigation/nav_graph`; đây là nơi các Fragment được hiển thị luân phiên.

- **File layout `fragment_student_list.xml`**: Định nghĩa giao diện cho `StudentListFragment`; sử dụng một `ListView` có id `listViewStudents` để hiển thị danh sách sinh viên; mỗi dòng trong danh sách sử dụng layout `item_student.xml`.

- **File layout `fragment_add_student.xml`**: File layout sử dụng Data Binding (bọc trong thẻ `<layout>`); chứa các ô nhập liệu `EditText` cho `MSSV`, `Họ tên`, `Số điện thoại`, `Địa chỉ` và một nút `Button` “Lưu”; được liên kết với `AddStudentFragment` thông qua `FragmentAddStudentBinding`.

- **File layout `fragment_update_student.xml`**: Tương tự như `fragment_add_student.xml` nhưng phục vụ cho chức năng cập nhật; cũng sử dụng Data Binding; hiển thị thông tin sinh viên hiện tại và cho phép chỉnh sửa, kèm nút `Button` “Cập nhật”.

- **File layout `item_student.xml`**: Định nghĩa giao diện cho từng dòng trong danh sách sinh viên; là một `LinearLayout` nằm ngang với một `TextView` (`textViewStudent`) hiển thị chuỗi kết hợp `mssv` và `hoTen`; sử dụng nền chọn `?android:attr/selectableItemBackground` để có hiệu ứng khi người dùng nhấn vào item.

- **File `nav_graph.xml`**: Đồ thị điều hướng (Navigation Graph) của ứng dụng; khai báo ba destination chính là `StudentListFragment` (màn hình danh sách, startDestination), `AddStudentFragment` (màn hình thêm), `UpdateStudentFragment` (màn hình cập nhật); định nghĩa các action điều hướng từ danh sách sang thêm và cập nhật; với `UpdateStudentFragment` khai báo các `argument` như `position` (kiểu `Int`) và `student` (kiểu `com.example.studentmanager.Student`) để truyền dữ liệu giữa các Fragment.

- **File `AndroidManifest.xml`**: Khai báo cấu hình ứng dụng Android; đăng ký `MainActivity` là Activity khởi động với `intent-filter` chứa `MAIN` và `LAUNCHER`; cấu hình icon, label, theme (`Theme.StudentManager`), các thuộc tính backup và data extraction; đây là nơi hệ điều hành biết ứng dụng bắt đầu từ `MainActivity` và cách hiển thị ứng dụng trên màn hình chính.
