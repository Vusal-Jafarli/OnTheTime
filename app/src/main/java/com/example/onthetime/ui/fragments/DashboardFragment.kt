package com.example.onthetime.ui.fragments

import android.app.Activity
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import android.graphics.Rect
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity.MODE_PRIVATE
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.example.onthetime.R
import com.example.onthetime.adapter.PhotoOptionsBottomSheet
import com.example.onthetime.databinding.FragmentDashboardBinding
import com.example.onthetime.model.BottomViewItem
import com.example.onthetime.model.Date
import com.example.onthetime.model.ToString
import com.example.onthetime.repository.EmployeeRepository
import com.example.onthetime.repository.EmployerRepository
import com.example.onthetime.ui.activities.MainActivity.Companion.REQUEST_CODE_PICK_IMAGE
import com.example.onthetime.ui.activities.MainActivity.Companion.REQUEST_CODE_TAKE_PHOTO
import com.example.onthetime.viewmodel.CalendarViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.type.TimeOfDay
import com.squareup.picasso.Picasso
import kotlinx.coroutines.launch
import java.io.File
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.UUID

class DashboardFragment : Fragment() {

    lateinit var binding: FragmentDashboardBinding
    lateinit var repository: EmployerRepository
    lateinit var employeeRepository: EmployeeRepository
    private val calendarViewModel: CalendarViewModel by activityViewModels()


    private lateinit var currentPhotoPath: String
    lateinit var photoOptionsBottomSheet: PhotoOptionsBottomSheet
    private var userStatus = ""
    private var collectionPath = "employers"


    var itemList = mutableListOf(
        (BottomViewItem("Choose from library", R.drawable.image_icon_bottom_view)),
        (BottomViewItem("Take photo", R.drawable.camera_icon_bottom_view)),
        (BottomViewItem("Remove current picture", R.drawable.delete_icon_bottom_view))
    )


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDashboardBinding.inflate(inflater, container, false)
        repository = EmployerRepository()
        employeeRepository = EmployeeRepository()

        var sharedPreferences =
            requireContext().getSharedPreferences("DashboardStatus", MODE_PRIVATE)
        val isDataLoaded = sharedPreferences.getBoolean("isDataLoaded", false)

        Toast.makeText(requireContext(), isDataLoaded.toString(), Toast.LENGTH_SHORT).show()


        photoOptionsBottomSheet =
            PhotoOptionsBottomSheet(itemList, object : PhotoOptionsBottomSheet.PhotoOptionListener {
                override fun onChooseFromLibrary() {
                    openGallery()
                    sharedPreferences.edit()
                        .putBoolean("isDataLoaded", true)
                        .apply()
                }

                override fun onTakePhoto() {
                    openCamera()
                }

                override fun onRemoveCurrentPicture() {
                    removePhoto()
                }
            })

        val currentUser = FirebaseAuth.getInstance().currentUser


        context.let {
            it
            if (!isDataLoaded) {
                repository.getUserStatus(currentUser!!.uid) { status ->
                    if (status == "employee") {
                        loadEmployeeProfilePhoto(currentUser.uid.toString(), binding.roundButton)
                        userStatus = "employee"
                        collectionPath = "employees"
                    } else if (status == "employer") {
                        loadEmployerProfilePhoto(currentUser.uid.toString(), binding.roundButton)
                        userStatus = "employer"
                        collectionPath = "employers"
                    }

                    var sharedPreferences =
                        it?.getSharedPreferences("user_data", MODE_PRIVATE)
                    sharedPreferences?.edit()
                        ?.putString("status", status)
                        ?.apply()

                    repository.getUserAnyField(
                        currentUser.uid,
                        collectionPath,
                        "firstName"
                    ) { name ->
                        if (name != null) {
                            binding.firstTextView.text = "Hello $name!"
                            sharedPreferences?.edit()
                                ?.putString("userName", name)
                                ?.putBoolean("isDataLoaded", true)
                                ?.apply()
                        }
                    }

                    repository.getUserAnyField(
                        currentUser.uid,
                        collectionPath,
                        "profilePhotoPath"
                    ) { profilePhotoPath ->
                        if (profilePhotoPath != null) {
                            sharedPreferences?.edit()
                                ?.putString("profilePhotoUrl", profilePhotoPath)
                                ?.putBoolean("isDataLoaded", true)
                                ?.apply()
                        }
                    }

                    repository.getUserAnyField(currentUser.uid, collectionPath, "email") { email ->
                        if (email != null) {
                            repository.getUserAnyField(
                                currentUser.uid,
                                collectionPath,
                                "password"
                            ) { password ->
                                if (password != null) {
                                    context.let {
                                        it
                                        var sharedPreferences =
                                            it?.getSharedPreferences("user_data", MODE_PRIVATE)
                                        sharedPreferences?.edit()
                                            ?.putString("email", email)
                                            ?.putString("password", password)
                                            ?.apply()
                                    }
                                }
                            }
                        }
                    }

                    binding.progressBarProfile.visibility = View.GONE
                }
            } else {
                val cachedName = sharedPreferences.getString("userName", "")
                val cachedPhotoUrl = sharedPreferences.getString("profilePhotoUrl", "")

                binding.firstTextView.text = "Hello $cachedName!"
                if (!cachedPhotoUrl.isNullOrEmpty()) {
                    Picasso.get().load(cachedPhotoUrl).into(binding.roundButton)
                }

                binding.progressBarProfile.visibility = View.GONE
            }
        }
        var currentUserEmail: String = ""
        var currentUserPassword: String = ""

