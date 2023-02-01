package com.cj.toolkit.modules.modules;

import com.cj.toolkit.modules.Category;
import com.cj.toolkit.modules.Module;
import com.cj.toolkit.setting.settings.BooleanSetting;

public class TNTHelper extends Module {
    public TNTHelper() {
        super("TNTHelper", "Random stuff with tnt", Category.CLIENT);

        addSettings(renderTag);
    }

    public static BooleanSetting renderTag = new BooleanSetting("Render Tag", "", true);
    public static BooleanSetting tntLitIndicator = new BooleanSetting("TNT Lit Indicator", "", true);
}
