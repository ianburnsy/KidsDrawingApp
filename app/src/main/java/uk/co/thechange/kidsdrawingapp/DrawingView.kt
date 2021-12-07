package uk.co.thechange.kidsdrawingapp

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View

// going to use this as a view.
// visible with the main activity but as a view.
// view is a new type we are using.
class DrawingView(context: Context, attrs: AttributeSet) : View(context, attrs) {

    private var mDrawPath: CustomPath? = null // our defined class of path (nested inner class in this file)
    private var mCanvasBitmap: Bitmap? = null // a bitmap is a pixel image https://techterms.com/definition/bitmap
    private var mDrawPaint: Paint? = null
    private var mCanvasPaint: Paint? = null // canvas paint view
    private var mBrushSize: Float = 0.toFloat()
    private var color = Color.BLACK
    // canvas to draw on
    private var canvas: Canvas? = null

    // store the lines drawn....
    private val mPaths = ArrayList<CustomPath>()

    init{
        setUpDrawing()
    }
    private fun setUpDrawing(){
        mDrawPaint = Paint() // https://developer.android.com/reference/android/graphics/Paint
        // setting our path
        /*
        Draw more complex shapes using the Path class. Define a shape by adding lines and curves to a
        Path object, then draw the shape using drawPath().
        Just as with primitive shapes, paths can be outlined, filled, or both, depending on the setStyle().
         */
        mDrawPath = CustomPath(color, mBrushSize)

        // setting the color of the draw path (although isn't tis done above?)
        mDrawPaint!!.color = color // can use assertion because we set up earlier.
        mDrawPaint!!.style = Paint.Style.STROKE
        mDrawPaint!!.strokeJoin = Paint.Join.ROUND
        mDrawPaint!!.strokeCap = Paint.Cap.ROUND // look at the difference.
        mCanvasPaint = Paint(Paint.DITHER_FLAG)
        mBrushSize = 20.toFloat()

    }
// when the screen is created the view gets inflated and calls this funciton.
    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        mCanvasBitmap = Bitmap.createBitmap(w,h,Bitmap.Config.ARGB_8888) // could probably use the alpha version for the signature app...
        canvas = Canvas(mCanvasBitmap!!)
    }

    // change canvas to canvas? if fails
    override fun onDraw(canvas:Canvas){
        super.onDraw(canvas)
        canvas.drawBitmap(mCanvasBitmap!!,0f,0f, mCanvasPaint) // passed the position to the top left
        //making persistance
        for(path in mPaths){
            mDrawPaint!!.strokeWidth = path.brushThickness
            mDrawPaint!!.color = path.color
            canvas.drawPath(path, mDrawPaint!!)
        }

        //what should happen when we draw....
        if(!mDrawPath!!.isEmpty){ // checking if empty not if null.
            mDrawPaint!!.strokeWidth = mDrawPath!!.brushThickness
            mDrawPaint!!.color = mDrawPath!!.color

        canvas.drawPath(mDrawPath!!, mDrawPaint!!) // mDrawpath needs to not be empty.
        }
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        val touchX = event?.x
        val touchY = event?.y

        when(event?.action) {
            // three things when you have motion events
            // action down, action move (moving finger on screen) and action up (release screen)
            MotionEvent.ACTION_DOWN -> {
                mDrawPath!!.color = color
                mDrawPath!!.brushThickness = mBrushSize

                //mDrawPath!!.reset()
                mDrawPath!!.moveTo(touchX!!, touchY!!) // can do surround with null checks also
            }
            MotionEvent.ACTION_MOVE -> {
                if (touchX != null) {
                    if (touchY != null) {
                        mDrawPath!!.lineTo(touchX,touchY)
                    }
                }
            }
            MotionEvent.ACTION_UP -> {
                mPaths.add(mDrawPath!!)
                mDrawPath = CustomPath(color, mBrushSize)
            }
            else -> return false
        }
        invalidate()
        return true
    }



    // creating a nested class then making it internal inner
    // there is a class type called Path
    // path needs to use a canvas to draw on.
    internal inner class CustomPath(var color: Int,
                                    var brushThickness: Float) : Path() {

    }
}