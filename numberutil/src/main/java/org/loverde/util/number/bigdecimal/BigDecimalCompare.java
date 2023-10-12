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

import java.math.BigDecimal;


/**
 * The BigDecimal javadoc says {@link BigDecimal#compareTo(BigDecimal) this} about <code>compareTo</code>:
 *
 * <p><em>
 *    This method is provided in preference to individual methods for each of the six boolean comparison
 *    operators.
 * </em></p>
 *
 * Welp, their preference doesn't match mine.  Using the six boolean comparison operators with -1, 0, 1
 * doesn't pass the readability test for me, and so this class was born.
 */
public class BigDecimalCompare {
   public static boolean isLessThan( final BigDecimal isThis, final BigDecimal lessThanThis ) {
      if( isThis == null ) throw new IllegalArgumentException( "First argument is null" );
      if( lessThanThis == null ) throw new IllegalArgumentException( "Second argument is null" );

      return isThis.compareTo( lessThanThis ) < 0;
   }

   public static boolean isLessThanOrEqualTo( final BigDecimal isThis, final BigDecimal lessThanOrEqualToThis ) {
      if( isThis == null ) throw new IllegalArgumentException( "First argument is null" );
      if( lessThanOrEqualToThis == null ) throw new IllegalArgumentException( "Second argument is null" );

      return isThis.compareTo( lessThanOrEqualToThis ) <= 0;
   }

   public static boolean isGreaterThan( final BigDecimal isThis, final BigDecimal greaterThanThis ) {
      if( isThis == null ) throw new IllegalArgumentException( "First argument is null" );
      if( greaterThanThis == null ) throw new IllegalArgumentException( "Second argument is null" );

      return isThis.compareTo( greaterThanThis ) > 0;
   }

   public static boolean isGreaterThanOrEqualTo( final BigDecimal isThis, final BigDecimal greaterThanOrEqualToThis ) {
      if( isThis == null ) throw new IllegalArgumentException( "First argument is null" );
      if( greaterThanOrEqualToThis == null ) throw new IllegalArgumentException( "Second argument is null" );

      return isThis.compareTo( greaterThanOrEqualToThis ) >= 0;
   }

   public static boolean isEqualTo( final BigDecimal isThis, final BigDecimal equalToThis ) {
      if( isThis == null ) throw new IllegalArgumentException( "First argument is null" );
      if( equalToThis == null ) throw new IllegalArgumentException( "Second argument is null" );

      return isThis.compareTo( equalToThis ) == 0;
   }

   public static boolean isWithinInclusiveRange( final BigDecimal bd, final BigDecimal lowBound, final BigDecimal highBound ) {
      if( bd == null ) throw new IllegalArgumentException( "First argument is null" );
      if( lowBound == null ) throw new IllegalArgumentException( "Low bound argument is null" );
      if( highBound == null ) throw new IllegalArgumentException( "High bound argument is null" );
      if( isGreaterThan(lowBound, highBound) ) throw new IllegalArgumentException( "Low bound is greater than high bound" );

      return isGreaterThanOrEqualTo( bd, lowBound ) && isLessThanOrEqualTo( bd, highBound );
   }
}
