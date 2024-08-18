package `in`.bgaurav.passkeys

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import `in`.bgaurav.passkeys.view.AuthActivity

@SuppressLint("CustomSplashScreen")
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        startActivity(Intent(this@MainActivity, AuthActivity::class.java))
        finish()
    }
}