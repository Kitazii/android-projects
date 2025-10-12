package org.me.gcu.listview_custom;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class CustomAdapter extends BaseAdapter {
    Context context;
    String[] brdList; //storage for textual data
    int[] pics;  //storage for icons ids
    LayoutInflater inflter;

    public CustomAdapter(Context applicationContext, String[] birdList, int[] images) {
        this.context = context;
        this.brdList = birdList;
        this.pics = images;
        inflter = (LayoutInflater.from(applicationContext));
    }

    @Override
    public int getCount() {
        return brdList.length;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        CustomAdapter.WidgetsHolder holder = null;
        if(view == null) { //first time, create view and bound to widgets
            view = inflter.inflate(R.layout.layout_listview, null);
            holder = new CustomAdapter.WidgetsHolder();
            holder.bird = (TextView) view.findViewById(R.id.textView);
            holder.pic = (ImageView) view.findViewById(R.id.icon);
            view.setTag(holder); //store bound objects into the view
        }
        else{ //already existing, just reuse bound objects
            holder = (CustomAdapter.WidgetsHolder)view.getTag();
        }
        holder.bird.setText(brdList[i]);
        holder.pic.setImageResource(pics[i]);
        return view;
    }
    static class WidgetsHolder { //preserve objects bound to widgets
        TextView bird;
        ImageView pic;
    }
}
