package multilistview;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import multilistview.adapter.TextAdapter;
import multilistview.listener.IChangedViewListener;
import multilistview.sortlist.IMSideBar;
import multilistview.sortlist.adapter.SortAdapter;
import multilistview.sortlist.utils.CharacterParser;
import multilistview.sortlist.utils.PinyinComparator;
import multilistview.sortlist.vo.BaseFilterEntity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.example.multilistview.R;


/**
 * 支持2级/3级数据源，联动的ListView集合；在一级目录下支持sidebar的滑动选择定位；
 * 使用流程：
 *    (1)获得实例
 *    (2)设置二级、三级目录的数据源，调用setmFirstData，setmSecondData，setmThirdData
 *    (3)设置级别，调用setmLevel方法
 * @author Jack
 * @date: 2014-10-27 下午5:22:08
 */
public class LinkageListView extends LinearLayout {

    /** 上下文对象 */
    private Context mContext;
    /** 一级目录 */
    private ListView mFirstListView;
    /** 二级目录 */
    private ListView mSecondListView;
    /** 三级目录 */
    private ListView mThirdListView;
    /**已经选择的第一个列表的位置*/
    private int mSelectFirstPosition = -1;
    /**已经选择的第二个列表的位置*/
    private int mSelectSecondPosition = -1;
    /**已经选择的第三个列表的位置*/
    private int mSelectThirdPosition = -1;
    
    /** 一级目录的adapter */
    private SortAdapter mFirstAdapter;
    /** 二级目录的adapter */
    private TextAdapter mSecondAdapter;
    /** 三级目录的adapter */
    private TextAdapter mThirdAdapter;
    /** 一级目录的数据源 */
    private String[] mFirstData;
    /** 二级目录的数据源 */
    private String[][] mSecondData;
    /** 三级目录的数据源 */
    private String[][][] mThirdData;
    /** 层级，即列表集合的数据源层级数， 默认2级。现在最多支持3级。 */
    private int mLevel = 2;
    /**右侧的sidebar*/
    private IMSideBar mSideBar;
    /**  根据拼音来排列ListView里面的数据类 */  
    private PinyinComparator mPinyinComparator;  
    /**回调时用的listener*/
    private IChangedViewListener mListener;
    /**标记是否要加载动画*/
    private boolean mTag;
    /**排序时需要使用的拼音*/
    private CharacterParser mParser;
    

