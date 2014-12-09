// Import List - http://my-flow.github.io/importlist/
// Copyright (C) 2011-2014 Florian J. Breunig
//
// This program is free software: you can redistribute it and/or modify
// it under the terms of the GNU General Public License as published by
// the Free Software Foundation, either version 3 of the License, or
// (at your option) any later version.
//
// This program is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
// GNU General Public License for more details.
//
// You should have received a copy of the GNU General Public License
// along with this program. If not, see <http://www.gnu.org/licenses/>.

package com.moneydance.modules.features.importlist.io;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

import org.junit.Test;

/**
 * @author Florian J. Breunig
 */
public final class DefaultDirectoryChooserTest {

    @Test
    public void testGetBaseDirectory() {
        AbstractDirectoryChooser directoryChooser = new DefaultDirectoryChooser(
                ".");
        assertThat(directoryChooser.getBaseDirectory(), notNullValue());
    }

    @Test
    public void testChooseBaseDirectory() {
        AbstractDirectoryChooser directoryChooser = new DefaultDirectoryChooser(
                null);
        directoryChooser.chooseBaseDirectory();
    }
}