package multilistview.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.multilistview.R;

/**
 * 带有图片的一级目录Adapter
 * @author Jack
 * @date: 2014-10-27 下午4:29:49
 */
public class ImageTextAdapter extends BaseAdapter {

    private LayoutInflater inflater;
    private String [] foods;
    private int [] images;
    private int selectedPosition = -1;     
    
    public ImageTextAdapter(Context context,String [] foods,int[] images){
        this.foods = foods;
        this.images = images;
        inflater=LayoutInflater.from(context);
    }
    
    @Override
    public int getCount() {
        return foods.length;
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder  holder = null;
        if(convertView==null){
            convertView = inflater.inflate(R.layout.common_linkagelist_view_mylist_item_with_image, parent, false);
            holder = new ViewHolder();
            holder.textView =(TextView)convertView.findViewById(R.id.textview);
            holder.imageView =(ImageView)convertView.findViewById(R.id.imageview);
            holder.layout=(LinearLayout)convertView.findViewById(R.id.colorlayout);
            convertView.setTag(holder);
        }else{
            holder=(ViewHolder)convertView.getTag();
        }
        // 设置选中效果    
        if(selectedPosition == position){   
             holder.textView.setTextColor(Color.BLUE);   
             holder.layout.setBackgroundColor(Color.LTGRAY);   
        } else {   
           holder.textView.setTextColor(Color.WHITE);   
           holder.layout.setBackgroundColor(Color.TRANSPARENT);   
        }   
        holder.textView.setText(foods[position]);
        holder.textView.setTextColor(Color.BLACK);
        if(images == null){
            holder.imageView.setVisibility(View.GONE);
        }else{
            holder.imageView.setVisibility(View.VISIBLE);
            holder.imageView.setBackgroundResource(images[position]);
        }
        holder.imageView.setVisibility(View.GONE);
        
        return convertView;
    }

    public static class ViewHolder{
        public TextView textView;
        public ImageView  imageView;
        public LinearLayout layout;
    }

    public void setSelectedPosition(int position) {   
       selectedPosition = position;   
    }   
}