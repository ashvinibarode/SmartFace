package com.sage.hitesh.sageattandance;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import com.google.mlkit.vision.face.Face;

import java.util.ArrayList;
import java.util.List;

public class FaceOverlayView extends View {
    private List<Face> faces = new ArrayList<>();
    private Paint paint;

    public FaceOverlayView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public FaceOverlayView(Context context) {
        super(context);
        init();
    }

    private void init() {
        paint = new Paint();
        paint.setColor(Color.GREEN);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(8f);
    }

    public void setFaces(List<Face> faces) {
        this.faces = faces;
        invalidate(); // Trigger a redraw
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        for (Face face : faces) {
            RectF rect = new RectF(face.getBoundingBox());
            canvas.drawRect(rect, paint);
        }
    }
}
