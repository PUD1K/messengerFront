package com.example.lab1.activity

import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.Window
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.lab1.R
import com.example.lab1.user_data_static
import com.squareup.picasso.Picasso
import java.io.File

class MyProfileActivity : AppCompatActivity() {
    companion object{
        const val name = "user_name"
        val IMAGE_REQUEST_CODE = 100
    }

    val mSocket = SocketHandler.getSocket()

    private lateinit var btn_image: Button
    private lateinit var img_view: ImageView
    private val cameraRequest = 1888

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.activity_myprofile)
        getSupportActionBar()?.setTitle("My profile");
        getSupportActionBar()?.setBackgroundDrawable(ColorDrawable(resources.getColor(R.color.blue)))

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val btn_photo = findViewById<Button>(R.id.btn_photo)
        val tv_name = findViewById<TextView>(R.id.etvName)

        img_view = findViewById<ImageView>(R.id.imgView)
        btn_image = findViewById<Button>(R.id.btn_image)


//        val img = Uri.parse("content://com.miui.gallery.open/raw/%2Fstorage%2Femulated%2F0%2FPictures%2FVK%2FRFTlS-MAuLc.jpg")
//        img_view.setImageURI(img)
        val fileName = "%2Fstorage%2Femulated%2F0%2FDCIM%2FScreenshots%2FScreenshot_2022-10-16-12-51-58-329_com.android.chrome.jpg"
        val path = "${Environment.getExternalStorageDirectory()}/$fileName"
        val file = File(path);
        val uri = Uri.fromFile(file);
        Picasso.with(this).load(uri).into(img_view)
//        img_view.setImageURI(uri)
        Log.d("img", "${uri}")

        if(ContextCompat.checkSelfPermission(applicationContext, android.Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED)
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.CAMERA), cameraRequest)

        btn_image.setOnClickListener {
            pickImageGallery()
        }

        btn_photo.setOnClickListener{
            val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            startActivityForResult(cameraIntent, cameraRequest)
        }

        tv_name.setText("Welcome, ${user_data_static.name}")
    }

    private fun pickImageGallery(){
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, IMAGE_REQUEST_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == IMAGE_REQUEST_CODE && resultCode == RESULT_OK){
            img_view.setImageURI(data?.data)
            Log.d("img", "${data?.data}")
        }
        else if(requestCode == cameraRequest && resultCode == RESULT_OK){
            val photo: Bitmap = data?.extras?.get("data") as Bitmap
            img_view.setImageBitmap(photo)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.activity_main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle item selection
        if(item.toString() == "Logout"){
            startActivity(Intent(this, LoginActivity::class.java))
            mSocket.emit("offline", user_data_static.number)
        }
        else if(item.itemId == android.R.id.home){
            val intent = Intent(this, MainMenuActivity::class.java)
            startActivity(intent)
        }
        finish()
        return true
    }
}