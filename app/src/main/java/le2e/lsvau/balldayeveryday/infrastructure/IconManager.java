package le2e.lsvau.balldayeveryday.infrastructure;

import java.util.HashMap;
import java.util.Map;

import le2e.lsvau.balldayeveryday.R;

public class IconManager
{
    private static Map<String, Integer> imageMap;

    private static void setItemListIcon()
    {
        imageMap = new HashMap<String, Integer>();
        imageMap.put("clear-day", R.mipmap.ic_ball_clear);
        imageMap.put("clear-night", R.mipmap.ic_clear_night);
        imageMap.put("cloudy", R.mipmap.ic_cloudy);
        imageMap.put("rain", R.mipmap.ic_rain);
        imageMap.put("partly-cloudy-day", R.mipmap.ic_partly_cloudy);
        imageMap.put("partly-cloudy-night", R.mipmap.ic_partly_cloudy_night);
        imageMap.put("wind", R.mipmap.ic_wind);
        imageMap.put("default", R.mipmap.ic_launcher);
    }

    public static int getIconReference(String iconString)
    {
        setItemListIcon();
        if(imageMap.get(iconString) == null)
            return imageMap.get("default");
        else
            return imageMap.get(iconString);
    }
}