        var todayDate = Date(
            calendarViewModel.thisYear.value!!,
            calendarViewModel.toMonth.value!!,
            calendarViewModel.today.value!!,
            calendarViewModel.weekDayInit.value!!,
            "",
            ""
        )
        var shiftDayText = "You don't have any shift today."
        if (currentUser?.uid != null) {

            context.let {

                employeeRepository.getShiftsByEmployee(currentUser.uid) { shifts ->
                    for (item in shifts) {
                        if (item.valuedDatesList != null) {

                            for (date in item.valuedDatesList) {

                                if (todayDate.day == date.day && todayDate.month == date.month && todayDate.year == date.year)
                                {
                                    shiftDayText = "Your next shift is\nToday  ${item.startTime?.ToString()} to ${item.endTime?.ToString()}"
                                    break
                                }
                            }

                        }
                    }
                    binding.todaysShiftButton.text = shiftDayText
                }



                repository.getUserAnyField(currentUser.uid, collectionPath, "email") { email ->
                    if (!email.isNullOrEmpty()) {
                        currentUserEmail = email

                        repository.getUserAnyField(
                            currentUser.uid,
                            collectionPath,
                            "password"
                        ) { password ->
                            if (!password.isNullOrEmpty()) {
                                currentUserPassword = password

                                saveToSharedPreferences(currentUserEmail, currentUserPassword)
                            }
                        }
                    }
                }
            }
        }

        binding.roundButton.setOnClickListener {

            photoOptionsBottomSheet.show(parentFragmentManager, "PhotoOptionsBottomSheet")

        }

//        loadEmployerProfilePhoto(currentUser?.uid.toString(), binding.roundButton)


