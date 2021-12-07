package uk.co.thechange.kidsdrawingapp

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts

// inherits from appcompatactivity etc.
class MainActivity : AppCompatActivity() {

    private val cameraResultLauncher: ActivityResultLauncher<String> =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()){
            isGranted ->
            if(isGranted){
                Toast.makeText(this,
                "Permission granted for camera.", Toast.LENGTH_LONG).show()
                }else{
                 Toast.makeText(this, "Permission was denied for the camera", Toast.LENGTH_LONG).show()
            }
        }

    private val cameraAndLocationResultLauncher: ActivityResultLauncher<Array<String>> =
        registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()){
               permissions ->
            permissions.entries.forEach{
                val permissionName = it.key
                val isGranted = it.value
                if(isGranted){
                    if(permissionName == Manifest.permission.ACCESS_FINE_LOCATION){
                        Toast.makeText(
                            this,
                            "Permission granted for location",
                            Toast.LENGTH_LONG
                        )
                            .show()
                    }else{
                        Toast.makeText(
                            this,
                            "Permission granted for Camera",
                            Toast.LENGTH_LONG
                        )
                            .show()
                    }
                } else{
                    if(permissionName == Manifest.permission.ACCESS_FINE_LOCATION){
                        Toast.makeText(
                            this,
                            "Permission denied for location",
                            Toast.LENGTH_LONG
                        )
                            .show()
                    }else{
                        Toast.makeText(
                            this,
                            "Permission denied for camera",
                            Toast.LENGTH_LONG
                        )
                            .show()
                    }
                }
            }
            }




    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val btnCameraPermission: Button = findViewById(R.id.btnCameraPermission)
        btnCameraPermission.setOnClickListener{
            if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M &&
                    shouldShowRequestPermissionRationale(Manifest.permission.CAMERA)){
                showRationaleDialog("App needs to access the camera so you can send and save images.",
                "The app currently can't be used because access has been denied.")
            } else{
                cameraAndLocationResultLauncher.launch(
                    arrayOf(Manifest.permission.CAMERA,
                    Manifest.permission.ACCESS_FINE_LOCATION)
                )
            }
        }

    }
    /*
    * THIS SECTION IS WITHIN THE MAIN ACTIVITY CLASS BUT OUTSIDE THE ON CREATE FUNCTION
    * IT HAS TO BE IN THE MAIN ACTIVITY CLASS OR IT CANNOT FIND THE CONTECT FOR "THIS"
    * */
    /*
    * Shows the rationale dialog for displaying why the app needs permission.
    * Only shown if the user has denied the permission request previously.
    */
    private fun showRationaleDialog(
        title: String,
        message: String,
    ){
        val builder: AlertDialog.Builder = AlertDialog.Builder(this)
        builder.setTitle(title)
            .setMessage(message)
            .setPositiveButton("Cancel"){dialog, _->
                dialog.dismiss()
            }
        builder.create().show()
    }
}



