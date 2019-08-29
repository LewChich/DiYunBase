package com.example.mybase.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.CustomListener;
import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.example.mybase.R;
import com.google.gson.Gson;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Author  DLQ
 * Create on 2018/10/9
 * 选择器
 */

public class WheelPickerUtils {

    private OptionsPickerView mPickerView, pvNoLinkOptions;
    private int mCurrent = 0;
    private TimePickerListener mPickerListener;
    private OptionsPickerView pvOptions;
    private ArrayList<JsonBean> options1Items = new ArrayList<>();
    private ArrayList<ArrayList<String>> options2Items = new ArrayList<>();
    private ArrayList<ArrayList<ArrayList<String>>> options3Items = new ArrayList<>();
    private Thread thread;
    private static final int MSG_LOAD_DATA = 0x0001;
    private static final int MSG_LOAD_SUCCESS = 0x0002;
    private static final int MSG_LOAD_FAILED = 0x0003;
    private Context mContext;
    private boolean isLoaded = false;
    private AreaCallBack mCallBack;
    private int mLevelType;
    private String mtitle;


    public static final int LEVEL_ONE = 1;
    public static final int LEVEL_TWO = 2;
    public static final int LEVEL_THREE = 3;

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_LOAD_DATA:
                    if (thread == null) {//如果已创建就不再重新创建子线程了

                        thread = new Thread(new Runnable() {
                            @Override
                            public void run() {
                                // 子线程中解析省市区数据
                                initJsonData(mContext);
                            }
                        });
                        thread.start();
                    }
                    break;

                case MSG_LOAD_SUCCESS:
                    isLoaded = true;
                    if (TextUtils.isEmpty(mtitle)) {
                        mtitle = "城市选择";
                    }
                    showAreaPickerView(mContext, mtitle);
                    break;

                case MSG_LOAD_FAILED:
                    ToastUtils.show(mContext,"省市列表初始化失败",3);
                    break;
            }
        }
    };

    public static WheelPickerUtils getInstance() {
        return new WheelPickerUtils();
    }

    /**
     * 单项选择器
     *
     * @param context     上下文
     * @param pickerTitle 选择器title
     * @param list        选择器数据源
     * @param option      返回选择后的位置
     */
    public void initCustomOptionPicker(Context context, final String pickerTitle, ArrayList<WheelPickerBean> list, final CurrentOption option) {//条件选择器初始化，自定义布局


        /**
         * @description
         *
         * 注意事项：
         * 自定义布局中，id为 optionspicker 或者 timepicker 的布局以及其子控件必须要有，否则会报空指针。
         * 具体可参考demo 里面的两个自定义layout布局。
         */
        mPickerView = new OptionsPickerBuilder(context, new OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int option2, int options3, View v) {
                //返回的分别是三个级别的选中位置
//                String tx = cardItem.get(options1).getPickerViewText();
//                btn_CustomOptions.setText(tx);
                mCurrent = options1;
                option.onCurrentOption(mCurrent);
            }
        })
                .setLayoutRes(R.layout.pickerview_custom_options, new CustomListener() {
                    @Override
                    public void customLayout(View v) {
                        final TextView tvSubmit = (TextView) v.findViewById(R.id.wheel_finish);
                        TextView ivCancel = (TextView) v.findViewById(R.id.wheel_cancel);
                        TextView title = (TextView) v.findViewById(R.id.wheel_title);
                        title.setText(pickerTitle);
                        tvSubmit.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                mPickerView.returnData();
                                mPickerView.dismiss();
                            }
                        });

                        ivCancel.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                mPickerView.dismiss();
                            }
                        });


                    }
                })
                .setTextColorCenter(Color.BLACK)//设置选中项的颜色
                .build();

        mPickerView.setPicker(list);//添加数据
        mPickerView.show();

    }

//    /**
//     * 两列选择器
//     * @param context 上下文
//     * @param listOne 第一列数据
//     * @param listTwo 第二列数据
//     * @param currentTime 返回每列的选择结果
//     */
//    public void timePickerDialog(Context context, ArrayList<String> listOne, ArrayList<String> listTwo, final CurrentTime currentTime){
//        TimeDialog dialog = new TimeDialog(context,listOne,listTwo);
//        dialog.setListener(new TimeDialog.OnItemSelectorListener() {
//
//
//            @Override
//            public void onSelector(String fristLable, String twoLable) {
//                currentTime.onSelectorLable(fristLable,twoLable);
//            }
//        });
//        dialog.show();
//    }

    /**
     * 时间年月选择器
     *
     * @param context
     */
    public void showTimeWheelPicker(Context context, String title, final TimePickerListener listener) {

        mPickerListener = listener;
        Calendar selectedDate = Calendar.getInstance();
        Calendar startDate = Calendar.getInstance();
//        startDate.set(2018,1,1);
        Calendar endDate = Calendar.getInstance();
//        endDate.set(2030,1,1);

        //正确设置方式 原因：注意事项有说明
        startDate.set(2018, 0, 1);
        endDate.set(2025, 11, 31);

        new TimePickerBuilder(context, new OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {//选中事件回调
                listener.showTime(date);
            }
        })
                .setType(new boolean[]{true, true, false, false, false, false})// 默认全部显示
