package com.princeakash.projectified

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.princeakash.projectified.user.view.SplashScreenFragment

class InitialActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_initial)
        if(savedInstanceState==null) {
            supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_initial, SplashScreenFragment(), "Splash")
                    .commit()
        }
    }
}