package com.example.onthetime.view.fragments

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import android.graphics.Rect
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import com.example.onthetime.adapter.PhotoOptionsBottomSheet
import com.example.onthetime.databinding.FragmentDashboardBinding
import com.example.onthetime.model.Date
import com.example.onthetime.repository.EmployerRepository
import com.example.onthetime.view.activities.MainActivity.Companion.REQUEST_CODE_PICK_IMAGE
import com.example.onthetime.view.activities.MainActivity.Companion.REQUEST_CODE_TAKE_PHOTO
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
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDashboardBinding.inflate(inflater, container, false)
        repository = EmployerRepository()

        binding.roundButton.setOnClickListener {
            val photoOptionsBottomSheet =
                PhotoOptionsBottomSheet(object : PhotoOptionsBottomSheet.PhotoOptionListener {
                    override fun onChooseFromLibrary() {
                        openGallery()
                    }

                    override fun onTakePhoto() {
                        openCamera()
                    }
                })

            photoOptionsBottomSheet.show(parentFragmentManager, "PhotoOptionsBottomSheet")
        }

        val currentUser = FirebaseAuth.getInstance().currentUser

        loadEmployerProfilePhoto(currentUser?.uid.toString(), binding.roundButton)


        return binding.root
    }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, REQUEST_CODE_PICK_IMAGE)
    }

    private lateinit var currentPhotoPath: String

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
//                        val circularBitmap = getCircularBitmap(bitmap) // Resim yuvarlak yapılıyor
                        binding.roundButton.setImageBitmap(bitmap)


//                        binding.roundButton.setImageBitmap(bitmap)

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
                    Toast.makeText(requireContext(), "Image uploaded: $uri", Toast.LENGTH_SHORT)
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

//    fun loadImageFromFirebaseStorage(profilePhotoPath: String, imageView: ImageView) {
//        val storageReference = FirebaseStorage.getInstance().reference.child(profilePhotoPath)
//
//        storageReference.downloadUrl
//            .addOnSuccessListener { uri ->
//                Picasso.get()
//                    .load(uri)
//                    .into(imageView)
//            }
//            .addOnFailureListener {
//                Toast.makeText(requireContext(), "Error loading image.", Toast.LENGTH_SHORT).show()
//            }
//    }


    fun loadImageFromFirebaseStorage(profilePhotoPath: String, imageView: ImageView) {
        Picasso.get()
            .load(profilePhotoPath)
            .into(imageView)
    }


    fun loadEmployerProfilePhoto(employerId: String, imageView: ImageView) {
        repository.getEmployerPhotoPath(employerId) { profilePhotoPath ->
            if (profilePhotoPath != null) {
                loadImageFromFirebaseStorage(profilePhotoPath, imageView)
            } else {
                Toast.makeText(
                    requireContext(),
                    "Profile photo not found for employer.",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }


}
