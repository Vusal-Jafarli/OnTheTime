package com.example.onthetime.ui.fragments

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import android.graphics.Rect
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.ContactsContract.CommonDataKinds.Im
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import com.example.onthetime.R
import com.example.onthetime.adapter.PhotoOptionsBottomSheet
import com.example.onthetime.databinding.FragmentDashboardBinding
import com.example.onthetime.model.Date
//import com.example.onthetime.model.User
import com.example.onthetime.repository.EmployerRepository
import com.example.onthetime.ui.activities.MainActivity.Companion.REQUEST_CODE_PICK_IMAGE
import com.example.onthetime.ui.activities.MainActivity.Companion.REQUEST_CODE_TAKE_PHOTO
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.squareup.picasso.Picasso
import java.io.File
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.UUID

class DashboardFragment : Fragment() {

    lateinit var binding: FragmentDashboardBinding
    lateinit var repository: EmployerRepository
    private lateinit var currentPhotoPath: String


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDashboardBinding.inflate(inflater, container, false)
        repository = EmployerRepository()


        val currentUserFromFirebase = FirebaseAuth.getInstance().currentUser
        var currentUserEmail: String = ""
        var currentUserPassword: String = ""

        repository.getEmployerEmail(currentUserFromFirebase!!.uid) { email ->
            if (!email.isNullOrEmpty()) {
                currentUserEmail = email

                repository.getEmployerPassword(currentUserFromFirebase.uid) { password ->
                    if (!password.isNullOrEmpty()) {
                        currentUserPassword = password

                        saveToSharedPreferences(currentUserEmail, currentUserPassword)
                    }
                }
            }
        }


        binding.roundButton.setOnClickListener {

            val photoOptionsBottomSheet =
                PhotoOptionsBottomSheet(object : PhotoOptionsBottomSheet.PhotoOptionListener {
                    override fun onChooseFromLibrary() {
                        openGallery()
                    }

                    override fun onTakePhoto() {
                        openCamera()
                    }

                    override fun onRemoveCurrentPicture() {
                        removePhoto()
                    }
                })
//            val currentUser = FirebaseAuth.getInstance().currentUser
            photoOptionsBottomSheet.show(parentFragmentManager, "PhotoOptionsBottomSheet")
//            loadEmployerProfilePhoto(currentUser?.uid.toString(), photoOptionsBottomSheet.requireView().findViewById<ImageView>(R.id.imageview_bottom))
        }

        val currentUser = FirebaseAuth.getInstance().currentUser

        loadEmployerProfilePhoto(currentUser?.uid.toString(), binding.roundButton)
        repository.getEmployerName(currentUser?.uid.toString()) { name ->
            if (name != null) {
                binding.firstTextView.text = "Hello " + name + "!"
                binding.progressBarProfile.visibility = View.GONE
            } else {
                Toast.makeText(requireContext(), "Name undefined", Toast.LENGTH_SHORT).show()
            }

        }

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
            Toast.makeText(
                requireContext(),
                "Image has been changed succesfully.",
                Toast.LENGTH_SHORT
            )
            binding.roundButton.setImageResource(R.drawable.none_profile_photo)
        }


    }

    private fun openCamera() {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (takePictureIntent.resolveActivity(requireActivity().packageManager) != null) {
            val photoFile: File? = createImageFile()
            photoFile?.let {
                val photoURI: Uri = FileProvider.getUriForFile(
                    requireContext(),
                    "${requireContext().packageName}.fileprovider",
                    it
                )
                currentPhotoPath = it.absolutePath
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                startActivityForResult(takePictureIntent, REQUEST_CODE_TAKE_PHOTO)
            }
        }
    }

    private fun createImageFile(): File {
        val timeStamp: String =
            SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val storageDir: File =
            requireContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES)!!
        return File.createTempFile("JPEG_${timeStamp}_", ".jpg", storageDir)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                REQUEST_CODE_PICK_IMAGE -> {
                    val selectedImageUri: Uri? = data?.data
                    selectedImageUri?.let {
                        uploadToFirebase(it)
                        val inputStream = requireContext().contentResolver.openInputStream(it)
                        val bitmap = BitmapFactory.decodeStream(inputStream)
                        binding.roundButton.setImageBitmap(bitmap)
                    }
                }

                REQUEST_CODE_TAKE_PHOTO -> {
                    val photoUri: Uri = Uri.fromFile(File(currentPhotoPath))
                    uploadToFirebase(photoUri)

                }
            }
        }
    }


    private fun deleteProfilePhotoPathFromFirebase() {

        val uri = "null"
        val currentUser = FirebaseAuth.getInstance().currentUser
        repository.updateProfilePhotoPath(currentUser!!.uid, uri.toString()) {
            Toast.makeText(
                requireContext(),
                "Image deleted from Firebase: $uri",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    fun deleteImageFromFirebaseStorage(filePath: String) {
        val storageReference = FirebaseStorage.getInstance().reference

        val fileRef = storageReference.child(filePath)

        fileRef.delete()
            .addOnSuccessListener {
                Toast.makeText(context, "Photo has been deleted succesfully.", Toast.LENGTH_SHORT)
                    .show()
            }
            .addOnFailureListener { exception ->
                Toast.makeText(
                    context,
                    "Error:Deleting the photo ${exception.message}",
                    Toast.LENGTH_SHORT
                ).show()
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
                    Toast.makeText(requireContext(), "Image uploaded: $uri", Toast.LENGTH_SHORT)
                        .show()
                }
            }.addOnFailureListener {
                println("Error uploading image: ${it.message}")
            }
        }

        fun getCircularBitmap(bitmap: Bitmap): Bitmap {
            val size = Math.min(bitmap.width, bitmap.height)
            val output = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888)

            val canvas = Canvas(output)

            val paint = Paint()
            val rect = Rect(0, 0, size, size)

            paint.isAntiAlias = true
            canvas.drawARGB(0, 0, 0, 0)
            canvas.drawCircle(
                (size / 2).toFloat(),
                (size / 2).toFloat(),
                (size / 2).toFloat(),
                paint
            )

            paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)
            canvas.drawBitmap(bitmap, rect, rect, paint)

            return output
        }


    }

    private fun updatePhotoPathInFirestore(newPhotoPath: String) {
        val db = FirebaseFirestore.getInstance()
        val employerId = "employer_document_id"

        val update = hashMapOf<String, Any>(
            "photo_path" to newPhotoPath
        )

        db.collection("employers").document(employerId).update(update)
            .addOnSuccessListener {
                println("Photo path successfully updated")
            }
            .addOnFailureListener { e ->
                println("Error updating photo path: ${e.message}")
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

        repository.getEmployerPhotoPath(employerId) { profilePhotoPath ->
            if (profilePhotoPath != null && profilePhotoPath != "") {
                loadImageFromFirebaseStorage(profilePhotoPath, imageView)
            } else {
                Toast.makeText(
                    requireContext(),
                    "Profile photo not found for employer.",
                    Toast.LENGTH_SHORT
                ).show()
                binding.roundButton.setImageResource(R.drawable.none_profile_photo)

            }
        }
    }


    private fun saveToSharedPreferences(email: String, password: String) {
        val sharedPref = requireContext().getSharedPreferences("user_data", Activity.MODE_PRIVATE)
        val editor = sharedPref.edit()
        val currentUserId = FirebaseAuth.getInstance().currentUser!!.uid
        repository.getEmployerID(currentUserId){ id ->
            editor.putString("email", email)
            editor.putString("password", password)
            editor.putString("id", id)
            editor.apply()
        }
    }

}
