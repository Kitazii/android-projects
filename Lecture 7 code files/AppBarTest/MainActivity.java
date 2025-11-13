package gcu.mpd.appbartest;

import androidx.appcompat.app.AppCompatActivity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.PopupMenu;
// import android.widget.Toolbar;
import androidx.appcompat.widget.Toolbar;

public class MainActivity extends AppCompatActivity implements PopupMenu.OnMenuItemClickListener,View.OnClickListener
{
    private View topView;
    private TextView textView1;
    private PopupMenu popupMenu;
    private TextView anchorView;
    private final static int ONE = 1;
    private final static int TWO = 2;
    private final static int THREE = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Sort out the Toolbar ready for some options menus
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        topView = (View)findViewById(R.id.topView);
        textView1 = (TextView)findViewById(R.id.textView1);
        registerForContextMenu(textView1);

        popupMenu = new PopupMenu(this, findViewById(R.id.textView2));
        popupMenu.getMenu().add(Menu.NONE, ONE, Menu.NONE, "Light salmon");
        popupMenu.getMenu().add(Menu.NONE, TWO, Menu.NONE, "Light green");
        popupMenu.getMenu().add(Menu.NONE, THREE, Menu.NONE, "Light blue");
        popupMenu.setOnMenuItemClickListener(this);

        anchorView = (TextView)findViewById(R.id.anchor);
        anchorView.setOnClickListener(this);

    }

    @Override
    public void onClick(View v)
    {
        popupMenu.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        super.onOptionsItemSelected(item);
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

        }

        return true;
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo)
    {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.setHeaderTitle("Colour Context Menu");
        menu.add(0,v.getId(),0,"Red");
        menu.add(0,v.getId(),0,"Green");
        menu.add(0,v.getId(),0,"Blue");
        menu.add(0,v.getId(),0,"Yellow");
    }

    @Override
    public boolean onContextItemSelected(MenuItem item)
    {
        if (item.getTitle()=="Red")
        {
            Toast.makeText(this, "Red Option", Toast.LENGTH_LONG).show();
            textView1.setBackgroundColor(Color.RED);
        }
        else if (item.getTitle()=="Green")
        {
            textView1.setBackgroundColor(Color.GREEN);
        }
        else
        if (item.getTitle()=="Blue")
        {
            textView1.setBackgroundColor(Color.BLUE);
        }
        else
        if (item.getTitle()=="Yellow")
        {
            textView1.setBackgroundColor(Color.YELLOW);
        }
        else
            return false;

        return true;
    }

    @Override
    public boolean onMenuItemClick(MenuItem item)
    {
        TextView tv = (TextView) findViewById(R.id.selection);
        switch (item.getItemId()) {
            case ONE:
                tv.setText("Light Salmon");
                anchorView.setBackgroundResource(R.color.LightSalmon);
                break;
            case TWO:
                tv.setText("Light Green");
                anchorView.setBackgroundResource(R.color.LightGreen);
                break;
            case THREE:
                tv.setText("Light Blue");
                anchorView.setBackgroundResource(R.color.LightBlue);
                break;
        }
        return false;
    }

}