//                .setCancelText("Cancel")//取消按钮文字
                .setSubmitText("确认")//确认按钮文字
//                .setContentSize(15)//滚轮文字大小
                .setContentTextSize(15)
                .setTitleSize(8)//标题文字大小
                .setTitleText(title)//标题文字
                .setOutSideCancelable(true)//点击屏幕，点在控件外部范围时，是否取消显示
                .isCyclic(true)//是否循环滚动
                .setGravity(Gravity.CENTER)
//                .setTitleColor(Color.BLACK)//标题文字颜色
//                .setSubmitColor(R.color.font_ff33)//确定按钮文字颜色
//                .setCancelColor(Color.BLUE)//取消按钮文字颜色
//                .setTitleBgColor(R.color.white)//标题背景颜色 Night mode
//                .setBgColor(R.color.gray)//滚轮背景颜色 Night mode
//                .setDate(selectedDate)// 如果不设置的话，默认是系统时间*/
                .setRangDate(startDate, endDate)//起始终止年月日设定
                .setLabel("年", "月", "", "", "", "")//默认设置为年月日时分秒
                .isCenterLabel(true) //是否只显示中间选中项的label文字，false则每项item全部都带有label。
//                .isDialog(true)//是否显示为对话框样式
                .build()
                .show();
    }

    /**
     * 时间年月日选择器
     *
     * @param context
     */
    public void showTimeWheelPicker(Context context, String title, boolean flag, final TimePickerListener listener) {

        mPickerListener = listener;
        Calendar selectedDate = Calendar.getInstance();
        Calendar startDate = Calendar.getInstance();
//        startDate.set(2018,1,1);
        Calendar endDate = Calendar.getInstance();
//        endDate.set(2030,1,1);

        //正确设置方式 原因：注意事项有说明
//        startDate.set(2018,0,1);
//        endDate.set(2025,11,31);

        new TimePickerBuilder(context, new OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {//选中事件回调
                listener.showTime(date);
            }
        })
                .setType(new boolean[]{true, true, true, false, false, false})// 默认全部显示
                .setCancelText("")//取消按钮文字
                .setSubmitText("确认")//确认按钮文字
//                .setContentSize(15)//滚轮文字大小
                .setContentTextSize(15)
                .setTitleSize(17)//标题文字大小
                .setTitleText(title)//标题文字
                .setOutSideCancelable(true)//点击屏幕，点在控件外部范围时，是否取消显示
                .isCyclic(true)//是否循环滚动
                .setLineSpacingMultiplier(1.2f)
                .setGravity(Gravity.CENTER)
                .setTitleColor(Color.WHITE)//标题文字颜色
                .setSubmitColor(Color.WHITE)//确定按钮文字颜色
//                .setCancelColor(Color.BLUE)//取消按钮文字颜色
                .setTitleBgColor(Color.BLUE)//标题背景颜色 Night mode
//                .setBgColor(R.color.gray)//滚轮背景颜色 Night mode
//                .setDate(selectedDate)// 如果不设置的话，默认是系统时间*/
//                .setRangDate(startDate,endDate)//起始终止年月日设定
                .setLabel("年", "月", "日", "", "", "")//默认设置为年月日时分秒
                .isCenterLabel(true) //是否只显示中间选中项的label文字，false则每项item全部都带有label。
