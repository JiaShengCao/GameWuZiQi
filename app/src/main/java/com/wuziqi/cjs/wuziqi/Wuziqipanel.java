package com.wuziqi.cjs.wuziqi;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.drawable.RippleDrawable;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by cjs on 2016/8/28.
 */
public class Wuziqipanel extends View {

    private int mPanelWidth;
    private float mLineHeight;
    private int MAX_LINE = 10;
    private int MAX_COUNT_IN_LINE=5;

    private Paint mPaint = new Paint();

    private float radioPieceOfLineHeight = 3 * 1.0f / 4;

    private boolean mIsGameOver;
    private boolean mIsWhiteWinner;

    private Bitmap mWhitePiece;//白色棋子
    private Bitmap mblackPiece;//黑色棋子

    private boolean mIsWhite = true;//该哪个棋子下
    private ArrayList<Point> mWhiteArray = new ArrayList<>();
    private ArrayList<Point> mBlackArray = new ArrayList<>();

    public Wuziqipanel(Context context, AttributeSet attrs) {
        super(context, attrs);
        initpaint();
    }

    private void initpaint() {
        mPaint.setColor(0x88000000);
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        mPaint.setStyle(Paint.Style.STROKE);

        mWhitePiece = BitmapFactory.decodeResource(getResources(), R.drawable.stone_w2);
        mblackPiece = BitmapFactory.decodeResource(getResources(), R.drawable.stone_b1);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        //设置为正方形
        int widthsize = MeasureSpec.getSize(widthMeasureSpec);
        int widthmodel = MeasureSpec.getMode(widthMeasureSpec);

        int heightsize = MeasureSpec.getSize(heightMeasureSpec);
        int heighmodel = MeasureSpec.getMode(heightMeasureSpec);

        int width = Math.min(widthsize, heightsize);

        if (widthmodel == MeasureSpec.UNSPECIFIED) {
            width = heightsize;
        } else if (heighmodel == MeasureSpec.UNSPECIFIED) {
            width = widthsize;
        }

        setMeasuredDimension(width, width);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mPanelWidth = w;
        mLineHeight = mPanelWidth * 1.0f / MAX_LINE;
        int pieceWidth = (int) (mLineHeight * radioPieceOfLineHeight);

        //更改棋子大小
        mWhitePiece = Bitmap.createScaledBitmap(mWhitePiece, pieceWidth, pieceWidth, false);
        mblackPiece = Bitmap.createScaledBitmap(mblackPiece, pieceWidth, pieceWidth, false);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        drawBoard(canvas);
        drawPieces(canvas);
        checkGameOver();
    }

    private void checkGameOver() {
        boolean whiteWin=checkFiveInLine(mWhiteArray);
        boolean blackWin=checkFiveInLine(mBlackArray);
        if (whiteWin||blackWin){
            mIsGameOver=true;
            mIsWhiteWinner=whiteWin;

            String text=mIsWhiteWinner?"白棋胜利":"黑棋胜利";

            Toast.makeText(getContext(),text,Toast.LENGTH_SHORT).show();
        }
    }

    private boolean checkFiveInLine(List<Point> points) {
        for (Point p:points){
            int x=p.x;
            int y=p.y;

            boolean win = checkHorizontal(x,y,points);
            if (win)return true;
            win = checkVertical(x,y,points);
            if (win)return true;
            win = checkLeftDiagonal(x,y,points);
            if (win)return true;
            win = checkRightDiagonal(x,y,points);
            if (win)return true;
        }

        return false;
    }
    //判断x，y位置的棋子是否横向有相邻的五个位置
    private boolean checkHorizontal(int x, int y, List<Point> points) {
        int count=1;
        for (int i=1;i<MAX_COUNT_IN_LINE;i++){
            if (points.contains(new Point(x-i,y))){
                count++;
            }else {
                break;
            }
        }
        if (count==MAX_COUNT_IN_LINE)
            return true;
        for (int i=1;i<MAX_COUNT_IN_LINE;i++){
            if (points.contains(new Point(x+i,y))){
                count++;
            }else {
                break;
            }
        }
        if (count==MAX_COUNT_IN_LINE)
            return true;
        return false;
    }
    //判断x，y位置的棋子是否纵向有相邻的五个位置
    private boolean checkVertical(int x, int y, List<Point> points) {
        int count=1;
        for (int i=1;i<MAX_COUNT_IN_LINE;i++){
            if (points.contains(new Point(x,y-i))){
                count++;
            }else {
                break;
            }
        }
        if (count==MAX_COUNT_IN_LINE)
            return true;
        for (int i=1;i<MAX_COUNT_IN_LINE;i++){
            if (points.contains(new Point(x,y+i))){
                count++;
            }else {
                break;
            }
        }
        if (count==MAX_COUNT_IN_LINE)
            return true;
        return false;
    }
    //判断x，y位置的棋子是否左斜方向有相邻的五个位置
    private boolean checkLeftDiagonal(int x, int y, List<Point> points) {
        int count=1;
        for (int i=1;i<MAX_COUNT_IN_LINE;i++){
            if (points.contains(new Point(x-i,y+i))){
                count++;
            }else {
                break;
            }
        }
        if (count==MAX_COUNT_IN_LINE)
            return true;
        for (int i=1;i<MAX_COUNT_IN_LINE;i++){
            if (points.contains(new Point(x+i,y-i))){
                count++;
            }else {
                break;
            }
        }
        if (count==MAX_COUNT_IN_LINE)
            return true;
        return false;
    }
    //判断x，y位置的棋子是否左斜方向有相邻的五个位置
    private boolean checkRightDiagonal(int x, int y, List<Point> points) {
        int count=1;
        for (int i=1;i<MAX_COUNT_IN_LINE;i++){
            if (points.contains(new Point(x-i,y-i))){
                count++;
            }else {
                break;
            }
        }
        if (count==MAX_COUNT_IN_LINE)
            return true;
        for (int i=1;i<MAX_COUNT_IN_LINE;i++){
            if (points.contains(new Point(x+i,y+i))){
                count++;
            }else {
                break;
            }
        }
        if (count==MAX_COUNT_IN_LINE)
            return true;
        return false;
    }

