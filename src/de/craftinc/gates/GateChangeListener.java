/*  Craft Inc. Gates
    Copyright (C) 2011-2014 Craft Inc. Gates Team (see AUTHORS.txt)

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU Lesser General Public License as published
    by the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Lesser General Public License for more details.

    You should have received a copy of the GNU Lesser General Public License
    along with this program (LGPLv3).  If not, see <http://www.gnu.org/licenses/>.
*/
package de.craftinc.gates;


import java.util.Map;

public interface GateChangeListener {
    String newGate = "GateChangeListener-newGate"; // value will be null
    String removedGate = "GateChangeListener-removedGate"; // value will be null
    String changedID = "GateChangeListener-changedID"; // value will be the old ID
    String changedLocation = "GateChangeListener-changedLocation"; // value will the old location
    String changedExit = "GateChangeListener-changedExit"; // value will be the old exit

    void gateChangedHandler(final Gate g, final Map<String, Object> changeSet);
}
