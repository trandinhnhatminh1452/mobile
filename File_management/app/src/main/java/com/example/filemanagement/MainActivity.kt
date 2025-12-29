package com.example.filemanagement

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.Settings
import android.text.InputType
import android.view.Menu
import android.view.MenuItem
import android.webkit.MimeTypeMap
import android.widget.EditText
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.filemanagement.databinding.ActivityMainBinding
import java.io.File
import java.io.IOException

class MainActivity : AppCompatActivity(), FileAdapter.OnFileClickListener {

    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: FileAdapter
    private lateinit var currentPath: String
    private var copiedFile: File? = null

    private val requestPermissionLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { _ ->
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            if (Environment.isExternalStorageManager()) {
                initFileManager()
            } else {
                Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeButtonEnabled(true)

        binding.rvFiles.layoutManager = LinearLayoutManager(this)

        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                val rootPath = Environment.getExternalStorageDirectory().path
                val parentFile = File(currentPath).parentFile
                if (currentPath != rootPath && parentFile != null) {
                    currentPath = parentFile.absolutePath
                    displayFiles(currentPath)
                } else {
                    isEnabled = false
                    onBackPressedDispatcher.onBackPressed()
                }
            }
        })

        if (checkPermission()) {
            initFileManager()
        } else {
            requestPermission()
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return true
    }

    private fun checkPermission(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            Environment.isExternalStorageManager()
        } else {
            val result = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
            result == PackageManager.PERMISSION_GRANTED
        }
    }

    private fun requestPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            try {
                val intent = Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION)
                intent.addCategory("android.intent.category.DEFAULT")
                intent.data = Uri.parse(String.format("package:%s", applicationContext.packageName))
                requestPermissionLauncher.launch(intent)
            } catch (e: Exception) {
                val intent = Intent()
                intent.action = Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION
                requestPermissionLauncher.launch(intent)
            }
        } else {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), 1)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 1) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                initFileManager()
            } else {
                Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun initFileManager() {
        val sdcard = Environment.getExternalStorageDirectory().path
        currentPath = sdcard
        displayFiles(currentPath)
    }

    private fun displayFiles(path: String) {
        supportActionBar?.subtitle = path
        val files = File(path).listFiles()
        if (files != null) {
            val fileList = files.toList().sortedWith(compareBy({ !it.isDirectory }, { it.name.lowercase() }))
            adapter = FileAdapter(this, fileList)
            adapter.listener = this
            binding.rvFiles.adapter = adapter
        } else {
            Toast.makeText(this, "Cannot access path", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onFileClick(file: File) {
        if (file.isDirectory) {
            currentPath = file.absolutePath
            displayFiles(currentPath)
        } else {
            openFileWithExternalApp(file)
        }
    }

    private fun openFileWithExternalApp(file: File) {
        val uri = FileProvider.getUriForFile(this, "${applicationContext.packageName}.provider", file)
        val intent = Intent(Intent.ACTION_VIEW)
        val mimeType = getMimeType(file.absolutePath)
        intent.setDataAndType(uri, mimeType)
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        try {
            startActivity(intent)
        } catch (e: Exception) {
            Toast.makeText(this, "No app to open this file", Toast.LENGTH_SHORT).show()
        }
    }

    private fun getMimeType(url: String): String? {
        var type: String? = null
        val extension = MimeTypeMap.getFileExtensionFromUrl(url)
        if (extension != null) {
            type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension)
        }
        return type
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.options_menu, menu)
        return true
    }

    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        val pasteItem = menu?.findItem(R.id.menu_paste)
        pasteItem?.isVisible = copiedFile != null
        return super.onPrepareOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_new_folder -> createNewFolder()
            R.id.menu_new_file -> createNewFile()
            R.id.menu_paste -> pasteFile()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun createNewFolder() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("New Folder")
        val input = EditText(this)
        input.inputType = InputType.TYPE_CLASS_TEXT
        builder.setView(input)
        builder.setPositiveButton("Create") { _, _ ->
            val folderName = input.text.toString()
            val newFolder = File(currentPath, folderName)
            if (!newFolder.exists()) {
                newFolder.mkdir()
                displayFiles(currentPath)
            } else {
                Toast.makeText(this, "Folder already exists", Toast.LENGTH_SHORT).show()
            }
        }
        builder.setNegativeButton("Cancel", null)
        builder.show()
    }

    private fun createNewFile() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("New File")
        val input = EditText(this)
        input.inputType = InputType.TYPE_CLASS_TEXT
        builder.setView(input)
        builder.setPositiveButton("Create") { _, _ ->
            var fileName = input.text.toString()
            if (!fileName.endsWith(".txt", ignoreCase = true)) {
                fileName += ".txt"
            }
            val newFile = File(currentPath, fileName)
            if (!newFile.exists()) {
                try {
                    newFile.createNewFile()
                    displayFiles(currentPath)
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            } else {
                Toast.makeText(this, "File already exists", Toast.LENGTH_SHORT).show()
            }
        }
        builder.setNegativeButton("Cancel", null)
        builder.show()
    }

    private fun pasteFile() {
        copiedFile?.let {
            val destFile = File(currentPath, it.name)
            try {
                it.copyTo(destFile, overwrite = true)
                displayFiles(currentPath)
                Toast.makeText(this, "File pasted", Toast.LENGTH_SHORT).show()
            } catch (e: IOException) {
                e.printStackTrace()
                Toast.makeText(this, "Paste failed: ${e.message}", Toast.LENGTH_LONG).show()
            }
            copiedFile = null
            invalidateOptionsMenu()
        }
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        val file = adapter.files[item.groupId]
        when (item.itemId) {
            0 -> renameFile(file)
            1 -> deleteFile(file)
            2 -> copyFile(file)
        }
        return super.onContextItemSelected(item)
    }

    private fun renameFile(file: File) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Rename")
        val input = EditText(this)
        input.setText(file.name)
        builder.setView(input)
        builder.setPositiveButton("Rename") { _, _ ->
            val newName = input.text.toString()
            val newFile = File(file.parent, newName)
            if (file.renameTo(newFile)) {
                displayFiles(currentPath)
            } else {
                Toast.makeText(this, "Rename failed", Toast.LENGTH_SHORT).show()
            }
        }
        builder.setNegativeButton("Cancel", null)
        builder.show()
    }

    private fun deleteFile(file: File) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Delete")
        builder.setMessage("Are you sure you want to delete ${file.name}?")
        builder.setPositiveButton("Delete") { _, _ ->
            if (file.deleteRecursively()) {
                displayFiles(currentPath)
            } else {
                Toast.makeText(this, "Delete failed", Toast.LENGTH_SHORT).show()
            }
        }
        builder.setNegativeButton("Cancel", null)
        builder.show()
    }

    private fun copyFile(file: File) {
        copiedFile = file
        invalidateOptionsMenu() // This will call onPrepareOptionsMenu to show/hide the paste button
        Toast.makeText(this, "Copied ${file.name}", Toast.LENGTH_SHORT).show()
    }
}
