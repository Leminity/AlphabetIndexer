package com.tistory.leminity.alphabetindexer;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.util.regex.Pattern;

/**
 * Created by User on 2016-06-21.
 */
public class IndexerBar extends View {

    private static final String PATTERN = "^[A-Za-z]+$";

    private static final String HASH_MARK = "#";

    private static final int TEXT_SIZE = 10;

    private OnTouchingLetterChangedListener onTouchingLetterChangedListener;

    private String[] mIndexer = {};

    private int mChoose = -1;

    private Paint mPaint = new Paint();

    boolean mTouchPressed = false;

    public IndexerBar(Context context) {
        super(context);
    }

    public IndexerBar(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public IndexerBar(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void setIndexList(String[] indexArray) {
        mIndexer = indexArray;
        postInvalidate();
    }

    /********************************************************************************************************************
     * 그리기 관련 처리
     ********************************************************************************************************************/

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int     CANVAS_HALF_WIDTH           = getWidth() / 2;
        int     height          = getHeight();
        float   textSize        = getResources().getDisplayMetrics().density * TEXT_SIZE;
        int     singleHeight    = height / mIndexer.length;
        int     skipIdxCnt      = getskipIndexCountIfOverlap(mIndexer.length, (int)textSize);

        setBackgroundResource(mTouchPressed ? R.drawable.shape_round_rectangle_pressed : R.drawable.shape_round_rectangle_normal);

        for (int i = 0; i < mIndexer.length; i++) {
            float yPos = (singleHeight * i) + singleHeight;

            if(isShowDotInsteadAlphabet(mIndexer.length, i, skipIdxCnt)) {
                drawDot(canvas, i, CANVAS_HALF_WIDTH, yPos - (singleHeight / 2));
            } else {
                drawAlphabet(canvas, textSize, i, CANVAS_HALF_WIDTH, yPos);
            }
        }
    }

    private void drawDot(Canvas canvas, int i, float canvasHalfWidth, float yPos) {
        final int RADIUS_DOT = 4;

        mPaint.setColor(Color.GRAY);
        mPaint.setAntiAlias(true);
        if (i == mChoose) {
            mPaint.setColor(ContextCompat.getColor(getContext(), android.R.color.holo_blue_light));
        }

        canvas.drawCircle(canvasHalfWidth - (RADIUS_DOT / 2), yPos, RADIUS_DOT, mPaint);
        mPaint.reset();
    }

    private void drawAlphabet(Canvas canvas, float textSize, int i, float canvasHalfWidth, float yPos) {
        final String CONSONANT = mIndexer[i];

        mPaint.setColor(Color.GRAY);
        mPaint.setTextSize(textSize);
        mPaint.setAntiAlias(true);
        if (i == mChoose) {
            mPaint.setColor(ContextCompat.getColor(getContext(), android.R.color.holo_blue_light));
            mPaint.setFakeBoldText(true);
        }

        canvas.drawText(CONSONANT, canvasHalfWidth - (mPaint.measureText(CONSONANT) / 2), yPos, mPaint);
        mPaint.reset();
    }

    /**
     * 텍스트가 겹쳐 Draw 될 경우, 스킵할 index count를 반환한다.
     * @return
     */
    private int getskipIndexCountIfOverlap(int indexCnt, int textSize) {
        int     viewHeight      = getHeight();
        int     requiredHeight  = indexCnt * textSize;


        if(requiredHeight > viewHeight) {
            //뷰가 겹쳐서 그려진 것으로 판단(필요한 height보다 view height가 작으므로)
            int overHeight = requiredHeight - viewHeight;
            int skipCnt    = overHeight / textSize;

            //필요한 높이보단 더 필요하지만, 눈으로 볼땐 안겹치는 경우도 존재한다.(필요한 추가 높이가 textSize보다 작은 경우)
            if(skipCnt <= 0)
                skipCnt = 1;

            if((skipCnt % 2) != 0) //짝수개 스킵되도록(특별한 이윤 없음)
                skipCnt += 2;

            return skipCnt;
        }

        return 0;
    }

    private boolean isShowDotInsteadAlphabet(int indexLength, int alphabetPos, int skipCnt) {
        if(skipCnt <= 0)
            return false;

        int skipGap = indexLength / skipCnt;
        int skipSeq = (alphabetPos / skipGap) + 1;

        if((alphabetPos + 1) == (skipGap * skipSeq))
            return true;
        return false;
    }

    /********************************************************************************************************************
     * 터치 이벤트 관련 처리
     ********************************************************************************************************************/

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        final int action = event.getAction();
        final float y = event.getY();
        final int oldChoose = mChoose;
        final OnTouchingLetterChangedListener listener = onTouchingLetterChangedListener;
        final int characterIndex = (int) (y / getHeight() * mIndexer.length);

        switch (action) {
            case MotionEvent.ACTION_DOWN:
                mTouchPressed = true;
                if (oldChoose != characterIndex && listener != null) {
                    if (characterIndex >= 0 && characterIndex < mIndexer.length) {
                        listener.onTouchingLetterChanged(mIndexer[characterIndex]);
                        mChoose = characterIndex;
                        invalidate();
                    }
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if (oldChoose != characterIndex && listener != null) {
                    if (characterIndex >= 0 && characterIndex < mIndexer.length) {
                        listener.onTouchingLetterChanged(mIndexer[characterIndex]);
                        mChoose = characterIndex;
                        invalidate();
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
                mTouchPressed = false;
                mChoose = -1;
                invalidate();
                break;
        }
        return true;
    }

    public void setOnTouchingLetterChangedListener(
            OnTouchingLetterChangedListener onTouchingLetterChangedListener) {
        this.onTouchingLetterChangedListener = onTouchingLetterChangedListener;
    }

    public interface OnTouchingLetterChangedListener {
        public void onTouchingLetterChanged(String s);
    }

    public static String getAlpha(String str) {
        if (str == null) {
            return HASH_MARK;
        }

        if (str.trim().length() == 0) {
            return HASH_MARK;
        }

        String firstChar = str.trim().substring(0, 1);
        Pattern pattern = Pattern.compile(PATTERN);
        if (pattern.matcher(firstChar).matches()) {
            return firstChar.toUpperCase();
        } else {
            return HASH_MARK;
        }
    }

    private void showLog(String message) {
        Log.d(getClass().getSimpleName(), "indexerBar : " + message);
    }
}
