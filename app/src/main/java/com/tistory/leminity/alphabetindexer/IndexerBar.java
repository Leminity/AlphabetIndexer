package com.tistory.leminity.alphabetindexer;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by User on 2016-06-21.
 */
public class IndexerBar extends View {

    private static final String PATTERN = "^[A-Za-z]+$";
    private static final String HASH_MARK = "#";

    private static final int TEXT_SIZE = 10;

    private OnIndexBarListener mOnIndexBarListener;

    private String[]                mConsonantArray = {};
    private boolean mIsDefineUnicode = false;
    private List<ConsonantUnicode> mConsonantUnicodeList = null;

    private int mChoose = -1;

    private Paint mPaint = new Paint();

    boolean mTouchPressed = false;

    public IndexerBar(Context context) {
        super(context);
        initConsonants();
    }

    public IndexerBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        initConsonants();
    }

    public IndexerBar(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initConsonants();
    }

    /**
     * Load consonant List
     */
    private void initConsonants() {
        Resources res = getResources();
        mConsonantArray = res.getStringArray(R.array.ary_alphabet_idx);
        mIsDefineUnicode = res.getBoolean(R.bool.defined_unicode);
        if(mIsDefineUnicode)
            initUnicode();
    }

    /**
     * Load consonant Unicode List
     */
    private void initUnicode() {
        Resources res           = getResources();
        TypedArray typedArray   = res.obtainTypedArray(R.array.unicode_array);
        int length              = typedArray.length();
        mConsonantUnicodeList = new ArrayList(length);

        String[][] array = new String[length][];
        for (int i = 0; i < length; ++i) {
            int id = typedArray.getResourceId(i, 0);
            if(id > 0)
                array[i] = res.getStringArray(id);
        }
        typedArray.recycle();

        final int CONSONANT_ID_IDX      = 0; //리소스 파일(ex:jp)을 보면 첫번째는 ID임
        final int CONSONANT_TXT_IDX     = 1; //실제 자음
        final int UNICODE_START_IDX   = 2; //유니코드 시작 IDX
        for(int i = 0; i < array.length; i++) {
            String[] aryValue = array[i];
            String consonant = aryValue[CONSONANT_TXT_IDX];
            char[] unicodeAry = new char[aryValue.length - UNICODE_START_IDX];

            int insertIdx = 0;
            for (int j = UNICODE_START_IDX; j < aryValue.length; j++) {
                unicodeAry[insertIdx++] = aryValue[j].toCharArray()[0];
            }

            mConsonantUnicodeList.add(new ConsonantUnicode(consonant, unicodeAry));
        }
    }

    /********************************************************************************************************************
     * Public api
     ********************************************************************************************************************/
    public void setOnIndexBarListener(OnIndexBarListener onIndexBarListener) {
        this.mOnIndexBarListener = onIndexBarListener;
    }

    public boolean isDefineUnicode() {
        return mIsDefineUnicode;
    }

    /**
     * return consonant list.
     * (depends device language.)
     * @return
     */
    public String[] getConsonants() {
        return mConsonantArray;
    }

    /**
     * return consonant Unicode Info List or null.
     * return null when If not required unicode process.
     * @return
     */
    public List<ConsonantUnicode> getConsonantUnicodeList() {
        return mConsonantUnicodeList;
    }

    /********************************************************************************************************************
     * private api
     ********************************************************************************************************************/

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int     CANVAS_HALF_WIDTH           = getWidth() / 2;
        int     height          = getHeight();
        float   textSize        = getResources().getDisplayMetrics().density * TEXT_SIZE;
        int     singleHeight    = height / mConsonantArray.length;
        int     skipIdxCnt      = getskipIndexCountIfOverlap(mConsonantArray.length, (int)textSize);

        setBackgroundResource(mTouchPressed ? R.drawable.shape_round_rectangle_pressed : R.drawable.shape_round_rectangle_normal);

        for (int i = 0; i < mConsonantArray.length; i++) {
            float yPos = (singleHeight * i) + singleHeight;

            if(isShowDotInsteadAlphabet(mConsonantArray.length, i, skipIdxCnt)) {
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
            mPaint.setColor(ContextCompat.getColor(getContext(), R.color.colorIndexerRoundPressed));
        }

        canvas.drawCircle(canvasHalfWidth - (RADIUS_DOT / 2), yPos, RADIUS_DOT, mPaint);
        mPaint.reset();
    }

    private void drawAlphabet(Canvas canvas, float textSize, int i, float canvasHalfWidth, float yPos) {
        final String CONSONANT = mConsonantArray[i];

        mPaint.setColor(Color.GRAY);
        mPaint.setTextSize(textSize);
        mPaint.setAntiAlias(true);
        if (i == mChoose) {
            mPaint.setColor(ContextCompat.getColor(getContext(), R.color.colorIndexerRoundPressed));
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
        final OnIndexBarListener listener = mOnIndexBarListener;
        final int characterIndex = (int) (y / getHeight() * mConsonantArray.length);

        switch (action) {
            case MotionEvent.ACTION_DOWN:
                mTouchPressed = true;
                if (oldChoose != characterIndex && listener != null) {
                    if (characterIndex >= 0 && characterIndex < mConsonantArray.length) {
                        listener.onTouchingConsonantChanged(mConsonantArray[characterIndex]);
                        mChoose = characterIndex;
                        invalidate();
                    }
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if (oldChoose != characterIndex && listener != null) {
                    if (characterIndex >= 0 && characterIndex < mConsonantArray.length) {
                        listener.onTouchingConsonantChanged(mConsonantArray[characterIndex]);
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

    private void showLog(String message) {
        Log.d(getClass().getSimpleName(), "indexerBar : " + message);
    }

    public interface OnIndexBarListener {
        public void onTouchingConsonantChanged(String s);
    }
}