        return binding.root
    }


    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, REQUEST_CODE_PICK_IMAGE)
    }


    private fun removePhoto() {
//        val defaultPhotoUrl = "https://firebasestorage.googleapis.com/v0/b/onthetime-53976.appspot.com/o/images%2Fnone_profile_photo.png?alt=media&token=8441b234-e554-4bf6-a1f4-5618c8b5f883"
        val defaultPhotoUrl =
            "https://firebasestorage.googleapis.com/v0/b/onthetime-53976.appspot.com/o/images%2Fblank-profile-picture-973460_960_720-ezgif.com-webp-to-jpg-converter.jpg?alt=media&token=253a6402-e39c-40e8-9385-30423dcc002e"
        val currentUser = FirebaseAuth.getInstance().currentUser!!.uid
        repository.updateProfilePhotoPath(currentUser, defaultPhotoUrl) {
            context.let {
                Toast.makeText(
                    it,
                    "Image has been changed succesfully.",
                    Toast.LENGTH_SHORT
                ).show()
                binding.roundButton.setImageResource(R.drawable.none_profile_photo)
            }
        }


    }

    private fun openCamera() {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (takePictureIntent.resolveActivity(requireActivity().packageManager) != null) {
            val photoFile: File? = createImageFile()
            context?.let { context ->
                photoFile?.let {
                    val photoURI: Uri = FileProvider.getUriForFile(
                        context,
                        "${context.packageName}.fileprovider",
                        it
                    )
                    currentPhotoPath = it.absolutePath
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                    startActivityForResult(takePictureIntent, REQUEST_CODE_TAKE_PHOTO)
                }
            }
        }
    }

    private fun createImageFile(): File {
        val timeStamp: String =
            SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        context.let {
            val storageDir: File =
                requireContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES)!!
            return File.createTempFile("JPEG_${timeStamp}_", ".jpg", storageDir)
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                REQUEST_CODE_PICK_IMAGE -> {
                    val selectedImageUri: Uri? = data?.data
                    selectedImageUri?.let {
                        uploadToFirebase(it)
                        context.let { ctx ->
                            val inputStream = ctx?.contentResolver?.openInputStream(it)
                            val bitmap = BitmapFactory.decodeStream(inputStream)
                            binding.roundButton.setImageBitmap(bitmap)


                            var sharedPreferences =
                                requireContext().getSharedPreferences(
                                    "DashboardStatus",
                                    MODE_PRIVATE
                                )
                            val isDataLoaded = sharedPreferences.getBoolean("isDataLoaded", false)
                            sharedPreferences.edit()
                                .putBoolean("isDataLoaded", false)
                                .apply()
                            binding.roundButton.setImageBitmap(bitmap)
                            photoOptionsBottomSheet.binding.imageviewBottom.setImageBitmap(bitmap)

                        }
                    }
                }

                REQUEST_CODE_TAKE_PHOTO -> {
                    val photoUri: Uri = Uri.fromFile(File(currentPhotoPath))
                    uploadToFirebase(photoUri)

                }
            }
        }
    }

    private fun uploadToFirebase(imageUri: Uri) {
        val storage = FirebaseStorage.getInstance()
        val storageRef = storage.reference
        val imageRef = storageRef.child("images/${UUID.randomUUID()}.jpg")

        val uploadTask = imageRef.putFile(imageUri)

        uploadTask.addOnSuccessListener {
            imageRef.downloadUrl.addOnSuccessListener { uri ->
                val currentUser = FirebaseAuth.getInstance().currentUser
                repository.updateProfilePhotoPath(currentUser!!.uid, uri.toString()) {
//                    Toast.makeText(requireContext(), "Image uploaded: $uri", Toast.LENGTH_SHORT)
//                        .show()
                }
            }.addOnFailureListener {
                println("Error uploading image: ${it.message}")
            }
        }

    }

    fun loadImageFromFirebaseStorage(profilePhotoPath: String, imageView: ImageView) {
        if (profilePhotoPath != null && profilePhotoPath != "") {
            Picasso.get()
                .load(profilePhotoPath)
                .into(imageView)
        } else {
            binding.roundButton.setImageResource(R.drawable.none_profile_photo)
        }
    }

    fun loadEmployerProfilePhoto(employerId: String, imageView: ImageView) {

        context.let {
            if (view != null) {
                viewLifecycleOwner.lifecycleScope.launch {
                    repository.getEmployerPhotoPath(employerId) { profilePhotoPath ->
                        if (profilePhotoPath != null && profilePhotoPath != "") {
                            loadImageFromFirebaseStorage(profilePhotoPath, imageView)
                        } else {
                            context.let {
                                Toast.makeText(
                                    it,
                                    "Profile photo not found for employer.",
                                    Toast.LENGTH_SHORT
                                ).show()
                                binding.roundButton.setImageResource(R.drawable.none_profile_photo)
                            }
                        }
                    }
                }
            }
        }
    }

    fun loadEmployeeProfilePhoto(employeeId: String, imageView: ImageView) {
        context.let {
            if (view != null) {
                viewLifecycleOwner.lifecycleScope.launch {
                    repository.getEmployeePhotoPath(employeeId) { profilePhotoPath ->
                        if (profilePhotoPath != null && profilePhotoPath != "") {
                            loadImageFromFirebaseStorage(profilePhotoPath, imageView)
                        } else {
                            context.let {
                                Toast.makeText(
                                    it,
                                    "Profile photo not found for employee.",
                                    Toast.LENGTH_SHORT
                                ).show()
                                binding.roundButton.setImageResource(R.drawable.none_profile_photo)
                            }
                        }
                    }
                }
            }
        }
    }


    private fun saveToSharedPreferences(email: String, password: String) {
        context?.let {
            val sharedPref = it.getSharedPreferences("user_data", Activity.MODE_PRIVATE)
            val editor = sharedPref.edit()
            val currentUserId = FirebaseAuth.getInstance().currentUser!!.uid
            repository.getEmployerID(currentUserId) { id ->
                editor.putString("email", email)
                editor.putString("password", password)
                editor.putString("id", id)
                editor.apply()
            }
        }
    }

}
