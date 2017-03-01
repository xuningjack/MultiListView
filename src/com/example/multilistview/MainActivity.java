package com.example.multilistview;

import multilistview.LinkageListView;
import multilistview.listener.IChangedViewListener;
import android.app.Activity;
import android.os.Bundle;
import android.widget.Toast;

/**
 * 二级展示数据页面 
 * @author Jack
 * @date: 2014-10-27 下午6:25:14
 */
public class MainActivity extends Activity {

    private LinkageListView mLinkageListView;
    String firstData[] =new String []{"全部频道","好吃的","Fun","Shopping","WineShop","Beauty","Sport","Marry","Child","Car","Service"};
    String secondData[][] = new String[][] {
            new String[] {"全部好吃的", "本帮江浙菜", "川菜", "粤菜", "湘菜","东北菜","台湾菜","新疆/清真","素菜","火锅","自助餐","小吃快餐","日本","韩国料理",
            "东南亚菜","西餐","面包甜点","其他"},
            new String[] {"全部Fun","咖啡厅","酒吧","茶馆","KTV","电影院","游乐游艺","公园","景点/郊游","洗浴","足浴按摩","文化艺术",
                "DIY手工坊","桌球馆","桌面游戏","更多Fun"},
            new String[] {"全部Shopping", "综合商场", "服饰鞋包", "运动户外","珠宝饰品","化妆品","数码家电","ChildShopping","家居建材"
                ,"书店","书店","眼镜店","特色集市","更多Shopping场所","食品茶酒","超市/便利店","药店"},
            new String[] {"全部Fun","咖啡厅","酒吧","茶馆","KTV","电影院","游乐游艺","公园","景点/郊游","洗浴","足浴按摩","文化艺术",
                    "DIY手工坊","桌球馆","桌面游戏","更多Fun"},
            new String[] {"全","咖啡厅","酒吧","茶馆","KTV","游乐游艺","公园","景点/郊游","洗浴","足浴按摩","文化艺术",
                    "DIY手工坊","桌球馆","桌面游戏","更多Fun"},
            new String[] {"全部","咖啡厅","酒吧","茶馆","电影院","游乐游艺","公园","景点/郊游","洗浴","足浴按摩","文化艺术",
                    "DIY手工坊","桌球馆","桌面游戏","更多Fun"},
            new String[] {"全部休","咖啡厅","酒吧","茶馆","KTV","电影院","游乐游艺","公园","景点/郊游","洗浴","足浴按摩","文化艺术",
                    "DIY手工坊","桌球馆","桌面游戏","更多Fun"},
            new String[] {"全部休闲","咖啡厅","酒吧","茶馆","KTV","电影院","游乐游艺","公园","景点/郊游","洗浴","足浴按摩","文化艺术",
                    "DIY手工坊","桌球馆","桌面游戏","更多Fun"},
            new String[] {"全部休闲娱","咖啡厅","酒吧","茶馆","KTV","电影院","游乐游艺","公园","景点/郊游","洗浴","足浴按摩","文化艺术",
                    "DIY手工坊","桌球馆","桌面游戏"},
            new String[] {"全部Fun","咖啡厅","酒吧","茶馆","KTV","电影院","游乐游艺","公园","景点/郊游","洗浴","足浴按摩","文化艺术",
                    "DIY手工坊","桌球馆","桌面游戏","更多Fun"},
            new String[] {"全部休闲aaa","咖啡厅","酒吧","茶馆","KTV","电影院","游乐游艺","公园","景点/郊游","洗浴","足浴按摩","文化艺术",
                    "DIY手工坊","桌球馆","桌面游戏"},
            };
    
    /**
     * @see android.app.Activity#onCreate(android.os.Bundle)
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        mLinkageListView = (LinkageListView)findViewById(R.id.linkageListView);
        mLinkageListView.setmFirstData(firstData);
        mLinkageListView.setmSecondData(secondData);
//        mLinkageListView.setmLevel(2); 
        mLinkageListView.setmLevel(3); 
        mLinkageListView.setmListener(new IChangedViewListener() {
            
            @Override
            public void changeView(String result) {
                
                Toast.makeText(MainActivity.this, result, Toast.LENGTH_LONG).show();
            }
        });
    }
}