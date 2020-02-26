package com.nhatle.demosharedpreferences

import android.content.Context
import android.os.Bundle
import android.os.Environment
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import java.io.*


class MainActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var string: String
    private var filename = "SampleFile.txt"
    private var filepath = "MyFileStorage"
    lateinit var myExternalfile :File
    var myData  = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        buttoninternal.setOnClickListener(this)
        buttonExternal.setOnClickListener(this)
        buttonShare.setOnClickListener(this)
        buttonRead.setOnClickListener(this)
    }

    override fun onClick(view: View) {
        string = editOne.text.toString()
        when (view.id) {
            R.id.buttoninternal -> {
                try {

                    var openFileOutput = openFileOutput("nhatle.txt", Context.MODE_PRIVATE)
                    openFileOutput.write(string.toByteArray())
                    openFileOutput.close()
                    // cach 2
//                    var file :File = File(cacheDir,"myFile")
//                    var openFileOutput1 = FileOutputStream(file)
//                    openFileOutput1.write("ahihi do goc".toByteArray())
//                    openFileOutput1.close()
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
            R.id.buttonExternal -> {
                myExternalfile = File(getExternalFilesDir(filepath),filename)
                try {

                    var fos :FileOutputStream = FileOutputStream(myExternalfile)
                    fos.write(string.toByteArray())
                    fos.close()

                } catch (ex: FileNotFoundException) {
                    ex.printStackTrace()
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
            R.id.buttonShare -> {
                var mPreferences = getPreferences(Context.MODE_PRIVATE)
                var editor = mPreferences.edit()
                editor.putString("one", string)
                editor.apply()
            }
            R.id.buttonRead -> {
                textExternalStore.text=""
                textSharedPre.text = ""
                textInternalStore.text=""
                // doc tu internal
                readInternal()
                //doc tu external
                readExternal()
                // doc tu shareprefe
               readSharePreferences()

            }

        }
    }
    //Read file
    private fun  readInternal(){
        var fin = openFileInput("nhatle.txt")
        var inputStreamReader = InputStreamReader(fin)
        var bufferedReader: BufferedReader = BufferedReader(inputStreamReader)
        var stringBundle: StringBuilder = StringBuilder()
        var text: String? = null
        while ({ text = bufferedReader.readLine(); text }() != null) {
            stringBundle.append(text)
        }
        textInternalStore.text = (stringBundle.toString())
    }
    private fun readExternal(){
        try{
            var fileInputStream = FileInputStream(myExternalfile)
            var inp  = DataInputStream(fileInputStream)
            var inputStReader: InputStreamReader = InputStreamReader(fileInputStream)
            var bufferedRea: BufferedReader = BufferedReader(inputStReader)
            var textE: String? = null
            while ({ textE = bufferedRea.readLine(); textE }() != null) {
                myData = myData + textE
            }
            fileInputStream.close()
            textExternalStore.text = myData
        }catch (exx :IOException){
            exx.printStackTrace()
        }

    }
    private fun readSharePreferences(){
        var preferences = getPreferences(Context.MODE_PRIVATE)
        var one: String? = preferences.getString("one", "")
        textSharedPre.text = one
        if (!isExternalStorageAvailable || isExternalStorageReadOnly) {
            buttonShare.isEnabled = false
        }
    }

    private val isExternalStorageReadOnly: Boolean
        get() {
            val extStorageState = Environment.getExternalStorageState()
            return if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(extStorageState)) {
                true
            } else {
                false
            }
        }
    private val isExternalStorageAvailable: Boolean
        get() {
            val extStorageState = Environment.getExternalStorageState()
            return if (Environment.MEDIA_MOUNTED.equals(extStorageState)) {
                true
            } else {
                false
            }
        }
}

