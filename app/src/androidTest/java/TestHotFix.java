import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;


import com.miqt.demo.ITextFixBean;
import com.miqt.demo.TextFixBean;
import com.miqt.wand.ObjectFactory;
import com.miqt.wand.Wand;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;


/**
 * Created by t54 on 2018/6/21.
 */
@RunWith(AndroidJUnit4.class)
public class TestHotFix {
    @Test
    public void test() throws Exception {
        Wand.get().init(InstrumentationRegistry.getTargetContext());
        Wand.get().attachPackUrl("https://github.com/miqt/WandFix/raw/master/hotfix_pack.dex");

        ITextFixBean iTextFixBean = ObjectFactory.make(TextFixBean.class);

        ((TextFixBean) iTextFixBean).public_static_int = 100;

        Assert.assertEquals(100, (ObjectFactory.make(TextFixBean.class)).public_static_int);
        Assert.assertEquals(0, TextFixBean.public_static_int);
    }
}
