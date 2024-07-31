package com.evan.dino;

import android.content.Context;
import android.util.AttributeSet;

import androidx.transition.ChangeBounds;
import androidx.transition.ChangeTransform;
import androidx.transition.TransitionSet;

/**
 * Created by Evan on 2022/6/8.
 * <p>
 * Description：
 */
public class _MyTransition extends TransitionSet {

    public _MyTransition() {
        init();
    }

    public _MyTransition(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        setOrdering(ORDERING_SEQUENTIAL);

        // 位置移動動畫
        ChangeBounds changeBounds = new ChangeBounds();
        changeBounds.setDuration(5000);

        // 縮放旋轉動畫
        ChangeTransform transform = new ChangeTransform();
        transform.setDuration(5000);

        // 兩個動畫放到一個set裡面，並行執行
        TransitionSet set = new TransitionSet();
        set.setOrdering(TransitionSet.ORDERING_TOGETHER);
        set.addTransition(changeBounds);
        set.addTransition(transform);
        addTransition(set);

    }
}