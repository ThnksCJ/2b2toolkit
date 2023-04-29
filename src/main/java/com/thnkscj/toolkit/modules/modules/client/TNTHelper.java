package com.thnkscj.toolkit.modules.modules.client;

import com.thnkscj.toolkit.modules.Category;
import com.thnkscj.toolkit.modules.Module;
import com.thnkscj.toolkit.setting.settings.BooleanSetting;

public class TNTHelper extends Module {
    public static BooleanSetting renderTag = new BooleanSetting("Render Tag", "", true);
    public static BooleanSetting tntLitIndicator = new BooleanSetting("TNT Lit Indicator", "", true);

    public TNTHelper() {
        super("TNTHelper", "Random stuff with tnt", Category.CLIENT);

        addSettings(renderTag);
    }
}
