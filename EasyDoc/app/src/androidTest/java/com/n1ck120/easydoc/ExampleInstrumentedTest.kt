package com.n1ck120.easydoc

import android.os.Environment
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.n1ck120.easydoc.core.document.DocumentGen

import org.junit.Test
import org.junit.runner.RunWith
import org.junit.Assert.*

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {

    val testDocPath : String = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS).toString() + "/TestDoc"

    @Test
    fun useAppContext() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        assertEquals("com.n1ck120.easydoc", appContext.packageName)
    }

    @Test
    fun pdf_generation(){
        DocumentGen.generatePDF("Test123","Test123","Pimpoio", "$testDocPath.pdf", null)
    }

    @Test
    fun docx_generation(){
        DocumentGen.generatePDF("Test123","Test123","Pimpoio", "$testDocPath.docx", null)
    }
}