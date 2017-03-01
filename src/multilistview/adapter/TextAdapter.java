package multilistview.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.multilistview.R;


/**
 * 二级ListView的纯文本Adapter
 * @author Jack
 * @date: 2014-10-27 下午4:28:41
 */
public class TextAdapter extends BaseAdapter {
    
    /**上下文对象*/
    private Context mContext;
    /**布局填充器*/
    private LayoutInflater mLayoutInflater;
    /**二级数据源*/
    private String[][] mSecondDatas;
    /**三级数据源*/
    private String[][][] mThirdDatas;
    /**选中的一级目录的位置*/
    private int mFirstPosition;
    /**选中的二级目录的位置*/
    private int mSecondPosition;
    /**设置adapter的类型，支持2、3级目录的类别*/
    private int mKind = 2;

    
    public TextAdapter(Context context, String[][] mSecondDatas, int position, int kind) {
        
        this.mContext = context;
        mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.mSecondDatas = mSecondDatas;
        this.mFirstPosition = position;
        this.mKind = kind;
    }
    

    /**
     * Creates a new instance of TextAdapter. 
     * @param context
     * @param mThirdDatas
     * @param firstpositon
     * @param secondposition
     * @param kind 二级目录or三级目录
     */
    public TextAdapter(Context context, String[][][] mThirdDatas, int firstpositon, int secondposition, int kind) {
        
        this.mContext = context;
        mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.mThirdDatas = mThirdDatas;
        this.mFirstPosition = firstpositon;
        this.mSecondPosition = secondposition;
        this.mKind = kind;
    }


    @Override
    public int getCount() {
        
        int result = 0;
        if(mKind == 2 && mSecondDatas != null){
            
            result = mSecondDatas.length;
        }else if(mKind == 3 && mThirdDatas != null){
            
            result = mThirdDatas[mFirstPosition][mSecondPosition].length;
        }
        return result;
    }

    @Override
    public String getItem(int position) {
        
        String result = "";
        if(mKind == 2 && mSecondDatas != null){
            
            result = mSecondDatas[mFirstPosition][position];
        }else if(mKind == 3 && mThirdDatas != null){
            
            result = mThirdDatas[mFirstPosition][mSecondPosition][position];
        }
        return result;
    }

    @Override
    public long getItemId(int position) {
        
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        
        ViewHolder viewHolder = new ViewHolder();
        if (convertView == null) {
            
            convertView = mLayoutInflater.inflate(R.layout.common_linkagelist_view_mylist_item, parent, false);
            viewHolder.textView = (TextView) convertView.findViewById(R.id.textView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        if(mKind == 2){  //二级目录
            
            viewHolder.textView.setText(mSecondDatas[mFirstPosition][position]);
        }else if(mKind == 3){  //三级目录
            
            viewHolder.textView.setText(mThirdDatas[mFirstPosition][mSecondPosition][position]);
        }
        viewHolder.textView.setTextColor(Color.BLACK);
        return convertView;
    }

    public static class ViewHolder {
        public TextView textView;
    }

    /**
     * 设置类别（2、3级目录）
     * @param mKind the mKind to set
     */
    public void setmKind(int mKind) {
        this.mKind = mKind;
    }
}