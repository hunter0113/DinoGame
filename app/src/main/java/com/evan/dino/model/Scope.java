package com.evan.dino;

/**
 * Created by Evanwei on 2022/5/4.
 * <p>
 * Description：
 */
public class Scope {
   private final Point bottomLeftPoint;
   private final Point topRightPoint;

   public Scope(Point bottomLeftPoint, Point topRightPoint){
       this.bottomLeftPoint = bottomLeftPoint;
       this.topRightPoint = topRightPoint;
   }

   public final Point getBLPoint(){
       return bottomLeftPoint;
   }

    public final Point getTRPoint(){
        return topRightPoint;
    }
}