    private void drawPieces(Canvas canvas) {
        for (int i = 0, n = mWhiteArray.size(); i < n; i++) {
            Point whitePoint = mWhiteArray.get(i);
            canvas.drawBitmap(mWhitePiece, (whitePoint.x + (1 - radioPieceOfLineHeight) / 2) * mLineHeight,
                    (whitePoint.y + (1 - radioPieceOfLineHeight) / 2) * mLineHeight,null);
        }

        for (int i = 0, n = mBlackArray.size(); i < n; i++) {
            Point blackpoint = mBlackArray.get(i);
            canvas.drawBitmap(mblackPiece, (blackpoint.x + (1 - radioPieceOfLineHeight) / 2) * mLineHeight,
                    (blackpoint.y + (1 - radioPieceOfLineHeight) / 2) * mLineHeight,null);
        }
    }

    private void drawBoard(Canvas canvas) {
        int w = mPanelWidth;
        float lineHeight = mLineHeight;

        for (int i = 0; i < MAX_LINE; i++) {
            int starrX = (int) (lineHeight / 2);
            int endX = (int) (w - lineHeight / 2);

            int y = (int) ((0.5 + i) * lineHeight);
            //画棋盘横竖线
            canvas.drawLine(starrX, y, endX, y, mPaint);
            canvas.drawLine(y, starrX, y, endX, mPaint);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (mIsGameOver)
            return false;
        int action = event.getAction();
        if (action == MotionEvent.ACTION_UP) {
            int x = (int) event.getX();
            int y = (int) event.getY();
            Point p = getValidPoint(x, y);

            if (mWhiteArray.contains(p) || mBlackArray.contains(p)) {
                return false;
            }

            if (mIsWhite) {
                mWhiteArray.add(p);
            } else {
                mBlackArray.add(p);
            }

            invalidate();
            mIsWhite = !mIsWhite;

        }
        return true;
    }

    private Point getValidPoint(int x, int y) {
        return new Point((int) (x / mLineHeight), (int) (y / mLineHeight));
    }

    private static final String INSTANCE="instance";
    private static final String INSTANCE_GAME_OVER="intstace_game_over";
    private static final String INSTANCE_WHITE_ARRAY="intstace_white_array";
    private static final String INSTANCE_BLACK_ARRAY="instance_black_array";

    @Override
    protected Parcelable onSaveInstanceState() {
        Bundle bundle=new Bundle();
        bundle.putParcelable(INSTANCE,super.onSaveInstanceState());
        bundle.putBoolean(INSTANCE_GAME_OVER,mIsGameOver);
        bundle.putParcelableArrayList(INSTANCE_WHITE_ARRAY,mWhiteArray);
        bundle.putParcelableArrayList(INSTANCE_BLACK_ARRAY,mBlackArray);
        return bundle;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        if (state instanceof Bundle){
            Bundle bundle=(Bundle)state;
            mIsGameOver=bundle.getBoolean(INSTANCE_GAME_OVER);
            mWhiteArray=bundle.getParcelableArrayList(INSTANCE_WHITE_ARRAY);
            mBlackArray=bundle.getParcelableArrayList(INSTANCE_BLACK_ARRAY);
            super.onRestoreInstanceState(bundle.getParcelable(INSTANCE));
            return;
        }
        super.onRestoreInstanceState(state);
    }
    public void start (){
        mWhiteArray.clear();
        mBlackArray.clear();
        mIsGameOver=false;
        mIsWhiteWinner=false;
        invalidate();
    }
}
