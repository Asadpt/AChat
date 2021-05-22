package com.asad.firebasechat

import android.Manifest
import android.content.ContentValues
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.asad.firebasechat.utility.MyPreferences
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.snackbar.Snackbar
import id.zelory.compressor.Compressor
import kotlinx.android.synthetic.main.activity_profile.*
import kotlinx.android.synthetic.main.row_user.view.*
import kotlinx.coroutines.launch
import java.io.File

class ProfileActivity : AppCompatActivity() {

    var imageURI: Uri? = null
    var finalFile:File? = null
    var compressedImage: File? = null
    var mymanager = MyManager()
    var currentUser:UserData? = null
    var imgRequestOption= RequestOptions().fitCenter().override(800,800).diskCacheStrategy(DiskCacheStrategy.ALL)

        companion object {
        private val CAMERA_CODE = 1000
        private val GALLERY_CODE = 1001
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        supportActionBar?.hide()
        currentUser = MyPreferences?.getuser()
        bindViews()

    }

    private fun bindViews() {
        back?.setOnClickListener { onBackPressed() }
        profileimg?.setOnClickListener { showPictureDialog() }
        if(currentUser != null){
            userName?.text = currentUser?.name
            if(currentUser?.profileImageUrl != null)
            Glide.with(this).load(currentUser?.profileImageUrl?:"").apply(imgRequestOption).into(profileimg)
        }
    }

    fun showPictureDialog() {
        val pictureDialog = AlertDialog.Builder(this)
        val pictureDialogItems = arrayOf("Gallery", "Camera")
        pictureDialog.setItems(pictureDialogItems, DialogInterface.OnClickListener { dialog, which ->
            when (which) {
                0 -> {
                    try { storagePermission() } catch (e: Exception) { e.printStackTrace(); }
                    dialog.cancel()
                }
                1 -> {
                    try { cameraPermission() } catch (e: Exception) { e.printStackTrace(); }
                    dialog.cancel()
                }
            }
        })
        pictureDialog.show()
    }

    private fun cameraPermission() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA), GALLERY_CODE)
        }else
            takePhotoFromCamera()
    }

    private fun storagePermission() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.CAMERA), GALLERY_CODE);
        } else {
            choosePhotoFromGallary()
        }
    }


    fun choosePhotoFromGallary() {
        val galleryIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(galleryIntent, GALLERY_CODE)
    }

    private fun takePhotoFromCamera() {
        imageURI = getImagePathURI()
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageURI)
        startActivityForResult(intent, CAMERA_CODE)
    }

    private fun getImagePathURI(): Uri? {
        var values = ContentValues()
        values.put(MediaStore.Images.Media.TITLE, "New Picture");
        values.put(MediaStore.Images.Media.DESCRIPTION, "From your Camera")
        imageURI = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
        return imageURI
    }

    public override fun onActivityResult(requestCode: Int, resultCode: Int, imageReturnedIntent: Intent?) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent)
        if (resultCode != null && resultCode == RESULT_CANCELED && imageReturnedIntent == null) {
            return
        }
        when (requestCode) {
            GALLERY_CODE -> {
                if (imageReturnedIntent!!.data != null) {
                    var contentURI = imageReturnedIntent.data
                    if(contentURI != null){
                        val filePathColumn = arrayOf(MediaStore.Images.Media.DATA)
                        val cursor = getContentResolver().query(contentURI!!, filePathColumn, null, null, null)
                        cursor!!.moveToFirst()
                        val columnIndex = cursor.getColumnIndex(filePathColumn[0])
                        val picturePath = cursor.getString(columnIndex)
                        finalFile = File(picturePath)
                        showPreview(finalFile)
                    }
                }
            }
            CAMERA_CODE -> {
                if (imageURI != null) {
                    finalFile = File(getRealPathFromURI(imageURI!!))
                    showPreview(finalFile)
                }
            }

        }
    }

    fun getRealPathFromURI(uri: Uri): String {
        val cursor = getContentResolver().query(uri, null, null, null, null)
        cursor!!.moveToFirst()
        val idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA)
        return cursor.getString(idx)
    }

    fun showPreview(finalFile:File?){
        finalFile?.let {
            lifecycleScope.launch {
                try{
                    compressedImage = Compressor.compress(this@ProfileActivity,it)
                    profileimg?.setImageBitmap(BitmapFactory.decodeFile(compressedImage?.absolutePath))
                    var user = MyPreferences.getuser()
                    mymanager.sendFileToFireStorage(user!!,compressedImage)


                }catch (e:Exception){
                    e.printStackTrace()
                }

            }
        }


    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if(requestCode == GALLERY_CODE){

            //CHECK READ STORAGE PERMISSION ENABLED
            try {
                if (permissions?.size >= 1 &&
                    permissions[0] == Manifest.permission.READ_EXTERNAL_STORAGE &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                } else {

                }
                //CHECK WRITE storage PERMISSION ENABLED
                if (permissions?.size >= 2 &&
                    permissions[1] == Manifest.permission.WRITE_EXTERNAL_STORAGE &&
                    grantResults[1] == PackageManager.PERMISSION_GRANTED) {

                } else {

                }

                //CHECK CAMERA PERMISSION ENABLED
                if (permissions?.size >= 3 &&
                    permissions[2] == Manifest.permission.CAMERA &&
                    grantResults[2] == PackageManager.PERMISSION_GRANTED) {

                } else {

                }
            }catch(e:Exception){

            }
        }
    }






}




