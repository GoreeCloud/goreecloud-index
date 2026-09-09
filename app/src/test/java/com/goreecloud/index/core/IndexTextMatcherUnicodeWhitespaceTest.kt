package com.goreecloud.index.core

import org.junit.Assert.assertEquals
import org.junit.Test

class IndexTextMatcherUnicodeWhitespaceTest {
    @Test
    fun unicodeSeparatorsPreserveWordPrefixRanking() {
        assertEquals(
            760,
            IndexTextMatcher.score(
                query = "sett",
                title = "Privacy\u00A0Settings",
            ),
        )
        assertEquals(
            760,
            IndexTextMatcher.score(
                query = "cal",
                title = "Work\u2003Calendar",
            ),
        )
    }
}
