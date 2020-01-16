package nl.management.finance.app;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import dagger.android.AndroidInjection;
import nl.management.finance.app.R;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AndroidInjection.inject(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

}
