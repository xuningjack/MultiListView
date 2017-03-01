package multilistview.sortlist.adapter;

import java.util.List;

import multilistview.sortlist.vo.BaseFilterEntity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.SectionIndexer;
import android.widget.TextView;

import com.example.multilistview.R;

/**
 * SideBar需要使用的adapter
 * @author Jack
 * @date: 2014-10-28 下午2:04:36
 */
public class SortAdapter extends BaseAdapter implements SectionIndexer {

    private List<BaseFilterEntity> list = null;
    private Context mContext;

    public SortAdapter(Context mContext, List<BaseFilterEntity> list) {
        this.mContext = mContext;
        this.list = list;
    }

    /**
     * 当ListView数据发生变化时,调用此方法来更新ListView
     * 
     * @param list
     */
    public void updateListView(List<BaseFilterEntity> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    public int getCount() {
        return list.size();
    }

    public Object getItem(int position) {
        return list.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(final int position, View view, ViewGroup parent) {
        ViewHolder viewHolder = null;
        final BaseFilterEntity mContent = list.get(position);
        if (view == null) {
            viewHolder = new ViewHolder();
            view = LayoutInflater.from(mContext).inflate(R.layout.sort_item, parent, false);
            viewHolder.tvTitle = (TextView) view.findViewById(R.id.title);
            viewHolder.tvLetter = (TextView) view.findViewById(R.id.catalog);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        //根据position获取分类的首字母的char ascii值  
        int section = getSectionForPosition(position);

        //如果当前位置等于该分类首字母的Char的位置 ，则认为是第一次出现  
        if (position == getPositionForSection(section)) { //中文比对
            viewHolder.tvLetter.setVisibility(View.VISIBLE);
            viewHolder.tvLetter.setText(mContent.getSortLetter());
        } else {
            viewHolder.tvLetter.setVisibility(View.GONE);
        }
        viewHolder.tvTitle.setText(this.list.get(position).getmName());
        return view;
    }

    final static class ViewHolder {
        /**首字母的textview*/
        TextView tvTitle;
        /**显示具体内容的textview*/
        TextView tvLetter;
    }

    /**
     * 根据ListView的当前位置获取分类的首字母的char ascii值
     */
    public int getSectionForPosition(int position) {

        return list.get(position).getSortLetter().charAt(0);
    }

    /**
     * 根据分类的首字母的Char ascii值获取其第一次出现该首字母的位置
     */
    public int getPositionForSection(int section) {

        for (int i = 0; i < getCount(); i++) {

            String sortStr = list.get(i).getSortLetter(); //需要区分中文
            int firstChar = sortStr.toUpperCase().charAt(0);
            if (firstChar == section) {

                return i;
            }
        }
        return -1;
    }

    @Override
    public Object[] getSections() {
        return null;
    }
}