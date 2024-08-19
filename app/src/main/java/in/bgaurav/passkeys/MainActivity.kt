package `in`.bgaurav.passkeys

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import `in`.bgaurav.passkeys.utility.DataProvider
import `in`.bgaurav.passkeys.view.AuthActivity
import `in`.bgaurav.passkeys.view.BookActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        DataProvider.initSharedPref(this)

        if(DataProvider.isSignedIn())
            startActivity(Intent(this@MainActivity, BookActivity::class.java))
        else
            startActivity(Intent(this@MainActivity, AuthActivity::class.java))

        finish()
    }
}