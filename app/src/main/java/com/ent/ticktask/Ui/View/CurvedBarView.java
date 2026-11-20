package com.ent.ticktask.Ui.View;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.ent.ticktask.R;

public class CurvedBarView extends View {

    private final Paint barPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Path barPath = new Path();

    private final float inset;
    private final float barHeight;
    private final float bumpHeight;
    private final float bumpHalfWidth;
    private final float corner;

    public CurvedBarView(Context context) {
        this(context, null);
    }

    public CurvedBarView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CurvedBarView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        inset = dp(14);
        barHeight = dp(72);
        bumpHeight = dp(26);
        bumpHalfWidth = dp(72);
        corner = dp(28);

        setLayerType(LAYER_TYPE_SOFTWARE, null);
        barPaint.setStyle(Paint.Style.FILL);
        barPaint.setColor(ContextCompat.getColor(context, R.color.bottom_bar_bg));
        barPaint.setShadowLayer(dp(10), 0, dp(3), 0x26000000);
    }

    private float dp(float value) {
        return value * getResources().getDisplayMetrics().density;
    }


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        float left = inset;
        float right = w - inset;
        float bottom = h - inset;

        // ارتفاع مستقل بدنه کناری
        float sideTop = bottom - dp(90);
        float cx = w / 2f;
        float d = corner * 2;

        barPath.reset();

        barPath.moveTo(left + corner, sideTop);
        barPath.lineTo(cx - bumpHalfWidth, sideTop);

        barPath.cubicTo(
                cx - bumpHalfWidth / 2f, sideTop,
                cx - bumpHalfWidth / 2f, sideTop - bumpHeight,
                cx, sideTop - bumpHeight
        );

        barPath.cubicTo(
                cx + bumpHalfWidth / 2f, sideTop - bumpHeight,
                cx + bumpHalfWidth / 2f, sideTop,
                cx + bumpHalfWidth, sideTop
        );

        barPath.lineTo(right - corner, sideTop);

        barPath.arcTo(
                new RectF(right - d, sideTop, right, sideTop + d),
                270, 90
        );

        barPath.lineTo(right, bottom - corner);

        barPath.arcTo(
                new RectF(right - d, bottom - d, right, bottom),
                0, 90
        );

        barPath.lineTo(left + corner, bottom);

        barPath.arcTo(
                new RectF(left, bottom - d, left + d, bottom),
                90, 90
        );

        barPath.lineTo(left, sideTop + corner);

        barPath.arcTo(
                new RectF(left, sideTop, left + d, sideTop + d),
                180, 90
        );

        barPath.close();
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawPath(barPath, barPaint);
    }
}