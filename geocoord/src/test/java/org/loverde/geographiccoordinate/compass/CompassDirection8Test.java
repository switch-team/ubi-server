/*
 * GeographicCoordinate
 * https://github.com/kloverde/java-GeographicCoordinate
 *
 * Copyright (c) 2013 Kurtis LoVerde
 * All rights reserved
 *
 * Donations:  https://paypal.me/KurtisLoVerde/5
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

package org.loverde.geographiccoordinate.compass;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;
import org.loverde.geographiccoordinate.exception.GeographicCoordinateException;
import org.loverde.util.number.bigdecimal.BigDecimalCompare;

import static org.junit.jupiter.api.Assertions.*;


public class CompassDirection8Test {

//   @Rule
//   public ExpectedException thrown = ExpectedException.none();


    @Test
    public void getMinMidMaxIncreases() {
        for (final CompassDirection8 dir : CompassDirection8.values()) {
            assertTrue(BigDecimalCompare.isGreaterThanOrEqualTo(dir.getMinimum(), BigDecimal.ZERO), String.format("Comparing %s minimum to 0", dir.name()));
            assertTrue(BigDecimalCompare.isLessThan(dir.getMiddle(), dir.getMaximum()), String.format("Comparing %s middle to max", dir.name()));
            assertTrue(BigDecimalCompare.isLessThanOrEqualTo(dir.getMaximum(), new BigDecimal(360)), String.format("Comparing %s maximum to 360", dir.name()));
            assertTrue(BigDecimalCompare.isLessThan(dir.getMaximum(), dir.getNext().getMinimum()), String.format("Comparing %s maximum to next minimum", dir.name()));

            if (dir != CompassDirection8.NORTH) {
                assertTrue(BigDecimalCompare.isLessThan(dir.getMinimum(), dir.getMiddle()), String.format("Comparing %s minimum to middle", dir.name()));
            } else {
                assertTrue(BigDecimalCompare.isGreaterThan(dir.getMinimum(), dir.getMiddle()), String.format("Comparing %s minimum to middle", dir.name()));
            }
        }
    }

    @Test
    public void getPrevious() {
        assertEquals(CompassDirection8.NORTHEAST, CompassDirection8.EAST.getPrevious());  // verify that getPrevious moves backward by 1
        assertEquals(CompassDirection8.NORTHWEST, CompassDirection8.NORTH.getPrevious());  // verify loop-around
    }

    @Test
    public void getNext() {
        assertEquals(CompassDirection8.WEST, CompassDirection8.SOUTHWEST.getNext());  // verify that getNext moves forward by 1
        assertEquals(CompassDirection8.NORTH, CompassDirection8.NORTHWEST.getNext());  // verify loop-around
    }

    @Test
    public void getByAbbreviation() {
        assertEquals(CompassDirection8.NORTH, CompassDirection8.getByAbbreviation(CompassDirection8.NORTH.getAbbreviation()));
        assertEquals(CompassDirection8.NORTHEAST, CompassDirection8.getByAbbreviation(CompassDirection8.NORTHEAST.getAbbreviation()));
        assertEquals(CompassDirection8.EAST, CompassDirection8.getByAbbreviation(CompassDirection8.EAST.getAbbreviation()));
        assertEquals(CompassDirection8.SOUTHEAST, CompassDirection8.getByAbbreviation(CompassDirection8.SOUTHEAST.getAbbreviation()));
        assertEquals(CompassDirection8.SOUTH, CompassDirection8.getByAbbreviation(CompassDirection8.SOUTH.getAbbreviation()));
        assertEquals(CompassDirection8.SOUTHWEST, CompassDirection8.getByAbbreviation(CompassDirection8.SOUTHWEST.getAbbreviation()));
        assertEquals(CompassDirection8.WEST, CompassDirection8.getByAbbreviation(CompassDirection8.WEST.getAbbreviation()));
        assertEquals(CompassDirection8.NORTHWEST, CompassDirection8.getByAbbreviation(CompassDirection8.NORTHWEST.getAbbreviation()));
    }

    @Test
    public void getByBearing_minMax() {
        assertEquals(CompassDirection8.NORTH, CompassDirection8.getByBearing(CompassDirection8.NORTH.getMinimum()));
        assertEquals(CompassDirection8.NORTH, CompassDirection8.getByBearing(CompassDirection8.NORTH.getMaximum()));

        assertEquals(CompassDirection8.NORTHEAST, CompassDirection8.getByBearing(CompassDirection8.NORTHEAST.getMinimum()));
        assertEquals(CompassDirection8.NORTHEAST, CompassDirection8.getByBearing(CompassDirection8.NORTHEAST.getMaximum()));

        assertEquals(CompassDirection8.EAST, CompassDirection8.getByBearing(CompassDirection8.EAST.getMinimum()));
        assertEquals(CompassDirection8.EAST, CompassDirection8.getByBearing(CompassDirection8.EAST.getMaximum()));

        assertEquals(CompassDirection8.SOUTHEAST, CompassDirection8.getByBearing(CompassDirection8.SOUTHEAST.getMinimum()));
        assertEquals(CompassDirection8.SOUTHEAST, CompassDirection8.getByBearing(CompassDirection8.SOUTHEAST.getMaximum()));

        assertEquals(CompassDirection8.SOUTH, CompassDirection8.getByBearing(CompassDirection8.SOUTH.getMinimum()));
        assertEquals(CompassDirection8.SOUTH, CompassDirection8.getByBearing(CompassDirection8.SOUTH.getMaximum()));

        assertEquals(CompassDirection8.SOUTHWEST, CompassDirection8.getByBearing(CompassDirection8.SOUTHWEST.getMinimum()));
        assertEquals(CompassDirection8.SOUTHWEST, CompassDirection8.getByBearing(CompassDirection8.SOUTHWEST.getMaximum()));

        assertEquals(CompassDirection8.WEST, CompassDirection8.getByBearing(CompassDirection8.WEST.getMinimum()));
        assertEquals(CompassDirection8.WEST, CompassDirection8.getByBearing(CompassDirection8.WEST.getMaximum()));

        assertEquals(CompassDirection8.NORTHWEST, CompassDirection8.getByBearing(CompassDirection8.NORTHWEST.getMinimum()));
        assertEquals(CompassDirection8.NORTHWEST, CompassDirection8.getByBearing(CompassDirection8.NORTHWEST.getMaximum()));
    }

    @Test
    public void getByBearing_testRounding() {
        assertEquals(CompassDirection8.NORTHWEST, CompassDirection8.getByBearing(new BigDecimal("337.494999999999999999")));
        assertEquals(CompassDirection8.NORTH, CompassDirection8.getByBearing(new BigDecimal("337.495")));
    }

    @Test
    public void getByBearing_north() {
        assertEquals(CompassDirection8.NORTH, CompassDirection8.getByBearing(BigDecimal.ZERO));
        assertEquals(CompassDirection8.NORTH, CompassDirection8.getByBearing(new BigDecimal("359.9")));
        assertEquals(CompassDirection8.NORTH, CompassDirection8.getByBearing(new BigDecimal(360)));
    }

    @Test
    public void getByBearing_invalidMin() {
        assertThrows(GeographicCoordinateException.class, () -> CompassDirection8.getByBearing(new BigDecimal("-0.000000000001")), "Bearing -0.000000000001 is not in range [0, 360]");
    }

    @Test
    public void getByBearing_invalidMax() {
        assertThrows(GeographicCoordinateException.class, () -> CompassDirection8.getByBearing(new BigDecimal("360.000000000001")), "Bearing 360.000000000001 is not in range [0, 360]");
        ;
    }

    @Test
    public void getPrintName() {
        assertEquals("east", CompassDirection8.EAST.getPrintName());
        assertEquals("north", CompassDirection8.NORTH.getPrintName());
        assertEquals("northeast", CompassDirection8.NORTHEAST.getPrintName());
        assertEquals("northwest", CompassDirection8.NORTHWEST.getPrintName());
        assertEquals("south", CompassDirection8.SOUTH.getPrintName());
        assertEquals("southeast", CompassDirection8.SOUTHEAST.getPrintName());
        assertEquals("southwest", CompassDirection8.SOUTHWEST.getPrintName());
        assertEquals("west", CompassDirection8.WEST.getPrintName());
    }
}
