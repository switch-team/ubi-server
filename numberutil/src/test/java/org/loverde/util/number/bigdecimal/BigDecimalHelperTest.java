/*
 * NumberUtil
 * https://github.com/kloverde/java-NumberUtil
 *
 * Copyright (c) 2017 Kurtis LoVerde
 * All rights reserved
 *
 * Donations:  https://paypal.me/KurtisLoVerde/1
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 *     1. Redistributions of source code must retain the above copyright
 *        notice, this list of conditions and the following disclaimer.
 *     2. Redistributions in binary form must reproduce the above copyright
 *        notice, this list of conditions and the following disclaimer in the
 *        documentation and/or other materials provided with the distribution.
 *     3. Neither the name of the copyright holder nor the names of its
 *        contributors may be used to endorse or promote products derived from
 *        this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package org.loverde.util.number.bigdecimal;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

public class BigDecimalHelperTest {
   private static final BigDecimal NEG_1_10 = new BigDecimal( "-1.10" ),
                                   NEG_1_11 = new BigDecimal( "-1.11" ),
                                   POS_1_10 = new BigDecimal( "1.10" ),
                                   POS_1_11 = new BigDecimal( "1.11" );

   @Test
   public void isLessThan_success() {
      // pos-pos
      assertTrue( BigDecimalCompare.isLessThan(POS_1_10, POS_1_11) );
      assertFalse( BigDecimalCompare.isLessThan(POS_1_11, POS_1_10) );

      // pos-neg
      assertFalse( BigDecimalCompare.isLessThan(POS_1_10, NEG_1_10) );

      // neg-neg
      assertTrue( BigDecimalCompare.isLessThan(NEG_1_11, NEG_1_10) );
      assertFalse( BigDecimalCompare.isLessThan(NEG_1_10, NEG_1_11) );

      // identical pos
      assertFalse( BigDecimalCompare.isLessThan(new BigDecimal("1.1"), new BigDecimal("1.1")) );

      // identical neg
      assertFalse( BigDecimalCompare.isLessThan(new BigDecimal("-1.1"), new BigDecimal("-1.1")) );
   }

   @Test
   public void isLessThanOrEqualTo_success() {
      // pos-pos
      assertTrue( BigDecimalCompare.isLessThanOrEqualTo( POS_1_10, POS_1_11) );
      assertFalse( BigDecimalCompare.isLessThanOrEqualTo(POS_1_11, POS_1_10) );

      // pos-neg
      assertFalse( BigDecimalCompare.isLessThanOrEqualTo(POS_1_10, NEG_1_10) );

      // neg-neg
      assertTrue( BigDecimalCompare.isLessThanOrEqualTo(NEG_1_11, NEG_1_10) );
      assertFalse( BigDecimalCompare.isLessThanOrEqualTo(NEG_1_10, NEG_1_11) );

      // identical pos
      assertTrue( BigDecimalCompare.isLessThanOrEqualTo(new BigDecimal("1.1"), new BigDecimal("1.1")) );

      // identical neg
      assertTrue( BigDecimalCompare.isLessThanOrEqualTo(new BigDecimal("-1.1"), new BigDecimal("-1.1")) );
   }

   @Test
   public void isGreaterThan_success() {
      // pos-pos
      assertTrue( BigDecimalCompare.isGreaterThan(POS_1_11, POS_1_10) );
      assertFalse( BigDecimalCompare.isGreaterThan(POS_1_10, POS_1_11) );

      // pos-neg
      assertTrue( BigDecimalCompare.isGreaterThan(POS_1_10, NEG_1_10) );
      assertFalse( BigDecimalCompare.isGreaterThan(NEG_1_10, POS_1_10) );

      // neg-neg
      assertTrue( BigDecimalCompare.isGreaterThan(NEG_1_10, NEG_1_11) );
      assertFalse( BigDecimalCompare.isGreaterThan(NEG_1_11, NEG_1_10) );

      // identical pos
      assertFalse( BigDecimalCompare.isGreaterThan(new BigDecimal("1.1"), new BigDecimal("1.1")) );

      // identical neg
      assertFalse( BigDecimalCompare.isGreaterThan(new BigDecimal("-1.1"), new BigDecimal("-1.1")) );
   }

   @Test
   public void isGreaterThanOrEqualTo_success() {
      // pos-pos
      assertTrue( BigDecimalCompare.isGreaterThanOrEqualTo(POS_1_11, POS_1_10) );
      assertFalse( BigDecimalCompare.isGreaterThan(POS_1_10, POS_1_11) );

      // pos-neg
      assertTrue( BigDecimalCompare.isGreaterThanOrEqualTo(POS_1_10, NEG_1_10) );
      assertFalse( BigDecimalCompare.isGreaterThanOrEqualTo(NEG_1_10, POS_1_10) );

      // neg-neg
      assertTrue( BigDecimalCompare.isGreaterThanOrEqualTo(NEG_1_10, NEG_1_11) );
      assertFalse( BigDecimalCompare.isGreaterThanOrEqualTo(NEG_1_11, NEG_1_10) );

      // identical pos
      assertTrue( BigDecimalCompare.isGreaterThanOrEqualTo(new BigDecimal("1.1"), new BigDecimal("1.1")) );

      // identical neg
      assertTrue( BigDecimalCompare.isGreaterThanOrEqualTo(new BigDecimal("-1.1"), new BigDecimal("-1.1")) );
   }

   @Test
   public void isEqualTo_success() {
      assertTrue( BigDecimalCompare.isEqualTo(new BigDecimal("1.1"), new BigDecimal("1.1")) );
      assertTrue( BigDecimalCompare.isEqualTo(new BigDecimal("-1.1"), new BigDecimal("-1.1")) );
   }

   @Test
   public void isWithinInclusiveRange_success() {
      assertTrue( BigDecimalCompare.isWithinInclusiveRange(new BigDecimal("1"), new BigDecimal("1"), new BigDecimal("1")) );
      assertTrue( BigDecimalCompare.isWithinInclusiveRange(new BigDecimal("4.3"), new BigDecimal("4"), new BigDecimal("4.3000001")) );
      assertTrue( BigDecimalCompare.isWithinInclusiveRange(new BigDecimal("-4.3"), new BigDecimal("-5"), new BigDecimal("-4")) );
   }

   @Test
   public void isWithinInclusiveRange_fail_invalidArgs() {
      assertThrows(IllegalArgumentException.class, () -> BigDecimalCompare.isWithinInclusiveRange( POS_1_10, new BigDecimal("2"), new BigDecimal("1") ), "Low bound is greater than high bound");
   }
}
