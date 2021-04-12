package com.example.myapplication.activity

import android.app.Activity
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.R
import com.google.android.gms.tasks.Continuation
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import kotlinx.android.synthetic.main.activity_home_page_experiment.*
import okio.IOException
import java.util.*


class HomePageExperimentActivity : AppCompatActivity() {
    lateinit var button: Button
    private val TAG = "DocSnippets"
   val  PICK_IMAGE = 100
    private val PICK_IMAGE_REQUEST = 71
    var filePath23:Uri?=null
    private var storageReference: StorageReference? = null
    private var firebaseStore: FirebaseStorage? = null
    val db = Firebase.firestore
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_page_experiment)
        val sharedPreferences=getSharedPreferences("sharedPrefs",Context.MODE_PRIVATE)
        firebaseStore = FirebaseStorage.getInstance()
        storageReference = FirebaseStorage.getInstance().reference
        // for initalisation of cloud firestore

        title="Register your restaurant"
        button = findViewById(R.id.buttonforsalary)
        button.setOnClickListener {
            uploadImage()
        }
        btn_choose_image.setOnClickListener { launchGallery() }
        btn_upload_image.setOnClickListener { uploadImage() }
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }private fun launchGallery() {
        val gallery =
            Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
        startActivityForResult(gallery, PICK_IMAGE_REQUEST)

    }

    private fun uploadImage(){
        if(filePath23 != null){
          val progressDialog=ProgressDialog(this)
            progressDialog.setTitle("uploading..>>")
            progressDialog.show()
            val imageRef=storageReference!!.child("images/"+UUID.randomUUID().toString())

            imageRef.putFile(filePath23!!)
                .addOnSuccessListener {
                    progressDialog.dismiss()
                    Toast.makeText(this, "uploaded", Toast.LENGTH_SHORT).show()



                }
                .addOnFailureListener {
                    progressDialog.dismiss()
                    Toast.makeText(this, "upload failed", Toast.LENGTH_SHORT).show()
                }
                .addOnProgressListener {taskSnapShot->
                    val progress = 100.0 * taskSnapShot.bytesTransferred/taskSnapShot.totalByteCount
                    progressDialog.setMessage("uploading..."+progress.toInt() +"%...")
                }

               val uploadTask=imageRef.putFile(filePath23!!)
            val urlTask=uploadTask.continueWithTask(Continuation<UploadTask.TaskSnapshot, Task<Uri>> { task ->
    if (!task.isSuccessful) {
        task.exception?.let {
            throw it
        }
    }
    imageRef.downloadUrl
}).addOnCompleteListener {task->
                if (task.isSuccessful) {
                    val downloadUri = task.result
                    val url=downloadUri.toString()
                    val ResField = hashMapOf(
                        "ResName" to ResName.text.toString(),
                        "Price_for_one"  to ResPriceForOne.text.toString(),
                        "Rating"  to  ResRating.text.toString(),
                        "Images" to  downloadUri.toString()
                    )
                    val FoodField = hashMapOf(
                        "FoodName" to  salary.text.toString(),
                        "Food_Price_for_one"  to FoodPriceForOne.text.toString(),
                        "Food_Rating"  to  FoodRating.text.toString()
                    )

//             to write data in cloud firestore:
                    db.collection("Restaurant_Name1")
                        .document(ResName.text.toString())
                        .set(ResField)
                    db.collection("Restaurant_Name1")
                        .document(ResName.text.toString())
                        .collection("FoodName")
                        .document(salary.text.toString())
                        .set(FoodField).addOnSuccessListener {
                            Toast.makeText(this, "success", Toast.LENGTH_SHORT).show()
                        }
                        .addOnFailureListener { e ->
                            Log.w(TAG, "Error adding document", e)
                            Toast.makeText(this, "failed", Toast.LENGTH_SHORT).show()
                        }
                } else {
                    // Handle failures
                    // ...
                }
            }


        }else{
            Toast.makeText(this, "Please select an Image", Toast.LENGTH_SHORT).show()
        }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK) {
            if(data != null || data?.data != null) {
                filePath23 = data.data
                try {
                    val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, filePath23)
                    imageView!!.setImageBitmap(bitmap)
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }

}


//// Reference to an image file in Cloud Storage
//val storageReference = Firebase.storage.reference
//
//// ImageView in your Activity
//val imageView = findViewById<ImageView>(R.id.imageView)
//
//// Download directly from StorageReference using Glide
//// (See MyAppGlideModule for Loader registration)
//Glide.with(this /* context */)
//.load(storageReference)
//.into(imageView)


//val uploadTask = imageRef?.putFile(filePath23!!)
//val urlTask = uploadTask?.continueWithTask(Continuation<UploadTask.TaskSnapshot, Task<Uri>> { task ->
//    if (!task.isSuccessful) {
//        task.exception?.let {
//            throw it
//        }
//    }
//    return@Continuation imageRef.downloadUrl
//}).addOnCompleteListener {task ->
//    if (task.isSuccessful){
//        val downloadurl=task.result
//        Toast.makeText(this, "${downloadurl.toString()}", Toast.LENGTH_SHORT).show()
//        val db = FirebaseFirestore.getInstance()
//
//
//
//        db.collection("Restaurant_Name1")
//            .document(ResName.text.toString())
//            .collection("Images")
//            .document("ImagestoUrl")
//            .set(downloadurl.toString())
//            .addOnSuccessListener { documentReference ->
//                Toast.makeText(this, "Saved to DB", Toast.LENGTH_LONG).show()
//            }
//            .addOnFailureListener { e ->
//                Toast.makeText(this, "Error saving to DB", Toast.LENGTH_LONG).show()
//            }
//    }
//}
//    .addOnFailureListener {
//        Toast.makeText(this, "failed", Toast.LENGTH_SHORT).show()
//    }