package wandfix.test;

import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.util.Log;

import com.miqt.demo.clazz.ITextFixBean;
import com.miqt.demo.clazz.TextFixBean;
import com.miqt.wand.ObjectFactory;
import com.miqt.wand.Wand;
import com.miqt.wand.anno.ParentalEntrustmentLevel;
import com.miqt.wand.utils.FileUtils;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.File;

import static com.miqt.demo.clazz.TextFixBean.public_final_static_String;


/**
 * Created by t54 on 2018/6/21.
 */
@RunWith(AndroidJUnit4.class)
public class TestHotFix {
    @Test
    public void test() throws Exception {
        Wand.getInstance(InstrumentationRegistry.getTargetContext());
        // Wand.get().attachPackUrl("https://github.com/miqt/WandFix/raw/master/hotfix_pack.dex");

        File file = FileUtils.downloadFile("https://github.com/miqt/WandFix/raw/master/hotfix_pack.dex"
                , InstrumentationRegistry.getTargetContext().getCacheDir().getAbsolutePath() + "/dex/", null);
        Wand.get().attachPack(file);

        //热修复包中的class
        ITextFixBean fix1Bean = ObjectFactory.make(TextFixBean.class, ParentalEntrustmentLevel.PROJECT);
        ITextFixBean fix2Bean = ObjectFactory.make(TextFixBean.class, ParentalEntrustmentLevel.NEVER);
        //apk中的class
        ITextFixBean apk1Bean = ObjectFactory.make(TextFixBean.class, ParentalEntrustmentLevel.PROJECT);
        //apk中的class
        ITextFixBean apk2Bean = new TextFixBean();

        Log.d("com.wandfix.test.TestHotFix", "fix1Bean class:" + fix1Bean.getClass().hashCode() + "\n" +
                "fix2Bean class:" + fix2Bean.getClass().hashCode() + "\n" +
                "apk1Bean class:" + apk1Bean.getClass().hashCode() + "\n" +
                "apk2Bean class:" + apk2Bean.getClass().hashCode() + "\n"
        );
        Log.d("com.wandfix.test.TestHotFix", "fix1Bean class:" + fix1Bean.getStaticString() + "\n" +
                "fix2Bean class:" + fix2Bean.getStaticString() + "\n" +
                "apk1Bean class:" + apk1Bean.getStaticString() + "\n" +
                "apk2Bean class:" + apk2Bean.getStaticString() + "\n"
        );

        fix1Bean.setStaticString("hello");
        Assert.assertEquals(fix1Bean.getStaticString(), apk1Bean.getStaticString());
        Assert.assertEquals(apk1Bean.getStaticString(), apk2Bean.getStaticString());

        Assert.assertNotEquals(fix1Bean.getStaticString(), fix2Bean.getStaticString());

        //测试静态内部类静态方法
        String result = ObjectFactory.invokeStaticMethod(TextFixBean.StaticInnerC.class, "getString", "123");
        Assert.assertEquals("123", result);

        //测试内部类
        ITextFixBean bean = ObjectFactory.make(TextFixBean.class);
        Object innerC = ObjectFactory.make(TextFixBean.InnerC.class, bean);
        Assert.assertEquals("123", ObjectFactory.invokeMethod(innerC,
                TextFixBean.InnerC.class.getName()
                , "getString", null, "123"));
    }

    @Test
    public void test2() throws Exception {

        Assert.assertNotNull(public_final_static_String);

    }


}
