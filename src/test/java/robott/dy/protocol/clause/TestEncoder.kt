package robott.dy.protocol.clause

import junit.framework.TestCase
import org.junit.Test

/**
 * Created by linpeng on 2016/4/1.
 */
class TestEncoder : TestCase() {

    @Test
    fun testIt() {
        val map = mapOf(Pair("name", "lili"), Pair("age", 11));
        val byte1 = DouyuEncoderObject.asBytes(map)

        assertNotNull(byte1)
    }

}