//                .isDialog(true)//是否显示为对话框样式
                .build()
                .show();


    }


    /**
     * 双向项选择器
     *
     * @param context     上下文
     * @param pickerTitle 选择器title
     * @param list        选择器数据源
     * @param option      返回选择后的位置
     */
    public void initOptionPicker(Context context, final String pickerTitle, ArrayList<WheelPickerBean> list, ArrayList<ArrayList<WheelPickerBean>> listOne, final CurrentOption option) {//条件选择器初始化，自定义布局


        /**
         *
         * 注意事项：
         * 自定义布局中，id为 optionspicker 或者 timepicker 的布局以及其子控件必须要有，否则会报空指针。
         * 具体可参考demo 里面的两个自定义layout布局。
         */
        mPickerView = new OptionsPickerBuilder(context, new OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int option2, int options3, View v) {
                //返回的分别是三个级别的选中位置
//                String tx = cardItem.get(options1).getPickerViewText();
//                btn_CustomOptions.setText(tx);
                mCurrent = options1;
                option.onCurrentOption(mCurrent);
            }
        })
                .setLayoutRes(R.layout.pickerview_custom_options, new CustomListener() {
                    @Override
                    public void customLayout(View v) {
                        final TextView tvSubmit = (TextView) v.findViewById(R.id.wheel_finish);
                        TextView ivCancel = (TextView) v.findViewById(R.id.wheel_cancel);
                        TextView title = (TextView) v.findViewById(R.id.wheel_title);
                        title.setText(pickerTitle);
                        tvSubmit.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                mPickerView.returnData();
                                mPickerView.dismiss();
                            }
                        });

                        ivCancel.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                mPickerView.dismiss();
                            }
                        });


                    }
                })
                .setTextColorCenter(Color.LTGRAY)//设置选中项的颜色
                .build();

        mPickerView.setPicker(list, listOne);//添加数据
        mPickerView.show();

    }

    /**
     * 城市选择列表
     *
     * @param context
     * @param callBack
     */
    public void initAreaData(Context context, int level, String title, final AreaCallBack callBack) {
        mtitle = title;
        mLevelType = level;
        mContext = context;
        mHandler.sendEmptyMessage(MSG_LOAD_DATA);
        mCallBack = callBack;
    }


    private void initJsonData(Context context) {//解析数据

        /**
         * 注意：assets 目录下的Json文件仅供参考，实际使用可自行替换文件
         * 关键逻辑在于循环体
         *
         * */
        String JsonData = new GetJsonDataUtil().getJson(context, "province.json");//获取assets目录下的json文件数据

        ArrayList<JsonBean> jsonBean = parseData(JsonData);//用Gson 转成实体

        /**
         * 添加省份数据
         *
         * 注意：如果是添加的JavaBean实体，则实体类需要实现 IPickerViewData 接口，
         * PickerView会通过getPickerViewText方法获取字符串显示出来。
         */
        options1Items = jsonBean;

        for (int i = 0; i < jsonBean.size(); i++) {//遍历省份
            ArrayList<String> CityList = new ArrayList<>();//该省的城市列表（第二级）
            ArrayList<ArrayList<String>> Province_AreaList = new ArrayList<>();//该省的所有地区列表（第三极）

            for (int c = 0; c < jsonBean.get(i).getCityList().size(); c++) {//遍历该省份的所有城市
                String CityName = jsonBean.get(i).getCityList().get(c).getName();
                CityList.add(CityName);//添加城市
                ArrayList<String> City_AreaList = new ArrayList<>();//该城市的所有地区列表

                //如果无地区数据，建议添加空字符串，防止数据为null 导致三个选项长度不匹配造成崩溃
                if (jsonBean.get(i).getCityList().get(c).getArea() == null
                        || jsonBean.get(i).getCityList().get(c).getArea().size() == 0) {
                    City_AreaList.add("");
                } else {
                    City_AreaList.addAll(jsonBean.get(i).getCityList().get(c).getArea());
                }
                Province_AreaList.add(City_AreaList);//添加该省所有地区数据
            }

            /**
             * 添加城市数据
             */
            options2Items.add(CityList);

            /**
             * 添加地区数据
             */
            options3Items.add(Province_AreaList);
        }

        mHandler.sendEmptyMessage(MSG_LOAD_SUCCESS);

    }

    public ArrayList<JsonBean> parseData(String result) {//Gson 解析
        ArrayList<JsonBean> detail = new ArrayList<>();
        try {
            JSONArray data = new JSONArray(result);
            Gson gson = new Gson();
            for (int i = 0; i < data.length(); i++) {
                JsonBean entity = gson.fromJson(data.optJSONObject(i).toString(), JsonBean.class);
                detail.add(entity);
            }
        } catch (Exception e) {
            e.printStackTrace();
            mHandler.sendEmptyMessage(MSG_LOAD_FAILED);
        }
        return detail;
    }


    public void destroy() {
        if (mHandler != null) {
            mHandler.removeCallbacksAndMessages(null);
        }
    }

    public void showAreaPickerView(Context context, String title) {// 弹出选择器

        OptionsPickerView pvOptions = new OptionsPickerBuilder(context, new OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                String are;
                switch (mLevelType) {
                    case LEVEL_ONE:
                        are = options1Items.get(options1).getPickerViewText();
                        mCallBack.areaInfo(are);
                        break;
                    case LEVEL_TWO:
                        are = options1Items.get(options1).getPickerViewText();
                        mCallBack.areaInfo(are, options2Items.get(options1).get(options2));
                        break;
                    case LEVEL_THREE:
                        are = options1Items.get(options1).getPickerViewText() +
                                options2Items.get(options1).get(options2) +
                                options3Items.get(options1).get(options2).get(options3);
                        mCallBack.areaInfo(are);
                        break;

                }

            }
        })

                .setTitleText(title)
                .setDividerColor(Color.GRAY)
                .setTextColorCenter(Color.BLACK) //设置选中项文字颜色
                .setContentTextSize(20)
                .build();


        switch (mLevelType) {
            case LEVEL_ONE:
                pvOptions.setPicker(options1Items);//一级选择器
                break;
            case LEVEL_TWO:
                pvOptions.setPicker(options1Items, options2Items);//二级选择器
                break;
            case LEVEL_THREE:
                pvOptions.setPicker(options1Items, options2Items, options3Items);//三级选择器
                break;
        }

        pvOptions.show();
    }

    public interface AreaCallBack {

        void areaInfo(String area);

        void areaInfo(String area, String s);

    }

    public interface TimePickerListener {

        void showTime(Date date);

    }

    public interface CurrentOption {

        void onCurrentOption(int current);

    }

    public interface CurrentTime {

        void onSelectorLable(String fristLable, String twoLable);

    }


}
