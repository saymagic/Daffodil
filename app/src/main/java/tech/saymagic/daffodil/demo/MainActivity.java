package tech.saymagic.daffodil.demo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.util.Random;

import tech.saymagic.daffodil.lib.Daffodil;
import tech.saymagic.daffodil.lib.DaffodilPrinter;
import tech.saymagic.daffodil.lib.MethodInfo;
import tech.saymagic.daffodil.lib.MethodRemember;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.btn_random_max_test).setOnClickListener(this);
    }

    @Daffodil
    public int max(int a, int b) {
        return Math.max(a, b);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.btn_random_max_test:
                Random random = new Random();
                boolean enabled = random.nextBoolean();
                DaffodilPrinter.setEnabled(enabled);
                Toast.makeText(this, max(random.nextInt(), random.nextInt()) + " " + enabled, Toast.LENGTH_LONG).show();
                break;
        }
    }
}
