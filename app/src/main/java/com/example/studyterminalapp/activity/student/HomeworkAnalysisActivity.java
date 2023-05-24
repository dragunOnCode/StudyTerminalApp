package com.example.studyterminalapp.activity.student;

import static android.app.PendingIntent.getActivity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.WindowManager;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.studyterminalapp.R;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.DefaultAxisValueFormatter;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.utils.ViewPortHandler;

import java.util.ArrayList;
import java.util.List;

public class HomeworkAnalysisActivity extends AppCompatActivity {
    private BarChart bc;
    private PieChart mPieChart;

    /**
     * 1.正确率（饼图）
     *
     * 2.自己得分/班级平均得分（TextView）
     *
     * 3.作答时间（柱状图）
     *
     * 4.错题知识点分布（饼图）
     *
     * 5.本大章中各小节分数统计（分组柱状图）
     * @param savedInstanceState
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_homework_analysis);

        initBarChart();
        initPieChart();
    }

    private void initBarChart() {
        bc = (BarChart) findViewById(R.id.bc_answer_time);
        Matrix matrix = new Matrix();
        //x轴缩放1.5倍
        matrix.postScale(1.5f, 1f);
        //在图表动画显示之前进行缩放
        bc.getViewPortHandler().refresh(matrix, bc, false);
        // x轴执行动画
        bc.animateX(2000);

        setDesc();
        setLegend();
        setXAxis();
        setYAxis();
        loadData();
    }

    private void initPieChart() {
        mPieChart = (PieChart) findViewById(R.id.pc_correct_rate);
        List<PieEntry> strings = new ArrayList<>();
        strings.add(new PieEntry(30f,"男生"));
        strings.add(new PieEntry(70f,"女生"));
        PieDataSet dataSet = new PieDataSet(strings,"");

        ArrayList<Integer> colors = new ArrayList<Integer>();
        colors.add(Color.RED);
        colors.add(Color.BLUE);
        dataSet.setColors(colors);

        PieData pieData = new PieData(dataSet);
        pieData.setDrawValues(true);
        pieData.setValueFormatter(new PercentFormatter(mPieChart));
        pieData.setValueTextSize(12f);
        pieData.setValueTextColor(Color.WHITE);

        Description description = new Description();
        description.setText("");
        mPieChart.setDescription(description);
        mPieChart.setHoleRadius(0f);
        mPieChart.setTransparentCircleRadius(0f);
        mPieChart.setUsePercentValues(true);
        mPieChart.setData(pieData);
        mPieChart.invalidate();
    }

    //设置Title
    private void setDesc() {
        Description description = bc.getDescription();

        // 获取屏幕中间x 轴的像素坐标
        WindowManager wm = (WindowManager)getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(dm);
        float x = dm.widthPixels / 2;

        description.setPosition(x,50);
        description.setText("年龄群体车辆违章的占比统计");
        description.setTextSize(16f);
        description.setTextColor(Color.BLACK);
        bc.setDescription(description);
    }

    //设置图例
    private void setLegend() {
        Legend legend = bc.getLegend();
        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        legend.setDrawInside(true);
        legend.setTextSize(14f);
        legend.setTypeface(Typeface.DEFAULT_BOLD);
        legend.setXOffset(30);
        legend.setOrientation(Legend.LegendOrientation.VERTICAL);
        legend.setTypeface(Typeface.DEFAULT_BOLD);
    }

    //设置Y轴
    private void setYAxis() {
        //不显示右侧的y轴
        bc.getAxisRight().setEnabled(false);
        //得到左侧的y轴对象
        YAxis yAxis = bc.getAxisLeft();
        yAxis.setTextSize(14f);
        yAxis.setTextColor(Color.BLACK);
        //设置y轴在x方向上的偏移量
        yAxis.setXOffset(15);
        yAxis.setAxisMaximum(800);
        yAxis.setAxisMinimum(0);
        yAxis.setGranularity(200f);
        yAxis.setLabelCount(7);
    }

    //设置X轴
    private void setXAxis() {
        XAxis xAxis = bc.getXAxis();
        //设置x轴是显示在顶部还是底部，柱状图内还是外
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setTextSize(16f);
        xAxis.setTypeface(Typeface.DEFAULT_BOLD);
        //是否绘制竖向的网格
        xAxis.setDrawGridLines(false);
        xAxis.setLabelCount(5);
        //设置最大值
        xAxis.setAxisMaximum(6.3f);
        //设置最小值，可通过该属性设置与左边的距离
        xAxis.setAxisMinimum(-0.3f);
        //设置间隔
        xAxis.setGranularity(1f);
        //自定义格式
        xAxis.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {

                int tep = (int) (9 - value);
                return tep + "0后";
            }
        });
    }

    //设置数据
    private void loadData() {
        //为了看到更明显的效果，我这里设置了图形在上下左右四个方向上的偏移量
        bc.setExtraOffsets(30,70,30,50);
        bc.animateXY(1000,1000);
        bc.setFitBars(true);
        final float d_1[] = {230f, 480f, 480f, 480f, 170f, 110f, 100f, 200f};
        final float d_2[] = {120f, 600f, 400f, 200f, 80f, 50f, 20f, 120f};
        List<BarEntry> entries = new ArrayList<BarEntry>();
        List<BarEntry> entries1 = new ArrayList<BarEntry>();

        for(int i = 0; i < d_1.length; i++){
            //使用BarEntry的构造方法的第三个参数来保存需要在柱块上显示的数据
            entries.add(new BarEntry(i,d_1[i],(d_1[i] / (d_1[i] + d_2[i])) * 100));
            entries1.add(new BarEntry(i,d_2[i],(d_2[i] / (d_1[i] + d_2[i])) * 100));
        }

        BarDataSet barDataSet = new BarDataSet(entries,"无违章");
        BarDataSet barDataSet1 = new BarDataSet(entries1,"有违章");

        //依次设置每次柱块的颜色,int... 类型
        barDataSet.setColor(Color.parseColor("#6D9C00"));
        //设置柱块上文字的格式
        barDataSet.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
                //获取entry中的数据
                float res = (float) entry.getData();
                return String.format("%.1f",res) + "%";
            }
        });


        barDataSet1.setColor(Color.parseColor("#F07408"));
        barDataSet1.setValueTextColor(Color.RED);
        barDataSet1.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
                //获取entry中的数据
                float res = (float) entry.getData();
                return String.format("%.1f",res) + "%";
            }
        });

        List<IBarDataSet> barDataSets = new ArrayList<IBarDataSet>();
        barDataSets.add(barDataSet);
        barDataSets.add(barDataSet1);

        BarData barData = new BarData(barDataSets);
        barData.setBarWidth(0.3f);//设置柱块的宽度
        //参数1：距左边的距离（开始会偏移一个组的距离）
        //参数二：组与组之间的间距
        //参数三：一组柱块之中每个之间的距离
        barData.groupBars(-0.35f,0.35f,0);

        //是否支持缩放
        bc.setScaleEnabled(true);

        bc.setData(barData);
    }
}