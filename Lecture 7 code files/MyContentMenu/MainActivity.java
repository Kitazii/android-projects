package org.me.gcu.mycontentmenu;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private View topView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        topView = (View)findViewById(R.id.tV);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.red:  Toast.makeText(this, "Red Option", Toast.LENGTH_LONG).show();
                topView.setBackgroundColor(Color.RED);
                break;
            case R.id.green:Toast.makeText(this, "Green Option", Toast.LENGTH_LONG).show();
                topView.setBackgroundColor(Color.GREEN);
                break;
            case R.id.blue: Toast.makeText(this, "Blue Option", Toast.LENGTH_LONG).show();
                topView.setBackgroundColor(Color.BLUE);
                break;
            case R.id.black:Toast.makeText(this, "Black Option", Toast.LENGTH_LONG).show();
                topView.setBackgroundColor(Color.BLACK);
                break;
            case R.id.yellow:Toast.makeText(this, "Yellow Option", Toast.LENGTH_LONG).show();
                topView.setBackgroundColor(Color.YELLOW);
                break;
            case R.id.purple:Toast.makeText(this, "Purple Option", Toast.LENGTH_LONG).show();
                topView.setBackgroundColor(Color.rgb(204, 153, 255));
                break;
            default:
                return super.onOptionsItemSelected(item);
        }

        return true;
    }
}