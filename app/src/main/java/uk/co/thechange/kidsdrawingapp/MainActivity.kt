package uk.co.thechange.kidsdrawingapp

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts

// inherits from appcompatactivity etc.
class MainActivity : AppCompatActivity() {

    private var drawingView: DrawingView? = null
    private var mImageButtonCurrentPaint: ImageButton? = null

    val requestPermission: ActivityResultLauncher<Array<String>> =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()){
            permissions ->
            permissions.entries.forEach{
                val permissionName = it.key
                val isGranted = it.value

                if(isGranted){
                    Toast.makeText(this,
                        "Permission granted now you can read the storage files",
                        Toast.LENGTH_LONG).show()
                }else{
                    if(permissionName==Manifest.permission.READ_EXTERNAL_STORAGE){
                        Toast.makeText(this,
                            "Ooops you just denied permission",
                            Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

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

       drawingView = findViewById(R.id.drawing_view) // the xml drawing view we created.
        // or could of used databinding.

        drawingView?.setSizeForBrush(5.toFloat())

        val ib_brush : ImageButton = findViewById(R.id.ib_pen) // not a button but an image button
        ib_brush.setOnClickListener{
            showBrushSizeChoserDialog()
        }




    /*
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
        */




    }
    // SHOWING BRUSH SIZE CHOOSER
    private fun showBrushSizeChoserDialog(){
        val brushDialog = Dialog(this)
        brushDialog.setContentView(R.layout.dialog_brush_size)
        brushDialog.setTitle("Brush size: ")
        val smallBtn: ImageButton = brushDialog.findViewById(R.id.ib_small_brush) // why is this not working???
        smallBtn.setOnClickListener {
            drawingView?.setSizeForBrush(5.toFloat())
            brushDialog.dismiss()
        }
    }

    /*
    * THIS SECTION IS WITHIN THE MAIN ACTIVITY CLASS BUT OUTSIDE THE ON CREATE FUNCTION
    * IT HAS TO BE IN THE MAIN ACTIVITY CLASS OR IT CANNOT FIND THE CONTECT FOR "THIS"
    * */

    private fun alertDialogFunction(){
        // Use the Builder class for convenient dialog construction
        val builder = AlertDialog.Builder(this)
        // set title for alert dialog
        builder.setTitle("Alert")
        // set message for alert dialog
        builder.setMessage("This is Alert Dialog. Which is used to show alert")
        builder.setIcon(android.R.drawable.ic_dialog_alert)

        // performing positive action
        builder.setPositiveButton("Yes"){dialogInterface,which ->
            Toast.makeText(applicationContext,"Clicked yes", Toast.LENGTH_LONG).show()
            dialogInterface.dismiss() // Dialog will be dismissed
        }
        // performing cancel action
        builder.setNeutralButton("cancel"){dialogInterface, which ->
            Toast.makeText(
                applicationContext,
                "clicked cancel\n operation cancelled",
                Toast.LENGTH_LONG
            ).show()
            dialogInterface.dismiss() // Dialog will be dismissed
        }
        // performing negative action
        builder.setNegativeButton("No!"){dialogInterface, which ->
            Toast.makeText(applicationContext, "clicked no", Toast.LENGTH_LONG).show()
            dialogInterface.dismiss()
        }
        // Create the Alert dialog
        val alertDialog: AlertDialog = builder.create()
        // Set other dialog properties
        alertDialog.setCancelable(false) // will not allow user to cancel after clicking on remaining screen area
        alertDialog.show() // show the dialog to the UI

    }

    // TODO: 08/12/2021 Need to create the xml view for the custom dialog to work... see lecture 228
    private fun customDialogFunction(){
        val customDialog = Dialog(this)
        /*Set the screen content from a layout resource.
        The resource will be inflated, adding all top-level views to the screen.
         */
        customDialog.setContentView(R.layout.dialog_custom)
        customDialog.tv_submit.setOnClickListener(View.OnClickListener {
            Toast.makeText(applicationContext, "Clicked Submit", Toast.LENGTH_LONG).show()
            customDialog.dismiss() //
        })
        customDialog.tv_cancel.setOnClickListener(View.OnClickListener {
            Toast.makeText(applicationContext, "clicked cancel!", Toast.LENGTH_LONG).show()
            // sometimes using application context is safer than using this...
            customDialog.dismiss()
        })
    }

    // TODO: 08/12/2021 Create the xml for dialog custom progress
    // look at the progress bar documentation for this...
    // also check out the general documentation on dialogs
    private fun customProgressDialogFunction(){
        val customProgressDialog = Dialog(this)

        /*Set the screen content from a layout resource. The resource will be inflated,
        adding all top-level views to the screen
         */
        customProgressDialog.setContentView(R.layout.dialog_custom_progress)

        //Start the dialog and display it on screen
        customProgressDialog.show()
    }

    /*Shows the rationale dialog for displaying why the app needs permission.
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



