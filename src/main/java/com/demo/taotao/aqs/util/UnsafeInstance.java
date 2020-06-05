package com.demo.taotao.aqs.util;

//下面是sun.misc.Unsafe.java类源码
import com.demo.taotao.aqs.current.MyLock;
import org.apache.tomcat.jni.Error;
import sun.misc.Unsafe;

import java.lang.reflect.Field;
/***
 * This class should provide access to low-level operations and its
 * use should be limited to trusted code.  Fields can be accessed using
 * memory addresses, with undefined behaviour occurring if invalid memory
 * addresses are given.
 * 这个类提供了一个更底层的操作并且应该在受信任的代码中使用。可以通过内存地址
 * 存取fields,如果给出的内存地址是无效的那么会有一个不确定的运行表现。
 *
 * @author Tom Tromey (tromey@redhat.com)
 * @author Andrew John Hughes (gnu_andrew@member.fsf.org)
 */
public class UnsafeInstance
{

    public static Unsafe reflectGetUnsafe(){
        try{
            Field field = Unsafe.class.getDeclaredField("theUnsafe");
            field.setAccessible(true);
            return (Unsafe)field.get(null);
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }


}