    /**
     * 重新绘制布局视图
     * @author Jack
     */
    private void reLayout(int level) {

        if (level == 2) { //2级目录

            mThirdListView.setVisibility(View.GONE);
            mFirstListView.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 4));
            mSecondListView.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 6));
        } else if (level == 3) { //3级目录

            if (mThirdListView.getVisibility() != View.VISIBLE) {

                mThirdListView.setVisibility(View.VISIBLE);
            }
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 2);
            mFirstListView.setLayoutParams(params);
            mSecondListView.setLayoutParams(params);
            mThirdListView.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 6));
            showListViewDynamic(mContext, mThirdListView);
        }
    }

    /**
     * 设置层级，初始化时需要设置
     * 
     * @param mLevel  the mLevel to set
     */
    public void setmLevel(int level) {
        this.mLevel = level;
        mParser = CharacterParser.getInstance();
        mPinyinComparator = new PinyinComparator();
        initView(mContext, getFakeData(mFirstData));
    }
    
    /**
     * 在一级列表的头部添加说明文字
     * @param head 说明文字
     */
    public void addFirstHeader(Context context, String head){
    	
    	TextView headerView = new TextView(context);
    	headerView.setText(head);
    	headerView.setGravity(Gravity.LEFT);
    	headerView.setTextSize(16);
    	mFirstListView.addHeaderView(headerView);
    }
    
    /**
     * 获得临时的排序列表
     * @author Jack
     * @param data
     * @return
     */
    public List<BaseFilterEntity> getFakeData(String[] data){
        
    	 List<BaseFilterEntity> list = new ArrayList<BaseFilterEntity>();
         for(String item : data){
             
             BaseFilterEntity model = new BaseFilterEntity();
             model.setmName(item);
             
             //汉字转拼音
             String pinyin = mParser.getSelling(item);
             String sortString = pinyin.substring(0, 1).toUpperCase(Locale.CHINA);
             
             // 正则表达式，判断首字母是否是英文字母  
             if(sortString.matches("[A-Z]")){
                 
                 model.setSortLetter(sortString.toUpperCase(Locale.CHINA));
             }else{
                 
                 model.setSortLetter("#");
             }
             list.add(model);
         }
         return list;
    }

    /**
     * 查找UI控件
     * @author Jack
     * @param context
     */
    private void findView(Context context) {

        View mView = LayoutInflater.from(context).inflate(R.layout.common_linkagelist_view, this);
        mFirstListView = (ListView) mView.findViewById(R.id.firstListView);
        mSecondListView = (ListView) mView.findViewById(R.id.secondListView);
        mThirdListView = (ListView) mView.findViewById(R.id.thirdListView);
        mSideBar = (IMSideBar)mView.findViewById(R.id.sidebar);
        //设置sidebar的背景色
        mSideBar.setBackgroundColor(getResources().getColor(android.R.color.white));
        mFirstListView.setSelector(android.R.color.transparent);
        mSecondListView.setSelector(android.R.color.transparent);
        mThirdListView.setSelector(android.R.color.transparent);
    }

    /**
     * 初始化UI
     * @author Jack
     * @param context
     */
    private void initView(Context context, List<BaseFilterEntity> list) {

        Collections.sort(list, mPinyinComparator);  
        mFirstAdapter = new SortAdapter(context, list);
        mFirstListView.setAdapter(mFirstAdapter);
        mFirstListView.setDivider(null);
        selectDefult(mContext);
        mFirstListView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, final int position, long arg3) {

                final int firstPosition = position;
                if(mSideBar.getVisibility() == View.VISIBLE){  //单击一级目录的时候就隐藏SideBar
                	
                	mSideBar.setVisibility(View.GONE);
                }
                if (mSecondListView.getVisibility() != View.VISIBLE) {

                    mSecondListView.setVisibility(View.VISIBLE);
                    mSecondListView.setDivider(null);
                }
                
                if(mSelectFirstPosition != -1){
                    
                    mFirstListView.getChildAt(mSelectFirstPosition).findViewById(R.id.title).setBackgroundColor(mContext.getResources().getColor(android.R.color.white));
                }
                mFirstListView.getChildAt(position).findViewById(R.id.title).setBackgroundColor(mContext.getResources().getColor(R.color.sort_list_selected));
                mSelectFirstPosition = position;
                mSecondAdapter = new TextAdapter(mContext, mSecondData, position, 2);
                mSecondListView.setAdapter(mSecondAdapter);
                showListViewDynamic(mContext, mSecondListView); //动态展示二级目录
                
                
                mSecondListView.setOnItemClickListener(new OnItemClickListener() {

                    @Override
                    public void onItemClick(AdapterView<?> arg0, View arg1, final int secondPosition, long arg3) {

                        if(mSelectSecondPosition != -1){
                            mSecondListView.getChildAt(mSelectSecondPosition).findViewById(R.id.textView).setBackgroundColor(mContext.getResources().getColor(android.R.color.white));
                        }
                        mSecondListView.getChildAt(secondPosition).findViewById(R.id.textView).setBackgroundColor(mContext.getResources().getColor(R.color.sort_list_selected));
                        mSelectSecondPosition = secondPosition;
                        
                    	 if(mSideBar.getVisibility() == View.VISIBLE){
                         	
                         	mSideBar.setVisibility(View.GONE);
                         }
                        if (mLevel == 2) { //二级目录的设置

                            reLayout(2);
                            if (mListener != null) {

                                mListener.changeView("二级hello\t" + mSecondData[firstPosition][secondPosition]);
                            }
                        } else if (mLevel == 3) { //三级目录的设置

                            reLayout(3);
                            produceThirdData(4);
                            mThirdListView.setDivider(null);
                            mThirdListView.setOnItemClickListener(new OnItemClickListener() {

                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int thirdPosition, long id) {

                                    mListener.changeView("三级 hello\t" + mThirdData[firstPosition][secondPosition][thirdPosition]);
                                }
                            });
                        }
                    }
                });
            }
        });
        
        mSideBar.setOnTouchingLetterChangedListener(new IMSideBar.OnTouchingLetterChangedListener() {  //单击sidebar后联动listview
            
            @Override
            public void onTouchingLetterChanged(String s) {

                int position = mFirstAdapter.getPositionForSection(s.charAt(0));
                if(position != -1){
                    
                    mFirstListView.setSelection(position);
                }
            }
        });
    }

    /**
     * 产生3级列表数据
     * @author Jack
     */
    public void produceThirdData(int num) { //TODO  需要外部传入3级目录数据

        mThirdData = new String[mFirstData.length][mSecondData.length][num];
        for (int i = 0; i < mFirstData.length; i++) {

            for (int j = 0; j < mSecondData.length; j++) {

                for (int k = 0; k < num; k++) {

                    String data = String.valueOf(i + "\t" + j + "\t" + k);
                    mThirdData[i][j][k] = data;
                }
                mThirdAdapter = new TextAdapter(mContext, mThirdData, i, j, 3);
                mThirdListView.setAdapter(mThirdAdapter);
            }
        }
    }

    /**
     * Creates a new instance of LinkageListView.
     * @param context
     */
    public LinkageListView(Context context) {
        super(context);
        mContext = context;
        findView(context);
    }

    /**
     * Creates a new instance of LinkageListView.
     * @param context
     * @param attrs
     */
    public LinkageListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        findView(context);
    }

    /**
     * 动态展示ListView，效果从右向左划入一大半
     * @author Jack
     * @param listView
     */
    public void showListViewDynamic(Context context, ListView listView) {

    	if(!mTag){  //只有第一次时显示动画
    		
    		  Animation animation = AnimationUtils.loadAnimation(context, R.anim.slide_in_from_right_large_half);
    	      listView.setAnimation(animation);
    	      mTag = true;
    	}
    }

    /**
     * 初始化，默认是2级目录结构
     * @author Jack
     * @param context
     */
    private void selectDefult(Context context) {

        mFirstAdapter.notifyDataSetInvalidated();
        mSecondAdapter = new TextAdapter(mContext, mSecondData, 0, 2);
        mSecondListView.setAdapter(mSecondAdapter);
    }

    /**
     * 设置一级目录的数据源
     * @param mFirstData the mFirstData to set
     */
    public void setmFirstData(String[] mFirstData) {
        this.mFirstData = mFirstData;
    }

    /**
     * 设置二级数据的数据源
     * @param mSecondData the mSecondData to set
     */
    public void setmSecondData(String[][] mSecondData) {
        this.mSecondData = mSecondData;
    }

    /**
     * @param mListener  the mListener to set
     */
    public void setmListener(IChangedViewListener mListener) {
        this.mListener = mListener;
    }

	/**
	 * @param mThirdData the mThirdData to set
	 */
	public void setmThirdData(String[][][] mThirdData) {
		this.mThirdData = mThirdData;
	}
}