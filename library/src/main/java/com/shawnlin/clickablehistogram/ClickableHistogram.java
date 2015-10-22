package com.shawnlin.clickablehistogram;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.widget.LinearLayout;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ShawnLin on 15/9/27.
 * 用于显示统计数据的柱状图,在点击数据条时,可以触发点击事件,并做出相应的操作
 */
public class ClickableHistogram extends LinearLayout {
    private static final String TAG = ClickableHistogram.class.getSimpleName();

    private static final int DRAW_X_LINE = 0;
    private static final int START_COLUMNS_ANIMATION = 1;

    private int width, height; //the width and height of this view.
    private int columnWidth, columnMargin, axisWidth, textSize; //dimensions
    private int columnColor, axisColor, textColor; //colors
    private float lineEndX, moveX;
    private float columnMaxHeight; //图列最大高度,为控件高度的0.8倍
    private float expMaxHeight; //图列期望最大高度,由输入数据源中最大的数决定
    /**
     * 输入数据源,控件根据数据源初始化X轴长度和图列
     */
    private List<ColumnData> dataSource = new ArrayList<>();
    /**
     * 实际的图列控件
     */
    private List<ChartColumn> columns = new ArrayList<>();

    WeakReference<Handler> wr = new WeakReference<Handler>(new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case DRAW_X_LINE:
                    if (moveX < lineEndX) {
                        moveX += lineEndX / 50;
                        invalidate();
                        this.sendEmptyMessageDelayed(0, 10);
                    }
                    break;
                case START_COLUMNS_ANIMATION:
                    drawColumns();
                    break;
            }
        }
    });
    Paint linePaint = new Paint();

    public ClickableHistogram(Context context) {
        super(context);
        //由于容器控件默认为不绘制背景.所以需要将此项重设,使得onDraw()方法可以执行,以保证X轴得以绘制.
        setWillNotDraw(false);
    }

    /*读取用户设定的属性*/
    public ClickableHistogram(Context context, AttributeSet attrs) {
        super(context, attrs);
        setWillNotDraw(false);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.ClickableHistogram);
        columnWidth = typedArray.getDimensionPixelSize(R.styleable.ClickableHistogram_columnWidth, 75);
        columnMargin = typedArray.getDimensionPixelSize(R.styleable.ClickableHistogram_columnMargin, 30);
        axisWidth = typedArray.getDimensionPixelSize(R.styleable.ClickableHistogram_axisWidth, 5);
        textSize = typedArray.getDimensionPixelSize(R.styleable.ClickableHistogram_textSize, 20);
        columnColor = typedArray.getColor(R.styleable.ClickableHistogram_columnColor,
                getResources().getColor(R.color.colorPrimary));
        axisColor = typedArray.getColor(R.styleable.ClickableHistogram_axisColor,
                getResources().getColor(R.color.black));
        textColor = typedArray.getColor(R.styleable.ClickableHistogram_textColor,
                getResources().getColor(R.color.black));
        typedArray.recycle();

    }
    /**
     * 在此方法中获取到控件的宽高
     */
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        this.width = w;
        this.height = h;
        columnMaxHeight = h * 0.8f;

        lineEndX = width;
        moveX = 0;
        Handler handler = wr.get();
        handler.sendEmptyMessage(DRAW_X_LINE);
        handler.sendEmptyMessage(START_COLUMNS_ANIMATION);
        //set child position, upon x line
        this.setPadding(getPaddingLeft(), getPaddingTop(), getPaddingRight(), getPaddingBottom() + textSize + axisWidth);
        //initialize X line paint
        linePaint.setAntiAlias(true);
        linePaint.setColor(axisColor);
        linePaint.setStyle(Paint.Style.FILL);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        Log.e(TAG, "widthMeasureSpec = " + widthMeasureSpec + ", height = " + height);
        if (width == 0) {
            width = columns.size() * (columnWidth + 2 * columnMargin);
        }
        if (height == 0) {
            height = 100;
        }
        setMeasuredDimension(width, height);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Log.d(TAG, "onDraw");
        drawLine(canvas);
        if (moveX >= lineEndX) {
            drawText(canvas);
        }
    }

    /**
     * 绘制图表X轴
     */
    private void drawLine(Canvas canvas) {
        //set columns align chart bottom
        this.setGravity(Gravity.BOTTOM);
        //draw x line, the x line final length depends on the number of columns from the data source
        canvas.drawLine(0, height - textSize - axisWidth, moveX, height - textSize - axisWidth, linePaint);
    }

    /**
     * 绘制图列
     */
    private void drawColumns() {
        for (int i = 0; i < columns.size(); i++) {
            //向容器中添加控件
            int columnHeight = getColumnHeight(dataSource.get(i).getValue());
            Log.d(TAG, "columnHeight = " + columnHeight);
            ChartColumn column = columns.get(i);
            column.setColumnHeight(columnHeight);
            column.startAnimation(1000);
        }
    }

    /**
     * 绘制文字
     */
    private void drawText(Canvas canvas) {
        Paint textPaint = new Paint();
        textPaint.setColor(textColor);
        textPaint.setTextSize(textSize);
        textPaint.setTextAlign(Paint.Align.CENTER);
        Paint.FontMetricsInt fontMetrics = textPaint.getFontMetricsInt();
        int textXPos = 0;
        for (int i = 0; i < columns.size(); i++) {
            int columnHeight = columns.get(i).getHeight();
            //绘制图列名
            int textRectWidth = textXPos + columnWidth + 2 * columnMargin;
            Rect nameRect = new Rect(textXPos, height - textSize, textRectWidth, height);
            int baseline = nameRect.bottom - fontMetrics.bottom;
            String name = getOmitString(dataSource.get(i).getName(), textPaint, columnWidth + 2 * columnMargin);
            canvas.drawText(name, nameRect.centerX(), baseline, textPaint);
            //绘制图列值
            int valueTextBottom = height - textSize - axisWidth - columnHeight - 5;
            Rect valueRect = new Rect(textXPos, valueTextBottom - textSize, textRectWidth, valueTextBottom);
            baseline = valueRect.bottom - fontMetrics.bottom;
            String value = getOmitString(String.valueOf(dataSource.get(i).getValue()), textPaint, columnWidth + 2 * columnMargin);
            canvas.drawText(value, valueRect.centerX(), baseline, textPaint);

            textXPos = textXPos + nameRect.width();
        }
    }

    /**
     * 取得简略字符串,将超出指定最大长度的部分省略
     */
    public String getOmitString(String str, Paint paint, float maxLength) {
        String result = str.charAt(0) + "...";
        float textLength = paint.measureText(str);
        if (textLength > maxLength) {
            int i = str.length() - 1;
            while (paint.measureText(str, 0, i) > maxLength) {
                i--;
            }
            if (i > 3) {
                result = str.substring(0, i - 3) + "...";
            }
            return result;
        } else {
            return str;
        }
    }

    /**
     * 设置数据源并初始化柱状图
     */
    public void setDataSource(List<ColumnData> dataSource) {
        this.dataSource = dataSource;
        this.removeAllViews();
        for (ColumnData data : dataSource) {
            //求出图列期望最大高度
            float temp = data.getValue();
            if (temp > expMaxHeight) {
                expMaxHeight = temp;
            }
            //向表容器中添加图列控件
            ChartColumn column = new ChartColumn(getContext(), columnWidth, columnMargin, columnColor);
            columns.add(column);
            this.addView(column.getView());
        }
        this.invalidate();
    }

    /**
     * 为图列设置OnClickLister
     * */
    public void setColumnOnClickListener(OnClickListener listener) {
        for(ChartColumn column : columns) {
            column.getView().setOnClickListener(listener);
        }
    }
    /**
     * 根据输入数据值计算图列高度
     */
    private int getColumnHeight(float value) {
        Log.d(TAG, "value = " + value + " columnMaxHeight = " + columnMaxHeight + " expMaxHeight = " + expMaxHeight);
        return (int) (value * columnMaxHeight / expMaxHeight);
    }

    /**
     * 柱状图图列数据
     */
    public class ColumnData {
        private String name; //列名
        private float value; //列值

        public void setName(String name) {
            this.name = name;
        }

        public void setValue(float value) {
            this.value = value;
        }

        public String getName() {
            return name;
        }

        public float getValue() {
            return value;
        }
    }
}
